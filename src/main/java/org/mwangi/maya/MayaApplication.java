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
        SpringApplication.run(MayaApplication.class, args);
        
       // SpringApplication.run(MayaApplication.class, args);
    }

}
