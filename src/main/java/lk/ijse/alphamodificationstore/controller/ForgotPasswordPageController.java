package lk.ijse.alphamodificationstore.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class ForgotPasswordPageController {

    public Label lblForgetPassword;
    public TextField txtUsername;
    public TextField txtEmail;
    public Label lblCheckUsername;

    public static int OTP;
    public static String Username;
    public ObservableList<String> obList;

    public void initialize() {
        animateLabelTyping();
        getUsernames();
    }

    public void btnForEmailOnAction(ActionEvent event) {
        String username = txtUsername.getText();

        try {
            String isCheckedEmail = ForgotPasswordPageController.checkEmail(username);
            if (isCheckedEmail != null) {
                txtEmail.setText(isCheckedEmail);
                btnSendOtpOnAction(event);
            }else {
                txtEmail.clear();
                new Alert(Alert.AlertType.ERROR,"Can't find email.");
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Can't find email.");
        }
    }

    private static String checkEmail(String username) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE user_name = ?");

        if (resultSet.next()) {
            return resultSet.getString("email");
        }
        return null;
    }

    private void getUsernames(){
        obList = FXCollections.observableArrayList();
        try {
            List<String> NoList = ForgotPasswordPageController.getUser();
            for (String username : NoList) {
                obList.add(username);
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Can't find email.");
        }
    }

    private static List<String> getUser() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT user_name FROM user");
        List<String> idList = null;
        while (resultSet.next()){
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

    public void btnSendOtpOnAction(ActionEvent event) throws IOException {
        String email = txtEmail.getText();
        String username = txtUsername.getText();

        if (username != null){
            Random random = new Random();
            int otp = 1000 + random.nextInt(9000);

            boolean sendingOTP = SendMail.sendMail(email, otp);
            if (sendingOTP){
                new Alert(Alert.AlertType.INFORMATION, "OTP Sent", ButtonType.OK).show();
            } else {
                new Alert(Alert.AlertType.ERROR, "OTP Failed", ButtonType.OK).show();
            }
            Username = username;
            OTP = otp;
            System.out.println(">>>" + otp);

            //Parent root = FXCollections.load(getClass().getResource("/view/ForgetPasswordOtpForm.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/view/ForgetPasswordOtpForm.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("OTP form");
            stage.show();
        }else {
            new Alert(Alert.AlertType.ERROR,"username type ", ButtonType.OK).show();
        }
    }

    public void hyperOnActionLogin(ActionEvent event) throws IOException {
       // Parent root = FXCollections.load(getClass().getResource("/view/LoginPage.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Page");
        stage.show();
    }

    private void animateLabelTyping(){
        String loginText = lblForgetPassword.getText();
        int animationDuration = 250;

        lblForgetPassword.setText("");
        Timeline typingAnimation = new Timeline();// Duration of animation in milliseconds

        for (int i = 0; i <= loginText.length(); i++) {
            int finalI = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(animationDuration * i),
                    event -> lblForgetPassword.setText(loginText.substring(0, finalI))
            );
            typingAnimation.getKeyFrames().add(keyFrame);
        }
        typingAnimation.play();
    }
}
