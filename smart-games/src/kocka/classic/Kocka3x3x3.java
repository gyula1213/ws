package kocka.classic;

import base.GameController;
import kocka.Kocka;
import kocka.forgat.Forgat;

/**
 * Klasszikus 3x3-as bűvös kocka tekerései
 * 
 * TODO Olyan speciális kocka, ahol az is számít, hogy a lap közepén álló elem pontosan hogy áll (pl kép van a kockán)
 * 
 * @author slenk
 *
 */
public class Kocka3x3x3 extends Kocka
{
	static int cntCol = 9;
    private static String [] colors = {"x", "F", "Z", "P", "K", "N", "S"};
    private Kocka3x3x3( int [] stage, int prevStep )
    {
    	super( stage, prevStep );
    }
    public Kocka3x3x3( int [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Kocka3x3x3( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( int [] stage, int prevStep )
	{
    	return new Kocka3x3x3( stage, prevStep );
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
    	return ready;
    }
    /**
     * Elemi forgatások:
     * 	-- Tekerés(t)  : A jobb szélső lap fordítása az óramutató járásával megegyező irányba
     *  -- Fordítás(f) : Az egész koka elfordítása a z tengely körül az óramutató járásával megegyező irányba (az eleje a bal oldalra kerül)
     *  -- Döntés(d)   : A kocka eldöntése jobbra vagy más szóval az elfordítása az y tengely körül az óramutató járásával megegyező irányba
     *  --               a felsó lap a jobb oldalra kerül
     */
    
    /*
     * Az első 9 elem a felső lap
     * a második 9, az első
     * harmadik 9 a jobb oldali
     * negyedik 9 a hátsó
     * ötödik 9 a bal
     * hatodik 9 az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * Lapon belül az elrendezés a következő:
     * 012
     * 345
     * 678
     */
    // Az eredeti elrendezés:
	private static int [] origin = {
			 0,  1,  2,  3,	 4,  5,  6,  7,  8,	// felső
			 9, 10, 11, 12, 13,	14, 15, 16, 17, // első
			18, 19, 20, 21, 22, 23,	24, 25, 26, // jobb
			27, 28, 29, 30, 31, 32, 33,	34, 35, // hátsó
			36, 37, 38, 39, 40, 41, 42, 43, 44, // bal
			45, 46, 47, 48, 49, 50, 51, 52, 53  // alsó
	};
	// A lapközepek eredeti álláshoz képesti helyzetét külön tömbben tartjuk nyilvan
	private static int [] origin_center = { 0, 0, 0, 0, 0, 0 };	// Mindegyik a helyén áll
    // Tekerés(t)
	private static int [] tekeres = {
			 0,  1, 11,  3,	 4, 14,  6,	 7, 17, // felső
			 9, 10, 47, 12, 13,	50, 15, 16, 53, // első
			24, 21, 18, 25, 22, 19,	26, 23, 20, // jobb
			 8, 28, 29,  5, 31, 32,  2,	34, 35, // hátsó
			36, 37, 38, 39, 40, 41, 42, 43, 44, // bal
			45, 46, 33, 48, 49, 30, 51, 52, 27  // alsó
	};
	private static int [] tekeres_center = { 0, 0, 1, 0, 0, 0 };	// a jobb oldali lap negfordul
    // Fordítás(f)
	private static int [] forditas = {
			 6,  3,  0,  7,	 4,  1,  8,  5,  2,	// felső
			18, 19, 20, 21, 22, 23,	24, 25, 26, // első
			27, 28, 29, 30, 31, 32, 33,	34, 35, // jobb
			36, 37, 38, 39, 40, 41, 42, 43, 44, // hátsó
			 9, 10, 11, 12, 13,	14, 15, 16, 17, // bal
			47, 50, 53, 46, 49, 52, 45, 48, 51  // alsó
	};
	private static int [] forditas_center = { 1, 0, 0, 3, 0, 0 };	// a felső lap jobbra, az alsó balra fordul
    // Döntés(d)
	private static int [] dontes = {
			42, 39, 36, 43, 40, 37, 44, 41, 38, // felső
			15, 12,  9, 16, 13,	10, 17, 14, 11, // első
			 6,  3,  0,  7,	 4,  1,  8,  5,  2,	// jobb
			29, 32, 35, 28, 31, 34, 27,	30, 33, // hátsó
			51, 48, 45, 52, 49, 46, 53, 50, 47,  // bal
			24, 21, 18, 25, 22, 19,	26, 23, 20, // alsó
	};
	private static int [] dontes_center = { 0, 1, 0, 0, 3, 0 };	// az első lap jobbra, a hátsó balra fordul
    
    /**
     * Inicializáljuk a lehetséges forgatásokat
     * 
     */
	@Override
	public void initForgatasok()
	{
		int [] t = tekeres;
		int [] f = forditas;
		int [] d = dontes;
		
		Forgat.init();

		Forgat.addForgatas("teker", origin, t);
		Forgat.addForgatas("fordit", origin, f);
		Forgat.addForgatas("dont", origin, d);
		
		Forgat.addForgatas("felul", origin, d, t, d, d, d );
		Forgat.addForgatas("felul2", origin, d, t, t, d, d, d );
		Forgat.addForgatas("felul3", origin, d, t, t, t, d, d, d );
		
		Forgat.addForgatas("elol", origin, f, f, f, t, f  );
		Forgat.addForgatas("elol2", origin, f, f, f, t, t, f );
		Forgat.addForgatas("elol3", origin, f, f, f, t, t, t, f );

		Forgat.addForgatas("jobb", origin, t);
		Forgat.addForgatas("jobb2", origin, t, t);
		Forgat.addForgatas("jobb3", origin, t, t, t);
		
		Forgat.addForgatas("hatul", origin, f, t, f, f, f);
		Forgat.addForgatas("hatul2", origin, f, t, t, f, f, f);
		Forgat.addForgatas("hatul3", origin, f, t, t, t, f, f, f);
		
		Forgat.addForgatas("bal", origin, f, f, t, f, f);
		Forgat.addForgatas("bal2", origin, f, f, t, t, f, f);
		Forgat.addForgatas("bal3", origin, f, f, t, t, t, f, f);
		
		Forgat.addForgatas("alul", origin, d, f, f, t, d, f, f);
		Forgat.addForgatas("alul2", origin, d, f, f, t, t, d, f, f);
		Forgat.addForgatas("alul3", origin, d, f, f, t, t, t, d, f, f);
	}
    private static int [] ready = {
			1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fehér
			2, 2, 2, 2, 2, 2, 2, 2, 2, // első: zöld
			3, 3, 3, 3, 3, 3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, 4, // hátsó: kék
			5, 5, 5, 5, 5, 5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, 6, 6, 6, 6, 6, // alsó: sárga
	};
	// Így néz ki most a kocka
	private static int [] pill = {
			1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fehér
			2, 2, 2, 2, 2, 2, 2, 2, 2, // első: zöld
			3, 3, 3, 3, 3, 3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, 4, // hátsó: kék
			5, 5, 5, 5, 5, 5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, 6, 6, 6, 6, 5, // alsó: sárga
	};
	// teszt
	private static int [] teszt = {
			2, 1, 5, 4, 1, 5, 3, 1, 5, // felső: fehér
			1, 3, 1, 5, 2, 2, 5, 2, 2, // első: zöld
			4, 4, 4, 1, 3, 1, 1, 5, 5, // jobb: piros
			6, 5, 6, 4, 4, 6, 2, 2, 6, // hátsó: kék
			3, 6, 4, 3, 5, 2, 3, 3, 2, // bal: narancs
			1, 3, 3, 4, 6, 6, 4, 6, 6, // alsó: sárga
	};
	private static int [][] kockak = 
	    {
	        ready, pill
	    };

    private static int [] teteje = {
			1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fehér
			2, 2, 2, 0, 0, 0, 0, 0, 0, // első: zöld
			3, 3, 3, 0, 0, 0, 0, 0, 0, // jobb: piros
			4, 4, 4, 0, 0, 0, 0, 0, 0, // hátsó: kék
			5, 5, 5, 0, 0, 0, 0, 0, 0, // bal: narancs
			0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Kocka3x3x3 game = new Kocka3x3x3( teszt );
        System.out.println("X0" + game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}