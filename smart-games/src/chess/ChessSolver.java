package chess;

import chess.piece.ChessPiece;
import chess.piece.ChessPieceType;
import sakk.Sakk;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChessSolver {
	
	private static List<ChessTable> solutions = new LinkedList<>(); 
	
	public static void main(String[] args) {
		
		
		ChessTable startTable = getTableByArray(30);
		
		List<ChessTable> tabs = new LinkedList<>();
		tabs.add(startTable);
		System.out.println("*** START ***");
		System.out.println(tabs);
		System.out.println("-------------------");
		solve(tabs);
		
		System.out.println("*** SOLUTIONS ***");
		System.out.println(solutions.size());
		System.out.println(solutions);
	}
	
	public static ChessTable getTableByArray(int index){
		List<ChessPiece> pieces = new LinkedList<>();
		
		int[] array = Sakk.tables[index];
		printTable(array);
		
		for (int i=0;i<array.length;i++){
			if (array[i] != 0){
				Point pos = new Point(i/4+1, i%4+1);
				ChessPieceType type = ChessPieceType.getTypeByValue(array[i]);
				pieces.add(ChessPiece.newInstance(type,pos));
			}
		}
		
		return new ChessTable(pieces, Collections.<String> emptyList());
	}
	
	public static void printTable(int[] array){
		String s="";
        String c="";
        int cntCol=4;
        for ( int i=0; i<array.length; i++ )
        {
            switch ( array[i] )
            {
            case 0:
            	c = "-";
            	break;
            case 1:
            	c = "K";
            	break;
            case 2:
            	c = "V";
            	break;
            case 3:
            	c = "B";
            	break;
            case 4:
            	c = "F";
            	break;
            case 5:
            	c = "H";
            	break;
            case 6:
            	c = "P";
            	break;
            }
            s += c;
            if ( (i+1)%cntCol == 0 )
                s += "\n";
            else
                s += " ";
        }
        System.out.println(s);
	}
	
	public static ChessTable getTable(int index){
		List<ChessPiece> table = new LinkedList<>();
		
		if (index ==0 ){
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(1,1)));
			//table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(1,2)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(3,1)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(3,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(3,4)));
			//table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(1,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(1,4)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(2,1)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(4,1)));
		}

		
		if (index==1){
			table.add(ChessPiece.newInstance(ChessPieceType.HUSZAR, new Point(1,4)));
			table.add(ChessPiece.newInstance(ChessPieceType.FUTO, new Point(2,2)));
			table.add(ChessPiece.newInstance(ChessPieceType.GYALOG, new Point(1,3)));
		} else if (index == 14){
			table.add(ChessPiece.newInstance(ChessPieceType.HUSZAR, new Point(1,2)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(2,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.BASTYA, new Point(2,4)));
			table.add(ChessPiece.newInstance(ChessPieceType.FUTO, new Point(4,1)));
		} else if (index == 15){
			table.add(ChessPiece.newInstance(ChessPieceType.HUSZAR, new Point(1,4)));
			table.add(ChessPiece.newInstance(ChessPieceType.FUTO, new Point(2,2)));
			table.add(ChessPiece.newInstance(ChessPieceType.GYALOG, new Point(1,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.BASTYA, new Point(3,3)));
		} else if (index == 31){
			table.add(ChessPiece.newInstance(ChessPieceType.GYALOG, new Point(1,4)));
			table.add(ChessPiece.newInstance(ChessPieceType.BASTYA, new Point(2,1)));
			table.add(ChessPiece.newInstance(ChessPieceType.KIRALY, new Point(2,2)));
			table.add(ChessPiece.newInstance(ChessPieceType.VEZER, new Point(2,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.GYALOG, new Point(3,3)));
			table.add(ChessPiece.newInstance(ChessPieceType.BASTYA, new Point(4,3)));
		}
		
		return new ChessTable(table, Collections.<String> emptyList());
		
	}
	
	
	
	
	public static void solve(List<ChessTable> tabs){
		List<ChessTable> newTabs = new LinkedList<>();
		
		for (ChessTable tab:tabs){
			newTabs.addAll(getNextTabs(tab));
		}
		
		//Elfogytak a lehetőségek
		if (newTabs.size()==0)
			return;

		System.out.println(newTabs.size());
		//printTabs(newTabs);
		System.out.println("-------------------");
		//Jöhet a következő iteráció
		solve(newTabs);
		
	}
	
	private static void printTabs(List<ChessTable> tabs){
		for (ChessTable tab:tabs){
			System.out.println(tab);
		}
	}
	
	private static List<ChessTable> getNextTabs(ChessTable tab){
		List<ChessTable> newTabs = new LinkedList<>();
		
		List<ChessPiece> pieces = tab.getPieces();
		
		//Az i figurával lépünk minden helyre, ahová lehet
		for (int i=0;i<pieces.size();i++){
			//Minden j-t meg kell vizsgálni, hogy oda lehet-e
			for (int j=0;j<pieces.size();j++){
				if (tab.canMove(i, j)){
						
					//Kell egy új tábla
					ChessTable newTab = createNewTab(tab, i, j);
					
					if (newTab.getNumberOfPieces()==1){
						solutions.add(newTab);
					} else {
						newTabs.add(newTab);
					}
				}
			}
		}
		return newTabs;
	}
	
	private static ChessTable createNewTab(ChessTable tab, int selectedPiece, int targetPiece){
		List<ChessPiece> newPieces = new LinkedList<>();
		
		List<ChessPiece> pieces = tab.getPieces();
		String move = "";
		
		for (int i=0;i<pieces.size();i++){
			if (i != selectedPiece && i != targetPiece){
				newPieces.add(pieces.get(i));
			} else if (i == targetPiece){
				ChessPiece selected = pieces.get(selectedPiece);
				ChessPiece target = pieces.get(targetPiece);
				
				ChessPieceType type = selected.getType();
				Point pos = pieces.get(targetPiece).getPosition();
				newPieces.add(ChessPiece.newInstance(type, pos));
				
				move = getMove(selected, target);
			}
		}
		
		List<String> moves = tab.getMoves();
		moves.add(move);
		
		return new ChessTable(newPieces, moves);
	}




	private static String getMove(ChessPiece selected, ChessPiece target) {
		Point p0 = selected.getPosition();
		Point p1 = target.getPosition();
		
		return "["+p0.x+", "+p0.y+"] - ["+p1.x+", "+p1.y+"] "+selected.getType();
	}
	

}
