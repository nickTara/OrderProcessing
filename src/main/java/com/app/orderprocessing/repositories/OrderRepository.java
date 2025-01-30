package com.app.orderprocessing.repositories;

import com.app.orderprocessing.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

     List<String> findAllOrdersByCustomerId(String customerId); // need to test this functionality
}
