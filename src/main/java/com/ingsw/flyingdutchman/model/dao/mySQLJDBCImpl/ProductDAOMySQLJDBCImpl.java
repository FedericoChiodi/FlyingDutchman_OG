package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.*;

public class ProductDAOMySQLJDBCImpl implements ProductDAO {
    Connection conn;

    public ProductDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Product create(String description, Integer min_price, Integer starting_price, Blob image, Category category, User owner) {
        return null;
    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public Product findByProductID(Long productID) {
        PreparedStatement ps;
        Product product  = new Product();

        try {
            String sql
                    = "SELECT *" +
                    "FROM `PRODUCT`" +
                    "WHERE" +
                    "productID = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, productID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                product = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public Product[] findByOwner(User owner) {
        return new Product[0];
    }

    public Product read(ResultSet rs){
        Product product = new Product();
        try{
            product.setProductID(rs.getLong("productID"));
            product.setDescription(rs.getString("description"));
            product.setMin_price(rs.getInt("min_price"));
            product.setStarting_price(rs.getInt("starting_price"));
            product.getCategory().setCategoryID(rs.getLong("categoryID"));
            product.getOwner().setUserID(rs.getLong("userID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Auction");
        }

        return product;
    }
}
