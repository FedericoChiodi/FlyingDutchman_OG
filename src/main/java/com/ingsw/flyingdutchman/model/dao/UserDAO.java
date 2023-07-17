package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.mo.User;

import java.sql.Date;

public interface UserDAO {
    public User create(
            String username,
            String password,
            String firstname,
            String surname,
            Date birthdate,
            String address,
            Short civic_number,
            Short cap,
            String city,
            String state,
            String email,
            String cel_number,
            String role,
            String deleted
    );
    public void update(User user);
    public void delete(User user);
    public User findLoggedUser();
    public User findByUserID(Long userID);
    public User findByUsername(String username);
    public User[] findByRole(String role);
    public User[] findAllUsers();
    public User[] findAllUsersExceptMeAndDeleted(User user);

}
