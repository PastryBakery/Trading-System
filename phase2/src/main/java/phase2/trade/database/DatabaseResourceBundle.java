package phase2.trade.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.Shutdownable;
import phase2.trade.config.DatabaseConfig;
import phase2.trade.gateway.EntityGatewayBundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseResourceBundle implements Shutdownable {

    private static final Logger logger = LogManager.getLogger(DatabaseResourceBundle.class);

    private final ExecutorService threadPool;

    private final SessionFactory sessionFactory;

    private final EntityGatewayBundle entityGatewayBundle;

    public DatabaseResourceBundle(DatabaseConfig databaseConfig) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

        logger.info("Database: " + databaseConfig.getDatabaseType());
        logger.info("Dialect: " + databaseConfig.getConfiguredDialect());
        logger.info("Driver: " + databaseConfig.getConfiguredDriver());
        logger.info("Url: " + databaseConfig.getConfiguredURL());
        logger.info("UserName: " + databaseConfig.getUsername());
        logger.info("hbm2ddl: " + databaseConfig.getHbm2ddl());

        configuration.setProperty("hibernate.dialect", databaseConfig.getConfiguredDialect());
        configuration.setProperty("hibernate.connection.driver_class", databaseConfig.getConfiguredDriver());
        configuration.setProperty("hibernate.connection.url", databaseConfig.getConfiguredURL());
        configuration.setProperty("hibernate.connection.username", databaseConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", databaseConfig.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", databaseConfig.getHbm2ddl());
        configuration.setProperty("hibernate.connection.autoReconnect", String.valueOf(databaseConfig.isAutoReconnect()));
        configuration.setProperty("hibernate.show_sql", String.valueOf(databaseConfig.isShowSQL()));
        configuration.setProperty("hibernate.connection.pool_size", String.valueOf(databaseConfig.getPoolSize()));

        logger.info("Connecting to Database...");
        sessionFactory = configuration.configure().buildSessionFactory();
        threadPool = Executors.newFixedThreadPool(databaseConfig.getPoolSize());

        entityGatewayBundle = new EntityGatewayBundle(this);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void stop() {
        sessionFactory.close();
        threadPool.shutdown();
    }

    public EntityGatewayBundle getEntityGatewayBundle() {
        return entityGatewayBundle;
    }
}
