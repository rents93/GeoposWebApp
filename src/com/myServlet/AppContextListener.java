package com.myServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//LkF 03/05 listener per DB
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
//            System.out.println("Errore driverInit()");
        }
        //instanzio il dbmanager e lo registro nel context
        DBManager dbManager = DBManager.getdbManager();
        sce.getServletContext().setAttribute("dbManager", dbManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
