package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Auction {
    private Long auctionID;
    private Bid[] bids;
    private Threshold[] thresholds;
    private Product product_auctioned;
    private Timestamp opening_timestamp;
    private Timestamp closing_timestamp;
    private boolean product_sold;


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
    public Timestamp getOpening_timestamp(){return opening_timestamp;}
    public void setOpening_timestamp(Timestamp opening_timestamp){this.opening_timestamp = opening_timestamp;}
    public Timestamp getClosing_timestamp(){return closing_timestamp;}
    public void setClosing_timestamp(Timestamp closing_timestamp){this.closing_timestamp = closing_timestamp;}
    public boolean isProduct_sold(){return product_sold;}
    public void setProduct_sold(boolean product_sold){this.product_sold = product_sold;}
}
