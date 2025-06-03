package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EmployeeDto {
    private String employeeId;
    private String employeeName;
    private String employeeContact;
    private String employeeAddress;
    private String employeeNic;
    private int employeeAge;
    private double salary;
}
