package lk.ijse.alphamodificationstore.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CustomerTm {
    private String customerId;
    private String customerName;
    private String customerContact;
    private String customerAddress;

}
