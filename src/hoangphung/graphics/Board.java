package hoangphung.graphics;

import java.awt.Color;
import java.awt.Graphics;

public class Board {

	public Cell[][] cells;

	public Board() {
		cells = new Cell[GameMain.ROWS][GameMain.COLS];
		for (int row = 0; row < GameMain.ROWS; row++) {
			for (int col = 0; col < GameMain.COLS; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}
	}

	public void init() {
		for (int row = 0; row < GameMain.ROWS; row++) {
			for (int col = 0; col < GameMain.COLS; col++) {
				cells[row][col].clear();
			}
		}
	}

	public boolean isDraw() {
		for (int row = 0; row < GameMain.ROWS; row++) {
			for (int col = 0; col < GameMain.COLS; col++) {
				if (cells[row][col].content == Seed.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean hasWon(Seed seed, int seedRow, int seedCol) {
		int count = 0;
		// horizontal
		for (int i = -4; i <= 4; i++) {
			if ((seedCol + i >= 0) && (seedCol + i < GameMain.COLS)) {
				if (cells[seedRow][seedCol + i].content == seed) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		count = 0;
		// vertical
		for (int i = -4; i <= 4; i++) {
			if ((seedRow + i >= 0) && (seedRow + i < GameMain.ROWS)) {
				if (cells[seedRow + i][seedCol].content == seed) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		count = 0;
		// diagonal
		for (int i = -4; i <= 4; i++) {
			if ((seedCol + i >= 0) && (seedCol + i < GameMain.COLS)
					&& (seedRow + i >= 0) && (seedRow + i < GameMain.ROWS)) {
				if (cells[seedRow + i][seedCol + i].content == seed) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		count = 0;
		// opposite-diagonal
		for (int i = -4, j = 4; i <= 4 && j >= -4; i++, j--) {
			if ((seedCol + j < GameMain.COLS) && (seedRow + i >= 0)
					&& (seedCol + j >= 0) && (seedRow + i < GameMain.ROWS)) {
				if (cells[seedRow + i][seedCol + j].content == seed) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		return false;
	}

	public void paint(Graphics g) {
		g.setColor(new Color(198, 212, 239));
		for (int row = 1; row < GameMain.ROWS; row++) {
			g.drawLine(0, row * GameMain.CELL_SIZE - 1, GameMain.CANVAS_WIDTH,
					row * GameMain.CELL_SIZE - 1);
		}
		for (int col = 1; col < GameMain.COLS; col++) {
			g.drawLine(col * GameMain.CELL_SIZE - 1, 0, col
					* GameMain.CELL_SIZE - 1, GameMain.CANVAS_HEIGHT);
		}
		g.setColor(Color.WHITE);
		for (int row = 1; row < GameMain.ROWS; row++) {
			g.drawLine(0, row * GameMain.CELL_SIZE + 1, GameMain.CANVAS_WIDTH,
					row * GameMain.CELL_SIZE + 1);
		}
		for (int col = 1; col < GameMain.COLS; col++) {
			g.drawLine(col * GameMain.CELL_SIZE + 1, 0, col
					* GameMain.CELL_SIZE + 1, GameMain.CANVAS_HEIGHT);
		}
		for (int row = 0; row < GameMain.ROWS; ++row) {
			for (int col = 0; col < GameMain.COLS; ++col) {
				cells[row][col].paint(g);
			}
		}
	}
}
