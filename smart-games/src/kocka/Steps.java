package kocka;

import java.util.ArrayList;
import java.util.List;

/**
 * Kocka kirakása több lépésben
 * Ez az osztály fogja össze a lépéseket
 * @author slenk
 *
 */
public class Steps
{
	private List<OneStep> steps = new ArrayList<>();
	private int maxLevel = 0;	// A legmagasabb szint, ameddig már meg van fejtve a kocka
	private int [] startOrig;	// A kocka kiinduló állapota
 	
	public void init()
	{
		steps = new ArrayList<>();
	}
	public void addStep( OneStep step )
	{
		steps.add(step);
	}
	public void print()
	{
		int cnt=0;
		for ( OneStep step : steps )
		{
			System.out.print((++cnt) + ":: ");
			System.out.println(step);
		}
	}
	public void printResult()
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
	public boolean solve( int n )
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
	public boolean allSolve()
	{
		return solve(0);
	}
	public boolean allSolve( int [] orig )
	{
		initSolve(orig);
		return solve(0);
	}
	/**
	 * Beállítja a kiinduló állapotot
	 * @param orig
	 */
	public void initSolve( int [] orig )
	{
		startOrig = orig;
	}
	/**
	 * Az n. állapotot próbáljuk előállítani
	 * Ha az előző állapot sincs még meg, akkor false;
	 * @param n
	 * @return
	 */
//	public boolean solve( int n )
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
	public int [] getStage( int n )
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
	public String getName(int n) {
		OneStep step = steps.get(n-1); 
		return step.getName();
	}
	public int[] getTarget(int n) {
		OneStep step = steps.get(n-1); 
		return step.getTarget();
	}
	public String[] getCommands(int n) {
		OneStep step = steps.get(n-1); 
		return step.getCommands();
	}
	public int[] getOrig(int n) {
		OneStep step = steps.get(n-1); 
		return step.getOrig();
	}
	public int[] getEnd(int n) {
		OneStep step = steps.get(n-1); 
		return step.getEnd();
	}
	public String[] getResult(int n) {
		OneStep step = steps.get(n-1); 
		return step.getResult();
	}
}
