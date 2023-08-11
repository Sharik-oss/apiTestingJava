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
    public ResponseEntity<List<Api>> getAllApis(Model model) throws IOException {
        List<Api> apis = apiService.findAllApis();

        // Update response codes for all APIs
        apiChecker.updateResponseCodes(apis);

        return new ResponseEntity<>(apis, HttpStatus.OK);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<Api> getApiById(@PathVariable("id") Long id) throws IOException {
        Api api = apiService.findApiById(id);
        List<Api> apis = apiService.findAllApis();
        ApiChecker.checkingApi(apis);
        return new ResponseEntity<>(api, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ModelAndView addApi(@RequestParam("name") String name,
                                      @RequestParam("url") String url) throws IOException {

        Api api = new Api();
        api.setName(name);
        api.setUrl(url);

        // Perform the API call to get the response code
        int responseCode = apiChecker.getResponseCode(url);
        api.setResponseCode(String.valueOf(responseCode));

        Api newApi = apiService.addApi(api);
        List<Api> apis = apiService.findAllApis();

        apiChecker.checkingApi(apis);

        return new ModelAndView("redirect:/");
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Api> updateApi(@PathVariable Long id, @RequestBody Api updatedApi) {
        try {
            // Retrieve the existing API from your data source based on the id
            Api existingApi = apiService.findApiById(id);

            if (existingApi == null) {
                // API with the given id not found
                return ResponseEntity.notFound().build();
            }

            // Update the properties of the existingApi with the properties of updatedApi
            existingApi.setName(updatedApi.getName());
            existingApi.setUrl(updatedApi.getUrl());

            // Save the updated API back to the data source
            apiService.updateApi(existingApi);

            // Return the updated API in the response
            return ResponseEntity.ok(existingApi);
        } catch (Exception e) {
            // Handle exceptions, such as database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteApi(@PathVariable("id") Long id) throws IOException {
        apiService.deleteApi(id);
        return new ModelAndView("redirect:/");
    }


}
