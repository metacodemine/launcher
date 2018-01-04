package org.eientei.codemine.launcher.data;

import java.util.List;
import java.util.Map;

public class VersionsDesc {
    private Map<String, String> latest;
    private List<VersionDesc> versions;

    public Map<String, String> getLatest() {
        return latest;
    }

    public void setLatest(Map<String, String> latest) {
        this.latest = latest;
    }

    public List<VersionDesc> getVersions() {
        return versions;
    }

    public void setVersions(List<VersionDesc> versions) {
        this.versions = versions;
    }
}
