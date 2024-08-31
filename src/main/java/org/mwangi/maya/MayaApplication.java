package org.mwangi.maya;

import com.africastalking.AfricasTalking;
import com.africastalking.ApplicationService;
import com.africastalking.Logger;
import com.africastalking.SmsService;
import com.africastalking.application.ApplicationResponse;
import com.africastalking.sms.Message;
import org.mwangi.maya.services.ATService;
import org.mwangi.maya.utility.SafaricomPayment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class MayaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(MayaApplication.class, args);
        ATService n=new ATService("sandbox","atsk_9112b4dd710d1668ebece7fab5e366c52fca75096f5a1f592ffadc195296135af3080e9b");
       n.initService();
        // SpringApplication.run(MayaApplication.class, args);
    }

}
