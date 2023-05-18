package kocka;

import base.GameController;
import kocka.forgat.Forgat;

/**
 * Kocka kirakása több lépésben
 * Ez az osztály az egy lépést leíró jellemzőket tartalmazza, illetve egy lépést hajt végre (próbál meg végrehajtani)
 * @author slenk
 *
 */
public class OneStep
{
	private String name;			// A cél megnevezése
	private int [] orig;			// A lépés meghívásakor a kiinduló állapot
	private int [] target;			// A lépés végrehajtásakor az igényelt célállapot
	private int [] end;				// A lépés végrehajtása után ez jött létre
	private String [] commands;		// A végrehajtás során ezeket a parancsokat (forgatásokat) használhatjuk
	private Kocka kocka;			// A kocka, amin dolgozni fogunk
	
	String [] result;				// A megoldás lépései
	
	public OneStep(String name, int[] target, String... commands) {
		super();
		this.name = name;
		this.target = target;
		this.commands = commands;
	}
	public void createCube(int[] orig) {
		this.orig = orig;
        kocka = newKocka(orig);
        commands = kocka.init( commands ); // init beállítja a forgatásokat és egy tömbben visszaadja, amit valóban beállított
        kocka.setTarget(target);
//        System.out.println("One step...");
//        System.out.println("X0" + kocka);
//        Vector<Gameable>kockak = kocka.nextStages();
//        for ( Gameable g:kockak)
//            System.out.println(g);
	}
	protected Kocka newKocka(int []act) {
		return new Kocka(act);
	}
    public boolean solve()
    {
        GameController gc = new GameController(kocka); 
        //System.out.println();
        //System.out.println(kocka);
        gc.solve();
        result = gc.getResult();
        end = Forgat.forgatasok(orig, result);
        return true;	// TODO kezelni kell a sikertelen hívást is
    }
    public int [] getEnd()
    {
    	return end;
    }
    public int [] getOrig()
    {
    	return orig;
    }
    public String [] getResult()
    {
    	return result;
    }
    
    public int[] getTarget() {
		return target;
	}
	public String[] getCommands() {
		return commands;
	}
	public String getName()
    {
    	return name;
    }
    public String getSteps()
    {
    	String s = "";
    	for ( String res : result )
    	    s += res + ", ";
    	return s;
    }
    public String toString() {
    	String s = name;
    	if ( kocka!=null ) {
        	if ( target!=null ) {
            	s += "\ntarget:";
            	s += kocka.toString(target);
        	}
        	if ( orig!=null ) {
            	s += "\norig:";
            	s += kocka.toString(orig);
        	}
        	if ( result!=null ) {
            	s += "\nsteps:" + getSteps() + "\n";
        	}
        	if ( end!=null ) {
            	s += "\nnew:";
            	s += kocka.toString(end);
        	}
    	}
    	else {
        	s += "\ntarget:";
        	s += newKocka(target).toString(target);
    	}
    	return s;
    }
}
