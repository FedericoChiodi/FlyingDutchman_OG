package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.*;

public interface ProductDAO {
    public Product create(
            Long productID,
            String description,
            int min_price,
            int starting_price,
            Category category,
            Auction[] auctions,
            Order order

    );
    public void update(Product product);
    public void delete(Product product);
    public Product findByProductID(Long productID);
    public Product[] findByCategory(Category category);

}
