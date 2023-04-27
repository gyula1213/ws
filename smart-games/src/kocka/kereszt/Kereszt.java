package kocka.kereszt;

import base.GameController;
import kocka.Kocka;

//Bűvös kereszt kirakása
public class Kereszt extends Kocka
{
	int cntCol = 8;
    private Kereszt( byte [] stage, int prevStep )
    {
    	super( stage, prevStep );
    }
    public Kereszt( byte [] stage )
    {
    	super( stage, -1 );
    	idSzamlalo=1;
    }
    public Kereszt( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( byte [] stage, int prevStep )
	{
    	return new Kereszt( stage, prevStep );
	}
    /*
     * Az első 8 elem a felső lap
     * a második 8, az első
     * harmadik 8 a jobb oldali
     * negyedik 8 a hátsó
     * ötödik 8 a bal
     * hatodik 8 az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * Lapon belül az elrendezés a következő:
     * 0xxx1
     * x4x7x
     * xxxxx
     * x5x6x
     * 2xxx3
     */
    // A felső lap középső keresztje mindig helyben marad. Ez a forgatás fix pontja
    // Az elnevezést az adja, hogy a felső lapon melyik elem fog forogni (mindig csak 1)
    // Az egyes forgatások:
    // 
	private static int [] jobb_also_1 = {
			 0,  1,  2, 35,  4,  5,  6,  7,		// felső
			 8, 40, 43, 41, 45, 46, 47, 44,		// első
			10, 17, 11,  9, 13, 14, 15, 12,		// jobb
			24, 25,  3, 27, 28, 29, 30, 31,		// hátsó
			32, 33, 34, 26, 36, 37, 38, 39,		// bal
			19, 18, 42, 16, 22, 23, 20, 21		// alsó
	};
	private static int [] jobb_also_2 = {
			 0,  1,  2, 26,  4,  5,  6,  7,		// felső
			 8, 19, 16, 18, 23, 20, 21, 22,		// első
			43, 17, 41, 40, 46, 47, 44, 45,		// jobb
			24, 25, 35, 27, 28, 29, 30, 31,		// hátsó
			32, 33, 34,  3, 36, 37, 38, 39,		// bal
			 9, 11, 42, 10, 15, 12, 13, 14		// alsó
	};
	private static int [] jobb_felso_1 = {
			 0, 11,  2,  3,  4,  5,  6,  7,		// felső
			 8,  9, 10, 34, 12, 13, 14, 15,		// első
			16, 41, 42, 43, 44, 45, 46, 47,		// jobb
			18, 25, 19, 17, 21, 22, 23, 20,		// hátsó
			32, 33,  1, 35, 36, 37, 38, 39,		// bal
			40, 27, 24, 26, 31, 28, 29, 30		// alsó
	};
	private static int [] jobb_felso_2 = {
			 0, 34,  2,  3,  4,  5,  6,  7,		// felső
			 8,  9, 10,  1, 12, 13, 14, 15,		// első
			16, 27, 24, 26, 31, 28, 29, 30,		// jobb
			42, 25, 43, 41, 45, 46, 47, 44,		// hátsó
			32, 33, 11, 35, 36, 37, 38, 39,		// bal
			40, 17, 18, 19, 20, 21, 22, 23		// alsó
	};
	private static int [] bal_felso_1 = {
			19,  1,  2,  3,  4,  5,  6,  7,		// felső
			 8,  9,  0, 11, 12, 13, 14, 15,		// első
			16, 17, 18, 10, 20, 21, 22, 23,		// jobb
			24, 43, 40, 42, 47, 44, 45, 46,		// hátsó
			26, 33, 27, 25, 29, 30, 31, 28,		// bal
			32, 41, 34, 35, 36, 37, 38, 39		// alsó
	};
	private static int [] bal_felso_2 = {
			10,  1,  2,  3,  4,  5,  6,  7,		// felső
			 8,  9, 19, 11, 12, 13, 14, 15,		// első
			16, 17, 18,  0, 20, 21, 22, 23,		// jobb
			24, 35, 32, 34, 39, 36, 37, 38,		// hátsó
			40, 33, 42, 43, 44, 45, 46, 47,		// bal
			26, 41, 27, 25, 29, 30, 31, 28		// alsó
	};
	private static int [] bal_also_1 = {
			 0,  1, 27,  3,  4,  5,  6,  7,		// felső
			34,  9, 35, 33, 37, 38, 39, 36,		// első
			16, 17,  2, 19, 20, 21, 22, 23,		// jobb
			24, 25, 26, 18, 28, 29, 30, 31,		// hátsó
			32, 42, 41, 40, 46, 47, 44, 45,		// bal
			10,  8, 11, 43, 13, 14, 15, 12		// alsó
	};
	private static int [] bal_also_2 = {
			 0,  1, 18,  3,  4,  5,  6,  7,		// felső
			41,  9, 40, 42, 47, 44, 45, 46,		// első
			16, 17, 27, 19, 20, 21, 22, 23,		// jobb
			24, 25, 26,  2, 28, 29, 30, 31,		// hátsó
			32, 11,  8, 10, 15, 12, 13, 14,		// bal
			35, 34, 33, 43, 38, 39, 36, 37		// alsó
	};
	static int [][] forgatasok = 
	{
		jobb_also_1, jobb_also_2, jobb_felso_1, jobb_felso_2, bal_felso_1, bal_felso_2, bal_also_1, bal_also_2
	};
	// -1: Cél: Teljesen kész
    // -2: Cél: Kész van, csak a közepe lehet elfordulva
    @Override
    public String getForgatasName(int prevStep)
    {
    	switch( prevStep )
        {
	    	case -1 : return "init";
	    	case 0 : return "JA1";
	    	case 1 : return "JA2";
	    	case 2 : return "JF1";
	    	case 3 : return "JF2";
	    	case 4 : return "BF1";
	    	case 5 : return "BF2";
	    	case 6 : return "BA1";
	    	case 7 : return "BA2";
	    	default : return "nincs ilyen lépés";
        }
    }
    private static String [] colors = {"x", "S", "K", "P", "Z", "F", "N"};

    		
    public String toString()
    {
    	String s = "";
    	s += getId() + ":: " + getPrevStep() + ":: ";
    	s += toString(actual);
        s += "\npill.: " + getSource(actual);
    	return s;
    }
	@Override
    public String toString( byte [] tabla )
    {
        String s="";
        for ( int i=0; i<tabla.length; i++ )
        {
            if ( (i+1)%cntCol > 4 )		// Az első 4-et és a 8.-at írja ki
            	continue;
            int val = tabla[i] > 10 ? tabla[i]/10 : tabla[i]; 
            String color = colors[val];
            s += color +"";
            if ( (i+1)%cntCol == 0 )
                //s += "\n";
            	s += ",     ";
            else
                s += " ";
        }
        return s;
    }
    private String getSource( byte [] tabla )
    {
        String s="";
        for ( int i=0; i<tabla.length; i++ )
        {
            int val = tabla[i] > 10 ? tabla[i]/10 : tabla[i]; 
            s += val +"";
            if ( (i+1)%cntCol == 0 )
                //s += "\n";
            	s += ",     ";
            else
                s += ",";
        }
        return s;
    }
    private static byte [] ready = {
			1, 1, 1, 1, 1, 1, 1, 1,	// felső: sárga
			2, 2, 2, 2, 2, 2, 2, 2,	// első: kék
			3, 3, 3, 3, 3, 3, 3, 3,	// jobb: piros
			4, 4, 4, 4, 4, 4, 4, 4,	// hátsó: zöld
			5, 5, 5, 5, 5, 5, 5, 5,	// bal: fehér
			6, 6, 6, 6, 6, 6, 6, 6,	// alsó: narancs
	};
	private static byte [] ready_full = {
			1, 1, 1, 1, 11, 12, 13, 14,	// felső: sárga
			2, 2, 2, 2, 21, 22, 23, 24,	// első: kék
			3, 3, 3, 3, 31, 32, 33, 34,	// jobb: piros
			4, 4, 4, 4, 41, 42, 43, 44,	// hátsó: zöld
			5, 5, 5, 5, 51, 52, 53, 54,	// bal: fehér
			6, 6, 6, 6, 61, 62, 63, 64,	// alsó: narancs
	};
	private static byte [] x = {
			1, 1, 2, 6, 1, 1, 1, 1,	// felső
			3, 2, 4, 1, 2, 2, 2, 2,	// első
			5, 5, 4, 6, 3, 3, 3, 3,	// jobb
			2, 2, 3, 5, 4, 4, 4, 4,	// hátsó
			3, 6, 6, 5, 5, 5, 5, 5,	// bal
			1, 3, 4, 4, 6, 6, 6, 6,	// alsó
	};
	private static byte [] csak_kozep = {
    		0, 0, 0, 0, 1, 1, 1, 1,	// felső: sárga
			0, 0, 0, 0, 5, 5, 5, 5,	// első: kék
			0, 0, 0, 0, 4, 4, 4, 4,	// jobb: piros
			0, 0, 0, 0, 6, 6, 6, 6,	// hátsó: zöld
			0, 0, 0, 0, 3, 3, 3, 3,	// bal: fehér
			0, 0, 0, 0, 2, 2, 2, 2,	// alsó: narancs
	};
	private static byte [] y = {
			1, 1, 1, 1, 11, 12, 13, 14,	// felső
			2, 2, 2, 2, 21, 22, 23, 24,	// első
			3, 3, 3, 3, 31, 32, 33, 34,	// jobb
			4, 4, 4, 4, 41, 42, 43, 44,	// hátsó
			5, 5, 5, 5, 53, 54, 51, 52,	// bal
			6, 6, 6, 6, 63, 64, 61, 62,	// alsó
	};
	//private static byte [] pill = {1,1,1,1,1,1,1,1,     2,6,6,4,2,2,2,2,     2,5,3,1,3,3,3,3,     4,4,3,6,4,4,4,4,     5,5,2,4,5,5,5,5,     3,1,3,2,6,6,6,6,    };
	private static byte [] pill = {6,4,1,1,1,1,1,1,     2,2,6,1,2,2,2,2,     3,6,4,5,3,3,3,3,     5,2,4,3,4,4,4,4,     5,5,6,4,5,5,5,5,     3,3,2,1,6,6,6,6,      };

	private static byte [][] kockak = 
	    {
	        ready_full, pill, x, y
	    };

	

    private static byte [] kozepek = {
    		0, 0, 0, 0, 0, 0, 0, 1,	// felső: sárga
			0, 0, 0, 0, 0, 0, 0, 2,	// első: kék
			0, 0, 0, 0, 0, 0, 0, 3,	// jobb: piros
			0, 0, 0, 0, 0, 0, 0, 4,	// hátsó: zöld
			0, 0, 0, 0, 0, 0, 0, 5,	// bal: fehér
			0, 0, 0, 0, 0, 0, 0, 6,	// alsó: narancs
	};
    private static byte [] teteje = {
			1, 1, 1, 1, 1, 1, 1, 1,	// felső: sárga
			0, 0, 0, 0, 2, 2, 2, 2,	// első: kék
			0, 0, 0, 0, 3, 3, 3, 3,	// jobb: piros
			0, 0, 0, 0, 4, 4, 4, 4,	// hátsó: zöld
			0, 0, 0, 0, 5, 5, 5, 5,	// bal: fehér
			0, 0, 0, 0, 6, 6, 6, 6,	// alsó: narancs
	};
    private static byte [] elso_ketto = {
			0, 0, 1, 1, 1, 1, 1, 1,	// felső: sárga
			2, 2, 0, 0, 2, 2, 2, 2,	// első: kék
			0, 0, 0, 0, 3, 3, 3, 3,	// jobb: piros
			0, 0, 0, 0, 4, 4, 4, 4,	// hátsó: zöld
			0, 0, 0, 0, 5, 5, 5, 5,	// bal: fehér
			0, 0, 0, 0, 6, 6, 6, 6,	// alsó: narancs
	};
    private static byte [] teteje_felso = {
			1, 1, 1, 1, 1, 1, 1, 1,	// felső: sárga
			2, 2, 0, 0, 2, 2, 2, 2,	// első: kék
			3, 3, 0, 0, 3, 3, 3, 3,	// jobb: piros
			4, 4, 0, 0, 4, 4, 4, 4,	// hátsó: zöld
			5, 5, 0, 0, 5, 5, 5, 5,	// bal: fehér
			0, 0, 0, 0, 6, 6, 6, 6,	// alsó: narancs
	};
    private static byte [] cel = {
			1, 1, 1, 1, 1, 1, 1, 1,	// felső: sárga
			2, 2, 0, 0, 2, 2, 2, 2,	// első: kék
			0, 0, 0, 0, 3, 3, 3, 3,	// jobb: piros
			0, 0, 0, 0, 4, 4, 4, 4,	// hátsó: zöld
			0, 0, 0, 0, 5, 5, 5, 5,	// bal: fehér
			0, 0, 0, 0, 6, 6, 6, 6,	// alsó: narancs
	};
	private static byte [][] targets = 
	    {
	        cel, kozepek, teteje, elso_ketto, teteje_felso
	    };
	public boolean isReady()
    {
    	int target = -1;
    	switch (target)
        {
    	case -1: 
        	return equals( ready_full, actual );
    	case -2: 
        	return equals( ready, actual );
        default :
        	byte [] act_target = targets[target];
            for ( int i=0; i<actual.length; i++ )
            {
                if ( act_target[i] == 0 )    // itt mindegy mi van
                {
                    continue;
                }
                if ( actual[i] != act_target[i] )    // eltérés
                {
                    return false;  // ez nem jó
                }
            }
            return true;   // ez egyezett
        }
    }

    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Kereszt game = new Kereszt( 1 );
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}