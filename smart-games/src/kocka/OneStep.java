package kocka;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import base.GameController;
import base.Gameable;
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
        steps.add(this);
	}
	public void createCube(int[] orig) {
		this.orig = orig;
        kocka = newKocka(orig);
        kocka.init( commands );
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
    /**
     * Statikus tartalom:
     * A lépések egymásutáni kezelése
     */
	private static List<OneStep> steps = new ArrayList<>();
	private static int maxLevel = 0;	// A legmagasabb szint, ameddig már meg van fejtve a kocka
	private static int [] startOrig;	// A kocka kiinduló állapota
 	
	public static void init()
	{
		steps = new ArrayList<>();
	}
	public static void print()
	{
		int cnt=0;
		for ( OneStep step : steps )
		{
			System.out.print((++cnt) + ":: ");
			System.out.println(step);
		}
	}
	public static void printResult()
	{
		System.out.println("Steps:");
		int cnt=0;
		for ( OneStep step : steps )
		{
			System.out.println((++cnt) + ":: " + step.getName() + ": " + step.getSteps());
		}
	}
	/**
	 * Az n-edik szintig végrehajtja a lépéseket (megfejti a kockát)
	 * Ha maxLevel >= n, akkor nem csinál semmit.
	 * Ha n==0, akkor végigfejti
	 * @param orig
	 * @param n
	 * @return
	 */
	public static boolean solve( int n )
	{
		if ( n <= maxLevel )	// már meg van fejtve
			return true;
		
		int [] act = startOrig;
		int i=0;
		for ( OneStep step : steps )
		{
			if ( ++i > maxLevel ) {
				step.createCube(act);
				System.out.println("...before solve " + (maxLevel+1) + " level\n" + step);
				if ( !step.solve() )
					return false;
				System.out.println("...after solve " + (maxLevel+1) + " level\n" + step);
				maxLevel++;
				if ( n>0 && maxLevel>=n )
					break;
			}
			act = step.getEnd();
		}
		return true;
	}
	public static boolean allSolve()
	{
		return solve(0);
	}
	public static boolean allSolve( int [] orig )
	{
		initSolve(orig);
		return solve(0);
	}
	/**
	 * Beállítja a kiinduló állapotot
	 * @param orig
	 */
	public static void initSolve( int [] orig )
	{
		startOrig = orig;
	}
	/**
	 * Az n. állapotot próbáljuk előállítani
	 * Ha az előző állapot sincs még meg, akkor false;
	 * @param n
	 * @return
	 */
//	public static boolean solve( int n )
//	{
//		if ( n<1 || n>steps.size() )
//		{
//			// TODO exception
//			return false;
//		}
//		OneStep actStep = steps.get(n-1);
//		if ( actStep.getEnd() != null )	// Már meg van fejtve
//			return true;
//		if ( actStep.getOrig() == null )	// Nincs még beállítva a kiinduló állapot
//		{
//			if ( n == 0 )	// ez baj, mert így nem tudunk elindulni
//			{
//				// TODO exception
//				return false;
//			}
//			OneStep prev = steps.get(n-2);
//			int [] act = prev.getEnd();
//			if ( act == null )	// még az előző állapot sincs meg, rekurzívan megyünk vissza
//			{
//				if ( !solve(n-1))
//					return false;
//				act = prev.getEnd();
//			}
//			actStep.createCube(act);
//		}
//		System.out.println(actStep);
//		if ( !actStep.solve() )
//			return false;
//		System.out.println(actStep);
//		OneStep nextStep = steps.get(n-1);
//		nextStep.createCube(actStep.getEnd());
//		return true;
//	}
	/**
	 * Az n-edeik megfejtett állapotot adja vissza, ha n 0, akkor a kiinduló stage-t
	 * Ha nincs megfejtve eddig a szintig, akkor megfejti
	 * @param n
	 * @return
	 */
	public static int [] getStage( int n )
	{
		if ( n<=0 ) {
			return startOrig;	// a kiinduló állapot
		}
		if ( n>steps.size() ) {
			return startOrig;	// a kiinduló állapot TODO valami okosat kellene csinálni (nem kellene hagyni)
		}
		OneStep step = steps.get(n-1);
		int [] ret = step.getEnd();
		if ( ret == null ) {	// még nincs megfejtve, megfejtjük
			if ( !solve(n))
				return startOrig;	// a kiinduló állapot TODO valami okosat kellene csinálni hogy ilyenkor mit adjon vissza
			ret = step.getEnd();
		}
		return ret;
	}
	public static String getName(int n) {
		OneStep step = steps.get(n-1); 
		return step.getName(n);
	}
	public static int[] getTarget(int n) {
		OneStep step = steps.get(n-1); 
		return step.getTarget(n);
	}
	public static String[] getCommands(int n) {
		OneStep step = steps.get(n-1); 
		return step.getCommands(n);
	}
	public static int[] getOrig(int n) {
		OneStep step = steps.get(n-1); 
		return step.getOrig(n);
	}
	public static int[] getEnd(int n) {
		OneStep step = steps.get(n-1); 
		return step.getEnd(n);
	}
	public static String[] getResult(int n) {
		OneStep step = steps.get(n-1); 
		return step.getResult(n);
	}
}
