package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.BidDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Bid;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Connection;
import java.sql.Timestamp;

public class BidDAOMySQLJDBCImpl implements BidDAO {
    Connection conn;
    public BidDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Bid create(Integer amount_bid, Timestamp bid_time, User bidder, Auction auction) {
        return null;
    }

    @Override
    public void delete(Bid bid) {

    }

    @Override
    public void update(Bid bid) {

    }

    @Override
    public Bid findByBidID(Long bidID) {
        return null;
    }
}
