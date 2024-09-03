package org.mwangi.maya.services;

import okhttp3.*;
import org.mwangi.maya.utility.Fileio;
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
    private final String url="https://api.sandbox.africastalking.com/version1/messaging";
    @Override
    public void sendNotif(String recepient, String link, String trackNumber) {
        if(checkPhoneNumber(recepient)) {
                String message = "Your parcel has been sent use this link " + link + " and this tracking number to access it "
                        + " Parcel should be picked within 3 days";
                OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
                OkHttpClient httpClient = httpClientBuilder.build();
                RequestBody body = new FormBody.Builder()
                        .add("username",username)
                        .add("to",recepient)
                        .add("message",message)
                        .add("from","MAYA")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("apiKey", apiKey)
                        .addHeader("Accept", "application/json")
                        .post(body)
                        .build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {

                    String responseBody = response.body().string();
                    Fileio.saveATResponses(responseBody);
                } else {
                    System.err.println("Request failed: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            }


    }
    private boolean checkPhoneNumber(String phone)  {
        if (!phone.matches("^\\+\\d{1,3}\\d{3,}$")) {
            new IOException("Invalid phone number: " + phone + "; Expecting number in format +XXXxxxxxxxxx");
        }
        return true;
    }

    }
