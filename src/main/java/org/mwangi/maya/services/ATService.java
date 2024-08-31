package org.mwangi.maya.services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.mwangi.maya.dto.ATServiceResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class ATService {
    private ATServiceResponse atServiceResponse;
    private final String uSername;
    private final String apikey;
    public ATService(final String username,final  String apikey){
        this.uSername=username;
        this.apikey=apikey;
    }
    protected boolean checkPhoneNumber(String phone) throws IOException {
        if (!phone.matches("^\\+\\d{1,3}\\d{3,}$")) {
            throw new IOException("Invalid phone number: " + phone + "; Expecting number in format +XXXxxxxxxxxx");
        }
        return true;
    }
    public void initService() {
        String url = "https://api.sandbox.africastalking.com/version1/user?username="+uSername;

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        OkHttpClient httpClient = httpClientBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apiKey", apikey)
                .addHeader("Accept", "application/json")
                .build();
        System.out.println(request);
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Request failed: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


