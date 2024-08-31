package org.mwangi.maya.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SafaricomPayment {
    public static final String BEARER_AUTH_STRING = "Bearer";
    //@Value("${daraja.api.consumer.key}")
    private static final String consumerKey ="qmrARH4qbB5TDl2OlCg24XtaMr4AqKCAdrAoHWtwjJupjPQb";
   // @Value("${daraja.api.consumer.secret}")
    private static final String consumerSecret = "zNNdYfGYgeHHxwRdGmj3QKGp68hOBCjpjzgARl1qo1LFC3jtphGpk91P3DksFE9w";
    //@Value("${daraja.api.authorization.callback}")
    private static final String callbackUrl="https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
   public  static String   getAcessToken(){
       OkHttpClient client = new OkHttpClient().newBuilder().build();
       String auth = consumerKey + ":" + consumerSecret;
       //System.out.println(auth);
       String encodedAuth= Base64.getEncoder().encodeToString(auth.getBytes());
       Request request=new Request.Builder()
               .url(callbackUrl)
               .method("GET",null)
               .addHeader("Authorization","Basic "+encodedAuth)
               .build();
       try {
           Response response = client.newCall(request).execute();
           String responseBody = response.body().string();
           JSONObject jsonObject = new JSONObject(responseBody);
           return  jsonObject.getString("access_token");
       } catch (IOException e) {
           e.printStackTrace();
           return null;
       }
   }
   public static  Object getRegisterUrl() throws IOException {
       OkHttpClient client = new OkHttpClient().newBuilder().build();
       MediaType mediaType = MediaType.parse("application/json");
       ObjectMapper objectMapper = new ObjectMapper();
       Map<String, Object> bodyMap = new HashMap<>();
       bodyMap.put("ShortCode", 600990);
       bodyMap.put("ResponseType", "Completed");
       bodyMap.put("ConfirmationURL", "https://mydomain.com/confirmation");
       bodyMap.put("ValidationURL", "https://mydomain.com/validation");
       String jsonString=objectMapper.writeValueAsString(bodyMap);
       RequestBody body=RequestBody.create(mediaType,jsonString);
       Request request = new Request.Builder()
               .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl")
               .method("POST", body)
               .addHeader("Content-Type", "application/json")
               .addHeader("Authorization", String.format("%s %s",BEARER_AUTH_STRING,getAcessToken()))
               .build();
       Response response = client.newCall(request).execute();
       return  response.body().string();
   }

}
