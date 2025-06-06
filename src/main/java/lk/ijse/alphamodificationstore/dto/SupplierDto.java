package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SupplierDto {
    private String supplierId;
    private String supplierName;
    private String supplierContact;
    private String supplierAddress;
}
