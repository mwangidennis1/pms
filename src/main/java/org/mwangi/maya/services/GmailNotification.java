package org.mwangi.maya.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mwangi.maya.utility.EmailNotification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class GmailNotification implements EmailNotification {
   private JavaMailSender javaMailSender;

    public GmailNotification(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String recepientEmail, String link) {
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        try {
            helper.setFrom("denmwangi@usiu.ac.ke","MAYA");
            helper.setTo(recepientEmail);
            String subject="Here`s the link to reset your password";
            String content = "<p>Hello,</p>"
                             + "<p>You have requested to reset your password.</p>"
                             + "<p>Click the link below to change your password:</p>"
                             + "<p><a href=\"" + link + "\">Change my password</a></p>"
                             + "<br>"
                             + "<p>Ignore this email if you do remember your password, "
                             + "or you have not made the request.</p>";
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}
