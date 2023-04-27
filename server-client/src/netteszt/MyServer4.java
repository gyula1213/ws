package netteszt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.Timer;

public class MyServer4 {
	static Timer mehetIdozito;
	static ServerSocket listener;
    public static void main(String[] args) {
		mehetIdozito = new Timer (1, new MehetIdozito_Time());
		mehetIdozito.start();
        int port = 50000;
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("The date server is listening on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true);
    }
	static class MehetIdozito_Time implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
            System.out.println("Timer");
            while (true) {
                try (Socket socket = listener.accept()) {
                    System.out.println("A client has connected.");
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                    out.close();
                    System.out.println("Connection ended.");
                } catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
		}
	}
}