package org.eientei.codemine.launcher.data;

import java.util.List;

public class ForgeManifest {
    private String id;
    private String time;
    private String releaseTime;
    private String type;
    private String minecraftArguments;
    private String mainClass;
    private String inheritsFrom;
    private String jar;
    private List<MavenlibDesc> libraries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinecraftArguments() {
        return minecraftArguments;
    }

    public void setMinecraftArguments(String minecraftArguments) {
        this.minecraftArguments = minecraftArguments;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public List<MavenlibDesc> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<MavenlibDesc> libraries) {
        this.libraries = libraries;
    }
}
