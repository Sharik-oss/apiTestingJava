package com.crocobet.apitester.apitesting;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RestController
@RequestMapping("/apis")
public class ApiResource {
    private final MeterRegistry meterRegistry;

    private final ApiService apiService;

//    public ApiResource(MeterRegistry meterRegistry, ApiService apiService) {
//        this.meterRegistry = meterRegistry;
//        this.apiService = apiService;
//    }
//    private final ApiService apiService;
//    private final ApiChecker apiChecker;


    @Autowired
    public ApiResource(ApiService apiService, MeterRegistry meterRegistry) {
        this.apiService = apiService;
//        this.apiChecker = apiChecker;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Api>> getAllApis(Model model) throws IOException, InterruptedException {
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

//        // Update response codes for all APIs
//        apiChecker.sendGetRequest(apis);

        return new ResponseEntity<>(apis, HttpStatus.OK);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<Api> getApiById(@PathVariable("id") Long id) throws IOException, InterruptedException {
        Api api = apiService.findApiById(id);
        List<Api> apis = apiService.findAllApis();
        ApiChecker.sendGetRequest(apis);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ModelAndView addApi(@RequestParam("name") String name,
                               @RequestParam("url") String url,
                               @RequestParam("userName") String userName,
                               @RequestParam("password") String password) throws IOException, InterruptedException {
//        System.out.println("Received url:"  + url);
        Api api = new Api();
        api.setName(name.replace(",", ""));

        // Validate the URL format before using it
        api.setUrl(url.replace(",", ""));
        Api newApi = apiService.addApi(api);
        List<Api> apis = apiService.findAllApis();
        if(userName.isEmpty() && password.isEmpty()) {
            System.out.println(apis);
        }else{
//            apiChecker.postRequest(List.of(api), userName, password);
            System.out.println("Post request");
        }
        return new ModelAndView("redirect:/");
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    @PutMapping(value = "/update/{id}")
    public ModelAndView updateApi(@PathVariable Long id, @ModelAttribute Api updatedApi) {
        // Retrieve the existing API from your data source based on the id
        Api existingApi = apiService.findApiById(id);

        // Update the properties of the existingApi with the properties of updatedApi
        existingApi.setName(updatedApi.getName());
        existingApi.setUrl(updatedApi.getUrl());

        // Save the updated API back to the data source
        apiService.updateApi(existingApi);

        // Return the updated API in the response
        return new ModelAndView("redirect:/");

    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteApi(@PathVariable("id") Long id) throws IOException {
        apiService.deleteApi(id);
        return new ModelAndView("redirect:/");
    }


}
