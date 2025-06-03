package lk.ijse.alphamodificationstore.dto.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTm {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private String contact;
    private String address;
    private String role;
}
