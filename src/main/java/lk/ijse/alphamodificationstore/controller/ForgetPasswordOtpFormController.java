package lk.ijse.alphamodificationstore.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgetPasswordOtpFormController {

    public TextField txtForgetOTP1;
    public TextField txtForgetOTP2;
    public TextField txtForgetOTP3;
    public TextField txtForgetOTP4;
    public Button btnConfirm;

    public void initialize() {
        Platform.runLater(() -> txtForgetOTP1.requestFocus());

        txtForgetOTP1.setOnKeyReleased(event -> handleKeyEvent(txtForgetOTP1, txtForgetOTP2, null));
        txtForgetOTP2.setOnKeyReleased(event -> handleKeyEvent(txtForgetOTP2, txtForgetOTP3, txtForgetOTP1));
        txtForgetOTP3.setOnKeyReleased(event -> handleKeyEvent(txtForgetOTP3, txtForgetOTP4, txtForgetOTP2));
        txtForgetOTP4.setOnKeyReleased(event -> handleKeyEvent(txtForgetOTP4, null, txtForgetOTP3));
    }
    private void handleKeyEvent(TextField focusedField, TextField nextField, TextField previousField) {
        if (focusedField.getText().length() == 1) {
            if (nextField != null) {
                nextField.requestFocus();
            }
            } else if (previousField != null) {
                    if (previousField != null){
                        previousField.requestFocus();
                    }
            }
        }

    public void btnBackLoginOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.show();
    }
    public void btnConfirmOnAction(ActionEvent event) {
        String OTP1 = txtForgetOTP1.getText();
        String OTP2 = txtForgetOTP2.getText();
        String OTP3 = txtForgetOTP3.getText();
        String OTP4 = txtForgetOTP4.getText();

        String otp = OTP1 + OTP2 + OTP3 + OTP4;

        try {
            if ((String.valueOf(ForgotPasswordPageController.OTP)).equals(otp)) {
                new Alert(Alert.AlertType.CONFIRMATION,"Correct otp..", ButtonType.OK).show();
                Parent root = FXMLLoader.load(getClass().getResource("/view/ForgetNewPasswordForm.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Password Forget Form");
                stage.show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Incorrect OTP", ButtonType.OK).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }
}
