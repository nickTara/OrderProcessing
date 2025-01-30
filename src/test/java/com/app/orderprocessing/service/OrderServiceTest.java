package com.app.orderprocessing.service;

import com.app.orderprocessing.entities.Customer;
import com.app.orderprocessing.entities.Order;
import com.app.orderprocessing.models.OrderStatus;
import com.app.orderprocessing.models.PlaceOrderRequest;
import com.app.orderprocessing.repositories.CustomerRepository;
import com.app.orderprocessing.repositories.OrderRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createCustomer_success() {
        String customerId = UUID.randomUUID().toString();
        String customerName = "Test Customer";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Customer createdCustomer = orderService.create(customerId, customerName);

        assertNotNull(createdCustomer);
        assertEquals(customerId, createdCustomer.getCustomerId());
        assertEquals(customerName, createdCustomer.getCustomerName());

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals(customerId, savedCustomer.getCustomerId());
        assertEquals(customerName, savedCustomer.getCustomerName());
    }

    @Test
    void createCustomer_duplicate() {
        String customerId = UUID.randomUUID().toString();
        String customerName = "Test Customer";

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));

        assertThrows(DuplicateRequestException.class, () -> orderService.create(customerId, customerName));

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void createCustomer_missingFields() {
        assertThrows(IllegalArgumentException.class, () -> orderService.create(null, "Test"));
        assertThrows(IllegalArgumentException.class, () -> orderService.create("id", null));
    }

    @Test
    void placeOrder_success() {
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        PlaceOrderRequest payload = new PlaceOrderRequest("testId", itemList);
        Order placedOrder = orderService.placeOrder(payload.getCustomerId(), payload.getItems());

        assertNotNull(placedOrder);
        assertEquals(OrderStatus.PLACED.toString(), placedOrder.getOrderStatus());
        assertEquals(payload.getCustomerId(), placedOrder.getCustomerId());
        assertNotNull(placedOrder.getOrderId());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertEquals(OrderStatus.PLACED.toString(), savedOrder.getOrderStatus());
        assertEquals(payload.getCustomerId(), savedOrder.getCustomerId());
    }

    @Test
    void placeOrder_missingCustomerId() {
        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(null, null));
    }

    @Test
    void getCustomerById_success() {
        String customerId = UUID.randomUUID().toString();
        Customer customer = new Customer(customerId, "Test");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Optional<Customer> retrievedCustomer = orderService.getCustomerById(customerId);

        assertTrue(retrievedCustomer.isPresent());
        assertEquals(customer, retrievedCustomer.get());
    }

    @Test
    void getCustomerById_notFound() {
        String customerId = UUID.randomUUID().toString();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<Customer> retrievedCustomer = orderService.getCustomerById(customerId);

        assertTrue(retrievedCustomer.isEmpty());
    }


    @Test
    void getCustomerById_missingId() {
        assertThrows(NoSuchElementException.class, () -> orderService.getCustomerById(null));
    }

    @Test
    void getOrderById_success() {
        String orderId = UUID.randomUUID().toString();
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        Order order = new Order(orderId, OrderStatus.PLACED.toString(), itemList, UUID.randomUUID().toString());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<Order> retrievedOrder = orderService.getOrderById(orderId);

        assertTrue(retrievedOrder.isPresent());
        assertEquals(order, retrievedOrder.get());
    }

    @Test
    void getOrderById_notFound() {
        String orderId = UUID.randomUUID().toString();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> retrievedOrder = orderService.getOrderById(orderId);

        assertTrue(retrievedOrder.isEmpty());
    }

    @Test
    void getOrderById_missingId() {
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(null));
    }

    @Test
    void cancelOrder_success() {
        String orderId = UUID.randomUUID().toString();
        List<String> itemList = List.of(new String[]{"biscuits", "shampoo"});
        Order existingOrder = new Order(orderId, OrderStatus.PLACED.toString(), itemList, UUID.randomUUID().toString());
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        Order cancelledOrder = orderService.cancelOrder(orderId);

        assertNotNull(cancelledOrder);
        assertEquals(OrderStatus.CANCELLED.toString(), cancelledOrder.getOrderStatus());
        assertEquals(existingOrder.getCustomerId(), cancelledOrder.getCustomerId());
        assertEquals(existingOrder.getOrderId(), cancelledOrder.getOrderId());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(orderId, savedOrder.getOrderId());
        assertEquals(OrderStatus.CANCELLED.toString(), savedOrder.getOrderStatus());

    }

    @Test
    void cancelOrder_notFound() {
        String orderId = UUID.randomUUID().toString();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Order cancelledOrder = orderService.cancelOrder(orderId);

        assertNull(cancelledOrder);

    }

    @Test
    void getAllOrdersByCustomer() {
        String customerId = UUID.randomUUID().toString();
        List<String> orders = List.of("order1", "order2");
        when(orderRepository.findAllOrdersByCustomerId(customerId)).thenReturn(orders);

        List<String> retrievedOrders = orderService.getAllOrdersByCustomer(customerId);

        assertEquals(orders, retrievedOrders);
    }
}
