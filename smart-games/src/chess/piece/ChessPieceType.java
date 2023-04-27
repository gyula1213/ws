package chess.piece;

import java.awt.Point;

public enum ChessPieceType{
	GYALOG(6) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessGyalog(position);
		}
	},
	FUTO(4) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessFuto(position);
		}
	},
	HUSZAR(5) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessHuszar(position);
		}
	},
	BASTYA(3) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessBastya(position);
		}
	},
	VEZER(2) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessVezer(position);
		}
	},
	KIRALY(1) {
		@Override
		public ChessPiece newInstance(Point position) {
			return new ChessKiraly(position);
		}
	}
	;
	public int value;
	ChessPieceType(int value){
		this.value = value;
	}
	public abstract ChessPiece newInstance(Point position);
	
	public static ChessPieceType getTypeByValue(int value){
		for (ChessPieceType type:ChessPieceType.values()){
			if (value == type.value)
				return type;
			
		}
		throw new IllegalArgumentException("A megadott értékhez nem tartozik bábu. VALUE: "+value);
		
	}
	
	 /* A bábuk:
     * Király: 1
     * Vezér: 2
     * Bástya: 3
     * Futó: 4
     * Huszár: 5
     * Paraszt: 6
     */
}
