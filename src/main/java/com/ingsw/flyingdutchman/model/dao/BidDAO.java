package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Bid;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Timestamp;

public interface BidDAO {
    public Bid create(
            Long bidID,
            User bidder,
            Auction auction,
            int amount_bid,
            Timestamp timestamp
    );
    public void delete(Bid bid);
    public void update(Bid bid);
    public Bid findByBidID(Long bidID);
    public Bid[] findByAuction(Auction auction);
    public Bid[] findByBidder(User bidder);
}
