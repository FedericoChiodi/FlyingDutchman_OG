package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.User;

import java.util.Date;

public interface UserDAO {
    public User create(
            String username,
            String password,
            String firstname,
            String surname,
            Date birthdate,
            String address,
            short civic_number,
            short cap,
            String city,
            String state,
            String email,
            String cel_number,
            String role
    );
    public void update(User user);
    public void delete(User user);
    public User findLoggedUser();
    public User findByUserID(Long userID);
    public User findByUsername(String username);
    public User[] findByRole(String role);

}
