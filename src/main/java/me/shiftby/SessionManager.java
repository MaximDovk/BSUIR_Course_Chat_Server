package me.shiftby;

import me.shiftby.command.Interpreter;
import me.shiftby.entity.User;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class SessionManager {

    private HashMap<String, Session> sessions = new HashMap<>();
    private Interpreter interpreter;

    public SessionManager(Interpreter interpreter) {
        this.interpreter = interpreter;
    }


    public Session createSession(Socket socket, User user) throws IOException {
        Session session = new Session(socket, user);
        sessions.put(user.getUsername(), session);
        return session;
    }
    public void stopSession(Session session) throws IOException {
        sessions.remove(session.getUser().getUsername());
        session.stopSession();
    }

    public Session getByUsername(String username) {
        return sessions.get(username);
    }
    public Set<String> getUsers() {
        return sessions.keySet();
    }

    public void broadcast(String message, User from) {
        sessions.forEach((login, session) -> {
            if (session.getUser() != from) {
                try {
                    session.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void close() {
        Object[] values = sessions.values().toArray();
        for (int i = 0; i < values.length; i++) {
            try {
                stopSession((Session) values[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }
}
