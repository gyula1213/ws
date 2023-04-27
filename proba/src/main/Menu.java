package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu {
	
    private static String serverName = "localhost";
    //private static String serverName = "192.168.0.122";
    private static int port = 50000;

    private String highscorepath;
	private int communicationID;
	private int communicationRole;
	
	//TODO status enum
	//TODO functions
	private int gametime;
	//TODO player12,drinks
	
	ClientHandler handler;
	
	public Menu() {
		JFrame startScreen = new JFrame();
		JPanel buttons = new JPanel();
		buttons.setSize(400,300);
		
		
		JButton startServer = new JButton("Start Server");
		startServer.addActionListener(listener -> {
			startScreen.setVisible(false);
			handler = startServer();
			GameScreen gs = new GameScreen( true, handler ); 
		});
		
		JButton startClient = new JButton("Start Client");
		startClient.addActionListener(listener -> {
			startScreen.setVisible(false);
			handler = startClient();
			GameScreen gs = new GameScreen( false, handler );
		});
		
		buttons.add(startServer);		
		buttons.add(startClient);		
		startScreen.getContentPane().add(buttons);		
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setTitle("ALKESZ MENU");		
		startScreen.setSize(600,400);
		startScreen.setResizable(false);		
		startScreen.setVisible(true);
	}
	
	public static void main(String[] args) {
		System.out.println("Menu indul!");
		new Menu();
	}
	
    public static ClientHandler startServer() {
        ClientHandler myThread = null;
		ServerSocket myserverSocket = null;;
		// getting client request
		Socket mynewSocket = null;
		try
		{
			myserverSocket = new ServerSocket(port);
			System.out.println("Waiting for client");
			// mynewSocket object to receive incoming client requests
			mynewSocket = myserverSocket.accept();
			System.out.println("A new connection identified : " + mynewSocket);
			// obtaining input and out streams
			DataInputStream ournewDataInputstream = new DataInputStream(mynewSocket.getInputStream());
			DataOutputStream ournewDataOutputstream = new DataOutputStream(mynewSocket.getOutputStream());
			System.out.println("Thread assigned");
			myThread = new ClientHandler(mynewSocket, ournewDataInputstream, ournewDataOutputstream);
			// starting
			myThread.start();
			return myThread;
			
		}
		catch (Exception e){
			try {
				mynewSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return myThread;
	}
    public static ClientHandler startClient() {
        System.out.println("Trying connection");
        ClientHandler myThread = null;
        try
        {
            // establishing the connection 
            Socket ournewsocket = new Socket(serverName, port);           
            DataInputStream ournewDataInputstream = new DataInputStream(ournewsocket.getInputStream());
            DataOutputStream ournewDataOutputstream = new DataOutputStream(ournewsocket.getOutputStream());
			System.out.println("Thread assigned");
			myThread = new ClientHandler(ournewsocket, ournewDataInputstream, ournewDataOutputstream);
			// starting
			myThread.start();
            
            // TODO ournewDataInputstream.close();	// Valahol le kell majd zárni
            // TODO ournewDataOutputstream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    	return myThread;
    }
}
