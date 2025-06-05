package service;

import dao.OrderDAO;
import exception.EntityNotFoundException;
import model.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public void placeOrder(Order order) throws SQLException {
        orderDAO.addOrder(order);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public Order getOrderById(int id) throws SQLException {
        Order order = orderDAO.getOrderById(id);
        if (order == null) throw new EntityNotFoundException("Order", id);
        return order;
    }

    public void updateOrder(Order order, int id) throws SQLException {
        getOrderById(id);
        orderDAO.updateOrder(order, id);
    }

    public void deleteOrder(int id) throws SQLException {
        getOrderById(id);
        orderDAO.deleteOrder(id);
    }
}