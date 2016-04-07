import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Handles all of the game logic. Is responsible for holding the gameboard, 
 * altering the board state, swapping turn order, and checking for winning
 * conditions
 * 
 * @author Kyle
 *
 */
public class Game extends JComponent {
	private static final long serialVersionUID = 1L;
	public static ArrayList<ArrayList<JButton>> gameboard;
	public static boolean isPlayersTurn;
	MiniMax minimax;
	
	public static int player; // turn order. first player = 0, second = 1
	public static int ai; // same as player, but for the computer
	public static final int SIZE = 6; // 6x6 board
	
	// created color for background of the gameboard
	public static Color forestGreen; 
	
	// list of cardinal directions in which board positions are altered after 
	// making a move
	public static ArrayList<Cardinals> validDirections; 
	
	// directions relative to a position on the board
	public enum Cardinals {
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
	}
	
	/**
	 * initializes variables
	 */
	public Game() {
		gameboard = new ArrayList<ArrayList<JButton>>();
		validDirections = new ArrayList<>();
		minimax = new MiniMax();
		
		forestGreen = new Color(34, 139, 34);	
	}
	
	/**
	 * main game loop
	 */
	public void run() {
		// intro message
		System.out.println("OTHELLO");
		System.out.println("On your turn, make a move by clicking on one of "
				+ "the squares");

		// determine play order
		if (getPlayOrder() == 0) {
			player = 0;
			ai = 1;
			Game.isPlayersTurn = true;
		} else {
			player = 1;
			ai = 0;
			isPlayersTurn = false;
		}

		// adds an action listener to each button corresponding to the player
		// making a move, depending on whether or not it is a valid move and
		// it is their turn
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int y = i;
				int x = j;
				gameboard.get(i).get(j).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (isValidMove(gameboard, x, y, player) && isPlayerTurn(gameboard)) {
							flipPieces(gameboard, x, y, player);
							isPlayersTurn = false;
						}
					}
				});
			}
		}
		
		// Alternate between the computer taking a turn and the player
		while (!isGameWon(gameboard) && (hasMovesAvailable(gameboard, 0) || hasMovesAvailable(gameboard, 1))) {
			while (!isPlayersTurn && hasMovesAvailable(gameboard, ai)) {
				getCurrentScore();
				aiTurn();
				getCurrentScore();
			}	
		}
	}

	/**
	 * Process what happens during the computer's turn. calls the minimax
	 * algorithm and prints the location of its move
	 */
	public void aiTurn() {
		System.out.println("AI's Turn"); 
		Point move = minimax.iterativeDeepeningMiniMax(ai);
		flipPieces(Game.gameboard, move.x, move.y, ai);
		System.out.println("AI made a move at (" + move.x + ", " + move.y + ")");
		System.out.println("Player's Turn");
		isPlayersTurn = true;
	}
	
	/**
	 * Determines if it is the players turn
	 * 
	 * @param gameboard the board state that is being searched
	 * @return true if it is the player's turn. False otherwise
	 */
	public boolean isPlayerTurn(ArrayList<ArrayList<JButton>> gameboard) {
		if (isPlayersTurn && hasMovesAvailable(gameboard, player)) return true;
		else if (!isPlayersTurn && hasMovesAvailable(gameboard, player) && !hasMovesAvailable(gameboard, ai)) return true;
		else return false;
	}
	
	/**
	 * Initialize the gameboard
	 * 
	 * @param panel			the window on which to create the buttons
	 * @param gameboard		the board to create
	 */
	public void initBoard(JPanel panel, ArrayList<ArrayList<JButton>> gameboard) {
		GridBagConstraints gbc; // to handle window formatting
		
		// lines between the board positions
		Border border = new LineBorder(Color.RED, 1); 
		
		// create the gameboard
		for (int i = 0; i < SIZE; i++) {
			ArrayList<JButton> temp = new ArrayList<>();
			for (int j = 0; j < SIZE; j++) {
				temp.add(new JButton());
			}
			gameboard.add(new ArrayList<JButton>(temp));
			temp.clear();
		}
		
		// set the properties of each button and orient them correctly
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				gameboard.get(y).get(x).setOpaque(true);
				gameboard.get(y).get(x).setBackground(forestGreen);
				gameboard.get(y).get(x).setBorder(border);
				gameboard.get(y).get(x).setPreferredSize(new Dimension(100, 100));
				gbc = new GridBagConstraints(); 
				gbc.fill = GridBagConstraints.BOTH;
				gbc.gridx = x;
				gbc.gridy = y + 1;
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
				
				panel.add(gameboard.get(y).get(x), gbc);
			}
		}
		
		// set background color for beginning pieces
		gameboard.get(2).get(2).setBackground(Color.WHITE);
		gameboard.get(2).get(3).setBackground(Color.BLACK);
		gameboard.get(3).get(2).setBackground(Color.BLACK);
		gameboard.get(3).get(3).setBackground(Color.WHITE);
	}

	/**
	 * Opens up a dialog box letting the user choose to either play first or 
	 * second
	 * 
	 * @return an integer, 0 corresponding to first and 1 to second
	 */
	public int getPlayOrder() {
		String[] options = new String[] { "First", "Second" };
		int response = JOptionPane.showOptionDialog(null,	
				"Play first or second?", "Othello",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		
		return response;
	}
	
	/**
	 * Determine if a proposed move is valid or not.
	 * 
	 * @param gameboard		gameboard in which to check
	 * @param posX			x-coordinate of the proposed move
	 * @param posY			y-coordinate of the proposed move
	 * @param player		the player to check for
	 * @return
	 */
	public static boolean isValidMove(ArrayList<ArrayList<JButton>> gameboard, int posX, int posY, int player) {
		Color playerColor;
		Color opposingColor;
		boolean validMove = false;
		
		// set color
		if (player == 0) { // first player
			playerColor = Color.BLACK;
			opposingColor = Color.WHITE;
		} else {
			playerColor = Color.WHITE;
			opposingColor = Color.BLACK;
		}
		
		
		// if there isn't already a piece on the position
		if (gameboard.get(posY).get(posX).getBackground() == forestGreen) {	
			// north
			if (posY > 0 && 
					gameboard.get(posY-1).get(posX).getBackground() == opposingColor) {
				for (int i = posY-2; i >= 0; i--) {
					if (gameboard.get(i).get(posX).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(posX).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// northwest
			if (posY > 0 && posX > 0 &&
					gameboard.get(posY-1).get(posX-1).getBackground() == opposingColor) {
				for (int i = posY-2, j = posX-2; i >= 0 && j >= 0; i--, j--) {
					
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// northeast
			if (posY > 0 && posX < SIZE - 1 &&
					gameboard.get(posY-1).get(posX+1).getBackground() == opposingColor) {
				for (int i = posY-2, j = posX+2; i >= 0 && j < SIZE; i--, j++) {
					
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// south
			if (posY < SIZE - 1 &&
					gameboard.get(posY+1).get(posX).getBackground() == opposingColor) {
				for (int i = posY+2; i < SIZE; i++) {
					if (gameboard.get(i).get(posX).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(posX).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// southwest
			if (posY < SIZE - 1 && posX > 0 &&
					gameboard.get(posY+1).get(posX-1).getBackground() == opposingColor) {
				for (int i = posY+2, j = posX-2; i < SIZE && j >= 0; i++, j--) {
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// southeast
			if (posY < SIZE - 1 && posX < SIZE - 1 &&
					gameboard.get(posY+1).get(posX+1).getBackground() == opposingColor) {
				for (int i = posY+2, j = posX+2; i < SIZE && j < SIZE - 1; i++, j++) {
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// west
			if (posX > 0 &&
					gameboard.get(posY).get(posX-1).getBackground() == opposingColor) {
				for (int i = posX-2; i >= 0; i--) {
					
					if (gameboard.get(posY).get(i).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(posY).get(i).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
			// east
			if (posX < SIZE - 1 &&
					gameboard.get(posY).get(posX+1).getBackground() == opposingColor) {
				for (int i = posX+2; i < SIZE; i++) {
					if (gameboard.get(posY).get(i).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(posY).get(i).getBackground() == playerColor) {
						validMove = true;
						break;
					}
				}
			}
		}
		return validMove;
	}
	
	/**
	 * Generates a list of all valid directions in which positions will be 
	 * changed after making a move.
	 * 
	 * The same as isValidMove(...), except that it returns what the actual 
	 * moves are.
	 * 
	 * @param gameboard		gameboard in which to check
	 * @param posX			x-coordinate of the proposed move
	 * @param posY			y-coordinate of the proposed move
	 * @param player		the player to check for
	 * @return				List of directions in which positions will change
	 */
	public static ArrayList<Cardinals> getValidMoves(ArrayList<ArrayList<JButton>> gameboard, int posX, int posY, int player) {
		ArrayList<Cardinals> directions = new ArrayList<>();
		
		Color playerColor;
		Color opposingColor;
		
		// set color
		if (player == 0) { // first player
			playerColor = Color.BLACK;
			opposingColor = Color.WHITE;
		} else {
			playerColor = Color.WHITE;
			opposingColor = Color.BLACK;
		}
		
		
		if (gameboard.get(posY).get(posX).getBackground() == forestGreen) {	
			// north
			if (posY > 0 && 
					gameboard.get(posY-1).get(posX).getBackground() == opposingColor) {
				for (int i = posY-2; i >= 0; i--) {
					if (gameboard.get(i).get(posX).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(posX).getBackground() == playerColor) {
						directions.add(Cardinals.NORTH);
						break;
					}
				}
			}
			// northwest
			if (posY > 0 && posX > 0 &&
					gameboard.get(posY-1).get(posX-1).getBackground() == opposingColor) {
				for (int i = posY-2, j = posX-2; i >= 0 && j >= 0; i--, j--) {
					
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						directions.add(Cardinals.NORTHWEST);
						break;
					}
				}
			}
			// northeast
			if (posY > 0 && posX < SIZE - 1 &&
					gameboard.get(posY-1).get(posX+1).getBackground() == opposingColor) {
				for (int i = posY-2, j = posX+2; i >= 0 && j < SIZE; i--, j++) {
					
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						directions.add(Cardinals.NORTHEAST);
						break;
					}
				}
			}
			// south
			if (posY < SIZE - 1 &&
					gameboard.get(posY+1).get(posX).getBackground() == opposingColor) {
				for (int i = posY+2; i < SIZE; i++) {
					if (gameboard.get(i).get(posX).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(posX).getBackground() == playerColor) {
						directions.add(Cardinals.SOUTH);
						break;
					}
				}
			}
			// southwest
			if (posY < SIZE - 1 && posX > 0 &&
					gameboard.get(posY+1).get(posX-1).getBackground() == opposingColor) {
				for (int i = posY+2, j = posX-2; i < SIZE && j >= 0; i++, j--) {
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						directions.add(Cardinals.SOUTHWEST);
						break;
					}
				}
			}
			// southeast
			if (posY < SIZE - 1 && posX < SIZE - 1 &&
					gameboard.get(posY+1).get(posX+1).getBackground() == opposingColor) {
				for (int i = posY+2, j = posX+2; i < SIZE && j < SIZE - 1; i++, j++) {
					if (gameboard.get(i).get(j).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(i).get(j).getBackground() == playerColor) {
						directions.add(Cardinals.SOUTHEAST);
						break;
					}
				}
			}
			// west
			if (posX > 0 &&
					gameboard.get(posY).get(posX-1).getBackground() == opposingColor) {
				for (int i = posX-2; i >= 0; i--) {
					
					if (gameboard.get(posY).get(i).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(posY).get(i).getBackground() == playerColor) {
						directions.add(Cardinals.WEST);
						break;
					}
				}
			}
			// east
			if (posX < SIZE - 1 &&
					gameboard.get(posY).get(posX+1).getBackground() == opposingColor) {
				for (int i = posX+2; i < SIZE; i++) {
					if (gameboard.get(posY).get(i).getBackground() == forestGreen) {
						break;
					}
					else if (gameboard.get(posY).get(i).getBackground() == playerColor) {
						directions.add(Cardinals.EAST);
						break;
					}
				}
			}
		}
		
		return directions;
	}
	
	/**
	 * Loop through each of the valid directions and change the colors 
	 * accordingly
	 * 
	 * @param gameboard		gameboard in which to check
	 * @param x				x-coordinate of the move
	 * @param y				y-coordinate of the move
	 * @param player		the player in which to turn the pieces into
	 */
	public static void flipPieces(ArrayList<ArrayList<JButton>> gameboard, int x, int y, int player) {
		Color playerColor;
		Color opposingColor;
		
		if (player == 0) {
			playerColor = Color.BLACK; 
			opposingColor = Color.WHITE;
		}
		else {
			playerColor = Color.WHITE; 
			opposingColor = Color.BLACK;
		}
		
		JButton currPiece = new JButton();
		
		for (Cardinals dir : getValidMoves(gameboard, x, y, player)) {
			currPiece = gameboard.get(y).get(x);
			currPiece.setBackground(playerColor);
			int i = 0;
			switch (dir) {
			case NORTH:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y - i > 0) currPiece = gameboard.get(y - i).get(x);
					
				} while (currPiece.getBackground() == opposingColor);
				break;
			case NORTHEAST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y - i > 0 && x+i < SIZE) currPiece = gameboard.get(y - i).get(x+i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case EAST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (x+i < SIZE) currPiece = gameboard.get(y).get(x+i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTHEAST:
				i = 0;
				 do {
					 i++;
					 currPiece.setBackground(playerColor);
					 if ((y+i) < SIZE && (x+i) < SIZE) currPiece = gameboard.get(y+i).get(x+i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTH:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y+i < SIZE) currPiece = gameboard.get(y+i).get(x);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTHWEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y+i < SIZE && x-i > 0) currPiece = gameboard.get(y+i).get(x-i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case WEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (x-i > 0) currPiece = gameboard.get(y).get(x-i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			case NORTHWEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y-i > 0 && x-i > 0) currPiece = gameboard.get(y-i).get(x-i);
				} while (currPiece.getBackground() == opposingColor);
				break;
			default:
				break;
			}
		}
		
		validDirections.clear();
	}

	/**
	 * Checks the board to determine if there is a winner
	 * 
	 * @param 	gameboard board in which to check
	 * @return	true if the game is won; false otherwise
	 */
	public static boolean isGameWon(ArrayList<ArrayList<JButton>> gameboard) {
		boolean isWon = true;
		
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (isValidMove(gameboard, x, y, 0) || isValidMove(gameboard, x, y, 1)) {
					isWon = false;
					break;
				}
			}
		}
		
		return isWon;
	}
	
	/**
	 * loops through every possible board position and determines if the given
	 * player has a valid move
	 * 
	 * @param gameboard		board state in which to check
	 * @param player		player whom is being evaluated
	 * @return				true if player has a move; false otherwise
	 */
	public static boolean hasMovesAvailable(ArrayList<ArrayList<JButton>> gameboard, int player) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (isValidMove(gameboard, x, y, player)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * After the game is over, it will evaluate the scores of each player and 
	 * will determine which player won.
	 * 
	 * @return 	String containing the winning player and each player's 
	 * 			respective scores
	 */
	public String getWinningScore() {
		if (heuristic(gameboard, 0) > heuristic(gameboard, 1)) 
			return "Player 1 wins, " + heuristic(gameboard, 0) + " to " + heuristic(gameboard, 1);
		else
			return "Player 2 wins, " + heuristic(gameboard, 1) + " to " + heuristic(gameboard, 0);
	}
	
	/**
	 * 	Display the score of each player
	 */
	public void getCurrentScore() {
		System.out.println("Player 1: " + heuristic(gameboard, 0));
		System.out.println("Player 2: " + heuristic(gameboard, 1)); 
	}
	
	/**
	 * Evaluates the scores of the given player. The score is represented as 
	 * the number of positions on the board corresponding to that player's 
	 * color
	 * 
	 * @param gameboard		board in which to evaluate
	 * @param player		player in which to evaluate
	 * @return				score of the player
	 */
	public static int heuristic(ArrayList<ArrayList<JButton>> gameboard, int player) {
		Color c = forestGreen;
		int score = 0;
		
		if (player == 0) c = Color.BLACK;
		else if (player == 1) c = Color.WHITE;
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (gameboard.get(i).get(j).getBackground() == c) score++;
			}
		}
		
		return score;
	}
	
	/**
	 * Generates a list of all possible moves available to the player
	 * 
	 * @param player		player to generate moves for
	 * @param gameboard		board in which moves will be taken
	 * @return				list of all available moves
	 */
	public static ArrayList<Point> getPossibleMoves(int player, ArrayList<ArrayList<JButton>> gameboard) {
		ArrayList<Point> moves = new ArrayList<>();
		
		if (hasMovesAvailable(gameboard, player)) {
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					if (isValidMove(gameboard, x, y, player)) {
						moves.add(new Point(x, y));
					}
				}
			}
		}
		
		return moves;
		
	}
	
	/**
	 * Determine if player one has won
	 * 
	 * @param gameboard		gameboard to check for win
	 * @return				true if player one has won; false otherwise
	 */
	public static boolean hasBlackWon(ArrayList<ArrayList<JButton>> gameboard) {
		if (isGameWon(gameboard) && heuristic(gameboard, 0) > heuristic(gameboard, 1)) return true;
		else return false;
	}
	
	/**
	 * Determine if player two has won
	 * 
	 * @param gameboard		gameboard to check for win
	 * @return				true if player two has won; false otherwise
	 */
	public static boolean hasWhiteWon(ArrayList<ArrayList<JButton>> gameboard) {
		if (isGameWon(gameboard) && heuristic(gameboard, 1) > heuristic(gameboard, 0)) return true;
		else return false;
	}
	
}
