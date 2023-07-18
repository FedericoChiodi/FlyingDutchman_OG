package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.*;

import java.sql.Timestamp;

public interface AuctionDAO {
    public Auction create(
            Timestamp Opening_timestamp,
            Product product
    );
    public void delete(Auction auction);
    public void update(Auction auction);
    public Auction findAuctionByID(Long auctionID);
    public Auction[] findByProductOwner(Product product);
    public Auction[] findByOwner(User user);
    public Auction[] findByOwnerNotPremium(User user);
    public Auction[] findOpenAuctionsByOwnerNotDeleted(User user);
    public Auction[] findAllOpenAuctionsExceptUser(User user);
    public Auction[] findAllAuctions();
    public Auction[] findAllAuctionsExceptPremium();

}
