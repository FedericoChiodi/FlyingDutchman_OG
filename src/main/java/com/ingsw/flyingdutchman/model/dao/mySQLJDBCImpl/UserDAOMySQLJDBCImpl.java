package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAOMySQLJDBCImpl implements UserDAO {

    private final String COUNTER_ID = "userID";
    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public User create(String username, String password, String firstname, String surname, Date birthdate, String address, short civic_number, short cap, String city, String state, String email, String cel_number, String role) {
        throw new UnsupportedOperationException("ToDo");
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("ToDo");
    }

    @Override
    public void delete(User user) {
        throw new UnsupportedOperationException("ToDo");
    }

    @Override
    public User findLoggedUser() {
        throw new UnsupportedOperationException("ToDo");
    }

    @Override
    public User findByUserID(Long userID) {

        PreparedStatement ps;
        User user = null;

        try {
            String sql =
                    "SELECT *" +
                    "FROM user" +
                    "WHERE" +
                    "userID = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {

        PreparedStatement ps;
        User user = null;

        try {
            String sql =
                    "SELECT *" +
                    "FROM user" +
                    "WHERE" +
                    "username = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                user = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User[] findByRole(String role) {

        PreparedStatement ps;
        List<User> users = new ArrayList<>();

        try {
            String sql =
                    "SELECT *" +
                    "FROM user" +
                    "WHERE" +
                    "role = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, role);

            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                User user = read(resultSet);
                users.add(user);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return users.toArray(new User[0]);
    }

    User read(ResultSet rs){
        User user = new User();
        try{
            user.setUserID(rs.getLong("userID"));
        } catch (SQLException e) {
        }
        try{
            user.setUsername(rs.getString("username"));
        } catch (SQLException e) {
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
        }
        try {
            user.setFirstname(rs.getString("firstname"));
        } catch (SQLException e) {
        }
        try {
            user.setSurname(rs.getString("surname"));
        } catch (SQLException e) {
        }
        try {
            user.setBirthdate(rs.getDate("birthdate"));
        } catch (SQLException e) {
        }
        try {
            user.setAddress(rs.getString("address"));
        } catch (SQLException e) {
        }
        try {
            user.setCivic_number(rs.getShort("civic_number"));
        } catch (SQLException e) {
        }
        try{
            user.setCap(rs.getShort("cap"));
        } catch (SQLException e) {
        }
        try {
            user.setCity(rs.getString("city"));
        } catch (SQLException e) {
        }
        try {
            user.setState(rs.getString("state"));
        } catch (SQLException e) {
        }
        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException e) {
        }
        try {
            user.setCel_number(rs.getString("cel_number"));
        } catch (SQLException e) {
        }
        try {
            user.setRole(rs.getString("role"));
        } catch (SQLException e) {
        }
        try{
            user.setDeleted(rs.getString("deleted").equals("Y"));
        } catch (SQLException e) {
        }

        return user;
    }
}
