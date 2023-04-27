import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ColorflipConsole {

	private Colorflip colorflip;

	private int size; // T�bla m�rete
	private int x, y; // Aktu�lis tipp koordin�t�i

	private boolean isCommandSize = false;
	private boolean isPlaying = false;

	public ColorflipConsole(Colorflip colorflip) {
		this.colorflip = colorflip;
		consoleProgram();
	}

	private void consoleProgram() {
		if (!work("help"))
			return;
		try (Scanner consoleRead = new Scanner(System.in)) {
			while (consoleRead.hasNextLine()) {
				String inpLine = consoleRead.nextLine();
				// System.out.println(inpLine);
				if (!work(inpLine.toLowerCase()))
					break;
			}
		}
	}

	/**
	 * Be��l�tja a t�bla m�ret�t Visszat�r�s false, ha hib�s a tipp
	 */
	private boolean checkSize(String s) {
		try {
			size = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Be��l�tja x,y koordin�t�kat Visszat�r�s false, ha hib�s a tipp
	 */
	private boolean checkTipp(String s) {
		String[] f = s.split(" ");
		if (f.length != 2)
			return false;
		try {
			x = Integer.parseInt(f[0]);
			y = Integer.parseInt(f[1]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private void saveFile() {
		try {
			FileWriter myWriter = new FileWriter("oldgame.txt");
			colorflip.saveFile(myWriter);
			myWriter.close();
			System.out.println("Successfully wrote to the file \"oldgame.txt\".");
		} catch (IOException e) {
			System.out.println("Unsuccessfully save.");
		}
	}

	private boolean continueOldGame() {
		try {
			File myObj = new File("oldgame.txt");
			Scanner myReader = new Scanner(myObj);
			colorflip = new Colorflip(myReader);
			myReader.close();
			isPlaying = true;
			size = colorflip.getSize();
			System.out.println("Loaded file \"oldgame.txt\".");
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			System.out.println("Check the file: " + "oldgame.txt");
			return false;
		}
		return true;
	}

	/**
	 * Commands: start: J�t�k ind�t�sa. �j t�bla gener�l�sa, vagy r�gi folytat�sa
	 * exit: Kil�p�s a j�t�kb�l stop: Aktu�lis j�t�k felad�sa save: Aktu�lis j�t�k
	 * ment�se solve: Solver ind�t�sa
	 * 
	 * @param command
	 * @return
	 */
	private boolean work(String command) {
		if ("exit".equals(command)) { // Program v�ge, b�rnikor j�het ez a parancs
			return false;
		}
		if ("help".equals(command) || "h".equals(command)) { // Help
			System.out.println("Commands:");
			System.out.println("Start: New game");
			System.out.println("Old: Reloading old game");
			System.out.println("Stop: Stop actual game");
			System.out.println("Restart: Restart actual game");
			System.out.println("Solver: Show the way for a result");
			System.out.println("Print: Show actual table");
			System.out.println("Save: save actual game");
			System.out.println("Exit: End of game");
			System.out.println("Help: This screen");
			return true;
		}
		if ("start".equals(command)) { // �j j�t�k ind�t�sa, csak, ha nincs akt�v j�t�k
			if (isPlaying) {
				System.out.println("Command valid only out of the game. Press >stop<");
				return true;
			}
			isCommandSize = true;
			System.out.println("Size? [2-20]");
			isPlaying = false;
			return true;
		}
		if ("old".equals(command)) { // R�gi j�t�k ind�t�sa, csak, ha nincs akt�v j�t�k
			if (isPlaying) {
				System.out.println("Command valid only out of the game. Press >stop<");
				return true;
			}
			if (!continueOldGame())
				return true;
			colorflip.printActTable();
			isPlaying = true;
			System.out.println("First tipp? (row col)");
			return true;
		}
		if ("restart".equals(command)) { // J�t�k �jtaind�t�sa, csak j�t�k k�zben van �rtelme
			if (!isPlaying) {
				System.out.println("Command valid only in the game. Press >start<, >old<");
				return true;
			}
			colorflip.startGame();
			colorflip.printActTable();
			System.out.println("Next tipp? (row col)");
			return true;
		}
		if ("save".equals(command)) { // J�t�k elment�se, csak j�t�k k�zben van �rtelme
			if (!isPlaying) {
				System.out.println("Command valid only in the game. Press >start<, >old<");
				return true;
			}
			saveFile();
			return true;
		}
		if ("solver".equals(command)) { // Megold�s mutat�sa
			if (!isPlaying) {
				System.out.println("Command valid only in the game. Press >start<, >old<");
				return true;
			}
			colorflip.printActSolver();
			System.out.println("Next tipp? (row col)");
			return true;
		}
		if ("print".equals(command)) { // Pillanatnyi �llapot mutat�sa
			if (!isPlaying) {
				System.out.println("Command valid only in the game. Press >start<, >old<");
				return true;
			}
			colorflip.printActTable();
			System.out.println("Next tipp? (row col)");
			return true;
		}
		if ("stop".equals(command)) { // J�t�k le�ll�t�sa, csak ha megy
			if (!isPlaying && !isCommandSize) {
				System.out.println("Command valid only in the game. Press >start<, >old< or >exit<");
				return true;
			}
			isCommandSize = false;
			isPlaying = false;
			colorflip.stopGame();
			System.out.println("Stop of this game");
			return true;
		}
		if (isCommandSize) { // J�t�k indul�s�n�l vagyunk, most csak m�retet fogad el
			if (!checkSize(command)) {
				System.out.println("Need number [2-20]");
				return true;
			}
			if (size < 2 || size > 20) {
				System.out.println("Size must be between 2 and 20");
				return true;
			}
			colorflip.newGame(size);
			System.out.println("Table generated");
			colorflip.printActTable();
			isCommandSize = false;
			isPlaying = true;
			System.out.println("First tipp? (row col)");
			return true;
		}
		if (isPlaying) {
			if (!checkTipp(command)) {
				System.out.println("Need two number: row col, or a valid command");
				return true;
			}
			if (x < 1 || x > size || y < 1 || y > size) {
				System.out.println("Numbers must be between 1 and " + size);
				return true;
			}
			colorflip.step(x, y);
			colorflip.printActTable();
			if (colorflip.checkActTable()) {
				System.out.println("Gratula, you are ready in " + colorflip.getCnt() + " step");
				isPlaying = false;
			} else
				System.out.println("Next tipp? (row col)");
			return true;
		}
		System.out.println("Invalid command. Press >help<");
		return true;
	}
}
