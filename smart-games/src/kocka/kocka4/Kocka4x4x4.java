package kocka.kocka4;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import base.GameController;
import base.Gameable;
import kocka.Kocka;
import kocka.OneStep;
import kocka.forgat.Forgat;

//
/**
 * 2x2-es kocka kirakása 
 * @author slenk
 *
 */
public class Kocka4x4x4 extends Kocka
{
    private static String [] colors = {"x", "F", "P", "K", "N", "Z", "S", "R"};
    private static int cntCol = 16;
    private static int _REF_ELEMENT = 7;	// A fekete oldal V betűje
    
    private static boolean all = false;
    private static boolean lotus = false;
    private static boolean csere = false;
    private static boolean helyben = false;
    private static boolean ruBuTuli = false;
    private static boolean centrum = true;
	
    protected Kocka4x4x4( int [] stage, int prevStep )
    {
    	super( stage, prevStep );
    }
    public Kocka4x4x4( int [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Kocka4x4x4( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( int [] stage, int prevStep )
	{
    	return new Kocka4x4x4( stage, prevStep );
	}
    @Override
    public String [] getColors()
    {
    	return colors;
    }
    @Override
    public int getCntCol()
    {
    	return cntCol;
    }
	/**
	 * A pillanatnyi célnak megfelelő állapotot adja vissza
	 * @return
	 */
    @Override
	public int [] getTarget()
    {
    	return target;
    	//return sarok;
    	//return teteje_sarok;
    	//return teteje;
    	//return pill;
    	//return centrums;
    	//return ready;
    }
    /**
     * Visszadja az original tömböt (hogy hogyan számoztuk meg az eredeti elemeket
     */
	@Override
	public int[] getOriginal() {
		return origin;
	}
    
    /**
     * Beállítjuk a lehetséges forgatásokat
     * Minden forgatás olyan (legyen), hogya a ref pozíciót (felső oldalon 3;3 pozícióban a "V" betű) a helyén tartja
     * Ebben az esetben nincs szükség a getNorma() függvényre
     */
    @Override
	public void initForgatasok()
	{
		Forgat.init();
		int [] f = egesz_bal;	// fordítás
		int [] d = jobbra_dont;	// döntés
		int [] t = Forgat.forgatasok(origin, jobb);		// jobb szélső oldal egy fordítása jobbra
		int [] t2 = Forgat.forgatasok(origin, jobb_reteg);		// jobb szélső réteg (2. réteg) egy fordítása jobbra

		//		t2 = Forgat.forgatasok(origin, jobb, jobb_reteg, f, d, d, d, f, f, f); Ez helyben tartja ref elemet, de inkább ez nem kell
		
//		Forgat.addForgatas("egesz_bal", origin, egesz_bal);	// Csak teszt
//		Forgat.addForgatas("jobbra_dont", origin, jobbra_dont);	// Csak teszt

		if ( all )
		{
			addAllSimpleForgatas();
		}
		else if ( lotus )
		{
			Forgat.addForgatas("lo", origin, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f);
			Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
			Forgat.addForgatas("Fu", origin, f, f, f, jobb, jobb, f);
			Forgat.addForgatas("ri", origin, jobb_reteg);
			Forgat.addForgatas("ro", origin, jobb_reteg, jobb_reteg, jobb_reteg);
		}
		else if ( csere )
		{
			Forgat.addForgatas("ru", origin, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
			Forgat.addForgatas("tu", origin, d, jobb_reteg, jobb_reteg, d, d, d);
		}
		else if ( helyben )
		{
			Forgat.addForgatas("ru", origin, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("Bu", origin, f, jobb, jobb, f, f, f);
			Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
			Forgat.addForgatas("li", origin, f, f, jobb_reteg, f, f);
			Forgat.addForgatas("lo", origin, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f);
			Forgat.addForgatas("ri", origin, jobb_reteg);
			Forgat.addForgatas("ro", origin, jobb_reteg, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("Fu", origin, f, f, f, jobb, jobb, f);
		}
		if ( ruBuTuli )
		{
			Forgat.addForgatas("ru", origin, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("Bu", origin, f, jobb, jobb, f, f, f);
			Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
			Forgat.addForgatas("li", origin, f, f, jobb_reteg, f, f);
			Forgat.addForgatas("lo", origin, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f);
			Forgat.addForgatas("ri", origin, jobb_reteg);
			Forgat.addForgatas("ro", origin, jobb_reteg, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("Fu", origin, f, f, f, jobb, jobb, f);
			Forgat.addForgatas("ru-Bu", origin, "ru", "Bu");
			Forgat.addForgatas("Bu-ru", origin, "Bu", "ru");
			Forgat.addForgatas("Tu-li", origin, "Tu", "li");
			Forgat.addForgatas("Tu-ri", origin, "Tu", "ri");
			Forgat.addForgatas("Tu-ro", origin, "Tu", "ro");
			Forgat.addForgatas("Fu-ri", origin, "Fu", "ri");
			Forgat.addForgatas("Fu-lo", origin, "Fu", "lo");
			Forgat.setActive(false,"ru", "Bu", "Fu", "li", "lo", "ri", "ro");
		}
		else if ( centrum )
		{
			Forgat.addForgatas("Ti", origin, d, jobb, d, d, d);
			Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
			Forgat.addForgatas("To", origin, d, jobb, jobb, jobb, d, d, d);
			Forgat.addForgatas("li", origin, f, f, jobb_reteg, f, f);
			Forgat.addForgatas("lu", origin, f, f, jobb_reteg, jobb_reteg, f, f);
			Forgat.addForgatas("lo", origin, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f);
			Forgat.addForgatas("ri", origin, jobb_reteg);
			Forgat.addForgatas("ru", origin, jobb_reteg, jobb_reteg);
			Forgat.addForgatas("ro", origin, jobb_reteg, jobb_reteg, jobb_reteg);
		}

//		Forgat.addForgatas("jobb2", origin, t2 );
//		Forgat.addForgatas("felul2", origin, d, t2, d, d, d );
//		Forgat.addForgatas("elol2", origin, f, f, f, t2, f  );
//		Forgat.addForgatas("jobb", origin, t );
//		Forgat.addForgatas("felul", origin, d, t, d, d, d, f, f, f );
//		Forgat.addForgatas("elol", origin, f, f, f, t, f  );
//		Forgat.addForgatas("bal", origin, f, f, t, f, f );
//		Forgat.addForgatas("alul", origin, d, d, d, t, d );
//		Forgat.addForgatas("hatul", origin, f, t, f, f, f );
//		Forgat.addForgatas("jobb(2)", origin, t, t );
//		Forgat.addForgatas("felul(2)", origin, d, t, t, d, d, d, f, f );
//		Forgat.addForgatas("elol(2)", origin, f, f, f, t, t, f  );
//		Forgat.addForgatas("bal(2)", origin, f, f, t, t, f, f );
//		Forgat.addForgatas("alul(2)", origin, d, d, d, t, t, d );
//		Forgat.addForgatas("hatul(2)", origin, f, t, t, f, f, f );
//		Forgat.addForgatas("jobb_reteg", origin, jobb_reteg );
//		Forgat.addForgatas("felul_reteg", origin, d, jobb_reteg, d, d, d);
//		Forgat.addForgatas("elol_reteg", origin, f, jobb_reteg, f, f, f);
//		Forgat.addForgatas("bal_reteg", origin, f, f, jobb_reteg, f, f );
//		Forgat.addForgatas("alul_reteg", origin, d, d, d, jobb_reteg, d );
//		Forgat.addForgatas("hatul_reteg", origin, f, jobb_reteg, f, f, f );
		
//		int [] jobb = Forgat.forgatasok( origin, t );
//		int [] felul = Forgat.forgatasok( origin, d, t, d, d, d );
//		int [] elol = Forgat.forgatasok( origin, f, f, f, t, f  );
	}
    /**
     * Az összes alapforgatást felveszi
     */
    @Override
    public void addAllSimpleForgatas()
    {
		Forgat.init();
		int [] f = egesz_bal;	// fordítás
		int [] d = jobbra_dont;	// döntés

		// Tető
		Forgat.addForgatas("Ti", origin, d, jobb, d, d, d);
		Forgat.addForgatas("Tu", origin, d, jobb, jobb, d, d, d);
		Forgat.addForgatas("To", origin, d, jobb, jobb, jobb, d, d, d);
		Forgat.addForgatas("ti", origin, d, jobb_reteg, d, d, d);
		Forgat.addForgatas("tu", origin, d, jobb_reteg, jobb_reteg, d, d, d);
		Forgat.addForgatas("to", origin, d, jobb_reteg, jobb_reteg, jobb_reteg, d, d, d);

			// Elöl
		Forgat.addForgatas("Fi", origin, f, f, f, jobb, f);
		Forgat.addForgatas("Fu", origin, f, f, f, jobb, jobb, f);
		Forgat.addForgatas("Fo", origin, f, f, f, jobb, jobb, jobb, f);
		Forgat.addForgatas("fi", origin, f, f, f, jobb_reteg, f);
		Forgat.addForgatas("fu", origin, f, f, f, jobb_reteg, jobb_reteg, f);
		Forgat.addForgatas("fo", origin, f, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f);
	
			// Jobb oldal
		Forgat.addForgatas("Ri", origin, jobb);
		Forgat.addForgatas("Ru", origin, jobb, jobb);
		Forgat.addForgatas("Ro", origin, jobb, jobb, jobb );
		Forgat.addForgatas("ri", origin, jobb_reteg);
		Forgat.addForgatas("ru", origin, jobb_reteg, jobb_reteg);
		Forgat.addForgatas("ro", origin, jobb_reteg, jobb_reteg, jobb_reteg);

			// Hátul
		Forgat.addForgatas("Bi", origin, f, jobb, f, f, f);
		Forgat.addForgatas("Bu", origin, f, jobb, jobb, f, f, f);
		Forgat.addForgatas("Bo", origin, f, jobb, jobb, jobb, f, f, f);
		Forgat.addForgatas("bi", origin, f, jobb_reteg, f, f, f);
		Forgat.addForgatas("bu", origin, f, jobb_reteg, jobb_reteg, f, f, f);
		Forgat.addForgatas("bo", origin, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f, f);
		
			// Bal
		Forgat.addForgatas("Li", origin, f, f, jobb, f, f);
		Forgat.addForgatas("Lu", origin, f, f, jobb, jobb, f, f);
		Forgat.addForgatas("Lo", origin, f, f, jobb, jobb, jobb, f, f);
		Forgat.addForgatas("li", origin, f, f, jobb_reteg, f, f);
		Forgat.addForgatas("lu", origin, f, f, jobb_reteg, jobb_reteg, f, f);
		Forgat.addForgatas("lo", origin, f, f, jobb_reteg, jobb_reteg, jobb_reteg, f, f);

			// Alul
		Forgat.addForgatas("Di", origin, d, d, d, jobb, d);
		Forgat.addForgatas("Du", origin, d, d, d, jobb, jobb, d);
		Forgat.addForgatas("Do", origin, d, d, d, jobb, jobb, jobb, d);
		Forgat.addForgatas("di", origin, d, d, d, jobb_reteg, d);
		Forgat.addForgatas("du", origin, d, d, d, jobb_reteg, jobb_reteg, d);
		Forgat.addForgatas("do", origin, d, d, d, jobb_reteg, jobb_reteg, jobb_reteg, d);
    }
    /*
     * Az első 16 elem a felső lap
     * a második 16, az első
     * harmadik 16 a jobb oldali
     * negyedik 16 a hátsó
     * ötödik 16 a bal
     * hatodik 16 az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * Lapon belül az elrendezés a következő:
     *  0  1  2  3
     *  4  5  6  7
     *  8  9 10 11
     * 12 13 14 15
     */
    // Az eredeti elrendezés:
	private static int [] origin = {
			 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,		// felső 
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,   	// első
			32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,		// jobb 
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,		// hátsó 
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 	// bal
			80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95	 	// alsó
	};
	/**
	 * Az alap forgatások:
	 * jobb: A jobb szélső oldal fordítása az óramutató járásával egyező irányban
	 * jobb_reteg: A jobb szélről a 2. réteg  fordítása az óramutató járásával egyező irányban
	 * egesz_bal: Az egész kockát egyben balra fordítjuk egyet
	 *            A felső lap a helyén marad.
	 *            Az első lap a bal oldalra kerül
	 * jobbra_dont: Az egész kockát egyben jobbra döntjük
	 *              Az első lap a helyén marad.
	 *              A felső lap a jobb oldalra kerül
	 */
	private static int [] jobb = {
			 0,  1,  2, 19,  4,  5,  6, 23,  8,  9, 10, 27, 12, 13, 14, 31,		// felső 
			16, 17, 18, 83, 20, 21, 22, 87, 24, 25, 26, 91, 28, 29, 30, 95,   	// első
			44, 40, 36, 32, 45, 41, 37, 33, 46, 42, 38, 34, 47, 43, 39, 35,		// jobb 
			15, 49, 50, 51, 11, 53, 54, 55,  7, 57, 58, 59,  3, 61, 62, 63,		// hátsó 
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 	// bal
			80, 81, 82, 60, 84, 85, 86, 56, 88, 89, 90, 52, 92, 93, 94, 48	 	// alsó
	};
	private static int [] jobb_reteg = {
			 0,  1, 18,  3,  4,  5, 22,  7,  8,  9, 26, 11, 12, 13, 30, 15,		// felső 
			16, 17, 82, 19, 20, 21, 86, 23, 24, 25, 90, 27, 28, 29, 94, 31,   	// első
			32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,		// jobb 
			48, 14, 50, 51, 52, 10, 54, 55, 56,  6, 58, 59, 60,  2, 62, 63,		// hátsó 
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 	// bal
			80, 81, 61, 83, 84, 85, 57, 87, 88, 89, 53, 91, 92, 93, 49, 95	 	// alsó
	};
	private static int [] egesz_bal = {
			12,  8,  4,  0, 13,  9,  5,  1, 14, 10,  6,  2, 15, 11,  7,  3,		// felső 
			32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,		// jobb --> első 
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,		// hátsó --> jobb
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 	// bal --> hátsó
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,   	// első --> bal
			83, 87, 91, 95, 82, 86, 90, 94, 81, 85, 89, 93, 80, 84, 88, 92	 	// alsó
	};
	private static int [] jobbra_dont = {
			76, 72, 68, 64, 77, 73, 69, 65, 78, 74, 70, 66, 79, 75, 71, 67,		// bal --> felső
			28, 24, 20, 16, 29, 25, 21, 17, 30, 26, 22, 18, 31, 27, 23, 19,  	// első
			12,  8,  4,  0, 13,  9,  5,  1, 14, 10,  6,  2, 15, 11,  7,  3,		// felső --> jobb
			51,	55, 59, 63, 50, 54, 58, 62, 49, 53, 57, 61, 48, 52, 56, 60, 	// hátsó
			92, 88, 84, 80, 93, 89, 85, 81, 94, 90, 86, 82, 95, 91, 87, 83,		// alsó --> bal
			44, 40, 36, 32, 45, 41, 37, 33, 46, 42, 38, 34, 47, 43, 39, 35		// jobb --> alul
	};
    private static int [] pill_x = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
			2, 2, 2, 2, 5, 2, 5, 6, 3, 6, 6, 3, 4, 2, 3, 2, // első: piros
			3, 3, 3, 3, 3, 4, 2, 5, 2, 3, 5, 6, 3, 4, 2, 3, // jobb kék
			4, 4, 4, 4, 4, 6, 5, 4, 2, 4, 4, 6, 4, 2, 4, 2, // hátsó narancs
			5, 5, 5, 5, 3, 2, 3, 6, 5, 3, 4, 2, 6, 6, 3, 6, // bal: zöld
			5, 6, 6, 6, 4, 3, 2, 6, 4, 5, 6, 5, 5, 5, 5, 6, // alsó: sárga
	};
    private static int [] pill_centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 2, 6, 0, 0, 5, 6, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 6, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 2, 5, 0, 0, 5, 4, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 2, 5, 0, 0, 4, 6, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 2, 4, 0, 0, 3, 4, 0, 0, 0, 0, 0, // alsó: sárga
	};

    
    
    private static int [] teszt_pill = {
    		3, 4, 2, 4,  1, 4, 3, 1,  5, 4, 7, 2,  2, 4, 5, 6, // felső: fekete
    		1, 6, 4, 3,  1, 3, 1, 4,  1, 4, 2, 5,  1, 5, 6, 6, // első: piros
    		2, 1, 4, 6,  3, 3, 3, 3,  1, 2, 6, 4,  5, 5, 5, 5, // jobb kék
    		5, 6, 1, 6,  2, 6, 5, 2,  3, 6, 2, 1,  1, 6, 3, 4, // hátsó narancs
			4, 5, 6, 3,  6, 5, 4, 3,  2, 5, 1, 3,  1, 5, 2, 3, // bal: zöld
			4, 4, 3, 2,  3, 1, 6, 2,  6, 2, 5, 2,  5, 6, 4, 2, // alsó: sárga
	};
    private static int [] pill = {
    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 7, 1,  1, 2, 2, 1, // felső: fekete
			2, 1, 1, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
	};
//    private static int [] target = {
//    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1, // felső: fekete
//			2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
//			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
//			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
//			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
//			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
//	};
//    private static int [] pill = {
//			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 7, 0, 0, 0, 0, 0, // felső: fekete
//			0, 0, 0, 0, 0, 4, 6, 0, 0, 2, 6, 0, 0, 0, 0, 0, // első: piros
//			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, // jobb kék
//			0, 0, 0, 0, 0, 4, 2, 0, 0, 4, 2, 0, 0, 0, 0, 0, // hátsó narancs
//			0, 0, 0, 0, 0, 5, 5, 0, 0, 5, 5, 0, 0, 0, 0, 0, // bal: zöld
//			0, 0, 0, 0, 0, 6, 4, 0, 0, 6, 2, 0, 0, 0, 0, 0, // alsó: sárga
//	};
//    private static int [] target = {
//			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
//			0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, // első: piros
//			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, // jobb kék
//			0, 0, 0, 0, 0, 4, 4, 0, 0, 4, 4, 0, 0, 0, 0, 0, // hátsó narancs
//			0, 0, 0, 0, 0, 5, 5, 0, 0, 5, 5, 0, 0, 0, 0, 0, // bal: zöld
//			0, 0, 0, 0, 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, // alsó: sárga
//	};
    
    
    
    
    
    
    
    
    private static int [] teteje = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 4, 4, 0, 0, 4, 4, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 5, 5, 0, 0, 5, 5, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] sarok = {
    		1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, // felső: fekete
    		2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, // első: piros
			3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, // jobb kék
			4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, // hátsó narancs
			5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, // bal: zöld
			6, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 6, // alsó: sárga
	};
    private static int [] kozepe_sarok = {
    		1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, // felső: fekete
    		2, 0, 0, 2, 0, 2, 2, 0, 0, 2, 1, 0, 2, 0, 0, 2, // első: piros
			3, 0, 0, 3, 0, 3, 3, 0, 0, 3, 3, 0, 3, 0, 0, 3, // jobb kék
			4, 0, 0, 4, 0, 4, 4, 0, 0, 4, 4, 0, 4, 0, 0, 4, // hátsó narancs
			5, 0, 0, 5, 0, 5, 5, 0, 0, 5, 5, 0, 5, 0, 0, 5, // bal: zöld
			6, 0, 0, 6, 0, 6, 6, 0, 0, 6, 6, 0, 6, 0, 0, 6, // alsó: sárga
	};
    private static int [] teteje_sarok = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] teteje_kozepe = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] ready = {
    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1, // felső: fekete
			2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
	};
	private static int [][] kockak = 
    {
        ready, centrums, pill
    };
	private static int [][] targets = 
    {
        ready, centrums
    };
	// A névben jelzett oldalt a tetőre mozgatja
	//private static int [] felul_side = Forgat.forgatasok( origin );
	private static int [] felul_side = origin;
	private static int [] elol_side = Forgat.forgatasok( origin, egesz_bal, jobbra_dont );
	private static int [] jobb_side = Forgat.forgatasok( origin, jobbra_dont, jobbra_dont, jobbra_dont );
	private static int [] hatul_side = Forgat.forgatasok( origin, egesz_bal, jobbra_dont, jobbra_dont, jobbra_dont );
	private static int [] bal_side = Forgat.forgatasok( origin, jobbra_dont );
	private static int [] alul_side = Forgat.forgatasok( origin, jobbra_dont, jobbra_dont );
	private static int [][] sides = { felul_side,  elol_side, jobb_side, hatul_side, bal_side, alul_side };
	/**
     * Normalizál egy állást, hogy az összehasonlítás megoldható legyen
     * Ez a konkrét esetekben azt jelenti, hogy az összehasonlításnál van egy fix pont.
     * A 4x4x4-es kockán a fix pont a "V" betűvel jelölt fekete mező. 
     * Az a normalizált állapotban, a teteőn, a (3,3) pozíción található
     */
	@Override
	protected int [] getNorma(int [] act )
	{
		int place = searchRefElement(act);
		int side = place/cntCol;	// melyik oldal
			// A felső oldalra forgatjuk
		int [] top = Forgat.forgat(act, sides[side]);
		
			// Megnézzük hányat kell fordítani, hogy a helyére kerüljön
		int posNum = getPosNum(top);
		int [] ret = top;
		for ( int i=1; i<=posNum; i++ )
			ret = Forgat.forgat(ret, egesz_bal);
		return ret;
	}
	/**
	 * Visszadja a referenciaelem helyét a tömbben
	 * @param t
	 * @return
	 */
	private int searchRefElement(int [] t)
	{
		for ( int i=0; i<t.length; i++ )
		{
			if ( t[i] == _REF_ELEMENT )
				return i;
		}
		return (-1);
	}
	/**
	 * Visszadja hogy hányat kell a kockán fordítani, hogy a tetőn lévő referenciaelem a helyére kerüljön
	 * @param t
	 * @return
	 */
	private int getPosNum(int [] t)
	{
		int i;
		for ( i=0; i<t.length; i++ )
		{
			if ( t[i] == _REF_ELEMENT )
				break;
		}
		switch (i)
		{
		case 10 : return 0;
		case 9 : return 3;
		case 5 : return 2;
		case 6 : return 1;
		default: return -1;
		}
	}
	/**
	 * A cél függvény.
	 * Visszatérés true, ha elégedettek vagyunk a pillanatnyi állapottal
	 * getTarget() fv mondja meg, hogy mi az aktuális cél
	 * Ha a "target" pozíción 0 van, ott bármi lehet
	 * 
	 * Ha nem konkrét eredményt várunk, akkor felülírható (pl. Domino)
	 */
	@Override
	public boolean isReady()
    {
    	int [] target = getTarget();
        for ( int i=0; i<actual.length; i++ )
        {
            if ( target[i] == 0 )    // itt mindegy mi van
            {
                continue;
            }
            int act = actual[i];
            if ( act == _REF_ELEMENT)
            	act = 1;
            if ( act != target[i] )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;	//Minden OK
    }
	@Override
	public void initGame() {
		createSteps();
		OneStep.initSolve(teszt_pill);
	}
	
	@Override
    public String toString( int [] tabla )
    {
        String s="\n";
        String [] colors = getColors();
        int cntCol = getCntCol();
        for ( int i=0; i<4; i++ )	// 4 sor
        {
            for ( int j=0; j<6; j++ )	// 6 lap
            {
                for ( int k=0; k<4; k++ )	// 4 oszlop
                {
                    String color = colors[tabla[16*j+4*i+k]];
                    s += color +"";
                }
                s+="   ";
            }    
            s+="\n";
        }
        return s;
    }
	
    /**
     * Visszaadja azt a célt, amit úgy képezünk, hogy "stabil" megadott elemei nem változhatnak, "act" bizonyos elemei klónozódnak,
     * a többi elem mindegy 
     */
	private static int [] getNewTarget( int [] stabil, int [] act )
	{
		int [] ret = new int [act.length];
		for ( int i=0; i<ret.length; i++ )
		{
			ret[i] = stabil[i];
		}
		ret[20] = act[40];
		ret[71] = act[27];
		//ret[]
		return ret;
	}
	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        //solveOld();
        //solve();
        solveTeszt();
        System.out.println("Vége");
        System.exit(0);
    }
    public static void createSteps()
    {
        OneStep4x4x4 step;
        
        OneStep.init();
        step = new OneStep4x4x4( "top_centrums", top_centrums, "R", "t", "d", "l", "b" );
        step = new OneStep4x4x4( "top_bottom_centrums", top_bottom_centrums, "D", "R", "t", "d", "l", "b" );
        step = new OneStep4x4x4( "black_red_edge", black_red_edge, "F", "R", "B", "L", "t", "d");
        step = new OneStep4x4x4( "black_blue_edge", black_blue_edge, "F", "R", "B", "L", "t", "d");
        step = new OneStep4x4x4( "black_green_edge", black_green_edge, "F", "R", "B", "L", "t", "d");
        step = new OneStep4x4x4( "red_centrum", red_centrum, "B", "t", "d");
        step = new OneStep4x4x4( "red_blue_centrum", red_blue_centrum, "B", "t", "d");
        step = new OneStep4x4x4( "all_centrum", all_centrum, "B", "t", "d");
        //step = new OneStep4x4x4( "black_orange_edge_1", black_orange_edge_1, "B", "R", "D");
        //step = new OneStep4x4x4( "black_orange_edge", black_orange_edge, "do", "di", "Ru", "Do", "B");	// Ez jó, ebben az esetben (teszt_pill)
        step = new OneStep4x4x4( "black_orange_edge", black_orange_edge, "do", "di", "to", "ti", "R", "B", "D");
    }
    public static void solveTeszt()
    {
        createSteps();
        System.out.println("--- print ----------------------------------------");
        OneStep.print();
        OneStep.initSolve(teszt_pill);

        OneStep.solve(1);
        System.out.println("--- solve1 ----------------------------------------");
        OneStep.print();

        OneStep.solve(4);
        System.out.println("--- solve4 ----------------------------------------");
        OneStep.print();
    }
    public static void solve()
    {
        int [] act = teszt_pill;
        //act = oneStep( act, ready, "Lo", "Lo-lo", "Ro", "Bo", "Bo-bo", "Fo" );
        
        createSteps();
        System.out.println("--- print ----------------------------------------");
        OneStep.print();
        System.out.println("--- solve ----------------------------------------");
        OneStep.allSolve(teszt_pill);
        System.out.println("--- print2 ----------------------------------------");
        OneStep.print();
        System.out.println("--- steps ----------------------------------------");
        OneStep.printResult();
    }
    public static void solveOld()
    {
        int [] act = teszt_pill;
        //act = oneStep( act, ready, "Lo", "Lo-lo", "Ro", "Bo", "Bo-bo", "Fo" );
        
        Kocka4x4x4 master = new Kocka4x4x4( act );
        
        System.out.println("top_centrums");
        String [] res = oneStep( act, top_centrums, "Ro", "Ri", "Ru", "to", "ti", "tu", "do", "di", "du", "ro", "ri", "ru", "lo", "li", "lu", "fo", "fi", "fu", "bo", "bi", "bu" );
        act = Forgat.forgatasok(act, res);
        System.out.println("top_centrums:\n" + master.toString(act));
        
        act = master.getNorma(act);
        master = new Kocka4x4x4( act );
        System.out.println("Top centrum, norma:\n" + master.toString(act));
        
        System.out.println("top_bottom_centrums");
        res = oneStep( act, top_bottom_centrums, "Do", "Di", "Du", "Ro", "Ri", "Ru", "to", "ti", "tu", "do", "di", "du", "ro", "ri", "ru", "lo", "li", "lu", "fo", "fi", "fu", "bo", "bi", "bu" );
        act = Forgat.forgatasok(act, res);
        System.out.println("top_bottom_centrums:\n" + master.toString(act));
        
        System.out.println("black_red_edge");
        res = oneStep( act, black_red_edge, "Fo", "Fi", "Fu", "To", "Ti", "Tu", "Do", "Di", "Du", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("black_red_edge:\n" + master.toString(act));
        
        System.out.println("black_blu_edge");
        res = oneStep( act, black_blue_edge, "Ro", "Ri", "Ru", "To", "Ti", "Tu", "Do", "Di", "Du", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("black_blu_edge:\n" + master.toString(act));
        
        System.out.println("black_green_edge");
        res = oneStep( act, black_green_edge, "Lo", "Li", "Lu", "To", "Ti", "Tu", "Do", "Di", "Du", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("black_green_edge:\n" + master.toString(act));
        
        System.out.println("red_centrum");
        res = oneStep( act, red_centrum, "Bo", "Bi", "Bu", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("red_centrum:\n" + master.toString(act));
        
        System.out.println("red_blue_centrum");
        res = oneStep( act, red_blue_centrum, "Bo", "Bi", "Bu", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("red_blue_centrum:\n" + master.toString(act));
        
        System.out.println("all_centrum");
        res = oneStep( act, all_centrum, "Bo", "Bi", "Bu", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("all_centrum:\n" + master.toString(act));
        
        System.out.println("black_orange_edge");
        res = oneStep( act, black_orange_edge, "Bo", "Bi", "Bu", "Do", "Di", "Du", "Ro", "Ri", "Ru", "to", "ti", "tu", "do", "di", "du");
        act = Forgat.forgatasok(act, res);
        System.out.println("black_orange_edge:\n" + master.toString(act));
        
		Forgat.addForgatas("U2", origin, jobbra_dont, jobbra_dont, jobbra_dont, jobb_reteg, jobbra_dont, jobbra_dont, jobbra_dont);
        act = Forgat.forgatasok(act, "U2");
        master = new Kocka4x4x4( act );
        System.out.println("U2:\n" + master.toString(act));
        int [] stabil = Forgat.forgatasok(black_orange_edge, "U2");
        int [] target = getNewTarget( stabil, act );
        System.out.println("first_edge");
        System.out.println("target: " + master.toString(target));
        res = oneStep( act, target, "Fo", "Fi", "Lo", "Li", "To", "Ti", "Tu", "Bo", "Bi");
        act = Forgat.forgatasok(act, res);
        System.out.println("first_edge:\n" + master.toString(act));
        //act = oneStep( act, top_centrums, "Ro", "Ri", "Ru", "to", "ti", "tu", "do", "di", "du", "ro", "ri", "ru", "lo", "li", "lu", "fo", "fi", "fu", "bo", "bi", "bu" );
    }
    public static String [] oneStep(int [] act, int [] target, String... commands)
    {
        System.out.println("One step...");
        Kocka4x4x4 game = new Kocka4x4x4( act );
        game.setTarget(target);
        game.init( commands );

//        System.out.println("X0" + game);
//        Vector<Gameable>kockak = game.nextStages();
//        for ( Gameable g:kockak)
//            System.out.println(g);
//
        GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        return gc.getResult();
    }
    private static int [] top_centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] top_bottom_centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] black_red_edge = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static int [] black_blue_edge = {
			0, 0, 0, 0,  0, 1, 1, 1,  0, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
    private static int [] black_green_edge = {
			0, 0, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
    private static int [] red_centrum = {
			0, 0, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 2, 2, 0,  0, 2, 2, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
    private static int [] red_blue_centrum = {
			0, 0, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 2, 2, 0,  0, 2, 2, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 3, 3, 0,  0, 3, 3, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 0, 0, 0,  0, 0, 0, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
    private static int [] all_centrum = {
			0, 0, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 2, 2, 0,  0, 2, 2, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 3, 3, 0,  0, 3, 3, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  0, 4, 4, 0,  0, 4, 4, 0,  0, 0, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 5, 5, 0,  0, 5, 5, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
    private static int [] black_orange_edge_1 = {
			0, 0, 0, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 2, 2, 0,  0, 2, 2, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 3, 3, 4,  0, 3, 3, 0,  0, 0, 0, 0, // jobb kék
			0, 0, 0, 0,  1, 4, 4, 0,  0, 4, 4, 0,  0, 4, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 5, 5, 0,  0, 5, 5, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 1, 0, // alsó: sárga
	};
    private static int [] black_orange_edge = {
			0, 1, 1, 0,  1, 1, 1, 1,  1, 1, 1, 1,  0, 1, 1, 0, // felső: fekete
			0, 2, 2, 0,  0, 2, 2, 0,  0, 2, 2, 0,  0, 0, 0, 0, // első: piros
			0, 3, 3, 0,  0, 3, 3, 0,  0, 3, 3, 0,  0, 0, 0, 0, // jobb kék
			0, 4, 4, 0,  0, 4, 4, 0,  0, 4, 4, 0,  0, 0, 0, 0, // hátsó narancs
			0, 5, 5, 0,  0, 5, 5, 0,  0, 5, 5, 0,  0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0,  0, 6, 6, 0,  0, 6, 6, 0,  0, 0, 0, 0, // alsó: sárga
	};
	
}