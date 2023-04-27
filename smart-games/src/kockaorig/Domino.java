package kockaorig;

import java.util.Arrays;
import java.util.Vector;

import base.GameController;
import base.Gameable;

//Bűvös dominó kirakása
public class Domino implements Gameable
{
    private int prevStep;

    private static int idSzamlalo=0;
	private int id;
	
    private int cntCol = 2;
    
    private int [] actual;
    
    private Domino( int [] stage, int prevStep )
    {
    	this.prevStep = prevStep;
        this.actual = stage;
        id = idSzamlalo++;
    }
    public Domino( int [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Domino( int melyik )
    {
    	this( kockak[melyik] );
    }
    public int getId()
    {
        return id;
    }
    int [] getActual()
    {
        return this.actual;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
    	
        Domino game = null;
        for ( int i=0; i<forgatasok.length; i++ )
        {
            //System.out.println("Actual:\n" + toString(actual));
            int [] nextStage = forgat( actual, i);    //lépjünk egyet
            if ( nextStage == null )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            if ( equals( actual, nextStage))
            	continue;
            game = new Domino( nextStage, i );
            ret.add(game);
        }
        return ret;
    }
    /*
     * Az első 9 elem a felső lap
     * a második 9, az alsó
     * lapon belül a számozás mindig a bal felső sarokból indul (az alsó lap így tükörképe lett a felsőnek)
     */
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
	private static int [] jf2jf2jf2 = {
			0, 7, 2,
			3, 4, 5,
			6, 1, 8,
			
			9, 10, 11,
			12, 13, 14,
			15, 16, 17,
	};
	private static int [] jbfjhjfjhb = {
			0, 12, 2,
			3, 4, 5,
			6, 1, 8,
			
			9, 7, 11,
			14, 13, 10,
			15, 16, 17,
	};
	// azt az állapotot adjuk vissza
	// amikor adott 'lepes' szerint forgatjuk a kockát
    // 0: jobb, 1: hátsó, 2: bal, 3: első, 4: felső
	private static int [][] forgatasok = 
	{
		jobb, hatul, bal, elol, felul1, felul2, felul3, jf2jf2jf2 //, jbfjhjfjhb
	};
	static int [] forgat( int [] param, int lepes )
	{
        int [] ret = new int [param.length];
        int [] forog = forgatasok[lepes];
        for (int i=0; i<param.length; i++)	// lemásoljuk
        {
        	ret[i] = param[forog[i]];
        }
	    return ret;
	}
	// 0. Cél: Teljesen kész
    // 1. Cél: Felül fehér, alul fekete
	// 2. Cél: Fehér 1,2,3
	// 3. Cél: Csak a sarkok
	// 4. Cél: A sarkok és fehér 1,2,3,4
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
    private int [] getStage()
    {
        return actual;        
    }

    
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof Domino))
    		return false;
    	
    	int [] stage = ((Domino)game).getStage();
    	return equals( stage, actual );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(actual);
    }
    
    // Visszatérés true, ha a két tábla ugyanaz
    private boolean equals(int [] t1, int [] t2 )
    {
        for ( int i=0; i<t1.length; i++ )
        {
            if ( t1[i] != t2[i] )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;   // ez egyezett
    }
    public String getPrevStep()
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
	    	case 8 : return "(jbfjhjfjhb)";
	    	default : return "nincs ilyen lépés";
        }
    }
    /**
     * Megmondja, hogy ez az állapot a végső megfejtés szemőpontjából
     * sikerrel kecsegtet-e. megtartsuk-e a későbbiekre
     * A mintafeladatban nem tekintünk semmit igéretesnek
     */
    public boolean isHopefulStage()
    {
    	return false;
    }
    /**
    * Megmondja, hogy az adott mélység kritikus-e
    * (általában empírikus úton derül ki)
     * A mintafeladatban 10-et veszünk, de ez nyilván bármennyire átírható
     * Azt kell beírni, mileőtt a gép "kifekszik" :)
    * @return
    */
   public boolean isCriticalDeep( int deep )
   {
	   return (deep%10==0);	// pl. minden 10.
   }
    
    public String toString()
    {
    	String s = "";
    	s += getId() + ":: " + getPrevStep() + ":: ";
    	s += toString(actual);
    	return s;
    }
    private String toString( int [] tabla )
    {
        String s="";
        for ( int i=0; i<tabla.length; i++ )
        {
            s += tabla[i]+"";
            if ( (i+1)%cntCol == 0 )
                //s += "\n";
            	s += ", ";
            else
                s += " ";
        }
        return s;
    }
	private static int [] ready = {
			11, 12, 13,
			14, 15, 16,
			17, 18, 19,
			
			21, 22, 23,
			24, 25, 26,
			27, 28, 29
	};
	private static int [] kocka = {
			11, 24, 13,
			12, 15, 18,
			21, 14, 19,
			
			27, 16, 23,
			28, 25, 26,
			17, 22, 29
	};
	
	private static int [] y = {
			11, 22, 13,
			14, 15, 16,
			17, 18, 19,
			
			21, 12, 23,
			24, 25, 26,
			27, 28, 29
	};

	private static int [] x = {
			11, 12, 13,
			16, 15, 18,
			17, 14, 19,
			
			21, 22, 23,
			24, 25, 26,
			27, 28, 29
	};

	private static int [][] kockak = 
    {
        ready, kocka, y, x
    };

	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
    	Domino game = new Domino( 2 );
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}