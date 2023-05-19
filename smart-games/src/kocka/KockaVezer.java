package kocka;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import base.Gameable;
import hello.MemoryTest;
import kocka.forgat.Forgat;
import kocka.kocka4.Kocka4x4x4;
import screen.Cubeable;

/**
 * Bűvös kocka
 * 
 * Ez az osztály teremti meg a kapcsolatot az interaktív képernyő és a kockakirakó program között
 * 
 * @author slenk
 *
 */
public class KockaVezer
{
    private Kocka kocka;		// Ez egy "Master"-kocka
    private int [] orig;
    private Steps steps;
    private int [] saveStarted;		// Ezzel az állapottal indul a fejtés
    private String type;			// A kocka típusa
    
    public KockaVezer()	// Screen-ről hívott konstruktor
    {
    }
    public void setType(String type)
    {
    	this.type = type;
    }
    public void createKocka()
    {
    	kocka = new Kocka4x4x4();	// TODO type-tól függ
    }
    public static OneStep newOneStep( String name, int [] target, String... commands )
    {
    	OneStep oneStep = newOneStep( name, target, commands);
    	return oneStep;
    }
    private void addOneStep( String name, int [] target, String... commands )
    {
    	OneStep oneStep = newOneStep( name, target, commands);
    	steps.addStep(oneStep);
    }
    
    public static int [] getReady() {
    	return null;
    }
    /**
     * Tekereés inicializálása
     * Itt történik a lehetséges OneStep-ek beállítása
     */
	public void initGame() {
		steps = new Steps();
		kocka.createTryingSteps(steps);
		steps.initSolve(saveStarted);
	}
	public void restart() {
		kocka.restart(saveStarted);
	}
	
	public void init(String... commands) {
		kocka.addAllSimpleForgatas();
		for ( String s : commands ) {
			String [] x = s.split("-");
			if ( x.length > 1 )
				Forgat.addForgatas(s, kocka.getOriginal(), x);
		}
		Forgat.setAllActive(false);
		Forgat.setActive(true, commands);
	}
	public int getSize() {
		return 4;
	}
	public String [] getColors() {
		return kocka.getColors();
	}
	public boolean checkActTable() {
		return kocka.isReady();
	}
	public int[] getActTable() {
		return kocka.getActual();
	}
	public void forgat( String s ) {
		kocka.forgat(s);
	}
    /**
     * Az n. állapotnak megfelelő step adatait állítja be 
     * A tekerés végére teszi a táblát
     * Beállítja a megfelelő forgatásokat TODO
     */
    public void setActStep(int n) {
		int [] act = steps.getStage(n);
		// TODO Forgat osztályban az aktuális forgatások beállítása!
		kocka.setActTable(act);
    }
	/**
	 * Véletlenszerűen összekevert kocka indítása
	 */
	public void setRandomTable() {
		int [] act = kocka.createRandom();
    	saveStarted = act.clone();
		kocka.setActTable(act);
	}
	/**
	 * Teszteléshez beállított kocka indítása
	 */
	public void setTesztTable() {
		int [] act = kocka.getTeszt();
    	saveStarted = act.clone();
		kocka.setActTable(act);
	}
	
	public int getCntSteps() {
		if ( steps == null )
			return 0;
		return steps.size();
	}
	public String getName(int n) {
		return steps.getName(n);
	}
	public int[] getTarget(int n) {
		return steps.getTarget(n);
	}
	public String[] getCommands(int n) {
		return steps.getCommands(n);
	}
	public void setOrig(int n) {
		int [] act = steps.getOrig(n);
		kocka.setActTable(act);
	}
	public int[] getOrig(int n) {
		return steps.getOrig(n);
	}
	public void setEnd(int n) {
		int [] act = steps.getEnd(n);
		kocka.setActTable(act);
	}
	public int[] getEnd(int n) {
		return steps.getEnd(n);
	}
	public String[] getResult(int n) {
		return steps.getResult(n);
	}
}