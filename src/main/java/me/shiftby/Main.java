package me.shiftby;

import me.shiftby.command.Interpreter;
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

    private static Properties properties;
    private static Logger logger;
    private static SessionFactory sessionFactory;
    private static SocketListener socketListener;
    private static SessionManager sessionManager;

    public static void main(String[] args) throws Exception {
        start();
    }

    private static void start()
            throws IOException, ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        properties = readProperties("server.conf.xml");
        Properties commandsProperties = readProperties("command.conf.xml");

        logger = createLogger(properties);
        sessionFactory = createSessionFactory(properties);
        Interpreter interpreter = createInterpreter(commandsProperties);
        sessionManager = createSessionManager(interpreter);

        GroupManager.getInstance();
        MessageManager.getInstance();
        UserManager.getInstance();

        socketListener = createSocketListener(properties);
    }

    public static void stop() throws Exception {
        socketListener.close();
        GroupManager.getInstance().close();
        MessageManager.getInstance().close();
        UserManager.getInstance().close();
        sessionFactory.close();
        sessionManager.close();
    }

    public static void restart() throws Exception {
        stop();
        Thread.sleep(1000);
        start();
    }

    private static Properties readProperties(String path) throws IOException {
        Properties properties = new Properties();
        InputStream stream = new FileInputStream(path);
        properties.loadFromXML(stream);
        return properties;
    }

    private static Interpreter createInterpreter(Properties p) {
        return new Interpreter(p);
    }
    private static SessionManager createSessionManager(Interpreter interpreter) {
        return new SessionManager(interpreter);
    }
    private static SocketListener createSocketListener(Properties p) throws IOException {
        int serverPort = Integer.parseInt(properties.getProperty("server.port", "6700"));
        return new SocketListener(serverPort);
    }
    private static SessionFactory createSessionFactory(Properties p) {
        Configuration cfg = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                .setProperty("hibernate.connection.url", p.getProperty("mysql.url", "jdbc:mysql://localhost:3306/chat?serverTimezone=UTC"))
                .setProperty("hibernate.connection.username", p.getProperty("mysql.username", "root"))
                .setProperty("hibernate.connection.password", p.getProperty("mysql.password", ""))
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
    private static Logger createLogger(Properties p)
            throws ClassNotFoundException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class loggerClass = Class.forName(properties.getProperty("logger", "me.shiftby.logger.ConsoleLogger"));
        int loggerLevel = Integer.parseInt(properties.getProperty("logger.level", "3"));

        return (Logger)loggerClass.getDeclaredConstructors()[0].newInstance(loggerLevel);
    }


    public static SessionManager getSessionManager() {
        return sessionManager;
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static Logger getLogger() {
        return logger;
    }
}
