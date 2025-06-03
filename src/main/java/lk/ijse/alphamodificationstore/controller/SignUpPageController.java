package lk.ijse.alphamodificationstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.alphamodificationstore.dto.UserDto;
import lk.ijse.alphamodificationstore.model.UserModel;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;

public class SignUpPageController {

    public TextField txtUsername;
    public TextField txtEmail;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtRole;
    public PasswordField txtPassword;
    public PasswordField txtRePassword;
    public Button btnSignIn;
    public Button btnSignUp;

    private final String userNamePattern = "^[A-Za-z0-9_]{3,}$";
    private final String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private final String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
    private final String contactPattern = "^[0-9]{10}$";
    public AnchorPane ancSignUpPage;

    public void initialize() {
        txtUsername.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );
        txtEmail.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );
        txtAddress.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );
        txtContact.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );
        txtPassword.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );
        txtRePassword.textProperty().addListener((observableValue, oldValue, newValue) ->validateFields() );

        btnSignIn.setDisable(true);
    }

    private void validateFields(){
        boolean isValidUserName = txtUsername.getText().matches(userNamePattern);
        boolean isValidEmail = txtEmail.getText().matches(emailPattern);
        //boolean isValidPassword = txtPassword.getText().matches(passwordPattern);
        boolean isValidContact = txtContact.getText().matches(contactPattern);
        boolean isValidPassword = txtPassword.getText().equals(txtRePassword.getText());
        boolean isValidRePassword = txtRePassword.getText().equals(txtPassword.getText());

        txtUsername.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtEmail.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtContact.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtAddress.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtPassword.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtRePassword.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        txtRole.setStyle("-fx-border-color: #7367F0; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        if (!isValidUserName) txtUsername.setStyle("-fx-border-color: red; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        if (!isValidEmail) txtEmail.setStyle("-fx-border-color: red; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        if (!isValidContact) txtContact.setStyle("-fx-border-color: red; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        if (!isValidPassword) txtPassword.setStyle("-fx-border-color: red; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        if (!isValidRePassword) txtRePassword.setStyle("-fx-border-color: red; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        btnSignUp.setDisable(!(isValidUserName && isValidEmail && isValidContact && isValidPassword && isValidRePassword));

    }

    public void btnSignInOnAction(ActionEvent actionEvent) {
        //navigateTo("/view/LoginPage.fxml");
        try {
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/view/DashboardPage.fxml"));
            Stage dashboardStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            dashboardStage.setScene(new Scene(dashboardRoot));
            dashboardStage.setTitle("Alpha Modifications");
            dashboardStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Faild to load Sign Up Page").show();

        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {

        String inputUsername = txtUsername.getText();
        String inputEmail = txtEmail.getText();
        String inputAddress = txtAddress.getText();
        String inputContact = txtContact.getText();
        String inputRole = txtRole.getText();
        String inputPassword = txtPassword.getText();
        String inputRePassword = txtRePassword.getText();

        if (inputUsername.isEmpty() || inputEmail.isEmpty() || inputContact.isEmpty() ||inputAddress.isEmpty() || inputPassword.isEmpty() || inputRePassword.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields").show();
            return;
        }
        if (!inputEmail.matches(emailPattern)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid email address").show();
            return;
        }
        if (!inputContact.matches(contactPattern)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid contact number").show();
            return;
        }

        if (!inputPassword.matches(passwordPattern)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid password").show();
            return;
        }

        if (!inputRePassword.matches(inputRePassword)) {
            new Alert(Alert.AlertType.ERROR, "Please reenter a valid password").show();
            return;
        }

        if (!inputPassword.equals(inputRePassword)) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match").show();
            return;
        }

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE user_name = ? OR email = ?" , inputUsername , inputEmail);

            if (resultSet.next()){
                new Alert(Alert.AlertType.ERROR, "User already exists").show();
                return;
            }
            String userId = UserModel.getNextUserId();

            boolean isSaved = UserModel.saveUser(new UserDto(
                    userId ,
                    inputUsername ,
                    inputEmail ,
                    inputPassword,
                    inputContact ,
                    inputAddress ,
                    inputRole
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User has been created").show();
                btnSignInOnAction(actionEvent);
            }else {
                new Alert(Alert.AlertType.ERROR, "User has not been saved").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Sign Up Faild").show();
        }
    }

    private void navigateTo(String path) {
        try {
            ancSignUpPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSignUpPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSignUpPage.heightProperty());

            ancSignUpPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void goToSignIn(ActionEvent event) {
        navigateTo("/view/LoginPage.fxml");
    }
}
