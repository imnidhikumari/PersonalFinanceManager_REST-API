package org.finance2.health;

import org.finance2.core.Category;
import org.finance2.core.TransactionTable;
import org.finance2.core.User;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class HibernateConfig {

    private final String jdbcUrl;
    private final String driverClass;
    private final String user;
    private final String password;

    public HibernateConfig(String jdbcUrl, String driverClass, String user, String password){
        this.jdbcUrl=jdbcUrl;
        this.driverClass=driverClass;
        this.user=user;
        this.password=password;
    }

    public SessionFactory buildSessionFactory(){
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class",driverClass);
        configuration.setProperty("hibernate.connection.url",jdbcUrl);
        configuration.setProperty("hibernate.connection.user",user);
        configuration.setProperty("hibernate.connection.password",password);

        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(TransactionTable.class);
        configuration.addAnnotatedClass(User.class);

        return configuration.buildSessionFactory();
    }

    public SessionFactory getSessionFactory(){ return buildSessionFactory(); }

}