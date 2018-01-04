package org.eientei.codemine.launcher.service;

public class DownloadItem {
    private String url;
    private Integer size;
    private String destination;
    private String name;
    private boolean natives;

    public DownloadItem(String url, Integer size, String destination) {
        this(url, size, destination, destination, false);
    }

    public DownloadItem(String url, Integer size, String destination, boolean natives) {
        this(url, size, destination, destination, natives);
    }

    public DownloadItem(String url, Integer size, String destination, String name, boolean natives) {
        this.url = url;
        this.size = size;
        this.destination = destination;
        this.name = name;
        this.natives = natives;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNatives() {
        return natives;
    }

    public void setNatives(boolean natives) {
        this.natives = natives;
    }
}
