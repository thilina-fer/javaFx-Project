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
}
