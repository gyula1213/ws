package alkesz;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

public class Character extends Rectangle {
		
	private String name;
	private int score;
	private int drunkScore;
	private int position;
	private int speed = 15;
	private boolean penalty;
	Image pic;
	boolean drink_catched; //need?
		
	Character( String name, String file) {
		super(GameScreen.CHARACTER_START_X, GameScreen.CHARACTER_LINE_Y, GameScreen.CHARACTER_WITH, GameScreen.CHARACTER_HEIGHT );	// A befoglaló méretek
		this.score=0;
		this.name = name;
		try {
			pic=ImageIO.read(new File("resource/"+file)); //import file? and start looking for extra files
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getScore() {
		return score;
	}
	public int getDrunkScore() {
		return drunkScore;
	}

	public void addScore(int score,int drunkscore) {
		this.score += score;
		this.drunkScore += drunkscore;
		if (this.drunkScore < 0) {
			this.drunkScore = 0;
		}
		if (this.drunkScore > 100) {
			this.drunkScore = 100;
		}
	}
	/**
	 * Jobbra megy
	 * @param endPoint: palya szele
	 */
	public void rightStep( int endPoint ) {
		if( x < endPoint - width - speed  ) { //only update pos if we are in the window size!
			x += speed;
		}
		else {
			x = endPoint - width;
		}
	}
	/**
	 * Balra megy
	 * @param startPoint: palya szele
	 */
	public void leftStep( int startPoint ) {
		if( x > startPoint + speed  ) { //only update pos if we are in the window size!
			x -= speed;
		}
		else {
			x = startPoint;
		}
	}

	String score_text = "Pontszám: ";
	String drunkscore_text = "Részegségi pontszám: ";

	public void draw(Graphics g, Component c) {
			//if(!drink_catched)
				g.drawImage(pic, x, y, width, height,c);
				String score_sz = Integer.toString(score);
				String drunkscore_sz = Integer.toString(drunkScore);
				String out_score = score_text + score_sz;
				String out_drunkscore = drunkscore_text + drunkscore_sz +"%";
				//g.setColor(Color.black);
				g.drawString(out_score, 400, 20);
				g.drawString(out_drunkscore, 335, 35);
				// draw drunkline
				g.drawRect(400,60,100*2,20);
				g.setColor(Color.red);
				g.fillRect(400,60,drunkScore*2,20);
	}
}
