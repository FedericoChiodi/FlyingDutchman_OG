package com.ingsw.flyingdutchman.model.mo;

public class Product {
    private Long productID;
    private String description;
    private int min_price;
    private int starting_price;
    private Category category;
    private Auction[] auctions;
    private Order order;

    public Long getProductID(){return productID;}
    public void setProductID(Long productID){this.productID = productID;}
    public String getDescription(){return description;}
    public void setDescription(String description) {this.description = description;}
    public int getMin_price(){return min_price;}
    public void setMin_price(int min_price){this.min_price = min_price;}
    public int getStarting_price(){return starting_price;}
    public void setStarting_price(int starting_price){this.starting_price = starting_price;}
    public Category getCategory() {return category;}
    public void setCategory(Category category){this.category = category;}
    public Auction[] getAuctions(){return auctions;}
    public void setAuctions(Auction[] auctions){this.auctions = auctions;}
    public Auction getAuctions(int index){return auctions[index];}
    public void setAuctions(Auction auctions, int index){this.auctions[index] = auctions;}
    public Order getOrder(){return order;}
    public void setOrder(Order order){this.order = order;}
}
