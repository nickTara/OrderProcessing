package com.app.orderprocessing.repositories;

import com.app.orderprocessing.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    Integer findCustomer(String customerId, String customerName);

}
