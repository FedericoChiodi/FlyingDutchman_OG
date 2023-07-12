package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.ThresholdDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.*;

public class ThresholdDAOMySQLJDBCImpl implements ThresholdDAO {
    Connection conn;
    public ThresholdDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Threshold create(Integer price, Timestamp reservation_date, User buyer, Auction auction) {
        PreparedStatement ps;
        Threshold threshold = new Threshold();
        try {
            String sql
                    = "INSERT INTO `THRESHOLD` "
                    + "(price, reservation_date, userID, auctionID) "
                    + "VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++, price);
            ps.setTimestamp(i++, reservation_date);
            ps.setLong(i++, buyer.getUserID());
            ps.setLong(i++, auction.getAuctionID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return threshold;
    }

    @Override
    public void delete(Threshold threshold) {
        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="DELETE FROM `THRESHOLD` "
                    +"WHERE "
                    +"thresholdID = ?";
            ps = conn.prepareStatement(sql);

            ps.setLong(1, threshold.getThresholdID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Threshold threshold) {
        PreparedStatement ps;
        try {
            String sql
                    = "UPDATE `THRESHOLD` "
                    + "SET "
                    + "price = ?, "
                    + "reservation_date = ?, "
                    + "userID = ?, "
                    + "auctionID = ?, "
                    + "WHERE "
                    + "thresholdID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++, threshold.getPrice());
            ps.setTimestamp(i++, threshold.getReservation_date());
            ps.setLong(i++, threshold.getUser().getUserID());
            ps.setLong(i++, threshold.getAuction().getAuctionID());
            ps.setLong(i++, threshold.getThresholdID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Threshold findByID(Long thresholdID) {
        PreparedStatement ps;
        Threshold threshold = new Threshold();
        try {
            String sql
                    = "SELECT * " +
                    "FROM `THRESHOLD` " +
                    "WHERE " +
                    "thresholdID = ?";

            ps = conn.prepareStatement(sql);
            ps.setLong(1, thresholdID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                threshold = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return threshold;
    }

    private Threshold read(ResultSet rs){
        Threshold threshold = new Threshold();
        try {
            threshold.setThresholdID(rs.getLong("thresholdID"));
            threshold.setPrice(rs.getInt("price"));
            threshold.setReservation_date(rs.getTimestamp("reservation_date"));
            threshold.getUser().setUserID(rs.getLong("userID"));
            threshold.getAuction().setAuctionID(rs.getLong("auctionID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Threshold");
        }

        return threshold;
    }
}
