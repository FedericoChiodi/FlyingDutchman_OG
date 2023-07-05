package com.ingsw.flyingdutchman.model.dao.CookieImpl;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class CookieDAOFactory extends DAOFactory {
    private Map factoryParameters;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public CookieDAOFactory(Map factoryParameters){this.factoryParameters = factoryParameters;}

    @Override
    public void beginTransaction(){
        try{
            this.request=(HttpServletRequest) factoryParameters.get("request");
            this.response=(HttpServletResponse) factoryParameters.get("response");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction(){}

    @Override
    public void rollbackTransaction(){}

    @Override
    public void closeTransaction(){}

    @Override
    public UserDAO getUserDAO(){
        return new UserDAOCookieImpl(request,response);
    }

    // ...


}
