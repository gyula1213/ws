package chess;

import chess.piece.ChessPiece;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class ChessTable {
	
	private List<ChessPiece> pieces;
	private List<String> moves; 
	
	ChessTable(List<ChessPiece> pieces, List<String> moves){
		this.pieces = new LinkedList<ChessPiece>(pieces);
		this.moves = new LinkedList<String>(moves);
	}
	
	public int getNumberOfPieces(){
		return pieces.size();
	}
	
	public List<ChessPiece> getPieces(){
		return new LinkedList<ChessPiece>(pieces);
	}
	
	public List<String> getMoves(){
		return new LinkedList<String>(moves);
	}
	
	public boolean canMove(int selected, int target){
		if (selected==target)
			return false;
		
		ChessPiece selectedPiece = pieces.get(selected);
		ChessPiece targetPiece = pieces.get(target);
		
		//Át tud menni elvileg?
		if (!selectedPiece.canMove(targetPiece.getPosition()))
				return false;
		//Akadályozható a bábu?
		if (!selectedPiece.isAkadalyozhato())
			return true;
		
		//Át tud menni, de akadályozható, meg kell nézni, hogy van-e akadály
		
		Point p0 = selectedPiece.getPosition();
		Point p1 = targetPiece.getPosition();
		
		for (int i=0;i<pieces.size();i++){
			if (i==selected || i==target)
				continue;
			
			Point pC = pieces.get(i).getPosition();
			
			if (isBetween(pC, p0, p1))
				return false;
		}
		
		return true;
	}
	
	private boolean isBetween(Point currPoint, Point point1, Point point2) {
		int dxc = currPoint.x - point1.x;
		int dyc = currPoint.y - point1.y;

		int dxl = point2.x - point1.x;
		int dyl = point2.y - point1.y;

		int cross = dxc * dyl - dyc * dxl;
		
		if (cross != 0)
			  return false;
		
		if (Math.abs(dxl) >= Math.abs(dyl)){
		  return dxl > 0 ? 
				    point1.x <= currPoint.x && currPoint.x <= point2.x :
				    point2.x <= currPoint.x && currPoint.x <= point1.x;
		} else {
		  return dyl > 0 ? 
				    point1.y <= currPoint.y && currPoint.y <= point2.y :
				    point2.y <= currPoint.y && currPoint.y <= point1.y;
		}
	}

	@Override 
	public String toString(){
		return " Lépések: "+moves+ ", Bábuk: "+ pieces.toString();
	}
}
