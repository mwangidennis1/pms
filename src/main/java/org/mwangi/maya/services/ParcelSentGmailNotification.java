package org.mwangi.maya.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mwangi.maya.utility.Notif;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service

public class ParcelSentGmailNotification implements Notif {

    private JavaMailSender javaMailSender;

    public ParcelSentGmailNotification( JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendNotif(String recepient, String link, String trackNumber) {

        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        try {
            helper.setFrom("denmwangi@usiu.ac.ke","MAYA");
            helper.setTo(recepient);
            String subject="PARCEL DELIVERY USO";
            String content = "<p>Hello,</p>"
                    + "<p>You receiving this email because we have your parcel</p>"
                    + "<p>Just walk straight and pretend no one is following you and trace it from</p>"
                    + "<p><a href=\"" + link + "\">Trace parcel</a></p>"
                    + "<p>Copy this tracking number below: </p>"+trackNumber
                    + "<br>"
                    + "<p>Ignore this email if you think you dont deserve a suprise "
                    + "or you do not like what you see.</p>";
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
