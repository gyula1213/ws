package chess.piece;

import java.awt.Point;

public class ChessHuszar extends ChessPiece {

	protected ChessHuszar(Point position) {
		super(position);
		type = ChessPieceType.HUSZAR;
	}

	@Override
	public boolean canMove(Point newPos) {
		if (newPos.x==position.x && newPos.y==position.y)
			return false;
		
		int absMoveX = Math.abs(newPos.x-position.x);
		int absMoveY = Math.abs(newPos.y-position.y);
		
		return absMoveX==2 && absMoveY == 1 || absMoveX==1 && absMoveY == 2;
	}
	
	@Override
	public boolean isAkadalyozhato() {
		return false;
	}


}
