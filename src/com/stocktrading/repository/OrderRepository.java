package com.stocktrading.repository;

import com.stocktrading.models.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public boolean save(Order order) {
        if (order == null || order.getOrderId() == null || findById(order.getOrderId()) != null) {
            return false;
        }
        return orders.add(order);
    }

    public Order findById(String orderId) {
        if (orderId == null) {
            return null;
        }
        for (Order order : orders) {
            if (orderId.equals(order.getOrderId())) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public boolean update(Order updatedOrder) {
        if (updatedOrder == null || updatedOrder.getOrderId() == null) {
            return false;
        }
        for (int i = 0; i < orders.size(); i++) {
            if (updatedOrder.getOrderId().equals(orders.get(i).getOrderId())) {
                orders.set(i, updatedOrder);
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(String orderId) {
        return orderId != null && orders.removeIf(order -> orderId.equals(order.getOrderId()));
    }
}
