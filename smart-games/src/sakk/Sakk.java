package sakk;

import java.awt.Point;
import java.util.Vector;

import base.Gameable;

    // Szoliter sakk, logikai játék
	// Egy 4x4-es táblán fent van néhány sakkbábú
	// Feladat az, hogy minden lépésben valamelyikkel lépve ütni kell, úgy hogy a végén egyetlen bábú maradjon
public class Sakk implements Gameable
{
    private static int cntCol = 4;
    /* A bábuk:
     * Király: 1
     * Vezér: 2
     * Bástya: 3
     * Futó: 4
     * Huszár: 5
     * Paraszt: 6
     */
    
    private int id=-1;
    
    private int [] actual;
    private String step;
    
    private Sakk( int [] stage, String step )
    {
    	actual = stage;
        this.step = step;
    }
    public Sakk( int [] stage )
    {
        this( stage, "init" );
    }
    public Sakk( int melyik )
    {
        this( tables[melyik] );
    }
    public int getId()
    {
        return id;
    }
    public String getPrevStep()
    {
        return step;
    }
    String getPosName(int pos)
    {
    	String betu="";
    	switch( pos%cntCol )
        {
    	case 0: betu = "a"; break;
    	case 1: betu = "b"; break;
    	case 2: betu = "c"; break;
    	case 3: betu = "d"; break;
        }
    	int szam = cntCol - pos/cntCol;
    	String s = betu + szam;
        return s;
    }
    String getStepName( int babu, int honnan, int hova )
    {
    	String s = "";
    	s += this.getBabuName(babu);
    	s += ":" + this.getPosName(honnan);
    	s += "-" + this.getPosName(hova);
    	return s;
    }
    
    	// Megmondja, hogy az adott bábu át tud-e menni az egyik helyről a másikra
    boolean canMove( int babu, int pos1, int pos2 )
    {
    	boolean ret=false;
    	int y1=pos1/cntCol;
    	int x1=pos1%cntCol;
    	int y2=pos2/cntCol;
    	int x2=pos2%cntCol;
        switch ( babu )
        {
        case 0:
        	break;
        case 1:
        	if ( Math.abs(y2-y1) <= 1 && Math.abs(x2-x1) <= 1 )
				ret = true;
        	break;
        case 2:
        	ret =  canMove(3,pos1,pos2) || canMove(4,pos1,pos2);
        	break;
        case 3:
        	if ( y2==y1 || x2==x1 )
				ret = true;
        	break;
        case 4:
        	if (  Math.abs(y2-y1) == Math.abs(x2-x1) )
				ret = true;
        	break;
        case 5:
        	if ( Math.abs(y2-y1) == 1 && Math.abs(x2-x1) == 2 )
				ret = true;
        	if ( Math.abs(y2-y1) == 2 && Math.abs(x2-x1) == 1 )
				ret = true;
        	break;
        case 6:
        	if ( (y1-y2) == 1 && Math.abs(x2-x1) == 1 )
				ret = true;
        	break;
        }
    	return ret;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        Sakk game = null;
        for ( int i=0; i<actual.length; i++)	// Végigmegy a táblán
        {
        	if ( actual[i] == 0 )	// itt nincs most bábu
        		continue;
        	
            for ( int j=0; j<actual.length; j++)
            {
            	if ( i==j )
            		continue;
            	if ( actual[j] == 0 )	// itt nincs most bábu
            		continue;
            	int babu=actual[i];
            	if ( canMove(babu,i,j) && !isAkadaly(babu,i,j))
                {
                    int [] nextStage = nextStage( i, j );    // lépjünk egyet
                    game = new Sakk( nextStage, getStepName(babu, i, j));
                    ret.add(game);
                }
            }
        }
        return ret;
    }
        // azt az állapotot adjuk vissza
        // amikor a 'pos' mezőn álló bábu 'irany' irányban lép egyet
        // ha ez nem lehetséges, akkor null
    private int [] nextStage( int honnan, int hova )
    {
            // Szabad a pálya, lépünk
        int [] ret = (int [])actual.clone();
        int babu = ret[honnan];   // ezzel lépünk
        ret[honnan] = 0;   // a régi helye üres lesz
        ret[hova] = babu;   // a bábu átkerül az új helyre
        return ret;
    }
    Point getPoint( int pos )
    {
    	return new Point( pos%cntCol, pos/cntCol );
    }

    boolean isAkadaly( int babu, int pos1, int pos2 )
    {
    	if ( babu == 0 || babu==1 || babu==5 || babu==6 )
    		return false;
        for ( int i=0; i<actual.length; i++)	// Végigmegy a táblán
        {
        	if ( i==pos1 || i==pos2 )
        		continue;
        	if ( actual[i] == 0 )	// itt nincs bábu, nem lehet akadály
        		continue;
        	if ( isBetween(getPoint(i), getPoint(pos1), getPoint(pos2)) ) 
        		return true;
        }
        return false;
    }

	private boolean isBetween(Point currPoint, Point point1, Point point2) {
		int dxc = currPoint.x - point1.x;
		int dyc = currPoint.y - point1.y;

		int dxl = point2.x - point1.x;
		int dyl = point2.y - point1.y;

		int cross = dxc * dyl - dyc * dxl;
		
		if (cross != 0)
			  return false;
		
		if (Math.abs(dxl) >= Math.abs(dyl)){
		  return dxl > 0 ? 
				    point1.x <= currPoint.x && currPoint.x <= point2.x :
				    point2.x <= currPoint.x && currPoint.x <= point1.x;
		} else {
		  return dyl > 0 ? 
				    point1.y <= currPoint.y && currPoint.y <= point2.y :
				    point2.y <= currPoint.y && currPoint.y <= point1.y;
		}
	}
    public boolean isReady()
    {
    	int cnt = 0;
    	for ( int i=0; i<actual.length; i++ )
        {
    		if ( actual[i] > 0 )
    			cnt++;
        }
        if ( cnt == 1 )
            return true;
        else
            return false;
    }

    public String getBabuName(int babu)
    {
        switch ( babu )
        {
        case 0:	return "-";
        case 1: return "K";
        case 2: return "V";
        case 3: return "B";
        case 4: return "F";
        case 5: return "H";
        case 6: return "P";
        }
        return "";
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
        for ( int i=0; i<actual.length; i++ )
        {
            s += getBabuName( actual[i] );
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        return s;
    }
    private static int [] table_0 = {
    		0,0,0,0,
    		0,0,0,0,
    		0,0,0,0,
    		0,0,0,0, };
    private static int [] table_1 = {
    		5,0,0,0,
    		0,4,0,0,
    		6,0,0,0,
    		0,0,0,0, };
    private static int [] table_2 = {
    		0,5,0,0,
    		0,0,4,0,
    		3,0,0,0,
    		0,0,0,0, };
    private static int [] table_3 = {
    		0,0,0,3,
    		5,0,6,0,
    		0,0,0,0,
    		5,0,0,6, };
    private static int [] table_4 = {
    		0,0,0,0,
    		0,3,6,0,
    		6,3,0,0,
    		0,0,0,0, };
    private static int [] table_5 = {
    		0,0,0,0,
    		0,0,3,0,
    		4,2,0,0,
    		0,1,0,0, };
    private static int [] table_6 = {
    		0,0,0,0,
    		0,3,4,0,
    		1,0,0,0,
    		5,0,0,0, };
    private static int [] table_7 = {
    		0,6,0,0,
    		0,0,0,0,
    		3,0,2,0,
    		0,4,0,0, };
    private static int [] table_8 = {
    		3,0,2,0,
    		0,0,0,0,
    		0,5,0,0,
    		0,0,6,0, };
    private static int [] table_9 = {
    		0,0,2,0,
    		0,0,6,0,
    		5,0,0,0,
    		0,6,0,0, };
    private static int [] table_10 = {
    		0,0,6,0,
    		0,0,5,0,
    		2,0,0,0,
    		0,4,0,0, };
    private static int [] table_11 = {
    		3,0,0,3,
    		0,0,6,0,
    		0,5,0,0,
    		0,0,0,0, };
    private static int [] table_12 = {
    		0,0,4,0,
    		0,2,0,0,
    		6,0,0,4,
    		0,0,0,0, };
    private static int [] table_13 = {
    		0,3,0,0,
    		0,1,0,0,
    		4,4,0,0,
    		0,0,3,0, };
    private static int [] table_14 = {
    		0,5,0,0,
    		0,0,2,3,
    		0,0,0,0,
    		4,0,0,0, };
    private static int [] table_15 = {
    		0,0,0,0,
    		0,4,0,0,
    		6,0,3,0,
    		5,0,0,0, };
    private static int [] table_16 = {
    		0,3,0,0,
    		0,2,0,0,
    		0,0,5,4,
    		3,0,0,0, };
    private static int [] table_17 = {
    		4,0,0,0,
    		0,3,6,0,
    		2,0,0,0,
    		0,6,0,0, };
    private static int [] table_18 = {
    		0,0,0,5,
    		2,0,0,0,
    		3,0,0,0,
    		4,0,4,0, };
    private static int [] table_19 = {
    		0,0,0,5,
    		0,5,0,0,
    		4,0,0,0,
    		4,6,0,0, };
    private static int [] table_20 = {
    		0,3,6,0,
    		3,4,0,0,
    		0,6,0,0,
    		0,0,0,0, };
    private static int [] table_21 = {
    		0,0,5,0,
    		2,0,4,0,
    		0,0,0,0,
    		6,5,0,0, };
    private static int [] table_22 = {
    		6,0,0,0,
    		0,2,0,0,
    		0,0,3,5,
    		5,0,0,0, };
    private static int [] table_23 = {
    		5,0,0,0,
    		3,0,2,0,
    		0,0,0,5,
    		4,0,0,0, };
    private static int [] table_24 = {
    		0,0,0,0,
    		0,3,0,5,
    		0,2,5,0,
    		3,0,0,0, };
    private static int [] table_25 = {
    		0,0,6,0,
    		0,0,0,0,
    		3,5,0,3,
    		0,6,0,0, };
    private static int [] table_26 = {
    		0,0,3,0,
    		4,4,0,0,
    		5,0,0,0,
    		0,0,5,0, };
    private static int [] table_27 = {
    		0,0,5,0,
    		4,0,0,0,
    		0,2,0,3,
    		3,0,0,0, };
    private static int [] table_28 = {
    		3,5,0,0,
    		0,0,0,3,
    		4,0,0,0,
    		0,4,0,0, };
    private static int [] table_29 = {
    		0,0,0,4,
    		0,0,0,5,
    		4,3,0,0,
    		0,0,5,0, };
    private static int [] table_30 = {
    		5,0,0,0,
    		0,0,3,5,
    		0,4,0,0,
    		3,0,0,0, };
    private static int [] table_31 = {
    		0,0,0,6,
    		3,1,2,0,
    		0,0,6,0,
    		0,0,3,0, };
    private static int [] table_32 = {
    		0,5,0,0,
    		4,0,5,0,
    		3,0,0,0,
    		2,0,0,6, };
    private static int [] table_33 = {
    		0,4,0,0,
    		5,0,0,0,
    		0,4,5,0,
    		6,6,0,0, };
    private static int [] table_34 = {
    		0,6,0,0,
    		0,2,0,0,
    		3,0,0,0,
    		3,0,5,4, };
    private static int [] table_35 = {
    		0,3,2,0,
    		0,0,0,6,
    		4,5,0,0,
    		6,0,0,0, };
    private static int [] table_36 = {
    		0,5,0,0,
    		0,6,4,4,
    		0,0,2,0,
    		3,0,0,0, };
    private static int [] table_37 = {
    		6,0,3,0,
    		0,0,3,0,
    		0,4,0,4,
    		0,0,6,0, };
    private static int [] table_38 = {
    		0,0,0,3,
    		0,5,0,4,
    		6,0,2,0,
    		0,0,3,0, };
    private static int [] table_39 = {
    		0,0,0,4,
    		5,0,0,0,
    		4,2,0,0,
    		0,3,5,0, };
    private static int [] table_40 = {
    		0,6,0,0,
    		0,2,0,3,
    		4,0,5,0,
    		0,0,0,6, };
    private static int [] table_41 = {
    		0,0,4,0,
    		5,0,0,6,
    		0,5,0,0,
    		0,6,4,0, };
    private static int [] table_42 = {
    		0,0,0,6,
    		3,0,0,0,
    		0,4,0,3,
    		4,0,6,0, };
    private static int [] table_43 = {
    		0,3,0,0,
    		4,0,3,5,
    		0,6,0,0,
    		0,4,0,0, };
    private static int [] table_44 = {
    		6,0,0,0,
    		3,0,6,0,
    		1,3,0,0,
    		5,4,0,4, };
    private static int [] table_45 = {
    		0,5,3,0,
    		5,0,0,0,
    		4,0,0,4,
    		0,0,6,0, };
    private static int [] table_46 = {
    		0,5,0,4,
    		0,0,0,0,
    		3,3,0,0,
    		5,0,4,0, };
    private static int [] table_47 = {
    		0,0,0,5,
    		0,0,5,0,
    		0,3,3,6,
    		6,4,0,0, };
    private static int [] table_48 = {
    		0,3,3,0,
    		0,4,0,6,
    		5,4,0,0,
    		5,0,0,0, };
    private static int [] table_49 = {
    		0,0,0,5,
    		0,0,0,3,
    		4,4,5,0,
    		3,6,6,0, };
    private static int [] table_50 = {
    		0,3,0,6,
    		5,3,0,0,
    		0,4,0,0,
    		0,4,5,0, };
    private static int [] table_51 = {
    		0,4,5,0,
    		0,4,6,0,
    		0,3,0,0,
    		6,5,0,0, };
    private static int [] table_52 = {
    		0,0,0,3,
    		4,1,5,0,
    		0,3,0,0,
    		6,4,6,0, };
    private static int [] table_53 = {
    		0,0,6,4,
    		0,3,3,0,
    		6,4,0,0,
    		5,5,0,0, };
    private static int [] table_54 = {
    		0,0,6,0,
    		0,0,0,0,
    		5,3,4,6,
    		0,3,5,0, };
    private static int [] table_55 = {
    		0,0,0,4,
    		0,0,0,3,
    		4,5,5,0,
    		6,6,3,0, };
    private static int [] table_56 = {
    		5,0,0,0,
    		4,6,0,4,
    		0,0,0,5,
    		3,3,6,0, };
    private static int [] table_57 = {
    		0,3,4,5,
    		0,5,0,6,
    		0,0,0,4,
    		6,3,0,0, };
    private static int [] table_58 = {
    		0,0,0,6,
    		5,0,0,0,
    		4,6,3,3,
    		5,4,0,0, };
    private static int [] table_59 = {
    		0,6,0,0,
    		0,3,3,0,
    		0,0,5,5,
    		4,0,6,4, };
    private static int [] table_60 = {
    		0,0,3,6,
    		5,4,3,0,
    		4,6,0,0,
    		0,0,5,0, };

    public static int [][] tables = 
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
        Sakk sakk = new Sakk( 2 );
        System.out.println( sakk );
        System.exit(0);
    }
}