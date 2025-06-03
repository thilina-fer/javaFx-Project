package lk.ijse.alphamodificationstore.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EmployeeTm {
    private String employeeId;
    private String employeeName;
    private String employeeContact;
    private String employeeAddress;
    private String employeeNic;
    private int employeeAge;
    private double salary;
}
