package org.mwangi.maya;


import org.mwangi.maya.services.ATService;
import org.mwangi.maya.services.SmsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MayaApplication {


    public static void main(String[] args) {
        SpringApplication.run(MayaApplication.class, args);
        //ATService n=new ATService("sandbox","atsk_9112b4dd710d1668ebece7fab5e366c52fca75096f5a1f592ffadc195296135af3080e9b");
       //n.initService();
        /*try {
            System.out.println(checkPhoneNumber("+254794658495"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        // SpringApplication.run(MayaApplication.class, args);
    }

}
