package netteszt;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
class Player extends Thread {
	private Socket socket;
	private Player otherPlayer;
	private String name;
	//private Scanner in;
	private PrintWriter out;
	
    public Player(String name, Socket socket) {
    	this.name = name;
        this.socket = socket;
    }
 
    @Override
    public void run() {
        System.out.println("Connected: " + socket);
        try ( Scanner in = new Scanner(socket.getInputStream())) {
//        	while ( out == null )
//        		;
//            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                System.out.println("Message from " + name);
            	if ( out == null )
            		continue;
                out.println(in.nextLine().toUpperCase());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected: " + socket);
    }

	public void setOtherPlayer(Player otherPlayer) {
		this.otherPlayer = otherPlayer;
        try {
			out = new PrintWriter(otherPlayer.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	@Override
	public String toString() {
		return "Player [socket=" + socket + ", name=" + name + "]";
	}
    
}
 
public class GameServer {
    public static void main(String[] args) throws Exception {
        int port = 40000;
        Player player1 = null;
        Player player2 = null;
        boolean isConnected = false;
        try (ServerSocket listener = new ServerSocket(port)) {
            System.out.println("The game server is running on port " + port);
            //ExecutorService pool = Executors.newFixedThreadPool(3);
            if (true) {
            	if ( player1 == null ) {
                    System.out.println("Waiting for player1");
                    player1 = new Player("Player1", listener.accept());
                    player1.start();
                    //pool.execute(player1);
            	}
            	if ( player2 == null ) {
                    System.out.println("Waiting for player2");
                    player2 = new Player("Player2", listener.accept());
                    player2.start();
                    //pool.execute(player2);
            	}
            	if ( !isConnected ) {
            		isConnected = true;
                	player1.setOtherPlayer(player2);
                	player2.setOtherPlayer(player1);
            	}
                //System.out.println("player1: " + player1);
                //System.out.println("player2: " + player2);
            }
            while(true);
        }
    }
}
