package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttendanceDto {
    private String attendanceId;
    private String employeeNic;
    private String date;
    private String attendTime;
    private String duration;
}
