package com.example.main_activity;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

public class Mailsender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    public Mailsender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", mailhost);
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true"); // Enable TLS
        props.setProperty("mail.smtp.ssl.enable", "false"); // Disable SSL

        // Create a session with authentication
        session = Session.getInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    //send the email
    public synchronized void sendMail(String subject, String body, String sender, String recipient) throws Exception {
        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        //set details of email
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        message.setDataHandler(handler);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        // Send the message using Transport class
        Transport.send(message);
    }
}
