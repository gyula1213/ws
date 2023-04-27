package robot;

import java.util.Arrays;
import java.util.Vector;

import base.GameController;
import base.Gameable;

/**
     *  Száguldó robotok
     * @author Slenker Gyula
     * 
     * Egy mxn-es táblán kell az "alaprobotot" a 'start' pozícióból 'finish' pozícióba átmozgatni
     * Egy lépés lehet jobbra, fel, balra, le, úgy hogy az adott bábú mindaddig megy, míg akadályba nem ütközik
     * vagy eléri 'finish' pozíciót
     * Vannak "segédrobotok", amik befolyásolhatják az alaprobot mozgását
     * A segédrobotok ugyanúgy mozognak, mint az alaprobot
     * Pl. egy feladat:
     * A tábla:
     * Egy mxn-es tábla, ahol 4 segédtömbben megmondjuk, hogy hol vannak a falak (left, right, up, down), amik akadályozzák a mozgást
     * 
     * Egy játékban a robotokat kell elhelyezni a megfelelő kiinduló pozícióba, és a cél hely kordinátáit kell megmondani
     * A robotok helye egy tömb, az alaprobotot azonosítani kell
     * A cél egy szám, ami egy pozíciót jelöl a táblán
     */
public class Robot implements Gameable
{
	private static int idSzamlalo=0;
	private int id;
	
		// A static változókat a kintről hívott konstruktorban inicializáljuk
	private static int cntCol;	// Az oszlopok száma a táblán
    private static int alapRobot; 	// Amelyik robotot a célba kell juttatni, ha (-1), akkor bármelyik robot lehet
    private static int finish;		// A cél pozíciója
    private static int [] iranyok;	// jobbra, fel, balra, le
    private static int [][] walls;		// A falak, amiben jobbra, fel, balra, le
    private static int [] table;		// A tábla. Igazából csak a per jeleket használjuk róla
    private static int [] activeRobots;	// akik épp léphetnek
    private int [] robots;	// A robotok aktuális helyei kék, piros, zöld, sárga sorrendben
    
    private int prevStep;	// Az előző lépés. Egy kétjegyű szám, az első jegy megmondja, hogy melyik robottal léptünk, a második, hogy merre


    private static void createTable( int row, int col, int [][] createWalls, int [] createTable)
    {
    	cntCol = col;
        iranyok = new int [] { 1, -cntCol, -1, cntCol };	// jobbra, fel, balra, le
        walls = createWalls;
        table = createTable;
    }
    	// Kintről hívandó konstruktor a feladat inicializásához
    public Robot( int [] startRobots, int alap, int target, int [] active)
    {
        finish = target;
        alapRobot = alap-1;
    	idSzamlalo=0;
    	activeRobots = active;
        init( startRobots );
    }
    private Robot( int [] act, int babu, int irany )
    {
        init( act );
        this.prevStep = 10*(babu+1)+irany+1;	// Megjegyezzük, hogy jöttünk ide
    }
    private void init( int [] act)
    {
    	this.robots = act;
        this.id = idSzamlalo++;
    }
    public int getId()
    {
        return id;
    }
    public int [] getRobots()
    {
        return robots;
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
        Robot game = null;
        for ( int robot=0; robot<robots.length; robot++ )
        {
        	if ( activeRobots[robot] == 0)	// ez most nem játszik
        		continue;
            for ( int irany=0; irany<4; irany++ )
            {
                //System.out.println("Act pos:\n" + toString(actual));
                int [] next = getNext( robot, irany );    //lépjünk egyet
                if ( next == null )
                    continue;
                //System.out.println("Irany: " + irany + "\n");
                //System.out.println(toString(nextStage));
                game = new Robot( next, robot, irany );
                ret.add(game);
            }
        }
        return ret;
    }
    public boolean isHopefulStage()
    {
    	return false;
    }
    public boolean isCriticalDeep( int deep )
    {
	    return (deep%20==0);	// pl. minden 10.
    }
    
        // azt az állapotot adjuk vissza (az összes robot új helyzetét)
        // amikor adott robotot az adott irányban mozgatjuk
        // ha ez nem lehetséges, akkor null
	private int [] getNext( int robot, int irany )
	{
		int pos=robots[robot];
		int newPos = pos;
		while(true)
		{
			if ( walls[irany][newPos] == 1 )	// Falba ütközött
				break;
			newPos += iranyok[irany];
    		if ( isOtherRobot(robot, newPos) )	// Másik robotba ütközött
    		{
    			newPos -= iranyok[irany];
    			break;
    		}
    		if ( table[newPos] > 10 )	// Egy eltérítő jelet találtunk
    		{
    			int ujIrany = getUjIrany( robot, irany, table[newPos]);
    			int savePos = robots[robot];
    			robots[robot] = newPos;	// innen folytatódik a lépés
    			int [] next = getNext( robot, ujIrany );
    			robots[robot] = savePos;	// vissza kell állítani avalódi helyzetbe
    			return next;
    		}
		}
		if ( pos == newPos )	// Nem tudott lépni
			return null;
		int [] ret = new int [robots.length];
    	for ( int i=0; i<robots.length; i++ )
    	{
    		if ( i == robot )
    			ret[i] = newPos;
    		else
    			ret[i] = robots[i];
    	}
    	return ret;
	}
	/**
	 * Megmondja merre kell továbbmenni, ha rálépünk egy jelre
	 * @param robot, Amelyikkel ráléptünk a jelre
	 * @param irany, eddig erre mentünk
	 * @param jel, ilyen színű és alakú a jel
	 * @return az új irány
	 */
	private int getUjIrany( int robot, int irany, int jel )
	{
		int jelSzin = jel/10 -1;
		int jelTipus = jel%10;
		if ( jelSzin == robot )// nem hat rá a jel
			return irany;
		int ret = -1;
    	switch( irany )
        {
	    	case 0 : ret = jelTipus == 1 ? 3 : 1 ; break;
	    	case 1 : ret = jelTipus == 1 ? 2 : 0 ; break;
	    	case 2 : ret = jelTipus == 1 ? 1 : 3 ; break;
	    	case 3 : ret = jelTipus == 1 ? 0 : 2 ; break;
        }
		return ret;
	}
		// true, ha a mezőn másik robot áll
	private boolean isOtherRobot(int robot, int pos)
	{
    	for ( int i=0; i<robots.length; i++ )	// Másik robotba ütközött
    	{
    		if ( i == robot )
    			continue;
    		if ( robots[i] == pos )
    			return true;
    	}
    	return false;
	}

    	// Kész, ha elértük a célpozíciót
    public boolean isReady()
    {
    	if ( alapRobot >=0 )
    		return (robots[alapRobot]==finish);
    		
    	// ilyenkor bármelyik robot lehet a célban
    	for ( int i=0; i<robots.length; i++ )
    	{
    		if ( robots[i] == finish )
    			return true;;
    	}
    	return false;
    }
    // Visszatérés true, ha a két állás ugyanaz
    public boolean equals(Gameable game)
    {
    	int [] others = ((Robot)game).getRobots();
    	for ( int i=0; i<robots.length; i++ )
    	{
    		if ( robots[i] == others[i] )
    			return false;
    	}
    	return true;
    }
    public String getPrevStepBJ()
    {
    	if ( prevStep == 0 )
    		return "";
    	int robot = prevStep/10;
    	int irany = prevStep%10-1;
    	String ret = robot + "";
    	switch( irany )
        {
	    	case 0 : ret += "2"; break;
	    	case 1 : ret += "3"; break;
	    	case 2 : ret += "1"; break;
	    	case 3 : ret += "4"; break;
        }
    	return ret;
    }
    public String getPrevStep()
    {
    	//if (true)
    	//	return getPrevStepBJ();
    	
    	if ( prevStep == 0 )
    		return "init";
    	int robot = prevStep/10;
    	int irany = prevStep%10-1;
    	String ret = "";
    	switch( robot )
        {
	    	case 1 : ret = "kék "; break;
	    	case 2 : ret = "piros "; break;
	    	case 3 : ret = "zöld "; break;
	    	case 4 : ret = "sárga "; break;
	    	case 5 : ret = "barna "; break;
        }
    	switch( irany )
        {
	    	case 0 : ret += "jobbra"; break;
	    	case 1 : ret += "fel"; break;
	    	case 2 : ret += "balra"; break;
	    	case 3 : ret += "le"; break;
        }
    	return ret;
    }
    
    public String toString()
    {
    	String s = "";
    	for ( int i=0; i<robots.length; i++ )
    	{
    		s += (i+1) + ":(" + (robots[i]/cntCol+1) + "," + (robots[i]%cntCol+1) + ") ";
    	}
		s += "Cél: (" + (finish/cntCol+1) + "," + (finish%cntCol+1) + ") ";
    	return s;
    }
    private static int [] leftMinta =
    {
    	1,0,0,1,0,0,
    	1,0,0,0,0,0,
    	1,0,0,0,1,0,
    	1,1,0,0,0,0,
    	1,0,0,1,1,0,
    	1,0,0,0,0,0,
    };
    private static int [] rightMinta =
    {
    	0,0,1,0,0,1,
    	0,0,0,0,0,1,
    	0,0,0,1,0,1,
    	1,0,0,0,0,1,
    	0,0,1,1,0,1,
    	0,0,0,0,0,1,
    };
    private static int [] upMinta =
    {
    	1,1,1,1,1,1,
    	0,0,1,0,0,0,
    	0,0,0,0,1,0,
    	0,0,0,0,0,0,
    	0,1,0,1,0,0,
    	0,0,0,1,0,0,
    };
    private static int [] downMinta =
    {
    	0,0,1,0,0,0,
    	0,0,0,0,1,0,
    	0,0,0,0,0,0,
    	0,1,0,1,0,0,
    	0,0,0,1,0,0,
    	1,1,1,1,1,1,
    };
    private static int [] verticalMinta =
    {
    	0,0,1,0,0,
    	0,0,0,0,0,
    	0,0,0,1,0,
    	1,0,0,0,0,
    	0,0,1,1,0,
    	0,0,0,0,0,
    };
    private static int [] horisontalMinta =
    {
    	0,0,1,0,0,0,
    	0,0,0,0,1,0,
    	0,0,0,0,0,0,
    	0,1,0,1,0,0,
    	0,0,0,1,0,0,
    };
    private static int [] verticalProba =
    {
    		0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,
    		0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,
    		0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,
    		0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,
    		0,0,0,0,0,0,1,1,1,0,1,0,0,0,0,
    		0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
    		0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,
    		0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,
    		1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,
    		0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,
    		
    };
    private static int [] horisontalProba =
    {
    		0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,
    		0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,
    		0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,1,0,0,0,1,1,0,1,0,0,1,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1,
    		0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,
    		1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,
    		0,1,0,0,0,0,1,0,0,1,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    };
    
    private static int [] tablaProba =
    {
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,
    		0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,
    		0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    };
    
    private static int [] getRightWalls(int row, int col, int [] verticalWalls)
    {
    	int [] ret = new int[row*col];
    	for ( int i=0; i<row; i++ )
    	{
        	for ( int j=0; j<col-1; j++ )
        	{
        		ret[cntCol*i+j] = verticalWalls[(cntCol-1)*i+j];
        	}
        	ret[cntCol*i+col-1] = 1;	// A jobb oldalon mindig fal van
    	}
    	return ret;
    }
    private static int [] getLeftWalls(int row, int col, int [] verticalWalls)
    {
    	int [] ret = new int[row*col];
    	for ( int i=0; i<row; i++ )
    	{
        	ret[cntCol*i] = 1;	// A bal oldalon mindig fal van
        	for ( int j=0; j<col-1; j++ )
        	{
        		ret[cntCol*i+j+1] = verticalWalls[(cntCol-1)*i+j];
        	}
    	}
    	return ret;
    }
    private static int [] getDownWalls(int row, int col, int [] horisontalWalls)
    {
    	int [] ret = new int[row*col];
    	for ( int i=0; i<row-1; i++ )
    	{
        	for ( int j=0; j<col; j++ )
        	{
        		ret[cntCol*i+j] = horisontalWalls[cntCol*i+j];
        	}
    	}
    	for ( int j=0; j<col; j++ )
    	{
    		ret[cntCol*(row-1)+j] = 1;
    	}
    	return ret;
    }
    private static int [] getUpWalls(int row, int col, int [] horisontalWalls)
    {
    	int [] ret = new int[row*col];
    	for ( int j=0; j<col; j++ )
    	{
    		ret[j] = 1;
    	}
    	for ( int i=0; i<row-1; i++ )
    	{
        	for ( int j=0; j<col; j++ )
        	{
        		ret[cntCol*(i+1)+j] = horisontalWalls[cntCol*i+j];
        	}
    	}
    	return ret;
    }
    private static int [][] wallsMinta =
    {
    	rightMinta, upMinta, leftMinta, downMinta
    };
    private static int [] robotsMinta = {
        	4,6,5,25
    };
    private static int [] robotsProba = {
        	14, 181, 178, 16, 99
    };
    private static int [] activeRobotsProba = {	// akik épp léphetnek
        	1, 1, 1, 1, 1,
    };
// Szép! Bábuk	41, 233, 29, 164, 121 Cél 2, 168, játszik 0, 1, 1, 0, 0
// Szép! Bábuk	41, 233, 29, 164, 121 Cél 2, 168, játszik 0, 0, 1, 1, 0
// Szép! Bábuk	41, 233, 29, 164, 121 Cél 5, 168, játszik 0, 0, 0, 1, 1 // semelyik másik párossal nem ment (10 lépés)

    static int robot = 2;
    static int cel = 168;

    public static void main(String[] args)
    {
    	robotMain(args);
    }
    public static void robotMain(String[] args)
    {
//    	createTable( 6, 6, wallsMinta );
//    	int [] r = getRightWalls(6,6, verticalMinta);
//    	int [] l = getLeftWalls(6,6, verticalMinta);
//    	int [] d = getDownWalls(6,6, horisontalMinta);
//    	int [] u = getUpWalls(6,6, horisontalMinta);
//    	wallsMinta[0] = r;
//    	System.out.println("r\n" + Arrays.toString(r));
//    	System.out.println("l\n" + Arrays.toString(l));
//    	System.out.println("u\n" + Arrays.toString(u));
//    	System.out.println("d\n" + Arrays.toString(d));
//    	wallsMinta[1] = u;
//    	wallsMinta[2] = l;
//    	wallsMinta[3] = d;
//    	createTable( 6, 6, wallsMinta );
    	createTable( 16, 16, null, null );
    	int [] r = getRightWalls(16,16, verticalProba);
    	int [] l = getLeftWalls(16,16, verticalProba);
    	int [] d = getDownWalls(16,16, horisontalProba);
    	int [] u = getUpWalls(16,16, horisontalProba);
    	wallsMinta[0] = r;
//    	System.out.println("r\n" + Arrays.toString(r));
//    	System.out.println("l\n" + Arrays.toString(l));
//    	System.out.println("u\n" + Arrays.toString(u));
//    	System.out.println("d\n" + Arrays.toString(d));
    	wallsMinta[1] = u;
    	wallsMinta[2] = l;
    	wallsMinta[3] = d;
    	createTable( 16, 16, wallsMinta, tablaProba );
        System.out.println("Indul...");
        Robot game = new Robot( robotsProba, robot, cel, activeRobotsProba );
        System.out.println(game);
        if (fejtes() == 1 )
            System.out.println("Kész");
        else
        	System.out.println("Nem sikerült");
        System.exit(0);
    }
    public static int fejtes()
    {
    	if ( egyProba( new int [] {1,1,1,1,1}, 500000 ) == 1 )
			return 1;
    	int [] active2 = new int[5];	// 2 aktív, az alaprobot és még egy
    	for (int i=0; i<active2.length; i++)
    	{
    		if ( i==(robot-1) )
    			continue;
        	for (int j=0; j<active2.length; j++)
        	{
        		if ( i==j || j==(robot-1))
        			active2[j] = 1;
        		else
        			active2[j] = 0;
        	}
        	if ( egyProba( active2, 1000000 ) == 1 )
    			return 1;
    	}
    	int [] active1 = new int[5];	// 1 aktív, az alaprobot
    	active1[robot-1] = 1;
    	if ( egyProba( active1, 1000000 ) == 1 )
			return 1;
		return 0;
    }
    public static int egyProba( int [] actives, int maxStages )
    {
        Robot game = new Robot( robotsProba, robot, cel, actives );
    	GameController gc = new GameController(game); 
        gc.setMaxStages(maxStages);
        return gc.solve();
    }

}