package me.shiftby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketListener extends Thread implements AutoCloseable {

    private ServerSocket serverSocket;
    private boolean isRunning;

    public SocketListener(int port) throws IOException {
        super("Server socket listener");
        serverSocket = new ServerSocket(port);
        start();
        Main.getLogger().info("socketListener.start");
        isRunning = true;
    }

    @Override
    public void run() {
        try {
            serverSocket.setSoTimeout(1000);
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    UserAuth.fromSocket(clientSocket);
                } catch (SocketTimeoutException e) {
                    Main.getLogger().lowLevel("socketListener.timeout");
                }
            }
            serverSocket.close();
            Main.getLogger().info("socketListener.stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        Main.getLogger().info("socketListener.stopping");
        isRunning = false;
    }
}
