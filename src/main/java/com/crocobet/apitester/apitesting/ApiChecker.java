package com.crocobet.apitester.apitesting;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApiChecker {
    private static final Summary responseSizeSummary = Summary.build()
            .name("api_response_body_size_bytes")
            .help("Response body size in bytes")
            .register();
    private static final Histogram requestDurationHistogram = Histogram.build()
            .name("api_request_response_duration_seconds")
            .help("Request and response duration in seconds")
            .register();

    private static final Summary responseCodeCounter = Summary.build()
            .name("api_response_code_total")
            .help("Total number of API responses with this code")
            .labelNames("code")
            .register();

    private static final Histogram responseTimeHistogram = Histogram.build()
            .name("api_response_time_seconds")
            .help("Response time in milliseconds")
            .register();


    private static ApiService apiService;
    private final Map<String, Counter> responseCodeCounters = new ConcurrentHashMap<>();

    public ApiChecker(ApiService apiService) {
        this.apiService = apiService;
    }


    public static void sendGetRequest(List<Api> apis) {
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//
//        for (Api api : apis) {
//            List<String> urls = new ArrayList<>();
//            urls.add(api.getUrl());
//
//            AtomicInteger newResponseCode = new AtomicInteger();
//
//            for (String apiUrl : urls) {
//                executor.scheduleAtFixedRate(() -> {
//                    try {
//                        long responseStartTime = System.currentTimeMillis();
//                        long startTime = System.nanoTime();
//                        URL url = new URI(apiUrl).toURL();
//                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                        con.setRequestMethod("GET");
//                        newResponseCode.set(con.getResponseCode());
//
//                        long responseEndTime = System.currentTimeMillis();
//                        double responseTime = (responseEndTime - responseStartTime) / 1000.0;
//                        System.out.println("Response time " + responseTime);
//
//                        responseCodeCounter.observe(Double.valueOf(String.valueOf(newResponseCode.get())));
//
//                        responseTimeHistogram.observe(responseTime);
//                        System.out.println("I'm here");
//
//                        InputStream inputStream = con.getInputStream();
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        byte[] buffer = new byte[1024];
//                        System.out.println(buffer);
//                        System.out.println("And here below the buffer");
//                        int bytesRead;
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            byteArrayOutputStream.write(buffer, 0, bytesRead);
//                        }
//
//
//                        byte[] responseBodyBytes = byteArrayOutputStream.toByteArray();
//
//                        inputStream.close();
//                        byteArrayOutputStream.close();
//                        System.out.println("Response body: " + responseBodyBytes);
//                        responseSizeSummary.observe(responseBodyBytes.length);
//
//                        long endTime = System.nanoTime();
//                        double durationInSeconds = (endTime - startTime) / 1e9;
//                        System.out.println("Duration: " + durationInSeconds);
//                        requestDurationHistogram.observe(durationInSeconds);
//
//                    } catch (IOException | URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                }, 0, 15, TimeUnit.SECONDS);
//            }
//        }
    }


//    public static AtomicInteger sendGetRequest(List<Api> apis) {
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        AtomicInteger newResponseCode = new AtomicInteger();
//        for (Api api : apis) {
//            List<String> urls = new ArrayList<>();
//            urls.add(api.getUrl());
//            for (String apiUrl : urls) {
//                executor.scheduleAtFixedRate(() -> {
//                    try {
//                        URL url = new URI(apiUrl).toURL();
//                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                        con.setRequestMethod("GET");
//                        newResponseCode.set(con.getResponseCode());
//
////                        System.out.println(newResponseCode.get());
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (URISyntaxException e) {
//                        throw new RuntimeException(e);
//                    }
//                }, 0, 15, TimeUnit.SECONDS);
//
//            }
//
//
//        }
//        return newResponseCode;
//    }




}



/*
 public static void postRequest(List<Api> apis, String userName, String password) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(apis.size());

        int maxRequestPerURL = 3;
        int delayBetweenRequests = 6000;

        for (Api api : apis) {
            List<String> urls = new ArrayList<>();
            urls.add(api.getUrl());  // Add the URL(s) you want to send requests to here

            for (String apiUrl : urls) {
                executor.scheduleAtFixedRate(() -> {
                    for (int i = 0; i < maxRequestPerURL; i++) {
                        try {
                            URL url = new URL(apiUrl);  // Use the URL directly, not within a URI
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            con.setDoOutput(true);

                            con.setRequestProperty("Content-Type", "application/json");
                            int newResponseCode = con.getResponseCode();

                            String payload = "{\"login\": \"" + userName + "\", \"password\": \"" + password + "\"}";
                            try (DataOutputStream outputStream = new DataOutputStream(con.getOutputStream())) {
                                outputStream.writeBytes(payload);
                            }
                        } catch (IOException e) {
                            // Log the exception and possibly implement a retry mechanism
                            e.printStackTrace();
                        }
                    }
                }, 0, delayBetweenRequests * maxRequestPerURL, TimeUnit.MILLISECONDS);
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





 */