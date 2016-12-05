package me.shiftby;

import me.shiftby.entity.User;
import me.shiftby.orm.UserManager;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.Socket;

public class UserAuth {
    public static void fromSocket(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String username = reader.readLine();
        String password = reader.readLine();
        User user = UserManager.getInstance().findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            writer.write("status.credential.valid");
            writer.flush();
            Main.getSessionManager().createSession(socket, user);
        } else {
            writer.write("status.credential.invalid");
            writer.flush();
            writer.close();
        }
    }
}
