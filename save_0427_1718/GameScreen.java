package alkesz;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameScreen extends JFrame{
	private int gametime;
	//TODO player12,drinks
	
	/**
	 * Ebben az osztalyban van a kepernyo tervezése
	 * Csak itt szerepelnek szamok (meretek)
	 */
	
	public static int CHARACTER_WITH = 50;		// jatekos szelessege
	public static int CHARACTER_HEIGHT = 50;	// jatekos magassaga
	public static int CHARACTER_LINE_Y = 480;	// y koordináta, ahol mozog
	public static int CHARACTER_START_X = 200;	// x koordináta, ahonnan indul
	
	public static int DRINK_WITH = 50;			// ital szelessege
	public static int DRINK_HEIGHT = 50;		// ital magassaga
	public static int DRINK_START_Y = 30; 		// y koordináta, ahol megjelenik
	
//	private JFrame gameScreen = new JFrame("Alkesz");		
	private Game game;
	
	public JLabel label1;
	
	private Character otherPlayer;
	private Character actPlayer;
	private ArrayList<Drinks> drinks = new ArrayList<Drinks>();

	GameScreen()  {
		super( "Alkesz gamescreen");
		otherPlayer = new Character("Other","player2.png");
		actPlayer = new Character("Actual","player3.png");
		drinks = new ArrayList<Drinks>();

		createGameScreen();
		game = new Game(this);
		getContentPane().add(game);
		
		// TODO JButton highscores = new JButton("Start"); //buttons TODO
		
		game.setActPlayer(actPlayer);
		otherPlayer.setSpeed(40);
		game.setOtherPlayer(otherPlayer);
		game.setDrinks(drinks);
		setVisible(true);
	}
	
	private void createGameScreen() {
		JPanel scoreTable = new JPanel();
		label1 = new JLabel("Pontszám: " + actPlayer.getScore());
		scoreTable.add(label1);
		
		scoreTable.setBorder(BorderFactory.createLineBorder(Color.black));
		scoreTable.setBounds(300,100,200,200);
		getContentPane().add(scoreTable);

		// Csak teszteléshez
//		JButton left = new JButton("Left");
//		left.addActionListener(listener -> {
//			otherPlayer.leftStep(0);
//			game.setFocusable(true);
//		});
//		scoreTable.add(left);
//		JButton right = new JButton("Right");
//		left.addActionListener(listener -> {
//			otherPlayer.rightStep(game.getWidth());
//			game.setFocusable(true);
//		});
//		scoreTable.add(right);
		

		JButton exit = new JButton("Exit");
		exit.addActionListener(listener -> {
			game.history();
			System.exit(0);		// TODO ezt meg at kell gondolni
		});
		scoreTable.add(exit);
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ALKESZ GAME");		
		setLocation(100,100);
		setSize(650,600);
		setResizable(false);	
	}
	public void setLabel1( String text ) {
		label1.setText(text);
	}
	
//	public void repaint() {
//		setLabel1(""+actPlayer.getScore());
//		gameScreen.repaint();
//	}
//
	public void paint(Graphics g) {
		super.paint(g);
		setLabel1("XXXXX: "+actPlayer.getScore());
//		otherPlayer.draw(g, this);
	}
	
}
