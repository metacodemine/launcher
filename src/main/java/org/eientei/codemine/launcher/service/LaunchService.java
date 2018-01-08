package org.eientei.codemine.launcher.service;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.eientei.codemine.launcher.Wrapper;
import org.eientei.codemine.launcher.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class LaunchService {
    @Value("${minecraft.manifest}")
    private String manifestUrl;

    @Value("${minecraft.versions}")
    private String versionsUrl;

    @Value("${minecraft.resources}")
    private String resourcesUrl;

    @Value("${minecraft.central}")
    private String centralUrl;

    @Value("${minecraft.users}")
    private String usersUrl;

    @Value("${minecraft.domain}")
    private String domain;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigService configService;

    private String osname = isWindows() ? "windows" : (isMac() ? "osx" : "linux");

    RestTemplate fetcher = new RestTemplate(Collections.singletonList(new ByteArrayHttpMessageConverter()));

    public void launch(Progressor progressor) {
        try {
            VersionManifest versionManifest = downloadAll(progressor);
            progressor.updateProgress(1.0);
            progressor.updateText("Done");
            if (versionManifest == null) {
                throw new Exception("null version manifest");
            }
            execute(versionManifest, progressor);
        } catch (Exception e) {
            e.printStackTrace();
            progressor.error(e.getMessage());
        }
    }

    private void execute(VersionManifest versionManifest, Progressor progressor) throws IOException, URISyntaxException {
        Map<String, String> props = new HashMap<>();
        props.put("auth_player_name", configService.getUserName());
        props.put("version_name", versionManifest.getId());
        props.put("version_type", versionManifest.getType());
        props.put("game_directory", configService.getDataDir());
        props.put("assets_root", configService.getDataDir() + File.separator + "assets");
        props.put("assets_index_name", versionManifest.getAssets());
        props.put("auth_uuid", configService.getToken());
        props.put("auth_access_token", configService.getToken());
        props.put("user_properties", "{}");
        props.put("user_type", "mojang");

        File launcherFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        if (!launcherFile.isDirectory()) {
            Files.copy(launcherFile.toPath(), new File(configService.getDataDir() + File.separator + "libraries" + File.separator + "launcher.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        StringBuilder libs = new StringBuilder();
        Files.find(new File(configService.getDataDir() + File.separator + "libraries").toPath(), Integer.MAX_VALUE, (path, attr) -> path.toString().toLowerCase().endsWith(".jar")).forEach(p -> {
            libs.append(p.toAbsolutePath()).append(File.pathSeparator);
        });

        libs.append(configService.getDataDir()).append(File.separator).append("client.jar");
        List<String> args = new ArrayList<>();
        args.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
        args.add("-d64");
        args.add("-Xmx" + configService.getMemory());
        args.add("-Djava.library.path=" + configService.getDataDir() + File.separator + "natives");
        //args.add(versionManifest.getLogging().get("client").getArgument());
        args.add("-DauthToken=" + configService.getToken());
        args.add("-cp");
        args.add(libs.toString());
        args.add(Wrapper.class.getName());
        args.add(versionManifest.getMainClass());
        args.add(usersUrl + "/session?value=");
        args.add(domain);

        String[] split = versionManifest.getMinecraftArguments().split("\\s+");
        for (String s : split) {
            if (s.startsWith("${") && s.endsWith("}")) {
                args.add(props.get(s.substring(2, s.length()-1)));
            } else {
                args.add(s);
            }
        }

        progressor.switchToLog();

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(new File(configService.getDataDir()));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        InputStreamReader isr = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            progressor.appendLog(line);
        }
    }

    private void fetch(Progressor progressor, DownloadItem item) throws IOException, ZipException {
        File file = new File(configService.getDataDir() + File.separator + item.getDestination());
        if (file.exists() && (file.length() == item.getSize() || (item.getSize() < 0 && file.length() > 0))) {
            return;
        }
        progressor.updateText("Downloading " + item.getName() + " ...");
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                System.out.println(file.getParentFile().getAbsolutePath());
                System.exit(1);
            }
        }
        try {
            FileCopyUtils.copy(fetcher.exchange(item.getUrl(), HttpMethod.GET, null, byte[].class).getBody(), file);
        } catch (HttpClientErrorException e) {
            return;
        }
        progressor.updateText("Downloading " + item.getName() + " complete");
        if (item.isNatives()) {
            progressor.updateText("Unpacking native libraries " + item.getName());
            new ZipFile(file).extractAll(configService.getDataDir() + File.separator + "natives");
            progressor.updateText("Unpacking native libraries " + item.getName() + " complete");
        }
    }

    private VersionManifest downloadAll(Progressor progressor) throws IOException, ZipException {
        progressor.updateProgress(0.0);
        progressor.updateText("Downloading manifest");
        CodemineManifest codemineManifest = restTemplate.getForObject(manifestUrl, CodemineManifest.class);
        progressor.updateText("Downloading version manifest");
        VersionManifest versionManifest = restTemplate.getForObject(codemineManifest.getUpstream(), VersionManifest.class);
        ArtifactDesc clientArtifact = versionManifest.getDownloads().get("client");
        if (clientArtifact == null) {
            progressor.error("cannot get client artifact");
            return null;
        }

        List<DownloadItem> items = new ArrayList<>();
        items.add(new DownloadItem(clientArtifact.getUrl(), clientArtifact.getSize(), "client.jar"));
        ArtifactDesc logging = versionManifest.getLogging().get("client").getFile();
        items.add(new DownloadItem(logging.getUrl(), logging.getSize(), "logging.xml"));
        extractLibraries(versionManifest, items);
        AssetsDesc assets = restTemplate.getForObject(versionManifest.getAssetIndex().getUrl(), AssetsDesc.class);
        items.add(new DownloadItem(versionManifest.getAssetIndex().getUrl(), versionManifest.getAssetIndex().getSize(), "assets" + File.separator + "indexes" + File.separator + versionManifest.getAssetIndex().getId() + ".json"));
        extractAssets(assets, items);
        ForgeManifest forgeManifest = restTemplate.getForObject(codemineManifest.getForge(), ForgeManifest.class);
        enrichForge(forgeManifest, versionManifest, items);
        extractMods(codemineManifest, items);
        double total = items.size();
        double current = 0;
        for (DownloadItem item : items) {
            fetch(progressor, item);
            current++;
            progressor.updateProgress(current / total);
        }
        return versionManifest;
    }

    private void extractMods(CodemineManifest codemineManifest, List<DownloadItem> items) {
        codemineManifest.getMods().forEach(artifact -> items.add(new DownloadItem(artifact.getUrl(), artifact.getSize(), artifact.getPath().replace('/', File.separatorChar))));
    }

    private void enrichForge(ForgeManifest forgeManifest, VersionManifest versionManifest, List<DownloadItem> items) {
        versionManifest.setId(forgeManifest.getId());
        versionManifest.setMinecraftArguments(forgeManifest.getMinecraftArguments());
        versionManifest.setMainClass(forgeManifest.getMainClass());
        for (MavenlibDesc mavenlibDesc : forgeManifest.getLibraries()) {
            String url = centralUrl;
            if (mavenlibDesc.getUrl() != null) {
                url = mavenlibDesc.getUrl();
            }
            String[] split = mavenlibDesc.getName().split(":");

            String path = split[0].replace('.', '/');
            path += "/";
            path += split[1];
            path += "/";
            path += split[2];
            path += "/";
            path += split[1] + "-" + split[2];
            if (split[1].equals("forge")) {
                path += "-universal";
            }
            path += ".jar";
            items.add(new DownloadItem(url + "/" + path, -1, "libraries" + File.separator + path.replace('/', File.separatorChar)));
            items.add(new DownloadItem("https://repo.maven.apache.org/maven2/" + path, -1, "libraries" + File.separator + path.replace('/', File.separatorChar)));
        }
    }

    private void extractAssets(AssetsDesc assets, List<DownloadItem> items) {
        assets.getObjects().forEach((k, v) -> {
            items.add(new DownloadItem(resourcesUrl + "/" + v.getHash().substring(0, 2) + "/" + v.getHash(), v.getSize(), "assets" + File.separator + "objects" + File.separator + v.getHash().substring(0, 2) + File.separator + v.getHash(), k, false));
        });
    }

    private void extractLibraries(VersionManifest versionManifest, List<DownloadItem> items) {
        versionManifest.getLibraries().forEach(l -> {
            ArtifactDesc artifact = l.getDownloads().getArtifact();
            if (artifact != null) {
                if (artifact.getPath().toLowerCase().contains("guava")) {
                    return;
                }
                items.add(new DownloadItem(artifact.getUrl(), artifact.getSize(), "libraries" + File.separator + artifact.getPath().replace('/', File.separatorChar)));
            }

            Map<String, String> natives = l.getNatives();
            if (natives != null) {
                ArtifactDesc artifactDesc = l.getDownloads().getClassifiers().get(natives.get(osname));
                if (artifactDesc != null) {
                    items.add(new DownloadItem(artifactDesc.getUrl(), artifactDesc.getSize(), "libraries" + File.separator + artifactDesc.getPath().replace('/', File.separatorChar), true));
                }
            }
        });
    }

    public static boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().contains("win"));
    }

    public static boolean isMac() {
        return (System.getProperty("os.name").toLowerCase().contains("mac"));
    }
}
