package com.dynamodb.data.repository;

import java.util.Optional;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import com.dynamodb.data.model.Product;

@EnableScan
public interface ProductRepository extends CrudRepository<Product, String> {
    Optional<Product> findById(String id);
}
