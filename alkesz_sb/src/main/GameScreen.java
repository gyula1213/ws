package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.DecimalFormat;



public class GameScreen extends JFrame{
	
	private boolean isServer = false;	// false: client, true: server
	private ClientHandler handler = null;
	//TODO player12,drinks

	
	/**
	 * Ebben az osztalyban van a kepernyo tervezése
	 * Csak itt szerepelnek szamok (meretek)
	 */
	
	public static int CHARACTER_WITH = 50;		// jatekos szelessege
	public static int CHARACTER_HEIGHT = 50;	// jatekos magassaga
	public static int CHARACTER_LINE_Y = 500;	// y koordináta, ahol mozog
	public static int CHARACTER_START_X = 200;	// x koordináta, ahonnan indul
	
	public static int DRINK_WITH = 50;			// ital szelessege
	public static int DRINK_HEIGHT = 50;		// ital magassaga
	public static int DRINK_START_Y = 30; 		// y koordináta, ahol megjelenik
	
	public static int GAME_SCREEN_END_Y = 600;	
	public static int GAME_SCREEN_END_X = 800;
	public static int GAME_SCREEN_START_Y = 10;	
	public static int GAME_SCREEN_START_X = 0;
	public static int MAX_DARKNESS_Y = 400;
//	private JFrame gameScreen = new JFrame("Alkesz");		
	private Game game;
	
	public JLabel label1;
	public JLabel label2;
	public JLabel label3;
	public JLabel label4;
	
	private Character otherPlayer;
	private Character actPlayer;
	private ArrayList<Drinks> drinks = new ArrayList<Drinks>();
	
	javax.swing.Timer timer;	
	int second, minute;
	String ddSecond, ddMinute;
	JLabel counterLabel;
	DecimalFormat dFormat = new DecimalFormat("00");
	

	GameScreen( boolean isServer, ClientHandler handler, String name ) {
		super( "Alkesz gamescreen");
		this.isServer = isServer;
		this.handler = handler;
		if ( isServer )  {
			actPlayer = new Character(name,true);
			otherPlayer = new Character("",false);
		}
		else  {
			actPlayer = new Character(name,false);
			otherPlayer = new Character("",true);
		}
		drinks = new ArrayList<Drinks>();

		createGameScreen();
		game = new Game(this, isServer, handler );
		getContentPane().add(game);
		
		game.setActPlayer(actPlayer);
		otherPlayer.setSpeed(40);
		game.setOtherPlayer(otherPlayer);
		game.setDrinks(drinks);
		setVisible(true);
	}
	
	private void createGameScreen() {
		JPanel scoreTable_player1 = new JPanel();
		JPanel scoreTable_player2 = new JPanel();
		label1 = new JLabel();
		scoreTable_player1.add(label1);
		label2 = new JLabel();
		scoreTable_player1.add(label2);
		label3 = new JLabel();
		scoreTable_player2.add(label3);
		label4 = new JLabel();
		scoreTable_player2.add(label4);
		
		counterLabel = new JLabel("");
		scoreTable_player1.add(counterLabel);
		counterLabel.setText("02:00");
		second =0;
		minute =2;
		

		scoreTable_player1.setBorder(BorderFactory.createLineBorder(Color.red));
		scoreTable_player1.setBounds(GAME_SCREEN_END_X-200,GAME_SCREEN_START_Y+130,150,80);
		getContentPane().add(scoreTable_player1);

		scoreTable_player2.setBorder(BorderFactory.createLineBorder(Color.green));
		scoreTable_player2.setBounds(GAME_SCREEN_END_X-200,GAME_SCREEN_START_Y+260,150,50);
		getContentPane().add(scoreTable_player2);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(listener -> {
			dispose();
			
			new Menu();
			//game.history(); // logolás
		});
		scoreTable_player1.add(exit);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100,100);
		setSize(GAME_SCREEN_END_X,GAME_SCREEN_END_Y);
		setResizable(false);	
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		label1.setText(actPlayer.getName() + ", Score: "+actPlayer.getScore());
		label2.setText(actPlayer.getName() + ", DrunkScore: "+actPlayer.getDrunkScore());
		label3.setText(otherPlayer.getName() + ", Score: "+otherPlayer.getScore());
		label4.setText(otherPlayer.getName() + ", DrunkScore: "+otherPlayer.getDrunkScore());
		
	}
	
	public void countdownTimer() {
		
		timer = new javax.swing.Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);	
				counterLabel.setText(ddMinute + ":" + ddSecond);
				
				if(second==-1) {
					second = 59;
					minute--;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);	
					counterLabel.setText(ddMinute + ":" + ddSecond);
				}
				if(minute==0 && second==0) {
					timer.stop();
				}
			}
		});		
	}		
			

}
