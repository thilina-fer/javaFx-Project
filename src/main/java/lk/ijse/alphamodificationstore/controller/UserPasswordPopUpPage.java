package lk.ijse.alphamodificationstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class UserPasswordPopUpPage {
    public TextField txtUsename;
    public TextField txtPassword;
    public TextField txtEmail;
    public Button btnGoToUserPage;

    private final String userNamePattern = "^[A-Za-z0-9_ ]{3,}$";
    private final String passwordPattern = "^[A-Za-z0-9@#$%^&+=]{6,}$";
    private final String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public AnchorPane popUpPage;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUsename.textProperty().addListener((observable, oldValue, newValue) -> validFields());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> validFields());
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> validFields());

        btnGoToUserPage.setDisable(true);
    }

    private void validFields() {
        boolean isValidUsername = txtUsename.getText().matches(userNamePattern);
        boolean isValidPassword = txtPassword.getText().matches(passwordPattern);
        boolean isValidEmail = txtEmail.getText().matches(emailPattern);

        txtUsename.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        txtPassword.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        txtEmail.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        if (!isValidUsername)
            txtUsename.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPassword)
            txtPassword.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidEmail)
            txtEmail.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        btnGoToUserPage.setDisable(!(isValidUsername && isValidPassword && isValidEmail));
    }

    public void btnGoToUserPageOnAction(ActionEvent actionEvent) {
        String username = txtUsename.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();
        String role = "owner";

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT user_name, password, email FROM user WHERE role = ?", role);

            if (resultSet.next()) {
                String dbUsername = resultSet.getString("user_name");
                String dbPassword = resultSet.getString("password");
                String dbEmail = resultSet.getString("email");

                if (username.equals(dbUsername) && password.equals(dbPassword) && email.equals(dbEmail)) {
                    new Alert(Alert.AlertType.INFORMATION, "Welcome User Manage").show();
                    navigateTo("/view/UserPage.fxml");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Only access this page on Owner").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "No user found with the role 'owner'.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void navigateTo(String path) {
        try {
            popUpPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(popUpPage.widthProperty());
            anchorPane.prefHeightProperty().bind(popUpPage.heightProperty());

            popUpPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }
}
