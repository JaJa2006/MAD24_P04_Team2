package com.example.main_activity;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.util.Log;

public class GmailSender {

    private String username; // Email address of the sender
    private String password; // Password or app-specific password for the sender's email

    public GmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //send the email
    public void sendMail(String fromEmail, String toEmail, String subject, String text) {
        // Set up email server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Use TLS for security
        props.put("mail.smtp.host", "smtp.gmail.com"); // Gmail SMTP server
        props.put("mail.smtp.port", "587"); // Port for TLS

        // Create a session with the email server
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password); // Authenticate with email and password
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail)); // Set the sender's email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Set the recipient's email address
            message.setSubject(subject); // Set the subject of the email
            message.setText(text); // Set the body of the email

            // Send the email
            Transport.send(message);
            Log.d("Email", "Email sent successfully."); // Log success message
        } catch (MessagingException e) {
            // Log any errors that occur while sending the email
            Log.d("Email", "Error occurred while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
