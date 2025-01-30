package com.app.orderprocessing.service;

import com.app.orderprocessing.models.Customer;
import com.app.orderprocessing.models.Order;
import com.app.orderprocessing.models.OrderStatus;
import com.app.orderprocessing.repositories.CustomerRepository;
import com.app.orderprocessing.repositories.OrderRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(CustomerRepository crepository, OrderRepository orepository) {

        this.customerRepository = crepository;
        this.orderRepository=orepository;
    }

    public Customer create(String customerId, String customerName)  {
        if(null == customerId || null == customerName){
            throw new IllegalArgumentException("Fields are missing");
        }
        Integer existingCustomer = customerRepository.findCustomer(customerId, customerName);
        if(existingCustomer != 0){
            throw new DuplicateRequestException("Duplicate Found!");
        }
        Customer customer = new Customer(customerId, customerName);
        customerRepository.save(customer);
        return customer;
    }

    public Order placeOrder(String customerId) {
        if(null == customerId){
            throw new IllegalArgumentException("Fields are missing");
        }
        Order order = new Order(UUID.randomUUID().toString(), OrderStatus.PLACED.toString(), customerId);
        orderRepository.save(order);
        return order;
    }

    public Customer getCustomerById(String customerId) {
        if(null == customerId){
            throw new NoSuchElementException("CustomerId not present");
        }
        return customerRepository.findById(customerId);
    }
}
