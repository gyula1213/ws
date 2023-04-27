package numbers;

import java.util.Arrays;
import java.util.Vector;

import base.GameController;
import base.Gameable;
import kocka.Kocka;

/**
     * A Times számos játéka
     * 
     * @author Slenker Gyula
     * 
     * Adott 6 különböző kiinduló szám és egy cél szám
     * Minden lépésben 2 megadoot számon elvégezzük a 4 alapművelet egyikét, 
     * a 2 eredeti számot töröljük, a helyére beírjuk a kapott eredményt
     * 
     * Mindezt a műveletsorozatot addig végezzük míg eredményül a cél számot nem kapjuk
     * A művelet eredménye nem lehet negatív szám, illetve tört
     * 
     * Egy lépés:
     * A 2 szám és a művelet kiválasztása a kapott eredmény beírása a 2 szám helyett
     */
public class Numbers implements Gameable
{
	private static int idSzamlalo=0;
	private int id;
	
		// A static változókat a kintről hívott konstruktorban inicializáljuk
	private static int targetNumber = 495;

    private int [] numbers;	// Az aktuálisan használható számok
    
    private int prevNum1;
    private int prevNum2;
    private byte prevOp;	// +, -, *, :
    
    	// Kintről hívandó konstruktor a feladat inicializásához
    public Numbers( int [] numbers, int target )
    {
    	this.numbers = numbers;
    	targetNumber = target;
    }
    private Numbers( int [] numbers, int num1, int num2, byte op )
    {
    	this.numbers = numbers;
    	prevNum1 = num1;
    	prevNum2 = num2;
    	prevOp = op;
        this.id = idSzamlalo++;
    }
    public int getId()
    {
        return id;
    }
    public int [] getNumbers()
    {
        return numbers;
    }
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        Numbers game = null;
        for ( int i=0; i<numbers.length; i++ )
        {
        	int num1 = numbers[i];
            for ( int j=i+1; j<numbers.length; j++ )
            {
            	int num2 = numbers[j];
                for ( byte op=0; op<4; op++ )
                {
                    int res = getResult( num1, num2, op);    //lépjünk egyet
                    if ( res == 0 )	// nincs értelme a műveletnek
                        continue;
                    int [] newNumbers = getNewNumbers( res, i, j );
                    game = new Numbers( newNumbers, num1, num2, op );
                    //System.out.println(game);
                    ret.add(game);
                }
            }
        }
        return ret;
    }
    /**
     * Elvégzi 'a' és 'b' számon 'op' műveletet
     * Visszatérési érték a művelet eredménye, vagy 0, ha nincs értelme az adott műveletnek
     * @param a
     * @param b
     * @param op
     * @return
     */
    private int getResult( int a, int b, byte op )
    {
    	switch( op )
        {
	    	case 0 : return a+b;
	    	case 1 : return ( a>b ? a-b : b-a );
	    	case 2 : return a*b;
	    	case 3 : 
	    		if (a%b==0)
	    			return a/b;
	    		if (b%a==0)
	    			return b/a;
	    		return 0;
	    	default : return 0;
        }
    }
    /**
     * Visszaadja az aktuális számokat
     * Elejére teszi az új számot
     * 'ind1' és 'ind2' helyén álló régi számokat kihagyja
     * @param res
     * @param ind1
     * @param ind2
     * @return
     */
    private int [] getNewNumbers( int res, int ind1, int ind2 )
    {
    	int [] ret = new int [numbers.length-1];
    	ret[0] = res;	// az elejére tesszük az új számot
    	int cnt = 1;
        for ( int i=0; i<numbers.length; i++ )
        {
        	if ( i == ind1 || i == ind2 )
        		continue;
        	ret[cnt++] = numbers[i];
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
    
    	// Kész, ha elértük a célpozíciót
    public boolean isReady()
    {
        return (numbers[0]==targetNumber);	// mindig az első helyre tesszük az új számot
    }
    @Override
    public boolean equals(Object game)
    {
    	if (!(game instanceof Numbers))
    		return false;
    	
    	int [] stage = ((Numbers)game).getNumbers();
//    	if ( !getPrevStep().equalsIgnoreCase(((Numbers)game).getPrevStep() ))	// TODO csak ha az összes megoldás kell
//    		return false;
    	return equals( stage, numbers );
    }
    
    @Override
    public int hashCode()
    {
    	return Arrays.hashCode(numbers);
    }
	    // Visszatérés true, ha a két tábla ugyanaz
    protected boolean equals(int [] t1, int [] t2 )
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
    public String getPrevStep()
    {
    	String s = "";
    	if ( prevNum1 == 0 )
    		return "init";
    	String s1 = prevNum1 + " ";
    	String s2 = prevNum2 + " ";
    	if ( prevNum2>prevNum1 )
    	{
        	s2 = prevNum1 + " ";
        	s1 = prevNum2 + " ";
    	}
    	s += s1;
    	s += getOpSign(prevOp) + " ";
    	s += s2 + "= " + numbers[0];
    	return s;
    }
    public String getOpSign(byte op)
    {
    	switch( op )
        {
	    	case 0 : return "+";
	    	case 1 : return "-";
	    	case 2 : return "*";
	    	case 3 : return ":";
	    	default : return "nincs ilyen lépés";
        }
    }
    
    public String toString()
    {
    	String s = "";
    	for ( int i=0; i<numbers.length; i++ )
    		s += numbers[i] + ", ";
    	return s;
    }
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        int [] num = { 3, 5, 6, 7, 9, 11 }; 
        Numbers game = new Numbers( num, 315 );
        //int [] num = { 1, 2, 3 }; 
        //Numbers game = new Numbers( num, 6 );
        System.out.println(game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
}