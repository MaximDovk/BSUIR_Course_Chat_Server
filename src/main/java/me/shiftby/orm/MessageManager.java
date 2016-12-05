package me.shiftby.orm;

import me.shiftby.Main;
import me.shiftby.UserAuth;
import me.shiftby.entity.Message;
import me.shiftby.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class MessageManager {
    private SessionFactory sessionFactory;

    private static volatile MessageManager instance;
    private MessageManager() {
        sessionFactory = Main.getSessionFactory();
    }
    public static MessageManager getInstance() {
        MessageManager localInstance = instance;
        if (localInstance == null) {
            synchronized (UserAuth.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = instance = new MessageManager();
                }
            }
        }
        return localInstance;
    }

    public List<Message> getMissedMessages(User to) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Message> query = builder.createQuery(Message.class);
        query.where(builder.equal(query.from(Message.class).get("to"), to));
        List<Message> messages = session.createQuery(query).getResultList();
        CriteriaDelete<Message> delete = builder.createCriteriaDelete(Message.class);
        delete.where(builder.equal(delete.from(Message.class).get("to"), to));
        session.createQuery(delete).executeUpdate();
        transaction.commit();
        session.close();
        return messages;
    }
    public void saveMessage(Message message) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(message);
        transaction.commit();
        session.close();
    }

    public void close() {
        instance = null;
    }

}
