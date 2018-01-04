package org.eientei.codemine.launcher.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.eientei.codemine.launcher.data.UserInfo;
import org.eientei.codemine.launcher.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

@FXMLController
public class LoginController {
    @Value("${minecraft.users}")
    private String usersUrl;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @Autowired
    private RootController rootController;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RestTemplate restTemplate;

    @FXML
    public void enterPressed(KeyEvent event) {
        loginUsername.setStyle("");
        if (event.getCode() == KeyCode.ENTER) {
            if (StringUtils.isEmpty(loginUsername.getText())) {
                loginUsername.requestFocus();
            } else if (StringUtils.isEmpty(loginPassword.getText())) {
                loginPassword.requestFocus();
            } else {
                loginAction();
            }
        }
    }

    @FXML
    public void loginAction() {
        UserInfo user = null;
        try {
            user = restTemplate.getForObject(usersUrl + "/login?username=" + URLEncoder.encode(loginUsername.getText(), "UTF-8") + "&password=" + URLEncoder.encode(loginPassword.getText(), "UTF-8"), UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            loginUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ");
        } else {
            loginUsername.setStyle("");
            configService.setUserName(user.getName());
            configService.setToken(user.getToken());
            rootController.showMain();
        }
    }

    @FXML
    public void clickSignup() {
        rootController.showSignup();
    }
}
