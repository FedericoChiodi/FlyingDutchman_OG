package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Threshold {
    private Long ThresholdID;
    private User buyer;
    private Auction auction;
    private int price;
    private Timestamp reservation_date;

    public Long getThresholdID(){return ThresholdID;}
    public void setThresholdID(Long ThresholdID){this.ThresholdID = ThresholdID;}
    public User getBuyer(){return buyer;}
    public void setBuyer(User buyer){this.buyer = buyer;}
    public Auction getAuction(){return auction;}
    public void setAuction(Auction auction){this.auction = auction;}
    public int getPrice(){return price;}
    public void setPrice(int price){this.price = price;}
    public Timestamp getReservation_date(){return reservation_date;}
    public void setReservation_date(Timestamp reservationDate){this.reservation_date = reservationDate;}
}
