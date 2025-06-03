package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderDto {
    private String orderId;
    private String customerContact;
    private String date;
}
