package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.CartDto;
import lk.ijse.alphamodificationstore.dto.ItemDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {
    public boolean saveItem(ItemDto itemDto) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute(
                "INSERT INTO Item VALUES(?,?,?,?,?)",
                itemDto.getItemId(),
                itemDto.getItemName(),
                itemDto.getQuantity(),
                itemDto.getBuyPrice(),
                itemDto.getSellPrice()
        );
    }

    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Item SET item_name = ? , quantity = ? , buying_price = ? , selling_price = ? WHERE item_id = ?",
                itemDto.getItemName(),
                itemDto.getQuantity(),
                itemDto.getBuyPrice(),
                itemDto.getSellPrice(),
                itemDto.getItemId()
        );
    }

    public boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM item WHERE item_id ? ",
                itemId);
    }

    public ArrayList<ItemDto> getAllItem() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<ItemDto> itemDtoArrayList = new ArrayList<>();
        while (resultSet.next()){
            ItemDto itemDto = new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            );
            itemDtoArrayList.add(itemDto);
        }
        return itemDtoArrayList;

    }
    public String getNextItemId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item ORDER BY item_id DESC LIMIT 1");
        char tableChartacter = 'I';

        if(resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChartacter + "%03d" , nextIdNumber);

            return nextIdString;
        }
        return tableChartacter + "001";
    }
    /*public ArrayList<ItemDto> getItemDetailsFromName(String itemName) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM item WHERE item_name = ?", itemName);
        ArrayList<ItemDto> dtos = new ArrayList<>();
        if (rst.next()) {
            dtos.add(new ItemDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4),
                    rst.getDouble(5)
            ));
        }
        return dtos;
    }*/

    public ArrayList<ItemDto> searchItem(String searchText) throws SQLException {
        ArrayList<ItemDto> dtos = new ArrayList<>();
        String sql = "SELECT * FROM item WHERE item_id LIKE ? OR item_name LIKE ? OR quantity LIKE ? OR buying_price LIKE ? OR selling_price LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql , pattern , pattern , pattern , pattern , pattern);

        while (resultSet.next()){
            ItemDto itemDto = new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            );
            dtos.add(itemDto);
        }
        return dtos;
    }

    public boolean reduceQty(CartDto cartDto) throws SQLException {
        return CrudUtil.execute("UPDATE item SET quantity = quantity - ? WHERE item_id = ?",
                cartDto.getQuantity(),
                cartDto.getItemId()
        );
    }
    public ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT item_id FROM item");
        ArrayList<String> itemIds = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            itemIds.add(id);
        }
        return itemIds;
    }
    public ItemDto findById(String selectedItemId) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM item WHERE item_id = ?",
                selectedItemId
        );
        if (resultSet.next()) {
            return new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            );
        }
        return null;
    }
}
