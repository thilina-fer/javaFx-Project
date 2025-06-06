package lk.ijse.alphamodificationstore.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.scene.control.Alert;

import java.util.Properties;

public class SendMail {
    private static final String MY_ACCOUNT_EMAIL = "dilshanfernando20031010@gmail.com";
    private static final String PASSWORD = "nggb jrvz ghwr jmeh";

    public boolean sendEmails(String recipient, int otp) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MY_ACCOUNT_EMAIL, PASSWORD);
                }
            });

            Message message = prepareMessage(session, recipient, otp);
            if (message != null) {
                Transport.send(message);
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to prepare message").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send mail").show();
            return false;
        }
        return false;
    }

    private static Message prepareMessage(Session session, String recipient, int otp) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MY_ACCOUNT_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Your OTP");
            message.setText("Your OTP is " + otp);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Connection Error").show();
        }
        return null;
    }
}