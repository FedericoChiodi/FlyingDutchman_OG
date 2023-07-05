package com.ingsw.flyingdutchman.model.mo;

import java.sql.Timestamp;

public class Order {
    private Long orderID;
    private User seller;
    private User buyer;
    private Product product;
    private int selling_price;
    private Timestamp order_time;

    public Long getOrderID(){return orderID;}
    public void setOrderID(Long orderID){this.orderID = orderID;}
    public User getSeller(){return seller;}
    public void setSeller(User seller){this.seller = seller;}
    public User getBuyer(){return buyer;}
    public void setBuyer(User buyer){this.buyer = buyer;}
    public Product getProduct(){return product;}
    public void setProduct(Product product){this.product = product;}
    public int getSelling_price(){return selling_price;}
    public void setSelling_price(int selling_price){this.selling_price = selling_price;}
    public Timestamp getOrder_time(){return order_time;}
    public void setOrder_time(Timestamp orderTime){this.order_time = orderTime;}
}
