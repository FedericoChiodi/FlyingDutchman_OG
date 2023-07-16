package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.*;

import java.sql.Blob;

public interface ProductDAO {
    public Product create(
            String description,
            Float min_price,
            Float starting_price,
            Float current_price,
            Blob image,
            Boolean deleted,
            Category category,
            User owner
    );
    public void update(Product product);
    public void delete(Product product);
    public Product findByProductID(Long productID);
    public Product[] findByOwner(User owner);
    public Product[] findAllProducts();
    public Product[] findByOwnerNotDeleted(User user);
    public Product findByAuction(Auction auction);
}
