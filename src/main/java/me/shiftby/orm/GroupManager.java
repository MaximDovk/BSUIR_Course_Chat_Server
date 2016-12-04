package me.shiftby.orm;

import me.shiftby.Main;
import me.shiftby.UserAuth;
import me.shiftby.entity.Group;
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
    private GroupManager() {
        sessionFactory = Main.getSessionFactory();
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
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object> query = builder.createQuery();
        query.select(query.from(Group.class));
        List list = session.createQuery(query).getResultList();
        transaction.commit();
        session.close();
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
    public void createGroup(Group group) throws Exception {
        if (!groups.containsKey(group.getName())) {
            groups.put(group.getName(), group);
            org.hibernate.Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            session.save(group);
            transaction.commit();
            session.close();
        } else {
            throw new Exception();
        }
    }
    public void changeGroup(Group group) {
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(group);
        transaction.commit();
        session.close();
    }
    public void removeGroup(Group group) {
        groups.remove(group.getName());
        org.hibernate.Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.remove(group);
        transaction.commit();
        session.close();
    }

    public Set<String> getGroups() {
        return groups.keySet();
    }

    public void close() {
        groups.forEach((name, group) -> {
            changeGroup(group);
        });
    }
}
