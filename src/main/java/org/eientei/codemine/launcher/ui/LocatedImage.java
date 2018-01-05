package org.eientei.codemine.launcher.ui;

import javafx.scene.image.Image;

public class LocatedImage extends Image {
    private final String url;

    public LocatedImage(String url) {
        super(url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
