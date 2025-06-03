package lk.ijse.alphamodificationstore.dto;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class CartDto {
    private String orderId;
    private String customerContact;
    private String itemId;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private Button btnRemove;
    public CartDto(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

}
