package base;

import java.util.Vector;

/**
     *  Minta object Gameable osztály megvalósítására
     * @author Slenker Gyula
     * 
     * Egy mxn-es táblán kell a bábut 'start' pozícióból 'finish' pozícióba átmozgatni
     * Egy lépés lehet jobbra, fel, balra, le, míg el nem érjük a tábla szélét,
     * vagy a 'finish' pozíciót
     * Pl. egy feladat:
     * A tábla:
     * ABC
     * DEF
     * GHI
     * A feladat: el kell jutni 'A'-ból, 'G'-be
     * Ezt így írjuk le: (3,3,0,6): 3x3 -as táblán a 0.-ból a 6. pozícióba
     */
public class Game implements Gameable
{
	private static int idSzamlalo=0;
	private int id;
	
		// A static változókat a kintről hívott konstruktorban inicializáljuk
	private static int cntCol;
    private static int size;
    private static int start;
    private static int finish;
    private static int [] iranyok;	// jobbra, fel, balra, le

    private int act;	// Aktuális pozíció (ez jellemzi az állást)
    
    private int prevStep;
    
    	// Kintről hívandó konstruktor a feladat inicializá
    public Game( int row, int col, int source, int target )
    {
        cntCol = col;
        iranyok = new int [] { 1, -cntCol, -1, cntCol };	// jobbra, fel, balra, le
        size = row*col;
        start = source;
        finish = target;
    	idSzamlalo=0;
        init( start );
    }
    private Game( int act, int prevStep )
    {
        init( act );
        this.prevStep = prevStep;	// Megjegyezzük, hogy jöttünk ide
    }
    private void init( int act)
    {
    	this.act = act;
        this.id = idSzamlalo++;
    }
    public int getId()
    {
        return id;
    }
    public int getAct()
    {
        return act;
    }
    	// Ha esetleg más konstruktor-t akarunk használni, akkor az oszlopok beállítása így kell:
    public void setCol(int col)
    {
    	cntCol = col;
    	iranyok[1] = -col;	// fel
    	iranyok[3] = col;	// le
    }

    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        Game game = null;
        for ( int irany=0; irany<4; irany++ )
        {
            //System.out.println("Act pos:\n" + toString(actual));
            int next = getNext( iranyok[irany], act);    //lépjünk egyet
            if ( next == (-1) )
                continue;
            //System.out.println("Irany: " + irany + "\n");
            //System.out.println(toString(nextStage));
            game = new Game( next, irany );
            //game.setCol(cntCol);	// TODO ezt még át kell gondolni, hogy a tulajdonságok öröklődjenek, 
            // vagy statikus legyen egy adott feladatra
            ret.add(game);
        }
        return ret;
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
    
        // azt az állapotot adjuk vissza
        // amikor adott irányban mozgatjuk az elemet
        // ha ez nem lehetséges, akkor -1
	private int getNext( int irany, int ind )
	{
		if(isTablaSzele(irany, ind))
			return (-1);
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

    	// Kész, ha elértük a célpozíciót
    public boolean isReady()
    {
        return (act==finish);
    }
    // Visszatérés true, ha a két állás ugyanaz
    public boolean equals(Gameable game)
    {
    	int other = ((Game)game).getAct();
    	return act == other;
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
	    	default : return "nincs ilyen lépés";
        }
    }
    
    public String toString()
    {
    	String s = Character.toString((char) ('A' + act));
    	return s;
    }
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Game game = new Game( 3,3,0,6 );
        System.out.println(game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
}