package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Bid;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Timestamp;

public interface BidDAO {
    public Bid create(
            Integer amount_bid,
            Timestamp bid_time,
            User bidder,
            Auction auction
    );
    public void delete(Bid bid);
    public void update(Bid bid);
    public Bid findByBidID(Long bidID);
}
