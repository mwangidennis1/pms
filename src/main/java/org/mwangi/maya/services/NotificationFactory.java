package org.mwangi.maya.services;

import org.mwangi.maya.utility.Notif;
import org.springframework.stereotype.Service;

@Service
public class NotificationFactory {
    private  ParcelReceiveGmailNotification parcelReceiveGmailNotification;
    private  ParcelReceiveSmsNotification parcelReceiveSmsNotification;

    public NotificationFactory(ParcelReceiveGmailNotification parcelReceiveGmailNotification, ParcelReceiveSmsNotification parcelReceiveSmsNotification) {
        this.parcelReceiveGmailNotification = parcelReceiveGmailNotification;
        this.parcelReceiveSmsNotification = parcelReceiveSmsNotification;
    }
    public Notif getNotificationService(String type){
        if (type.equalsIgnoreCase("email")){
            return parcelReceiveGmailNotification;
        }else if(type.equalsIgnoreCase("sms")){
            return  parcelReceiveSmsNotification;
        }
        throw new IllegalArgumentException("Unknown notification type: " + type);
    }
}
