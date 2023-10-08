package com.crocobet.apitester.apitesting;
import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import io.prometheus.client.exporter.HTTPServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ApiTestingApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApiTestingApplication.class, args);

    }
}
