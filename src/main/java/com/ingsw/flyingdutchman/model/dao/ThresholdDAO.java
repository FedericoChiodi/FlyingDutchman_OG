package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Threshold;

import java.sql.Timestamp;

public interface ThresholdDAO {
    public Threshold create(
            Long thresholdID,
            User buyer,
            Auction auction,
            int price,
            Timestamp timestamp
    );
    public void delete(Threshold threshold);
    public void update(Threshold threshold);
    public Threshold findByID(Long thresholdID);
    public Threshold[] findByBuyer(User buyer);
    public Threshold[] findByAuction(Auction auction);
}
