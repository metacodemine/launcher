package org.eientei.codemine.launcher.data;

import java.util.Map;

public class DownloadsDesc {
    private Map<String, ArtifactDesc> classifiers;
    private ArtifactDesc artifact;

    public Map<String, ArtifactDesc> getClassifiers() {
        return classifiers;
    }

    public void setClassifiers(Map<String, ArtifactDesc> classifiers) {
        this.classifiers = classifiers;
    }

    public ArtifactDesc getArtifact() {
        return artifact;
    }

    public void setArtifact(ArtifactDesc artifact) {
        this.artifact = artifact;
    }
}
