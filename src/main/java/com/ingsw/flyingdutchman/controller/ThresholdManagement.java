package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.Threshold;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThresholdManagement {
    private ThresholdManagement(){}

    public static void view(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("thresholds",thresholds);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Threshold Controller Error / view", e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }

    public static void delete(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            // prendo il dao dei cookies e trovo il loggedUser
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            // prendo il dao del db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            Long thresholdID = Long.parseLong(request.getParameter("thresholdID"));

            Threshold threshold = daoFactory.getThresholdDAO().findByID(thresholdID);

            try {
                daoFactory.getThresholdDAO().delete(threshold);
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Errore eliminazione prenotazione " + e);
                throw new RuntimeException(e);
            }

            applicationMessage = "Prenotazione eliminata correttamente";

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("thresholds",thresholds);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/delete --" + e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }

    public static void insert(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            // prendo il dao dei cookies e trovo il loggedUser
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            // prendo il dao del db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            //Timestamp corrente
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));

            try {
                daoFactory.getThresholdDAO().create(
                        Integer.parseInt(request.getParameter("price")),
                        timestamp,
                        loggedUser,
                        auction
                );
            }
            catch (Exception e){
                applicationMessage = "Errore nella creazione della prenotazione!";
            }

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("thresholds",thresholds);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/insert --" + e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }

    public static void insertView(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            // prendo il dao dei cookies e trovo il loggedUser
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            // prendo il dao del db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/insModView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/insert --" + e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }

    public static void modify(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();
            // prendo il dao dei cookies e trovo il loggedUser
            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();
            // prendo il dao del db
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            //Timestamp corrente
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Threshold threshold = daoFactory.getThresholdDAO().findByID(Long.parseLong(request.getParameter("thresholdID")));

            threshold.setPrice(Integer.parseInt(request.getParameter("price")));
            threshold.setReservation_date(timestamp);

            try {
                daoFactory.getThresholdDAO().update(threshold);
            }
            catch (Exception e){
                applicationMessage = "Errore nella creazione della prenotazione!";
            }

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("thresholds",thresholds);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/insert --" + e);
            try {
                if(daoFactory != null) daoFactory.rollbackTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }
}



