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
import lk.ijse.alphamodificationstore.dto.PreOrderDto;
import lk.ijse.alphamodificationstore.dto.Tm.PreOrderTm;
import lk.ijse.alphamodificationstore.model.PreOrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PreOrderPageController implements Initializable {
    public AnchorPane ancPreOrderPage;
    public Label lblPreOrderId;
    public ComboBox comboUserId;
    public ComboBox comboItemId;
    public TextField txtAdvance;

    public TableView<PreOrderTm> tblPayment;
    public TableColumn<PreOrderTm , String> colPreId;
    public TableColumn<PreOrderTm , String> colUserId;
    public TableColumn<PreOrderTm , String> colItemId;
    public TableColumn<PreOrderTm , Double> colAdvance;

    private final PreOrderModel preOrderModel = new PreOrderModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPreId.setCellValueFactory(new PropertyValueFactory<>("preOrderId"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colAdvance.setCellValueFactory(new PropertyValueFactory<>("advance"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    public void loadTableData() throws SQLException, ClassNotFoundException {
        tblPayment.setItems(FXCollections.observableArrayList(
                preOrderModel.getAllPreOrders().stream()
                        .map(preOrderDto -> new PreOrderTm(
                                preOrderDto.getPreOrderId(),
                                preOrderDto.getUserId(),
                                preOrderDto.getItemId(),
                                preOrderDto.getAdvance()
                        )).toList()
        ));
    }

    private void resetPage()  {
        try {
            loadTableData();
            loadUserId();
            loadItemDetils();
            loadNextId();

            btnSave.setDisable(false);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

            comboUserId.getSelectionModel().clearSelection();
            comboItemId.getSelectionModel().clearSelection();
            txtAdvance.setText(null);

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String preOrderId = lblPreOrderId.getText();
        String userId = (String) comboUserId.getValue();
        String itemId = (String) comboItemId.getValue();
        double advance = Double.parseDouble(txtAdvance.getText());

        PreOrderDto preOrderDto = new PreOrderDto(
                preOrderId,
                userId,
                itemId,
                advance
        );

        try {
            boolean isSaved = preOrderModel.savePreOrder(preOrderDto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Pre Order Saved Successfully!").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Save Pre Order!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }


    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String preOrderId = lblPreOrderId.getText();
        String userId = (String) comboUserId.getValue();
        String itemId = (String) comboItemId.getValue();
        double advance = Double.parseDouble(txtAdvance.getText());

        PreOrderDto preOrderDto = new PreOrderDto(
                preOrderId,
                userId,
                itemId,
                advance
        );

        try {
            boolean isUpdated = preOrderModel.updatePreOrder(preOrderDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Pre Order Updated Successfully!").show();
                resetPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Update Pre Order!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this Pre Order?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String preOrderId = lblPreOrderId.getText();
            try {
                boolean isDeleted = preOrderModel.deletePreOrder(preOrderId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Pre Order Deleted Successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to Delete Pre Order!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = preOrderModel.getNextPreOId();
        lblPreOrderId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        PreOrderTm selectedItem = tblPayment.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblPreOrderId.setText(selectedItem.getPreOrderId());
            comboUserId.getSelectionModel().select(selectedItem.getUserId());
            comboItemId.getSelectionModel().select(selectedItem.getItemId());
            txtAdvance.setText(String.valueOf(selectedItem.getAdvance()));

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void navigateTo(String path) {
        try {
            ancPreOrderPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancPreOrderPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancPreOrderPage.heightProperty());

            ancPreOrderPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void gotoItemPage(MouseEvent mouseEvent) {
        navigateTo("/view/ItemPage.fxml");
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()){
            try {
                loadTableData();
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Pre Orders").show();
            }
        }else {
            try {
                ArrayList<PreOrderDto> customerList = preOrderModel.searchPreOrder(searchText);
                tblPayment.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(preOrderDto -> new PreOrderTm(
                                        preOrderDto.getPreOrderId(),
                                        preOrderDto.getUserId(),
                                        preOrderDto.getItemId(),
                                        preOrderDto.getAdvance()
                                )).toList()
                ));
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search pre orders").show();
            }
        }
    }

    public void goToDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }
    private void loadUserId() throws SQLException {
        comboUserId.setItems(FXCollections.observableArrayList(preOrderModel.getAllUserId()));
    }
    private void loadItemDetils() throws SQLException {
        comboItemId.setItems(FXCollections.observableArrayList(preOrderModel.getAllItemDetails()));
    }
}
