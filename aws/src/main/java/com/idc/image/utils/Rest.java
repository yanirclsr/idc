package com.idc.image.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Rest {

    public static JSONObject GET(String url){

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        if(entity != null){

            String result = null;
            try {
                result = new BufferedReader(new InputStreamReader(entity.getContent())).readLine();
            } catch (UnsupportedOperationException | IOException e) {
                e.printStackTrace();
            }

            JSONObject json = (JSONObject) JSONValue.parse(result);
            System.out.println("returning a json");
            return json;
        }
        System.out.println("response is null");
        return null;
    }

}
