package hu.bme.mit.brszta.examples;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketExample {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9001);
            socket.getOutputStream().write(12);
            socket.getOutputStream().flush();
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
