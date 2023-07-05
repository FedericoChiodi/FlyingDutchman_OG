package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.Category;

public interface CategoryDAO {
    public Category create(
            Long categoryID,
            String Name
    );
    public void update(Category category);
    public void delete(Category category);
    public Category findByCategoryID(Long categoryID);
    public Category findByName(String name);
}
