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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

            Auction[] auctions = daoFactory.getAuctionDAO().findAllOpenAuctionsExceptUser(loggedUser);

            ProductDAO productDAO = daoFactory.getProductDAO();

            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product product = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
                product.setOwner(owner);
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

    public static void viewMyAuctions(HttpServletRequest request, HttpServletResponse response){
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

            Auction[] auctions = daoFactory.getAuctionDAO().findOpenAuctionsByOwnerNotDeleted(loggedUser);

            ProductDAO productDAO = daoFactory.getProductDAO();

            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product product = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
                product.setOwner(owner);
                auctions[i].setProduct_auctioned(product);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("auctions", auctions);
            request.setAttribute("canEdit",true);
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

            // Prendo tutte le aste con prodotti dell'utente e le riempio
            Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(loggedUser);
            for(int i = 0; i < auctions.length ; i++){
                Product product = daoFactory.getProductDAO().findByProductID(auctions[i].getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
                product.setOwner(owner);
                auctions[i].setProduct_auctioned(product);
            }

            // Creo una nuova lista di prodotti
            List<Product> productsList = new ArrayList<>();

            // Per ogni asta controllo se il prodotto è venduto o meno e lo aggiungo alla lista se lo è
            for (int i = 0; i < auctions.length ; i++){
                if(auctions[i].isProduct_sold()){
                    productsList.add(auctions[i].getProduct_auctioned());
                }
            }

            // Per ogni asta controllo se il prodotto è attualmente all'asta
            Auction[] auctions1 = daoFactory.getAuctionDAO().findOpenAuctionsByOwnerNotDeleted(loggedUser);
            for (Auction auction : auctions1){
                productsList.add(auction.getProduct_auctioned());
            }

            // Trovo tutti i prodotti dell'utente
            Product[] products = daoFactory.getProductDAO().findByOwnerNotDeleted(loggedUser);

            // Rimuovo dai prodotti totali quelli in comune con i prodotti venduti all'asta
            boolean contains = false;
            List<Product> results = new ArrayList<>();
            // O(n^2)...sigh...
            Product[] products1 = productsList.toArray(new Product[productsList.size()]);
            for(int i=0; i<products.length; i++) {
                for(int j=0; j<products1.length; j++) {
                    if(products[i].getProductID().equals(products1[j].getProductID())) {
                        contains = true;
                        break;
                    }
                }
                if(!contains) {
                    results.add(products[i]);
                }
                else{
                    contains = false;
                }
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("products",results.toArray(new Product[results.size()]));
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

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Auction[] auctions1 = auctionDAO.findByOwner(loggedUser);

            boolean alreadyAuctioned = false;
            Product product = productDAO.findByProductID(Long.parseLong(request.getParameter("productID")));
            for (int i = 0; i < auctions1.length; i++){
                if(auctions1[i].getProduct_auctioned().getProductID().equals(product.getProductID())){
                    alreadyAuctioned = true;
                    applicationMessage = "Questo prodotto e' gia' all'asta! Prima di iniziarne una nuova concludi quella";
                }
            }

            if(!alreadyAuctioned){
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
            }

            Auction[] auctions = daoFactory.getAuctionDAO().findAllOpenAuctionsExceptUser(loggedUser);

            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product productToFill = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(productToFill.getOwner().getUserID());
                productToFill.setOwner(owner);
                auctions[i].setProduct_auctioned(productToFill);
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

            //Trovo l'utente associato al prodotto in asta
            User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
            //Setto l'owner con tutti i suoi campi nel prodotto
            auction.getProduct_auctioned().setOwner(owner);

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

    public static void buyProductAuctioned(HttpServletRequest request, HttpServletResponse response){
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
            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));
            //Associo il prodotto all'asta
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            //Setto il prodotto con tutti i suoi campi
            auction.setProduct_auctioned(product);

            //Trovo l'utente associato al prodotto in asta
            User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());
            //Setto l'owner con tutti i suoi campi nel prodotto
            auction.getProduct_auctioned().setOwner(owner);

            //Passo tutto al controller degli ordini per piazzare un nuovo ordine.
            //Non chiudo ancora niente perchè può succedere che l'ordine venga annullato.

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
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

    public static void updateView(HttpServletRequest request, HttpServletResponse response){
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

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            User owner = daoFactory.getUserDAO().findByUserID(product.getOwner().getUserID());

            product.setOwner(owner);
            auction.setProduct_auctioned(product);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","auctionManagement/modifyPriceView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / modifyPriceView", e);
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

    public static void update(HttpServletRequest request, HttpServletResponse response){
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

            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));

            // Trovo il prodotto nell'asta e aggiorno il prezzo
            Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
            product.setCurrent_price(Float.parseFloat(request.getParameter("price")));

            auction.setProduct_auctioned(product);

            try {
                daoFactory.getProductDAO().update(product);
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Errore nell'update dell prezzo dell'asta! -- " + e);
                throw new RuntimeException(e);
            }

            Auction[] auctions = daoFactory.getAuctionDAO().findOpenAuctionsByOwnerNotDeleted(loggedUser);
            ProductDAO productDAO = daoFactory.getProductDAO();
            // Riempimento dei campi delle varie auctions
            for(int i = 0; i < auctions.length; i++){
                Product product1 = productDAO.findByProductID(auctions[i].getProduct_auctioned().getProductID());
                User owner = daoFactory.getUserDAO().findByUserID(product1.getOwner().getUserID());
                product1.setOwner(owner);
                auctions[i].setProduct_auctioned(product1);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("canEdit",true);
            request.setAttribute("auctions",auctions);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","auctionManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / update", e);
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
    public static void lowerAllView(HttpServletRequest request, HttpServletResponse response){
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

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","auctionManagement/lowerAllView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Auction Controller Error / lowerAllView", e);
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

}
