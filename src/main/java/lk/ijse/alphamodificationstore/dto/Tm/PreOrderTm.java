package lk.ijse.alphamodificationstore.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class PreOrderTm {
    private String preOrderId;
    private String userId;
    private String itemId;
    private double advance;
}
