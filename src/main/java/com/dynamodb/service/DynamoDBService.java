package com.dynamodb.service;

import java.util.Optional;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.dynamodb.data.model.Product;
import com.dynamodb.data.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DynamoDBService implements InitializingBean, DisposableBean {
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private ProductRepository productRepository;

    private DynamoDBMapper dynamoDBMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        
        // create table on application startup
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Product.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(tableRequest);
    }

    public void addProduct(Product pdt) {
        log.info("PostMapping || Pdt: {}", pdt);
        productRepository.save(pdt);
    }

    public void updateProduct(String id, Product newPdt) {
        Optional<Product> pdtOptional = productRepository.findById(id);
        if (!pdtOptional.isPresent()) {
            log.warn("PutMapping || Pdt not found with ID: {}", id);
            return;
        }
        Product pdt = pdtOptional.get();
        pdt.setName(newPdt.getName());
        pdt.setPrice(newPdt.getPrice());
        productRepository.save(pdt);
        log.info("Updated Pdt: {}", pdt);
    }

    public void deleteProduct(String id) {
        Optional<Product> pdtOptional = productRepository.findById(id);
        if (!pdtOptional.isPresent()) {
            log.warn("DeleteMapping || Pdt not found with ID: {}", id);
            return;
        }
        productRepository.delete(pdtOptional.get());
        log.info("Successfully deleted Pdt with ID: {}", id);
    }

    public Product getProduct(String id) {
        Optional<Product> pdtOptional = productRepository.findById(id);
        if (pdtOptional.isPresent()) {
            log.info("Pdt: {}", pdtOptional.get());
            return pdtOptional.get();
        }
        log.warn("GetMapping || Pdt not found with ID: {}", id);
        return null;
    }

    @Override
    public void destroy() throws Exception {
        // delete table on application shutdown
        DeleteTableRequest tableRequest = dynamoDBMapper.generateDeleteTableRequest(Product.class);
        amazonDynamoDB.deleteTable(tableRequest);
    }
}
