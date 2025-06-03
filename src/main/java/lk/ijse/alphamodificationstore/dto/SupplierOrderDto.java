package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SupplierOrderDto {
    private String supplyOrderId;
    private String supplierId;
    private String userId;
    private String date;
    private String itemId;
}
