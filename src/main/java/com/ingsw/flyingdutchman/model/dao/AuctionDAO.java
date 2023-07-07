package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Bid;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;

import java.sql.Timestamp;

public interface AuctionDAO {
    public Auction create(
            Timestamp Opening_timestamp,
            Product product
    );
    public void delete(Auction auction);
    public void update(Auction auction);
    public Auction findAuctionByID(Long auctionID);
}
