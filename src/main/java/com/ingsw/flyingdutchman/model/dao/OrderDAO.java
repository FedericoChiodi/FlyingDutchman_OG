package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Timestamp;

public interface OrderDAO {
    public Order create(
            Long OrderID,
            User seller,
            User buyer,
            Product product,
            int selling_price,
            Timestamp timestamp
    );
    public void delete(Order order);
    public void update(Order order);
    public Order findByOrderID(Long orderID);
    public Order[] findBySeller(User seller);
    public Order[] findByBuyer(User buyer);
    public Order findByProduct(Product product);
}
