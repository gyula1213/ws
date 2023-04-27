package main;

import java.awt.Label;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner; // Import the Scanner class to read text files

public class Menu {
	
    //private static String serverName = "localhost";
    private static String serverName;
    private static int port = 50000;

	private String myip=null;
	private String name=null;
	private String Highscore="";

	
	ClientHandler handler;
	
	public Menu() {
		try {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
		myip = in.readLine();
		}catch(Exception e)
		{
			
		}
		
		
		JFrame waitingScreen=new JFrame();
		Label l1=new Label("Your ip:"+myip);
		waitingScreen.add(l1);
		l1.setBounds(0, 0, 150, 30);
		waitingScreen.setSize(200,100);    
		waitingScreen.setLayout(null); 
		waitingScreen.setVisible(false);
		JFrame startScreen = new JFrame();
		
		JPanel buttons = new JPanel();
		buttons.setSize(400,300);
			
		JButton startServer = new JButton("Start Server");
		startServer.addActionListener(listener -> {
			name=JOptionPane.showInputDialog(startScreen, "Name:");
			JOptionPane.showMessageDialog(startScreen, "Waiting...");
			startScreen.setVisible(false);
			waitingScreen.setVisible(true);
			handler = startServer();
			waitingScreen.setVisible(false);
			GameScreen gs = new GameScreen( true, handler,name ); 
		});
		
		JButton startClient = new JButton("Start Client");
		startClient.addActionListener(listener -> {
			name=JOptionPane.showInputDialog(startScreen, "Name:");
			serverName=JOptionPane.showInputDialog(startScreen, "Enter IP (Example: 152.66.181.9)");
			startScreen.setVisible(false);
			handler = startClient();
			GameScreen gs = new GameScreen( false, handler, name );
		});
		
		JButton howToPlay = new JButton("How To Play");
		howToPlay.addActionListener(listener -> {
			JOptionPane.showMessageDialog(startScreen, "Play with your friend!\nCatch drinks to get points!\n");
		});
		
		JButton highScores = new JButton("High Scores");
		highScores.addActionListener(listener -> {
			
			try {
				  ArrayList<HighScore> data = new ArrayList<HighScore>();
				  
			      File myObj = new File("highscores.txt");
			      Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) {
			    	  String [] f = myReader.nextLine().split(" ");
			    	  data.add(new HighScore(f[0],Integer.parseInt(f[1])));
			      }
			      myReader.close();
			      Collections.sort(data, (o1,o2)-> o2.Highscore-o1.Highscore);
			      for(int i=0;i<data.size();i++)
			      {
			    	  
			    	  Highscore+=data.get(i).print()+"\n";
			      }
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			
			
			JOptionPane.showMessageDialog(startScreen,Highscore);
			Highscore="";
			
		});
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(listener -> {
			startScreen.setVisible(false);
        	startScreen.dispose(); 
		});
		
		buttons.add(startServer);		
		buttons.add(startClient);
		buttons.add(howToPlay);	
		buttons.add(highScores);
		buttons.add(exit);	
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
            
        }catch(Exception e){
            e.printStackTrace();
        }
    	return myThread;
    }
}
