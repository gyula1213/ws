package main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
	private Graphics g2;
	private GameScreen gameScreen;
	private boolean isServer = false;	// false: client, true: server
	private ClientHandler handler = null;
	
	// TODO private String howtoplay;
	private boolean isPlaying = false;
	private static long _GAME_TIME = 5*60*1000 + 200;	//  perc
	private static long refTime;
	
	private DrinkFactory drinkFactory;
	
	List<Drinks> drinks;
	Character actPlayer;
	Character otherPlayer;
	
	private static final int _PUNISHMENT_SEC = 10; 
	
	Game(GameScreen gameScreen, boolean isServer, ClientHandler handler ) {
		this.gameScreen = gameScreen;
		this.isServer = isServer;
		this.handler = handler;
		handler.setGame(this);
		addKeyListener(this); //implements keylistener
		setFocusable(true); //focus on the component
	}
	
	public void paint(Graphics g) {
		//super.paint(g);
		if ( g2 != null ) {
			g2 = g;
			gameScreen.setG2(g2);
		}
		otherPlayer.drawCharacter(g, this);
		for ( Drinks d:drinks ) {
			d.draw(g, this);
		}
		actPlayer.drawCharacter(g, this);
		actPlayer.drawDrunkResult(g, this);
	}
	
	public void update() { //where magic happens
		if ( !isPlaying )
			return;
		for ( Drinks drink:drinks ) {
			if ( !drink.isActive() )
				continue;
			
			if ( !drink.step() )	// lement a palyarol
				continue;

			if(drink.intersects(actPlayer)) {
				drink.setCatched(true);
				drink.setActive(false);
				actPlayer.addScore( drink.getScore(), drink.getDrunkScore());
				//System.out.println( actPlayer.getScore() + ", " + actPlayer.getDrunkScore() );
				handler.actualCharacter(actPlayer);
			}
		}
		gameScreen.repaint();
		//repaint();
	}
	
	public void setOtherPlayer(Character otherPlayer) {
		this.otherPlayer = otherPlayer;
	}

	public void setActPlayer(Character actPlayer) {
		this.actPlayer = actPlayer;
	}

	public void setDrinks(List<Drinks> drinks) {
		this.drinks = drinks;
	}
	public void setPunishment()  {
        System.out.println("Punishnent started: "+ new Date());
		actPlayer.setUnderPunishment(true);
		timerPunishment();
	}
	/**
	 * Büntetés automatikus befejezése x mp múlva
	 */
	private void timerPunishment()  {
	    Timer t = new Timer();
	    TimerTask task = new TimerTask() {
	      public void run()
	      {
	  		actPlayer.setUnderPunishment(false);
	        System.out.println("End of punishnent: "+ new Date());
	      }
	    };
	    t.schedule(task, _PUNISHMENT_SEC * 1000);
	}
			
	public void history() {
		System.out.println("\n-------------");
		for ( Drinks drink:drinks ) {
			System.out.println(drink);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if ( isServer ) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) { //game start on enter
				handler.startGame();
				gameStart();
				drinkFactory = new DrinkFactory();
				drinkFactory.start();
			}
			if( e.getKeyCode() == KeyEvent.VK_SPACE ) {
				Drinks drink = Drinks.newDrink();
				handler.addDrink( drink );
				addDrink(drink);
			}
		}
		if ( actPlayer.isMayBePunishment() ) {
			if( e.getKeyCode() == KeyEvent.VK_X ) {		// büntetés
				actPlayer.setMayBePunishment(false);
				actPlayer.setAfterPunishment(true);
				handler.setUnderPunishment();
			}
		}
		if ( !actPlayer.isUnderPunishment() ) {
			if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {		// jobbra
				actPlayer.rightStep(getWidth()-100);
				handler.actualCharacter(actPlayer);
			}
			if( e.getKeyCode() == KeyEvent.VK_LEFT ) {		// balra
				actPlayer.leftStep(0);
				handler.actualCharacter(actPlayer);
			}
		}
		// Teszt. Ennek servertÅ‘l kell jÃ¶nnie
		if( e.getKeyCode() == KeyEvent.VK_A ) {
			otherPlayer.leftStep(0);
		}
		if( e.getKeyCode() == KeyEvent.VK_D) {
			otherPlayer.rightStep(getWidth());
		}
		if( e.getKeyCode() == KeyEvent.VK_NUMPAD9 ) {
			otherPlayer.addScore( 10, 20);
		}
		if( e.getKeyCode() == KeyEvent.VK_NUMPAD3 ) {
			otherPlayer.addScore( 0, -5);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void gameStart() {
		if ( isPlaying )
			return;
		
		isPlaying = true;
		//gameScreen.startClock(0);
		gameScreen.goOnClock();
		refTime = new Date().getTime();
		new Thread(() -> {
			while(true) {
				if ( new Date().getTime() - refTime > _GAME_TIME ) {
					isPlaying = false;
					gameScreen.stopClock();
					break;
				}
				update();	
				try {			//give chance for other tasks
					Thread.sleep(10);
				} catch(InterruptedException err){
					err.printStackTrace();
				}
			}
		}).start();
	}
	public void addDrink( Drinks drink ) {
		drinks.add(drink);
	}
	public void actualOtherCharakter( String name, int pos, int score, int drunkScore, int maybePunisment ) {
		otherPlayer.setName(name);
		otherPlayer.setPos(pos);
		otherPlayer.setScore(score);
		otherPlayer.setDrunkScore(drunkScore);
		if ( maybePunisment == 1 )
			otherPlayer.setMayBePunishment(true);
		else
			otherPlayer.setMayBePunishment(false);
	}
	
	/**
	 * Sör adagolás, Server oldalon
	 */
	private class DrinkFactory extends Thread  {
	      public void run()
	      {
	    	  while(true) {
					Drinks drink = Drinks.newDrink();
					handler.addDrink( drink );
					addDrink(drink);
					try {
						Thread.sleep(3000);
					} catch(InterruptedException err){
						err.printStackTrace();
					}
	    	  }
	      }
	}
}
