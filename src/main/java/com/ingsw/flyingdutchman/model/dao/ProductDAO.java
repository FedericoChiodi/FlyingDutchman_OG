package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.*;

public interface ProductDAO {
    public Product create(
            String description,
            Integer min_price,
            Integer starting_price,
            Category category,
            User owner
    );
    public void update(Product product);
    public void delete(Product product);
    public Product findByProductID(Long productID);
    public Product[] findByOwner(User owner);
}
