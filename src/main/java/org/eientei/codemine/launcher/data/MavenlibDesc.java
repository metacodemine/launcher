package org.eientei.codemine.launcher.data;

public class MavenlibDesc {
    private String name;
    private String url;
    private Boolean serverreq;
    private Boolean clientreq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getServerreq() {
        return serverreq;
    }

    public void setServerreq(Boolean serverreq) {
        this.serverreq = serverreq;
    }

    public Boolean getClientreq() {
        return clientreq;
    }

    public void setClientreq(Boolean clientreq) {
        this.clientreq = clientreq;
    }
}
