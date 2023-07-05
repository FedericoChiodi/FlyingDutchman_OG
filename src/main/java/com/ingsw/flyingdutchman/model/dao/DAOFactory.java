package com.ingsw.flyingdutchman.model.dao;

import com.ingsw.flyingdutchman.model.dao.CookieImpl.CookieDAOFactory;
import com.ingsw.flyingdutchman.model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;

import java.util.Map;

public abstract class DAOFactory {
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL = "CookieImpl";

    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract UserDAO getUserDAO();
    // all other daos??

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters){
        if(whichFactory.equals(MYSQLJDBCIMPL)){
            return new MySQLJDBCDAOFactory(factoryParameters);
        }
        else if(whichFactory.equals(COOKIEIMPL)){
            return new CookieDAOFactory(factoryParameters);
        }
        else{
            return null;
        }
    }
}
