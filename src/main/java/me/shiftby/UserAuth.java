package me.shiftby;

import me.shiftby.entity.Role;
import me.shiftby.entity.User;
import me.shiftby.exception.AlreadyExistException;
import me.shiftby.orm.UserManager;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.Socket;

public class UserAuth {

    public static void fromSocket(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String command = reader.readLine();
        String username = reader.readLine();
        String password = reader.readLine();
        if (command.equals("/login")) {
            User user = UserManager.getInstance().findByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                writer.write("status.credential.valid");
                writer.flush();
                Main.getSessionManager().createSession(socket, user);
            } else {
                writer.write("status.credential.invalid");
                writer.close();
                socket.close();
            }
        } else if (command.equals("/signup")) {
            User user = new User(username, password, Role.USER);
            try {
                UserManager.getInstance().registerUsers(user);
                writer.write("status.user.valid");
            } catch (AlreadyExistException e) {
                writer.write("status.user.exist");
            } finally {
                writer.close();
                socket.close();
            }
        } else {
            writer.write("status.invalid.init.command");
            writer.close();
            socket.close();
        }
    }

}
