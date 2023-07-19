package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
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
    public Product create(String description, Float min_price, Float starting_price, Float current_price, String image, Boolean deleted, Category category, User owner) {
        PreparedStatement ps;
        String sql;
        Product product = new Product();
        product.setDescription(description);
        product.setMin_price(min_price);
        product.setStarting_price(starting_price);
        product.setCurrent_price(current_price);
        product.setImage(image);
        product.setDeleted(false);
        product.setCategory(category);
        product.setOwner(owner);

        try {
            sql
                    ="INSERT INTO `PRODUCT` "
                    +"(description,"
                    +"min_price,"
                    +"starting_price,"
                    +"current_price,"
                    +"image,"
                    +"deleted,"
                    +"categoryID,"
                    +"ownerID) "
                    +"VALUES (?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            //Arrotondamento
            min_price = Math.round(min_price * 100.0) / 100.0f;
            starting_price = Math.round(starting_price * 100.0) / 100.0f;
            current_price = Math.round(current_price * 100.0) / 100.0f;

            int i = 1;
            ps.setString(i++,description);
            ps.setFloat(i++,min_price);
            ps.setFloat(i++,starting_price);
            ps.setFloat(i++,current_price);
            ps.setString(i++,image);
            ps.setString(i++,"N");
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
                    +"current_price = ?,"
                    +"image = ?,"
                    +"categoryID = ?,"
                    +"ownerID = ? "
                    +"WHERE productID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, product.getDescription());
            ps.setFloat(i++,product.getMin_price());
            ps.setFloat(i++,product.getStarting_price());
            ps.setFloat(i++,product.getCurrent_price());
            ps.setString(i++,product.getImage());
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
                    ="UPDATE `PRODUCT` SET "
                    +"deleted = ? "
                    +"WHERE productID = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Y");
            ps.setLong(2,product.getProductID());

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
                    +"ownerID = ?";
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

    @Override
    public Product[] findAllProducts() {
        PreparedStatement ps;
        List<Product> products = new ArrayList<>();

        try {
            String sql
                    ="SELECT * "
                    +"FROM `PRODUCT`";
            ps = conn.prepareStatement(sql);

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

    @Override
    public Product[] findByOwnerNotDeleted(User user) {
        PreparedStatement ps;
        List<Product> products = new ArrayList<>();

        try {
            String sql
                    ="SELECT * "
                    +"FROM `PRODUCT` "
                    +"WHERE "
                    +"ownerID = ? AND deleted = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, user.getUserID());
            ps.setString(2, "N");

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

    @Override
    public Product findByAuction(Auction auction) {
        PreparedStatement ps;
        Product product  = new Product();

        try {
            String sql
                    = "SELECT productID, description, min_price, starting_price, current_price, image, deleted, categoryID, ownerID " +
                    "FROM `PRODUCT` NATURAL JOIN `AUCTION` " +
                    "WHERE " +
                    "auctionID = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, auction.getAuctionID());

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

    public Product read(ResultSet rs){
        Product product = new Product();
        Category category = new Category();
        User owner = new User();
        product.setCategory(category);
        product.setOwner(owner);
        try{
            product.setProductID(rs.getLong("productID"));
            product.setDescription(rs.getString("description"));
            product.setMin_price(rs.getFloat("min_price"));
            product.setStarting_price(rs.getFloat("starting_price"));
            product.setCurrent_price(rs.getFloat("current_price"));
            product.setImage(rs.getString("image"));
            product.setDeleted(rs.getString("deleted").equals("Y"));
            product.getCategory().setCategoryID(rs.getLong("categoryID"));
            product.getOwner().setUserID(rs.getLong("ownerID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Product");
        }

        return product;
    }
}
