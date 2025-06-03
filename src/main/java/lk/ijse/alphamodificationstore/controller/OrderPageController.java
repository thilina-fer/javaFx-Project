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
import lk.ijse.alphamodificationstore.dto.CartDto;
import lk.ijse.alphamodificationstore.dto.ItemDto;
import lk.ijse.alphamodificationstore.dto.OrderDto;
import lk.ijse.alphamodificationstore.dto.Tm.CartTm;
import lk.ijse.alphamodificationstore.model.CustomerModel;
import lk.ijse.alphamodificationstore.model.ItemModel;
import lk.ijse.alphamodificationstore.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    public Label lblOrderId;
    public Label orderDate;
    public ComboBox<String> cmbCustomerContact;
    public Label lblCustomerName;
    public ComboBox<String> cmbItemId;
    public Label lblItemName;
    public TextField txtAddToCartQty;
    public Label lblItemQty;
    public Label lblItemPrice;

    public TableView<CartTm> tblOrder;
    public TableColumn<CartTm, String> colContact;
    public TableColumn<CartTm, String> colItemId;
    public TableColumn<CartTm, String> colItemName;
    public TableColumn<CartTm, Integer> colQty;
    public TableColumn<CartTm, Double> colPrice;
    public TableColumn<CartTm, Double> colTotal;
    public TableColumn<?, ?> colAction;

    private final OrderModel orderModel = new OrderModel();
    private final CustomerModel customerModel = new CustomerModel();
    private final ItemModel itemModel = new ItemModel();

    private final ObservableList<CartTm> cartData = FXCollections.observableArrayList();
    public AnchorPane ancDashboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data....").show();
        }
    }

    private void setCellValues() {
        colContact.setCellValueFactory(new PropertyValueFactory<>("customerContact"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrder.setItems(cartData);
    }

    private void refreshPage() throws Exception {
        lblOrderId.setText(orderModel.getNextOrderId());
        orderDate.setText(LocalDate.now().toString());
        loadCustomerContacts();
        loadItemIds();
    }

    private void loadItemIds() throws SQLException {
        ArrayList<String> itemIdsList = itemModel.getAllItemIds();
        cmbItemId.setItems(FXCollections.observableArrayList(itemIdsList));
    }

    private void loadCustomerContacts() throws SQLException {
        ArrayList<String> customerContactList = customerModel.getAllCustomerContact();
        cmbCustomerContact.setItems(FXCollections.observableArrayList(customerContactList));
    }

    public void btnAddToCartOnAction(ActionEvent event) {
        String selectedItemId = cmbItemId.getValue();
        String cartQtyString = txtAddToCartQty.getText();
        String customerContact = cmbCustomerContact.getValue();

        if (selectedItemId == null || cartQtyString == null || !cartQtyString.matches("\\d+")) {
            new Alert(Alert.AlertType.WARNING, "Select item and enter valid quantity").show();
            return;
        }

        int cartQty = Integer.parseInt(cartQtyString);
        int stockQty = Integer.parseInt(lblItemQty.getText());

        if (stockQty < cartQty) {
            new Alert(Alert.AlertType.WARNING, "Insufficient stock").show();
            return;
        }

        String itemName = lblItemName.getText();
        double unitPrice = Double.parseDouble(lblItemPrice.getText());
        double total = cartQty * unitPrice;

        for (CartTm cartTm : cartData) {
            if (cartTm.getItemId().equals(selectedItemId)) {
                int newQty = cartTm.getCartQty() + cartQty;
                if (newQty > stockQty) {
                    new Alert(Alert.AlertType.WARNING, "Not enough stock").show();
                    return;
                }
                cartTm.setCartQty(newQty);
                cartTm.setTotal(newQty * unitPrice);
                tblOrder.refresh();
                txtAddToCartQty.clear();
                return;
            }
        }

        Button btnRemove = new Button("Remove");
        CartTm tm = new CartTm(customerContact, selectedItemId, itemName, cartQty, unitPrice, total, btnRemove);

        btnRemove.setOnAction(e -> {
            cartData.remove(tm);
            tblOrder.refresh();
        });

        cartData.add(tm);
        txtAddToCartQty.clear();
    }

    public void btnPlaceOrderOnAAction(ActionEvent event) {
        if (cartData.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").show();
            return;
        }

        String orderId = lblOrderId.getText();
        String customerContact = cmbCustomerContact.getValue();
        String date = orderDate.getText();

        ArrayList<CartDto> cartList = new ArrayList<>();
        for (CartTm tm : cartData) {
            CartDto dto = new CartDto(
                    orderId,
                    tm.getCustomerContact(),
                    tm.getItemId(),
                    tm.getItemName(),
                    tm.getCartQty(),
                    tm.getUnitPrice(),
                    tm.getTotal(),
                    tm.getBtnRemove()
            );
            cartList.add(dto);
        }

        OrderDto orderDto = new OrderDto(orderId, customerContact, date);

        try {
            boolean isPlaced = orderModel.placeOrder(orderDto, cartList);
            if (isPlaced) {
                cartData.clear();
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Order placed successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to place order").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while placing order").show();
        }
    }

    public void cmbCustomerOnAction(ActionEvent event) throws SQLException {
        String contact = cmbCustomerContact.getValue();
        String name = String.valueOf(customerModel.findByContacts(contact));
        lblCustomerName.setText(customerModel.getCustomerNameByContact(contact));
    }

    public void cmbItemOnAction(ActionEvent event) throws SQLException {
        String itemId = cmbItemId.getValue();
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

    public void btnDeleteOnAction(ActionEvent event) {
        // implement if needed
    }

    public void OnClickTable(MouseEvent mouseEvent) {
        // implement if needed
    }

    public void btnResetOnAction(ActionEvent event) {
        cartData.clear();
        tblOrder.refresh();
    }

    public void gotoDashboard(MouseEvent mouseEvent) {
        // implement navigation
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
}
