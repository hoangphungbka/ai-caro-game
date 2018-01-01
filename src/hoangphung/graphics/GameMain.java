package hoangphung.graphics;

import hoangphung.aiplayers.AIPlayerPruning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GameMain extends JPanel {

	public static final int ROWS = 15;
	public static final int COLS = 15;
	public static final String TITLE = "Caro Game";

	public static int CELL_SIZE = 30;
	public static int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static int CANVAS_HEIGHT = CELL_SIZE * ROWS;

	public static int CELL_PADDING = CELL_SIZE / 4;
	public static int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	public static int SYMBOL_STROKE_WIDTH = 3;

	private Board board;
	private AIPlayerPruning computer;
	private GameState currentState;
	private Seed currentPlayer;
	private JLabel statusBar;

	public GameMain() {
		board = new Board();
		initGame();
		computer = new AIPlayerPruning(board);
		computer.setSeed(Seed.NOUGHT);
		this.addMouseListener(new Listener());
		statusBar = new JLabel("         ", JLabel.CENTER);
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
		statusBar.setOpaque(true);
		statusBar.setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		add(statusBar, BorderLayout.PAGE_END);
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 27));
	}

	public void initGame() {
		board.init();
		currentState = GameState.PLAYING;
		currentPlayer = Seed.CROSS;
	}

	public void updateGame(Seed theSeed, int row, int col) {
		if (board.hasWon(theSeed, row, col)) {
			currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON
					: GameState.NOUGHT_WON;
		} else if (board.isDraw()) {
			currentState = GameState.DRAW;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		setBackground(new Color(239, 240, 255));
		board.paint(g);
		if (currentState == GameState.PLAYING) {
			statusBar.setForeground(Color.BLACK);
			if (currentPlayer == Seed.CROSS) {
				statusBar.setText("X's Turn");
			} else {
				statusBar.setText("O's Turn");
			}
		} else if (currentState == GameState.DRAW) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("It's a Draw! Click to play again.");
		} else if (currentState == GameState.CROSS_WON) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'X' Won! Click to play again.");
		} else if (currentState == GameState.NOUGHT_WON) {
			statusBar.setForeground(Color.RED);
			statusBar.setText("'O' Won! Click to play again.");
		}
	}

	private class Listener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int mouseX = e.getX();
			int mouseY = e.getY();
			int rowSelected = mouseY / CELL_SIZE;
			int colSelected = mouseX / CELL_SIZE;
			if (currentState == GameState.PLAYING) {
				if (currentPlayer == Seed.CROSS) {
					if (rowSelected >= 0 && rowSelected < ROWS
							&& colSelected >= 0 && colSelected < COLS
							&& board.cells[rowSelected][colSelected].content == Seed.EMPTY) {
						board.cells[rowSelected][colSelected].content = currentPlayer;
						updateGame(currentPlayer, rowSelected, colSelected);
						currentPlayer = Seed.NOUGHT;
						repaint();
					}
					if (currentState == GameState.PLAYING) {
						int[] go = computer.move(2);
						board.cells[go[0]][go[1]].content = currentPlayer;
						updateGame(currentPlayer, go[0], go[1]);
						currentPlayer = Seed.CROSS;
					}
				}
			} else {
				initGame();
			}
			repaint();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame(TITLE);
				frame.setContentPane(new GameMain());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
