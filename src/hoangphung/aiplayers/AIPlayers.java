package hoangphung.aiplayers;

import hoangphung.graphics.Board;
import hoangphung.graphics.Cell;
import hoangphung.graphics.GameMain;
import hoangphung.graphics.Seed;

public abstract class AIPlayers {

	protected int ROWS = GameMain.ROWS;
	protected int COLS = GameMain.COLS;
	protected Cell[][] cells;
	protected Seed mySeed; // computer's seed
	protected Seed oppSeed; // opponent's seed

	public AIPlayers(Board board) {
		cells = board.cells;
	}

	public void setSeed(Seed seed) {
		if (seed != Seed.EMPTY) {
			mySeed = seed;
			oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
		} else {
			mySeed = Seed.NOUGHT;
			oppSeed = Seed.CROSS;
		}
	}

	// Return int[2] of {row, col}
	abstract int[] move(int depth);
}
