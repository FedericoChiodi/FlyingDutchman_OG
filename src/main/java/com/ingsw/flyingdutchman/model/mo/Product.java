package com.ingsw.flyingdutchman.model.mo;

import javax.swing.*;
import java.sql.Blob;

public class Product {
    private Long productID;
    private String description;
    private Float min_price;
    private Float starting_price;
    private Float current_price;
    private Blob image;
    private Boolean deleted;
    private Category category;
    private User owner;

    public Long getProductID(){return productID;}
    public void setProductID(Long productID){this.productID = productID;}
    public String getDescription(){return description;}
    public void setDescription(String description) {this.description = description;}
    public Float getMin_price(){return min_price;}
    public void setMin_price(float min_price){this.min_price = min_price;}
    public Float getStarting_price(){return starting_price;}
    public void setStarting_price(float starting_price){this.starting_price = starting_price;}
    public Float getCurrent_price(){return current_price;}
    public void setCurrent_price(float current_price){this.current_price = current_price;}
    public Blob getImage(){return image;}
    public void setImage(Blob image){this.image = image;}
    public Category getCategory() {return category;}
    public void setCategory(Category category){this.category = category;}
    public User getOwner(){return owner;}
    public void setOwner(User owner){this.owner = owner;}
    public Boolean isDeleted(){return deleted;}
    public void setDeleted(Boolean deleted){this.deleted = deleted;}
}
