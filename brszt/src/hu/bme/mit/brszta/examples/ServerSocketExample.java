package hu.bme.mit.brszta.examples;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketExample {

    public static void main(String[] args) {

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(9001);
            Socket clientSocket = socket.accept();
            int data = clientSocket.getInputStream().read();
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( socket != null ) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
