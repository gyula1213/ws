package netteszt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
 
public class MyServer2 {
    public static void main(String[] args) {
        int port = 60000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("The capitalization server is running on port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("A client has connected: " + socket);
                    try (Scanner socketScanner = new Scanner(socket.getInputStream())) {
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        while (socketScanner.hasNextLine()) {
                            output.println(socketScanner.nextLine().toUpperCase());
                        }
                        output.close();
                    }
                    System.out.println("Connection closed: " + socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}