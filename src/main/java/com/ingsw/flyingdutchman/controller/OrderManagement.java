package com.ingsw.flyingdutchman.controller;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;
import com.ingsw.flyingdutchman.model.dao.OrderDAO;
import com.ingsw.flyingdutchman.model.dao.UserDAO;
import com.ingsw.flyingdutchman.model.mo.Auction;
import com.ingsw.flyingdutchman.model.mo.Order;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.services.config.Configuration;
import com.ingsw.flyingdutchman.services.logservice.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderManagement {
    private OrderManagement(){}

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

            //Creo un nuovo formato data nel formato che vuole mysql
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            //Setto il timestamp di chiusura
            auction.setClosing_timestamp(Timestamp.valueOf(sdf.format(timestamp)));

            //Setto prodotto venduto = true
            auction.setProduct_sold(true);

            //Update dei dati dell'asta anche  sul db
            try {
                daoFactory.getAuctionDAO().update(auction);
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Update dell'asta fallito - Ordine." + e);
                throw new RuntimeException(e);
            }

            //Creo un nuovo ordine e inserisco i dati
            Order order = new Order();
            order.setOrder_time(Timestamp.valueOf(sdf.format(timestamp)));
            order.setSelling_price(auction.getProduct_auctioned().getCurrent_price());
            order.setBuyer(loggedUser);
            order.setProduct(auction.getProduct_auctioned());

            //Inserisco l'ordine sul db
            try {
                daoFactory.getOrderDAO().create(
                        Timestamp.valueOf(sdf.format(timestamp)),
                        auction.getProduct_auctioned().getCurrent_price(), //TODO potrebbe essere null? qua il fatto
                        loggedUser,
                        auction.getProduct_auctioned()
                );
            }
            catch (Exception e){
                logger.log(Level.SEVERE, "Creazione ordine fallita - Ordine." + e);
                throw new RuntimeException(e);
            }

            applicationMessage = "Pagamento confermato! Il tuo ordine è stato inserito";

            Order[] orders = daoFactory.getOrderDAO().findByUser(loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser",loggedUser);
            request.setAttribute("orders",orders);
            request.setAttribute("applicationMessage",applicationMessage);
            request.setAttribute("viewUrl","orderManagement/view");
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
}
