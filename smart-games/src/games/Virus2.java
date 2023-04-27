package games;

import java.util.Arrays;
import java.util.Vector;

import base.Gameable;

    // Virus2 logikai játék
	// Hasonló Virus játékhoz, csak itt nem átlósan, hanem az oldalakkal párhuzamosan lehet mozgatni a bábukat
public class Virus2 implements Gameable
{
    private static int cntRow = 5;
    private static int cntCol = 6;
    private static final int TARGET_POS = cntCol-1; // célpozíció a jobb felső sarok, a 1. bábut kell odajuttatni
    private static final int SPEC_BABU = 5;  // Speciális bábu, mert majdnem körülölel egy területet
    private static int size = cntCol * cntRow;
    private static int maxBabu = 6; // a legnagyobb (számú) bábu, amit mozgatunk 
    /* A pálya:
     * 00000T
     * 00000x
     * 00000x
     * 00000x
     * 00000x
     * T: (Target) Jobb felső mező, itt kell kijuttatni a 1. bábut
     * x: Ezek a mezők nem játszanak, csak töltelékek, hogy egy 5x6-as tömbbel tudjunk dolgozni
     * 0: Az aktív pozíciók
     */
    
    private int id=-1;
    
    private int [] actual;
    private static final int NOPLACE = 100;  // A mezők értéke, amik nem játszanak
    private int prevBabu;
    private int prevIrany;
    
    private Virus2( int [] stage, int prevBabu, int prevIrany )
    {
        initActual( stage );
        this.prevBabu = prevBabu;
        this.prevIrany = prevIrany;
    }
    public Virus2( int [] stage )
    {
        this( stage, -1, -1 );
    }
    public Virus2( int melyik )
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
    
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof Virus2))
    		return false;
    	
    	int [] stage = ((Virus2)game).getStage();
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
        if ( stage.length == 30 )   // Már létezőből klónozzuk
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
	        	if ( y==0 )	// az első sorban eggyel szélesebb a pálya
	        		x = ( x==cntCol-1 ? -1 : x+1 );
	        	else
	        		x = ( x==cntCol-2 ? -1 : x+1 );
	            break;
	        case 1: //up
	            y = ( y==0 ? -1 : y-1 );
	            break;
	        case 2: //left
	            x = ( x==0 ? -1 : x-1 );
	            break;
	        case 3: //down
	        	if ( y==0 && x==cntCol-1 )	// a jobb felső sarokból lefelé nem tud menni
	        		y=-1;
	        	else
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
        Virus2 game = null;
        for ( int babu=1; babu<=maxBabu; babu++)
        {
        	int masikBabu=0;
            int [] babuk = searchBabuk( babu );
            if ( babuk == null )      // nincs ilyen bábu a táblán
                continue;
            
/* Összeragasztás még nincs */
            if ( babu == SPEC_BABU )	// Ebben az esetben van olyan, hogy 2 bábut egyszerre lehet mozgatni
            {
        		int kozep = this.getMiddleSpec(babuk);
        		if (actual[kozep] > 0 && actual[kozep] < 9)
                {
        			masikBabu = actual[kozep];
                }
            }
/* */
        	
            for ( int irany=0; irany<4; irany++)
            {
                int [] nextStage = nextStage( babu, babuk, irany);    // lépjünk egyet
                if ( nextStage == null )
                {
                	if ( masikBabu > 0 )
                    {
                		ragasztas( ret, babuk, babu, masikBabu, irany );
                    }
            		continue;
                }
                game = new Virus2( nextStage, babu, irany );
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
        if ( actual[TARGET_POS] == 1 )
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
        int [] other = ((Virus2)hs).getActual();
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
            String c = "" + actual[i];
            if ( actual[i] == NOPLACE ) c = "x";
            if ( actual[i] == 10 ) c = "X";
            s += c;
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        return s;
    }
    	// Összeragadt bábuk esetén van még egy próbálkozás
	private void ragasztas( Vector<Gameable> g, int [] babuk, int babu, int masikBabu, int irany )
    {
		int osszetettBabu = ragasztBabu( actual, babu, masikBabu );
		int [] osszetettBabuk = searchBabuk( osszetettBabu );
		int [] nextStage = nextStage( osszetettBabu, osszetettBabuk, irany);    // lépjünk egyet
		szetszedBabu( actual, babuk, osszetettBabu );
		if ( nextStage == null )
			return;
		int [] eltolBabuk = origEltol( babuk, irany );
		szetszedBabu( nextStage, eltolBabuk, osszetettBabu );
		Virus2 game = new Virus2( nextStage, osszetettBabu, irany );
		g.add(game);
    }
    	// A speciális elem középső helyét adja vissza
		// xax
		// aca
		// xbx
    	// 'a'-k vannak megadva
    	// Először megkeressük a 4. elem ('b') helyét
    	// és 'c'-t adjuk vissza
    private int getMiddleSpec(int [] a )
    {
    	int ret;
    	if ( a[1]-a[0]==2 )		// a 2 felső elem egy sorban van
        {
    		ret = a[0]+1;
        }
    	else
        {
    		ret = a[0]+cntCol;
        }
    	return ret;
    }
    	// Összeragasztja a bábukat egy új számmal
    private int ragasztBabu( int [] a, int egyik, int masik )
    {
    	int ret = 10 * egyik + masik;
    	for ( int i=0; i<a.length; i++ )
        {
    		if ( a[i] == egyik || a[i] == masik )
    			a[i] = ret;
        }
    	return ret;
    }
    	// A spec bábu original helyét eltolja 'irany' irányban
    private int [] origEltol( int [] origSpec, int irany )
    {
    	int [] ret = new int [origSpec.length];
    	for ( int i=0; i<origSpec.length; i++ )
        {
    		if ( origSpec[i] == (-1) )
            {
    			ret[i] = -1;
    			break;
            }
    		int pos = nearPos( origSpec[i], irany );
    	    ret [i] = pos;
        }
    	return ret;
    }
    
    private void szetszedBabu( int [] a, int [] origSpec, int osszetett )
    {
    	int egyik = osszetett / 10;
    	int masik = osszetett % 10;
    	for ( int i=0; i<a.length; i++ )
        {
    		if ( a[i] == osszetett )
            {
    			boolean found = false;
    			for ( int j=0; j<origSpec[j]; j++ )
                {
    	    		if ( origSpec[j] == (-1) )
    	    			break;
    				if ( i == origSpec[j] )
                    {
        				a[i] = egyik;
        				found = true;
        				break;
                    }
                }
    			if ( !found )
    				a[i] = masik;
            }
        }
    }
    private static int [] table_0 = {  0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_1 = {  0,0,0,4,0,0,
    		3,0,4,4,0,0,
    		0,3,3,0,0,0,
    		0,0,1,1,9,0,
    		0,0,0,0,0,0 };
    private static int [] table_2 = {  0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_3 = {  0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_4 = {  0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_5 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_6 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_7 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_8 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_9 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_10 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_11 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_12 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_13 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_14 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_15 = { 0,0,0,0,6,0,
    		3,3,5,6,0,0,
    		0,0,3,5,6,0,
    		0,0,5,1,1,0,
    		0,0,0,0,0,0 };
    private static int [] table_16 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_17 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_18 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_19 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_20 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_21 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_22 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_23 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_24 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_25 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_26 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_27 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_28 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_29 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_30 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_31 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_32 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_33 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_34 = { 1,1,5,0,0,0,
    		0,5,0,0,0,0,
    		0,2,5,0,0,0,
    		2,0,0,9,9,0,
    		9,0,0,0,0,0 };
    private static int [] table_35 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_36 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_37 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_38 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_39 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_40 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_41 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_42 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_43 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_44 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_45 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_46 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_47 = { 0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0,
    		0,0,0,0,0,0 };
    private static int [] table_48 = { 9,0,6,0,6,0,
    		0,2,5,6,5,0,
    		1,1,2,5,0,0,
    		0,0,4,0,0,0,
    		9,4,4,0,9,0 };
    private static int [] table_49 = { 9,0,0,4,0,0,
    		0,0,5,4,4,0,
    		0,0,0,5,9,0,
    		0,2,5,1,1,0,
    		0,0,2,0,0,0 };
    private static int [] table_50 = { 0,0,0,4,4,0,
    		0,6,0,4,5,0,
    		0,0,6,5,0,0,
    		0,6,0,0,5,0,
    		9,9,1,1,9,0 };
    private static int [] table_51 = { 5,9,9,0,0,0,
    		9,5,2,0,0,0,
    		5,6,0,2,0,0,
    		6,0,6,4,0,0,
    		1,1,4,4,0,0 };
    private static int [] table_52 = { 0,9,2,0,0,0,
    		6,1,1,2,0,0,
    		0,6,5,0,0,0,
    		6,5,0,0,9,0,
    		0,0,5,0,0,0 };
    private static int [] table_53 = { 5,0,5,2,0,0,
    		0,5,2,0,0,0,
    		4,4,1,1,6,0,
    		4,3,0,6,0,0,
    		0,0,3,3,6,0 };
    private static int [] table_54 = { 6,0,6,5,0,0,
    		0,6,5,4,4,0,
    		0,0,3,6,4,0,
    		0,3,0,1,1,0,
    		0,3,0,0,0,0 };
    private static int [] table_55 = { 3,3,0,0,6,0,
    		0,4,3,6,0,0,
    		5,4,4,0,6,0,
    		0,5,0,1,1,0,
    		5,0,0,0,0,0 };
    private static int [] table_56 = { 0,9,0,4,4,0,
    		0,1,1,3,4,0,
    		0,0,0,3,5,0,
    		0,0,3,5,0,0,
    		0,0,0,0,5,0 };
    private static int [] table_57 = { 0,5,0,5,0,0,
    		1,1,5,0,9,0,
    		0,6,2,0,0,0,
    		6,0,0,2,0,0,
    		0,6,0,0,0,0 };
    private static int [] table_58 = { 0,0,5,0,0,0,
    		3,5,0,5,9,0,
    		0,3,3,0,0,0,
    		0,4,0,0,0,0,
    		4,4,1,1,0,0 };
    private static int [] table_59 = { 6,0,6,0,4,0,
    		9,6,2,4,4,0,
    		0,2,0,0,5,0,
    		0,1,1,5,0,0,
    		0,0,0,9,5,0 };
    private static int [] table_60 = {  6,0,5,0,5,0,
    		0,6,0,5,3,0,
    		6,2,0,0,3,0,
    		2,0,0,3,4,0,
    		1,1,0,4,4,0 };
    
    private static int [][] tables = 
    {
        table_0,
        table_1, table_2, table_3, table_4, table_5, table_6, table_7, table_8, table_9, table_10,
        table_11, table_12, table_13, table_14, table_15, table_16, table_17, table_18, table_19, table_20,
        table_21, table_22, table_23, table_24, table_25, table_26, table_27, table_28, table_29, table_30,
        table_31, table_32, table_33, table_34, table_35, table_36, table_37, table_38, table_39, table_40,
        table_41, table_42, table_43, table_44, table_45, table_46, table_47, table_48, table_49, table_50,
        table_51, table_52, table_53, table_54, table_55, table_56, table_57, table_58, table_59, table_60
    };
    
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Virus2 virus = new Virus2( 2 );
        System.out.println( virus );
        System.exit(0);
    }
}