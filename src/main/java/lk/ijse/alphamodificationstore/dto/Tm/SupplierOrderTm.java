package lk.ijse.alphamodificationstore.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierOrderTm {
    private String supplyOrderId;
    private String supplierId;
    private String userId;
    private String date;
    private String itemId;
}
