package org.eientei.codemine.launcher.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.eientei.codemine.launcher.data.UserInfo;
import org.eientei.codemine.launcher.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@FXMLController
public class RootController {
    @Value("${minecraft.users}")
    private String usersUrl;

    @FXML
    private GridPane loginPane;

    @FXML
    private GridPane signupPane;

    @FXML
    private GridPane mainPane;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MainController mainController;

    @FXML
    public void initialize() {
        if (configService.getToken() != null) {
            UserInfo user = null;
            try {
                user = restTemplate.getForObject(usersUrl + "/token?value=" + URLEncoder.encode(configService.getToken(), "UTF-8"), UserInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user != null) {
                configService.setUserName(user.getName());
                configService.setToken(user.getToken());
                showMain();
            }
        }
    }

    public void showSignup() {
        loginPane.setVisible(false);
        signupPane.setVisible(true);
        mainPane.setVisible(false);
    }

    public void showLogin() {
        loginPane.setVisible(true);
        signupPane.setVisible(false);
        mainPane.setVisible(false);
    }

    public void showMain() {
        loginPane.setVisible(false);
        signupPane.setVisible(false);
        mainPane.setVisible(true);

        mainController.greet(configService.getUserName());
    }
}
