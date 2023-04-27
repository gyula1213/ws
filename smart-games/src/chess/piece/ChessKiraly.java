package chess.piece;

import java.awt.Point;

public class ChessKiraly extends ChessPiece {

	protected ChessKiraly(Point position) {
		super(position);
		type = ChessPieceType.KIRALY;
	}

	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;

		int absMoveX = Math.abs(newPos.x-position.x);
		int absMoveY = Math.abs(newPos.y-position.y);
		
		return absMoveX<=1 && absMoveY <=1;
	}
	
	@Override
	public boolean isAkadalyozhato() {
		return false;
	}


}
