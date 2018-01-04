package org.eientei.codemine.launcher.data;

import java.util.List;
import java.util.Map;

public class VersionManifest {
    private String minecraftArguments;
    private List<LibraryDesc> libraries;
    private String releaseTime;
    private String mainClass;
    private String time;
    private AssetIndexDesc assetIndex;
    private Integer minimumLauncherVersion;
    private String type;
    private String id;
    private String assets;
    private Map<String, LoggingDesc> logging;
    private Map<String, ArtifactDesc> downloads;

    public String getMinecraftArguments() {
        return minecraftArguments;
    }

    public void setMinecraftArguments(String minecraftArguments) {
        this.minecraftArguments = minecraftArguments;
    }

    public List<LibraryDesc> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<LibraryDesc> libraries) {
        this.libraries = libraries;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public AssetIndexDesc getAssetIndex() {
        return assetIndex;
    }

    public void setAssetIndex(AssetIndexDesc assetIndex) {
        this.assetIndex = assetIndex;
    }

    public Integer getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    public void setMinimumLauncherVersion(Integer minimumLauncherVersion) {
        this.minimumLauncherVersion = minimumLauncherVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public Map<String, LoggingDesc> getLogging() {
        return logging;
    }

    public void setLogging(Map<String, LoggingDesc> logging) {
        this.logging = logging;
    }

    public Map<String, ArtifactDesc> getDownloads() {
        return downloads;
    }

    public void setDownloads(Map<String, ArtifactDesc> downloads) {
        this.downloads = downloads;
    }
}
