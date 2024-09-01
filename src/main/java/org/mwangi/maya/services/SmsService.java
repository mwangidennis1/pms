package org.mwangi.maya.services;

import okhttp3.*;

import java.io.IOException;

public class SmsService {
    private final String uSername;
    private final String apikey;
    public SmsService(final String username, final  String apikey){
        this.uSername=username;
        this.apikey=apikey;
    }

    public void initService() {
        String url = "https://api.sandbox.africastalking.com/version1/messaging";

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        OkHttpClient httpClient = httpClientBuilder.build();
        RequestBody body = new FormBody.Builder()
                .add("username",uSername)
                .add("to","+254794658495")
                .add("message","reporting live from houston")
                .add("from","MAYA")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apiKey", apikey)
                .addHeader("Accept", "application/json")
                .post(body)
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
