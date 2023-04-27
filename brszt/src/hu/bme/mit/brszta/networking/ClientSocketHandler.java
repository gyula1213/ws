package hu.bme.mit.brszta.networking;

import hu.bme.mit.brszta.ui.MyCanvas;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketHandler implements Runnable {

    private MyCanvas canvas;
    private JLabel connectionStatusLabel;
    private boolean isRunning = true;
    private Socket socket;

    public ClientSocketHandler(MyCanvas canvas, JLabel connectionStatusLabel) {
        this.canvas = canvas;
        this.connectionStatusLabel = connectionStatusLabel;
    }

    public void setRunning(boolean running) {
        isRunning = running;

        // lezárjuk a meglévő socket-et is (ha van ilyen), mert ha van, akkor lehet épp a blokkoló read utasításban
        // vár mi meg arra fogunk várni, hogy kapjunk valami adatot a commandertől
        if( !isRunning && socket != null && !socket.isClosed() ) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Client socket started");

        while (isRunning) {
            connectionStatusLabel.setText("Connecting...");

            // try with resource megoldás: nem kell a sok null-check és finally block a socket helyes bezárásához, mert
            // ez a konstrukció megoldja helyettünk
            try (Socket socket = new Socket("localhost", 9001)) {
                connectionStatusLabel.setText("Connected");

                // tárolunk egy referenciát a socket-ről, ugyanis ha a klienst szeretnénk bezárni, akkor a blokkoló
                // read utasítás nem fogja hagyni bezárni -> valahogy meg kell szakítani a kapcsolatot
                this.socket = socket;

                while (isRunning) {
                    int state = socket.getInputStream().read();
                    System.out.println("Received: "+state);

                    // a canvas-nek csak az állapotát állítjuk be, ezt követően majd a kirajzolást maga intézi el
                    canvas.setState(state);
                }

            } catch (IOException e) {
                connectionStatusLabel.setText("Error: " + e.getMessage());
            }

            // valami gond volt, várjunk egy kicsit azzal az újracsatlakozással (meg az sem gond, ha látod az error üzenetet is)
            synchronized (this) {
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Client socket stopped.");

    }
}
