package com.ingsw.flyingdutchman.model.mo;

import java.sql.Date;

public class User {
    private Long userID;
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private Date birthdate;
    private String address;
    private Short civic_number;
    private Short cap;
    private String city;
    private String state;
    private String email;
    private String cel_number;
    private String role; // [Default - Premium - Admin - SuperAdmin]
    private boolean deleted;


    public Long getUserID(){return userID;}
    public void setUserID(Long userID){this.userID = userID;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}
    public String getFirstname(){return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public String getSurname(){return surname;}
    public void setSurname(String surname){this.surname = surname;}
    public Date getBirthdate(){return birthdate;}
    public void setBirthdate(Date birthdate){this.birthdate = birthdate;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}
    public Short getCivic_number(){return civic_number;}
    public void setCivic_number(short civic_number){this.civic_number = civic_number;}
    public Short getCap(){return cap;}
    public void setCap(short cap){this.cap = cap;}
    public String getCity(){return city;}
    public void setCity(String city){this.city = city;}
    public String getState(){return state;}
    public void setState(String state){this.state = state;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getCel_number(){return cel_number;}
    public void setCel_number(String celNumber){this.cel_number = celNumber;}
    public String getRole(){return role;}
    public void setRole(String role){this.role = role;}
    public boolean isDeleted(){return deleted;}
    public void setDeleted(boolean deleted){this.deleted = deleted;}
}
