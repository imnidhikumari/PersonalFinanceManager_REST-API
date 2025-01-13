package org.finance2;

import org.finance2.dao.CategoryDAO;
import org.finance2.dao.TransactionDAO;
import org.finance2.dao.UserDAO;
import org.finance2.health.DatabaseHealthCheck;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.finance2.resources.CategoryResource;
import org.finance2.resources.TransactionResource;
import org.finance2.resources.UserResource;
import org.finance2.security.JwtAuthFilter;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import javax.inject.Singleton;
import com.codahale.metrics.MetricRegistry;


public class PersonalFinanceManagerApplication extends Application<PersonalFinanceManagerConfiguration> {

    private static final Logger LOGGER= LoggerFactory.getLogger(PersonalFinanceManagerApplication.class);

    public static void main(String[] args) throws Exception {
        new PersonalFinanceManagerApplication().run(args);
    }

    @Override
    public String getName() {
        return "PersonalFinanceManager";
    }


    @Override
    public void initialize(Bootstrap<PersonalFinanceManagerConfiguration> bootstrap)
    {
        //logic if required
    }

    @Override
    public void run(PersonalFinanceManagerConfiguration configuration, Environment environment)
    {
        System.out.println("Hello, Dropwizard!");

        final SessionFactory sessionFactory = configuration.getHibernate().buildSessionFactory();
        final MetricRegistry metricRegistry = environment.metrics();

        //DAOs
        final UserDAO userDAO = new UserDAO(sessionFactory);
        final CategoryDAO categoryDAO = new CategoryDAO(sessionFactory);
        final TransactionDAO transactionDAO = new TransactionDAO(sessionFactory);

        //Resources
        final UserResource userResource = new UserResource(userDAO, metricRegistry);
        final CategoryResource categoryResource = new CategoryResource(categoryDAO, metricRegistry);
        final TransactionResource transactionResource = new TransactionResource(transactionDAO, metricRegistry);

        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(sessionFactory).to(SessionFactory.class);
                bind(userDAO).to(UserDAO.class).in(Singleton.class);
                bind(categoryDAO).to(CategoryDAO.class).in(Singleton.class);
                bind(transactionDAO).to(TransactionDAO.class).in(Singleton.class);
                bind(userResource).to(UserResource.class).in(Singleton.class);
                bind(categoryResource).to(CategoryResource.class).in(Singleton.class);
                bind(transactionResource).to(TransactionResource.class).in(Singleton.class);
            }
        });

        //Registering Resources
        environment.jersey().register(userResource);
        environment.jersey().register(categoryResource);
        environment.jersey().register(transactionResource);
        environment.jersey().register(JwtAuthFilter.class);
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        //Health Checks
        environment.healthChecks().register("database", new DatabaseHealthCheck(sessionFactory));

        LOGGER.info("Personal Finance Management Application started successfully");

    }



}