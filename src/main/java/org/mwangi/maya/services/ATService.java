package org.mwangi.maya.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class ATService {
    @Value("${africa.talking.api.key}")
    private String apiKey;
    @Value("${africa.talking.username}")
    private String username;

    protected boolean checkPhoneNumber(String phone) throws IOException {
        if (!phone.matches("^\\+\\d{1,3}\\d{3,}$")) {
            throw new IOException("Invalid phone number: " + phone + "; Expecting number in format +XXXxxxxxxxxx");
        }
        return true;
    }

    public String initService() {
        String url = "https://api.sandbox.africastalking.com/version1/user?username=" + username;
        String responseBodyout = "cannot get account balance now";
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        OkHttpClient httpClient = httpClientBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apiKey", apiKey)
                .addHeader("Accept", "application/json")
                .build();
        System.out.println(request);
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                responseBodyout = responseBody;

                //System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Request failed: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toBal(responseBodyout);
    }

    public static String toBal(String responseBodyout) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBodyout);
            return rootNode.path("UserData").path("balance").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

