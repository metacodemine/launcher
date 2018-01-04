package org.eientei.codemine.launcher.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.eientei.codemine.launcher.service.ConfigService;
import org.eientei.codemine.launcher.service.LaunchService;
import org.eientei.codemine.launcher.service.Progressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@FXMLController
public class MainController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${minecraft.news}")
    private String newsUrl;

    @FXML
    public Label welcome;

    @FXML
    private WebView newsView;

    @FXML
    private Button launchButton;

    @FXML
    private VBox progressBox;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Label progressText;

    @Autowired
    private RootController rootController;

    @Autowired
    private ConfigService configService;

    @Autowired
    private LaunchService launchService;

    public void greet(String name) {
        welcome.setText("Namaste, " + name + "!");
    }

    @FXML
    public void initialize() {
        WebEngine engine = newsView.getEngine();
        engine.load(newsUrl);
    }

    @FXML
    public void logout() {
        configService.setToken(null);
        rootController.showLogin();
    }

    @FXML
    public void launch() {
        launchButton.setVisible(false);
        progressBox.setVisible(true);

        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                launchService.launch(new Progressor() {
                    @Override
                    public void updateText(String text) {
                        Platform.runLater(() -> progressText.setText(text));
                    }

                    @Override
                    public void updateProgress(double value) {
                        Platform.runLater(() -> progressBar.setProgress(value));
                    }

                    @Override
                    public void error(String text) {
                        System.out.println(text);
                        System.exit(1);
                    }
                });
                return null;
            }
        }).start();
    }
}
