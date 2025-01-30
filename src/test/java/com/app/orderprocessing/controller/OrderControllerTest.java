package com.app.orderprocessing.controller;

import com.app.orderprocessing.entities.Customer;
import com.app.orderprocessing.entities.Order;
import com.app.orderprocessing.models.PlaceOrderRequest;
import com.app.orderprocessing.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void createCustomer_success() {
        Customer payload = new Customer("testId", "testName");
        Customer createdCustomer = new Customer("testId", "testName");
        when(orderService.create(payload.getCustomerId(), payload.getCustomerName())).thenReturn(createdCustomer);

        ResponseEntity<Object> response = orderController.createCustomer(payload);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCustomer, response.getBody());
    }

    @Test
    void createCustomer_exception() {
        Customer payload = new Customer("testId", "testName");
        String errorMessage = "Some error";
        when(orderService.create(payload.getCustomerId(), payload.getCustomerName())).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> response = orderController.createCustomer(payload);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, ((java.util.Map<String, String>)response.getBody()).get("message"));
    }

    @Test
    void placeOrder_success() {
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        PlaceOrderRequest payload = new PlaceOrderRequest("testId", itemList);

        Order createdOrder = new Order("orderId", "PLACED",itemList, "testId");
        when(orderService.placeOrder(payload.getCustomerId(), payload.getItems())).thenReturn(createdOrder);

        ResponseEntity<Object> response = orderController.placeOrder(payload);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdOrder, response.getBody());
    }

    @Test
    void placeOrder_exception() {
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        PlaceOrderRequest payload = new PlaceOrderRequest("testId", itemList);
        String errorMessage = "Some error";
        when(orderService.placeOrder(payload.getCustomerId(), payload.getItems())).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> response = orderController.placeOrder(payload);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, ((java.util.Map<String, String>)response.getBody()).get("message"));
    }


    @Test
    void cancelOrder_success() {
        String orderId = "testOrderId";
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        Order cancelledOrder = new Order(orderId, "CANCELLED", itemList, "testId");
        when(orderService.cancelOrder(orderId)).thenReturn(cancelledOrder);

        ResponseEntity<Object> response = orderController.cancelOrder(orderId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cancelledOrder, response.getBody());
    }

    @Test
    void cancelOrder_exception() {
        String orderId = "testOrderId";
        String errorMessage = "Some error";
        when(orderService.cancelOrder(orderId)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> response = orderController.cancelOrder(orderId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, ((java.util.Map<String, String>)response.getBody()).get("message"));
    }

    @Test
    void getCustomerById_found() {
        String customerId = "testId";
        Customer customer = new Customer(customerId, "testName");
        when(orderService.getCustomerById(customerId)).thenReturn(Optional.of(customer));

        ResponseEntity<Object> response = orderController.getCustomerById(customerId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(Optional.of(customer), response.getBody());
    }

    @Test
    void getCustomerById_notFound() {
        String customerId = "testId";
        when(orderService.getCustomerById(customerId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = orderController.getCustomerById(customerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody()); // or check if the body is an empty map if you return one in this scenario.
    }

    @Test
    void getOrderById_found() {
        String orderId = "testOrderId";
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        Order order = new Order(orderId, "PLACED", itemList, "testId");
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<Object> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(Optional.of(order), response.getBody());
    }

    @Test
    void getOrderById_notFound() {
        String orderId = "testOrderId";
        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllOrdersByCustomerId_success() {
        String customerId = "testCustomerId";
        List<String> orders = List.of("order1", "order2");
        when(orderService.getAllOrdersByCustomer(customerId)).thenReturn(orders);

        List<String> retrievedOrders = orderController.getAllOrdersByCustomerId(customerId);

        assertEquals(orders, retrievedOrders);
    }
}