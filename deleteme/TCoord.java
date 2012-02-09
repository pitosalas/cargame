package com.salas;

public class TCoord {
	public int row;
	public int col;
	
	TCoord(int r, int c) {
		row = r;
		col = c;
	}
	
	@Override 
	public boolean equals(Object t) {
		TCoord tt = (TCoord) t;
		if (this == t) return true;
		return (row == tt.row && col == tt.col);
	}
	
	public String toString() {
		return String.format("[%d, %d]", row, col);
	}

	public TPos scale(float factor) {
		return new TPos(col * factor, row * factor);
	}

}
