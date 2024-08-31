package org.mwangi.maya.services;

import com.africastalking.AfricasTalking;
import com.africastalking.Logger;
import com.africastalking.SmsService;
import org.mwangi.maya.utility.Notif;
import org.mwangi.maya.utility.SmsNotif;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ParcelReceiveSmsNotification implements Notif {
    @Value("${africa.talking.api.key}")
    private  String apiKey;
    @Value("${africa.talking.username}")
    private  String username;
    @Value("${email.track.url}")
    private  String trackLink;
    @Override
    public void sendNotif(String recepient, String link, String trackNumber) {
        try{
            AfricasTalking.initialize(username,apiKey);
            AfricasTalking.setLogger(new Logger(){
                @Override
                public void log(String message, Object... args) {
                    System.out.println(message);
                }
            });
            SmsService smsService = AfricasTalking.getService(SmsService.class);
            String message="Your parcel has arrived use this link " +link+" and this tracking number to access it "
                    + " Parcel should be picked within 3 days";
            Object response= smsService.send(message,"MAYA",new String[]{recepient},true);
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
