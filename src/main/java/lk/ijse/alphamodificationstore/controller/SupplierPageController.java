package lk.ijse.alphamodificationstore.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alphamodificationstore.dto.SupplierDto;
import lk.ijse.alphamodificationstore.dto.Tm.SupplierTm;
import lk.ijse.alphamodificationstore.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierPageController implements Initializable {
    public Label lblSupplierId;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtAddress;
    public AnchorPane ancSupplierPage;

    public TableView<SupplierTm> tblSupplier;
    public TableColumn<SupplierTm , String> colId;
    public TableColumn<SupplierTm , String> colName;
    public TableColumn<SupplierTm , String> colContact;
    public TableColumn<SupplierTm , String> colAddress;

    private final SupplierModel supplierModel = new SupplierModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String addressPattern = "^[A-Z][a-z]+(?: [A-Z][a-z]+)*$";
    public TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("supplierContact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("supplierAddress"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    public void loadTableData() throws SQLException {
        tblSupplier.setItems(FXCollections.observableArrayList(
                supplierModel.getAllSuppliers().stream()
                        .map(supplierDto -> new SupplierTm(
                                supplierDto.getSupplierId(),
                                supplierDto.getSupplierName(),
                                supplierDto.getSupplierContact(),
                                supplierDto.getSupplierAddress()
                        )).toList()
        ));
    }

    public void resetPage(){
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

            txtName.setText(null);
            txtContact.setText(null);
            txtAddress.setText(null);
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }
    public void btnSaveOnAction(ActionEvent actionEvent) {
        String supplierId = lblSupplierId.getText();
        String supplierName = txtName.getText();
        String supplierContact = txtContact.getText();
        String supplierAddress = txtAddress.getText();

        boolean isValidName = supplierName.matches(namePattern);
        boolean isValidContact = supplierContact.length() == 10;
        boolean isValidAddress = supplierAddress.matches(addressPattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;-fx-border-radius: 20;");
        txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: #7367F0; -fx-border-radius: 20;");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0;-fx-border-radius: 20;");

        if(!isValidName) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidContact) txtContact.setStyle(txtContact.getStyle() + ";-fx-border-color: red;");
        if (!isValidAddress) txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                supplierName,
                supplierContact,
                supplierAddress
        );

        if (isValidName && isValidContact && isValidAddress) {
            try {
                boolean isSaved = supplierModel.saveSupplier(supplierDto);
                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Saved").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Supplier Save Failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String supplierId = lblSupplierId.getText();
        String supplierName = txtName.getText();
        String supplierContact = txtContact.getText();
        String supplierAddress = txtAddress.getText();

        SupplierDto supplierDto = new SupplierDto(
                supplierId,
                supplierName,
                supplierContact,
                supplierAddress
        );

        try {
            boolean isUpdated = supplierModel.updateSupplier(supplierDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Supplier Update Failed").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
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

        if(response.isPresent() && response.get() == ButtonType.YES){
            String customerId = lblSupplierId.getText();
            try {
                boolean isDeleted = supplierModel.deleteSupplier(customerId);
                if(isDeleted){
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION,"Supplier Deleted Successfully.").show();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Supplier Delete Failed").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = supplierModel.getNextSupplierId();
        lblSupplierId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        SupplierTm selectedItem = tblSupplier.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblSupplierId.setText(selectedItem.getSupplierId());
            txtName.setText(selectedItem.getSupplierName());
            txtContact.setText(selectedItem.getSupplierContact());
            txtAddress.setText(selectedItem.getSupplierAddress());

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
            ancSupplierPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSupplierPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSupplierPage.heightProperty());

            ancSupplierPage.getChildren().add(anchorPane);

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    @FXML
    private void search() {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Suppliers").show();
            }
        }else {
            try {
                ArrayList<SupplierDto> customerList = supplierModel.searchSupplier(searchText);
                tblSupplier.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(supplierDto -> new SupplierTm(
                                        supplierDto.getSupplierId(),
                                        supplierDto.getSupplierName(),
                                        supplierDto.getSupplierContact(),
                                        supplierDto.getSupplierAddress()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search suppliers").show();
            }
        }


  /*  private void loadSearchResults(String phoneNum) {
        try {
            ArrayList<SupplierDto> contacts = supplierModel.getSupplierDetailsFromContact(phoneNum);
            if (contacts == null){
                tblSupplier.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
                tblSupplier.setItems(FXCollections.observableArrayList(
                        supplierModel.getSupplierDetailsFromContact(phoneNum).stream()
                                .map(supplierDto -> new SupplierTm(
                                        supplierDto.getSupplierId(),
                                        supplierDto.getSupplierName(),
                                        supplierDto.getSupplierContact(),
                                        supplierDto.getSupplierAddress()
                                )).toList()
                ));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
            e.printStackTrace();
        }*/
    }

    public void goToSupplierOrderPage(MouseEvent mouseEvent) {
        navigateTo("/view/SupplierOrderPage.fxml");
    }
}
