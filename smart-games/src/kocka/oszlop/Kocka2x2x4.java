package kocka.oszlop;

import base.GameController;
import kocka.Kocka;
import kocka.forgat.Forgat;

/**
 * 2x2x4-es bűvös kocka kirakásának az a része, amikor az elemek már bent vannak 2x2x4-es térben, és onnan nem is lépnek ki (classic)
 * @author slenk
 *
 */
public class Kocka2x2x4 extends Kocka
{
	private static int cntCol = 4;
    private static String [] colors = {"x", "F", "Z", "P", "S", "N", "K"};
    private Kocka2x2x4( int [] stage, int prevStep )
    {
    	super( stage, prevStep );
    	if ( Forgat.getSize() == 0 )
    	{
    		initForgatasok();
    		setActive();
    	}
    }
    public Kocka2x2x4( int [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Kocka2x2x4( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( int [] stage, int prevStep )
	{
    	return new Kocka2x2x4( stage, prevStep );
	}
    /**
     * Beállítjuk a lehetséges forgatásokat
     * Elemi forgatások:
     * 	-- jobb szélső lap jobbra (vagy balra - mindegy)
     *  -- egész oszlop egyet jobbra
     *  -- felső sor egyet jobbra
     *  -- második sor egyet jobbra
     *  -- fejreállítás, úgy hogy az első és a hátsó lap marad a helyén, a felső és az alső, a jobb oldali és a bal oldali helyet cserél
     */
    @Override
	public void initForgatasok()
	{
		Forgat.init();
		boolean onlyBase = false;
		Forgat.addForgatas("jobb", origin, jobb);
		Forgat.addForgatas("elol", origin, egesz_jobb, egesz_jobb, egesz_jobb, jobb, egesz_jobb );
		Forgat.addForgatas("f1_jobb", origin, egesz_jobb, egesz_jobb, egesz_jobb, jobb, egesz_jobb );

		if ( !onlyBase )
		{
			Forgat.addForgatas("f1_jobb2", origin, felso_jobb, felso_jobb, egesz_jobb, egesz_jobb );
			Forgat.addForgatas("f1_bal", origin, felso_jobb, felso_jobb, felso_jobb, egesz_jobb );
		}

		Forgat.addForgatas("f2_jobb", origin, masodik_jobb );

		//Forgat.addForgatas("f12_jobb", origin, felso_jobb, masodik_jobb );

		if ( !onlyBase )
		{
			Forgat.addForgatas("f2_jobb2", origin, masodik_jobb, masodik_jobb );
			Forgat.addForgatas("f2_bal", origin, masodik_jobb, masodik_jobb, masodik_jobb );
		}

		Forgat.addForgatas("f3_bal", origin, felborit, masodik_jobb, felborit );

		if ( !onlyBase )
		{
			Forgat.addForgatas("f3_jobb2", origin, felborit, masodik_jobb, masodik_jobb, felborit );
			Forgat.addForgatas("f3_jobb", origin, felborit, masodik_jobb, masodik_jobb, masodik_jobb, felborit );
		}
		Forgat.addForgatas("f4_bal", origin, felborit, felso_jobb, felborit );

		if ( !onlyBase )
		{
			Forgat.addForgatas("f4_jobb2", origin, felborit, felso_jobb, felso_jobb, felborit );
			Forgat.addForgatas("f4_jobb", origin, felborit, felso_jobb, felso_jobb, felso_jobb, felborit );
		}

		if ( !onlyBase )
		{
			Forgat.addForgatas("f12_jobb", origin, felso_jobb, masodik_jobb, egesz_jobb, egesz_jobb, egesz_jobb );
		}
	}
	private void setActive()
	{
		Forgat.setAllActive(false);
		Forgat.setActive(true, "jobb", "elol", "f2_jobb");
		//Forgat.setActive(true, "jobb", "elol", "f2_jobb", "f1_jobb");
		//Forgat.setActive(true, "jobb", "elol", "f12_jobb", "f2_jobb", "f1_jobb");
	}
    /*
     * Az első 4 elem a felső lap
     * a második 8, az első
     * harmadik 8 a jobb oldali
     * negyedik 8 a hátsó
     * ötödik 8 a bal
     * hatodik 4 az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * Lapon belül az elrendezés a következő:
     * 01
     * 23
     */
    // Az eredeti elrendezés:
	private static int [] origin = {
			 0,  1,  2,  3,		// felső
			 4,  5,  6,	 7, 8,  9, 10, 11, 	// első
			 12, 13, 14, 15, 16, 17, 18, 19,	// jobb
			 20, 21, 22, 23, 24, 25, 26, 27,	// hátsó
			 28, 29, 30, 31, 32, 33, 34, 35,	// bal
			 36, 37, 38, 39		// alsó
	};
    // Az egyes forgatások:
    // jobb szélső lap jobbra (vagy balra - mindegy)
    // 
	private static int [] jobb = {
			 0,  37,  2,  39,		// felső
			 4,  26,  6, 24, 8,  22, 10, 20, 	// első
			 19, 18, 17, 16, 15, 14, 13, 12,	// jobb
			 11, 21, 9, 23, 7, 25, 5, 27,	// hátsó
			 28, 29, 30, 31, 32, 33, 34, 35,	// bal
			 36, 1, 38, 3		// alsó
	};
    // Az egész kockát egyben jobbra fordítjuk egyet
	// A felső lap a helyén marad.
	// Az első lap a bal oldalra kerül
    // 
	private static int [] egesz_jobb = {
			 2,  0,  3,  1,		// felső
			 12, 13, 14, 15, 16, 17, 18, 19,	// jobb --> első
			 20, 21, 22, 23, 24, 25, 26, 27,	// hátsó --> jobb
			 28, 29, 30, 31, 32, 33, 34, 35,	// bal --> hátra
			 4,  5,  6,	 7, 8,  9, 10, 11, 	// első --> bal
			 37, 39, 36, 38		// alsó
	};
    // Felső sor egyet jobbra
    // 
	private static int [] felso_jobb = {
			 2,  0,  3,  1,		// felső
			 12,  13,  6, 7, 8,  9, 10, 11, 	// első
			 20, 21, 14, 15, 16, 17, 18, 19,	// jobb
			 28, 29, 22, 23, 24, 25, 26, 27,	// hátsó
			 4, 5, 30, 31, 32, 33, 34, 35,		// bal
			 36, 37, 38, 39		// alsó
	};
    // Második sor egyet jobbra
    // 
	private static int [] masodik_jobb = {
			 0,  1,  2,  3,		// felső
			 4,  5,  14, 15, 8,  9, 10, 11, 	// első
			 12, 13, 22, 23, 16, 17, 18, 19,	// jobb
			 20, 21, 30, 31, 24, 25, 26, 27,	// hátsó
			 28, 29, 6, 7, 32, 33, 34, 35,	// bal
			 36, 37, 38, 39		// alsó
	};
    // Felborít, úgy hogy az első és a hátsó lap marad a helyén, a felső és az alsó, a jobb oldali és a bal oldali helyet cserél:
	private static int [] felborit = {
			 39,  38,  37, 36,		// felső
			 11, 10, 9, 8, 7, 6, 5, 4, 	// első
			 35, 34, 33, 32, 31, 30, 29, 28,	// jobb
			 27, 26, 25, 24, 23, 22, 21, 20,	// hátsó
			 19, 18, 17, 16, 15, 14, 13, 12,	// bal
			 3, 2, 1, 0		// alsó
	};
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
    	//return teteje_alja;
    }
    
 	// Így néz ki most a kocka
	private static int [] pill = {
			1,1,1,1,
			2,2,5,2,5,2,2,2,
			3,3,3,3,3,3,3,3, 
			4,4,4,4,4,4,4,4,
			5,5,5,2,5,2,5,5,
			6,6,6,6
	};
	/**
	 * Targets
	 * Az alábbi célok megvalósításával jutunk el a megoldáshoz
	 */
    private static int [] kozepe = {
			0, 0, 0, 0, // felső: fehér
			0, 0, 2, 2, 2, 2, 0, 0, // első: zöld
			0, 0, 0, 0, 0, 0, 0, 0, // jobb: piros
			0, 0, 0, 0, 0, 0, 0, 0, // hátsó: sárga
			0, 0, 0, 0, 0, 0, 0, 0, // bal: narancs
			0, 0, 0, 0, // alsó: kék
	};
    private static int [] teteje_alja = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 0, 0, 0, 0, 2, 2, // első: zöld
			3, 3, 0, 0, 0, 0, 3, 3, // jobb: piros
			4, 4, 0, 0, 0, 0, 4, 4, // hátsó: sárga
			5, 5, 0, 0, 0, 0, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
    private static int [] teteje1 = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 0, 0, 0, 0, 2, 4, // első: zöld
			3, 3, 0, 0, 0, 0, 5, 3, // jobb: piros
			4, 4, 0, 0, 0, 0, 4, 2, // hátsó: sárga
			5, 5, 0, 0, 0, 0, 3, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
    private static int [] teteje2 = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 0, 0, 0, 0, 5, 2, // első: zöld
			3, 3, 0, 0, 0, 0, 3, 3, // jobb: piros
			4, 4, 0, 0, 0, 0, 4, 5, // hátsó: sárga
			5, 5, 0, 0, 0, 0, 2, 4, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
    /**
     * A teljesen kész kocka
     */
    private static int [] ready = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 2, 2, 2, 2, 2, 2, // első: zöld
			3, 3, 3, 3, 3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 5, 5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
    
    /**
     * Lehetséges tekeréskombinációk egyes nagyobb, de egyszerű végeredményt hozó művelet leítására
     * A "profi" kockakirakónak ezeket érdemes megtanulnia
     */
    private static int [] csere1 = {	// első lap 2.sorában 2 elem csere és fordítás
			1, 1, 1, 1, // felső: fehér
			2, 2, 3, 5, 2, 2, 2, 2, // első: zöld
			3, 3, 2, 3, 3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 2, 5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
			/* Lépések száma: 17
				elol,f2_jobb,elol,f2_jobb,elol,jobb,f2_jobb,jobb,f2_jobb,jobb,f2_jobb,f2_jobb,jobb,f2_jobb,elol,f2_jobb,f2_jobb,
			 */
	};
    private static int [] csere2 = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 3, 2, 3, 2, 2, 2, // első: zöld
			3, 3, 5, 3, 5, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 2, 5, 2, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
			/* Lépések száma: 17
				f2_jobb,jobb,f2_jobb,jobb,f2_jobb,f2_jobb,jobb,elol,f2_jobb,elol,f2_jobb,f2_jobb,jobb,f2_jobb,elol,f2_jobb,elol,
			*/
	};
    private static int [] csere3 = {	// 2 élközép fordít
			1, 1, 1, 1, // felső: fehér
			2, 2, 5, 2, 5, 2, 2, 2, // első: zöld
			3, 3, 3, 3, 3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 2, 5, 2, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
			/*
			 * Lépések száma: 17
				f2_jobb,jobb,elol,f2_jobb,elol,f2_jobb,f2_jobb,jobb,f2_jobb,jobb,elol,f2_jobb,f2_jobb,elol,f2_jobb,jobb,f2_jobb,
			 */
	};
	private static int [][] kockak = 
	    {
	        ready, pill
	    };
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Kocka2x2x4 game = new Kocka2x2x4( pill );
        System.out.println("X0" + game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}