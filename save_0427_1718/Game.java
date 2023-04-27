package alkesz;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener {
	private GameScreen gameScreen;
	// TODO private String howtoplay;
	private boolean isPlaying = false;
	
	List<Drinks> drinks;
	Character actPlayer;
	Character otherPlayer;
			
	Game(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		addKeyListener(this); //implements keylistener
		setFocusable(true); //focus on the component
	}
	
	public void paint(Graphics g) {	
		otherPlayer.draw(g, this);
		actPlayer.draw(g, this);
		for ( Drinks d:drinks ) {
			d.draw(g, this);
		}
	}
	
	public void update() { //where magic happens
		for ( Drinks drink:drinks ) {
			if ( !drink.isActive() )
				continue;
			
			if ( !drink.step() )	// lement a palyarol
				continue;

			if(drink.intersects(actPlayer)) {
				drink.setCatched(true);
				drink.setActive(false);
				actPlayer.addScore( drink.getScore(), drink.getDrunkScore());
				System.out.println( "Hurra: " + actPlayer.getScore() + ", " + actPlayer.getDrunkScore() );
			}
		}
		//gameScreen.setLabel1(""+actPlayer.getScore());
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) { //game start on enter
			if ( !isPlaying ) {
				isPlaying = true;
				new Thread(() -> {
					while(true) {
						update();	
						try {			//give chance for other tasks
							Thread.sleep(10);
						} catch(InterruptedException err){
							err.printStackTrace();
						}
					}
				}).start();
			}
		}
		if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {		// jobbra
			actPlayer.rightStep(getWidth());
		}
		if( e.getKeyCode() == KeyEvent.VK_LEFT ) {		// balra
			actPlayer.leftStep(0);
		}
		// Teszt. Ennek servertől kell jönnie
		if( e.getKeyCode() == KeyEvent.VK_NUMPAD0 ) {
			newDrink();
		}
		if( e.getKeyCode() == KeyEvent.VK_NUMPAD4 ) {
			otherPlayer.leftStep(0);
		}
		if( e.getKeyCode() == KeyEvent.VK_NUMPAD6 ) {
			otherPlayer.rightStep(getWidth());
		}
	}
	private void newDrink() {
		drinks.add(Drinks.newDrink());
	}
	public void history() {
		System.out.println("\n-------------");
		for ( Drinks drink:drinks ) {
			System.out.println(drink);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

