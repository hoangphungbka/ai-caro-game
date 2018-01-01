package hoangphung.aiplayers;

import java.util.ArrayList;
import java.util.List;

import hoangphung.graphics.Board;
import hoangphung.graphics.Seed;

public class AIPlayerPruning extends AIPlayers {

	public AIPlayerPruning(Board board) {
		super(board);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] move(int depth) {
		// TODO Auto-generated method stub
		int[] result = minimax(depth, mySeed, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
		return new int[] { result[1], result[2] };
	}

	// Return int[3] of {score, row, col}
	private int[] minimax(int depth, Seed player, int alpha, int beta) {
		List<int[]> nextMoves = generateMoves();
		// mySeed is maximizing while oppSeed is minimizing
		int bestRow = -1, bestCol = -1, score;
		if (nextMoves.isEmpty() || depth == 0) {
			score = evaluate();
			return new int[] { score, bestRow, bestCol };
		} else {
			for (int[] move : nextMoves) {
				cells[move[0]][move[1]].content = player;
				if (player == mySeed) {
					score = minimax(depth - 1, oppSeed, alpha, beta)[0];
					if (score > alpha) {
						alpha = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				} else {
					score = minimax(depth - 1, mySeed, alpha, beta)[0];
					if (score < beta) {
						beta = score;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				cells[move[0]][move[1]].content = Seed.EMPTY;
				if (alpha >= beta) {
					break;
				}
			}
			return new int[] { (player == mySeed) ? alpha : beta, bestRow,
					bestCol };
		}
	}

	// Return List of moves or empty list if gameover
	private List<int[]> generateMoves() {
		List<int[]> nextMoves = new ArrayList<>();
		if (hasWon(mySeed) || hasWon(oppSeed)) {
			return nextMoves;
		}
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (cells[row][col].content == Seed.EMPTY) {
					nextMoves.add(new int[] { row, col });
				}
			}
		}
		return nextMoves;
	}

	// Return true if thePlayer wins
	private boolean hasWon(Seed thePlayer) {
		for (int row = 0; row < ROWS; row++) {
			int count = 0;
			for (int col = 0; col < COLS; col++) {
				if (cells[row][col].content == thePlayer) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		for (int col = 0; col < COLS; col++) {
			int count = 0;
			for (int row = 0; row < ROWS; row++) {
				if (cells[row][col].content == thePlayer) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		for (int col = 4; col < COLS; col++) {
			int count = 0;
			for (int irow = 0, jcol = col; irow < ROWS && jcol >= 0; irow++, jcol--) {
				if (cells[irow][jcol].content == thePlayer) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		for (int row = 1; row <= 10; row++) {
			int count = 0;
			for (int irow = row, jcol = 14; irow < ROWS && jcol >= 0; irow++, jcol--) {
				if (cells[irow][jcol].content == thePlayer) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		for (int row = 10; row >= 0; row--) {
			int count = 0;
			for (int irow = row, jcol = 0; irow < ROWS && jcol < COLS; irow++, jcol++) {
				if (cells[irow][jcol].content == thePlayer) {
					if (++count == 5) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}
		for (int col = 1; col <= 10; col++) {
			int count = 0;
			for (int irow = 0, jcol = col; irow < ROWS && jcol < COLS; irow++, jcol++) {
				if (cells[irow][jcol].content == thePlayer) {
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

	private int evaluate() {
		int score = 0;
		for (int row = 0; row < ROWS; row++) {
			score += evaluateRow(row);
		}
		for (int col = 0; col < COLS; col++) {
			score += evaluateCol(col);
		}
		for (int row = 4; row < ROWS; row++) {
			score += evaluateDiagonal(row, 0);
		}
		for (int col = 1; col < 11; col++) {
			score += evaluateDiagonal(14, col);
		}
		for (int row = 10; row >= 0; row--) {
			score += evaluateOppositeDiagonal(row, 0);
		}
		for (int col = 1; col < 11; col++) {
			score += evaluateOppositeDiagonal(0, col);
		}
		return score;
	}

	private int evaluateRow(int row) {
		int score = 0;
		for (int col = 4; col < COLS; col++) {
			int countMySeed = 0, countOppSeed = 0, scoretemp = 0;
			if (cells[row][col].content == mySeed) {
				for (int i = col - 1; i >= col - 4; i--) {
					if (cells[row][i].content == oppSeed) {
						countOppSeed++;
					} else if (cells[row][i].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed);
				}
			} else if (cells[row][col].content == oppSeed) {
				for (int i = col - 1; i >= col - 4; i--) {
					if (cells[row][i].content == mySeed) {
						countMySeed++;
					} else if (cells[row][i].content == oppSeed) {
						countOppSeed++;
					}
				}
				if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed));
				}
			} else {
				for (int i = col - 1; i >= col - 4; i--) {
					if (cells[row][i].content == mySeed) {
						countMySeed++;
					} else if (cells[row][i].content == oppSeed) {
						countOppSeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed - 1);
				} else if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed - 1));
				}
			}
			score += scoretemp;
		}
		return score;
	}

	private int evaluateCol(int col) {
		int score = 0;
		for (int row = 4; row < COLS; row++) {
			int countMySeed = 0, countOppSeed = 0, scoretemp = 0;
			if (cells[row][col].content == mySeed) {
				for (int i = row - 1; i >= row - 4; i--) {
					if (cells[i][col].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][col].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed);
				}
			} else if (cells[row][col].content == oppSeed) {
				for (int i = row - 1; i >= row - 4; i--) {
					if (cells[i][col].content == mySeed) {
						countMySeed++;
					} else if (cells[i][col].content == oppSeed) {
						countOppSeed++;
					}
				}
				if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed));
				}
			} else {
				for (int i = row - 1; i >= row - 4; i--) {
					if (cells[i][col].content == mySeed) {
						countMySeed++;
					} else if (cells[i][col].content == oppSeed) {
						countOppSeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed - 1);
				} else if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed - 1));
				}
			}
			score += scoretemp;
		}
		return score;
	}

	private int evaluateDiagonal(int row, int col) {
		int score = 0;
		for (int irow = row - 4, jcol = col + 4; irow >= 0 && jcol < COLS; irow--, jcol++) {
			int countMySeed = 0, countOppSeed = 0, scoretemp = 0;
			if (cells[irow][jcol].content == mySeed) {
				for (int i = irow + 1, j = jcol - 1; i <= irow + 4
						&& j >= jcol - 4; i++, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed);
				}
			} else if (cells[irow][jcol].content == oppSeed) {
				for (int i = irow + 1, j = jcol - 1; i <= irow + 4
						&& j >= jcol - 4; i++, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed));
				}
			} else {
				for (int i = irow + 1, j = jcol - 1; i <= irow + 4
						&& j >= jcol - 4; i++, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed - 1);
				} else if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed - 1));
				}
			}
			score += scoretemp;
		}
		return score;
	}

	private int evaluateOppositeDiagonal(int row, int col) {
		int score = 0;
		for (int irow = row + 4, jcol = col + 4; irow < ROWS && jcol < COLS; irow++, jcol++) {
			int countMySeed = 0, countOppSeed = 0, scoretemp = 0;
			if (cells[irow][jcol].content == mySeed) {
				for (int i = irow - 1, j = jcol - 1; i >= irow - 4
						&& j >= jcol - 4; i--, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed);
				}
			} else if (cells[irow][jcol].content == oppSeed) {
				for (int i = irow - 1, j = jcol - 1; i >= irow - 4
						&& j >= jcol - 4; i--, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed));
				}
			} else {
				for (int i = irow - 1, j = jcol - 1; i >= irow - 4
						&& j >= jcol - 4; i--, j--) {
					if (cells[i][j].content == oppSeed) {
						countOppSeed++;
					} else if (cells[i][j].content == mySeed) {
						countMySeed++;
					}
				}
				if (countOppSeed == 0) {
					scoretemp = (int) Math.pow(10, countMySeed - 1);
				} else if (countMySeed == 0) {
					scoretemp = -((int) Math.pow(10, countOppSeed - 1));
				}
			}
			score += scoretemp;
		}
		return score;
	}
}
