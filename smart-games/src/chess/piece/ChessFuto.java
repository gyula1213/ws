package chess.piece;

import java.awt.Point;

public class ChessFuto extends ChessPiece {

	protected ChessFuto(Point position) {
		super(position);
		type = ChessPieceType.FUTO;
	}

	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;

		int moveX = newPos.x-position.x;
		int moveY = newPos.y-position.y;
		
		return Math.abs(moveX)==Math.abs(moveY);
	}
	
	@Override
	public boolean isAkadalyozhato() {
		return true;
	}


}
