package me.shiftby.orm;

import me.shiftby.Main;
import me.shiftby.UserAuth;
import me.shiftby.entity.User;
import org.hibernate.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.*;

public class UserManager {

    private SessionFactory sessionFactory;

    private static volatile UserManager instance;
    private UserManager() throws IOException {
        sessionFactory = Main.getSessionFactory();
    }
    public static UserManager getInstance() throws IOException {
        UserManager localInstance = instance;
        if (localInstance == null) {
            synchronized (UserAuth.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = instance = new UserManager();
                }
            }
        }
        return localInstance;
    }

    public User findByUsername(String username) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        query.where(builder.equal(query.from(User.class).get("username"), username));
        User user = session.createQuery(query).uniqueResult();
        transaction.commit();
        session.close();
        return user;
    }

    public boolean exist(String username) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(User.class)));
        query.where(builder.equal(query.from(User.class).get("username"), username));
        long count = session.createQuery(query).uniqueResult();
        transaction.commit();
        session.close();
        return count == 1;
    }
    public void registerUsers(User ... users) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        for (User user : users) {
            user.setUsername(user.getUsername().toLowerCase());
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(15)));
            session.save(user);
        }
        transaction.commit();
        session.close();
    }
    public void changeUser(User user) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    public void close() {

    }
}
