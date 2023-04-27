package chess.piece;

import java.awt.Point;

public class ChessGyalog extends ChessPiece {


	protected ChessGyalog(Point position) {
		super(position);
		type = ChessPieceType.GYALOG;
	}

	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;
		//Felfelé üt, balra vagy jobbra
		return (position.x-newPos.x)==1 && Math.abs(newPos.y-position.y)==1;
	}
	
	@Override
	public boolean isAkadalyozhato() {
		return false;
	}


}
