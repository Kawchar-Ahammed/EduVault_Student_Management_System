package util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Enhanced email service for sending various types of academic communications
 */
public class EmailService {
    
    // // Email configuration
    // private static final String SMTP_HOST = "smtp.gmail.com";
    // private static final String SMTP_PORT = "587";
    // private static final String EMAIL_USERNAME = "your-email@gmail.com"; // Configure this
    // private static final String EMAIL_PASSWORD = "your-app-password"; // Configure this
    // private static final String FROM_EMAIL = "noreply@pushpo.edu";
    // private static final String FROM_NAME = "Pushpo Academic System";


    private static final String FROM_EMAIL = "zakanakatemp@gmail.com"; // Change this to your email
    private static final String EMAIL_PASSWORD = "gvptforryxxsmeif"; // Use App Password for Gmail
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_NAME = "EduVault Student Management System";
    private static final String EMAIL_USERNAME = FROM_EMAIL;


    
    
    /**
     * Send email using JavaMail API
     */
    public boolean sendEmail(String toEmail, String subject, String body) {
        try {
            // Setup mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", SMTP_HOST);
            
            // Create authentication
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            };
            
            // Create session
            Session session = Session.getInstance(props, auth);
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            // Send message
            Transport.send(message);
            
            System.out.println("Email sent successfully to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
            return false;
        }
    }

    
    /**
     * Validate email address format
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}