package org.mwangi.maya;

import com.africastalking.AfricasTalking;
import com.africastalking.ApplicationService;
import com.africastalking.Logger;
import com.africastalking.SmsService;
import com.africastalking.application.ApplicationResponse;
import com.africastalking.sms.Message;
import org.mwangi.maya.utility.SafaricomPayment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class MayaApplication {

    public static void main(String[] args) {
      //  SpringApplication.run(MayaApplication.class, args);
        try{
            AfricasTalking.initialize("sandbox","atsk_9112b4dd710d1668ebece7fab5e366c52fca75096f5a1f592ffadc195296135af3080e9b");
            AfricasTalking.setLogger(new Logger(){
                @Override
                public void log(String message, Object... args) {
                    //System.out.println(message);
                }
            });
            ApplicationResponse response = AfricasTalking.getService(ApplicationService.class).fetchApplicationData();
            //smsService.send("where i am from","MAYA",new String[]{"+254728003836"},true);
            //List<Message> x=smsService.fetchMessages();
            System.out.println(response.userData.balance);
           /* String message="Your parcel has arrived use this link  and this tracking number to access it "
                    + " Parcel should be picked within 3 days";
            Object response= smsService.send(message,"MAYA",new String[]{recepient},true);
             System.out.println(response);*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*try {
            Object token=SafaricomPayment.getRegisterUrl();
            if(token != null){
                System.out.println("Register url: \n" +token);
            }else {
                System.out.println("An error occurred");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String token =SafaricomPayment.getAcessToken();
        if(token != null){
            System.out.println("Access token: \n" +token);
        }else{
            System.out.println("An error occurred");
        }*/
       // SpringApplication.run(MayaApplication.class, args);
    }

}
