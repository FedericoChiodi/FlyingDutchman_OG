package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.AuctionDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;

import java.sql.*;

public class AuctionDAOMySQLJDBCImpl implements AuctionDAO{
    Connection conn;

    @Override
    public Auction create(Long auctionID, Product product, Timestamp openingTimestamp) {
        PreparedStatement ps;
        String sql;
        Auction auction = new Auction();
        auction.setProduct_auctioned(product);
        auction.setOpening_timestamp(openingTimestamp);

        try{
            sql
                    = "INSERT INTO auction"
                    + "(productId,"
                    + "openingTimestamp,"
                    + "productSold)"
                    + "VALUES (?,?,'N')";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setLong(i++,product.getProductID());
            ps.setTimestamp(i++,openingTimestamp);

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return auction;
    }

    @Override
    public void delete(Auction auction) {

    }

    @Override
    public void update(Auction auction) {
        // ricordarsi di passare un'asta con le bids già inserite
    }

    @Override
    public Auction findAuctionByID(Long auctionID) {
        return null;
    }
}
