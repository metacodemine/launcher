package org.eientei.codemine.launcher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ConfigService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File configFile = new File(System.getProperty("user.home") + File.separator + ".codemine");

    private Config config = new Config();
    private String userName;

    public static class Config {
        private String token = null;
        private String memory = "2G";
        private String dataDir = new File(System.getProperty("user.home") + File.separator + "codemine").getAbsolutePath();

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getDataDir() {
            return dataDir;
        }

        public void setDataDir(String dataDir) {
            this.dataDir = dataDir;
        }
    }

    public ConfigService() {
        load();
        save();
    }

    public void load() {
        if (configFile.exists()) {
            try {
                config = objectMapper.readValue(configFile, Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getToken() {
        return config.token;
    }

    public void setToken(String token) {
        this.config.token = token;
        save();
    }

    public String getMemory() {
        return this.config.memory;
    }

    public void setMemory(String memory) {
        this.config.memory = memory;
        save();
    }

    public String getDataDir() {
        return this.config.dataDir;
    }

    public void setDataDir(String path) {
        this.config.dataDir = path;
        save();
    }

    public void save() {
        try {
            objectMapper.writeValue(configFile, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
