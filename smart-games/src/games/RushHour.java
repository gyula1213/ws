package games;

import java.util.Arrays;
import java.util.Vector;

import base.Gameable;

    // A piros autónak ki kell menni a parkolóból
public class RushHour implements Gameable
{
    private static int cntRow = 6;
    private static int cntCol = 6;
    private static final int TARGET_BABU = 'X'; // Célbábú, amit ki kell vinni
    private static final int TARGET_POS = 3*cntCol-1; // célpozíció a 3. sor széle, a célbábut kell odajuttatni
    private static int size = cntCol * cntRow;
    private static int maxBabu = TARGET_BABU; // a legnagyobb (számú) bábu, amit mozgatunk 
    /* A pálya:
     * 000000
     * 000000
     * 00000T
     * 000000
     * 000000
     * 000000
     * T: (Target) 3. sor széle, itt kell kijuttatni a 1. bábut
     * 0: Az aktív pozíciók
     */
    
    private int id=-1;
    
    private int [] actual;
    private int prevBabu;
    private int prevIrany;
    
    private RushHour( int [] stage, int prevBabu, int prevIrany )
    {
        initActual( stage );
        this.prevBabu = prevBabu;
        this.prevIrany = prevIrany;
    }
    public RushHour( int [] stage )
    {
        this( stage, -1, -1 );
    }
    public RushHour( String strtable )
    {
        initActual( createTable( strtable ) );
        this.prevBabu = -1;
        this.prevIrany = -1;
    }
    public RushHour( int melyik )
    {
        this( str_tables[melyik] );
    }
    private int [] createTable( String str )
    {
    	StringBuffer sb = new StringBuffer(str);
    	int [] t = new int [sb.length()];
    	for ( int i=0; i<sb.length(); i++ )
    	{
    		char ch = sb.charAt(i);
    		if (ch==' ')
    			t[i] = 0;
    		else
    			t[i] = ch;
    	}
    	return t;
     }
    public int getId()
    {
        return id;
    }
    public String getPrevStep()
    {
    	String s = "" + (char)prevBabu + "-" + prevIrany;
        return s;
    }
    
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof RushHour))
    		return false;
    	
    	int [] stage = ((RushHour)game).getStage();
    	return Arrays.equals(stage, actual);
    	//return equals( stage, actual );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(actual);
    }
    private int [] getStage()
    {
        return actual;        
    }

    private void initActual( int [] stage )
    {
    	int x = stage.length;
        if ( stage.length == 36 )   // Már létezőből klónozzuk
        {
            actual = stage;
            return;
        }
    }
        // A 'pos' melletti mezőt adja vissza 'irany' irányban
    private int nearPos( int pos, int irany )
    {
        int ret = -1;   // ha nincs nearPos
        int x = pos%cntCol;
        int y = pos/cntCol;
        switch(irany)
        {
	        case 0: //right
	        	x = ( x==cntCol-1 ? -1 : x+1 );
	            break;
	        case 1: //up
	            y = ( y==0 ? -1 : y-1 );
	            break;
	        case 2: //left
	            x = ( x==0 ? -1 : x-1 );
	            break;
	        case 3: //down
        		y = ( y==cntRow-1 ? -1 : y+1 );
	            break;
        }
        if ( x==(-1) || y==(-1) )
            return (ret);
        ret = y * cntCol + x;            
        return ret;
    }
    
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        RushHour game = null;
        for ( int babu=1; babu<=maxBabu; babu++)
        {
            int [] babuk = searchBabuk( babu );
            if ( babuk == null )      // nincs ilyen bábu a táblán
                continue;
            
            for ( int irany=0; irany<4; irany++)
            {
                int [] nextStage = nextStage( babu, babuk, irany);    // lépjünk egyet
                if ( nextStage == null )
                {
            		continue;
                }
                game = new RushHour( nextStage, babu, irany );
                ret.add(game);
            }
        	
        }
        return ret;
    }
    	// Megkeresi az adott "bábu" összes elemét
    private int [] searchBabuk( int babu )
    {
        int [] ret = new int [8];
        int k=0;
        for ( int i=0; i<size; i++ )
        {
            if ( actual[i] == babu )
            {
                ret[k++] = i;
            }
        }
        if ( k == 0 )    // nincs ilyen bábu a táblán
            return null;
            
        ret[k] = -1;    // ez mindig a lezáróelem
        return ret;
    }
        // azt az állapotot adjuk vissza
        // amikor a 'pos' mezőn álló bábu 'irany' irányban lép egyet
        // ha ez nem lehetséges, akkor null
    private int [] nextStage( int babu, int [] babuk , int irany )
    {
        //System.out.println( "BABU> " + babu );
        int [] newBabuk = new int [babuk.length];
        int k=0;
        for ( int i=0; babuk[i] != -1; i++ )    // Megnézzük léphetünk-e
        {
        	if ( babuk[1] == babuk[0] + 1 )	// vízszintes a kocsi
        	{
        		if ( irany == 1 || irany == 3 )
        			return null;
        	}
        	else	// függőleges
        	{
        		if ( irany == 0 || irany == 2 )
        			return null;
        	}
            int pos = nearPos( babuk[i], irany );
            if ( pos == -1 || (actual[pos] > 0 && actual[pos] != babu) )      // itt már van valami, nem léphetünk oda
                return null;
            newBabuk[k++] = pos;                
        }
        newBabuk[k] = -1;

            // Szabad a pálya, lépünk
        int [] ret = (int [])actual.clone();
        for ( int i=0; babuk[i] != -1; i++ )
        {
            ret[babuk[i]] = 0;   // a régi helye üres lesz
        }
        for ( int i=0; newBabuk[i] != -1; i++ )
        {
            ret[newBabuk[i]] = babu;     // az újba bekerül
        }
        return ret;
    }
    public boolean isReady()
    {
        if ( actual[TARGET_POS] == TARGET_BABU )
            return true;
        else
            return false;
    }
    private int [] getActual()
    {
        return actual;        
    }

        // Visszatérés true, ha a két Virus ekvivalens
    public boolean equals(Gameable hs)
    {
        int [] other = ((RushHour)hs).getActual();
        for ( int i=0; i<size; i++ )
        {
            if ( actual[i] != other[i] )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;   // minden egyezik
    }
    public boolean isHopefulStage()
    {
    	return false;
    }
    public boolean isCriticalDeep( int deep )
    {
	    return (deep%10==0);	// pl. minden 10.
    }
    
    public String toString()
    {
        String s="";
        for ( int i=0; i<size; i++ )
        {
            char c = (char)(actual[i]);
            if (actual[i]==0)
            	c = ' ';
            s += c;
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        return s;
    }
    private static String strtable__0 =
    		"  O AA" + 
			"  O   " +
			"XXO   " +
			"PPP  Q" +
			"     Q" +
			"     Q"
    ;
    private static String strtable__1 =
    		"  O AA" + 
			"  O   " +
			"XXO   " +
			"PPP  Q" +
			"     Q" +
			"     Q"
	;
    private static String strtable__2 =
    		"  O   " + 
    		"  O  P" + 
    		"  OXXP" + 
    		"QQQA P" + 
    		"   ABB" + 
    		"      " 
	;
    private static String strtable__3 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__4 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__5 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__6 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__7 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__8 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable__9 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_10 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
    ;
    private static String strtable_11 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
	;
    private static String strtable_12 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_13 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_14 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_15 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_16 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_17 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_18 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_19 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_20 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
    ;
    private static String strtable_21 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
	;
    private static String strtable_22 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_23 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_24 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_25 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_26 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_27 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_28 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_29 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_30 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
    ;
    private static String strtable_31 =
    		"  O AA" + 
			"  O   " +
			"PPPP Q" +
			"     Q" +
			"     Q"
	;
    private static String strtable_32 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_33 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_34 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_35 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_36 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_37 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_38 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_39 =
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" + 
    		"" 
	;
    private static String strtable_40 =
    		"ABBOOO" + 
    		"A CCD " + 
    		"XXE D " + 
    		"FFEGGP" + 
    		" HHI P" + 
    		"QQQI P" 
	;
    
    private static String [] str_tables = 
    {
        strtable__0,
        strtable__1, strtable__2, strtable__3, strtable__4, strtable__5, strtable__6, strtable__7, strtable__8, strtable__9, strtable_10,
        strtable_11, strtable_12, strtable_13, strtable_14, strtable_15, strtable_16, strtable_17, strtable_18, strtable_19, strtable_20,
        strtable_21, strtable_22, strtable_23, strtable_24, strtable_25, strtable_26, strtable_27, strtable_28, strtable_29, strtable_30,
        strtable_31, strtable_32, strtable_33, strtable_34, strtable_35, strtable_36, strtable_37, strtable_38, strtable_39, strtable_40
    };
    
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        RushHour rushHour = new RushHour( 1 );
        System.out.println( rushHour );
        System.exit(0);
    }
}