package chess.piece;

import java.awt.Point;

public class ChessVezer extends ChessPiece {

	protected ChessVezer(Point position) {
		super(position);
		type = ChessPieceType.VEZER;
	}
	
	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;

		int moveX = newPos.x-position.x;
		int moveY = newPos.y-position.y;
		
		return moveX==0 || moveY==0 || Math.abs(moveX)==Math.abs(moveY);
	}
	
	@Override
	public boolean isAkadalyozhato() {
		return true;
	}

}
