package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    CacheManager cacheManager;

    Callable<List<User>> callableObj = () -> {
        Thread.sleep(1000);
        return this.userRepo.findAll();
    };

    @Override
    @CircuitBreaker(name = "dbback" )
    @Bulkhead(name = "dbback", type = Type.THREADPOOL)
    @TimeLimiter(name = "dbback")
    public CompletableFuture<List<User>> getUsers() {
        return CompletableFuture.supplyAsync(() -> {
            
            return cacheManager.getCache("cache1").get("data", this.callableObj);
        });
    }




    @Override
    public User saveUser(User user) {
        
        return this.userRepo.save(user);
    }
    
}
