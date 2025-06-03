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
import lk.ijse.alphamodificationstore.dto.SupplierOrderDto;
import lk.ijse.alphamodificationstore.dto.Tm.SupplierOrderTm;
import lk.ijse.alphamodificationstore.model.SupplierOrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierOrderPageController implements Initializable {
    public Label lblSupplierOrderId;
    public ComboBox comboSupplierId;
    public ComboBox ComboUserId;
    public TextField txtDate;
    public ComboBox comboItemId;

    public TableView<SupplierOrderTm> tblSupplierOrder;
    public TableColumn<SupplierOrderTm, String> colOrderId;
    public TableColumn<SupplierOrderTm, String> colSupplierId;
    public TableColumn<SupplierOrderTm, String> colUserId;
    public TableColumn<SupplierOrderTm, String> colDate;
    public TableColumn<SupplierOrderTm, String> colItemId;

    private final SupplierOrderModel supplierOrderModel = new SupplierOrderModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TextField searchField;
    public AnchorPane ancSupplierOrderPage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("supplyOrderId"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        try {
            loadTableData();
            loadSupplierDetails();
            loadUserId();
            loadItemDetails();
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong").show();
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblSupplierOrder.setItems(FXCollections.observableArrayList(
                supplierOrderModel.getAllSuppilerOrders().stream()
                        .map(supplierOrderDto -> new SupplierOrderTm(
                                supplierOrderDto.getSupplyOrderId(),
                                supplierOrderDto.getSupplierId(),
                                supplierOrderDto.getUserId(),
                                supplierOrderDto.getDate(),
                                supplierOrderDto.getItemId()
                        )).toList()
        ));
    }

    private void resetPage() {
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            comboSupplierId.getSelectionModel().clearSelection();
            ComboUserId.getSelectionModel().clearSelection();
            txtDate.setText(java.time.LocalDate.now().toString());
            comboItemId.getSelectionModel().clearSelection();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String supplieerOrderId = lblSupplierOrderId.getText();
        String supplierId = (String) comboSupplierId.getValue();
        String userId = (String) ComboUserId.getValue();
        String date = txtDate.getText();
        String itemId = (String) comboItemId.getValue();

        SupplierOrderDto supplierOrderDto = new SupplierOrderDto(
                supplieerOrderId,
                supplierId,
                userId,
                date,
                itemId
        );
        try {
            boolean isSavd = supplierOrderModel.saveSuppilerOrder(supplierOrderDto);

            if (isSavd) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed").show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String supplieerOrderId = lblSupplierOrderId.getText();
        String supplierId = (String) comboSupplierId.getValue();
        String userId = (String) ComboUserId.getValue();
        String date = txtDate.getText();
        String itemId = (String) comboItemId.getValue();

        SupplierOrderDto supplierOrderDto = new SupplierOrderDto(
                supplieerOrderId,
                supplierId,
                userId,
                date,
                itemId
        );
        try {
            boolean isUpdated = supplierOrderModel.updateSuppilerOrder(supplierOrderDto);

            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Update Successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed").show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went Wrong").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are You Sure ? ",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            String soId = lblSupplierOrderId.getText();

            try {

                boolean isDeleted = supplierOrderModel.deleteSuppilerOrder(soId);

                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            }
        }
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = supplierOrderModel.getNextSoId();
        lblSupplierOrderId.setText(nextId);
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        SupplierOrderTm selectedItem = tblSupplierOrder.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblSupplierOrderId.setText(selectedItem.getSupplyOrderId());
            comboSupplierId.getSelectionModel().select(selectedItem.getSupplierId());
            ComboUserId.getSelectionModel().select(selectedItem.getUserId());
            txtDate.setText(selectedItem.getDate());
            comboItemId.getSelectionModel().select(selectedItem.getItemId());

        }
    }

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }

    public void goToSaveSupplierPage(MouseEvent mouseEvent) {
        navigateTo("/view/SupplierPage.fxml");
    }

    public void goToSaveUserPage(MouseEvent mouseEvent) {
        navigateTo("/view/SignUpPage.fxml");
    }

    public void goToSaveItemPage(MouseEvent mouseEvent) {
        navigateTo("/view/ItemPage.fxml");
    }

    private void navigateTo(String path) {
        try {
            ancSupplierOrderPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancSupplierOrderPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancSupplierOrderPage.heightProperty());

            ancSupplierOrderPage.getChildren().add(anchorPane);

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
                new Alert(Alert.AlertType.ERROR, "Failed to load Orders").show();
            }
        }else {
            try {
                ArrayList<SupplierOrderDto> orderList = supplierOrderModel.searchSupplierOrders(searchText);
                tblSupplierOrder.setItems(FXCollections.observableArrayList(
                        orderList.stream()
                                .map(supplierOrderDto -> new SupplierOrderTm(
                                        supplierOrderDto.getSupplyOrderId(),
                                        supplierOrderDto.getSupplierId(),
                                        supplierOrderDto.getUserId(),
                                        supplierOrderDto.getDate(),
                                        supplierOrderDto.getItemId()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search orders").show();
            }
        }
    }

   /* private void loadSearchResults(String phoneNum) {
        try {
            ArrayList<SupplierOrderDto> contacts = supplierOrderModel.getSupplierOrderDetailsFromSupplierOrderId(phoneNum);
            if (contacts == null) {
                tblSupplierOrder.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
                tblSupplierOrder.setItems(FXCollections.observableArrayList(
                        supplierOrderModel.getSupplierOrderDetailsFromSupplierOrderId(phoneNum).stream()
                                .map(supplierOrderDto -> new SupplierOrderTm(
                                        supplierOrderDto.getSupplyOrderId(),
                                        supplierOrderDto.getSupplierId(),
                                        supplierOrderDto.getUserId(),
                                        supplierOrderDto.getDate(),
                                        supplierOrderDto.getItemId()
                                )).toList()
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
        }
    }*/

    private void loadSupplierDetails() throws SQLException {
        comboSupplierId.setItems(FXCollections.observableArrayList(supplierOrderModel.getAllSupplierDetails()));
    }

    private void loadUserId() throws SQLException {
        ComboUserId.setItems(FXCollections.observableArrayList(supplierOrderModel.getAllUserId()));
    }
    private void loadItemDetails() throws SQLException {
        comboItemId.setItems(FXCollections.observableArrayList(supplierOrderModel.getAllItemDetails()));
    }
}
