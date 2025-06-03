package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.SupplierOrderDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierOrderModel {
    public boolean saveSuppilerOrder(SupplierOrderDto supplierOrderDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Supplier_Order VALUES(?,?,?,?,?)",
                supplierOrderDto.getSupplyOrderId(),
                supplierOrderDto.getSupplierId(),
                supplierOrderDto.getUserId(),
                supplierOrderDto.getDate(),
                supplierOrderDto.getItemId()
        );
    }
    public boolean updateSuppilerOrder(SupplierOrderDto supplierOrderDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Supplier_Order SET supplier_id = ? , user_id = ? , date = ? , item_id = ? WHERE so_id = ?",
                supplierOrderDto.getSupplierId(),
                supplierOrderDto.getUserId(),
                supplierOrderDto.getDate(),
                supplierOrderDto.getItemId(),
                supplierOrderDto.getSupplyOrderId()
        );
    }
    public boolean deleteSuppilerOrder(String soId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Supplier_Order WHERE so_id = ?",
                soId);
    }
    public ArrayList<SupplierOrderDto> getAllSuppilerOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Supplier_Order");
        ArrayList<SupplierOrderDto> supplierOrderDtoArrayList= new ArrayList<>();
        while (resultSet.next()) {
            SupplierOrderDto dto = new SupplierOrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            supplierOrderDtoArrayList.add(dto);
        }
        return supplierOrderDtoArrayList;
    }
    public String getNextSoId() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT so_id FROM supplier_order ORDER BY so_id DESC LIMIT 1");
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
   /* public ArrayList<SupplierOrderDto> getSupplierOrderDetailsFromSupplierOrderId(String id) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM so_id WHERE so_id = ?", id);
        ArrayList<SupplierOrderDto> dtos = new ArrayList<>();
        if (rst.next()) {
            dtos.add(new SupplierOrderDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return dtos;
    }*/

    public ArrayList<SupplierOrderDto> searchSupplierOrders(String searchText) throws SQLException {
        ArrayList<SupplierOrderDto> dtos = new ArrayList<>();
        String sql = "SELECT * FROM supplier_order WHERE so_id LIKE ? OR supplier_id LIKE ? OR user_id LIKE ? OR date LIKE ? OR item_id LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql , pattern , pattern , pattern , pattern , pattern);

        while (resultSet.next()) {
            SupplierOrderDto dto = new SupplierOrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
            dtos.add(dto);
        }
        return dtos;
    }
    public ArrayList<String> getAllSupplierDetails() throws SQLException {
        ArrayList<String> supplierDetails = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT supplier_id , supplier_name , supplier_contact , supplier_address FROM supplier");

        while (resultSet.next()){
            String details = "supplierId : " + resultSet.getString("supplier_id") + "\n" +
                    "supplierName : " + resultSet.getString("supplier_name") + "\n" +
                    "supplierContact : " + resultSet.getString("supplier_contact") + "\n" +
                    "supplierAddress : " + resultSet.getString("supplier_address") + "\n\n";

            supplierDetails.add(details);
        }
        return supplierDetails;
    }

    public ArrayList<String> getAllUserId() throws SQLException {
        ArrayList<String> userIds = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT user_id FROM user");

        while (resultSet.next()){
            userIds.add(resultSet.getString("user_id"));
        }
        return userIds;
    }
    public ArrayList<String> getAllItemDetails() throws SQLException {
        ArrayList<String> itemDetails = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT item_id , item_name , quantity , buying_price , " +
                "selling_price FROM item");

        while (resultSet.next()){
            String details = "item Id : " + resultSet.getString("item_id") + "\n" +
                    "item Name : " + resultSet.getString("item_name") + "\n" +
                    "quantity : " + resultSet.getInt("quantity") + "\n" +
                    "buying Price : " + resultSet.getDouble("buying_price") + "\n" +
                    "selling Price : " + resultSet.getDouble("selling_price") + "\n\n";

            itemDetails.add(details);
        }
        return itemDetails;
    }
}
