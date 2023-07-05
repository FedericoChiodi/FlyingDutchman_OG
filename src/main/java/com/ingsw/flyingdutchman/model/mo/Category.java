package com.ingsw.flyingdutchman.model.mo;

public class Category {
    private Long categoryID;
    private String name;
    private Product[] products;

    public Long getCategoryID() {return categoryID;}
    public void setCategoryID(Long categoryID){this.categoryID = categoryID;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public Product[] getProducts(){return products;}
    public void setProducts(Product[] products){this.products = products;}
    public Product getProducts(int index){return products[index];}
    public void setProducts(Product product, int index){this.products[index] = product;}
}
