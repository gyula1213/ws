package main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

public class Drinks extends Rectangle {
	private static long refTime;
	private static boolean isFirst = true;
	
	private enum TypeDrink {
		WATER( 0, -1, "water.jpg"),
		BEER( 1, 3, "beer.png"),
		WHISKEY(2,5,"whiskey.png");
		int score;	// Jutalompont az elkapasert
		int drunkScore;	// Reszegsegi allapot valtozasa
		Image pic;	// a kép file neve
		private TypeDrink( int score, int drunkScore, String name ) {
			this.score = score;
			this.drunkScore = drunkScore;
			try {
				pic=ImageIO.read(new File("resource/"+name));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private TypeDrink type;
	private int rnd;
	private int speed;
	private boolean isActive = true;
	private boolean isCatched = false;
	private long startTime;
			
	Drinks( int rnd, int startPosX, int speed  ) {
		super(startPosX, GameScreen.DRINK_START_Y, GameScreen.DRINK_WITH, GameScreen.DRINK_HEIGHT );	// A befoglaló méretek
		switch(rnd) {
		// 50 % esély a sörre, 30% a vízre és 20% a whiskeyre
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			type=TypeDrink.BEER;
			break;
		case 6:
		case 7:
		case 8:
			type=TypeDrink.WATER;
			break;
		case 9:
		case 10:
			type=TypeDrink.WHISKEY;
			break;
		default:	
		}
		this.rnd = rnd;
		this.speed = speed;
		this.startTime = new Date().getTime()	;
	}
	public int getRnd() {
		return rnd;
	}
	public int getScore() {
		return type.score;
	}
	public int getDrunkScore() {
		return type.drunkScore;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getSpeed() {
		return speed;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public void setCatched(boolean isCatched) {
		this.isCatched = isCatched;
	}
	public void draw(Graphics g, Component c) {
		if(isActive)				
			g.drawImage(type.pic, x, y, width, height, c);
	}
	/**
	 * Kovetkezo pozicio elfoglalasa
	 * @return false: leesett, true: minden ok
	 */
	public boolean step() {
		y += speed;
		if ( y > GameScreen.CHARACTER_LINE_Y) {
			isActive = false;
		}
		return isActive;
	}
	@Override
	public String toString() {
		String ret = "";
		ret += type.name();
		ret += ", started: " + (startTime - refTime);
		if ( isActive )
			ret += ", active";
		else
			ret += ", catched: " + this.isCatched;
 		return ret;
	}
	public String getCsv() {
		String ret = "Drink";
		ret += ";" + getRnd();
		ret += ";" + (int)getX();	// Startpos
		ret += ";" + getSpeed();
 		return ret;
	}
	public static Drinks newDrink() {
		if ( isFirst )
		{
			isFirst = false;
			refTime = new Date().getTime();
		}
		// random szám 1-10 között
        int type = (int)(10 * Math.random()) + 1;
        int pos = 20 + (int)(500 * Math.random()) + 1;
        int speed = (int)(3 * Math.random()) + 1;
		Drinks ret = new Drinks( type, pos, speed );
		
		return ret;
	}
}
