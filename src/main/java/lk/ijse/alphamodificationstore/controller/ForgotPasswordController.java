package lk.ijse.alphamodificationstore.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.alphamodificationstore.model.AuthenticationModel;

import java.awt.event.ActionEvent;
import java.util.Properties;
import java.util.Random;

public class ForgotPasswordController {
    public Label lblHeader;
    public TextField txtUserEmail;
    public TextField txtOtpCode;

    public AnchorPane ancOtpPage;
    public PasswordField txtPassword;
    public PasswordField txtConfirmPassword;

    private final AuthenticationModel forgotPasswordModel = new AuthenticationModel();
    public AnchorPane ancOtpPage2;


    private int otpCode;
    private static String userEmail = "";
    private final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.\\-_*])[a-zA-Z0-9@#$%^&+=.\\-_]{6,}$";
    private final String errorStyle = "-fx-border-color: #ce0101; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-background-radius: 10px";
    private final String normalStyle = "-fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 2px; -fx-background-radius: 10px";


    public void updatePassword(String password){
        try {
            boolean isPasswordUpdated = AuthenticationModel.updatePassword(password , userEmail);

            if (isPasswordUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Password has been updated successfully.").show();
                Stage stage = (Stage) ancOtpPage.getScene().getWindow();
                stage.close();
            }else {
                new Alert(Alert.AlertType.ERROR, "Error when updating password. Please try again!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error when updating password").show();
            e.printStackTrace();
        }
    }
        public void validatePassword(ActionEvent actionEvent){
        String password = txtPassword.getText().trim();
        if (!password.matches(passwordPattern)) {
            txtPassword.setStyle(errorStyle);
            new Alert(Alert.AlertType.ERROR, "Invalid Password").show();
        }else {
            if (!txtPassword.getText().trim().equals(txtConfirmPassword.getText().trim())) {
                new Alert(Alert.AlertType.ERROR, "Invalid Confirm Password").show();
            }else {
                txtPassword.setStyle(normalStyle);
                updatePassword(password);
            }
        }
    }
    private void navigateTo(String path) {
        try {
            ancOtpPage2.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancOtpPage2.widthProperty());
            anchorPane.prefHeightProperty().bind(ancOtpPage2.heightProperty());

            ancOtpPage2.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void sendOTP(javafx.event.ActionEvent event) {
        userEmail = txtUserEmail.getText().trim();
        Random random = new Random();
        otpCode = random.nextInt(9999) > 999 ? 1000 : random.nextInt(9999);

        try {
            boolean isEmailExisitiong = forgotPasswordModel.checkIfEmailExists(userEmail);

            if (!isEmailExisitiong) {
                new Alert(Alert.AlertType.ERROR, "Your Email Does not have in database").show();
                return;
            }
            final String systemEmail = "dilshanfernando20031010@gmail.com";
            final String userPassword =  "nggb jrvz ghwr jmeh";

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            jakarta.mail.Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(systemEmail , userPassword);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom();new InternetAddress(systemEmail);
                message.setRecipient(Message.RecipientType.TO,new InternetAddress(userEmail));
                message.setSubject("OTP Code for Forgot Password is " + otpCode);
                message.setText("Your otp code is " + otpCode + "never share it with anyone. \n Enjoy using Alpha Modification to your heart's content...");

                Transport.send(message);
                lblHeader.setText("OTP has been sent.");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when checking if email exists.").show();
        }
    }

    public void visitUpdatePassword(javafx.event.ActionEvent event) {
        if (Integer.parseInt(txtOtpCode.getText()) == otpCode) {
            navigateTo( "/view/UpdatePassword.fxml");
        }else {
            new Alert(Alert.AlertType.ERROR, "Invalid OTP Code").show();
        }
    }
}
