package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.CategoryDAO;
import com.ingsw.flyingdutchman.model.mo.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOMySQLJDBCImpl implements CategoryDAO {
    Connection conn;

    public CategoryDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Category create(String name) {
        PreparedStatement ps;
        Category category = new Category();
        try {
            String sql
                    = "INSERT INTO `CATEGORY` "
                    + "(name) "
                    + "VALUES (?)";
            ps = conn.prepareStatement(sql);

            ps.setString(1, name);

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public void update(Category category) {
        PreparedStatement ps;
        try {
            String sql
                    = "UPDATE `CATEGORY` "
                    + "SET "
                    + "name = ? "
                    + "WHERE "
                    + "categoryID = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, category.getName());
            ps.setLong(2, category.getCategoryID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Category category) {
        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="DELETE FROM `CATEGORY` "
                    +"WHERE "
                    +"categoryID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,category.getCategoryID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category findByCategoryID(Long categoryID) {
        PreparedStatement ps;
        Category category = new Category();
        try {
            String sql
                    = "SELECT * " +
                    "FROM `CATEGORY` " +
                    "WHERE " +
                    "categoryID = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, categoryID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                category = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category findByName(String name) {
        PreparedStatement ps;
        Category category = new Category();
        try {
            String sql
                    = "SELECT * " +
                    "FROM `CATEGORY` " +
                    "WHERE " +
                    "name = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, name);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                category = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category[] getAllCategories() {
        PreparedStatement ps;
        List<Category> categories = new ArrayList<>();

        try {
            String sql = "SELECT * FROM `CATEGORY`";

            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                Category category = read(resultSet);
                categories.add(category);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return categories.toArray(new Category[0]);
    }

    public Category read(ResultSet rs){
        Category category = new Category();
        try {
            category.setCategoryID(rs.getLong("categoryID"));
            category.setName(rs.getString("name"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Category");
        }

        return category;
    }

}
