package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;

public interface ProductDAO {
    public Product create(
            Long productID,
            String description,
            int min_price,
            int starting_price,
            String category,
            int quantity,
            User seller
    );
    public void update(Product product);
    public void delete(Product product);

}
