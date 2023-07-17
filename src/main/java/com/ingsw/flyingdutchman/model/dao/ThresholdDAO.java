package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.mo.Threshold;

import java.sql.Timestamp;

public interface ThresholdDAO {
    public Threshold create(
            Float price,
            Timestamp reservation_date,
            User buyer,
            Auction auction
    );
    public void delete(Threshold threshold);
    public void update(Threshold threshold);
    public Threshold findByID(Long thresholdID);
    public Threshold[] findByUser(User user);
    public Threshold[] findThresholdsByAuction(Auction auction);
    public Threshold[] findAllThresholds();
}
