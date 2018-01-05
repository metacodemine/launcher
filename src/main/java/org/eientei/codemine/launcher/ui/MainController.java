package org.eientei.codemine.launcher.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.eientei.codemine.launcher.service.ConfigService;
import org.eientei.codemine.launcher.service.LaunchService;
import org.eientei.codemine.launcher.service.Progressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@FXMLController
public class MainController {
    @Value("${minecraft.news}")
    private String newsUrl;

    @Value("${minecraft.users}")
    private String usersUrl;

    @FXML
    private TextField memoryField;

    @FXML
    private ImageView skinPreview;

    @FXML
    private Label welcome;

    @FXML
    private WebView newsView;

    @FXML
    private Button launchButton;

    @FXML
    private VBox progressBox;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressText;

    @Autowired
    private RootController rootController;

    @Autowired
    private ConfigService configService;

    @Autowired
    private LaunchService launchService;

    @Autowired
    private RestTemplate restTemplate;

    public void greet(String name) {
        welcome.setText("Namaste, " + name + "!");
        memoryField.setText(configService.getMemory());
        skinPreview.setImage(new LocatedImage(usersUrl + "/texture/" + configService.getToken()));
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

    @FXML
    public void saveSettings() throws IOException {
        configService.setMemory(memoryField.getText());
        Image image = skinPreview.getImage();
        byte[] data = new byte[] { 0x00 };
        if (image != null) {
            String url = ((LocatedImage) image).getUrl();
            InputStream inputStream = new URL(url).openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, n);
            }
            data = baos.toByteArray();
        }
        restTemplate.postForEntity(usersUrl + "/setskin/" + configService.getToken(), data, Void.class);
    }

    @FXML
    public void deleteSkin() {
        skinPreview.setImage(null);
    }

    public void loadSkin(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select skin");
        File file = fileChooser.showOpenDialog(((Button) actionEvent.getTarget()).getScene().getWindow());
        skinPreview.setImage(new LocatedImage("file:" + file.getAbsolutePath()));
    }
}
