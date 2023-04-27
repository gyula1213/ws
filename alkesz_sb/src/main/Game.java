package main;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class

public class Game extends JPanel implements KeyListener {
	
	private GameScreen gameScreen;
	private boolean isServer = false;	// false: client, true: server
	private ClientHandler handler = null;
	private boolean isPlaying = false;
	private static final int _PENALTY_SEC = 7; 

	
	javax.swing.Timer penaltyTimer;	
		
	
	List<Drinks> drinks;
	Character actPlayer;
	Character otherPlayer;
	DrinkGenerator drinkGenerator = new DrinkGenerator();
			
	Game(GameScreen gameScreen, boolean isServer, ClientHandler handler ) {
		this.gameScreen = gameScreen;
		this.isServer = isServer;
		this.handler = handler;
		handler.setGame(this);
		addKeyListener(this); //implements keylistener
		setFocusable(true); //focus on the component
	}
	
	public void paint(Graphics g) {
		otherPlayer.drawCharacter(g, this);
		for ( Drinks d:drinks ) {
			d.draw(g, this);
		}
		actPlayer.drawCharacter(g, this);
		actPlayer.drawDrunkResult(g, this);
	}
	
	public void update() 
	{ 
		
		if ( !isPlaying )
			return;
		for ( Drinks drink:drinks ) 
		{
			if ( !drink.isActive() )
				continue;
			
			if ( !drink.step() )	// lement a palyarol
				continue;

			if(drink.intersects(actPlayer)) 
			{
				drink.setCatched(true);
				drink.setActive(false);
				actPlayer.addScore( drink.getScore(), drink.getDrunkScore());
				handler.actualCharacter(actPlayer);
			}
		}
		if(otherPlayer.intersects(actPlayer) && actPlayer.isPunish()) {
			actPlayer.setPunish(false);
			actPlayer.setPunished(true);
			handler.setPenalty();
			System.out.println("Penalty set");
		}
		
		gameScreen.repaint();
		
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
	public void history() {
		System.out.println("\n-------------");
		for ( Drinks drink:drinks ) {
			System.out.println(drink);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if ( isServer ) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) { //game start on enter
				handler.startGame();
				gameStart();
				drinkGenerator.start();
			}
		}
		if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {		// jobbra
			actPlayer.rightStep(getWidth()-100);
			handler.actualCharacter(actPlayer);
		}
		if( e.getKeyCode() == KeyEvent.VK_LEFT ) {		// balra
			actPlayer.leftStep(0);
			handler.actualCharacter(actPlayer);
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
		gameScreen.countdownTimer();
		gameScreen.timer.start();
		
		new Thread(() -> {	
			while(true) {
				if (!gameScreen.timer.isRunning()) {
					isPlaying = false;
					drinkGenerator.stop();
					GameOver();
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
	public void actualOtherCharacter( String name, int pos, int score, int drunkScore, boolean punish ) {
		otherPlayer.setName(name);
		otherPlayer.setPos(pos);
		otherPlayer.setScore(score);
		otherPlayer.setDrunkScore(drunkScore);		
		otherPlayer.setPunish(punish);
	}
	
	public void GameOver() {
				
		
		try {
		      FileWriter myWriter = new FileWriter("highScores.txt",true);		      
		      myWriter.write(actPlayer.getName()+" "+String.valueOf(actPlayer.getScore())+ "\n"+otherPlayer.getName()+" "+String.valueOf(otherPlayer.getScore())+"\n" );
		      myWriter.close();		    
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
	}
	public void setPenalty()  {
        System.out.println("Penalty started: "+ new Date());
		actPlayer.setPenalty(true);
		timerPenalty();
	}
	/**
	 * Büntetés automatikus befejezése x mp múlva
	 */
	private void timerPenalty()  {
	    Timer t = new Timer();
	    TimerTask task = new TimerTask() {
	      public void run()
	      {
	  		actPlayer.setPenalty(false);
	        System.out.println("End of punishnent: "+ new Date());
	      }
	    };
	    t.schedule(task, _PENALTY_SEC * 1000);
	}
	
	
	private class DrinkGenerator extends Thread {
		
		
		public void run() {
			
		
			while(true) {
				Drinks drink = Drinks.newDrink();
				handler.addDrink( drink );
				addDrink(drink);
			
				try {			
					Thread.sleep(1000);
				} catch(InterruptedException err){
					err.printStackTrace();
				}
			}
			
		}				
	}
}



