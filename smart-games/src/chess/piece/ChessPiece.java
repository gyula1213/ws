package chess.piece;

import java.awt.Point;

public abstract class ChessPiece {
	protected ChessPieceType type;
	protected final Point position;
	
	protected ChessPiece(Point position){
		this.position = new Point(position.x, position.y);
	}
	
	public Point getPosition(){
		return new Point(position.x, position.y);
	}
	
	public ChessPieceType getType(){
		return type;
	}

	public ChessPiece move(Point newPos){
		return newInstance(type, newPos); 
	}

	public abstract boolean canMove(Point newPos);
	public abstract boolean isAkadalyozhato();
	
	public static ChessPiece newInstance(ChessPieceType type, Point position){
		return type.newInstance(position);
	}
	
	@Override 
	public String toString(){
		return type.toString()+" ["+position.x+", "+position.y+"]";
	}
}
