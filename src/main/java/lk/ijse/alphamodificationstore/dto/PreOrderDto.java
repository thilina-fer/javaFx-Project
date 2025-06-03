package lk.ijse.alphamodificationstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class PreOrderDto {
    private String preOrderId;
    private String userId;
    private String itemId;
    private double advance;
}
