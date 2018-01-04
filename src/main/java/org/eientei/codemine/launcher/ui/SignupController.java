package org.eientei.codemine.launcher.ui;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
public class SignupController {
    @Value("${minecraft.users}")
    private String usersUrl;

    @FXML
    public Button signupButton;

    @FXML
    private TextField signupUsername;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private PasswordField signupRepeat;

    @Autowired
    private RootController rootController;

    @Autowired
    private ConfigService configService;

    @Autowired
    private RestTemplate restTemplate;

    @FXML
    public void enterPressed(KeyEvent event) {
        ((TextField)event.getTarget()).setStyle("");
        if (event.getCode() == KeyCode.ENTER) {
            if (StringUtils.isEmpty(signupUsername.getText())) {
                signupUsername.requestFocus();
            } else if (StringUtils.isEmpty(signupPassword.getText())) {
                signupPassword.requestFocus();
            } else if (StringUtils.isEmpty(signupRepeat.getText())) {
                signupRepeat.requestFocus();
            } else if (!signupButton.isDisabled()) {
                signupAction();
            }
        } else {
            passwordMatch();
        }
    }

    public void passwordMatch() {
        if (signupPassword.getText().equals(signupRepeat.getText()) && signupPassword.getText().length() > 0) {
            signupRepeat.setStyle("");
            signupButton.setDisable(false);
        } else {
            signupRepeat.setStyle("-fx-border-color: red ; -fx-border-width: 2px ");
            signupButton.setDisable(true);
        }
    }

    @FXML
    public void signupAction() {
        UserInfo user = null;
        try {
            user = restTemplate.getForObject(usersUrl + "/signup?username=" + URLEncoder.encode(signupUsername.getText(), "UTF-8") + "&password=" + URLEncoder.encode(signupPassword.getText(), "UTF-8"), UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            signupUsername.setStyle("-fx-border-color: red ; -fx-border-width: 2px ");
        } else {
            signupUsername.setStyle("");
            configService.setUserName(user.getName());
            configService.setToken(user.getToken());
            rootController.showMain();
        }
    }

    @FXML
    public void clickCancel() {
        rootController.showLogin();
    }
}
