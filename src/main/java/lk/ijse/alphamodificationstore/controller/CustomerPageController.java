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
import lk.ijse.alphamodificationstore.dto.CustomerDto;
import lk.ijse.alphamodificationstore.dto.Tm.CustomerTm;
import lk.ijse.alphamodificationstore.model.CustomerModel;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {
    public Label lblCustomerId;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtAddress;

    public TableView<CustomerTm> tblCustomer;
    public TableColumn<CustomerTm, String> colId;
    public TableColumn<CustomerTm, String> colName;
    public TableColumn<CustomerTm, String> colContact;
    public TableColumn<CustomerTm, String> colAddress;

    private final CustomerModel customerModel = new CustomerModel();
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String contactPattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";
    private final String addressPattern = "^[A-Za-z ]+$";
    public AnchorPane ancCustomerPage;
    public TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("customerContact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("CustomerAddress"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void loadTableData() throws SQLException {
        tblCustomer.setItems(FXCollections.observableArrayList(
                customerModel.getAllCustomer().stream()
                        .map(customerDto -> new CustomerTm(
                                customerDto.getCustomerId(),
                                customerDto.getCustomerName(),
                                customerDto.getCustomerContact(),
                                customerDto.getCustomerAddresss()
                        )).toList()
        ));
    }

    private void resetPage() {
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

            txtName.setText(null);
            txtContact.setText(null);
            txtAddress.setText(null);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String customerId = lblCustomerId.getText();
        String customerName = txtName.getText();
        String customerContact = txtContact.getText();
        String customerAddress = txtAddress.getText();

        boolean isValidName = customerName.matches(namePattern);
        boolean isValidContact = customerContact.matches(contactPattern);
        boolean isValidAddress = customerAddress.matches(addressPattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: #7367F0");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0");

        if (!isValidName) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidContact) txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: red;");
        if (!isValidAddress) txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");

        CustomerDto customerDto = new CustomerDto(
                customerId,
                customerName,
                customerContact,
                customerAddress
        );

        if (isValidName && isValidContact && isValidAddress) {
            try {
                boolean isSaved = customerModel.saveCustomer(customerDto);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Saved").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String customerId = lblCustomerId.getText();
        String customerName = txtName.getText();
        String customerContact = txtContact.getText();
        String customerAddress = txtAddress.getText();

        CustomerDto customerDto = new CustomerDto(
                customerId,
                customerName,
                customerContact,
                customerAddress
        );
        try {
            boolean isUpdated = customerModel.updateCustomer(customerDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
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
            String customerId = lblCustomerId.getText();
            try {
                boolean isDeleted = customerModel.deleteCustomer(customerId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {

        resetPage();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = customerModel.getNextCustomerId();
        lblCustomerId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CustomerTm selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblCustomerId.setText(selectedItem.getCustomerId());
            txtName.setText(selectedItem.getCustomerName());
            txtContact.setText(selectedItem.getCustomerContact());
            txtAddress.setText(selectedItem.getCustomerAddress());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");

    }

    private void navigateTo(String path) {
        try {
            ancCustomerPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancCustomerPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancCustomerPage.heightProperty());

            ancCustomerPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Customers").show();
            }
        }else {
            try {
                ArrayList<CustomerDto> customerList = customerModel.searchCustomer(searchText);
                tblCustomer.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(customerDto -> new CustomerTm(
                                        customerDto.getCustomerId(),
                                        customerDto.getCustomerName(),
                                        customerDto.getCustomerContact(),
                                        customerDto.getCustomerAddresss()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search customers").show();
            }
        }
    }

    /*private void loadSearchResults(String phoneNum) {
        try {
            ArrayList<CustomerDto> contacts = customerModel.getCustomerDetailsFromContact(phoneNum);
            if (contacts == null) {
                tblCustomer.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
                tblCustomer.setItems(FXCollections.observableArrayList(
                        customerModel.getCustomerDetailsFromContact(phoneNum).stream()
                                .map(customerDto -> new CustomerTm(
                                        customerDto.getCustomerId(),
                                        customerDto.getCustomerName(),
                                        customerDto.getCustomerContact(),
                                        customerDto.getCustomerAddresss()
                                )).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
        }
    }*/
}
