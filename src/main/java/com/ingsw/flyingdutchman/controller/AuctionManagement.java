package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.AuctionDAO;
import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuctionManagement {
    private AuctionManagement(){}

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

            Auction[] auctions = daoFactory.getAuctionDAO().findAllAuctions();

            ProductDAO productDAO = daoFactory.getProductDAO();

            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product product = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                auctions[i].setProduct_auctioned(product);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("auctions", auctions);
            request.setAttribute("viewUrl","auctionManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / view -- " + e);
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
    public static void insertView(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

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
            Product[] products = daoFactory.getProductDAO().findByOwner(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("products",products);
            request.setAttribute("viewUrl","auctionManagement/insertView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / insertView --" + e);
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
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

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            AuctionDAO auctionDAO = daoFactory.getAuctionDAO();
            ProductDAO productDAO = daoFactory.getProductDAO();
            try {
                auctionDAO.create(
                        Timestamp.valueOf(request.getParameter("opening_timestamp")),
                        productDAO.findByProductID(Long.parseLong(request.getParameter("productID")))
                );
                applicationMessage = "Asta creata correttamente!";
            }catch (Exception e){
                applicationMessage = "Errore nella creazione dell'asta!";
                logger.log(Level.SEVERE, "Errore nella creazione dell'asta: " + e);
            }

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());
            Auction[] auctions = daoFactory.getAuctionDAO().findAllAuctions();

            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product product = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                auctions[i].setProduct_auctioned(product);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auctions",auctions);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","auctionManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / insert", e);
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
    public static void inspectAuction(HttpServletRequest request, HttpServletResponse response){
        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;

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

            //Trovo l'asta da vedere
            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));
            //Associo il prodotto
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            //Setto il prodotto
            auction.setProduct_auctioned(product);



            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
            request.setAttribute("viewUrl","auctionManagement/inspectAuction");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / inspectAuction --" + e);
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            }
            catch (Throwable t){}
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            }
            catch (Throwable t){}
        }
    }

}
