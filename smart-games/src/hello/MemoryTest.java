package hello;

import java.util.ArrayList;
import java.util.HashMap;

import base.Gameable;
import kocka.Kocka2x2x2;
import kocka.kocka4.Kocka4x4x4;

public class MemoryTest {
	
	int z;
	byte [] x = new byte[24];
	
	public MemoryTest() {
	}

	private static ArrayList l = new ArrayList();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world!");
	    HashMap<MemoryTest,MemoryTest> collStages = new HashMap<>(); 	// Itt gyűjtjük a játékokat jellemző állapotot
		
	    int [] act = new int [96];
	    act [1] = 1;
	    act [2] = 2;
	    act [3] = 3;
	    act [5] = 3;
	    act [6] = 2;
	    act [7] = 1;
	    byte[] ret = zip(act);
	    int [] act2 = unzip(ret);
	    for ( int i=0; i<12; i++ )
	    	System.out.println( act2[i] + ", ");
		int cnt = 0;
		while (true)
		{
			if ( 1==1)
				break;
			cnt++;
			MemoryTest mt = new MemoryTest();
			//Kocka2x2x2 mt = new Kocka2x2x2(0);
			collStages.put(mt, mt);
			l.add(mt);
			if ( cnt%100000 == 0 )
			{
				System.out.println( cnt);
			}
		}

	}
	/**
	 * Tömörítés
	 * @param act
	 * @return
	 */
    public static byte [] zip( int [] act) {
    	byte [] ret = new byte [act.length];
    	for ( int i=0; i<act.length; i++ )
    		ret [i] = (byte)act[i];
    	return ret;
    }
	/**
	 * Tömörítés
	 * @param act
	 * @return
	 */
    public static int [] unzip( byte [] act) {
    	int [] ret = new int [act.length];
    	for ( int i=0; i<act.length; i++ )
    		ret [i] = act[i];
    	return ret;
    }
//    public static byte [] zip( byte [] act) {
//    	byte [] ret = new byte[24];
//    	for ( int i=0; i<24; i++ )
//    	{
//    		byte a1 = (byte) (act[4*i] << 6);
//    		byte a2 = (byte) (act[4*i+1] << 4);
//    		byte a3 = (byte) (act[4*i+2] << 2);
//    		byte a4 = act[4*i+3];
//    		ret[i] = (byte) (a1 | a2 | a3 | a4) ; 
//    	}
//    	return ret;
//    }
//    public static byte [] unzip( byte [] act) {
//    	byte [] ret = new byte[96];
//    	for ( int i=0; i<24; i++ )
//    	{
//    		ret[4*i] = (byte) ((act[i] & 192)>>6 );
//    		ret[4*i+1] = (byte) ((act[i] & 48)>>4);
//    		ret[4*i+2] = (byte) ((act[i] & 12)>>2);
//    		ret[4*i+3] = (byte) (act[i] & 3);
//    	}
//    	return ret;
//    }
}
