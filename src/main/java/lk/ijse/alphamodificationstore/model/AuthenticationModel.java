package lk.ijse.alphamodificationstore.model;

import lk.ijse.alphamodificationstore.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationModel {
    public static boolean updatePassword(String password , String email) throws SQLException {
        return CrudUtil.execute("UPDATE user SET password = ? WHERE email = ? " ,
                password ,
                email
        );
    }
    public boolean checkIfEmailExists(String email) throws SQLException {
        ResultSet resultSet = CrudUtil.execute(
                "SELECT email FROM user WHERE email = ? ORDER BY email DESC LIMIT 1",
                email
        );
        if (resultSet.next()){
            return true;
        }
        return false;
    }
}
