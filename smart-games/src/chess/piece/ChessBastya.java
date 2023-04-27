package chess.piece;

import java.awt.Point;

public class ChessBastya extends ChessPiece {

	protected ChessBastya(Point position) {
		super(position);
		type = ChessPieceType.BASTYA;
	}

	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;

		int moveX = newPos.x-position.x;
		int moveY = newPos.y-position.y;
		
		return moveX==0 || moveY==0;
	}

	@Override
	public boolean isAkadalyozhato() {
		return true;
	}

}
