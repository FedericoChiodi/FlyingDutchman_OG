package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.OrderDAO;
import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OrderDAOMySQLJDBCImpl implements OrderDAO {
    Connection conn;
    public OrderDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Order create(Timestamp order_time, Integer selling_price, User buyer, Product product) {
        PreparedStatement ps;
        String sql;
        Order order = new Order();
        order.setOrder_time(order_time);
        order.setSelling_price(selling_price);
        order.setBuyer(buyer);
        order.setProduct(product);

        try {
            sql
                    ="INSERT INTO `ORDER` "
                    +"(order_time,"
                    +"selling_price,"
                    +"userID,"
                    +"productID) "
                    +"VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setTimestamp(i++,order_time);
            ps.setInt(i++,selling_price);
            ps.setLong(i++,buyer.getUserID());
            ps.setLong(i++,product.getProductID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public void update(Order order) {

    }

    @Override
    public Order findByOrderID(Long orderID) {
        return null;
    }

    @Override
    public Order findByProduct(Product product) {
        return null;
    }
}
