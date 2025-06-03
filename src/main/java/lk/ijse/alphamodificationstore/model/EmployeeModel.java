package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.dto.EmployeeDto;
import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {
    public boolean saveEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute(
                "INSERT INTO employee VALUES(?,?,?,?,?,?,?)",
                employeeDto.getEmployeeId(),
                employeeDto.getEmployeeName(),
                employeeDto.getEmployeeContact(),
                employeeDto.getEmployeeAddress(),
                employeeDto.getEmployeeNic(),
                employeeDto.getEmployeeAge(),
                employeeDto.getSalary()
        );
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute(
                "UPDATE employee SET emp_id = ? , emp_name = ? , emp_contact = ? , emp_address = ? , emp_age = ? , salary = ? WHERE emp_nic = ?",
                employeeDto.getEmployeeId(),
                employeeDto.getEmployeeName(),
                employeeDto.getEmployeeContact(),
                employeeDto.getEmployeeAddress(),
                employeeDto.getEmployeeAge(),
                employeeDto.getSalary(),
                employeeDto.getEmployeeNic()

        );
    }

    public boolean deleteEmployee(String empNic) throws ClassNotFoundException, SQLException {
        return CrudUtil.execute("DELETE FROM employee WHERE emp_nic = ?",
                empNic);
    }

    public ArrayList<EmployeeDto> getAllEmployee() throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        ArrayList<EmployeeDto> employeeDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getDouble(7)
            );
            employeeDtoArrayList.add(employeeDto);
        }
        return employeeDtoArrayList;
    }

    public String getNextEmployeeId() throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT emp_id FROM employee ORDER BY emp_id DESC LIMIT 1");
        char tableChartacter = 'E';

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChartacter + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChartacter + "001";
    }

    /* public ArrayList<EmployeeDto> getEmployeeDetailsFromContact(String phoneNum) throws SQLException {
         ResultSet rst = CrudUtil.execute("SELECT * FROM employee WHERE emp_contact = ? OR emp_nic", phoneNum);
         ArrayList<EmployeeDto> dtos = new ArrayList<>();
         if (rst.next()) {
             dtos.add(new EmployeeDto(
                     rst.getString(1),
                     rst.getString(2),
                     rst.getString(3),
                     rst.getString(4),
                     rst.getString(5),
                     rst.getInt(6),
                     rst.getDouble(7)
                     ));
         }
         return dtos;
     }*/
    public ArrayList<EmployeeDto> searchEmployee(String searchText) throws SQLException {
        ArrayList<EmployeeDto> dtos = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE emp_id LIKE ? OR emp_name LIKE ? OR emp_contact LIKE ? OR emp_address LIKE ? OR emp_age LIKE ? OR salary LIKE ?";
        String pattern = "%" + searchText + "%";
        ResultSet resultSet = CrudUtil.execute(sql, pattern, pattern, pattern, pattern, pattern, pattern);

        while (resultSet.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getDouble(7)
            );
            dtos.add(employeeDto);
        }
        return dtos;
    }
}
