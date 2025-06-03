package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.PreOrderDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PreOrderModel {
    public boolean savePreOrder(PreOrderDto preOrderDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO pre_order_manage VALUES(?,?,?,?)",
                preOrderDto.getPreOrderId(),
                preOrderDto.getUserId(),
                preOrderDto.getItemId(),
                preOrderDto.getAdvance()
        );
    }
    public boolean updatePreOrder(PreOrderDto preOrderDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE pre_order_manage SET user_id = ? , item_id = ? , advance_payment = ? WHERE  pre_order_id = ? ",
                preOrderDto.getUserId(),
                preOrderDto.getItemId(),
                preOrderDto.getAdvance(),
                preOrderDto.getPreOrderId()
        );
    }
    public boolean deletePreOrder(String preOrderId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE Pre_Order_Manage WHERE pre_order_id =  ? ",
                preOrderId);
    }
    public ArrayList<PreOrderDto> getAllPreOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM pre_order_manage");
        ArrayList<PreOrderDto> dtos = new ArrayList<>();
        while (resultSet.next()) {
            PreOrderDto dto = new PreOrderDto(
                    resultSet.getString("pre_order_id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("item_id"),
                    resultSet.getDouble("advance_payment")
            );
            dtos.add(dto);
        }
        return dtos;
    }
    public String getNextPreOId() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT pre_order_id FROM pre_order_manage ORDER BY pre_order_id DESC LIMIT 1");
        String tableChartacter = "PR";

        if(resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(tableChartacter.length());
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChartacter + "%03d" , nextIdNumber);

            return nextIdString;
        }
        return tableChartacter + "001";
    }

    public ArrayList<PreOrderDto> searchPreOrder(String searchText) throws SQLException {
        ArrayList<PreOrderDto> dtos = new ArrayList<>();
        String sql = "SELECT * FROM pre_order_manage WHERE pre_order_id LIKE ? OR user_id LIKE ? OR item_id LIKE ? OR advance_payment LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql , pattern , pattern , pattern , pattern);

        while (resultSet.next()) {
            PreOrderDto dto = new PreOrderDto(
                    resultSet.getString("pre_order_id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("item_id"),
                    resultSet.getDouble("advance_payment")
            );
            dtos.add(dto);
        }
        return dtos;
    }
    public ArrayList<String> getAllUserId() throws SQLException {
        ArrayList<String> userIds = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT user_id FROM user");

        while (resultSet.next()) {
            userIds.add(resultSet.getString("user_id"));
        }
        return userIds;
    }
    public ArrayList<String> getAllItemDetails() throws SQLException {
        ArrayList<String> itemDetails = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT item_id , item_name , quantity , buying_price , " +
                "selling_price FROM item");

        while (resultSet.next()){
            String details =  resultSet.getString("item_id") + "\n" +
                    resultSet.getString("item_name") + "\n" +
                    resultSet.getInt("quantity") + "\n" +
                    resultSet.getDouble("buying_price") + "\n" +
                    resultSet.getDouble("selling_price") + "\n\n";

            itemDetails.add(details);
        }
        return itemDetails;
    }
}
