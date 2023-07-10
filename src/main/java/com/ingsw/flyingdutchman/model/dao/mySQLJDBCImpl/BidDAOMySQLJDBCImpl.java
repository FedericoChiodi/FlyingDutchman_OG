package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.BidDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Bid;
import com.ingsw.flyingdutchman.model.mo.User;

import javax.xml.transform.Result;
import java.sql.*;

public class BidDAOMySQLJDBCImpl implements BidDAO {
    Connection conn;
    public BidDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Bid create(Integer amount_bid, Timestamp bid_time, User bidder, Auction auction) {
        PreparedStatement ps;
        String sql;
        Bid bid = new Bid();
        bid.setAmount_bid(amount_bid);
        bid.setBid_timestamp(bid_time);
        bid.setBidder(bidder);
        bid.setAuction(auction);

        try {
            sql
                    ="INSERT INTO `BID` "
                    +"(amount_bid,"
                    +"bid_time,"
                    +"userID,"
                    +"auctionID) "
                    +"VALUES (?,?,?,?)";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, amount_bid);
            ps.setTimestamp(i++,bid_time);
            ps.setLong(i++,bidder.getUserID());
            ps.setLong(i++,auction.getAuctionID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return bid;
    }

    @Override
    public void delete(Bid bid) {
        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="DELETE FROM `BID` "
                    +"WHERE "
                    +"bidID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, bid.getBidID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Bid bid) {

        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="UPDATE `BID` SET "
                    +"amount_bid = ?,"
                    +"bid_time = ?,"
                    +"userID = ?,"
                    +"auctionID = ? "
                    +"WHERE bidID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++,bid.getAmount_bid());
            ps.setTimestamp(i++,bid.getBid_timestamp());
            ps.setLong(i++,bid.getBidder().getUserID());
            ps.setLong(i++,bid.getAuction().getAuctionID());
            ps.setLong(i++,bid.getBidID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bid findByBidID(Long bidID) {
        PreparedStatement ps;
        String sql;
        Bid bid = new Bid();

        try {
            sql
                    ="SELECT * "
                    +"FROM `BID` "
                    +"WHERE bidID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, bidID);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                bid = read(rs);
            }
            rs.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return bid;
    }

    Bid read(ResultSet rs){
        Bid bid = new Bid();
        try {
            bid.setBidID(rs.getLong("bidID"));
            bid.setAmount_bid(rs.getInt("amount_bid"));
            bid.setBid_timestamp(rs.getTimestamp("bid_time"));
            bid.getBidder().setUserID(rs.getLong("userID"));
            bid.getAuction().setAuctionID(rs.getLong("auctionID"));
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return bid;
    }
}
