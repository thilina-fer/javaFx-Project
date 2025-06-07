package lk.ijse.alphamodificationstore.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alphamodificationstore.db.DBConnection;
import lk.ijse.alphamodificationstore.dto.ItemDto;
import lk.ijse.alphamodificationstore.dto.SupOrderCartDto;
import lk.ijse.alphamodificationstore.dto.SupplierOrderDto;
import lk.ijse.alphamodificationstore.dto.Tm.SupOrderCartTm;
import lk.ijse.alphamodificationstore.model.ItemModel;
import lk.ijse.alphamodificationstore.model.SupOrderModel;
import lk.ijse.alphamodificationstore.model.SupplierModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupplyOrderPageController implements Initializable {

    public Label lblOrderId;
    public Label orderDate;
    public ComboBox cmbSupplierId;
    public Label lblSupplierName;
    public ComboBox cmbItemId;
    public Label lblItemName;
    public TextField txtAddToCartQty;
    public Label lblItemQty;
    public Label lblItemPrice;

    public TableView<SupOrderCartTm> tblOrder;
    public TableColumn<SupOrderCartTm , String > colSupId;
    public TableColumn<SupOrderCartTm , String > colItemId;
    public TableColumn<SupOrderCartTm , String > colItemName;
    public TableColumn<SupOrderCartTm , Integer > colQty;
    public TableColumn<SupOrderCartTm , Double > colPrice;
    public TableColumn<SupOrderCartTm , Double > colTotal;
    public TableColumn<? , ? > colAction;

    private final SupOrderModel supOrderModel = new SupOrderModel();
    private final SupplierModel supplierModel = new SupplierModel();
    private final ItemModel itemModel = new ItemModel();

    private final ObservableList<SupOrderCartTm> supCartData = FXCollections.observableArrayList();
    public AnchorPane ancDashboard;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValues();

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Data.").show();
        }
    }



    private void setCellValues() {
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrder.setItems(supCartData);
    }

    private void refreshPage() throws SQLException, ClassNotFoundException {

        lblOrderId.setText(supOrderModel.getSupOrderID());
        orderDate.setText(LocalDate.now().toString());
        loadSupplierIds();
        loadItemIds();
    }

    private void loadSupplierIds() throws SQLException {
        ArrayList<String> customerContactList = supplierModel.getSupplierIds();
        cmbSupplierId.setItems(FXCollections.observableArrayList(customerContactList));
    }

    private void loadItemIds() throws SQLException {
        ArrayList<String> itemIdsList = itemModel.getAllItemIds();
        cmbItemId.setItems(FXCollections.observableArrayList(itemIdsList));
    }

    public void btnAddToCartOnAction(ActionEvent event) {

        String selectedItemId = (String) cmbItemId.getValue();
        String cartQtyString = txtAddToCartQty.getText();
        String supplierId = cmbSupplierId.getValue().toString();

        if (selectedItemId == null || cartQtyString == null || !cartQtyString.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Please select an item and enter a valid quantity.").show();
            return;
        }

        int supCartQty = Integer.parseInt(cartQtyString);
        int stockQty = Integer.parseInt(lblItemQty.getText());

/*        if (stockQty < 2){
            new Alert(Alert.AlertType.ERROR, "Already have this item on stock").show();
            return;
        }*/
        String itemName = lblItemName.getText();
        double unitPrice = Double.parseDouble(lblItemPrice.getText());
        double total = supCartQty * unitPrice;

        for (SupOrderCartTm supOrderCartTm : supCartData) {
            if (supOrderCartTm.getItemId().equals(selectedItemId)) {
                int newQty = supOrderCartTm.getCartQty() + supCartQty;
                if (newQty < stockQty) {
                    new Alert(Alert.AlertType.ERROR, "stock available.").show();
                    return;
                }
                supOrderCartTm.setCartQty(newQty);
                supOrderCartTm.setTotal(newQty * unitPrice);
                tblOrder.refresh();
                txtAddToCartQty.clear();
                return;
            }
        }

        Button btnRemove = new Button("Remove");
        SupOrderCartTm tm = new SupOrderCartTm(supplierId, selectedItemId, itemName, supCartQty, unitPrice, total, btnRemove);

        btnRemove.setOnAction(e-> {;
            supCartData.remove(tm);
            tblOrder.refresh();
        });
        supCartData.add(tm);
        txtAddToCartQty.clear();
    }
/*
    public void btnPlaceOrderOnAAction(ActionEvent event) {

        if (supCartData.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Cart is Empty" ).show();
            return;
        }
        String orderId = lblOrderId.getText();
        String supplierId = (String) cmbSupplierId.getValue();
        String date = orderDate.getText();

        ArrayList<SupOrderCartDto> cartList = new ArrayList<>();
        for (SupOrderCartTm tm : supCartData){
            SupOrderCartDto dto = new SupOrderCartDto(
                    orderId,
                    tm.getSupplierId(),
                    tm.getItemId(),
                    tm.getItemName(),
                    tm.getCartQty(),
                    tm.getUnitPrice(),
                    tm.getTotal(),
                    tm.getBtnRemove()
            );
            cartList.add(dto);
        }

        SupplierOrderDto supOrderDto = new SupplierOrderDto(orderId , supplierId , date);

        try {
            boolean isPlaced = supOrderModel.placeOrder(supOrderDto, cartList);
            if (isPlaced) {
                supCartData.clear();
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Order Placed").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Order Not Placed").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error").show();
        }
    }*/
    public void cmbSupplierOnAction(ActionEvent event) throws SQLException {
        String id = (String) cmbSupplierId.getValue();
        String name = supplierModel.findSupplierById(id).getSupplierName();
        lblSupplierName.setText(name);
    }


    public void cmbItemOnAction(ActionEvent event) throws SQLException {
        String itemId = (String) cmbItemId.getValue();
        ItemDto itemDto = itemModel.findById(itemId);
        if (itemDto != null) {
            lblItemName.setText(itemDto.getItemName());
            lblItemQty.setText(String.valueOf(itemDto.getQuantity()));
            lblItemPrice.setText(String.valueOf(itemDto.getSellPrice()));
        } else {
            lblItemName.setText("");
            lblItemQty.setText("");
            lblItemPrice.setText("");
        }
    }



    public void OnClickTable(MouseEvent mouseEvent) {
    }

    public void btnResetOnAction(ActionEvent event) {
        supCartData.clear();
        tblOrder.refresh();
    }



    public void gotoDashboard(MouseEvent mouseEvent) {
        navigateTo("/view/DashboardPage.fxml");
    }

    private void navigateTo(String path) {
        try {
            ancDashboard.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancDashboard.widthProperty());
            anchorPane.prefHeightProperty().bind(ancDashboard.heightProperty());

            ancDashboard.getChildren().add(anchorPane);

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent event) {

        Connection connection= null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String supplyOrderId = lblOrderId.getText();
            String itemId = (String) cmbItemId.getValue();
            String date = orderDate.getText();
            if (supCartData.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Cart is Empty").show();
                return;
            }

            boolean allItemsSaved = true;
            for( SupOrderCartTm cart : supCartData ){
                boolean isSupplierOrdersSaved = supplierModel.saveNewRow(
                        supplyOrderId,
                        cart.getSupplierId(),
                        date
                );
                if (!isSupplierOrdersSaved){
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Fail to save supplier order").show();
                }
                boolean isItemSaved = supOrderModel.saveNewOrderItem(
                        supplyOrderId,
                        cart.getItemId(),
                        cart.getCartQty(),
                        cart.getUnitPrice()
                );
                if (!isItemSaved) {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Fail to save item order").show();
                }
                boolean isItemUpdated = itemModel.updateItemQty(
                        cart.getItemId(),
                        cart.getCartQty()
                );
                if (!isItemUpdated) {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Fail to update item order").show();
                }

            }
            if (!allItemsSaved) {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Fail to save supply order or update inventory ").show();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to place order.").show();
        }finally {
            try {
                if (connection != null) {
                    connection.commit();
                    connection.setAutoCommit(true);
                    new Alert(Alert.AlertType.INFORMATION, "Order Placed").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to commit transaction.").show();
            }
        }
    }
}
