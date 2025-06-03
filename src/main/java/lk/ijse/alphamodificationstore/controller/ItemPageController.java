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
import lk.ijse.alphamodificationstore.dto.ItemDto;
import lk.ijse.alphamodificationstore.dto.Tm.ItemTm;
import lk.ijse.alphamodificationstore.model.ItemModel;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ItemPageController implements Initializable {

    public Label lblItemId;
    public TextField txtName;
    public TextField txtQuantity;
    public TextField txtBuyingPrice;
    public TextField txtSellingPrice;


    public TableView<ItemTm> tblItem;
    public TableColumn<ItemTm, String> colId;
    public TableColumn<ItemTm, String> colName;
    public TableColumn<ItemTm, Integer> colQuantity;
    public TableColumn<ItemTm, Double> colBuyPrice;
    public TableColumn<ItemTm, Double> colSellPrice;

    private final ItemModel itemModel = new ItemModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private final String quantityPattern = "^\\d+$";
    private final String buyingPricePattern = "^\\d+(\\.\\d{1,2})?$";
    private final String sellingPricePattern = "^\\d+(\\.\\d{1,2})?$";
    public AnchorPane ancItemPage;
    public TextField searchField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("BuyPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("SellPrice"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblItem.setItems(FXCollections.observableArrayList(
                itemModel.getAllItem().stream()
                        .map(itemDto -> new ItemTm(
                                itemDto.getItemId(),
                                itemDto.getItemName(),
                                itemDto.getQuantity(),
                                itemDto.getBuyPrice(),
                                itemDto.getSellPrice()
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

            txtName.setText(null);
            txtQuantity.setText(null);
            txtBuyingPrice.setText(null);
            txtSellingPrice.setText(null);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String itemId = lblItemId.getText();
        String itemName = txtName.getText();
        String qty = txtQuantity.getText();
        String buyingPrice = txtBuyingPrice.getText();
        String sellingPrice = txtSellingPrice.getText();

        boolean isValidQuantity = qty.matches(quantityPattern);
        boolean isValidBuyPrice = buyingPrice.matches(buyingPricePattern);
        boolean isValidSellPrice = sellingPrice.matches(sellingPricePattern);

        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtQuantity.setStyle(txtQuantity.getStyle() + ";-fx-border-color: #7367F0");
        txtBuyingPrice.setStyle(txtBuyingPrice.getStyle() + ";-fx-border-color: #7367F0");
        txtSellingPrice.setStyle(txtSellingPrice.getStyle() + ";-fx-border-color: #7367F0");

        if (!isValidQuantity) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidBuyPrice) txtBuyingPrice.setStyle(txtBuyingPrice.getStyle() + ";-fx-border-color: red;");
        if (!isValidSellPrice) txtSellingPrice.setStyle(txtSellingPrice.getStyle() + ";-fx-border-color: red;");

        int presedQuantity = Integer.parseInt(qty);
        double presedBuyPrice = Double.parseDouble(buyingPrice);
        double presedSellPrice = Double.parseDouble(sellingPrice);

        ItemDto itemDto = new ItemDto(
                itemId,
                itemName,
                presedQuantity,
                presedBuyPrice,
                presedSellPrice
        );

        if (isValidQuantity && isValidBuyPrice && isValidSellPrice) {
            try {
                boolean isSaved = itemModel.saveItem(itemDto);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Item Saved Successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String itemId = lblItemId.getText();
        String itemName = txtName.getText();
        String qty = txtQuantity.getText();
        String buyingPrice = txtBuyingPrice.getText();
        String sellingPrice = txtSellingPrice.getText();

        int presedQuantity = Integer.parseInt(qty);
        double presedBuyPrice = Double.parseDouble(buyingPrice);
        double presedSellPrice = Double.parseDouble(sellingPrice);

        ItemDto itemDto = new ItemDto(
                itemId,
                itemName,
                presedQuantity,
                presedBuyPrice,
                presedSellPrice
        );

        try {
            boolean isUpdated = itemModel.updateItem(itemDto);

            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Item Updated Successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failedd").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure ? ",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String itemId = lblItemId.getText();
            try {
                boolean isDeleted = itemModel.deleteItem(itemId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Item Deleted Successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Delete Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void loadNextId() throws ClassNotFoundException, SQLException {
        String nextId = itemModel.getNextItemId();
        lblItemId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ItemTm selectedItem = tblItem.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblItemId.setText(selectedItem.getItemId());
            txtName.setText(selectedItem.getItemName());
            txtQuantity.setText(String.valueOf(selectedItem.getQuantity()));
            txtBuyingPrice.setText(String.valueOf(selectedItem.getBuyPrice()));
            txtSellingPrice.setText(String.valueOf(selectedItem.getSellPrice()));

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
            ancItemPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancItemPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancItemPage.heightProperty());

            ancItemPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void search(KeyEvent keyEvent) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            try {
                loadTableData();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Faild to load Suppliers").show();
            }
        } else {
            try {
                ArrayList<ItemDto> customerList = itemModel.searchItem(searchText);
                tblItem.setItems(FXCollections.observableArrayList(
                        customerList.stream()
                                .map(itemDto -> new ItemTm(
                                        itemDto.getItemId(),
                                        itemDto.getItemName(),
                                        itemDto.getQuantity(),
                                        itemDto.getBuyPrice(),
                                        itemDto.getSellPrice()
                                )).toList()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to search items").show();
            }
        }
    }
    /*private void loadSearchResults(String phoneNum) {
        try {
            ArrayList<ItemDto> contacts = itemModel.getItemDetailsFromName(phoneNum);
            if (contacts == null) {
                tblItem.setItems(FXCollections.observableArrayList(new ArrayList<>()));
            } else {
               tblItem.setItems(FXCollections.observableArrayList(
                       itemModel.getItemDetailsFromName(phoneNum).stream()
                                .map(itemDto -> new ItemTm(
                                        itemDto.getItemId(),
                                        itemDto.getItemName(),
                                        itemDto.getQuantity(),
                                        itemDto.getBuyPrice(),
                                        itemDto.getSellPrice()
                                )).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error when display results").show();
        }
    }*/

    public ArrayList<String> getAllItemId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item");
        ArrayList<String> itemIdList = new ArrayList<>();
        while (resultSet.next()) {
            String itemId = resultSet.getString(1);
            itemIdList.add(itemId);
        }
        return itemIdList;
    }

    public String finadNameById(String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT item_name FROM item WHERE item_id = ?",
                selectedItemId
        );
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public void btnReportOnAction(ActionEvent event) {

    }


   /* public void btnReportOnAction(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/Report/Item.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> params = new HashMap<>();
            params.put("P_Date", LocalDate.now().toString());
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, connection);

            JasperViewer.viewReport(jasperPrint, false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


}
