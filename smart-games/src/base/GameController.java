package base;

import java.util.HashMap;
import java.util.Vector;

import sakk.Sakk;
import games.Tilt;
import games.Virus;

// Logikai játékokat kezel
	//  0. Game -- Egy egyszerű minta, hogy mire is jó ez az egész
    //  1. HotSpot
    //  2. Hdos -- http://www.kongregate.com:80/games/GameReclaim/hdos-databank-request-01
	//  3. Vírus (átlós mozgással
	//	4. Tilt (ami meg van írva javascriptben)
public class GameController
{
	private Gameable game;	// az aktuális játék, amit éppen játszunk
	
//    private HashMap<Gameable,Vector<Gameable>> collStages; 	// Itt gyűjtjük a játékokat jellemző állapotot
    private HashMap<Gameable,Gameable> collStages; 	// Itt gyűjtjük a játékokat jellemző állapotot
    private int cntStep=0;
    private Vector <Gameable> hopefulStages = new Vector<>();	// Itt gyűjtjük azokaz az állapotokat, amit egy estleges "takarítás" után is megtartanánk
    private int maxStages = 0;	// Ha 0-nál nagyobb, akkor maximum ennyi stage után leáll a fejtés
    
    public GameController( Gameable game )
    {
    	this.game = game;
    }
    
	public void setMaxStages(int maxStages) {
		this.maxStages = maxStages;
	}

	public int solve()
    {
		collStages = new HashMap<>();
		Vector <Gameable> stagesForNext = new Vector<>();
		stagesForNext.add(game);
//		collStages.put(game,stagesForNext);
		collStages.put(game,null);
		if ( game.isReady() )
	    {
			System.out.println( "Kész! " );
			return 0;
	    }
		return solver(stagesForNext);
    }
	public int solver(Vector <Gameable> actStages)
    {
		System.out.println( (cntStep++) + ". step ::" + collStages.size() + " stages" );
		if ( actStages.size() == 0 )
	    {
			System.out.println("Nincs megoldás:");
			return 0;
	    }
		Vector <Gameable> stagesForNext = new Vector<>();
		if ( game.isCriticalDeep(cntStep) )
		{
				// Újrakezdjük a munkát, csak az igéretes állapotokat tartjuk meg
			System.out.println( (cntStep) + ". step ::" + " Cleaning!!!" );
			if ( hopefulStages.size() == 0 )	// nincs igéretes állapot
		    {
				System.out.println("Takarítás után nincs igéretes állapot:");
				return 0;
		    }
			HashMap<Gameable,Gameable> saveCollStages = new HashMap<>(); 	// megmentjük, amire később is sükség lesz
			for(Gameable g: hopefulStages)
		    {
//				Vector <Gameable> eddigStages = collStages.get(g);
//				saveCollStages.put(g,eddigStages);	// Átmásoljuk az állapotot
				saveCollStages.put(g,collStages.get(g));	// Átmásoljuk az állapotot
		    }
			collStages = saveCollStages;	// amit régebben gyűjtöttünk, annak a helyére tesszük
			stagesForNext = hopefulStages;
			hopefulStages = new Vector<>();
			return solver(stagesForNext);
		}
		for(Gameable g: actStages)
	    {
			//Vector <Gameable> eddigStages = collStages.get(g);
			
			Vector <Gameable> nextStages = g.nextStages();
			for(Gameable stage: nextStages)
		    {
				//System.out.println(stage);
				if ( collStages.get(stage) != null )
					continue;
				//Vector <Gameable> mostStages = new Vector<>(eddigStages);
				//mostStages.add(stage);
				//collStages.put(stage,mostStages);
				collStages.put(stage,g);
				//System.out.println(((Gameable)stage).getId());
				//System.out.println(((Gameable)stage).getHistory());
				if ( stage.isReady() )
			    {
					//System.out.println("Kész vagyunk:");
					//System.out.println(((Gameable)stage).getHistory());
					printHistory( stage );
					return 1;	// TODO, ha az összes megoldás kell, akkor ki kell venni ezt a sort
			    }
				if ( stage.isHopefulStage() )	// az igéretes állapotokat megjegyezzük
			    {
					//System.out.println("Igéretes állapot:");
					//System.out.println(stage);
					hopefulStages.add(stage);
			    }
				stagesForNext.add(stage);
				int x = collStages.size();
				if (x%1000000 == 0)
			    {
					System.out.println(" stages: " + x);
			    }
				if ( maxStages > 0 && collStages.size() > maxStages )
			    {
					System.out.println(maxStages + " stage felhasználásával nem tudtuk megfejteni:");
					return 0;
			    }
		    }
	    }
		return solver(stagesForNext);
    }
	private void printHistory( Gameable g )
    {
//	    Vector<Gameable> histStages = collStages.get(g);
//	    System.out.println("Lépések száma: " + (histStages.size()-1));
//	    for ( int i=1; i<histStages.size(); i++ )
//	    {
//	    	Gameable game = histStages.get(i);
//	    	//System.out.println(game);
//	    	System.out.print(game.getPrevStep()+",");
//	    }
		
	    String s = "";
	    int cnt = 1;
	    Gameable prevStage = collStages.get(g);
	    String prevStep = g.getPrevStep();
	    int tmp=1;
	    while( true )
	    {
	    	if ( prevStage == null )
	    		break;
	    	String act = prevStage.getPrevStep();
	    	if ( "init".equals(act)) 
	    		break;
	    	if ( act.equals(prevStep)) {
	    		tmp++;
	    	}
	    	else {
	    		if ( prevStep.length() > 0 ) {
			    	if ( tmp > 1 )
			    	{
				    	s = prevStep + "(" + tmp + "), " + s;
			    	}
			    	else
			    	{
				    	s = prevStep + ", " + s;
			    	}
	    		}
	    		tmp=1;
		    	prevStep = act;
	    	}
	    	cnt++;
	    	prevStage = collStages.get(prevStage);
	    }
    	if ( tmp > 1 )
    	{
	    	s = prevStep + "(" + tmp + "), " + s;
    	}
    	else
    	{
	    	s = prevStep + ", " + s;
    	}
	    System.out.println("Lépések száma: " + cnt);
    	System.out.println(s);
    	System.out.println();
    	System.out.println();
    	System.out.println(g);
    }
    public static void main(String[] args)
    {
        System.out.println("Indul...");
        int item = 30;
        int from = 1;
        int to = 60;
        if (item >= 0) 
        {
            from = item;
            to = item;
        }
        for ( int i=from; i<=to; i++ )
        {
        	//Gameable game = new Game( 3,3,5,7 );
        	//Gameable game = new HotSpot( i );
        	//Gameable game = new Virus( i );
        	//Gameable game = new Hdos( i );
        	//Gameable game = new Virus( i );
        	//Gameable game = new Tilt( i );
        	//Gameable game = new KockaOszlop( i );
        	//Gameable game = new Virus2( i );
        	Gameable game = new Sakk( i );
        	//Gameable game = new Domino( i );
        	//Gameable game = new Kereszt( i );
        	//Gameable game = new RushHour( i );
        	GameController gc = new GameController(game); 
            System.out.println();
            System.out.println("--Game-- " + (i) + " --");
            System.out.println(game);
            gc.solve();
        }
        System.out.println("\n--Vége--");
        System.exit(0);
    }
}