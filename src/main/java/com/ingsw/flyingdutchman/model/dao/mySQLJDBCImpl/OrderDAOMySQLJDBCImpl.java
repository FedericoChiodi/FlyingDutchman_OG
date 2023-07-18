package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.OrderDAO;
import com.ingsw.flyingdutchman.model.mo.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOMySQLJDBCImpl implements OrderDAO {
    Connection conn;
    public OrderDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Order create(Timestamp order_time, Float selling_price,Boolean bought_from_threshold, User buyer, Product product) {
        PreparedStatement ps;
        String sql;
        Order order = new Order();
        order.setOrder_time(order_time);
        order.setSelling_price(selling_price);
        order.setBought_from_threshold(bought_from_threshold);
        order.setBuyer(buyer);
        order.setProduct(product);

        try {
            sql
                    ="INSERT INTO `ORDER` "
                    +"(order_time,"
                    +"selling_price,"
                    +"bought_from_threshold,"
                    +"userID,"
                    +"productID) "
                    +"VALUES (?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            // Arrotondamento a 2 decimali prima di inserire nel db
            selling_price = (Math.round(selling_price * 100.0) / 100.0f);

            int i = 1;
            ps.setTimestamp(i++,order_time);
            ps.setFloat(i++,selling_price);
            ps.setString(i++,bought_from_threshold ? "Y" : "N");
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
        PreparedStatement ps;
        Order order = new Order();
        String sql;

        try {
            sql
                    = "SELECT * "
                    + "FROM `ORDER` "
                    + "WHERE "
                    + "orderID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,orderID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                order = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return order;
    }

    @Override
    public Order findByProduct(Product product) {
        PreparedStatement ps;
        Order order = new Order();
        String sql;

        try {
            sql
                    = "SELECT * "
                    + "FROM `ORDER` "
                    + "WHERE "
                    + "productID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,product.getProductID());

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                order = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return order;
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
            order.setSelling_price(rs.getFloat("selling_price"));
            order.setBought_from_threshold(rs.getString("bought_from_threshold").equals("Y"));
            order.getBuyer().setUserID(rs.getLong("userID"));
            order.getProduct().setProductID(rs.getLong("productID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Order");
        }

        return order;
    }
}
