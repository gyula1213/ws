package kocka.forgat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Bűvös kocka kirakásához segédosztály
 * Egy forgatás tulajdonságait tartalmazza
 * 
 * @author slenk
 *
 */
public class Forgat {
	private static List <Forgat> forgatasok = new ArrayList<>();	// Ide állítjuk be azokat a forgatásokat, amivel ki próbáljuk rakni a keresett mintát
	
	private String name;
	private int [] matrix;
	private boolean isActive = true;	// Az adott forgatást alkalmazzuk-e

	public Forgat(String name, int[] matrix) {
		this.name = name;
		this.matrix = matrix;
		check(name, matrix);
	}
	public Forgat(String name, int [] orig, int [] ... commands) {
		this.name = name;
		this.matrix = forgatasok( orig, commands);
	}
	/**
	 * Leellenőrzi, hogy a forgatási mátrix megfelelő-e
	 * @param matrix
	 * @return
	 * @throws Exception 
	 */
	private static boolean check( String name, int [] matrix ) {
		int [] chk = new int[matrix.length];
		
		for ( int i=0; i<matrix.length; i++ ) {
			if ( chk[matrix[i]] == 1) {
				int x = matrix[i];
				System.out.println( name + " forgatás hibás, többször van benne " + x );
			}
			chk[matrix[i]] = 1;
		}
		return true;
	}
	/**
	 * Üresre állítja a forgatási gyűjteményt
	 */
	public static void init() {
		forgatasok = new ArrayList<>();
	}
	/**
	 * Hozzáad egy új forgatást a gyűjteményhez
	 * @param name	: A forgatás neve (későbbi azonosítója)
	 * @param orig	: Az eredeti állapot
	 * @param commands	: forgatási mátrixok egymás utánja
	 */
	public static void addForgatas(String name, int [] orig, int [] ... commands) {
		int [] x = Forgat.forgatasok(orig, commands);
		check( name, x);
		forgatasok.add( new Forgat(name, x));
	}
	/**
	 * Hozzáad egy új forgatást a gyűjteményhez
	 * @param name	: A forgatás neve (későbbi azonosítója)
	 * @param orig	: Az eredeti állapot
	 * @param commands	: forgatási mátrixok egymás utánja
	 */
	public static void addForgatas(String name, int [] orig, String ... commands) {
		int [] x = Forgat.forgatasok(orig, commands);
		check( name, x);
		forgatasok.add( new Forgat(name, x));
	}
    /**
     * Azt a mátrixot adjuk vissza, ami az adott forgatáshoz szükséges, ha a forgatás nem aktív, akkor null-t
     * @param melyik
     * @return
     */
	public static int [] getMatrix(int index) {
		Forgat f = forgatasok.get(index);
		if ( !f.isActive() )
			return null;
		return f.getMatrix();
	}
    /**
     * Azt a mátrixot adjuk vissza, ami az adott forgatáshoz szükséges, ha a forgatás nem aktív, akkor null-t
     * @param a forgatás neve
     * @return
     */
	public static int [] getMatrix(String name) {
		Forgat f = searchByName(name);
		if ( !f.isActive() )
			return null;
		return f.getMatrix();
	}
    /**
     * Visszaadja a forgatás nevét
     * @param melyik
     * @return
     */
	public static String getName(int index) {
		Forgat f = forgatasok.get(index);
		return f.getName();
	}
	public static int getSize() {
		return forgatasok.size();
	}
    /**
     * Maga az adott forgatás elvégzése
     * Az orig tömbben található elemeket
     * "matrix" forgatási mátrix alapján új helyre tesszük
     * Egy új tömb, ahová forgatás után kerülnek az elemek
     * @param orig
     * @param matrix
     * @return
     */
	public static byte [] forgat( byte [] orig, int [] matrix )
	{
        byte [] ret = new byte [orig.length];
        for (int i=0; i<orig.length; i++)	// lemásoljuk
        {
        	ret[i] = orig[matrix[i]];
        }
	    return ret;
	}
	public static int [] forgat( int [] orig, int [] matrix )
	{
        int [] ret = new int [orig.length];
        for (int i=0; i<orig.length; i++)	// lemásoljuk
        {
        	ret[i] = orig[matrix[i]];
        }
	    return ret;
	}
	public static int [] forgatasok(int [] orig, int [] ... commands ) {
		int [] ret = orig;
		for ( int [] act:commands ) {
			ret = forgat( ret, act );
		}
		return ret;
	}
	public static int [] forgatasok(int [] orig, String ... commands ) {
		int [] ret = orig;
		for ( String s:getExtended3(commands) ) {
			int [] act = getMatrix(s);
			ret = forgat( ret, act );
		}
		return ret;
	}
	/**
	 * Az egybetűs forgatási parancsot kiterjesztjük mindhárom forgatásra
	 * @param commands
	 * @return
	 */
	private static String [] getExtended3( String ... commands) {
		List<String> tmp = new ArrayList<>();
		for ( String s:commands ) {
			if ( s.length() == 1 ) {		// Az egy betűs forgatásnevekhez mind a 3 irányt hozzákapcsoljuk
				tmp.add(s+"i");
				tmp.add(s+"u");
				tmp.add(s+"o");
			}
			else
				tmp.add(s);
		}
		String [] ret = new String[tmp.size()];
		int i = 0;
		for ( String s : tmp )
			ret[i++] = s;
		return ret;
	}
	/**
	 * Az összes forgatást aktiválja/inaktiválja
	 * @param flag
	 */
	public static void setAllActive( boolean flag ) {
		for ( Forgat f:forgatasok ) {
			f.setActive(flag);
		}
	}
	/**
	 * Az adott nevű forgatásokat aktiválja/inaktiválja
	 * @param flag
	 */
	public static void setActive(boolean flag, String ... names ) {
		for ( String name:getExtended3(names) ) {
			Forgat f = searchByName(name);
			f.setActive(flag);
		}
	}
	/**
	 * Véletlenszerűen visszaad egy forgatást a felvett forgatások közül.
	 * @return
	 */
	public static Forgat getRandom()  {
		int n = forgatasok.size();
		int i = (int)(n*Math.random());
		return forgatasok.get(i);
	}
	/**
	 * Véletlenszerűen használ egyet a megadott forgatások közül
	 * @return
	 */
	public static int [] random( int [] orig)  {
		Forgat forg = getRandom();
		return forgat( orig, forg.getMatrix());
	}
	/**
	 * Név alapján visszaad egy forgatást
	 * @param name
	 * @return
	 */
	static Forgat searchByName( String name ) {
		for ( Forgat f:forgatasok ) {
			if ( name.equals(f.getName() ))
				return f;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	public int[] getMatrix() {
		return matrix;
	}
	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	@Override
	public String toString() {
		return "Forgat [name=" + name + ", matrix=" + Arrays.toString(matrix) + ", isActive=" + isActive + "]";
	}
	
}
