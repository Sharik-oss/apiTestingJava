package com.crocobet.apitester.apitesting;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class PrometheusController {
    private final MeterRegistry meterRegistry;

    private final ApiService apiService;

    public PrometheusController(MeterRegistry meterRegistry, ApiService apiService) {
        this.meterRegistry = meterRegistry;
        this.apiService = apiService;
    }
paka
    @GetMapping("/metrics")
    public void generatePrometheusMetrics() {
        List<Api> apis = apiService.findAllApis();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        for (Api api : apis) {
            List<String> urls = new ArrayList<>();
            urls.add(api.getUrl());


            for (String apiUrl : urls) {
                Timer responseTimeSeconds = Timer.builder("response_time_seconds")
                        .tag("url", api.getUrl())
                        .register(meterRegistry);


                Timer pingingApi = Timer.builder("pinging_api")
                        .tag("url", api.getUrl())
                        .register(meterRegistry);

                executor.scheduleAtFixedRate(() -> {
                    try {
                        long startTime = System.nanoTime();

                        URL url = new URI(apiUrl).toURL();
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");


                        int responseCode = con.getResponseCode();


                        con.disconnect();
                        long endTime = System.nanoTime();

                        double responseTimeMs = (endTime - startTime) / 1e6;

                        double responseTime = (endTime - startTime) / 1e7;
                        System.out.println("Response time for: " + api.getUrl() + " : " + responseTime);
                        responseTimeSeconds.record((long) responseTime, TimeUnit.SECONDS);


                        pingingApi.record(Duration.ofMillis((long) responseTimeMs));

                        responseTime = 0;
                        Gauge.builder("response_code_counter", () -> responseCode)
                                .tag("url", api.getUrl())
                                .register(meterRegistry);

                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }, 0, 15, TimeUnit.SECONDS);

            }
        }
    }

}