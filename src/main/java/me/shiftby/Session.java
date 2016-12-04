package me.shiftby;

import me.shiftby.command.Command;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Session extends Thread {

    private String login;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean isActive;

    public Session(Socket socket, String login) throws IOException {
        super(login);
        this.socket = socket;
        this.login = login;
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
        String temp;
        try {
            socket.setSoTimeout(1000);
        } catch (SocketException e) {
            System.err.println("socket.timeout.error");
        }
        while (isActive) {
            try {
                temp = in.readLine();
                Command command = (new Interpreter()).interpret(temp, this);
                Main.getLogger().info(command.getClass().toString());
                command.execute();
            } catch (SocketTimeoutException e) {
                Main.getLogger().lowLevel("socket.timeout");
            } catch (Exception e) {
                isActive = false;
            }
        }
    }

    public String getLogin() {
        return login;
    }

    public void stopSession() throws IOException {
        send("STOP");
        isActive = false;
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }
}
