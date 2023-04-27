package kocka.domino;

import base.GameController;
import kocka.Kocka;

//Bűvös dominó kirakása
public class Domino extends Kocka //implements Gameable
{
    public Domino( byte [] stage, int prevStep )
    {
    	super( stage, prevStep );
    }
    public Domino( byte [] stage )
    {
    	super( stage, -1 );
    	idSzamlalo=1;
    }
    public Domino( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( byte [] stage, int prevStep )
	{
    	return new Domino( stage, prevStep );
	}
    /*
     * Az elemek elhelyezkedése
     * Az első 9 elem a felső lap, a második 9, az alsó
     * Az első számjegy a szín (1:fehér, 2:fekete), a második számjegy a pöttyök száma (1-9)
     * 
     * A kirakott kocka a "ready" tömbben látható
     */
	private static byte [] ready = {
			11, 12, 13,
			14, 15, 16,
			17, 18, 19,
			
			21, 22, 23,
			24, 25, 26,
			27, 28, 29
	};
	
    /*
     * Forgatások
     * Az első 9 elem a felső lap, a második 9, az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     * A kiinduló állapot az "origin" tömmben látható
     * Az egyes forgatások az elemeket a megfelelő nevű tömbbe mozgatják
     */
	private static int [] origin = {
			0, 1, 2,
			3, 4, 5,
			6, 7, 8,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17
	};
	private static int [] jobb = {
			0, 1, 15,
			3, 4, 12,
			6, 7, 9,
			
			8, 10, 11,
			5, 13, 14,
			2, 16, 17
	};
	private static int [] hatul = {
			9, 10, 11,
			3, 4, 5,
			6, 7, 8,
			
			0, 1, 2,
			12, 13, 14,
			15, 16, 17
	};
	private static int [] bal = {
			17, 1, 2,
			14, 4, 5,
			11, 7, 8,
			
			9, 10, 6,
			12, 13, 3,
			15, 16, 0
	};
	private static int [] elol = {
			0, 1, 2,
			3, 4, 5,
			15, 16, 17,
			
			9, 10, 11,
			12, 13, 14,
			6, 7, 8
	};
	private static int [] felul1 = {
			2, 5, 8,
			1, 4, 7,
			0, 3, 6,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17,
	};
	private static int [] felul2 = {
			8, 7, 6,
			5, 4, 3,
			2, 1, 0,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17,
	};
	private static int [] felul3 = {
			6, 3, 0,
			7, 4, 1,
			8, 5, 2,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17,
	};
	// Egy speciális forgatás, ami a felső lapon 2 elemet cserél meg
	private static int [] jf2jf2jf2 = {
			0, 7, 2,
			3, 4, 5,
			6, 1, 8,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17,
	};
	// azt az állapotot adjuk vissza
	// amikor adott 'lepes' szerint forgatjuk a kockát
    // 0: jobb, 1: hátsó, 2: bal, 3: első, 4: felső
	private static int [][] forgatasok = 
	{
		jobb, hatul, bal, elol, felul1, felul2, felul3, jf2jf2jf2
	};
	/**
	 * A cél függvény.
	 * Visszatérés true, ha elégedettek vagyunk a pillanatnyi állapottal
	 */
	// 0. Cél: Teljesen kész
    // 1. Cél: Felül fehér, alul fekete
	// 2. Cél: Fehér 1,2,3
	// 3. Cél: Csak a sarkok
	// 4. Cél: A sarkok és fehér 1,2,3,4
    @Override
    public boolean isReady()
    {
    	int target = 0;
    	switch (target)
        {
    	case 0: 
        	return equals( ready, actual );
    	case 1: 
        	for ( int i=0; i<actual.length; i++ ) 
        	{
        		if ( i<9 && actual[i] > 20 )	// az első 9 elem valamelyike fekete
        			return false;				// ez még nem jó
        	}
        	break;
    	case 2: 
        	for ( int i=0; i<3; i++ ) 
        	{
        		if ( i+11 != actual[i] )	// az első n fehér elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	for ( int i=9; i<12; i++ ) 
        	{
        		if ( i+12 != actual[i] )	// az első n fekete elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	break;
    	case 3: 
        	for ( int i=0; i<9; i++ ) 
        	{
        		if  ( i%2 == 1 )	// élközepet nem vizsgálunk
        			continue;
        		if ( i+11 != actual[i] )	// az első n fehér elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	for ( int i=9; i<18; i++ ) 
        	{
        		if  ( i%2 == 0 )	// élközepet nem vizsgálunk
        			continue;
        		if ( i+12 != actual[i] )	// az első n fekete elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	break;
    	case 4: 
        	for ( int i=0; i<9; i++ ) 
        	{
        		if  ( i>3 && i%2 == 1 )	// élközepet nem vizsgálunk
        			continue;
        		if ( i+11 != actual[i] )	// az első n fehér elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	for ( int i=9; i<18; i++ ) 
        	{
        		if  ( i>12 && i%2 == 0 )	// élközepet nem vizsgálunk
        			continue;
        		if ( i+12 != actual[i] )	// az első n fekete elem a helyén van-e
        			return false;				// ez még nem jó
        	}
        	break;
        }
        return true;
    }
    @Override
    public String getForgatasName(int prevStep)
    {
    	switch( prevStep )
        {
	    	case -1 : return "init";
	    	case 0 : return "jobb";
	    	case 1 : return "hátul";
	    	case 2 : return "bal";
	    	case 3 : return "elöl";
	    	case 4 : return "felül(1)";
	    	case 5 : return "felül(2)";
	    	case 6 : return "felül(3)";
	    	case 7 : return "(jf2jf2jf2)";
	    	default : return "nincs ilyen lépés";
        }
    }
	private static byte [] kiindulo = {
			11, 24, 13,
			12, 15, 18,
			21, 14, 19,
			
			27, 16, 23,
			28, 25, 26,
			17, 22, 29
	};
	
	private static byte [] fejtheto = { 
			11, 12, 23,
			26, 15, 14,
			13, 18, 21,
			
			19, 28, 27,
			24, 25, 16,
			17, 22, 29
	};

	private static byte [] csere = {
			11, 12, 13,
			16, 15, 18,
			17, 14, 19,
			
			21, 22, 23,
			24, 25, 26,
			27, 28, 29
	};

	private static byte [][] kockak = 
    {
        ready, fejtheto, kiindulo, csere
    };
	@Override
    public String toString()
    {
    	String s = "";
    	s += getId() + ":: " + getPrevStep() + ":: ";
    	s += toString(actual);
    	return s;
    }
	@Override
    public String toString( byte [] tabla )
    {
        String s="";
        String [] colors = getColors();
        int cntCol = getCntCol();
        for ( int i=0; i<tabla.length; i++ )
        {
        	int x = tabla[i];
            s += x+"";
            if ( (i+1)%2 == 0 )
                //s += "\n";
           		s += ", ";
           	else
                s += " ";
        }
        return s;
    }
	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
    	Domino game = new Domino( 1 );
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}