package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demo.Clock;
import demo.LedCanvas;

public class GameScreen extends JFrame{
	private Graphics g2;
	private int gametime;
	
	private boolean isServer = false;	// false: client, true: server
	private ClientHandler handler = null;
	//TODO player12,drinks
	
    private Clock clock;            // Az ido meresehez kell egy ora
    private JPanel clockPanel;       // Az orat ezen a panelon
	private LedCanvas clockCanvas;  //      jelenitjuk meg
	private int saveSec;            // Hogy tudjuk tovabbvinni a masodperceket
	
	
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

	GameScreen( boolean isServer, ClientHandler handler ) {
		super( "Alkesz gamescreen");
		this.isServer = isServer;
		this.handler = handler;
		
		if ( isServer )  {
			actPlayer = new Character("Server","player3");
			otherPlayer = new Character("","player2");
		}
		else  {
			actPlayer = new Character("Balazs","player2");
			otherPlayer = new Character("","player3");
		}
		drinks = new ArrayList<Drinks>();

		createGameScreen();
		game = new Game(this, isServer, handler );
		getContentPane().add(game);
		
		// TODO JButton highscores = new JButton("Start"); //buttons TODO
		
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
		
		createClockPanel();
		clockPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		clockPanel.setBounds(GAME_SCREEN_END_X-200,GAME_SCREEN_START_Y,150,50);
		getContentPane().add(clockPanel);

		scoreTable_player1.setBorder(BorderFactory.createLineBorder(Color.red));
		scoreTable_player1.setBounds(GAME_SCREEN_END_X-200,GAME_SCREEN_START_Y+130,150,80);
		getContentPane().add(scoreTable_player1);

		scoreTable_player2.setBorder(BorderFactory.createLineBorder(Color.green));
		scoreTable_player2.setBounds(GAME_SCREEN_END_X-200,GAME_SCREEN_START_Y+260,150,50);
		getContentPane().add(scoreTable_player2);

		// Csak teszteléshez
//		JButton left = new JButton("Left");
//		left.addActionListener(listener -> {
//			otherPlayer.leftStep(0);
//			repaint();
//			game.setFocusable(true);
//		});
//		scoreTable.add(left);
//		JButton right = new JButton("Right");
//		right.addActionListener(listener -> {
//			otherPlayer.rightStep(game.getWidth());
//			repaint();
//			game.setFocusable(true);
//		});
//		scoreTable.add(right);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(listener -> {
			game.history();
			System.exit(0);		// TODO ezt meg at kell gondolni
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
		//drunkline(g);
		//darkness(g);
		
	}
	private void drunkline(Graphics g) {
		g.drawRect(610, 125, 100, 20);
		g.setColor(Color.red);
		g.fillRect(610, 125, actPlayer.getDrunkScore(),20);
		g.setColor(Color.black);
	}

	private void darkness(Graphics g) {
		int darkness = actPlayer.getDrunkScore() *4;
		g.fillRect(0, 0, 570, darkness);
		g.drawRect(0, 0, 570, darkness);
	}

    public void createClockPanel()
    {
	    if ( clock != null )
	    {
    	    clockCanvas = new LedCanvas( 30, 3, clock.getSec() );
            clock.setLedCanvas( clockCanvas );
	    }
	    else
	    {
    	    clockCanvas = new LedCanvas( 30, 3, 0 );
	    }

	    clockPanel = new JPanel();
	    clockPanel.add( clockCanvas );
    }
    
    public void addClockPanel()
    {
		if ( clockPanel != null )
			remove( clockPanel );
        else
            createClockPanel();

		add(clockPanel);
    }
    
    public void startClock( int sec )
    {
        clock = new Clock(sec);
        clock.setLedCanvas( clockCanvas );
        clock.start();
    }
    public void stopClock()
    {
        if ( clock != null )
        {
            clock.stopClock();
            saveSec = clock.getSec();
            clock = null;
        }
    }
    public void goOnClock()
    {
        startClock( saveSec );
    }

	public void setG2(Graphics g2) {
		this.g2 = g2;
	}
}
