package org.eientei.codemine.launcher.data;

import java.util.List;

public class CodemineManifest {
    private String name;
    private String upstream;
    private String forge;
    private List<ArtifactDesc> mods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }

    public String getForge() {
        return forge;
    }

    public void setForge(String forge) {
        this.forge = forge;
    }

    public List<ArtifactDesc> getMods() {
        return mods;
    }

    public void setMods(List<ArtifactDesc> mods) {
        this.mods = mods;
    }
}
