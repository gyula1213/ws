package main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;

public class Character extends Rectangle {
		
	private String name;
	private int score;
	private int drunkScore;
	private int _PUNISHMENT_SCORE = 20;
	private int speed = 15;
	private boolean penalty = false; // i am under punishment
	private boolean punish = false; // i can punish 
	private boolean punished = false; // i punished so i cant punish anymore
	private boolean isServer;
	Image pic;
	Image baseball_pic;
	
		
	Character( String name, boolean isServer) {
		super(GameScreen.CHARACTER_START_X, GameScreen.CHARACTER_LINE_Y, GameScreen.CHARACTER_WITH, GameScreen.CHARACTER_HEIGHT );	// A befoglaló méretek
		this.score=0;
		this.name = name;
		this.isServer=isServer;
		setPic();	
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPos(int pos) {
		setLocation( pos, GameScreen.CHARACTER_LINE_Y );
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
	
	public boolean getPenalty() {
		return this.penalty;
	}
	
	public void setPunished(boolean punished) {
		this.punished = punished;
	}

	public boolean isPunish() {
		return this.punish;
	}

	public boolean isPenalty() {
		return penalty;
	}

	public void setPenalty(boolean penalty) {
		this.penalty = penalty;
	}

	
	

	
	
	
	public void setPunish(boolean punish) {
		this.punish=punish;
	}
	
	

	public void setScore(int score) {
		this.score = score;
	}

	public void setDrunkScore(int drunkScore) {
		this.drunkScore = drunkScore;
	}
	
	public void setPic() {
		String picture=null;
		
		if(punish && this.isServer) {
			picture="player2_baseball.png";
		} else if(!punish && this.isServer) {
			picture="player2.png";			
		}		
		else if(punish && (!this.isServer)) {
			picture="player3_baseball.png";
		} else if(!punish && (!this.isServer)) {
			picture="player3.png";			
		}
		
		try {
			pic=ImageIO.read(new File("resource/"+picture)); //import file? and start looking for extra files
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addScore(int score,int drunkscore) {
		this.score += score;
		this.drunkScore += drunkscore;
		
		if( this.score >= _PUNISHMENT_SCORE && !punished) {
			this.setPunish(true);
			this.setPic();
		}
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
		if(!penalty) {
			int reszeg = drunkScore/2 ;
			int remeg = (int)(reszeg * Math.random()) - reszeg/3;
			int lep = speed + remeg;

			if( x < endPoint - width - lep  ) { //only update pos if we are in the window size!
				x += lep;
			}
			else {
				x = endPoint - width;
			}
		}
	}
	/**
	 * Balra megy
	 * @param startPoint: palya szele
	 */
	public void leftStep( int startPoint ) {
		if(!penalty) {
			int reszeg = drunkScore/2;
			// kétharmad eséllyel elõre mész egyharmad eséllyel hátra
			int remeg = (int)(reszeg * Math.random()) - reszeg/3;
			int lep = speed + remeg;
	
			if( x > startPoint + lep  ) { //only update pos if we are in the window size!
				x -= lep;
			}
			else {
				x = startPoint;
			}
		}
	}

	private void drunkline(Graphics g) {
		g.drawRect(610, 100, 100, 20);
		g.setColor(Color.red);
		g.fillRect(610, 100, getDrunkScore(),20);
		g.setColor(Color.black);
	}

	private void darkness(Graphics g) {
		int darkness = getDrunkScore() *4;
		g.fillRect(0, 0, 570, darkness);
		g.drawRect(0, 0, 570, darkness);
	}

	public void drawCharacter(Graphics g, Component c) {
		setPic();
		g.drawImage(pic, x, y, width, height,c);
	}
	public void drawDrunkResult(Graphics g, Component c) {
		drunkline(g);
		darkness(g);
	}
	public String getCsv() {
		String ret = "Character";
		ret += ";" + getName();
		ret += ";" + (int)getX();
		ret += ";" + getScore();
		ret += ";" + getDrunkScore();
		ret += ";" + isPunish();
 		return ret;
	}
	
}
