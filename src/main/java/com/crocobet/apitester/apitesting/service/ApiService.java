package com.crocobet.apitester.apitesting.service;

import com.crocobet.apitester.apitesting.model.Api;
import com.crocobet.apitester.apitesting.repo.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiService {
    private final ApiRepository apiRepository;

    @Autowired
    public ApiService(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }


    public Api addApi(Api api){
        return apiRepository.save(api);
    }

    public List<Api> findAllApis(){
        return apiRepository.findAll();
    }

    public Api updateApi(Api api){
        return apiRepository.save(api);
    }

    public Api findApiById(Long id){
        return apiRepository.findApiById(id);
    }

    @Transactional
    public void deleteApi(Long id){
        apiRepository.deleteApiById(id);
    }

}
