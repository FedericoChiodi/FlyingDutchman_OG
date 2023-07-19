package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.Product;
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

public class OrderManagement {
    private OrderManagement(){}

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

            Order[] orders = daoFactory.getOrderDAO().findByUser(loggedUser);
            for(int i = 0; i < orders.length; i++){
                Product product = daoFactory.getProductDAO().findByProductID(orders[i].getProduct().getProductID());
                User productOwner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
                product.setOwner(productOwner);
                User user = daoFactory.getUserDAO().findByUserID(orders[i].getBuyer().getUserID());
                orders[i].setProduct(product);
                orders[i].setBuyer(user);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("orders",orders);
            request.setAttribute("viewUrl","orderManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Product Controller Error / view -- " + e);
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
    public static void pay(HttpServletRequest request, HttpServletResponse response){
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

            //Trovo l'asta che ho chiuso
            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));

            //Riempio i dati del prodotto
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            auction.setProduct_auctioned(product);

            //Ottenere il timestamp corrente
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            //Setto il timestamp di chiusura
            auction.setClosing_timestamp(timestamp);

            //Setto prodotto venduto = true
            auction.setProduct_sold(true);

            //Update dei dati dell'asta anche sul db
            try {
                daoFactory.getAuctionDAO().update(auction);
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Update dell'asta fallito - Ordine." + e);
                throw new RuntimeException(e);
            }

            //Inserisco l'ordine sul db
            try {
                daoFactory.getOrderDAO().create(
                        timestamp,
                        product.getCurrent_price(),
                        false,
                        loggedUser,
                        product
                );
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Creazione ordine fallita - Ordine." + e);
                throw new RuntimeException(e);
            }

            applicationMessage = "Pagamento confermato! Il tuo ordine è stato inserito";

            Order[] orders = daoFactory.getOrderDAO().findByUser(loggedUser);
            for(int i = 0; i < orders.length; i++){
                Product product1 = daoFactory.getProductDAO().findByProductID(orders[i].getProduct().getProductID());
                User productOwner = daoFactory.getUserDAO().findByUserID(product1.getOwner().getUserID());
                product1.setOwner(productOwner);
                User user = daoFactory.getUserDAO().findByUserID(orders[i].getBuyer().getUserID());
                orders[i].setProduct(product1);
                orders[i].setBuyer(user);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("orders",orders);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","orderManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Order Controller Error / pay -- " + e);
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
    public static void buyPremium(HttpServletRequest request, HttpServletResponse response){
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

            //Modifico il ruolo sul cookie per far vedere subito i changes
            sessionUserDAO.delete(null);
            sessionUserDAO.create(loggedUser.getUsername(), null, loggedUser.getFirstname(), loggedUser.getSurname(), null, null, null, null, null, null,null,null,"Premium",null);

            //Creazione prodotto premium membership - ID SEMPRE 1
            Product premium = daoFactory.getProductDAO().findByProductID(Long.parseLong("1"));

            //Ottenere il timestamp corrente
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

            //Inserire l'ordine sul db
            try {
                daoFactory.getOrderDAO().create(
                        timestamp,
                        premium.getCurrent_price(),
                        false,
                        loggedUser,
                        premium
                );
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Creazione ordine fallita - Premium Ordine." + e);
                throw new RuntimeException(e);
            }

            //Settare il nuovo role dell'utente loggato e aggiornare il db
            loggedUser.setRole("Premium");
            daoFactory.getUserDAO().update(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","orderManagement/premium");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Order Controller Error / buyPremium -- " + e);
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
    public static void buyPremiumView(HttpServletRequest request, HttpServletResponse response){
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

            //Trovo l'asta che voglio comprare
            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong("1")); //Fisso
            //Associo il prodotto all'asta
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            //Setto il prodotto con tutti i suoi campi
            auction.setProduct_auctioned(product);

            //Trovo l'utente associato al prodotto in asta
            User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
            //Setto l'owner con tutti i suoi campi nel prodotto
            auction.getProduct_auctioned().setOwner(owner);

            //Passo tutto al controller degli ordini per piazzare un nuovo ordine.
            //Non chiudo ancora niente perché può succedere che l'ordine venga annullato.

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
            request.setAttribute("isPremium", true);
            request.setAttribute("viewUrl","orderManagement/insertView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / buyProductAuctioned --" + e);
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
