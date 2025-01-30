package com.app.orderprocessing.repositories;

import com.app.orderprocessing.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}
