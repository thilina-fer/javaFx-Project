/*
package lk.ijse.alphamodificationstore.controller;

import com.mysql.cj.Session;
import javafx.scene.control.Alert;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import java.nio.charset.MalformedInputException;
import java.util.Properties;
import java.util.logging.Logger;

public class SendMail {
    public static boolean sendMail(String recepient , int otp){
        try {
            System.out.println("Preparing to send  mail");

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            String myAccountEmail = "dilshanfernando20031010@gmail.com";
            String password = "mgal rlek xdpp xruy";



 Session session = Session.getInstance(properties , new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password.toCharArray());
                }
            });


//            javax.mail.Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(myAccountEmail, password);
//                }
//            });

            Message message = prepareMessage(session , myAccountEmail , recepient , otp);
            if (message != null){
                Transport.send(message);
                System.out.println("Email sent successfully");
                return true;
            }else {
                new Alert(Alert.AlertType.ERROR,"Fail to preapare message");

            }

            }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to send email");
            return false;
        }
        return true;
    }
    private static Message prepareMessage(Session session , String myAccountEmail , String recepient , int otp){
        try {
            Message message = new MalformedInputException(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Your OTP");
            message.setText("Your OTP is " + otp);
            return message;
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to preapare message");
        }
        return null;
    }
}
*/



package lk.ijse.alphamodificationstore.controller;

//import javafx.scene.control.Alert;
//
//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Properties;
//import java.util.logging.Logger;


import jakarta.mail.Authenticator;
import jakarta.mail.Session;

import java.util.Properties;

public class SendMail {
    public static boolean sendMail(String email, int otp) {
      return true;}

    /*private static final Logger logger = Logger.getLogger(SendMail.class.getName());

    public static boolean sendMail(String recepient, int otp) {
        try {
            System.out.println("Preparing to send mail...");

            // Set up properties for email
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Securely configure email and password
            String myAccountEmail = System.getenv("dilshanfernando20031010@gmail.com");
            String password = System.getenv("mgal rlek xdpp xruy");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(myAccountEmail, password);
                }
            });

            // Prepare email message
            Message message = prepareMessage(session, myAccountEmail, recepient, otp);
            if (message != null) {
                Transport.send(message); // Send email
                System.out.println("Email sent successfully.");
                return true;
            } else {
                logger.severe("Failed to prepare the email message.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send email").show();
            return false;
        }
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, int otp) {
        try {
            MimeMessage message = new MimeMessage(session); // Create MimeMessage instance
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Your OTP");
            message.setText("Your OTP is " + otp);
            return message;

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to prepare message").show();
        }
        return null;
    }
}*/
