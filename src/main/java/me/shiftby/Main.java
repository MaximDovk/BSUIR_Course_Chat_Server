package me.shiftby;

import me.shiftby.entity.Group;
import me.shiftby.entity.Message;
import me.shiftby.entity.User;
import me.shiftby.logger.Logger;
import me.shiftby.orm.GroupManager;
import me.shiftby.orm.MessageManager;
import me.shiftby.orm.UserManager;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class Main {

    private static Properties p;
    private static Logger logger;
    private static SessionFactory sessionFactory;
    private static SocketListener socketListener;

    private static String mysqlUsername;
    private static String mysqlPassword;
    private static String mysqlUrl;

    private static Class loggerClass;
    private static int loggerLevel;

    private static int serverPort;

    public static void main(String[] args) throws Exception {
        p = readProperties("server.conf.xml");

        logger = createLogger(loggerClass, loggerLevel);
        sessionFactory = createSessionFactory(p);

        GroupManager.getInstance();
        MessageManager.getInstance();
        UserManager.getInstance();

        socketListener = new SocketListener(serverPort);
    }
    public static void stop() throws Exception {
        socketListener.close();
        GroupManager.getInstance().close();
        MessageManager.getInstance().close();
        UserManager.getInstance().close();
        sessionFactory.close();
        SessionManager.getInstance().close();
    }

    private static Properties readProperties(String path) throws IOException, ClassNotFoundException {
        Properties properties = new Properties();
        InputStream stream = new FileInputStream(path);
        properties.loadFromXML(stream);
        mysqlUsername = properties.getProperty("mysql.username", "root");
        mysqlPassword = properties.getProperty("mysql.password", "");
        mysqlUrl = properties.getProperty("mysql.url", "jdbc:mysql://localhost:3306/chat?serverTimezone=UTC");

        loggerClass = Class.forName(properties.getProperty("logger", "me.shiftby.logger.ConsoleLogger"));
        loggerLevel = Integer.parseInt(properties.getProperty("logger.level", "3"));

        serverPort = Integer.parseInt(properties.getProperty("server.port", "6700"));

        return properties;
    }
    public static String get(String key, String def) {
        return p.getProperty(key, def);
    }
    public static String get(String key) {
        return p.getProperty(key);
    }

    private static Logger createLogger(Class loggerClass, int loggerLevel)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Logger)loggerClass.getDeclaredConstructors()[0].newInstance(loggerLevel);
    }
    private static SessionFactory createSessionFactory(Properties p) {
        Configuration cfg = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url", mysqlUrl)
                .setProperty("hibernate.connection.username", mysqlUsername)
                .setProperty("hibernate.connection.password", mysqlPassword)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "validate")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Group.class)
                .addAnnotatedClass(Message.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return cfg.buildSessionFactory(serviceRegistry);
    }

    public static Logger getLogger() {
        return logger;
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
