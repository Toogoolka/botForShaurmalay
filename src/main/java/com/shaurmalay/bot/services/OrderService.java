package com.shaurmalay.bot.services;

import com.shaurmalay.bot.dao.OrderDao;
import com.shaurmalay.bot.dao.OrderStatusDao;
import com.shaurmalay.bot.dao.UserDao;
import com.shaurmalay.bot.exceptions.UserNotFoundException;
import com.shaurmalay.bot.model.Order;
import com.shaurmalay.bot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class OrderService {

    private OrderDao orderDao;
    private UserDao userDao;
    private UserService userService;
    private OrderStatusDao orderStatusDao;

    @Autowired
    public OrderService(OrderDao orderDao, UserDao userDao, UserService userService, OrderStatusDao orderStatusDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.userService = userService;
        this.orderStatusDao = orderStatusDao;
    }

    public void checkStatusOrder(Long statusId, Long orderId) {

    }

    @Transactional
    public void changeStatusOrder(Long statusId, Order order) {
        order.setOrderStatus(orderStatusDao.findById(statusId).get());
        orderDao.save(order);
    }

    @Transactional
    public void registerOrder(Long userId) throws UserNotFoundException {
        Order newOrder = new Order();
        Optional<User> customer = userDao.findByChatId(userId);

        if (customer.isEmpty()) {
            throw new UserNotFoundException();
        }
        newOrder.setCustomer(customer.get());
        newOrder.setOrderedAt(LocalDateTime.now());
        newOrder.setOrderStatus(orderStatusDao.findById(3l).get());
        userService.addOrderToList(newOrder, userId);
        orderDao.save(newOrder);
    }


}
