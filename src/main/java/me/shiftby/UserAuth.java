package me.shiftby;

import me.shiftby.orm.UserManager;

import java.io.*;
import java.net.Socket;

public class UserAuth {
    public static void fromSocket(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String login = reader.readLine();
        String password = reader.readLine();
        if (UserManager.getInstance().checkPassword(login, password)) {
            writer.write("Valid");
            SessionManager.getInstance().createSession(socket, login);
        } else {
            writer.write("Invalid");
        }
        writer.flush();
    }
}
