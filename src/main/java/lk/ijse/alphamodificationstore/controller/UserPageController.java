package lk.ijse.alphamodificationstore.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alphamodificationstore.dto.Tm.UserTm;
import lk.ijse.alphamodificationstore.dto.UserDto;
import lk.ijse.alphamodificationstore.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserPageController implements Initializable {
    public AnchorPane ancUserPage;
    public TextField searchField;

    public Label lblUserId;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtPassword;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtRole;

    public TableView<UserTm> tblUser;
    public TableColumn<UserTm , String> colId;
    public TableColumn<UserTm, String> colName;
    public TableColumn<UserTm , String> colEmail;
    public TableColumn<UserTm , String> colPassword;
    public TableColumn<UserTm , String> colContact;
    public TableColumn<UserTm , String> colAddress;
    public TableColumn<UserTm , String> colRole;

    private final UserModel userModel = new UserModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String userNamePattern = "^[A-Za-z0-9_ ]{3,}$";
    private final String passwordPattern = "^[A-Za-z0-9@#$%^&+=]{6,}$";

    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            resetPage();
        }catch (Exception e ){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK).show();
        }
    }
    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblUser.setItems(FXCollections.observableArrayList(
                userModel.getAllUsers().stream().
                        map(userDto -> new UserTm(
                                userDto.getUserId(),
                                userDto.getUserName(),
                                userDto.getEmail(),
                                userDto.getPassword(),
                                userDto.getContact(),
                                userDto.getAddress(),
                                userDto.getRole()
                        )).toList()
        ));
    }
    private void resetPage(){
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtName.setText(null);
            txtEmail.setText(null);
            txtPassword.setText(null);
            txtContact.setText(null);
            txtAddress.setText(null);
            txtRole.setText(null);

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK).show();
        }
    }


    public void btnSaveOnAction(ActionEvent actionEvent) {
        String userId = lblUserId.getText();
        String userName = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String role = txtRole.getText();

        boolean isValidName = userName.matches(userNamePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPassword = password.matches(passwordPattern);
        boolean isValidContact = contact.length() == 10;

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #7367F0");
        txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: #7367F0");
        txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: #7367F0");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0");
        txtRole.setStyle(txtRole.getStyle() + ";-fx-border-color: #7367F0");

        if (!isValidName) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidEmail) txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        if (!isValidPassword) txtPassword.setStyle(txtPassword.getStyle() + ";-fx-border-color: red;");
        if (!isValidContact) txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: red;");

        UserDto userDto = new UserDto(
                userId,
                userName,
                email,
                password,
                contact,
                address,
                role
        );

        if (isValidName && isValidEmail && isValidPassword && isValidContact) {
            try{
                boolean isSaved = userModel.saveUser(userDto);

                if(isSaved){
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Saved Successfully", ButtonType.OK).show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "User Save Failed", ButtonType.OK).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Save Failed", ButtonType.OK).show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String userId = lblUserId.getText();
        String userName = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String role = txtRole.getText();

        UserDto userDto = new UserDto(
                userId,
                userName,
                email,
                password,
                contact,
                address,
                role
        );

        try{
            boolean isUpdated = userModel.updateUser(userDto);

            if(isUpdated){
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.ERROR, "User Update Failed", ButtonType.OK).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Update Failed", ButtonType.OK).show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are You Sure ? ",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String userId = lblUserId.getText();
            try{
                boolean isDeleted = userModel.deleteUser(userId);

                if(isDeleted){
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK).show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "User Delete Failed", ButtonType.OK).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Delete Failed", ButtonType.OK).show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void loadNextId(){
        String userId = lblUserId.getText();
        lblUserId.setText(userId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        UserTm selectedUser = tblUser.getSelectionModel().getSelectedItem();

        lblUserId.setText(selectedUser.getUserId());
        txtName.setText(selectedUser.getUserName());
        txtEmail.setText(selectedUser.getEmail());
        txtPassword.setText(selectedUser.getPassword());
        txtContact.setText(selectedUser.getContact());
        txtAddress.setText(selectedUser.getAddress());
        txtRole.setText(selectedUser.getRole());

        btnSave.setDisable(true);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Users").show();
            }
        }else {
            try {
                ArrayList<UserDto> customerList = userModel.searchUser(searchText);
                tblUser.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(userDto -> new UserTm(
                                        userDto.getUserId(),
                                        userDto.getUserName(),
                                        userDto.getEmail(),
                                        userDto.getPassword(),
                                        userDto.getContact(),
                                        userDto.getAddress(),
                                        userDto.getRole()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search users").show();
            }
        }
    }

    private void navigateTo(String path) {
        try {
            ancUserPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancUserPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancUserPage.heightProperty());

            ancUserPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }
}
