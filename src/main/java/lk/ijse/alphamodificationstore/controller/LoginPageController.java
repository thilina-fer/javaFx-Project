package lk.ijse.alphamodificationstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;

public class LoginPageController {
    public TextField txtUsername;
    public TextField txtPassword;
    public Button btnSignIn;
    public Button btnSignUp;

    private final String userNamePattern = "^[A-Za-z0-9_ ]{3,}$";
    private final String passwordPattern = "^[A-Za-z0-9@#$%^&+=]{6,}$";
    public AnchorPane ancSignIn;

    private void initialize() {
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> validFields());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> validFields());

        btnSignIn.setDisable(true);
    }

    private void validFields() {
        boolean isValidUsername = txtUsername.getText().matches(userNamePattern);
        boolean isValidPassword = txtPassword.getText().matches(passwordPattern);

        txtUsername.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        txtPassword.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        if (!isValidUsername)
            txtUsername.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPassword)
            txtPassword.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");

        btnSignUp.setDisable(!(isValidUsername && isValidPassword));
    }

    public void btnSignInOnAction(ActionEvent actionEvent) {
        String inputUsername = txtUsername.getText();
        String inputPassword = txtPassword.getText();

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields", ButtonType.OK).show();
            return;
        }
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE user_name = ? AND password = ?",
                    inputUsername, inputPassword
            );
            if (resultSet.next()) {
                try {
                    Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/view/DashboardPage.fxml"));
                    Stage dashboardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    dashboardStage.setScene(new Scene(dashboardRoot));
                    dashboardStage.setTitle("Alpha Modifications");
                    dashboardStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK).show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password", ButtonType.OK).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Login Fail", ButtonType.OK).show();
        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        try {
            Parent dashBoardRoot = FXMLLoader.load(getClass().getResource("/view/SignUpPage.fxml"));
            Stage dashBoardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            dashBoardStage.setScene(new Scene(dashBoardRoot));
            dashBoardStage.setTitle("Alpha Modifications");
            dashBoardStage.setResizable(true);
            dashBoardStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SignUp Failed", ButtonType.OK).show();
        }
    }

    private void navigateTo(String path) {
        try {
            ancSignIn.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSignIn.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSignIn.heightProperty());

            ancSignIn.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void goToforgotPassword(MouseEvent mouseEvent) {
        navigateTo("/view/FogotPasswordPage.fxml");
    }
}
