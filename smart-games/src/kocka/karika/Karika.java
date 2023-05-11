package kocka.karika;

import base.GameController;
import kocka.Kocka;
import kocka.forgat.Forgat;

//
/**
 * 2x2-es kocka kirakása 
 * @author slenk
 *
 */
public class Karika extends Kocka
{
    private static String [] colors = {"x", "P", "F", "S", "K"};
    protected Karika( int [] stage, int prevStep )
    {
    	super( stage, prevStep );
    	initForgatasok();
    }
    public Karika( int [] stage )
    {
    	this( stage, -1 );
    	idSzamlalo=1;
    }
    public Karika( int melyik )
    {
    	this( kockak[melyik] );
    }
    public Kocka newForgat( int [] stage, int prevStep )
	{
    	return new Karika( stage, prevStep );
	}
    @Override
    public String [] getColors()
    {
    	return colors;
    }
	/**
	 * A pillanatnyi célnak megfelelő állapotot adja vissza
	 * @return
	 */
    @Override
	public int [] getTarget()
    {
    	//return ready;
    	return target;
    }
    /**
     * Beállítjuk a lehetséges forgatásokat
     * Minden forgatás olyan (legyen), hogya a ref pozíciót (felső, bal, hátsó sarok) a helyén tartja
     * Ebben az esetben nincs szükség a getNorma() függvényre
     */
    @Override
	public void initForgatasok()
	{
		Forgat.init();

		Forgat.addForgatas("felso_jobb", origin, felso_jobb);
		Forgat.addForgatas("felso_bal", origin, felso_bal);
		Forgat.addForgatas("also_jobb", origin, also_jobb);
		Forgat.addForgatas("also_bal", origin, also_bal);
	}
    /*
     * Az eredeti elrendezés:
     *          
     *               
     *         . . . . .        	// Felső kör 0-18
     *     16             7
     *    17               6 
     *   18   22 21 20 19   5
     *        
     *     23             4			// Ez a két karika metszete 
     *     
     *   24    0  1  2  3   37
     *    25               36
     *     26             35
     *         . . . . .        	// Alsó kör 19-37
     */
	private static int [] origin = {
			  0,  1,  2,  3,  4,	// felső kör alja
			  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18,	// felső kör teteje
			 19, 20, 21, 22, 23,	// alsó kör teteje
			 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37	// alsó kör alja
	};
    // Az egyes forgatások:
    // Az elnevezést az adja, hogy melyik kört milyen irányban forgatjuk
    // 
	private static int [] felso_jobb = {
			  1,  2,  3,  4,  5,	// felső kör alja
			  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 23,	// felső kör teteje
			 19, 20, 21, 22,  0,	// alsó kör teteje
			 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37	// alsó kör alja
	};
	private static int [] felso_bal = {
			 23,  0,  1,  2,  3, 	// felső kör alja
			  4, 5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17,	// felső kör teteje
			 19, 20, 21, 22, 18,	// alsó kör teteje
			 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37	// alsó kör alja
	};
	private static int [] also_jobb = {
			  0,  1,  2,  3, 19,	// felső kör alja
			  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18,	// felső kör teteje
			 20, 21, 22, 23, 24,	// alsó kör teteje
			 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,  4	// alsó kör alja
	};
	private static int [] also_bal = {
			  0,  1,  2,  3, 37,	// felső kör alja
			  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18,	// felső kör teteje
			  4, 19, 20, 21, 22,	// alsó kör teteje
			 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36	// alsó kör alja
	};
    private static int [] ready = {
    		4, 4, 4, 4, 4, // kék
    		4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 	// piros
    		2, 2, 2, 2, 2, // fekete
    		2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3 // sárga
	};
    
    /**
     * Így néz ki most a karika
     */
	private static int [] pill = {
    		4, 4, 4, 4, 2, // kék
    		4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 	// piros
    		2, 2, 2, 3, 3, // fekete
    		2, 3, 3, 3, 3, 3, 3, 3, 4, 2, 2, 2, 2, 2,   // sárga
	};
    /**
     * A pillanatnyi cél
     */
	private static int [] target = {
    		4, 4, 4, 4, 2, // kék
    		4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 	// piros
    		2, 2, 2, 2, 3, // fekete
    		3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,   // sárga
	};

	private static int [][] kockak = 
    {
        ready, pill
    };
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        Karika game = new Karika( 1 );
        System.out.println("X0" + game);
        
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
	
}