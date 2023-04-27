package netteszt;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
 
public class MyClient1 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 50000)) {
            try (Scanner in = new Scanner(socket.getInputStream())) {
                System.out.println(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
