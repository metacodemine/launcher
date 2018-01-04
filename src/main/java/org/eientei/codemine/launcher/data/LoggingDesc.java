package org.eientei.codemine.launcher.data;

public class LoggingDesc {
    private String type;
    private ArtifactDesc file;
    private String argument;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArtifactDesc getFile() {
        return file;
    }

    public void setFile(ArtifactDesc file) {
        this.file = file;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}

