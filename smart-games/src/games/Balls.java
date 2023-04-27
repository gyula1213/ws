package games;

import java.util.Vector;

import base.GameController;
import base.Gameable;

/**
     * Labdák a csövekben
     * @author Slenker Gyula
     * 
     * Van 'n' db cső, melyek mindegyéke 4 labda fér
     * És van 4*'m' golyó, ami véletlenszerűen van elhelyezve az 'm' darab csőben.
     * 'm' < 'n', amiből következik hogy van néhány ('n'-'m') üres csövünk is
     * Feladat az, hogy úgy átrendezni a labdákat, hogy amelyik csőben van labda, abban 4 db egyforma színű labda legyen
     * Ez értelemszerűen azt jelenti, hogy lesz 'm' db teli csövünk (egyforma labdákkal) és a többi üres
     * Egy lépésnél egy labdát tudunk áthelyezni 'A' csőből 'B' csőbe.
     * Az áthelyezés feltélei
     * 1. A labda az 'A' csőben legfelelül legyen (ne legyen rajta más labda)
     * 2. 
     *   -- A 'B' csőben 4-nél kevesebb labda legyen
     *      és a legfelső labda ugyanolyan színű legyen, mint amit be akarunk tenni, vagy
     *   -- A 'B' cső legyen üres
     *   
     * Minta kiindulás:
     * 3 cső, ebből 2 teli (n=3, m=2)
     * 
     * 1	2	0
     * 2	1	0
     * 1	2	0
     * 2	1	0
     * 
     * Végállapot (több is lehet):
     * 2	0	1
     * 2	0	1
     * 2	0	1
     * 2	0	1
     */
public class Balls implements Gameable
{
	private static int idSzamlalo=0;
	private int id;
	
		// A static változókat a kintről hívott konstruktorban inicializáljuk
	private static int cntCso;
    private static int db=4;	// Ezt a leírásban mindig 4-nek írtam, de tulajdonképpen változó

    private int [][]act;	// Aktuális pozíció (ez jellemzi az állást)
    
    	// Egy lépést jellemez:
    private int honnan;
    private int hova;
    
    	// Kintről hívandó konstruktor a feladat inicializá
    public Balls( int n, int [][]source )
    {
    	cntCso = n;
    	idSzamlalo=0;
        init( source );
    }
    private Balls( int[][] act, int honnan, int hova )
    {
        init( act );
        this.honnan = honnan;	// Megjegyezzük, mit léptünk
        this.hova = hova;		
    }
    private void init( int [][]act)
    {
//    	this.act = act.clone();
    	this.act = act;
        this.id = idSzamlalo++;
    }
    public int getId()
    {
        return id;
    }
    public int[][] getAct()
    {
        return act;
    }

	int uresCso;	// Üres csövek figyelése
    public Vector<Gameable> nextStages()
    {
    	Vector<Gameable> ret = new Vector<>();
        Balls game = null;
    	uresCso = -1;
        for ( int i=0; i<cntCso; i++ )
        {
            for ( int j=0; j<cntCso; j++ )
            {
            	if ( i==j )
            		continue;
                int [][]next = getNext( act, i, j);    // i. csőből j. csőbe tesszük a labdát, halehet
                if ( next == null )	// nem sikerült
                    continue;
                game = new Balls( next, i, j );
                ret.add(game);
            }
        }
        return ret;
    }
    /**
     * Megmondja, hogy az adott mélység kritikus-e
     * @return
     */
	@Override
	public boolean isCriticalDeep( int deep )
	{
		return (deep%7==0);	// minden 7. után takarítunk
		//return ( deep == 2 );
	}
    
    /**
     * Megmondja, hogy ez az állapot a végső megfejtés szemőpontjából
     * sikerrel kecsegtet-e
     * Itt azt tekintjük reményteljesnek, ha van üres csövünk
     * @return
     */
	@Override
    public boolean isHopefulStage()
    {
		if ( id < 1000 )	// az elején levőket eldobjuk, mert egy-két lépésen belül újra ürsenek kell lennie
			return false;
        for ( int i=0; i<cntCso; i++ )
        {
        	if ( isUres(act[i]))	// Ha találtunk üres csövet, akkoe ez igéretes
        		return true;
        }
        return false;
    }
    /**
     * act csővekben 'honnan' csőből 'hova' csőbe tesszük át a golyót
     * @param act
     * @param honnan
     * @param hova
     * @return: Az új állapot, vagy null, ha szabálytalan a tevés
     */
    private int[][] getNext( int [][] act, int honnan, int hova )
	{
		if ( isEgyforma(act[honnan] ))	// ha egyforma elemek vannak csak benne
		{
			if ( act[honnan][db-1] != 0 )	// tele van
				return null;	// akkor innenmár nem veszünk ki
			if ( act[hova][0] == 0 )	// a másik üres
				return null;	// nincs értelme átrakni
		}
		if ( act[hova][db-1] != 0  )	// nincs hova tenni, mert tele van;
			return null;
					
		int honnanFelso = getFelso( act[honnan] );	// kivesszük a felső elemet
		int hovaFelso = getFelso( act[hova] );	// kivesszük a felső elemet
		if ( hovaFelso != 0 && honnanFelso != hovaFelso )
			return null;
		if ( isUres(act[hova]) )
		{
			if ( uresCso == (-1) )
			{
				uresCso = hova;
			}
			else
			{
				if ( uresCso != hova )
					return null;
			}
		}
		int[][] ret = getClone(act);	// Minden ellenőrzésen átment, átmásoljuk
		atTesz( ret, honnan, hova );	// most történik az átrakás
		rendbeTesz( ret );	// végrehajtjuk az összes olyan lépést, ami "konszolidálttá" teszi a csöveket
		return ret;
	}
    /**
     * Klónoz egy egy állapotot
     * @param csovek
     * @return
     */
    private int [][] getClone( int [][] csovek)
    {
    	int [][] ret = csovek.clone();	// ez csak a külső részt klónozza 
    	for ( int i=0; i<cntCso; i++ )
    	{
    		ret[i] = csovek[i].clone();
    	}
    	return ret;
    }
    /**
     * Visszaadja a 'cso' legfelső labdáját
     * @param cso
     * @return: a legfelső labda, vagy 0, ha üres a cső
     */
    private int getFelso(int [] cso )
    {
    	for ( int i=0; i<db; i++ )
    	{
    		if ( cso[db-1-i] != 0 )
    			return cso[db-1-i];
    	}
    	return 0;
    }
    /**
     * Megvizsgálja, hogy egy cső üres-e
     * @param cso
     * @return: true: igen üres, false: nem üres
     */
    private boolean isUres(int [] cso )
    {
    	return ( cso[0] == 0 );	// nincs labda az alján
    }
    /**
     * Megvizsgálja, hogy a cső tele van-e
     * @param cso
     * @return: true: igen tele van, nem nincs tele
     */
    private boolean isTele(int [] cso )
    {
    	return ( cso[db-1] > 0 );	// van labda a tetején
    }
    /**
     * Megvizsgálja, hogy a csőben csak egyforma elemek vannak-e (lehet üres is és tele is)
     * @param cso
     * @return: true: igen, egyformák, false: ven benne legalább 2 különböző szín
     */
    private boolean isEgyforma(int [] cso )
    {
    	int prev = -1;
        for ( int i=0; i<db; i++ )
        {
        	if ( cso[i] == 0)	// vége a csőnek
        		break;
        	if ( prev != (-1) && prev != cso[i])
        		return false;
        	prev = cso[i];
        }
    	return true;	// egyformák
    }
    /**
     * Megvizsgálja, hogy a cső jó lesz-e "tarto"-nak, amibe mindenképp pakolni kell
     * Ezt akkor mondjuk, ha csak egyforma elemek vannak benne (de legalább egy és nincs tele a cső)
     * @param cso
     * @return: 'n'>0: igen, ez egy "Tartó" és visszadja, hogy melyik az az elem ami már benne van
     * 			    0: nem jó tartónak, mert vagy különbözőek, vagy üres, vagy teli
     */
    private int getTarto(int [] cso )
    {
    	if ( isUres(cso) || isTele(cso) )	// Ha üres vagy tele, akkor nincs mit nézni
    		return 0;
    	if ( isEgyforma(cso))
    		return cso[0];	// visszaadjuk a legalsó elemet (a többi is ilyen)
    	else
    		return 0;	// nem megfelelő tartónak a cső
    }
    /**
     * A csőbe az első üres helyre beteszi az új labdát
     * Nem vizsgál semmit, itt már minden ellenőrzésen túl vagyunk
     * @param cso
     * @param ujFelso
     * @return
     */
	private void beleTesz( int [] cso, int ujFelso )
	{
    	for ( int i=0; i<db; i++ )
        {
        	if ( cso[i] == 0 )
        	{
        		cso[i] = ujFelso;
        		break;
        	}
        }
	}
	/**
	 * Kiveszi egy csó legfelső elemét és megmondaja melyik volt az
	 * @param cso
	 * @return A kivett elem
	 */
	int kiVesz( int [] cso )
	{
		int ret=0;
		for ( int i=db-1; i>=0; i-- )
        {
        	if ( cso[i] != 0 )
        	{
        		ret = cso[i];	// megjegyezzük
        		cso[i] = 0;		// kivesszük
        		break;
        	}
        }
		return ret;
	}
	/**
	 * 'honnan' cső legfelső labdáját átteszi 'hova' cső tetejére
	 * Itt már semmilyen ellenőrzést nem végez. Ez a tevékenység szabályosan végrehajtható
	 * @param work
	 * @param honnan
	 * @param hova
	 */
	private void atTesz( int [][] csovek, int honnan, int hova )
	{
		int mit = kiVesz( csovek[honnan]);
		beleTesz( csovek[hova], mit );
	}
	/**
	 * Ez egy rekurzív fv.
	 * Addíg futttajuk, míg a csövek állapota "konszolidált" lesz
	 * Ez azt jelenti, hohy hogyha egy csőben csak egyféle színű golyó van, akkor az összes ugyanolyan színű golyót,
	 * ami egy másik cső tetején van beletesszük ebbe a csőbe.
	 * Ezt mindaddig folytatjuk, amíg találunk ilyet.
	 * @param csovek
	 */
	private void rendbeTesz( int [][] csovek )	// végrehajtjuk az összes olyan lépést, ami "konsolidálttá" teszi a csöveket
	{
		for ( int i=0; i<cntCso; i++ )
		{
			int labda = getTarto(csovek[i]);
			if ( labda==0 )
				continue;
			boolean voltValtozas = false;
			for ( int j=0; j<cntCso; j++ )
			{
				if ( i==j )
					continue;
				int mit = getFelso(csovek[j]);
				if ( labda != mit )
					continue;
				atTesz( csovek, j, i );
				voltValtozas = true;
			}
			if ( voltValtozas )
				rendbeTesz( csovek );
		}
	}
	

    	// Kész, ha elértük a célpozíciót
    public boolean isReady()
    {
        for ( int i=0; i<cntCso; i++ )
        {
        	int [] cso = act[i];
        	if ( !isEgyforma(cso) )	// nem egyformák
        		return false;
        	//if ( cso[0] != 0 && cso[db-1] == 0 )	// nem teljesen üres, és nem teljesen teli
        	//	return false;
        }
        return true;
    }
    // Visszatérés true, ha a két állás ugyanaz
    public boolean equals(Gameable game)
    {
    	int[][] other = ((Balls)game).getAct();
        for ( int i=0; i<cntCso; i++ )
        {
            for ( int j=0; j<db; j++ )
            {
            	if ( act[i][j] != other[i][j] )
            		return false;
            }
        }
    	return true;
    }
    public String getPrevStep()
    {
    	String s = (honnan+1) + " --> " + (hova+1);
    	return s;
    }
    
    public String toString()
    {
    	String s = "";
        for ( int i=0; i<cntCso; i++ )
        {
            for ( int j=0; j<db; j++ )
            {
            	s += act[i][j] + ",";
            }
            s += "\t";
        }
    	return s;
    }
    private static int [] cso_0_1 = { 1, 0, 0, 0 };
    private static int [] cso_0_2 = { 2, 2, 2, 2 };
    private static int [] cso_0_3 = { 1, 1, 1, 0 };
    private static int [][] table_0 =
    	  { 
      		cso_0_1,
    		cso_0_2,
            cso_0_3  };
    
    private static int [] cso_ures = { 0, 0, 0, 0 };
    private static int [] cso_2_1 = { 1, 2, 1, 2 };
    private static int [] cso_2_2 = { 2, 1, 2, 1 };
    private static int [][] table_2 =
    	  { 
      		cso_2_1,
    		cso_2_2,
            cso_ures  };
    
    private static int [] cso_4_1 = { 1, 2, 3, 3 };
    private static int [] cso_4_2 = { 1, 2, 1, 2 };
    private static int [] cso_4_3 = { 3, 1, 2, 3 };
    private static int [][] table_4 =
    	  { 
      		cso_4_1,
    		cso_4_2,
    		cso_4_3,
    		cso_ures,
            cso_ures  };
    
    private static int [] cso_5_1 = { 1, 2, 3, 4 };
    private static int [] cso_5_2 = { 2, 1, 3, 4 };
    private static int [] cso_5_3 = { 4, 5, 2, 5 };
    private static int [] cso_5_4 = { 2, 4, 5, 3 };
    private static int [] cso_5_5 = { 1, 1, 5, 3 };
    private static int [][] table_5 =
    	  { 
      		cso_5_1,
    		cso_5_2,
    		cso_5_3,
    		cso_5_4,
    		cso_5_5,
    		cso_ures,
            cso_ures  };
    
    private static int [] cso_50_1 = { 1, 2, 3, 4 };
    private static int [] cso_50_2 = { 5, 6, 6, 6 };
    private static int [] cso_50_3 = { 7, 3, 2, 3 };
    private static int [] cso_50_4 = { 8, 7, 4, 1 };
    private static int [] cso_50_5 = { 1, 7, 2, 8 };
    private static int [] cso_50_6 = { 8, 7, 9, 6 };
    private static int [] cso_50_7 = { 4, 5, 2, 3 };
    private static int [] cso_50_8 = { 5, 9, 1, 9 };
    private static int [] cso_50_9 = { 5, 8, 4, 9 };
    private static int [][] table_50 =
    	  { 
      		cso_50_1,
    		cso_50_2,
    		cso_50_3,
    		cso_50_4,
    		cso_50_5,
    		cso_50_6,
    		cso_50_7,
    		cso_50_8,
    		cso_50_9,
    		cso_ures,
            cso_ures  };
    
    private static int [] cso_105_1 = { 1, 1, 2, 3 };
    private static int [] cso_105_2 = { 4, 5, 6, 7 };
    private static int [] cso_105_3 = { 8, 9, 7, 10 };
    private static int [] cso_105_4 = { 10, 9, 6, 10 };
    private static int [] cso_105_5 = { 3, 11, 9, 2 };
    private static int [] cso_105_6 = { 2, 8, 2, 11 };
    private static int [] cso_105_7 = { 12, 9, 7, 8 };
    private static int [] cso_105_8 = { 12, 6, 7, 12 };
    private static int [] cso_105_9 = { 3, 1, 4, 5 };
    private static int [] cso_105_10 = { 5, 4, 11, 8 };
    private static int [] cso_105_11 = { 3, 12, 11, 4 };
    private static int [] cso_105_12 = { 10, 6, 1, 5 };
    private static int [][] table_105 =
    	  { 
      		cso_105_1,
    		cso_105_2,
    		cso_105_3,
    		cso_105_4,
    		cso_105_5,
    		cso_105_6,
    		cso_105_7,
    		cso_105_8,
    		cso_105_9,
    		cso_105_10,
    		cso_105_11,
    		cso_105_12,
    		cso_ures,
            cso_ures  };
    
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        //Balls game = new Balls( 3, table_0);
        //Balls game = new Balls( 3, table_2);
        //Balls game = new Balls( 5, table_4);
        //Balls game = new Balls( 7, table_5);
        //Balls game = new Balls( 11, table_50);
        Balls game = new Balls( 14, table_105);
        System.out.println(game);
    	GameController gc = new GameController(game); 
        System.out.println();
        System.out.println(game);
        gc.solve();
        System.exit(0);
    }
}