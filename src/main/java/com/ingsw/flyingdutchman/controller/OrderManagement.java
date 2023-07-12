package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderManagement {
    private OrderManagement(){}
    public static void view(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOfactory = null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();
        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOfactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOfactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOfactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOfactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("viewUrl","orderManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - OrderManagement/view --" + e);
            try {
                if(sessionDAOfactory != null) sessionDAOfactory.rollbackTransaction();
            }catch (Throwable t){

            }
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(sessionDAOfactory!=null) sessionDAOfactory.closeTransaction();
            }
            catch (Throwable t){

            }
        }
    }
}
