module lk.ijse.alphamodificationstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.naming;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires net.sf.jasperreports.core;
    requires java.desktop;


    opens lk.ijse.alphamodificationstore.controller to javafx.fxml;
    opens lk.ijse.alphamodificationstore.dto.Tm to javafx.base;
    opens lk.ijse.alphamodificationstore.dto to javafx.base;

    exports lk.ijse.alphamodificationstore;
}