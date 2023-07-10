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

public class HomeManagement {

    private HomeManagement(){};

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
            request.setAttribute("viewUrl","homeManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error", e);
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

    public static void logon(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOfactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();
        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOfactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOfactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOfactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUsername(username);

            try {
                if (user == null || !user.getPassword().equals(password)){
                    sessionUserDAO.delete(null);
                    applicationMessage = "Username o Password errati!";
                    loggedUser = null;
                }
                else{
                    loggedUser = sessionUserDAO.create(user.getUsername(),null, user.getFirstname(),user.getSurname(),null,null,null,null,null,null,null,null,null);
                }
            }
            catch (NullPointerException e){
                sessionUserDAO.delete(null);
                applicationMessage = "Username o Password errati!";
                loggedUser = null;
            }

            daoFactory.commitTransaction();
            sessionDAOfactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","homeManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOfactory != null) sessionDAOfactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
             try {
                 if (daoFactory != null) daoFactory.closeTransaction();
                 if(sessionDAOfactory != null) sessionDAOfactory.closeTransaction();
             }
             catch (Throwable t){}
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response){

        DAOFactory sessionDAOfactory = null;

        Logger logger = LogService.getApplicationLogger();
        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOfactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOfactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOfactory.getUserDAO();
            sessionUserDAO.delete(null);

            sessionDAOfactory.commitTransaction();

            request.setAttribute("loggedOn",false);
            request.setAttribute("loggedUser",null);
            request.setAttribute("viewUrl","homeManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if(sessionDAOfactory != null) sessionDAOfactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(sessionDAOfactory != null) sessionDAOfactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
}
