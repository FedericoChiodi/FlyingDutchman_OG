package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Bid {
    private Long bidID;
    private User bidder;
    private Auction auction;
    private int amount_bid;
    private Timestamp bid_timestamp;

    public Long getBidID(){return bidID;}
    public void setBidID(Long bidID){this.bidID = bidID;}
    public User getBidder(){return bidder;}
    public void setBidder(User bidder){this.bidder = bidder;}
    public Auction getAuction(){return auction;}
    public void setAuction(Auction auction){this.auction = auction;}
    public int getAmount_bid(){return amount_bid;}
    public Timestamp getBid_timestamp(){return bid_timestamp;}
    public void setBid_timestamp(Timestamp bid_timestamp){this.bid_timestamp = bid_timestamp;}

}
