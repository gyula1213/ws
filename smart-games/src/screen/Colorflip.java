package screen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Colorflip {

	private int size; // T�bla m�rete
	private int[][] startTable = null; // Kiindul� t�bla
	private int cnt = 0; // tippek sz�ma
	private int[][] actTable = null; // Actu�lis t�bla
	private int[][] actSolver = null; // Ahol true van, azokat a mez�ket kell megb�kni a megold�shoz
	private int[][] startSolver = null; // A kiindulo allapot megoldasa
	private int sec = 0;

	public Colorflip() {
	}

	public Colorflip(Scanner reader) {
		String line;
//		while (reader.hasNextLine()) {
//			String line = reader.nextLine();
//			System.out.println(line);
//		}
		line = reader.nextLine(); // Size
		size = Integer.parseInt(line);
		line = reader.nextLine(); // StartTable
		startTable = createTable(line);
		line = reader.nextLine(); // StartSolver
		startSolver = createTable(line);
		line = reader.nextLine(); // Cnt
		cnt = Integer.parseInt(line);
		line = reader.nextLine(); // actTable
		actTable = createTable(line);
		line = reader.nextLine(); // StartSolver
		actSolver = createTable(line);
		line = reader.nextLine(); // Sec
		sec = Integer.parseInt(line);
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	private void generate() {
		if (size % 2 == 0) {
			startTable = genarateEven();
			actSolver = new int[size][size]; // alaphelyzetben hozzuk l�tre
			solve(); // ez egy bokes-sorozat, amivel a rejtveny meg lesz fejtve
		} else {
			actSolver = createRndSolveTable(); // ez egy veletlen bokes-sorozat
			startTable = genarateOdd();
		}
		betterSolve(); // megnezi, hogy a forditottja nem rovidebb-e
		startSolver = copySolver(actSolver); // megjegyezzuk, hogy igy indult
		startGame();
	}

	public void newGame(int size) {
		this.size = size;
		generate();
		startGame();
	}

	public void startGame() {
		actTable = startTable;
		actSolver = copySolver(startSolver); // visszaallitjuk a kiindulo helyzetet
		cnt = 0;
	}

	public void stopGame() {
		startTable = null;
		actTable = null;
		startSolver = null;
		actSolver = null;
		cnt = 0;
	}

	public int[][] getActTable() {
		return actTable;
	}

	public int getSize() {
		return size;
	}

	/**
	 * Paros mezore veletlen generalas
	 * 
	 * @param tomb
	 */
	private int[][] genarateEven() {
		int[][] ret = new int[size][size];
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[i].length; j++) {
				ret[i][j] = (int) (Math.random() * 2);
			}
		}
		return ret;
	}

	/**
	 * Paratlan mezore megoldasbol kiindulo generalas
	 * 
	 * @param tomb
	 */
	private int[][] genarateOdd() {
		int[][] ret = new int[size][size]; // kiindulo, ures tomb
		for (int i = 0; i < actSolver.length; i++) {
			for (int j = 0; j < actSolver[i].length; j++) {
				if (actSolver[i][j] == 1)
					ret = step(ret, (i + 1), (j + 1));
			}
		}
		return ret;
	}

	private void printTable(int[][] tomb) {
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				String s = (tomb[i][j] == 1) ? "X" : "O";
				System.out.print(s);
			}
			System.out.println();
		}
	}

	public void printActTable() {
		System.out.println("Number of steps: " + cnt);
		printTable(actTable);
	}

	public void printActSolver() {
		int[][] tomb = actSolver;
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				String s = (tomb[i][j] == 1) ? "+" : "-";
				System.out.print(s);
			}
			System.out.println();
		}
	}

	private int[][] step(int[][] tomb, int x, int y) {
		int[][] ret = new int[size][size];
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				if (x == (i + 1) || y == (j + 1))
					ret[i][j] = 1 - tomb[i][j]; // megford�tjuk
				else
					ret[i][j] = tomb[i][j];

			}
		}
		return ret;
	}

	public void step(int row, int col) {
		actTable = step(actTable, row, col);
		changeSolve(row - 1, col - 1);
		cnt++;
	}

	public int getCnt() {
		return cnt;
	}

	/**
	 * Visszat�r�s true, ha mind egyforma, false, ha nem
	 * 
	 * @param tomb
	 * @return
	 */
	private boolean checkTable(int[][] tomb) {
		int ref = tomb[0][0];
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				if (tomb[i][j] != ref)
					return false;
			}
		}
		return true;
	}

	/**
	 * Visszat�r�s true, ha mind egyforma, false, ha nem
	 * 
	 * @param tomb
	 * @return
	 */
	public boolean checkActTable() {
		return checkTable(actTable);
	}

	/**
	 * Egy veletlen bokessorozatot allit elo
	 */
	private int[][] createRndSolveTable() {
		int[][] ret = new int[size][size];
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[i].length; j++) {
				if ((int) (Math.random() * 2) == 1) // minden mezorol veletlenul eldontjuk, hogy megboktuk-e vagy nem
					ret[i][j] = 1;
			}
		}
		return ret;

	}

	/**
	 * Egy megoldas bokessorozatot allit elo
	 */
	private void solve() {
		int ref = startTable[0][0];
		for (int i = 0; i < startTable.length; i++) {
			for (int j = 0; j < startTable[i].length; j++) {
				if (startTable[i][j] != ref) // ami nem olyan, mint a ref azt megforditjuk
					turnOneField(i, j);
			}
		}
	}

	/**
	 * Egy mezo megforditasa ugy, hogy r�b�k�nk az �sszes soraban, vagy oszlop�ban
	 * �llo mezore A bokeseket megjegyezzuk
	 */
	private void turnOneField(int x, int y) {
		for (int i = 0; i < startTable.length; i++) {
			for (int j = 0; j < startTable[i].length; j++) {
				if (i == x || j == y) // abban a sorban, vagy oszlopban van
				{
					changeSolve(i, j); // solverTable megjegyzi a bokeseket
				}
			}
		}
	}

	/**
	 * megnezi, hogy a forditottja nem rovidebb-e ha igen, akkor megford�tja a
	 * bokesi tombot
	 */
	private void betterSolve() {
		int cnt = 0;
		for (int i = 0; i < actSolver.length; i++) {
			for (int j = 0; j < actSolver[i].length; j++) {
				if (actSolver[i][j] == 1)
					cnt++;
			}
		}
		if (cnt <= size * size / 2) // nem tudunk javitani
			return;
		// Tudunk javitani ugy, hogy mindent megforditunk
		for (int i = 0; i < actSolver.length; i++) {
			for (int j = 0; j < actSolver[i].length; j++) {
				changeSolve(i, j);
			}
		}
	}

	/**
	 * lemasolja solverTabla tartalmat ha igen, akkor megford�tja a bokesi tombot
	 */
	private int[][] copySolver(int[][] old) {
		int[][] ret = new int[size][size];
		for (int i = 0; i < actSolver.length; i++) {
			for (int j = 0; j < actSolver[i].length; j++) {
				ret[i][j] = old[i][j];
			}
		}
		return ret;
	}

	public boolean isInSolve(int i, int j) {
		return actSolver[i][j] == 1;
	}

	private void changeSolve(int i, int j) {
		actSolver[i][j] = 1 - actSolver[i][j];
	}

	private String getString(int[][] tomb) {
		String s = "";
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				s += tomb[i][j] + " ";
			}
		}
		return s;
	}

	/**
	 * Elmenti az aktualis allast size startTable startSolver cnt actTable actSolver
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void saveFile(FileWriter writer) throws IOException {
		writer.write(size + "\n");
		writer.write(getString(startTable) + "\n");
		writer.write(getString(startSolver) + "\n");
		writer.write(cnt + "\n");
		writer.write(getString(actTable) + "\n");
		writer.write(getString(actSolver) + "\n");
		writer.write(sec + "\n");
	}

	private int[][] createTable(String s) {
		String[] f = s.split(" ");
		int[][] ret = new int[size][size];
		for (int i = 0; i < ret.length; i++) {
			for (int j = 0; j < ret[i].length; j++) {
				int x = Integer.parseInt(f[i * size + j]);
				ret[i][j] = x;
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out.println("Program started");
		Colorflip colorflip = new Colorflip();
		//ColorflipConsole cc = new ColorflipConsole(colorflip);
		System.out.println("End of program");
	}

}
