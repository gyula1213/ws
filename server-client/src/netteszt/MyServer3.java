package netteszt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
class Capitalizer implements Runnable {
    private Socket socket;
 
    public Capitalizer(Socket socket) {
        this.socket = socket;
    }
 
    @Override
    public void run() {
        System.out.println("Connected: " + socket);
        try (Scanner in = new Scanner(socket.getInputStream())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                out.println(in.nextLine().toUpperCase());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected: " + socket);
    }
}
 
public class MyServer3 {
    public static void main(String[] args) throws Exception {
        int port = 55000;
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("The capitalization server is running on port " + port);
            ExecutorService pool = Executors.newFixedThreadPool(3);
            while (true) {
                pool.execute(new Capitalizer(listener.accept()));
            }
        }
    }
}
