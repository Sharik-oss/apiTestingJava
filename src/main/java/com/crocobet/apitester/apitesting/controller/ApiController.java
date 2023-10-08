package com.crocobet.apitester.apitesting.controller;


import com.crocobet.apitester.apitesting.ApiChecker;
import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApiController {
    public final ApiService apiService;

    @RequestMapping("/")
    public ModelAndView main(Model model) throws IOException, InterruptedException {
        List<Api> apis = apiService.findAllApis();
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
