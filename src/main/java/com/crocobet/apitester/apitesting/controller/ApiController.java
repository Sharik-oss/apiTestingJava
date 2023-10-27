package com.crocobet.apitester.apitesting.controller;


import com.crocobet.apitester.apitesting.ApiChecker;
import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
//@RequiredArgsConstructor
public class ApiController {
    private final MeterRegistry meterRegistry;
    public final ApiService apiService;

    @Autowired
    public ApiController(MeterRegistry meterRegistry, ApiService apiService){
        this.apiService = apiService;
        this.meterRegistry = meterRegistry;
    }


    @RequestMapping("/")
    public ModelAndView main(Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();
//        List<Api> apis = apiService.findAllApis();
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
        ApiChecker.sendGetRequest(apis);
        for(Api api : apis){
            String cleanedId = api.getId().toString().replaceAll(",", "");
            api.setId(Long.valueOf(cleanedId));
        }
        model.addAttribute("apis", apiService.findAllApis());
        return new ModelAndView("index");
    }

    @RequestMapping("/services")
    public ModelAndView services(Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();
        ApiChecker.sendGetRequest(apis);
        model.addAttribute("apiService", apiService.findAllApis());
        return new ModelAndView("services");
    }

    @RequestMapping("/updateApi")
    public ModelAndView services(Model model, Long id) throws IOException {
        Api api = apiService.findApiById(id);
        model.addAttribute("api", api);
        return new ModelAndView("services");
    }

    @RequestMapping("/api/{id}")
    public ModelAndView apiInfo(@PathVariable Long id, Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();
        ApiChecker.sendGetRequest(apis);
        Api api = apiService.findApiById(id);
        model.addAttribute("api", api);
        return new ModelAndView("updateApi");
    }

    @RequestMapping("/dashboard")
    public ModelAndView dashboard(Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();
        ApiChecker.sendGetRequest(apis);
        // Add the data to the model
        model.addAttribute("api", apis);
        return new ModelAndView("dashboard");
    }


    @RequestMapping("/add")
    public ModelAndView addService(Model model) throws IOException {
//        List<Api> apis = apiService.findAllApis();
//        ApiChecker.checkingApi(apis);
//        model.addAttribute("apiService", apiService.findAllApis());
        return new ModelAndView("addService");
    }
}
