package alkesz;

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
		WATER( 0, -5, "water.jpg"),
		BEER( 10, 20, "beer.png");
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
	private int speed;
	private boolean isActive = true;
	private boolean isCatched = false;;
	private long startTime;
			
	Drinks( int rnd, int startPosX, int speed  ) {
		super(startPosX, GameScreen.DRINK_START_Y, GameScreen.DRINK_WITH, GameScreen.DRINK_HEIGHT );	// A befoglaló méretek
		switch(rnd) {
		case 1:
			type=TypeDrink.WATER;
			break;
		case 2:
		default:
			type=TypeDrink.BEER;
			break;
		}
		this.speed = speed;
		this.startTime = new Date().getTime()	;
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
		if ( y > 500) {
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
	
	public static Drinks newDrink() {
		if ( isFirst )
		{
			isFirst = false;
			refTime = new Date().getTime();
		}
        int type = (int)(2 * Math.random()) + 1;
        int pos = 20 + (int)(400 * Math.random()) + 1;
        int speed = (int)(3 * Math.random()) + 1;
		Drinks ret = new Drinks( type, pos, speed );
		
		return ret;
	}
}
