package lk.ijse.alphamodificationstore.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.io.IOException;

public class ForgetNewPasswordFormController {

    public Label lblForgetPassword;
    public PasswordField txtNewPassword;
    public PasswordField txtReNewPassword;
    public Button btnChangePassword;


    private final String passwordPattern = "^[A-Za-z0-9@#$%^&+=]{6,}$";

    private void initialize() {
        //animateLabelTyping();
    }

    /*private void animateLabelTyping() {
        String loginText = lblForgetPassword.getText();
        int animationDuration = 250; // Adjust speed as needed

        lblForgetPassword.setText("");

        Timeline typingAnimation = new Timeline();
        for (int i = 0; i <= loginText.length(); i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(animationDuration * i)) event -> {
                lblForgetPassword.setText(loginText.substring(0, finalI));
            });
            typingAnimation.getKeyFrames().add(keyFrame);
        }
    }*/

    public void btnChangePasswordOnAction(ActionEvent event) {
        String newPassword1 = txtNewPassword.getText();
        String newPassword2 = txtReNewPassword.getText();
        String username = ForgotPasswordPageController.Username;

        if (newPassword1.isEmpty() || newPassword2.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Please Enter All Fields").show();
            return;
        }
        try {
            if (!isValidPassword()){
                boolean isChangePassword = false;
                if (newPassword1.equals(newPassword2)){
                    isChangePassword = changePassword(username, newPassword1);
                }else {
                    new Alert(Alert.AlertType.ERROR,"Passwords do not match").show();
                    return;
                }
                if (isChangePassword){
                    new Alert(Alert.AlertType.INFORMATION,"Password Change Successfully.").show();
                    hyperOnActionLogin(event);
                }else {
                    new Alert(Alert.AlertType.ERROR,"Password Change Failed.", ButtonType.OK).show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR,"Invalid Password Format").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"An error occurred while changing the password.").show();
        }
    }

    private boolean changePassword(String username, String newPassword2) throws Exception {
        return CrudUtil.execute("UPDATE user SET password = ? WHERE user_name = ?", newPassword2, username);

    }
    public boolean isValidPassword(){
       boolean isValidNewPassword = txtNewPassword.getText().matches(passwordPattern);
         boolean isValidReNewPassword = txtReNewPassword.getText().matches(passwordPattern);

         return true;
    }
    void hyperOnActionLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.show();
    }
}
