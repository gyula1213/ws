package netteszt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
 
public class MyServer1 {
    public static void main(String[] args) {
        int port = 50000;
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("The date server is listening on port " + port);
            while (true) {
                try (Socket socket = listener.accept()) {
                    System.out.println("A client has connected.");
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                    out.close();
                    System.out.println("Connection ended.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}