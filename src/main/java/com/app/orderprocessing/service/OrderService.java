package com.app.orderprocessing.service;

import com.app.orderprocessing.entities.Customer;
import com.app.orderprocessing.entities.Order;
import com.app.orderprocessing.models.OrderStatus;
import com.app.orderprocessing.repositories.CustomerRepository;
import com.app.orderprocessing.repositories.OrderRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(CustomerRepository crepository, OrderRepository orepository) {

        this.customerRepository = crepository;
        this.orderRepository = orepository;
    }

    public Customer create(String customerId, String customerName) {
        if (null == customerId || null == customerName) {
            throw new IllegalArgumentException("Fields are missing");
        }
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            throw new DuplicateRequestException("Duplicate Found!");
        }
        Customer customer = new Customer(customerId, customerName);
        customerRepository.save(customer);
        return customer;
    }

    public Order placeOrder(String customerId, List<String> items) {
        if (null == customerId) {
            throw new IllegalArgumentException("Fields are missing");
        }
        Order order = new Order(UUID.randomUUID().toString(), OrderStatus.PLACED.toString(), items, customerId);
        orderRepository.save(order);
        return order;
    }

    public Optional<Customer> getCustomerById(String customerId) {
        if (null == customerId) {
            throw new NoSuchElementException("CustomerId not present");
        }
        return customerRepository.findById(customerId);

    }

    public Optional<Order> getOrderById(String orderId) {
        if (null == orderId) {
            throw new NoSuchElementException("OrderID not present");
        }
        return orderRepository.findById(orderId);
    }

    // order status will get updated as cancelled but order will be there.
    public Order cancelOrder(String orderId) {
        if (!orderId.isEmpty()) {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                Order orderexisting = order.get();
                Order updatedOrder = new Order(orderexisting.getOrderId(), OrderStatus.CANCELLED.toString(), orderexisting.getItems(), orderexisting.getCustomerId());
                orderRepository.save(updatedOrder);
                return updatedOrder;
            }
        }
        return null;
    }

    public List<String> getAllOrdersByCustomer(String customerId) {
        return orderRepository.findAllOrdersByCustomerId(customerId);
    }

}