package hu.bme.mit.brszta.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

public class ServerSocketHandler implements Runnable {

    private boolean isRunning = true;
    private Queue<Integer> messageQueue;
    private ServerSocket serverSocket = null;

    /**
     * A ServerSocketHandler osztály konstruktorában vár egy üzenetsort amin keresztül kapja majd később az üzeneteket
     * <p>
     * Nem a ConcurrentLinkedQueue-t vesszük át! Interface-t veszünk át paraméterül hogy minél rugalmasabb legyen
     * az implementációnk.
     *
     * @param messageQueue
     */
    public ServerSocketHandler(Queue<Integer> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void setRunning(boolean running) {
        isRunning = running;

        if (!isRunning && serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        System.out.println("Server socket started");
        try {
            serverSocket = new ServerSocket(9001);

            while (isRunning) {
                try {
                    // külön try-catch-ben kezeljük azokat a hibákat, amik a kliens-sel kapcsolatos hibák: megszakadó
                    // kapcsolat stb.
                    Socket connection = serverSocket.accept();
                    System.out.println("Connection established.");

                    while (isRunning) {
                        // amit a queue-n keresztül kapunk rögtön küldjük is tovább a socketen
                        while (!messageQueue.isEmpty()) {
                            connection.getOutputStream().write(messageQueue.remove());
                            connection.getOutputStream().flush();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // teszünk egy minimális várakozást a szállal, hogy ne pörögjön 100%-on a processzor folyamatosan
                synchronized (this) {
                    try {
                        this.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Connection closed.");
        } catch (IOException e) {
            // itt csak azokat a hibákat kell kezelni, ami a server socket készítésekor merültek fel
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Server socket stopped");

    }
}
