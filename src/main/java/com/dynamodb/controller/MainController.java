package com.dynamodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dynamodb.data.model.Product;
import com.dynamodb.service.DynamoDBService;

@RestController(value = "/")
public class MainController {
    @Autowired
    private DynamoDBService dynamDBService;
    
    @PostMapping("/addPdt")
    public void doSomething(@RequestBody Product pdt) {
        dynamDBService.addProduct(pdt);
    }
    
    @GetMapping("/{id}")
    public Product getPdt(@PathVariable("id") String id) {
        return dynamDBService.getProduct(id);
    }
    
    @DeleteMapping("/{id}")
    public void removePdt(@PathVariable("id") String id) {
        dynamDBService.deleteProduct(id);
    }
    
    @PutMapping("/{id}")
    public void updatePdt(@PathVariable("id") String id, @RequestBody Product pdt) {
        dynamDBService.updateProduct(id, pdt);
    }
}
