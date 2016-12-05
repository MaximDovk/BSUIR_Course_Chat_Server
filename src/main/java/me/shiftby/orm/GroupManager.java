package me.shiftby.orm;

import me.shiftby.Main;
import me.shiftby.UserAuth;
import me.shiftby.entity.Group;
import me.shiftby.exception.AlreadyExistException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupManager {

    private SessionFactory sessionFactory;
    private HashMap<String, Group> groups;


    private static volatile GroupManager instance;
    private Session session;
    private Transaction transaction;

    private GroupManager() {
        sessionFactory = Main.getSessionFactory();
        session = sessionFactory.openSession();
        groups = loadGroups();

    }
    public static GroupManager getInstance() {
        GroupManager localInstance = instance;
        if (localInstance == null) {
            synchronized (UserAuth.class) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = instance = new GroupManager();
                }
            }
        }
        return localInstance;
    }


    private HashMap<String, Group> loadGroups() {
        transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = builder.createQuery();
        query.select(query.from(Group.class));
        List list = session.createQuery(query).getResultList();
        transaction.commit();
        HashMap<String, Group> result = new HashMap<>();
        list.forEach((val) -> result.put(((Group)val).getName(), (Group)val));
        return result;
    }


    public Group findByName(String name) {
        return groups.get(name);
    }

    public boolean exist(String name) {
        return groups.containsKey(name);
    }
    public void createGroup(Group group) throws AlreadyExistException {
        if (!groups.containsKey(group.getName())) {
            groups.put(group.getName(), group);
            Transaction transaction = session.beginTransaction();
            session.save(group);
            transaction.commit();
        } else {
            throw new AlreadyExistException();
        }
    }
    public void changeGroup(Group group) {
        Transaction transaction = session.beginTransaction();
        session.update(group);
        transaction.commit();
    }
    public void removeGroup(Group group) {
        groups.remove(group.getName());
        Transaction transaction = session.beginTransaction();
        session.remove(group);
        transaction.commit();
    }

    public Set<String> getGroups() {
        return groups.keySet();
    }

    public void close() {
        instance = null;
        Transaction transaction = session.beginTransaction();
        for (Group group : groups.values()) {
            session.update(group);
        }
        transaction.commit();
        session.close();
    }
}
