package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Auction {
    private Long auctionID;
    private Bid[] bids;
    private Threshold[] thresholds;
    private Product product_auctioned;
    private Timestamp timestamp;

    public Long getAuctionID(){return auctionID;}
    public void setAuctionID(Long auctionID){this.auctionID = auctionID;}
    public Bid[] getBids(){return bids;}
    public void setBids(Bid[] bids){this.bids = bids;}
    public Bid getBids(int index){return bids[index];}
    public void setBids(Bid bids, int index){this.bids[index] = bids;}
    public Threshold[] getThresholds(){return thresholds;}
    public void setThresholds(Threshold[] thresholds){this.thresholds = thresholds;}
    public Threshold getThresholds(int index){return thresholds[index];}
    public void setThresholds(Threshold threshold, int index){this.thresholds[index] = threshold;}
    public Product getProduct_auctioned(){return product_auctioned;}
    public void setProduct_auctioned(Product product_auctioned){this.product_auctioned = product_auctioned;}
    public Timestamp getTimestamp(){return timestamp;}
    public void setTimestamp(Timestamp timestamp){this.timestamp = timestamp;}
}
