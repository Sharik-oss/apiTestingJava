package com.crocobet.apitester.apitesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class TestingPostRequest {
    public static void main(String[] args) throws IOException {
//        try {
//            URL url = new URL("https://s8.egtmgs.com:8181/core-web-war/MGL?defenceCode=848288q03edcb8d156a09290f4f1072937c7ec04c6ad1ab&playerId=848288&portalCode=Crocobet_Portal_GEL&screenName=848288&language=KA&country=bg&gameId=533&client=flash");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestMethod("GET");
//            connection.setDoOutput(true);
//
//            connection.setRequestProperty("Content-Type", "application/json");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String line;
//            StringBuilder response = new StringBuilder();
//
//            while ((line = reader.readLine()) != null){
//                response.append(line);
//            }
//            reader.close();
//
//            System.out.println("Response code "  + response.toString());
//
//            connection.disconnect();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://betconstruct-1109.pragmaticplay.net/gs2c/playGame.do?key=token%3D1dace397740709735379ca482b23e%26symbol%3Dvs20olympgate%26technology%3DH5%26platform%3DWEB%26language%3Dka%26currency%3DGEL%26cashierUrl%3D%26lobbyUrl%3Dhttps%253a%252f%252fgames.crocobet.com%252fGoToHome%253fId%253dcrocobet.com%25252f&amp;stylename=btcnst_crocobet&amp;isGameUrlApiCalled=true");

        try {
            HttpResponse response = httpClient.execute(httpGet);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println(responseContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
