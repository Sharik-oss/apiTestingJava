package com.crocobet.apitester.apitesting;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
@RestController
@RequestMapping("/apis")
public class ApiResource {
    private final ApiService apiService;
    private final ApiChecker apiChecker;


    @Autowired
    public ApiResource(ApiService apiService, ApiChecker apiChecker) {
        this.apiService = apiService;
        this.apiChecker = apiChecker;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Api>> getAllApis(Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();

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
