package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Auction {
    private Long auctionID;
    private Timestamp opening_timestamp;
    private Timestamp closing_timestamp;
    private Boolean product_sold;
    private Boolean deleted;
    private Product product_auctioned;

    public Long getAuctionID(){return auctionID;}
    public void setAuctionID(Long auctionID){this.auctionID = auctionID;}
    public Product getProduct_auctioned(){return product_auctioned;}
    public void setProduct_auctioned(Product product_auctioned){this.product_auctioned = product_auctioned;}
    public Timestamp getOpening_timestamp(){return opening_timestamp;}
    public void setOpening_timestamp(Timestamp opening_timestamp){this.opening_timestamp = opening_timestamp;}
    public Timestamp getClosing_timestamp(){return closing_timestamp;}
    public void setClosing_timestamp(Timestamp closing_timestamp){this.closing_timestamp = closing_timestamp;}
    public Boolean isProduct_sold(){return product_sold;}
    public void setProduct_sold(boolean product_sold){this.product_sold = product_sold;}
    public Boolean isDeleted(){return deleted;}
    public void setDeleted(Boolean deleted){this.deleted = deleted;}
}
