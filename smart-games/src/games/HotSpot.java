package games;

import java.util.Arrays;
import java.util.Vector;

import base.GameController;
import base.Gameable;

// HotSpot logikai játék
public class HotSpot implements Gameable
{
    public static final int TARGET_POS = 0; //célpozíció a bal felső sarok
    private static int cntCol = 4;
    private static int cntRow = 4;
    private static int size = cntCol * cntRow;
    
    private int id=-1;
    
    private int [] actual;
    private int prevBabu;
    private int prevIrany;

    private HotSpot( int [] stage, int prevBabu, int prevIrany )
    {
        this.actual = stage;
        this.prevBabu = prevBabu;
        this.prevIrany = prevIrany;
    }
    public HotSpot( int [] stage )
    {
    	this( stage, -1, -1 );
        this.actual = stage;
    }
    public HotSpot( int melyik )
    {
        this( tables[melyik] );
    }
    public int getId()
    {
        return id;
    }
    public String getPrevStep()
    {
    	String s = "" + prevBabu + "-" + prevIrany;
        return s;
    }
        // A 'pos' melletti mezőt adja vissza 'irany' irányban
    private int nearPos( int pos, int irany )
    {
        int ret = -1;   // ha nincs nearPos
        switch(irany)
        {
            case 0: //right
                if ( pos/cntCol == (pos+1)/cntCol ) // ugyanaz a sor
                    ret = pos+1;
                break;
            case 1: //up
                if ( pos-cntCol >= 0 ) // még a táblán van
                    ret = pos-cntCol;
                break;
            case 2: //left
                if ( (pos-1)>=0 && pos/cntCol == (pos-1)/cntCol ) // ugyanaz a sor
                    ret = pos-1;
                break;
            case 3: //down
                if ( pos+cntCol < size ) // még a táblán van
                    ret = pos+cntCol;
                break;
        }
        return ret;
    }
    
/*
    public HotSpot next()
    {
        HotSpot hs = null;
        boolean found = false;
        int i=actBabu;
        int irany=actIrany;
        int initIrany = actIrany;
        for ( ; i<size; i++ )
        {
            for ( irany=initIrany; irany<4; irany++ )
            {
                if ( found )
                    break;

                int [] nextStage = nextStage( i, irany);    //lépjünk egyet
                if ( nextStage == null )
                    continue;
                //System.out.println();
                //System.out.println(toString(nextStage));
                hs = new HotSpot( nextStage );
                found = true;
                continue;   // még egyet lépünk
            }
            initIrany = 0;
            if ( found )
                break;
        }
        actBabu = i;    // megjegyezzuk, 
        actIrany = irany;   // hogy hol járunk
        return hs;
    }
        // azt az állapotot adjuk vissza
        // amikor a 'pos' mezőn álló bábu 'irany' irányban ugrik egyet
        // ha ez nem lehetséges, akkor null
*/
    private int [] nextStage( int pos, int irany )
    {
        if ( actual[pos] == 0 )      // nincs is itt b�bu
            return null;
        
        int near = nearPos( pos, irany );
        if ( near == (-1) || actual[near] == 0 ) //nincs mez�, vagy nincs ott semmi
            return null;
            
        int pos1 = nearPos( near, irany );  // a k�vetkez� mez�
        if ( pos1 == (-1) )     // nincs m�r
            return null;
        if ( actual[pos1] == 0 )     // ide lehet ugrani
            return newStage( pos, pos1 );

            // Két mezőn is volt bábu, megnézzük a következőt
        int pos2 = nearPos( pos1, irany );  // a következő mező
        if ( pos2 == (-1) )     // nincs már
            return null;
        if ( actual[pos2] == 0 )     // ide lehet ugrani
            return newStage( pos, pos2 );
        else            
            return null;
    }
        // új állapot, ha a bábu 'pos' mezőről 'newPos' mezőre ugrik
        // Null, ha a bábuk mérete miatt a lépés nem hajtható végre
    private int [] newStage( int pos, int newPos )
    {
        if ( isBig(actual[pos]) )    // ellenőrizni kell, ha a bábu nagyméretű
        {
            for ( int irany=0; irany<4; irany++ )   // megnézzük a szomszédait
            {
                int neighbour = nearPos( newPos, irany );
                if ( neighbour == (-1) )    // itt nincs is szomszéd
                    continue;
                if ( isBig( actual[neighbour]))  // ez bizony nagy, mégse léphet
                    return null;
            }
        }
        int [] ret = (int [])actual.clone();
        ret[pos] = 0;   // innen ugrik
        ret[newPos] = actual[pos];   // ide
        
        return ret;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        HotSpot game = null;
        for ( int babu=0; babu<size; babu++)
        {
            for ( int irany=0; irany<4; irany++)
            {
                int [] nextStage = nextStage( babu, irany);    // lépjünk egyet
                if ( nextStage == null )
            		continue;
                game = new HotSpot( nextStage, babu, irany );
                ret.add(game);
            }
        	
        }
        return ret;
    }
    public boolean isReady()
    {
        if ( actual[TARGET_POS] == 10)
            return true;
        else
            return false;
    }
        // true: Ha a bábu nagy méretű
    private boolean isBig(int babu)
    {
        if ( babu >= 6 )    // nagyméretű
            return true;    
        else    // üres, vagy kicsi
            return false;
    }
    private int [] getStage()
    {
        return actual;        
    }
        // Az azonos tipusú bábukat
        // egyenlőnek tekintjük
    private boolean equals( int babu1, int babu2 )
    {
        if ( babu1 == babu2 )   // üres, vagy pontosan ugyanaz a bábu
            return true;
        if ( babu1 >= 1 && babu1 <= 5 && babu2 >= 1 && babu2 <= 5 )  // kicsik
            return true;
        if ( babu1 >= 6 && babu2 <= 9 && babu2 >= 6 && babu2 <= 9 )  // nagyok
            return true;

        return false;       // nem egyformák            
    }
    
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof HotSpot))
    		return false;
    	
    	//int [] stage = ((HotSpot)game).getStage();
    	//return Arrays.equals(stage, actual);
    	return equals( (HotSpot)game );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(actual);
    }
    
        // Visszatérés true, ha a két HotSpot ekvivalens
    public boolean equals(Gameable hs)
    {
        int [] stage = ((HotSpot)hs).getStage();
        for ( int i=0; i<size; i++ )
        {
            if ( !equals(actual[i], stage[i] ) )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;   // ez egyezett
    }
    public String getLastStep()
    {
    	return "lastStep";
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
            s += actual[i]==10 ? "X" : actual[i]+"";
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        return s;
    }
    
    private static int [] table_0 = { 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static int [] table_1 = { 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 1, 0, 0, 0 };
    private static int [] table_2 = { 1, 0, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static int [] table_3 = { 0, 0, 0, 0, 0, 0, 2, 3, 1, 0, 0, 0, 10, 0, 0, 0 };
    private static int [] table_4 = { 2, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0 };
    private static int [] table_5 = { 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 10, 4, 5 };
    private static int [] table_6 = { 0, 0, 0, 0, 0, 1, 2, 0, 0, 3, 4, 0, 0, 0, 0, 10 };
    private static int [] table_7 = { 0, 2, 3, 10, 6, 0, 0, 0, 0, 0, 4, 5, 0, 0, 0, 0 };
    private static int [] table_8 = { 0, 0, 3, 0, 0, 0, 6, 4, 0, 5, 0, 0, 0, 10, 0, 0 };
    private static int [] table_9 = { 0, 0, 0, 0, 0, 0, 10, 0, 0, 7, 1, 0, 0, 4, 5, 0 };
    private static int [] table_10 = { 0, 10, 1, 0, 0, 3, 0, 0, 0, 0, 4, 0, 0, 5, 8, 0 };
    private static int [] table_11 = { 0, 0, 0, 0, 10, 0, 0, 0, 1, 0, 0, 0, 2, 3, 4, 0 };
    private static int [] table_12 = { 0, 0, 0, 1, 0, 3, 4, 0, 0, 5, 8, 0, 10, 0, 0, 0 };
    private static int [] table_13 = { 4, 6, 0, 0, 5, 0, 0, 0, 0, 7, 0, 0, 10, 0, 8, 0 };
    private static int [] table_14 = { 0, 6, 0, 3, 0, 0, 7, 0, 10, 0, 0, 8, 0, 9, 4, 5 };
    private static int [] table_15 = { 0, 0, 2, 0, 3, 10, 0, 0, 0, 0, 8, 4, 0, 5, 0, 0 };
    private static int [] table_16 = { 1, 0, 2, 0, 10, 0, 0, 0, 3, 0, 9, 0, 0, 5, 0, 0 };
    private static int [] table_17 = { 0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 10, 4, 5, 0 };
    private static int [] table_18 = { 0, 0, 1, 0, 0, 2, 0, 3, 0, 10, 0, 4, 0, 0, 5, 0 };
    private static int [] table_19 = { 0, 1, 0, 0, 0, 2, 10, 0, 0, 3, 4, 5, 0, 0, 0, 0 };
    private static int [] table_20 = { 0, 0, 0, 0, 0, 6, 3, 4, 0, 0, 7, 5, 0, 0, 0, 10 };
    private static int [] table_21 = { 0, 6, 1, 0, 10, 0, 0, 2, 3, 0, 0, 0, 0, 4, 0, 0 };
    private static int [] table_22 = { 0, 0, 0, 0, 0, 3, 0, 0, 0, 4, 5, 10, 9, 0, 0, 0 };
    private static int [] table_23 = { 3, 0, 0, 6, 0, 0, 10, 4, 0, 7, 0, 8, 0, 0, 9, 5 };
    private static int [] table_24 = { 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0, 5, 10 };
    private static int [] table_25 = { 0, 1, 0, 0, 10, 0, 0, 2, 4, 0, 0, 7, 0, 0, 5, 0 };
    private static int [] table_26 = { 0, 0, 1, 2, 0, 0, 0, 4, 0, 8, 0, 0, 0, 5, 0, 10 };
    private static int [] table_27 = { 0, 0, 0, 1, 0, 10, 2, 0, 0, 3, 9, 0, 5, 0, 0, 0 };
    private static int [] table_28 = { 1, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 4, 10, 0, 5, 0 };
    private static int [] table_29 = { 0, 0, 1, 10, 0, 0, 7, 2, 0, 0, 3, 8, 0, 0, 0, 0 };
    private static int [] table_30 = { 0, 0, 0, 1, 7, 2, 10, 0, 0, 8, 3, 0, 0, 0, 9, 0 };
    private static int [] table_31 = { 1, 0, 0, 6, 0, 10, 2, 0, 0, 3, 7, 0, 8, 0, 0, 4 };
    private static int [] table_32 = { 0, 0, 0, 0, 0, 3, 4, 0, 0, 10, 5, 0, 9, 0, 0, 0 };
    private static int [] table_33 = { 0, 1, 0, 0, 10, 0, 0, 2, 3, 0, 0, 0, 4, 0, 5, 0 };
    private static int [] table_34 = { 6, 0, 1, 8, 0, 7, 0, 0, 2, 3, 4, 9, 5, 10, 0, 0 };
    private static int [] table_35 = { 7, 0, 0, 8, 0, 0, 2, 0, 9, 4, 0, 0, 0, 10, 0, 5 };
    private static int [] table_36 = { 0, 6, 0, 0, 7, 1, 2, 0, 0, 0, 3, 8, 0, 0, 10, 0 };
    private static int [] table_37 = { 0, 1, 2, 0, 3, 10, 0, 0, 4, 0, 9, 0, 0, 0, 0, 0 };
    private static int [] table_38 = { 0, 0, 1, 4, 10, 0, 5, 6, 0, 0, 0, 0, 7, 0, 9, 0 };
    private static int [] table_39 = { 0, 10, 0, 0, 0, 3, 0, 4, 8, 0, 0, 0, 5, 0, 0, 0 };
    private static int [] table_40 = { 0, 0, 0, 0, 0, 10, 0, 6, 0, 0, 8, 1, 0, 9, 5, 2 };
    
    private static int [][] tables = 
    {
        table_0,
        table_1, table_2, table_3, table_4, table_5, table_6, table_7, table_8, table_9, table_10,
        table_11, table_12, table_13, table_14, table_15, table_16, table_17, table_18, table_19, table_20,
        table_21, table_22, table_23, table_24, table_25, table_26, table_27, table_28, table_29, table_30,
        table_31, table_32, table_33, table_34, table_35, table_36, table_37, table_38, table_39, table_40,
    };
    
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        HotSpot hs = new HotSpot( 3 );
        System.out.println(hs);
    	GameController gc = new GameController(hs); 
        System.out.println();
        System.out.println(hs);
        gc.solve();
        System.exit(0);
    }
}