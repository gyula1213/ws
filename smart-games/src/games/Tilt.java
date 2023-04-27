package games;

import java.util.Arrays;
import java.util.Vector;

import base.Gameable;

// HotSpot logikaiáték
public class Tilt implements Gameable
{
	private Vector<Tilt> history;
    private int prevStep;

    private static int idSzamlalo=0;
	private int id;
	
    private int cntCol = 5;
    private int size;
    private int [] iranyok = { 1, -cntCol, -1, cntCol };	// jobbra, fel, balra, le
    
    private int [] actual;
    
    private static final int _EMPTY = 0;
    private static final int _GREEN = 1;
    private static final int _BLUE = 2;
    private static final int _FIXED = 3;
    private static final int _HOLE = 9;

    private Tilt( Vector<Tilt> history, int [] stage, int prevStep )
    {
    	this.history = new Vector<>(history);
    	this.history.add(this);
    	this.prevStep = prevStep;
        this.actual = stage;
        size = stage.length;
        id = idSzamlalo++;
    }
    public Tilt( int [] stage )
    {
    	this( new Vector<Tilt>(), stage, -1 );
    	idSzamlalo=1;
    }
    public Tilt( int melyik )
    {
        this( tablak[melyik] );
    }
    public int getId()
    {
        return id;
    }
    public void setCol(int col)
    {
    	this.cntCol = col;
    	iranyok[1] = -col;	// fel
    	iranyok[3] = col;	// le
    }

/*
    public Tilt next()
    {
        Tilt game = null;
        int irany;
        int initIrany = actIrany+1;
        for ( irany=initIrany; irany<4; irany++ )
        {
            //System.out.println("Actual:\n" + toString(actual));
            int [] nextStage = nextStage( actual, iranyok[irany]);    //lépjünk egyet
            if ( nextStage == null )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            if ( equals( actual, nextStage))
            	continue;
            lastStep = irany;
            game = new Tilt( nextStage, false );
            game.setCol(cntCol);	// öröklődik
            break;
        }
        actIrany = irany;   // hogy hol járunk
        return game;
    }
*/
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
    	
        Tilt game = null;
        for ( int i=0; i<4; i++ )
        {
            //System.out.println("Actual:\n" + toString(actual));
            int [] nextStage = nextStage( actual, iranyok[i]);    //lépjünk egyet
            if ( nextStage == null )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            if ( equals( actual, nextStage))
            	continue;
            game = new Tilt( history, nextStage, i );
            game.setCol(cntCol);	// öröklődik
            ret.add(game);
        }
        return ret;
    }
        // azt az állapotot adjuk vissza
        // amikor adoot irányban mozgatjuk az elemeket
        // ha ez nem lehetséges, akkor null
    private int [] nextStage( int [] param, int irany )
    {
        int [] ret = new int [size];
        int [] tmp = new int [size];
        for (int i=0; i<param.length; i++)	// lemásoljuk
        {
        	tmp[i] = param[i];
        }

        boolean wasChanged = false;
        int act, next;
        for (int i=0; i<tmp.length; i++)
        {
            act = getAct( irany, i );
              // Helyben maradó elemek
            switch (tmp[act])
            {
				case _EMPTY :
				case _HOLE :
				case _FIXED :
					ret[act] = tmp[act];	// simán átmásoljuk
					continue;
            }
	            // Tábla szélén nem megy tovább
            if (isTablaSzele( irany, act ))
            {
				ret[act] = tmp[act];
				continue;
            }
            next = getNext( irany, act);
            //System.out.println("Next: "+next);
            	// Most lép, ha tud
            switch (tmp[next])
            {
              case _HOLE :    // Az elem a lyukba kerül
                if ( tmp[act] == _BLUE )	// leesett a kék, vége
                    return null;
                else // Ha a zöld esett le, akkor még további mozgások lehetnek
                {
                    ret[act] = 0;
                    tmp[act] = 0;		// TODO ez minek?
                    wasChanged = true;
                }
                break;
              case _EMPTY :
            	ret[next] = tmp[act];
                ret[act] = 0;
                tmp[act] = 0;		// TODO ez minek?
                wasChanged = true;
                break;
              default :
            	ret[act] = tmp[act];	// TODO ez minek?
                break;
            }
        }
        if ( wasChanged )
        	return nextStage( ret, irany);
        else
    	    return ret;
    }
    
	private int getAct( int irany, int ind )
	{
		if ( irany < 0 )
			return ind;
		else	// jobbra, le esetén fordítva megyünk a tömbön
			return size-ind-1;
	}
	
	private int getNext( int irany, int ind )
	{
		return ind + irany;
	}
	
	private boolean isTablaSzele( int irany, int ind )
	{
		if ( irany == 1 && (ind+1)%cntCol == 0 ) // jobbra
			return true;
		if ( irany == -1 && ind%cntCol == 0 ) 	// balra
			return true;
		if (  irany == cntCol && ind >= size - cntCol )	// le
			return true;
		if (  irany == -cntCol && ind < cntCol ) 	// fel
			return true;
		return false;	// minden egyéb esetben
	}

    // Kész, ha már nincs zöld a táblán
    public boolean isReady()
    {
    	for ( int i=0; i<actual.length; i++ ) 
    	{
            if ( actual[i] == _GREEN)
                return false;
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
    	if (!(game instanceof Tilt))
    		return false;
    	
    	int [] stage = ((Tilt)game).getStage();
    	return Arrays.equals(stage, actual);
    	//return equals( stage, actual );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(actual);
    }
    
    // Visszatérés true, ha a két állás ugyanaz
    public boolean equals(Gameable game)
    {
        int [] stage = ((Tilt)game).getStage();
        	return equals( stage, actual );
    }
    // Visszatérés true, ha a két tábla ugyanaz
    private boolean equals(int [] t1, int [] t2 )
    {
        for ( int i=0; i<size; i++ )
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
	    	case 0 : return "jobbra";
	    	case 1 : return "fel";
	    	case 2 : return "balra";
	    	case 3 : return "le";
	    	case 4 : return "nostep";
	    	default : return "nincs ilyen lépés";
        }
    }
    public boolean isHopefulStage()
    {
    	return false;
    }
    public boolean isCriticalDeep( int deep )
    {
	    return (deep%30==0);	// pl. minden 10.
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
        for ( int i=0; i<size; i++ )
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
    public String getHistory()
    {
    	String s = "";
    	for ( int i=0; i<history.size(); i++ )
        {
    		Tilt t = history.get(i);
        	s += t.getPrevStep() + "\n";
        	//s += getId() + "\n";
        }
    	return s;
    }
    
	private static int [] tabla0  = { 0, 3, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 1, 0, 0 };

	private static int [] tabla1  = { 1, 3, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 3, 0, 0 };

	private static int [] tabla2  = { 3, 1, 2, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla3  = { 2, 1, 3, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			2, 1, 0, 0, 0 };

	private static int [] tabla4  = { 3, 0, 0, 0, 1,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			2, 0, 0, 0, 1 };

	private static int [] tabla5  = { 2, 1, 2, 0, 0,
			2, 3, 1, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla6  = { 0, 3, 2, 0, 1,
			0, 3, 0, 0, 0,
			3, 3, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 1 };

	private static int [] tabla7  = { 3, 0, 3, 3, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			1, 0, 0, 0, 0,
			2, 0, 0, 0, 0 };

	private static int [] tabla8  = { 1, 0, 3, 0, 0,
			1, 3, 3, 0, 0,
			2, 3, 9, 0, 0,
			2, 0, 0, 0, 0,
			2, 0, 0, 0, 0 };

	private static int [] tabla9  = { 0, 0, 3, 2, 2,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			1, 2, 0, 0, 0 };

	private static int [] tabla10 = { 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 2, 2,
			0, 0, 3, 3, 3,
			0, 0, 0, 2, 1 };

	private static int [] tabla11 = { 2, 3, 0, 0, 0,
			1, 1, 3, 0, 3,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla12 = { 0, 3, 0, 0, 0,
			0, 1, 3, 1, 0,
			0, 0, 9, 3, 0,
			0, 0, 0, 0, 0,
			0, 3, 0, 0, 0 };

	private static int [] tabla13 = { 3, 3, 3, 0, 0,
			0, 3, 3, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			1, 1, 0, 0, 2 };

	private static int [] tabla14 = { 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 3, 3, 0,
			0, 0, 2, 1, 3 };

	private static int [] tabla15 = { 3, 2, 0, 0, 2,
			0, 0, 0, 0, 1,
			0, 0, 9, 0, 3,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla16 = { 3, 0, 0, 3, 0,
			1, 0, 3, 0, 0,
			2, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla17 = { 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 3, 9, 0, 0,
			0, 0, 0, 0, 0,
			3, 1, 2, 0, 0 };

	private static int [] tabla18 = { 2, 3, 0, 0, 0,
			2, 0, 0, 3, 0,
			3, 3, 9, 3, 0,
			1, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla19 = { 0, 0, 3, 1, 1,
			0, 0, 2, 2, 2,
			0, 0, 9, 0, 0,
			0, 3, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla20 = { 3, 2, 0, 0, 0,
			1, 0, 3, 0, 0,
			2, 0, 9, 0, 0,
			0, 0, 3, 0, 0,
			0, 0, 3, 0, 0 };

	private static int [] tabla21 = { 3, 0, 3, 0, 3,
			0, 0, 0, 0, 0,
			0, 3, 9, 0, 0,
			0, 0, 3, 0, 1,
			0, 0, 3, 0, 2 };

	private static int [] tabla22 = { 0, 0, 3, 1, 2,
			0, 3, 0, 0, 1,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 0, 0, 2 };

	private static int [] tabla23 = { 0, 0, 3, 0, 0,
			1, 0, 0, 0, 0,
			3, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			3, 2, 0, 0, 3 };

	private static int [] tabla24 = { 3, 0, 0, 0, 0,
			0, 0, 3, 1, 1,
			0, 0, 9, 3, 3,
			0, 0, 0, 2, 2,
			0, 0, 0, 0, 0 };

	private static int [] tabla25 = { 3, 0, 0, 0, 0,
			0, 0, 0, 0, 3,
			0, 0, 9, 3, 1,
			0, 0, 3, 2, 1,
			0, 0, 0, 2, 2 };

	private static int [] tabla26 = { 3, 0, 3, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 2, 2, 2,
			0, 0, 3, 1, 2 };

	private static int [] tabla27 = { 3, 3, 0, 0, 0,
			0, 0, 3, 0, 0,
			2, 0, 9, 0, 0,
			3, 3, 0, 0, 0,
			2, 1, 0, 0, 0 };

	private static int [] tabla28 = { 0, 3, 2, 0, 2,
			0, 3, 2, 0, 1,
			0, 0, 9, 3, 2,
			0, 3, 3, 3, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla29 = { 3, 2, 1, 2, 3,
			0, 3, 3, 0, 0,
			0, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 2, 3, 0, 0 };

	private static int [] tabla30 = { 0, 3, 3, 0, 0,
			0, 0, 0, 0, 0,
			0, 3, 9, 0, 0,
			3, 2, 3, 0, 0,
			2, 1, 2, 0, 3 };

	private static int [] tabla31 = { 0, 3, 3, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 0, 0,
			0, 1, 3, 2, 0,
			0, 3, 2, 1, 0 };

	private static int [] tabla32 = { 3, 3, 0, 0, 3,
			1, 1, 0, 0, 0,
			2, 0, 9, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 3, 0, 0 };

	private static int [] tabla33 = { 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0,
			0, 0, 9, 3, 0,
			0, 0, 3, 1, 2,
			3, 0, 3, 3, 1 };

	private static int [] tabla34 = { 0, 3, 0, 3, 0,
			0, 0, 0, 0, 0,
			0, 3, 9, 0, 0,
			0, 1, 2, 3, 0,
			0, 1, 2, 3, 0 };

	private static int [] tabla35 = { 0, 1, 3, 2, 0,
			1, 2, 3, 0, 0,
			3, 0, 9, 0, 0,
			2, 0, 0, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla36 = { 0, 3, 0, 2, 1,
			0, 3, 0, 1, 2,
			0, 0, 9, 3, 2,
			0, 0, 0, 0, 0,
			0, 0, 0, 3, 0 };

	private static int [] tabla37 = { 0, 0, 3, 2, 1,
			0, 3, 0, 2, 1,
			0, 3, 9, 0, 0,
			0, 0, 3, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla38 = { 3, 2, 3, 0, 0,
			1, 0, 0, 0, 0,
			1, 0, 9, 0, 3,
			0, 0, 0, 0, 3,
			0, 0, 3, 0, 0 };

	private static int [] tabla39 = { 3, 0, 3, 1, 1,
			0, 0, 0, 2, 3,
			0, 0, 9, 3, 0,
			0, 0, 3, 0, 0,
			0, 0, 0, 0, 0 };

	private static int [] tabla40 = { 3, 0, 0, 0, 1,
			1, 0, 3, 0, 2,
			2, 0, 9, 3, 0,
			0, 0, 3, 0, 0,
			0, 3, 0, 0, 0 };

	private static int [] tabla40_1 = { 3, 0, 0, 0, 0,
			0, 0, 3, 0, 0,
			0, 0, 9, 3, 0,
			1, 0, 3, 0, 1,
			2, 3, 0, 0, 2 };

	private static int [] tabla40_2 = { 3, 0, 0, 0, 0,
			0, 0, 3, 0, 0,
			0, 0, 9, 3, 0,
			1, 0, 3, 1, 0,
			2, 3, 2, 0, 0 };

	private static int [] tabla40_3 = { 3, 0, 0, 0, 0,
			0, 0, 3, 0, 0,
			0, 0, 9, 3, 0,
			1, 0, 3, 0, 0,
			2, 3, 2, 1, 0 };



	private static int [][] tablak = { tabla0,
	    tabla1, tabla2, tabla3, tabla4, tabla5,
	    tabla6, tabla7, tabla8, tabla9, tabla10,
	    tabla11, tabla12, tabla13, tabla14, tabla15,
	    tabla16, tabla17, tabla18, tabla19, tabla20,
	    tabla21, tabla22, tabla23, tabla24, tabla25,
	    tabla26, tabla27, tabla28, tabla29, tabla30,
	    tabla31, tabla32, tabla33, tabla34, tabla35,
	    tabla36, tabla37, tabla38, tabla39, tabla40,
	};
	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Tilt game = new Tilt( 1 );
        System.out.println(game);
        System.exit(0);
    }
}