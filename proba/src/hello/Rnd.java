package hello;

public class Rnd {

	public static void main(String[] args) {
		int oldal=50;
		int db=100000000;
		int []counter = new int [oldal];
		for( int i=0; i<db; i++ ) {
			double d = Math.random();
			int x = (int)(oldal*d);
			counter[x]++;
		}
		for( int i=0; i<oldal; i++ ) {
			double dd = 100*(double)counter[i]/db;
			System.out.println((i+1) + ": " + counter[i] + "  " + dd + "%");
		}
	}

}
