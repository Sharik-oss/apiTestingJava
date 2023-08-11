package com.crocobet.apitester.apitesting;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ApiChecker {
    private static ApiService apiService;

    public ApiChecker(ApiService apiService){
        this.apiService = apiService;
    }

    public void updateResponseCodes(List<Api> apis) {
        for (Api api : apis) {
            try {
                URL url = new URI(api.getUrl()).toURL();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int newResponseCode = con.getResponseCode();

                // Update response code only if it's different
                if (!String.valueOf(newResponseCode).equals(api.getResponseCode())) {
                    api.setResponseCode(String.valueOf(newResponseCode));
                    // You might want to log or notify about the change here
                }

            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }




    public static void checkingApi(List<Api> apis) throws IOException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(apis.size());
        for (Api api : apis) {
            List<String> urls = new ArrayList<String>();
            urls.add(api.getUrl());

            int maxRequestPerURL = 3;
            int delayBetweenRequests = 6000;
            for (String apiUrl : urls) {
                executor.scheduleAtFixedRate(() -> {
                    for (int i = 0; i < maxRequestPerURL; i++) {
                        try {
                            URL url = new URI(apiUrl).toURL();
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            int newResponseCode = con.getResponseCode();

                            // Update response code only if it's different
                            if (!String.valueOf(newResponseCode).equals(api.getResponseCode())) {
                                api.setResponseCode(String.valueOf(newResponseCode));
                                // You might want to log or notify about the change here
                            }

                            Thread.sleep(delayBetweenRequests);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, 0, delayBetweenRequests * maxRequestPerURL, TimeUnit.SECONDS);
            }
        }
    }


    public int getResponseCode(String apiUrl) {
        try {
            URL url = new URI(apiUrl).toURL();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con.getResponseCode();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return -1; // Return a default error code
        }
    }


}
