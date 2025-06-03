package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.db.DBConnection;
import lk.ijse.alphamodificationstore.dto.CartDto;
import lk.ijse.alphamodificationstore.dto.OrderDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderModel {

    private final OrderDetailsModel orderDetailsModel = new OrderDetailsModel();
    private static final Logger logger = Logger.getLogger(OrderModel.class.getName());

    public String getNextOrderId() throws SQLException {
        return OrderDetailsModel.generateNextOrderId();
    }

    public boolean placeOrder(OrderDto orderDto, ArrayList<CartDto> cartList) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute("INSERT INTO orders VALUES(?, ?, ?)",
                    orderDto.getOrderId(),
                    orderDto.getCustomerContact(),
                    orderDto.getDate()
            );

            if (isOrderSaved) {
                boolean isOrderDetailsSaved = orderDetailsModel.saveOrderDetailList(orderDto.getOrderId(), cartList);
                if (isOrderDetailsSaved) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Order Placement Failed", e);
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
