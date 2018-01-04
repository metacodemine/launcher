package org.eientei.codemine.launcher.data;

import java.util.Map;

public class LibraryDesc {
    private String name;
    private Map<String,String> natives;
    private ExtractDesc extract;
    private DownloadsDesc downloads;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getNatives() {
        return natives;
    }

    public void setNatives(Map<String, String> natives) {
        this.natives = natives;
    }

    public ExtractDesc getExtract() {
        return extract;
    }

    public void setExtract(ExtractDesc extract) {
        this.extract = extract;
    }

    public DownloadsDesc getDownloads() {
        return downloads;
    }

    public void setDownloads(DownloadsDesc downloads) {
        this.downloads = downloads;
    }
}
