package kocka;

import base.GameController;
import kocka.forgat.Forgat;

//
/**
 * 2x2-es kocka kirakása 
 * @author slenk
 *
 */
public class Kocka2x2x2 extends Kocka
{
    private static String [] colors = {"x", "F", "Z", "P", "S", "N", "K"};
	private static boolean combinedStep = false;	// Ha false, akkor elemi forgatások a 3 (jobb,elöl,alul) irányban egyet jobbra
											// ha true, akkor összetett forgatások a fenti irányban: 1,2,3 jobbra fordulat
	private static int cntCol = 4;
    protected Kocka2x2x2( byte [] stage, int prevStep )
    {
    	super( stage, prevStep );
    	initForgatasok();
    }
    public Kocka2x2x2( byte [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Kocka2x2x2( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( byte [] stage, int prevStep )
	{
    	return new Kocka2x2x2( stage, prevStep );
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
	public byte [] getTarget()
    {
    	return ready;
    }
    /**
     * Beállítjuk a lehetséges forgatásokat
     * Minden forgatás olyan (legyen), hogya a ref pozíciót (felső, bal, hátsó sarok) a helyén tartja
     * Ebben az esetben nincs szükség a getNorma() függvényre
     */
    @Override
	public void initForgatasok()
	{
		Forgat.init();

		Forgat.addForgatas("jobb", origin, jobb);
		
		if ( combinedStep )
			Forgat.addForgatas("jobb2", origin, jobb, jobb);
		
		if ( combinedStep )
			Forgat.addForgatas("jobb3", origin, jobb, jobb, jobb);
		
		Forgat.addForgatas("elol", origin, egesz_bal, egesz_bal, egesz_bal, jobb, egesz_bal);
		if ( combinedStep )
			Forgat.addForgatas("elol2", origin, egesz_bal, egesz_bal, egesz_bal, jobb, jobb, egesz_bal);

		if ( combinedStep )
			Forgat.addForgatas("elol3", origin, egesz_bal, egesz_bal, egesz_bal, jobb, jobb, jobb, egesz_bal);

		Forgat.addForgatas("alul", origin, jobbra_dont, jobbra_dont, jobbra_dont, jobb, jobbra_dont);

		if ( combinedStep )
			Forgat.addForgatas("alul2", origin, jobbra_dont, jobbra_dont, jobbra_dont, jobb, jobb, jobbra_dont);

		if ( combinedStep )
			Forgat.addForgatas("alul2", origin, jobbra_dont, jobbra_dont, jobbra_dont, jobb, jobb, jobb, jobbra_dont);
	}
    /*
     * Az első 4 elem a felső lap
     * a második 4, az első
     * harmadik 4 a jobb oldali
     * negyedik 4 a hátsó
     * ötödik 4 a bal
     * hatodik 4 az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * Lapon belül az elrendezés a következő:
     * 01
     * 23
     */
    // Az eredeti elrendezés:
	private static int [] origin = {
			 0,  1,  2,  3,		// felső
			 4,  5,  6,	 7,  	// első
			 8,  9, 10, 11,		// jobb
			12, 13, 14, 15,		// hátsó
			16, 17, 18, 19,		// bal
			20, 21, 22, 23		// alsó
	};
    // Az egyes forgatások:
    // Az elnevezést az adja, hogy melyik lapot milyen irányban forgatjuk
    // 
	private static int [] jobb = {
			 0,  5,  2,  7,		// felső
			 4, 21,  6,	23,  	// első
			10,  8, 11,  9,		// jobb
			 3, 13,  1, 15,		// hátsó
			16, 17, 18, 19,		// bal
			20, 14, 22, 12		// alsó
	};
    // Az egész kockát egyben balra fordítjuk egyet
	// A felső lap a helyén marad.
	// Az első lap a bal oldalra kerül
    // 
	private static int [] egesz_bal = {
			 2,  0,  3,  1,		// felső
			 8,  9, 10, 11,		// jobb --> első
			12, 13, 14, 15,		// hátsó --> jobb
			16, 17, 18, 19,		// bal --> hátsó
			 4,  5,  6,	 7,  	// első --> bal
			21, 23, 20, 22		// alsó
	};
    // Az egész kockát egyben jobbra döntjük
	// Az első lap a helyén marad.
	// A felső lap a jobb oldalra kerül
    // 
	private static int [] jobbra_dont = {
			18, 16, 19, 17,		// bal --> felső
			 6,  4,  7,	 5,  	// első
			 2,  0,  3,  1,		// felső --> jobb
			13, 15, 12, 14,		// hátsó
			22, 20, 23, 21,		// alsó --> bal
			10,  8, 11,  9,		// jobb --> alul
	};
    private static byte [] ready = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 2, 2, // első: zöld
			3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
	// Így néz ki most a kocka
	//private static int [] pill = {1,5,2,6,     5,4,2,3,     3,6,1,3,     2,4,6,6,     5,1,4,3,    1,4,5,2      };
	//private static byte [] pill = {1,2,3,6,     1,5,2,6,     4,1,3,2,     5,4,3,2,     5,4,6,3,    1,4,5,6      };	// ez fejthető
	//private static int [] pill = {1,1,1,1,     2,2,2,3,     3,3,6,6,     4,4,3,4,     5,5,5,5,    6,2,6,4      };	//2 elforgatva (nem megy)
	//private static int [] pill = {1,1,1,1,     2,2,2,3,     3,3,4,4,     4,4,5,2,     5,5,3,5,    6,6,6,6      }; //3-at körbe cserél
	//private static int [] pill = {1,1,1,1,     2,2,3,5,     3,3,2,3,     4,4,4,4,     5,5,5,2,    6,6,6,6      };
	//private static int [] pill = {1,4,5,5,     2,2,5,2,     1,3,1,1,     6,4,4,3,     5,6,6,4,    6,3,2,3      };
	//private static int [] pill = {1,2,1,2,     2,6,2,6,     3,3,3,3,     1,4,1,4,     5,5,5,5,    6,4,6,4      };
	//private static int [] pill = {1,1,3,1,     6,2,2,3,     3,3,4,6,     4,4,5,4,     5,2,5,1,    5,6,6,2      };
	private static byte [] pill = {1,1,1,1,     2,2,5,4,     3,3,6,4,     4,4,5,2,     5,5,3,6,    2,3,6,6      };

	private static byte [][] kockak = 
    {
        ready, pill
    };

    private static byte [] teteje = {
			1, 1, 1, 1, // felső: fehér
			0, 0, 0, 0, // első: zöld
			0, 0, 0, 0, // jobb: piros
			0, 0, 0, 0, // hátsó: sárga
			0, 0, 0, 0, // bal: narancs
			0, 0, 0, 0, // alsó: kék
	};
    private static byte [] teteje_oldala = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 0, 0, // első: zöld
			3, 3, 0, 0, // jobb: piros
			4, 4, 0, 0, // hátsó: sárga
			5, 5, 0, 0, // bal: narancs
			0, 0, 0, 0, // alsó: kék
	};
    private static byte [] cel = {
			1, 1, 1, 1, // felső: fehér
			2, 2, 2, 2, // első: zöld
			3, 3, 3, 3, // jobb: piros
			4, 4, 4, 4, // hátsó: sárga
			5, 5, 5, 5, // bal: narancs
			6, 6, 6, 6, // alsó: kék
	};
	private static byte [][] targets = 
    {
        cel, teteje, teteje_oldala
    };
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Kocka2x2x2 game = new Kocka2x2x2( 1 );
        System.out.println("X0" + game);
        
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}