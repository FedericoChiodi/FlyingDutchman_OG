package com.ingsw.flyingdutchman.services.config;

import java.util.Calendar;
import java.util.logging.Level;

import com.ingsw.flyingdutchman.model.dao.DAOFactory;

public class Configuration {
    // Config DB
    public static final String DAO_IMPL = DAOFactory.MYSQLJDBCIMPL;
    public static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SERVER_TIMEZONE = Calendar.getInstance().getTimeZone().getID();
    public static final String DATABASE_URL = "jdbc:mysql://localhost/flyingdutchmandb?user=root&password=StrongPassword123@&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=" + SERVER_TIMEZONE;

    // Session config
    public static final String COOKIE_IMPL = DAOFactory.COOKIEIMPL;

    // Logger config
    public static final String GLOBAL_LOGGER_NAME = "FlyingDutchman";
    public static final String GLOBAL_LOGGER_FILE = "/home/sanpc/Documents/logs/FlyingDutchman_log.%g.%U.txt";
    public static final Level GLOBAL_LOGGER_LEVEL = Level.ALL;
}
