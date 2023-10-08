package com.crocobet.apitester.apitesting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestVardoSite {
    public static void main(String[] args) throws IOException {
        while(true){
            try {
                URL url = new URL("http://sm.crocobet.local:5000/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                System.out.println("Request sent...");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
