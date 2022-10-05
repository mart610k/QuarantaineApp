package dk.quarantaine.app.service;

import dk.quarantaine.app.datamodel.APIResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class StringAPICaller implements BaseAPICaller,Callable<APIResponse>{

    String endpoint;
    String username;
    String password;

    public StringAPICaller(String endpoint, String username, String password){
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;

    }

    @Override
    public APIResponse call() throws Exception{
        Map<String,String> headers = new HashMap<>();

        headers.put("content-type", "application/x-www-form-urlencoded");

        String body = String.format("grant_type=password&username=%s&password=%s", URLEncoder.encode(username,StandardCharsets.UTF_8.toString()),URLEncoder.encode(password,StandardCharsets.UTF_8.toString()));
        String base64Encoded = new String(Base64.getEncoder().encode("YamnQiZ5tJhkWXDJDGJA:Upb7r7DereQnbPsfNHS8".getBytes(StandardCharsets.UTF_8)));

        headers.put("Authorization", "basic " + base64Encoded);

        APIResponse response = getAPIData("https://quarantaine.baage-it.dk","POST", endpoint,headers,body);

        return response;
    }


}
