package com.stocktrading.services;

import com.stocktrading.models.Order;
import com.stocktrading.repository.OrderRepository;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService() {
        this(new OrderRepository());
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public boolean updateOrder(Order order) {
        return orderRepository.update(order);
    }

    public boolean deleteOrder(String orderId) {
        return orderRepository.deleteById(orderId);
    }
}
