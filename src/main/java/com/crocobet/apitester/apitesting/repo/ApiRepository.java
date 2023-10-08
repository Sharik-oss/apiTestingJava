package com.crocobet.apitester.apitesting.repo;

import com.crocobet.apitester.apitesting.model.Api;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApiRepository extends JpaRepository<Api, Long> {
    void deleteApiById(Long id);

    Api findApiById(Long id);
}
