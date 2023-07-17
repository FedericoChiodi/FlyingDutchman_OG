package com.ingsw.flyingdutchman.model.dao.CookieImpl;

import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;

public class UserDAOCookieImpl implements UserDAO {

    HttpServletRequest request;
    HttpServletResponse response;

    public UserDAOCookieImpl(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    @Override
    public User create(String username, String password, String firstname, String surname, Date birthdate, String address, Short civic_number, Short cap, String city, String state, String email, String cel_number, String role, String deleted) {
        User loggedUser = new User();

        loggedUser.setUsername(username);
        loggedUser.setFirstname(firstname);
        loggedUser.setSurname(surname);
        loggedUser.setRole(role);

        Cookie cookie;
        cookie = new Cookie("loggedUser", encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    @Override
    public void update(User loggedUser) {
        Cookie cookie;
        cookie = new Cookie("loggedUser",encode(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void delete(User user) {
        Cookie cookie;
        cookie = new Cookie("loggedUser","");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public User findLoggedUser() {
        Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if(cookies != null){
            for(int i = 0; i < cookies.length && loggedUser == null; i++){
                if(cookies[i].getName().equals("loggedUser")){
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;
    }

    @Override
    public User findByUsername(String username) {
        /*Cookie[] cookies = request.getCookies();
        User loggedUser = null;

        if(cookies != null){
            for(int i = 0; i < cookies.length && loggedUser == null; i++){
                String[] values = cookies[i].getValue().split("#");
                if(values[0].equals(username)){
                    loggedUser = decode(cookies[i].getValue());
                }
            }
        }

        return loggedUser;*/
        throw new UnsupportedOperationException("Not supported. DEBUG");
    }

    private String encode(User loggedUser){
        String encodedUser;
        encodedUser = loggedUser.getUsername() + "#" + loggedUser.getFirstname() + "#" + loggedUser.getSurname() + "#" + loggedUser.getRole();
        return encodedUser;
    }

    private User decode(String encodedUser){
        User loggedUser = new User();

        String[] values = encodedUser.split("#");

        loggedUser.setUsername(values[0]);
        loggedUser.setFirstname(values[1]);
        loggedUser.setSurname(values[2]);
        loggedUser.setRole(values[3]);

        return loggedUser;
    }
    @Override
    public User[] findByRole(String role) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public User[] findAllUsers() { throw new UnsupportedOperationException("Not supported."); }

    @Override
    public User[] findAllUsersExceptMeAndDeleted(User user) { throw new UnsupportedOperationException("Not supported."); }

    @Override
    public User findByUserID(Long userID) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
