package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.OrderDAO;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Order[] findByUser(User user) {
        PreparedStatement ps;
        List<Order> orders = new ArrayList<>();

        try {
            String sql
                    ="SELECT * "
                    +"FROM `ORDER` "
                    +"WHERE "
                    +"userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getUserID());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Order order = read(resultSet);
                orders.add(order);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return orders.toArray(new Order[0]);
    }

    public Order read(ResultSet rs){
        Order order = new Order();
        Product product = new Product();
        User user = new User();

        order.setBuyer(user);
        order.setProduct(product);
        try{
            order.setOrderID(rs.getLong("orderID"));
            order.setOrder_time(rs.getTimestamp("order_time"));
            order.setSelling_price(rs.getInt("selling_price"));
            order.getBuyer().setUserID(rs.getLong("userID"));
            order.getProduct().setProductID(rs.getLong("productID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Order");
        }

        return order;
    }
}
