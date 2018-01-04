package org.eientei.codemine.launcher.service;

public interface Progressor {
    void updateText(String text);
    void updateProgress(double value);
    void error(String text);
}
