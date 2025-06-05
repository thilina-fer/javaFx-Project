package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.SupOrderCartDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupOrderDetailsModel {
    private final ItemModel itemModel = new ItemModel();

    public boolean saveSupOrderDetails(String orderId , ArrayList<SupOrderCartDto> supCartList) throws SQLException {
        for (SupOrderCartDto supOrderCartDto : supCartList){
            boolean isSupOrderDetailsSaved = saveSupOrderDetail(orderId, supOrderCartDto);
            if (!isSupOrderDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemModel.reduceQtyNew(supOrderCartDto);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    private boolean saveSupOrderDetail(String orderId, SupOrderCartDto supOrderCartDto) throws SQLException {
        return CrudUtil.execute("INSER INTO sup_order_details (SUP_order_id, item_id, qty, unit_price) VALUES (?, ?, ?, ?)",
                orderId,
                supOrderCartDto.getItemId(),
                supOrderCartDto.getQuantity(),
                supOrderCartDto.getUnitPrice()
                );
    }
    public static String getNextSupOrderId() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT sup_order_id FROM supplier_order ORDER BY sup_order_id DESC LIMIT 1");
        String  tableString = "SO";

        if(resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(tableString.length());
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableString + "%03d" , nextIdNumber);

            return nextIdString;
        }
        return tableString + "001";
    }
}
