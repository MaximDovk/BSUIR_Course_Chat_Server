package me.shiftby;

import me.shiftby.command.Command;
import me.shiftby.command.InvalidCommand;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;
import me.shiftby.orm.MessageManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Session extends Thread {

    private User user;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean isActive;

    public Session(Socket socket, User user) throws IOException {
        super(user.getUsername());
        this.socket = socket;
        this.user = user;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isActive = true;
        start();
    }

    public void send(String message) throws IOException {
        out.write(message);
        out.flush();
    }

    @Override
    public void run() {
        MessageManager.getInstance().getMissedMessages(user).forEach(
                message -> {
                    try {
                        send(new StringBuilder()
                                .append("/pm ")
                                .append(message.getFrom().getUsername())
                                .append(" ")
                                .append(message.getMessage())
                                .toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        String temp;
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            System.err.println("socket.timeout.error");
        }
        while (isActive) {
            try {
                temp = in.readLine();
                Command command = Main.getSessionManager().getInterpreter().interpret(temp, this.user);
                if (!Role.isGranted(command.getRole(), user.getRole())) {
                    command = new InvalidCommand(user);
                }
                command.execute();
            } catch (SocketTimeoutException e) {
                Main.getLogger().lowLevel("socket.timeout");
            } catch (SocketException e) {
                isActive = false;
            } catch (Exception e) {
                isActive = false;
                e.printStackTrace();
            }
        }
    }

    public User getUser() {
        return user;
    }

    void stopSession() throws IOException {
        send("server.stop");
        isActive = false;
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }
}
