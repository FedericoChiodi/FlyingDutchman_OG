package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOMySQLJDBCImpl implements ProductDAO {
    Connection conn;

    public ProductDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Product create(String description, Integer min_price, Integer starting_price, Blob image, Category category, User owner) {
        PreparedStatement ps;
        String sql;
        Product product = new Product();
        product.setDescription(description);
        product.setMin_price(min_price);
        product.setStarting_price(starting_price);
        product.setImage(image);
        product.setCategory(category);
        product.setOwner(owner);

        try {
            sql
                    ="INSERT INTO `PRODUCT` "
                    +"(description,"
                    +"min_price,"
                    +"starting_price,"
                    +"image,"
                    +"categoryID,"
                    +"ownerID) "
                    +"VALUES (?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,description);
            ps.setInt(i++,min_price);
            ps.setInt(i++,starting_price);
            ps.setBlob(i++,image);
            ps.setLong(i++,category.getCategoryID());
            ps.setLong(i++,owner.getUserID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public void update(Product product) {
        PreparedStatement ps;
        String sql;
        try {
            sql
                    ="UPDATE `PRODUCT` SET "
                    +"description = ?,"
                    +"min_price = ?,"
                    +"starting_price = ?,"
                    +"image = ?,"
                    +"categoryID = ?,"
                    +"ownerID = ? "
                    +"WHERE productID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, product.getDescription());
            ps.setInt(i++,product.getMin_price());
            ps.setInt(i++,product.getStarting_price());
            ps.setBlob(i++,product.getImage());
            ps.setLong(i++,product.getCategory().getCategoryID());
            ps.setLong(i++,product.getOwner().getUserID());
            ps.setLong(i++,product.getProductID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Product product) {
        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="DELETE FROM `PRODUCT` "
                    +"WHERE "
                    +"productID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,product.getProductID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findByProductID(Long productID) {
        PreparedStatement ps;
        Product product  = new Product();

        try {
            String sql
                    = "SELECT * " +
                    "FROM `PRODUCT` " +
                    "WHERE " +
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
        PreparedStatement ps;
        List<Product> products = new ArrayList<>();

        try {
            String sql
                    ="SELECT * "
                    +"FROM `PRODUCT` "
                    +"WHERE "
                    +"userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, owner.getUserID().toString());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Product product = read(resultSet);
                products.add(product);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return products.toArray(new Product[0]);
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