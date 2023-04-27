package netteszt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
 
public class MyClient2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 60000)) {
            System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
            try (Scanner consoleRead = new Scanner(System.in)) {
                try (Scanner networkRead = new Scanner(socket.getInputStream())) {
                	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    while (consoleRead.hasNextLine()) {
                        out.println(consoleRead.nextLine());
                        System.out.println(networkRead.nextLine());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
