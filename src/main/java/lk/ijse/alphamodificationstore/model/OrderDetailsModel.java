package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.CartDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsModel {
    private final ItemModel itemModel = new ItemModel();

    public boolean saveOrderDetailList(String orderId, ArrayList<CartDto> cartList) throws SQLException {
        for (CartDto cartDto : cartList) {
            boolean isOrderDetailsSaved = saveOrderDetail(orderId, cartDto);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemModel.reduceQty(cartDto);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetail(String orderId, CartDto cartDto) throws SQLException {
        return CrudUtil.execute(
                "INSERT INTO order_details (order_id, item_id, qty, unit_price) VALUES (?, ?, ?, ?)",
                orderId,
                cartDto.getItemId(),
                cartDto.getQuantity(),
                cartDto.getUnitPrice()
        );

    }

    public static String generateNextOrderId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        if (rs.next()) {
            String lastId = rs.getString(1);
            int id = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("O%03d", id);
        } else {
            return "O001";
        }
    }
}
