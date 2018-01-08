package org.eientei.codemine.launcher.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

@FXMLController
public class LogController {

    @FXML
    private TextArea logger;

    @FXML
    public void appendLog(String log) {
        int before = logger.getCaretPosition();
        boolean noscroll = before != logger.getLength();
        logger.appendText(log);
        logger.appendText("\n");
        if (noscroll) {
            logger.positionCaret(before);
        }
    }
}
