package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Threshold {
    private Long ThresholdID;
    private User buyer;
    private Product product;
    private int price;
    private Timestamp reservation_date;
    private boolean deleted;

    public Long getThresholdID(){return ThresholdID;}
    public void setThresholdID(Long ThresholdID){this.ThresholdID = ThresholdID;}
    public User getBuyer(){return buyer;}
    public void setBuyer(User buyer){this.buyer = buyer;}
    public Product getProduct(){return product;}
    public void setProduct(Product product){this.product = product;}
    public int getPrice(){return price;}
    public void setPrice(int price){this.price = price;}
    public Timestamp getReservation_date(){return reservation_date;}
    public void setReservation_date(Timestamp reservationDate){this.reservation_date = reservationDate;}
    public boolean isDeleted(){return deleted;}
    public void setDeleted(boolean deleted){this.deleted = deleted;}
}
