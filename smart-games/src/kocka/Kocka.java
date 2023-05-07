package kocka;

import java.util.Arrays;
import java.util.Vector;

import base.Gameable;
import hello.MemoryTest;
import kocka.forgat.Forgat;
import screen.Cubeable;

/**
 * Bűvös kocka jellegű, forgató játékok ösosztálya
 * 
 * @author slenk
 *
 */
public class Kocka implements Gameable, Cubeable
{
    private static String [] colors = {"x", "abcde", "Z", "P", "S", "N", "K"};

    private int prevStep;

    protected static int idSzamlalo=0;
    protected int id;
	
    protected byte [] actual;
    private static byte [] saveStarted;
    
    protected Kocka( byte [] stage, int prevStep )
    {
    	this.prevStep = prevStep;
        this.actual = MemoryTest.zip(stage);
        saveStarted = stage;
        id = idSzamlalo++;
    }
    public Kocka newForgat( byte [] stage, int prevStep )
	{
    	return new Kocka( stage, prevStep );
	}
    /**
     * Azt a mátrixot adjuk vissza, ami az adott forgatáshoz szükséges, ha a forgatás nem aktív, akkor null-t
     * @param melyik
     * @return
     */
    private int [] getForgatasok(int melyik) 
	{
		 return Forgat.getMatrix(melyik);
	};
	// Hány féle forgatás létezik
	private int getForgatasokDb() 
	{
		return Forgat.getSize();
	};
	/**
	 * A cél függvény.
	 * Visszatérés true, ha elégedettek vagyunk a pillanatnyi állapottal
	 * getTarget() fv mondja meg, hogy mi az aktuális cél
	 * Ha a "target" pozíción 0 van, ott bármi lehet
	 * 
	 * Ha nem konkrét eredményt várunk, akkor felülírható (pl. Domino)
	 */
	public boolean isReady()
    {
    	byte [] target = getTarget();
        for ( int i=0; i<actual.length; i++ )
        {
            if ( target[i] == 0 )    // itt mindegy mi van
            {
                continue;
            }
            if ( actual[i] != target[i] )    // eltérés
            {
                return false;  // ez nem jó
            }
        }
        return true;	//Minden OK
    }
	/**
	 * A pillanatnyi célnak megfelelő állapotot adja vissza
	 * Származtatott osztályban kell megadni 
	 * @return
	 */
	public byte [] getTarget()
    {
    	return new byte[actual.length];
    }
	/**
	 * @param melyik
	 * @return
	 */
	public String getForgatasName(int melyik)
    {
		if ( melyik == (-1))
			return "init";
		return Forgat.getName(melyik);
    }

    public final String getPrevStep()
    {
    	return getForgatasName(prevStep);
    }
    public final int getId()
    {
        return id;
    }
    private byte [] getActual()
    {
        return MemoryTest.unzip(actual);
    }
    public final Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
    	
        Kocka game = null;
        for ( int i=0; i<getForgatasokDb(); i++ )
        {
            //System.out.println("Actual:\n" + toString(actual));
            byte [] nextStage = forgat(i);    //lépjünk egyet
            if ( nextStage == null )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            if ( equals( actual, nextStage))
            	continue;
            game = newForgat( nextStage, i );
            ret.add(game);
        }
        return ret;
    }
	private byte [] forgatx( byte [] param, int lepes )
	{
        int [] forog = getForgatasok(lepes);
        return Forgat.forgat( param, forog );
	}
	/**
	 * Forgat egyet a "lepes"-sel azonosított lepes alapján 
	 * @param lepes
	 * @return
	 */
	private byte [] forgat( int lepes )
	{
        int [] forog = getForgatasok(lepes);
        if ( forog == null )
        	return null;
        return Forgat.forgat( getActual(), forog );
	}
	/**
	 * Forgat "n"-szer a "lepes"-sel azonosított lepes alapján 
	 * @param lepes
	 * @param n
	 * @return
	 */
	private byte [] forgat( int lepes, int n )
	{
        int [] forog = getForgatasok(lepes);
        byte [] act = getActual();
        for( int i=0; i<n; i++ )
    	{
            act = Forgat.forgat( act, forog );
    	}
        return act;
	}
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof Kocka))
    		return false;
    	
    	byte [] stage = ((Kocka)game).getActual();
    	return equals( getNorma(stage), getNorma(actual) );
    	//return equals( stage, actual );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(actual);
    }
	    // Visszatérés true, ha a két tábla ugyanaz
    protected boolean equals(byte [] t1, byte [] t2 )
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
    /**
     * Normalizál egy állást, hogy az összehasonlítás megoldható legyen
     * Ez a konkrét esetekben azt jelenti, hogy az összehasonlításnál van egy fix pont.
     * Ha a forgatásokat úgy írjuk meg, hogy biztos legyen, hogy egy pont helyben marad, akkor nincs szükség a fv-re
     * @param t
     * @return
     */
	protected byte [] getNorma(byte [] t )
	{
		return t;
	}
	/**
	 * Egyelőre nem tudjuk eldönteni, hogy mi alapján tekinthetünk egy állapotot jobbnak, mint a másik, úgyhogy ezzel nem foglalkozunk
	 */
    public final boolean isHopefulStage()
    {
    	return false;
    }
	/**
	 * Egyelőre nem tudjuk eldönteni, hogy mi alapján tekinthetünk egy állapotot jobbnak, mint a másik, úgyhogy ezzel nem foglalkozunk
	 */
    public final boolean isCriticalDeep( int deep )
    {
	    return (deep%1000==0);	// Jó nagy szám, amit soha nem ér el
    }
    
    public String toString()
    {
    	String s = "";
    	s += getId() + ":: " + getPrevStep() + ":: ";
    	s += toString(getActual());
//      s += "\npill.: " + getSource(actual);
    	return s;
    }
    /**
     * A színek betűjele a kiíratásnál
     * Az egyes kockáknál külön meg kell adni
     * @return
     */
    public String [] getColors()
    {
    	//String [] colors = { "F", "Z", "P", "K", "N", "S" }; 
    	return colors;
    }
    /**
     * Formázott kiíratáshoz segítség
     * @return
     */
    public int getCntCol()
    {
    	return 9;
    }
    public String toString( byte [] tabla )
    {
        String s="";
        String [] colors = getColors();
        int cntCol = getCntCol();
        for ( int i=0; i<tabla.length; i++ )
        {
            if ( (i+1)%getCntCol() > 9 )		// Az első 4-et és a 8.-at írja ki
            	continue;
            String color = colors[tabla[i]];
            s += color +"";
            if ( (i+1)%cntCol == 0 )
                //s += "\n";
            	s += ",     ";
            else
                s += " ";
        }
        return s;
    }
    public  void initForgatasok() {
    	
    }
    private int sec = 0;
	@Override
	public void init() {
		initForgatasok();
	}
	@Override
	public int getCnt() {
		// TODO Auto-generated method stub
		return 99;
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 4;
	}
	@Override
	public int getSec() {
		return sec;
	}
	@Override
	public void setSec(int sec) {
		this.sec = sec;
	}
	@Override
	public boolean checkActTable() {
		return isReady();
	}
	@Override
	public byte[] getActTable() {
		return getActual();
	}
	@Override
	public void forgat( String name ) {
		int [] matrix = Forgat.getMatrix(name);
        actual = Forgat.forgat( getActual(), matrix );
	}
	@Override
	public void restart() {
		actual = saveStarted.clone();
	}
}