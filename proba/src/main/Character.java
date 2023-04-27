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
	private int speed = 15;
	Image pic;
	Image picMayBePunishment;
	boolean drink_catched; //need?
		
	private boolean mayBePunishment = false;	// true, ha büntethetünk, false, ha nem
	private boolean underPunishment = false;	// true, ha büntetés alatt vagy
	private boolean afterPunishment = false;	// true, ha büntettél már
	private final int _PUNISHMENT_SCORE = 3;	// innen lehet büntetni
	
	Character( String name, String file) {
		super(GameScreen.CHARACTER_START_X, GameScreen.CHARACTER_LINE_Y, GameScreen.CHARACTER_WITH, GameScreen.CHARACTER_HEIGHT );	// A befoglalÃ³ mÃ©retek
		this.score=0;
		this.name = name;
		try {
			pic=ImageIO.read(new File("resource/"+file+".png")); //import file? and start looking for extra files
			picMayBePunishment=ImageIO.read(new File("resource/"+file+"_baseball.png")); //import file? and start looking for extra files
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void setScore(int score) {
		this.score = score;
	}

	public void setDrunkScore(int drunkScore) {
		this.drunkScore = drunkScore;
	}

	public void setAfterPunishment(boolean afterPunishment) {
		this.afterPunishment = afterPunishment;
	}

	public boolean isMayBePunishment() {
		return mayBePunishment;
	}

	public boolean isUnderPunishment() {
		return underPunishment;
	}

	public void setUnderPunishment(boolean underPunishment) {
		this.underPunishment = underPunishment;
	}

	public void setMayBePunishment(boolean mayBePunishment) {
		this.mayBePunishment = mayBePunishment;
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
		if ( this.score >= _PUNISHMENT_SCORE && !afterPunishment )
			this.mayBePunishment = true;
	}
	
	
	/**
	 * Jobbra megy
	 * @param endPoint: palya szele
	 */
	public void rightStep( int endPoint ) {
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
	/**
	 * Balra megy
	 * @param startPoint: palya szele
	 */
	public void leftStep( int startPoint ) {
		int reszeg = drunkScore/2;
		// kÃ©tharmad esÃ©llyel elÅ‘re mÃ©sz egyharmad esÃ©llyel hÃ¡tra
		int remeg = (int)(reszeg * Math.random()) - reszeg/3;
		int lep = speed + remeg;

		if( x > startPoint + lep  ) { //only update pos if we are in the window size!
			x -= lep;
		}
		else {
			x = startPoint;
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
		if ( this.isMayBePunishment() )
			g.drawImage(picMayBePunishment, x, y, width, height,c);
		else
			g.drawImage(pic, x, y, width, height,c);
		if ( this.isUnderPunishment() ) {
			g.fillRect(x, y, width, height);
		}
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
		if ( this.mayBePunishment )
			ret += ";1";
		else
			ret += ";0";
 		return ret;
	}
	
}
