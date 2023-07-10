package com.ingsw.flyingdutchman.model.mo;

public class Product {
    private Long productID;
    private String description;
    private Integer min_price;
    private Integer starting_price;
    private Category category;
    private User owner;

    public Long getProductID(){return productID;}
    public void setProductID(Long productID){this.productID = productID;}
    public String getDescription(){return description;}
    public void setDescription(String description) {this.description = description;}
    public Integer getMin_price(){return min_price;}
    public void setMin_price(int min_price){this.min_price = min_price;}
    public Integer getStarting_price(){return starting_price;}
    public void setStarting_price(int starting_price){this.starting_price = starting_price;}
    public Category getCategory() {return category;}
    public void setCategory(Category category){this.category = category;}
    public User getOwner(){return owner;}
    public void setOwner(User owner){this.owner = owner;}
}
