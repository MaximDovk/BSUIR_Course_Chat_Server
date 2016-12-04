package me.shiftby;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SessionManager {

    private static volatile SessionManager instance;
    private SessionManager() {}
    public static SessionManager getInstance() {
        SessionManager localInstance = instance;
        if (localInstance == null) {
            synchronized (SessionManager.class) {
                localInstance = instance = new SessionManager();
            }
        }
        return localInstance;
    }

    private HashMap<String, Session> sessions = new HashMap<>();

    public Session createSession(Socket socket, String login) throws IOException {
        Session session = new Session(socket, login);
        sessions.put(login, session);
        return session;
    }
    public void stopSession(Session session) throws IOException {
        sessions.remove(session.getLogin());
        session.stopSession();
    }
    public Session getByLogin(String login) {
        return sessions.get(login);
    }

    public void broadcast(String message, Session from) {
        sessions.forEach((login, session) -> {
            if (session != from) {
                try {
                    session.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
