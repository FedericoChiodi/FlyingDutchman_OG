package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.AuctionDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;

import java.sql.*;

import static java.sql.Types.NULL;

public class AuctionDAOMySQLJDBCImpl implements AuctionDAO{
    Connection conn;

    @Override
    public Auction create(Timestamp openingTimestamp, Product product) {
        PreparedStatement ps;
        String sql;
        Auction auction = new Auction();
        auction.setProduct_auctioned(product);
        auction.setOpening_timestamp(openingTimestamp);

        try{
            sql
                    = "INSERT INTO `AUCTION`"
                    + "(opening_timestamp,"
                    + "closing_timestamp,"
                    + "is_product_sold,"
                    + "productID)"
                    + "VALUES (?,?,?,?)";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setTimestamp(i++,openingTimestamp);
            ps.setNull(i++,NULL);
            ps.setString(i++,"N");
            ps.setLong(i++,product.getProductID());

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

    }

    @Override
    public Auction findAuctionByID(Long auctionID) {
        return null;
    }
}
