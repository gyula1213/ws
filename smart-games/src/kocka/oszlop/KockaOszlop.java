package kocka.oszlop;

import java.util.Arrays;
import java.util.Vector;

import base.GameController;
import base.Gameable;

/**
 * 2x2x4-es bűvös kocka kirakásának az a része, amikor az elemek kimennek a 2x2x4-es térből, és vissza kell rendezni
 * @author slenk
 *
 */
public class KockaOszlop implements Gameable
{
    private int prevStep;

    private static int idSzamlalo=0;
	private int id;
	
    private int cntCol = 4;
    private int size;
    
    private Cube [] actual;
	static KockaOszlop.Cube kockak [] = new KockaOszlop.Cube[16];
    
    private KockaOszlop( Cube [] stage, int prevStep )
    {
    	this.prevStep = prevStep;
        this.actual = stage;
        size = stage.length;
        id = idSzamlalo++;
    }
    public KockaOszlop( Cube [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public KockaOszlop( int melyik )
    {
    	this( kockak );
		if ( melyik == 1 )
		{
			kockak[0] = new Cube(-2, -1, -1);
			kockak[1] = new Cube(-1, -1, -1);
			kockak[2] = new Cube(-1, -1, 1);
			kockak[3] = new Cube(-2, 1, 1);
			kockak[4] = new Cube(-1, -1, 2);
			kockak[5] = new Cube(-1, 1, 1);
			kockak[6] = new Cube(1, -1, -1);
			kockak[7] = new Cube(1, 1, 2);
			kockak[8] = new Cube(2, -1, 1);
			kockak[9] = new Cube(-1, 1, -1);
			kockak[10] = new Cube(1, 1, 1);
			kockak[11] = new Cube(-1, 2, -1);
			kockak[12] = new Cube(1, -1, -2);
			kockak[13] = new Cube(1, 1, -1);
			kockak[14] = new Cube(1, -1, 1);
			kockak[15] = new Cube(1, 2, -1);
		}
		else
		{
			int cnt=0;
			for ( int i=0; i<kocka.length; i++ )
			{
				if ( kocka[i] == 1 )
				{
					kockak[cnt++] = new Cube(i);
				}
			}
		}
			
		System.out.println(getNorma(kockak));
		
    }
    public int getId()
    {
        return id;
    }
    Cube [] getActual()
    {
        return this.actual;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
    	
        KockaOszlop game = null;
        for ( int i=0; i<=8; i++ )
        {
            //System.out.println("Actual:\n" + toString(actual));
            Cube [] nextStage = nextStage( actual, i);    //lépjünk egyet
            if ( nextStage == null )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            if ( equals( actual, nextStage))
            	continue;
            game = new KockaOszlop( nextStage, i );
            ret.add(game);
        }
        return ret;
    }
        // azt az állapotot adjuk vissza
        // amikor adott 'tengely' körül (x,y,z)
    	// az óramutató járásával megyezően 'db' forgatást végzünk (1,2,3)
    Cube [] nextStage( Cube [] param, char tengely, int db )
    {
        Cube [] ret = new Cube [size];
        for (int i=0; i<param.length; i++)	// lemásoljuk
        {
        	ret[i] = new Cube(param[i]);
        }

        for (int i=0; i<ret.length; i++)
        {
        	ret[i].forgat( tengely, db );
        }
	    return ret;
    }
    
	// azt az állapotot adjuk vissza
	// amikor adott 'lepes' szerint forgatjuk a kockát
	private Cube [] nextStage( Cube [] param, int lepes )
	{
		char tengely='?';
		if (lepes/3 == 0 )
			tengely = 'x';
		if (lepes/3 == 1 )
			tengely = 'y';
		if (lepes/3 == 2 )
			tengely = 'z';
		int mennyi = lepes%3+1;
		
		return nextStage( param, tengely, mennyi );
	}
    // Kész, ha z és y irányban nem lóg ki egy elem sem
    public boolean isReady()
    {
    	for ( int i=0; i<actual.length; i++ ) 
    	{
    		if ( actual[i].z == -2 || actual[i].z == 2 )
    			return false;
    		if ( actual[i].y == -2 || actual[i].y == 2 )
    			return false;
    	}
        return true;
    }
    private Cube [] getStage()
    {
        return actual;        
    }

    
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof KockaOszlop))
    		return false;
    	
    	Cube [] stage = ((KockaOszlop)game).getStage();
    	return equals( stage, actual );
    }
    
    @Override
    public int hashCode()
    {
    	int [] norma = getNorma(actual);
    	return Arrays.hashCode(norma);
    }
    
     // Visszatérés true, ha a két állás ugyanaz
    //public boolean equals(Gameable game)
    //{
    //    Cube [] stage = ((KockaOszlop)game).getStage();
    //    	return equals( stage, actual );
    //}
    private int [] getNorma( Cube [] cubes )
    {
    	int [] ret = new int [64];
        for ( int i=0; i<ret.length; i++ )
        {
        	ret [i] = 0;
        }
        for ( int i=0; i<cubes.length; i++ )
        {
        	ret [cubes[i].getPos()] = 1;
        }
        return ret;
    }
    // Visszatérés true, ha a két tábla ugyanaz
    private boolean equals(Cube [] c1, Cube [] c2 )
    {
    	int [] t1 = getNorma(c1);
    	int [] t2 = getNorma(c2);
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
	    	case 0 : return "x1";
	    	case 1 : return "x2";
	    	case 2 : return "x3";
	    	case 3 : return "y1";
	    	case 4 : return "y2";
	    	case 5 : return "y3";
	    	case 6 : return "z1";
	    	case 7 : return "z2";
	    	case 8 : return "z3";
	    	default : return "nincs ilyen lépés";
        }
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
    	String s = "";
    	s += getId() + ":: " + getPrevStep() + ":: ";
    	s += toString(actual);
    	return s;
    }
    private String toString( Cube [] tabla )
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
	public static class Cube
    {
		int x,y,z;	// x,y,z jobbsodrású koordináták, -2,-1,1,2 lehetséges értékekkel
		public Cube( int x, int y, int z)
	    {
			this.x = x;
			this.y = y;
			this.z = z;
	    }
		// pos: 4x4x4-es tömmbben lévő pozíciója a kockának
		// lentről nézve: először az alsó 4x4
		public Cube( int pos)
	    {
			int [] koords = getKoords( pos );
			x = koords[0];
			y = koords[1];
			z = koords[2];
	    }
		private Cube( Cube c)
	    {
			this.x = c.x;
			this.y = c.y;
			this.z = c.z;
	    }
		static int getEltol(int k)
	    {
			//-2-->0
			//-1-->1
			//0 nincs
			//1 -->2
			//2 -->3
			return k>0?k+1:k+2;
	    }
		static int getVisszatol(int k)
	    {
			//0-->-2
			//1-->-1
			//2-->1
			//3-->2
			return k>1?k-1:k-2;
	    }
		// Visszaadja 0-63-ig, hogy melyik pozíción van
		int getPos()
	    {
			//-2-->0
			//-1-->1
			return 16*getEltol(z)+4*getEltol(y)+getEltol(x);
	    }
		// Visszaadja x,y,z koordinátákat
		static int [] getKoords(int pos)
	    {
			int [] ret = new int[3];
			ret[2] = getVisszatol(pos/16);
			ret[1] = getVisszatol(pos%16/4);
			ret[0] = getVisszatol(pos%16%4);
			return ret;
	    }
			// A forgatás egy 90 fokos elforgatást jelent az óramutató járásával MEGEGYEZŐ irányban
		private void forgat( char tengely )
	    {
			int tmp;
			switch( tengely )
			{
			case 'x' :
				if ( x<0 )
					return;
				tmp = y;
				y = -z;
				z = tmp;
				break;
			case 'y' :
				if ( y<0 )
					return;
				tmp = z;
				z = -x;
				x = tmp;
				break;
			case 'z' :
				if ( z<0 )
					return;
				tmp = x;
				x = -y;
				y = tmp;
				break;
			}
	    }
		public void forgat( char tengely, int mennyi )
	    {
			for( int i=0; i<mennyi; i++ )
		    {
				forgat( tengely );
		    }
	    }
		@Override
		public String toString()
	    {
			String s="";
			s = "[" + x + "," + y + "," + z + "]";
			return s;
	    }
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			result = prime * result + z;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cube other = (Cube) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			if (z != other.z)
				return false;
			return true;
		}
		
    }
	int [] kocka = {
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,
			0,0,0,0,	// alsó sor
			
			0,0,1,0,
			1,1,1,0,
			1,1,1,0,
			0,0,1,0,
			
			0,0,0,0,
			1,1,1,0,
			1,1,1,0,
			0,0,0,0,
			
			0,0,0,0,
			0,0,1,0,
			0,0,1,0,
			0,0,0,0,
			
	};
	
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        KockaOszlop game = new KockaOszlop( 1 );
        System.out.println(game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}