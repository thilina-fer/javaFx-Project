package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.CustomerDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public boolean saveCustomer(CustomerDto customerDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?)",
                customerDto.getCustomerId(),
                customerDto.getCustomerName(),
                customerDto.getCustomerContact(),
                customerDto.getCustomerAddresss()
        );
    }
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException {
        return CrudUtil.execute("UPDATE customer SET customer_name = ? , customer_contact = ? , customer_address = ? WHERE customer_id = ?",
                customerDto.getCustomerName(),
                customerDto.getCustomerContact(),
                customerDto.getCustomerAddresss(),
                customerDto.getCustomerId()
        );
    }
    public boolean deleteCustomer(String customerId) throws SQLException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?",
                customerId);
    }

    public static ArrayList<CustomerDto> getAllCustomer() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        ArrayList<CustomerDto> dtos = new ArrayList<>();

        while (resultSet.next()) {
            CustomerDto dto = new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            dtos.add(dto);
        }
        return dtos;
    }
    public String getNextCustomerId() throws SQLException , ClassNotFoundException{
        ResultSet resultSet = CrudUtil.execute("SELECT customer_id FROM Customer ORDER BY customer_id DESC LIMIT 1");
        char tableChartacter = 'C';

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
    public ArrayList<CustomerDto> searchCustomer(String searchText) throws SQLException {
        ArrayList<CustomerDto> dtos = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE customer_id LIKE ? OR customer_name LIKE ? OR customer_contact LIKE ? OR customer_address LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql , pattern , pattern , pattern , pattern);

        while (resultSet.next()) {
            CustomerDto dto = new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            dtos.add(dto);
        }
        return dtos;
    }
    public ArrayList<String> getAllCustomerContact() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT customer_contact FROM customer");
        ArrayList<String> contacts = new ArrayList<>();

        while (resultSet.next()) {
            String contact = resultSet.getString(1);
            contacts.add(contact);
        }
        return contacts;
    }

    public CustomerDto findByContacts(String selectedContacts) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer WHERE customer_contact = ?",
                selectedContacts
        );
        if (resultSet.next()) {
            return new CustomerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }

    public String getCustomerNameByContact(String contact) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT customer_name FROM customer WHERE customer_contact = ?", contact);
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
