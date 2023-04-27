package kocka.kocka4;

import java.util.Vector;

import base.GameController;
import base.Gameable;
import kocka.Kocka;
import kocka.forgat.Forgat;

//
/**
 * 2x2-es kocka kirakása 
 * @author slenk
 *
 */
public class Kocka4x4x4 extends Kocka
{
    private static String [] colors = {"x", "F", "P", "K", "N", "Z", "S"};
	private static int cntCol = 16;
	
    protected Kocka4x4x4( byte [] stage, int prevStep )
    {
    	super( stage, prevStep );
    }
    public Kocka4x4x4( byte [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Kocka4x4x4( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( byte [] stage, int prevStep )
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
	public byte [] getTarget()
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
     * Beállítjuk a lehetséges forgatásokat
     * Minden forgatás olyan (legyen), hogya a ref pozíciót (felső oldalon 3;3 pozícióban a "V" betű) a helyén tartja
     * Ebben az esetben nincs szükség a getNorma() függvényre
     */
    @Override
	public void initForgatasok()
	{
		Forgat.init();
		int [] t;				// tekerés
		int [] t2;				// tekerés
		int [] f = egesz_bal;	// fordítás
		int [] d = jobbra_dont;	// döntés
		

		//Forgat.addForgatas("jobb", origin, jobb);
		//Forgat.addForgatas("jobb_reteg", origin, jobb_reteg);
		//Forgat.addForgatas("egesz_bal", origin, egesz_bal);
		//Forgat.addForgatas("jobbra_dont", origin, jobbra_dont);

		t = Forgat.forgatasok(origin, jobb);
		t2 = Forgat.forgatasok(origin, jobb, jobb_reteg, f, d, d, d, f, f, f);
//		Forgat.addForgatas("jobb2", origin, t2 );
//		Forgat.addForgatas("felul2", origin, d, t2, d, d, d );
//		Forgat.addForgatas("elol2", origin, f, f, f, t2, f  );
//		Forgat.addForgatas("jobb", origin, t );
		Forgat.addForgatas("felul", origin, d, t, d, d, d, f, f, f );
		Forgat.addForgatas("elol", origin, f, f, f, t, f  );
//		Forgat.addForgatas("bal", origin, f, f, t, f, f );
//		Forgat.addForgatas("alul", origin, d, d, d, t, d );
		Forgat.addForgatas("hatul", origin, f, t, f, f, f );
//		Forgat.addForgatas("jobb(2)", origin, t, t );
		Forgat.addForgatas("felul(2)", origin, d, t, t, d, d, d, f, f );
		Forgat.addForgatas("elol(2)", origin, f, f, f, t, t, f  );
//		Forgat.addForgatas("bal(2)", origin, f, f, t, t, f, f );
//		Forgat.addForgatas("alul(2)", origin, d, d, d, t, t, d );
		Forgat.addForgatas("hatul(2)", origin, f, t, t, f, f, f );
		Forgat.addForgatas("jobb_reteg", origin, jobb_reteg );
//		Forgat.addForgatas("felul_reteg", origin, d, jobb_reteg, d, d, d);
//		Forgat.addForgatas("elol_reteg", origin, f, jobb_reteg, f, f, f);
		Forgat.addForgatas("bal_reteg", origin, f, f, jobb_reteg, f, f );
//		Forgat.addForgatas("alul_reteg", origin, d, d, d, jobb_reteg, d );
//		Forgat.addForgatas("hatul_reteg", origin, f, jobb_reteg, f, f, f );
		
//		int [] jobb = Forgat.forgatasok( origin, t );
//		int [] felul = Forgat.forgatasok( origin, d, t, d, d, d );
//		int [] elol = Forgat.forgatasok( origin, f, f, f, t, f  );
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
    private static byte [] pill_x = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
			2, 2, 2, 2, 5, 2, 5, 6, 3, 6, 6, 3, 4, 2, 3, 2, // első: piros
			3, 3, 3, 3, 3, 4, 2, 5, 2, 3, 5, 6, 3, 4, 2, 3, // jobb kék
			4, 4, 4, 4, 4, 6, 5, 4, 2, 4, 4, 6, 4, 2, 4, 2, // hátsó narancs
			5, 5, 5, 5, 3, 2, 3, 6, 5, 3, 4, 2, 6, 6, 3, 6, // bal: zöld
			5, 6, 6, 6, 4, 3, 2, 6, 4, 5, 6, 5, 5, 5, 5, 6, // alsó: sárga
	};
    private static byte [] pill_centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 2, 6, 0, 0, 5, 6, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 6, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 2, 5, 0, 0, 5, 4, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 2, 5, 0, 0, 4, 6, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 2, 4, 0, 0, 3, 4, 0, 0, 0, 0, 0, // alsó: sárga
	};

    
    
    private static byte [] pill = {
    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 2, 2, 1, // felső: fekete
			2, 1, 1, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
	};
    private static byte [] target = {
    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1, // felső: fekete
			2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
	};
    
    
    
    
    
    
    
    
    private static byte [] teteje = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static byte [] centrums = {
			0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, // felső: fekete
			0, 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, 0, // első: piros
			0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 3, 0, 0, 0, 0, 0, // jobb kék
			0, 0, 0, 0, 0, 4, 4, 0, 0, 4, 4, 0, 0, 0, 0, 0, // hátsó narancs
			0, 0, 0, 0, 0, 5, 5, 0, 0, 5, 5, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 6, 6, 0, 0, 6, 6, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static byte [] sarok = {
    		1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, // felső: fekete
    		2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, // első: piros
			3, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, // jobb kék
			4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, // hátsó narancs
			5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, // bal: zöld
			6, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 6, 0, 0, 6, // alsó: sárga
	};
    private static byte [] kozepe_sarok = {
    		1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, // felső: fekete
    		2, 0, 0, 2, 0, 2, 2, 0, 0, 2, 1, 0, 2, 0, 0, 2, // első: piros
			3, 0, 0, 3, 0, 3, 3, 0, 0, 3, 3, 0, 3, 0, 0, 3, // jobb kék
			4, 0, 0, 4, 0, 4, 4, 0, 0, 4, 4, 0, 4, 0, 0, 4, // hátsó narancs
			5, 0, 0, 5, 0, 5, 5, 0, 0, 5, 5, 0, 5, 0, 0, 5, // bal: zöld
			6, 0, 0, 6, 0, 6, 6, 0, 0, 6, 6, 0, 6, 0, 0, 6, // alsó: sárga
	};
    private static byte [] teteje_sarok = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static byte [] teteje_kozepe = {
    		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // felső: fekete
    		2, 2, 2, 2, 0, 0, 2, 2, 0, 0, 2, 2, 0, 0, 0, 0, // első: piros
			3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // jobb kék
			4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // hátsó narancs
			5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // bal: zöld
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // alsó: sárga
	};
    private static byte [] ready = {
    		1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1,  1, 1, 1, 1, // felső: fekete
			2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2,  2, 2, 2, 2, // első: piros
			3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3,  3, 3, 3, 3, // jobb kék
			4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4,  4, 4, 4, 4, // hátsó narancs
			5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5,  5, 5, 5, 5, // bal: zöld
			6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6,  6, 6, 6, 6, // alsó: sárga
	};
	private static byte [][] kockak = 
    {
        ready, centrums, pill
    };
	private static byte [][] targets = 
    {
        ready, centrums
    };
	@Override
    public String toString( byte [] tabla )
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
	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Kocka4x4x4 game = new Kocka4x4x4( pill );
        game.init();
        System.out.println("X0" + game);

        Vector<Gameable>kockak = game.nextStages();
        for ( Gameable g:kockak)
            System.out.println(g);
        
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}