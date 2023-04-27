package games;

import java.util.Vector;

import base.Gameable;

/**
 * 
 * @author slenk
 * Hdos -- http://www.kongregate.com:80/games/GameReclaim/hdos-databank-request-01
 *
 */
public class Hdos implements Gameable
{
    private static int cntCol = 6;
    private static int cntRow = 7;
    private static int size = cntCol * cntRow;
    
    private int id=-1;
    
    private int [] actual;
    private int prevPos1, prevPos2;

    private Hdos( int [] stage, int prevPos1, int prevPos2 )
    {
        this.actual = stage;
        this.prevPos1 = prevPos1;
        this.prevPos2 = prevPos2;
    }
    public Hdos( int [] stage )
    {
        this( stage, -1, -1 );
    }
    public Hdos( int melyik )
    {
        this( tables[melyik] );
    }
    public int getId()
    {
        return id;
    }
    public String getPrevStep()
    {
    	return "[" + prevPos1 + "," + prevPos2 + "]";
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
                if ( pos-cntCol >= 0 ) // m�g a t�bl�n van
                    ret = pos-cntCol;
                break;
        }
        return ret;
    }
        // A két pozíció tartalma helyet cserél
        // 'table' táblán
    private void csere( int [] table, int pos1, int pos2 )
    {
        int tmp = table[pos1];     // megjegyezzük
        table[pos1] = table[pos2];
        table[pos2] = tmp;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        Hdos game = null;
        for ( int pos1=0; pos1<size; pos1++ )
        {
            int pos2 = nearPos( pos1, 0 );
            if ( pos2 == (-1) ) //nincs mező
                continue;
        	
            int [] nextStage = nextStage( pos1, pos2);    // lépjünk egyet
            if ( nextStage == null )
                continue;
            game = new Hdos( nextStage, pos1, pos2 );
            ret.add(game);
        }
        return ret;
    }
        // azt az állapotot adjuk vissza
        // amikor a 'pos1' mezőn álló bábut  
        // kicseréljük a mellette levő 'pos2' mezőn álló bábuval
        // ha ez nem lehetséges, akkor null
    private int [] nextStage( int pos1, int pos2 )
    {
        if (   equals(actual[pos1], actual[pos2] ) )    // ugyanaz van, nem cserélünk
            return null;
        
        return newStage( pos1, pos2 );
    }
        // Új állapot, ha a bábu 'pos1' és 'pos2' mező tartalmát megcseréljük
    private int [] newStage( int pos1, int pos2 )
    {
        int [] ret = (int [])actual.clone();
        ret[pos1] = actual[pos2];
        ret[pos2] = actual[pos1];
        
        //System.out.println( "csere ---\n" + printStage(ret) );

        while( true )
        {
            boolean was = false;
            if ( leesik(ret) )
            {
                //System.out.println( "leesik ---\n" + printStage(ret) );
                was = true;
            }
            if ( eltunik(ret) )
            {
                //System.out.println( "eltunik ---\n" + printStage(ret) );
                was = true;
            }
            if (!was)
                break;
        }

        return ret;
    }
        // Ha egy bábu alatt nincs semmi, akkor lejjebb kerül
        // Visszatérés true, ha volt ilyen, false, ha nem
    public boolean leesik(int [] table )
    {
        boolean found = false;
        for ( int i=size-1; i>=cntCol; i--  )   // lentről nézzük, de a felső sort már nem
        {
            if ( table[i] > 0 )    // itt van valami, továbbmegyünk
                continue;
            int pos = i;
            int act = pos;
            while(true)                
            {
                int up = nearPos( act, 1 ); // felette levő
                if ( up == (-1) ) // kiértünk a tetejére
                    break;
                if ( table[up] == 0 )  // felette sincs semmi
                {
                    act = up;
                    continue;           // megyűnk tovább
                }
                found = true;
                csere( table, pos, up );   // up helyen levő leesik pos-ra
                break; // ezt a lukat betőltőttük
            }
        }
        return found;
    }
        // 3 egyforma alakzat, ami egymás mellett, vagy fölött van, eltűnik
        // Visszatérés true, ha volt ilyen, false, ha nem
    public boolean eltunik( int [] table )
    {
        int [] map = new int [size];
        boolean found = false;
        for ( int irany = 0; irany < 2; irany++ )   // először vízszintesen, aztán függőlegesen
        {                                           // megyünk végig a táblán
            for ( int i=0; i<size; i++  )
            {
                if ( table[i] == 0 )    // itt nincs semmi, továbbmegyünk
                    continue;
                int [] intoMap = new int [3];
                intoMap[0] = i;
                for(int x=1; x<=2; x++)      // még kettőt kell megnézni            
                {
                    int pos = nearPos( intoMap[x-1], irany ); // mellette/fölötte levő
                    if ( pos == (-1) ) // kiértünk a tábláról
                        break;
                    if ( !equals( table[i], table[pos]) )  // nem egyformák
                        break;
                    intoMap[x] = pos;                        
                }
                if ( intoMap[2] > 0 )   // Van 3 egyforma
                {
                    found = true;
                    for ( int x=0; x<3; x++ )
                    {
                        map[intoMap[x]] = 1;
                    }
                }
            }
        }
        if ( found )    // talált összefüggő alakzatot
        {
            for ( int i=0; i<size; i++ )
            {
                if ( map[i] > 0 )       // ahol találtunk
                {
                    table[i] = 0;      // eltüntetjük
                }
            }
        }
        return found;
    }
    public boolean isReady()
    {
        for ( int i=0; i<size; i++ )
        {
            if ( actual[i] > 0 )
                return false;
        }
        return true;
    }
    private int [] getStage()
    {
        return actual;        
    }
        // Az azonos tipusú bábukat
        // egyenlőnek tekintjük
    private boolean equals( int babu1, int babu2 )
    {
        if ( babu1/10 == babu2/10 )   // egyforma színű
            return true;

        return false;       // nem egyformák            
    }
    
        // Visszatérés true, ha a két Hdos ekvivalens
    public boolean equals(Gameable hs)
    {
        int [] stage = ((Hdos)hs).getStage();
        for ( int i=0; i<size; i++ )
        {
            if ( !equals(actual[i], stage[i] ) )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;   // ez egyezett
    }
    private String printStage( int [] stage )
    {
        String s="";
        for ( int i=0; i<size; i++ )
        {
            String act="";
            switch( stage[i]/10 )
            {
                case 0 : act = "."; break;
                case 1 : act = "A"; break;
                case 2 : act = "B"; break;
                case 3 : act = "C"; break;
                case 4 : act = "D"; break;
                case 5 : act = "E"; break;
                case 6 : act = "F"; break;
                default : act = "?"; break;
            }
            s += act;
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        return s;
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
        //s += "lep: " + stepPos + "\n";
        //s += "lep: " + actBabu + "\n";
        s += printStage( actual );
        return s;
    }
    
    private static int [] table_0 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0  };
    private static int [] table_1 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 11, 0, 12, 13, 0  };
    private static int [] table_2 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 23, 0, 0, 0,
                                      0, 0, 22, 0, 0, 0,
                                      0, 0, 11, 21, 12, 13  };
    private static int [] table_3 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 21, 0, 0,
                                      0, 0, 22, 12, 0, 0,
                                      0, 0, 13, 23, 0, 0  };
    private static int [] table_4 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 0, 0, 21, 0, 0,
                                      0, 0, 0, 22, 0, 0,
                                      0, 0, 0, 12, 0, 0,
                                      0, 13, 0, 23, 14, 0  };
    private static int [] table_5 = { 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 0, 0, 0,
                                      0, 12, 21, 31, 0, 0,
                                      0, 32, 13, 22, 0, 0,
                                      0, 33, 14, 23, 0, 0  };
    private static int [] table_6 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 15, 25, 16, 26, 0,
							            0, 23, 13, 24, 14, 0,
							            0, 11, 21, 12, 22, 0  };
    private static int [] table_7 = { 0, 0, 0, 0, 0, 0,
    								  0, 0, 16, 0, 0, 0,
						              0, 0, 33, 0, 0, 0,
						              0, 0, 15, 0, 0, 0,
						              0, 23, 13, 14, 0, 0,
						              0, 31, 32, 22, 0, 0,
						              0, 11, 21, 12, 0, 0 };
    private static int [] table_8 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 32, 33, 0, 0,
							            0, 0, 13, 23, 0, 0,
							            0, 0, 31, 12, 0, 0,
							            0, 11, 21, 22, 0, 0  };
    private static int [] table_9 = {   0, 0, 0, 0, 0, 0,
							            0, 0, 24, 0, 0, 0,
							            0, 0, 13, 0, 0, 0,
							            0, 0, 12, 0, 0, 0,
							            0, 0, 33, 0, 0, 0,
							            0, 0, 31, 32, 23, 0,
							            0, 0, 11, 21, 22, 0  };
    private static int [] table_10 = {  0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 22, 23, 0, 0,
							            0, 0, 13, 32, 33, 0,
							            0, 0, 11, 21, 12, 31  };
    private static int [] table_11 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 0, 12, 21, 0, 0,
                                      0, 0, 31, 22, 0, 0,
                                      0, 0, 13, 32, 0, 0,
                                      0, 0, 23, 33, 0, 0,
                                      14, 15, 34, 24, 0, 0  };
    private static int [] table_12 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 33, 0, 0, 0,
							            0, 0, 32, 13, 0, 0,
							            0, 0, 22, 23, 31, 0,
							            0, 0, 11, 21, 12, 0  };
    private static int [] table_13 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 0, 0, 0,
                                      0, 0, 21, 12, 0, 0,
                                      0, 0, 13, 22, 0, 0,
                                      0, 0, 31, 32, 0, 0,
                                      0, 0, 23, 41, 0, 0,
                                      0, 33, 42, 43, 0, 0  };
    private static int [] table_14 = { 0, 0, 0, 0, 27, 0,
							            0, 0, 0, 0, 26, 0,
							            0, 0, 0, 0, 43, 0,
							            0, 0, 0, 42, 25, 0,
							            0, 0, 41, 33, 13, 0,
							            0, 31, 32, 23, 24, 0,
							            0, 11, 21, 22, 12, 0  };
    private static int [] table_15 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 15, 0, 0,
							            0, 0, 0, 24, 0, 0,
							            0, 0, 0, 23, 0, 0,
							            0, 0, 0, 33, 0, 0,
							            0, 31, 32, 22, 0, 0,
							            0, 11, 12, 21, 13, 14  };
    private static int [] table_16 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 12, 0,
                                      0, 0, 21, 31, 13, 0,
                                      0, 0, 22, 41, 23, 0,
                                      0, 42, 43, 32, 33, 0  };
    private static int [] table_17 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 0, 0, 0,
                                      0, 0, 21, 12, 0, 0,
                                      0, 0, 31, 22, 0, 0,
                                      0, 0, 32, 41, 0, 0,
                                      0, 0, 42, 33, 0, 0,
                                      0, 0, 13, 43, 23, 24  };
    private static int [] table_18 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 12, 21, 0,
                                      0, 0, 13, 31, 41, 0,
                                      0, 0, 32, 42, 43, 0,
                                      0, 0, 44, 22, 23, 0,
                                      0, 0, 33, 45, 46, 0  };
    private static int [] table_19 = { 0, 0, 0, 0, 0, 0,
							            23, 0, 24, 0, 25, 0,
							            15, 0, 33, 0, 16, 0,
							            42, 0, 32, 0, 43, 0,
							            13, 0, 53, 0, 14, 0,
							            41, 0, 51, 0, 52, 0,
							            11, 21, 31, 22, 12, 0  };
    private static int [] table_20 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 0, 21, 22, 0, 0,
                                      0, 0, 31, 41, 0, 0,
                                      0, 0, 32, 12, 0, 0,
                                      0, 13, 42, 23, 0, 0,
                                      0, 24, 43, 33, 0, 0  };
    private static int [] table_21 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 0, 0, 0,
                                      0, 0, 21, 0, 0, 0,
                                      0, 22, 31, 0, 0, 0,
                                      0, 41, 42, 0, 0, 0,
                                      0, 32, 12, 23, 0, 0,
                                      0, 43, 33, 13, 0, 0  };
    private static int [] table_22 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 0, 15, 36, 16, 0,
							            32, 33, 13, 34, 35, 14,
						                11, 21, 22, 12, 23, 31  };
    private static int [] table_23 = { 0, 0, 0, 0, 0, 0,
							            0, 0, 0, 0, 0, 0,
							            0, 33, 15, 0, 0, 0,
							            0, 32, 23, 0, 0, 0,
							            0, 22, 43, 0, 0, 0,
							            0, 14, 31, 41, 42, 0,
							            0, 11, 21, 12, 13, 0  };
    private static int [] table_24 ={ 0, 11, 21, 0, 0, 0,
                                      31, 41, 22, 0, 0, 0,
                                      42, 51, 43, 0, 0, 0,
                                      23, 44, 24, 0, 0, 0,
                                      45, 12, 52, 13, 0, 0,
                                      46, 32, 33, 25, 0, 0,
                                      26, 27, 53, 28, 0, 0  };
    private static int [] table_25 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 11, 0,
                                      0, 0, 0, 12, 21, 0,
                                      0, 0, 31, 13, 22, 0,
                                      0, 0, 41, 23, 14, 0,
                                      0, 32, 42, 15, 24, 0,
                                      0, 43, 33, 16, 34, 0  };
    private static int [] table_26 ={ 0, 11, 0, 0, 0, 0,
                                      0, 21, 22, 0, 0, 0,
                                      0, 31, 12, 0, 0, 0,
                                      0, 32, 23, 0, 0, 0,
                                      0, 41, 42, 0, 0, 0,
                                      0, 33, 34, 24, 0, 0,
                                      0, 43, 13, 25, 26, 0  };
    private static int [] table_27 ={ 0, 0, 0, 0, 0, 0,
                                      0, 11, 21, 0, 0, 0,
                                      0, 31, 41, 0, 0, 0,
                                      0, 32, 33, 0, 0, 0,
                                      0, 42, 43, 0, 0, 0,
                                      0, 12, 22, 0, 0, 0,
                                      0, 34, 13, 14, 23, 0  };
    private static int [] table_28 ={ 0, 0, 0, 0, 11, 0,
                                      0, 0, 0, 0, 21, 0,
                                      0, 22, 0, 0, 31, 0,
                                      0, 12, 0, 23, 32, 0,
                                      0, 13, 33, 41, 16, 0,
                                      0, 42, 43, 17, 24, 0,
                                      0, 18, 25, 26, 34, 0  };
    private static int [] table_29 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 21, 22, 31, 0, 0,
                                      0, 12, 13, 23, 0, 0,
                                      0, 41, 14, 32, 0, 0,
                                      0, 15, 42, 43, 33, 16  };
    private static int [] table_30 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 15, 16, 32, 33, 0,
                                      0, 22, 31, 14, 23, 0,
                                      0, 11, 21, 12, 13, 0  };
    private static int [] table_31 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 36, 0, 0,
                                      0, 0, 0, 23, 0, 0,
                                      0, 0, 0, 35, 0, 0,
                                      0, 0, 0, 34, 0, 0,
                                      0, 32, 33, 13, 0, 0,
                                      11, 12, 21, 31, 22, 0  };
    private static int [] table_32 ={ 0, 0, 0, 16, 0, 0,
                                      0, 0, 0, 33, 0, 0,
                                      0, 0, 0, 23, 0, 0,
                                      0, 0, 0, 15, 0, 0,
                                      0, 0, 14, 22, 0, 0,
                                      0, 31, 32, 13, 0, 0,
                                      0, 11, 12, 21, 0, 0  };
    private static int [] table_33 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 0, 12, 21, 0, 0,
                                      0, 13, 22, 31, 23, 0,
                                      0, 32, 33, 14, 24, 0,
                                      15, 25, 26, 16, 17, 0  };
    private static int [] table_34 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 46, 0,
                                      0, 0, 44, 45, 13, 0,
                                      0, 0, 42, 43, 33, 0,
                                      0, 41, 22, 32, 23, 0,
                                      0, 11, 21, 31, 12, 0  };
    private static int [] table_35 ={ 0, 0, 0, 0, 0, 0,
                                      0, 0, 11, 0, 0, 0,
                                      0, 0, 21, 0, 0, 0,
                                      0, 0, 31, 0, 0, 0,
                                      0, 0, 22, 32, 0, 0,
                                      0, 0, 33, 12, 34, 0,
                                      0, 0, 23, 35, 13, 36  };
    private static int [] table_x ={ 51, 0, 0, 0, 0, 0,
                                      0, 0, 0, 11, 0, 0,
                                      0, 0, 12, 21, 0, 53,
                                      52, 0, 0, 22, 0, 0,
                                      0, 0, 13, 32, 0, 0,
                                      0, 0, 23, 33, 0, 0,
                                      14, 15, 34, 0, 0, 0  };
    private static int [] table_y = { 41, 42, 51, 43, 44, 0,
                                      0, 0, 0, 0, 21, 0,
                                      0, 0, 31, 24, 22, 25,
                                      34, 33, 32, 0, 23, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 0, 0, 0, 0, 0,
                                      0, 11, 14, 12, 13, 0  };
    
    private static int [][] tables = 
    {
        table_0,
        table_1,
        table_2,
        table_3,
        table_4,
        table_5,
    table_6,
    table_7,
    table_8,
    table_9,
    table_10,
        table_11,
    table_12,
    table_13,
    table_14,
    table_15,
    table_16,
        table_17,
        table_18,
    table_19,
    table_20,
    table_21,
    table_22,
    table_23,
    table_24,
    table_25,
    table_26,
    table_27,
    table_28,
    table_29,
    table_30,
    table_31,
    table_32,
    table_33,
    table_34,
    table_35,
        table_x,
        table_y,
    };
    
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Hdos hdos = new Hdos( 9 );
        System.out.println(hdos);
        hdos.leesik(hdos.getStage());
        System.out.println("leesik\n\n" + hdos);
        hdos.eltunik(hdos.getStage());
        System.out.println("eltunik\n\n" + hdos);
        
        System.exit(0);
    }
}