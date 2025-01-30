package com.app.orderprocessing.controller;

import com.app.orderprocessing.models.Customer;
import com.app.orderprocessing.models.Order;
import com.app.orderprocessing.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController

public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService service) {
        this.orderService = service;
    }

    @PostMapping(value = "/createCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCustomer(@RequestBody Customer payload)  {
        Customer customer = null;
        try {
            customer = orderService.create(payload.getCustomerId(), payload.getCustomerName());
        } catch(Exception ex){
            return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PostMapping(value = "/placeOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> placeOrder(@RequestBody Customer payload)  {
        Order order = null;
        try {
            order = orderService.placeOrder(payload.getCustomerId());
        } catch(Exception ex){
            return new ResponseEntity<>(Map.of("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getCustomerById/{Id}")
    public Optional<Customer> getCustomerById(@PathVariable String customerId) {
        return orderService.getCustomerById(customerId);
    }

    @GetMapping(value = "/getOrderById/{Id}")
    public Optional<Order> getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping(value = "/getAllOrdersByCustomer/{Id}")
    public List<String> getAllOrdersByCustomerId(@PathVariable String customerId) {
        return orderService.getAllOrdersByCustomer(customerId);
    }


}
