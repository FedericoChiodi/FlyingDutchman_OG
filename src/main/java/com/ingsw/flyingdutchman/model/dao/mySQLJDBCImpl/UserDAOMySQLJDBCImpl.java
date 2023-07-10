package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class UserDAOMySQLJDBCImpl implements UserDAO {
    Connection conn;

    public UserDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public User create(String username, String password, String firstname, String surname, Date birthdate, String address, Short civic_number, Short cap, String city, String state, String email, String cel_number, String role) {
        PreparedStatement ps;
        String sql;
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setSurname(surname);
        user.setBirthdate(birthdate);
        user.setAddress(address);
        user.setCivic_number(civic_number);
        user.setCap(cap);
        user.setCity(city);
        user.setState(state);
        user.setEmail(email);
        user.setCel_number(cel_number);
        user.setRole(role);
        user.setDeleted(false);

        try{
            sql
                    = "INSERT INTO `USER` "
                    + "(username,"
                    + "password,"
                    + "firstname,"
                    + "surname,"
                    + "birthdate,"
                    + "address,"
                    + "civic_number,"
                    + "cap,"
                    + "city,"
                    + "state,"
                    + "email,"
                    + "cel_number,"
                    + "role,"
                    + "deleted) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++,username);
            ps.setString(i++,password);
            ps.setString(i++,firstname);
            ps.setString(i++,surname);
            ps.setDate(i++,birthdate);
            ps.setString(i++,address);
            ps.setShort(i++,civic_number);
            ps.setShort(i++,cap);
            ps.setString(i++,city);
            ps.setString(i++,state);
            ps.setString(i++,email);
            ps.setString(i++,cel_number);
            ps.setString(i++,role);
            ps.setString(i++,"N");

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void update(User user) {
        PreparedStatement ps;
        String sql;
        try {
            sql = "UPDATE `USER` SET " +
                    "username = ?," +
                    "password = ?," +
                    "firstname = ?," +
                    "surname = ?," +
                    "birthdate = ?," +
                    "address = ?," +
                    "civic_number = ?," +
                    "cap = ?," +
                    "city = ?," +
                    "state = ?," +
                    "email = ?," +
                    "cel_number = ?," +
                    "role = ? " +
                    "WHERE userID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setString(i++, user.getUsername());
            ps.setString(i++, user.getPassword());
            ps.setString(i++, user.getFirstname());
            ps.setString(i++, user.getSurname());
            ps.setDate(i++, user.getBirthdate());
            ps.setString(i++, user.getAddress());
            ps.setShort(i++, user.getCivic_number());
            ps.setShort(i++, user.getCap());
            ps.setString(i++, user.getCity());
            ps.setString(i++, user.getState());
            ps.setString(i++, user.getEmail());
            ps.setString(i++, user.getCel_number());
            ps.setString(i++, user.getRole());
            ps.setLong(i++, user.getUserID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        PreparedStatement ps;
        String sql;

        try {
            sql
                    = "UPDATE `USER` SET "
                    + "deleted = ? "
                    + "WHERE userID = ?";
            ps = conn.prepareStatement(sql);

            ps.setString(1, "Y");
            ps.setLong(2, user.getUserID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUserID(Long userID) {
        PreparedStatement ps;
        User user = new User();

        try {
            String sql =
                    "SELECT * " +
                    "FROM `USER` " +
                    "WHERE " +
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
        User user = new User();

        try {
            String sql =
                    "SELECT * " +
                    "FROM `USER` " +
                    "WHERE " +
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
                    "SELECT * " +
                    "FROM `USER` " +
                    "WHERE " +
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
            System.err.println("Error. During read rs - User");
        }
        try{
            user.setUsername(rs.getString("username"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setFirstname(rs.getString("firstname"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setSurname(rs.getString("surname"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setBirthdate(rs.getDate("birthdate"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setAddress(rs.getString("address"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setCivic_number(rs.getShort("civic_number"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try{
            user.setCap(rs.getShort("cap"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setCity(rs.getString("city"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setState(rs.getString("state"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setEmail(rs.getString("email"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setCel_number(rs.getString("cel_number"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try {
            user.setRole(rs.getString("role"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }
        try{
            user.setDeleted(rs.getString("deleted").equals("Y"));
        } catch (SQLException e) {
            System.err.println("Error. During read rs - User");
        }

        return user;
    }

    @Override
    public User findLoggedUser() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
