package lk.ijse.alphamodificationstore.dto.Tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CartTm {
    private String customerContact;
    private String itemId;
    private String itemName;
    private int cartQty;
    private double unitPrice;
    private double total;
    private Button btnRemove;
}
