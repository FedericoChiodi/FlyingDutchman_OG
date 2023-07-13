package com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl;

import com.ingsw.flyingdutchman.model.dao.AuctionDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class AuctionDAOMySQLJDBCImpl implements AuctionDAO{
    Connection conn;

    public AuctionDAOMySQLJDBCImpl(Connection conn){this.conn = conn;}

    @Override
    public Auction create(Timestamp openingTimestamp, Product product) {
        PreparedStatement ps;
        String sql;
        Auction auction = new Auction();
        auction.setProduct_auctioned(product);
        auction.setOpening_timestamp(openingTimestamp);

        try{
            sql
                    = "INSERT INTO `AUCTION` "
                    + "(opening_timestamp,"
                    + "closing_timestamp,"
                    + "is_product_sold,"
                    + "productID) "
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
        PreparedStatement ps;
        String sql;

        try {
            sql
                    ="DELETE FROM `AUCTION` "
                    +"WHERE "
                    +"auctionID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, auction.getAuctionID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Auction auction) {
        PreparedStatement ps;
        String sql;
        try {
            sql
                    = "UPDATE `AUCTION` SET "
                    + "opening_timestamp = ?, "
                    + "closing_timestamp = ?, "
                    + "is_product_sold = ?, "
                    + "productID = ? "
                    + "WHERE auctionID = ?";
            ps = conn.prepareStatement(sql);

            int i = 1;
            ps.setTimestamp(i++,auction.getOpening_timestamp());
            ps.setTimestamp(i++,auction.getClosing_timestamp());
            if(auction.isProduct_sold() == true){
                ps.setString(i++,"Y");
            }
            else{
                ps.setString(i++,"N");
            }
            ps.setLong(i++,auction.getProduct_auctioned().getProductID());
            ps.setLong(i++,auction.getAuctionID());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Auction findAuctionByID(Long auctionID) {
        PreparedStatement ps;
        Auction auction = new Auction();
        String sql;

        try {
            sql
                    = "SELECT * "
                    + "FROM `AUCTION` "
                    + "WHERE "
                    + "auctionID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,auctionID);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                auction = read(resultSet);
            }
            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return auction;
    }

    @Override
    public Auction[] findByProductOwner(Product product) {
        PreparedStatement ps;
        List<Auction> auctions = new ArrayList<>();
        String sql;

        try {
            sql
                    = "SELECT auctionID, opening_timestamp, closing_timestamp, is_product_sold, productID "
                    + "FROM `AUCTION` NATURAl JOIN `PRODUCT` "
                    + "WHERE "
                    + "ownerID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,product.getOwner().getUserID());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Auction auction = read(resultSet);
                auctions.add(auction);
            }

            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return auctions.toArray(new Auction[0]);
    }

    @Override
    public Auction[] findByOwner(User user) {
        PreparedStatement ps;
        List<Auction> auctions = new ArrayList<>();
        String sql;

        try {
            sql
                    = "SELECT auctionID, opening_timestamp, closing_timestamp, is_product_sold, productID "
                    + "FROM `AUCTION` NATURAl JOIN `PRODUCT` "
                    + "WHERE "
                    + "ownerID = ?";
            ps = conn.prepareStatement(sql);
            ps.setLong(1,user.getUserID());

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Auction auction = read(resultSet);
                auctions.add(auction);
            }

            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return auctions.toArray(new Auction[0]);
    }

    @Override
    public Auction[] findAllAuctions() {
        PreparedStatement ps;
        List<Auction> auctions = new ArrayList<>();
        String sql;

        try {
            sql
                    = "SELECT * "
                    + "FROM `AUCTION`";
            ps = conn.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Auction auction = read(resultSet);
                auctions.add(auction);
            }

            resultSet.close();
            ps.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return auctions.toArray(new Auction[0]);
    }

    Auction read(ResultSet rs){
        Auction auction = new Auction();
        Product product = new Product();
        auction.setProduct_auctioned(product);
        try{
            auction.setAuctionID(rs.getLong("auctionID"));
            auction.setOpening_timestamp(rs.getTimestamp("opening_timestamp"));
            auction.setClosing_timestamp(rs.getTimestamp("closing_timestamp"));
            auction.setProduct_sold(rs.getString("is_product_sold").equals("Y"));
            auction.getProduct_auctioned().setProductID(rs.getLong("productID"));
        }
        catch (SQLException e){
            System.err.println("Error. During read rs - Auction");
        }

        return auction;
    }
}
