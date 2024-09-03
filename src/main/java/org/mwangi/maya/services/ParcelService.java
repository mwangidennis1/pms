package org.mwangi.maya.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.mwangi.maya.entities.*;
import org.mwangi.maya.repositories.ParcelRepository;
import org.mwangi.maya.repositories.ReceiverRepository;
import org.mwangi.maya.repositories.SenderRepository;
import org.mwangi.maya.utility.Notif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;

@Service
public class ParcelService {
    private String[] notifType={"email","sms"};
    private String notifMethod;
    @Value("${email.track.url}")
    private  String trackLink;
    private ApplicationEventPublisher applicationEventPublisher;
    private ParcelRepository parcelRepository;
    private ReceiverRepository receiverRepository;
    private SenderRepository senderRepository;

    private  ParcelSentGmailNotification parcelSentGmailNotification;

    private   NotificationFactory notificationFactory;

    public ParcelService(ApplicationEventPublisher applicationEventPublisher, ParcelRepository parcelRepository, ReceiverRepository receiverRepository, SenderRepository senderRepository, ParcelSentGmailNotification parcelSentGmailNotification, NotificationFactory notificationFactory) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.parcelRepository = parcelRepository;
        this.receiverRepository = receiverRepository;
        this.senderRepository = senderRepository;
        this.parcelSentGmailNotification = parcelSentGmailNotification;
        this.notificationFactory = notificationFactory;
    }


    public void createParcel(Parcel parcel, Receiver receiver,Sender sender){
        String trackingNumber= RandomStringUtils.randomAlphanumeric(7);
        receiverRepository.save(receiver);
        senderRepository.save(sender);
        parcel.setTrackingNumber(trackingNumber);
        parcel.setReceiver(receiver);
        parcel.setSender(sender);
        parcelRepository.save(parcel);
        Notif notif=notificationFactory.getNotificationService(notifMethod);
        if(notifMethod.equalsIgnoreCase("email")){
            notif.sendNotif(receiver.getReceiverEmail(),trackLink,trackingNumber);
        }else{
            String phoneNo=receiver.getReceiverPhoneNo();
            String front="+254" + phoneNo.substring(1);

            notif.sendNotif(front,trackLink,trackingNumber);
        }

        try {
            updateAutoHotkeyScript(trackingNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Parcel> getParcels(){
        return parcelRepository.findAll();
    }
    private void updateAutoHotkeyScript(String barcode) throws IOException {
        String pathToScript= "./Barcode.ahk";
        List<String> lines = Files.readAllLines(Paths.get(pathToScript));

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("SendBarcodeToSystem(")) {
                lines.set(i, "SendBarcodeToSystem(\"" + barcode + "\")");
                break;
            }
        }

        Files.write(Paths.get(pathToScript), lines);
    }
    public Parcel getParcelByTrackNumber(String number){

        return  parcelRepository.findParcelByTrackingNumber(number);

    }
    public void  updateParcelStatus(String tracknumber){
         Parcel parcel=getParcelByTrackNumber(tracknumber);

         parcel.setParcelStatus(ParcelStatus.DELIVERED);
         parcelRepository.save(parcel);
        Notif notif=notificationFactory.getNotificationService(notifMethod);
         if(parcel.getReceiver().getReceiverEmail().isEmpty()){
             String phoneNo=parcel.getReceiver().getReceiverPhoneNo();
             String front="+254" + phoneNo.substring(1);
             System.out.println(front);
             notif.sendNotif(front,trackLink,tracknumber);
         }else {
             notif.sendNotif(parcel.getReceiver().getReceiverEmail(),trackLink,tracknumber);
         }

    }
    public List<Parcel> reportResults(LocalDateTime from,LocalDateTime to){
        return  parcelRepository.findParcelByEstimatedDeliveryDateIsBetweenDate(from,to);
    }
    public Optional<Parcel> getByID(long id){
        return parcelRepository.findById(id);
    }
    public void deleteParcel(long id){
        parcelRepository.deleteById(id);
    }
    public  void saveParcel(Parcel parcel){

        parcelRepository.save(parcel);
    }
    public    void setType(int index){
          notifMethod=notifType[index];
    }

}
