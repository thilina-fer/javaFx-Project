package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.db.DBConnection;
import lk.ijse.alphamodificationstore.dto.SupOrderCartDto;
import lk.ijse.alphamodificationstore.dto.SupplierOrderDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupOrderModel {
    private final SupOrderDetailsModel supOrderDetailsModel = new SupOrderDetailsModel();

    public String getSupOrderID() throws SQLException, ClassNotFoundException {
        return SupOrderDetailsModel.getNextSupOrderId();
    }

    public boolean placeOrder(SupplierOrderDto supOrderDto, ArrayList<SupOrderCartDto> cartList) throws SQLException {

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute("INSERT INTO supplier_orders VALUES(?,?,?)",
                    supOrderDto.getOrderId(),
                    supOrderDto.getSupplierId(),
                    supOrderDto.getDate()
                    );

            if (isOrderSaved) {
                boolean isOrderDetailSaved = supOrderDetailsModel.saveSupOrderDetails(supOrderDto.getOrderId() , cartList);
                if (isOrderDetailSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }

    public boolean saveNewOrderItem(String supplyOrderId, String itemId, int cartQty, double unitPrice) {
        try {
            return CrudUtil.execute("INSERT INTO sup_order_details (sup_order_id, item_id, qty, unit_price) VALUES (?, ?, ?, ?)",
                    supplyOrderId,
                    itemId,
                    cartQty,
                    unitPrice
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
/*public void btnPlaceOrderOnAction(ActionEvent event) {
    Connection connection = null;
    try {
        connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        String supplyOrderId = lblOrderId.getText();
        String supplierId = (String) cmbSupplierId.getValue();
        String date = orderDate.getText();

        if (supCartData.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Cart is Empty").show();
            return;
        }

        boolean allSuccess = true;
        for (SupOrderCartTm cartItem : supCartData) {
            // Insert into sup_order_details
            String insertDetailSql = "INSERT INTO sup_order_details (order_id, item_id, qty, unit_price, total) VALUES (?, ?, ?, ?, ?)";
            try (var ps = connection.prepareStatement(insertDetailSql)) {
                ps.setString(1, supplyOrderId);
                ps.setString(2, cartItem.getItemId());
                ps.setInt(3, cartItem.getCartQty());
                ps.setDouble(4, cartItem.getUnitPrice());
                ps.setDouble(5, cartItem.getTotal());
                if (ps.executeUpdate() != 1) {
                    allSuccess = false;
                    break;
                }
            }*/