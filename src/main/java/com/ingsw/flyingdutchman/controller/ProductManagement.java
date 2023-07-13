package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.CategoryDAO;
import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.ProductDAO;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProductManagement {
    private ProductManagement(){}

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

            // Prendo tutte le aste con prodotti dell'utente e le riempio
            Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(loggedUser);
            for(int i = 0; i < auctions.length ; i++){
                Product product = daoFactory.getProductDAO().findByProductID(auctions[i].getProduct_auctioned().getProductID());
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

            // Trovo tutti i prodotti dell'utente
            Product[] products = daoFactory.getProductDAO().findByOwner(loggedUser);

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
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("products", results.toArray(new Product[results.size()]));
            request.setAttribute("viewUrl","productManagement/view");
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

            Category[] categories = daoFactory.getCategoryDAO().getAllCategories();
            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("categories",categories);
            request.setAttribute("viewUrl","productManagement/insertView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Product Controller Error / insertView --" + e);
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

            User user = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            ProductDAO productDAO = daoFactory.getProductDAO();
            CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
            try {
                productDAO.create(
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("min_price")),
                        Integer.parseInt(request.getParameter("starting_price")),
                        Integer.parseInt(request.getParameter("current_price")),
                        null, // TODO: se ho tempo implementare lettura immagini
                        categoryDAO.findByCategoryID(Long.parseLong(request.getParameter("categoryID"))),
                        user
                );
            }catch (Exception e){
                applicationMessage = "Errore nella creazione del prodotto.";
                logger.log(Level.SEVERE, "Errore nella creazione del prodotto: " + e);
            }

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            // Prendo tutte le aste con prodotti dell'utente e le riempio
            Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(loggedUser);
            for(int i = 0; i < auctions.length ; i++){
                Product product = daoFactory.getProductDAO().findByProductID(auctions[i].getProduct_auctioned().getProductID());
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

            // Trovo tutti i prodotti dell'utente
            Product[] products = daoFactory.getProductDAO().findByOwner(loggedUser);

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
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("products", results.toArray(new Product[results.size()]));
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","productManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Product Controller Error / insert" + e);
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

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            ProductDAO productDAO = daoFactory.getProductDAO();
            Product product1 = productDAO.findByProductID(Long.parseLong(request.getParameter("productID")));
            productDAO.delete(product1);

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            // Prendo tutte le aste con prodotti dell'utente e le riempio
            Auction[] auctions = daoFactory.getAuctionDAO().findByOwner(loggedUser);
            for(int i = 0; i < auctions.length ; i++){
                Product product = daoFactory.getProductDAO().findByProductID(auctions[i].getProduct_auctioned().getProductID());
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

            // Trovo tutti i prodotti dell'utente
            Product[] products = daoFactory.getProductDAO().findByOwner(loggedUser);

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
            request.setAttribute("products", results.toArray(new Product[results.size()]));
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","productManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "User Controller Error / delete", e);
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
