package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.*;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThresholdManagement {
    private static final int max = 20;
    private static final int min = 5;
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
            for (int i = 0; i < thresholds.length; i++){
                Auction auction = daoFactory.getAuctionDAO().findAuctionByID(thresholds[i].getAuction().getAuctionID());
                Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                auction.setProduct_auctioned(product);
                thresholds[i].setAuction(auction);
            }

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

            loggedUser = daoFactory.getUserDAO().findByUsername(loggedUser.getUsername());

            Threshold threshold = daoFactory.getThresholdDAO().findByID(Long.parseLong(request.getParameter("thresholdID")));

            try {
                daoFactory.getThresholdDAO().delete(threshold);
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Errore eliminazione prenotazione " + e);
                throw new RuntimeException(e);
            }

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);
            for (int i = 0; i < thresholds.length; i++){
                Auction auction1 = daoFactory.getAuctionDAO().findAuctionByID(thresholds[i].getAuction().getAuctionID());
                Product product = daoFactory.getProductDAO().findByProductID(auction1.getProduct_auctioned().getProductID());
                auction1.setProduct_auctioned(product);
                thresholds[i].setAuction(auction1);
            }

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
                        Float.parseFloat(request.getParameter("price")),
                        timestamp,
                        loggedUser,
                        auction
                );
            }
            catch (Exception e){
                applicationMessage = "Errore nella creazione della prenotazione!";
            }

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);
            for (int i = 0; i < thresholds.length; i++){
                Auction auction1 = daoFactory.getAuctionDAO().findAuctionByID(thresholds[i].getAuction().getAuctionID());
                Product product = daoFactory.getProductDAO().findByProductID(auction1.getProduct_auctioned().getProductID());
                auction1.setProduct_auctioned(product);
                thresholds[i].setAuction(auction1);
            }

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
            Product product = daoFactory.getProductDAO().findByAuction(auction);
            auction.setProduct_auctioned(product);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("auction",auction);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/insModView");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/insertView --" + e);
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

            threshold.setPrice(Float.parseFloat(request.getParameter("price")));
            threshold.setReservation_date(timestamp);

            try {
                daoFactory.getThresholdDAO().update(threshold);
            }
            catch (Exception e){
                applicationMessage = "Errore nella modifica della prenotazione!";
                logger.log(Level.SEVERE, "Errore nella modifica della prenotazione! -- " + e);
            }

            Threshold[] thresholds = daoFactory.getThresholdDAO().findByUser(loggedUser);
            for (int i = 0; i < thresholds.length; i++){
                Auction auction1 = daoFactory.getAuctionDAO().findAuctionByID(thresholds[i].getAuction().getAuctionID());
                Product product = daoFactory.getProductDAO().findByProductID(auction1.getProduct_auctioned().getProductID());
                auction1.setProduct_auctioned(product);
                thresholds[i].setAuction(auction1);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("thresholds",thresholds);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","thresholdManagement/view");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/modify --" + e);
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

    public static void modifyView(HttpServletRequest request, HttpServletResponse response){
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

            Threshold threshold = daoFactory.getThresholdDAO().findByID(Long.parseLong(request.getParameter("thresholdID")));
            Auction auction = daoFactory.getAuctionDAO().findAuctionByID(threshold.getAuction().getAuctionID());
            Product product = daoFactory.getProductDAO().findByAuction(auction);

            auction.setProduct_auctioned(product);
            threshold.setAuction(auction);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("threshold",threshold);
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

    public static void checkOnUpdate(HttpServletRequest request, HttpServletResponse response){
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

            //Preparo la pagina a cui devo ritornare dopo i controlli
            String pageToReturn = request.getParameter("pageToReturn");

            if(pageToReturn.equals("auctionManagement/view")) {
                //Trovo l'asta aggiornata e inserisco il prodotto
                Auction auction = daoFactory.getAuctionDAO().findAuctionByID(Long.parseLong(request.getParameter("auctionID")));
                Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                auction.setProduct_auctioned(product);

                //Prima di tutto aggiorno l'asta sul db se sono chiamato da un aggiornamento di un prezzo di un'asta
                try {
                    auction.getProduct_auctioned().setCurrent_price(Float.parseFloat(request.getParameter("price")));
                    daoFactory.getProductDAO().update(auction.getProduct_auctioned());
                    applicationMessage = "Prezzo abbassato correttamente!";
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Non ho potuto aggiornare il prezzo dell'asta." + e);
                    throw new RuntimeException(e);
                }

                //Trovo tutte le prenotazioni associate alla'asta aggiornata
                Threshold[] thresholds = daoFactory.getThresholdDAO().findThresholdsByAuction(auction);

                //Trovo tutte quelle che hanno prezzo superiore o uguale all'asta e le aggiungo a una lista
                if (thresholds.length > 0) {
                    List<Threshold> validThresholds = new ArrayList<>();
                    for (int i = 0; i < thresholds.length; i++) {
                        if (thresholds[i].getPrice() >= auction.getProduct_auctioned().getCurrent_price()) {
                            validThresholds.add(thresholds[i]);
                        }
                    }

                    if (validThresholds.size() > 0) {
                        //Tra tutte quelle aggiunte controllo quella con prezzo > e aggiunta prima cronologicamente
                        //e faccio l'ordine. Elimino poi tutte le altre
                        Threshold toOrder = validThresholds.get(0);
                        for (int i = 1; i < validThresholds.size(); i++) {
                            if (validThresholds.get(i).getPrice() > toOrder.getPrice()) {
                                toOrder = validThresholds.get(i);
                            } else if (validThresholds.get(i).getPrice() == toOrder.getPrice()) {
                                if (validThresholds.get(i).getReservation_date().compareTo(toOrder.getReservation_date()) < 0) {
                                    toOrder = validThresholds.get(i);
                                }
                            }
                        }
                        //Elimino dal db tutte le prenotazioni
                        for (int i = 0; i < validThresholds.size(); i++) {
                            try {
                                daoFactory.getThresholdDAO().delete(validThresholds.get(i));
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Could not remove Thresholds -- " + e);
                                throw new RuntimeException(e);
                            }
                        }

                        //Ottenere il timestamp corrente
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

                        //Setto il timestamp di chiusura
                        auction.setClosing_timestamp(timestamp);

                        //Setto prodotto venduto = true
                        auction.setProduct_sold(true);

                        //Aggiorno i dati del prodotto
                        Product productSold = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                        productSold.setCurrent_price(toOrder.getPrice());
                        try {
                            daoFactory.getProductDAO().update(productSold);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Update del prodotto fallito - ThresholdCheck." + e);
                            throw new RuntimeException(e);
                        }

                        //Update dei dati dell'asta anche sul db
                        try {
                            daoFactory.getAuctionDAO().update(auction);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Update dell'asta fallito - ThresholdCheck." + e);
                            throw new RuntimeException(e);
                        }

                        //Inserisco l'ordine sul db
                        User buyer = daoFactory.getUserDAO().findByUserID(toOrder.getUser().getUserID());
                        try {
                            daoFactory.getOrderDAO().create(
                                    timestamp,
                                    toOrder.getPrice(),
                                    true,
                                    buyer,
                                    product
                            );
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Creazione ordine fallita - ThresholdUpdateCheck." + e);
                            throw new RuntimeException(e);
                        }

                        //Elimino infine la prenotazione effettuata
                        try {
                            daoFactory.getThresholdDAO().delete(toOrder);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Non ho potuto eliminare la prenotazione valida." + e);
                            throw new RuntimeException(e);
                        }

                        applicationMessage = buyer.getUsername() + " ha comprato questo prodotto con una Prenotazione!";
                    }
                }
            }

            if(pageToReturn.equals("auctionManagement/lowerAllView")){
                //Trovo tutte le aste aperte non eliminate usando un dummy User
                User nullUser = new User();
                nullUser.setUserID(Long.parseLong("-1"));
                Auction[] auctions = daoFactory.getAuctionDAO().findAllOpenAuctionsExceptUser(nullUser);

                //Riempio i prodotti di tutte le aste
                for (Auction auction : auctions){
                    Product product = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                    auction.setProduct_auctioned(product);
                }

                //Aggiorno i dati di ogni asta e li scrivo sul db
                for(Auction auction : auctions){
                    //Genero un numero casuale tra max e min compresi
                    int randomNumber = (int) (Math.random() * (max - min + 1)) + min;
                    //Genero la percentuale di ribasso
                    float percentageToLower = randomNumber / 100f;

                    Product product = daoFactory.getProductDAO().findByAuction(auction);

                    float currentPrice = product.getCurrent_price();
                    float minPrice = product.getMin_price();

                    if(currentPrice - (currentPrice * percentageToLower) > minPrice){
                        product.setCurrent_price(currentPrice - (currentPrice * percentageToLower));
                    }
                    else {
                        product.setCurrent_price(minPrice);
                    }

                    try {
                        daoFactory.getProductDAO().update(product);
                    }
                    catch (Exception e){
                        logger.log(Level.SEVERE, "Non sono riuscito ad aggiornare i prezzi di un prodotto all'asta dopo il lower! - " + e);
                        throw new RuntimeException(e);
                    }

                    //Aggiorno il nuovo prodotto per poter essere subito ordinato
                    auction.setProduct_auctioned(product);
                }

                applicationMessage = "Prezzi correttamente abbassati in " + auctions.length + " aste!";

                //Importante! Aggiorno i cambiamenti
                try {
                    daoFactory.commitTransaction();
                }
                catch (Exception e){
                   try {
                       daoFactory.rollbackTransaction();
                   }
                   catch (Throwable t){}
                    logger.log(Level.SEVERE, "Errore nella commit!");
                    throw new RuntimeException(e);
                }
                finally {
                    daoFactory.closeTransaction();
                }

                daoFactory.beginTransaction();
                //Ora per ogni asta controllo se ci sono prenotazioni
                for(Auction auction : auctions){
                    Threshold[] thresholds = daoFactory.getThresholdDAO().findThresholdsByAuction(auction);

                    //Se ce ne sono inizio a fare i controlli
                    if (thresholds.length > 0) {
                        List<Threshold> validThresholds = new ArrayList<>();
                        for (int i = 0; i < thresholds.length; i++) {
                            if (thresholds[i].getPrice() >= auction.getProduct_auctioned().getCurrent_price()) {
                                validThresholds.add(thresholds[i]);
                            }
                        }

                        if (validThresholds.size() > 0) {
                            //Tra tutte quelle aggiunte controllo quella con prezzo > e aggiunta prima cronologicamente
                            //e faccio l'ordine. Elimino poi tutte le altre
                            Threshold toOrder = validThresholds.get(0);
                            for (int i = 1; i < validThresholds.size(); i++) {
                                if (validThresholds.get(i).getPrice() > toOrder.getPrice()) {
                                    toOrder = validThresholds.get(i);
                                } else if (validThresholds.get(i).getPrice() == toOrder.getPrice()) {
                                    if (validThresholds.get(i).getReservation_date().compareTo(toOrder.getReservation_date()) < 0) {
                                        toOrder = validThresholds.get(i);
                                    }
                                }
                            }
                            //Elimino dal db tutte le prenotazioni
                            for (int i = 0; i < validThresholds.size(); i++) {
                                try {
                                    daoFactory.getThresholdDAO().delete(validThresholds.get(i));
                                } catch (Exception e) {
                                    logger.log(Level.SEVERE, "Could not remove Thresholds -- " + e);
                                    throw new RuntimeException(e);
                                }
                            }

                            //Ottenere il timestamp corrente
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            Timestamp timestamp = Timestamp.valueOf(currentDateTime);

                            //Setto il timestamp di chiusura
                            auction.setClosing_timestamp(timestamp);

                            //Setto prodotto venduto = true
                            auction.setProduct_sold(true);

                            //Aggiorno i dati del prodotto
                            Product productSold = daoFactory.getProductDAO().findByProductID(auction.getProduct_auctioned().getProductID());
                            productSold.setCurrent_price(toOrder.getPrice());
                            try {
                                daoFactory.getProductDAO().update(productSold);
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Update del prodotto fallito - ThresholdCheck." + e);
                                throw new RuntimeException(e);
                            }

                            //Update dei dati dell'asta anche sul db
                            try {
                                daoFactory.getAuctionDAO().update(auction);
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Update dell'asta fallito - ThresholdCheck." + e);
                                throw new RuntimeException(e);
                            }

                            //Inserisco l'ordine sul db
                            User buyer = daoFactory.getUserDAO().findByUserID(toOrder.getUser().getUserID());
                            try {
                                daoFactory.getOrderDAO().create(
                                        timestamp,
                                        toOrder.getPrice(),
                                        true,
                                        buyer,
                                        productSold
                                );
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Creazione ordine fallita - ThresholdUpdateCheck." + e);
                                throw new RuntimeException(e);
                            }

                            //Elimino infine la prenotazione effettuata
                            try {
                                daoFactory.getThresholdDAO().delete(toOrder);
                            } catch (Exception e) {
                                logger.log(Level.SEVERE, "Non ho potuto eliminare la prenotazione valida." + e);
                                throw new RuntimeException(e);
                            }
                            System.out.println(buyer.getUsername() + " ha comprato con una Prenotazione!");
                        }
                    }
                }

            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl",pageToReturn);
            if(pageToReturn.equals("auctionManagement/view")){
                Auction[] auctions = daoFactory.getAuctionDAO().findOpenAuctionsByOwnerNotDeleted(loggedUser);
                for(int i = 0; i < auctions.length; i++){
                    Product product1 = daoFactory.getProductDAO().findByProductID(auctions[i].getProduct_auctioned().getProductID());
                    auctions[i].setProduct_auctioned(product1);
                }
                request.setAttribute("canEdit",true);
                request.setAttribute("auctions",auctions);
            }
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Controller Error - ThresholdManagement/checkUpdate --" + e);
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




