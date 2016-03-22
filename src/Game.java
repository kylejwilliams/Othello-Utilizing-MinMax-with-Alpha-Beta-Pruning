import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Game {
	public JButton[][] gameboard;
	public JTextArea gameOutput;
	public JTextField gameInput;
	public JScrollPane console;
	public Color forestGreen;
	public boolean isFirstPlayersTurn;
	private final int SIZE = 6; // 6x6 board
	public ArrayList<Cardinals> validDirections;
	public int playerOne;
	public int playerTwo;
	
	public enum Cardinals {
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
	}
	
	public Game() {
		gameboard = new JButton[SIZE][SIZE];
		gameOutput = new JTextArea(2, 50);
		gameOutput.setEditable(false);
		validDirections = new ArrayList<>();
		
		isFirstPlayersTurn = true;
		forestGreen = new Color(34, 139, 34);
		
		PrintStream printStream = new PrintStream(
				new CustomOutputStream(gameOutput));
		
		System.setOut(printStream);
		System.setErr(printStream);
		
		console = new JScrollPane(gameOutput);
	}

	public void run() {
		
		System.out.println("OTHELLO");
		System.out.println("On your turn, make a move by clicking on one of "
				+ "the squares");

		if (getPlayOrder() == 0) {
			playerOne = 0;
			playerTwo = 1;
		}
		else {
			playerOne = 1;
			playerTwo = 0;
		}
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int y = i;
				int x = j;
				gameboard[i][j].addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (isValidMove(x, y, playerOne) && isFirstPlayersTurn) {
								flipPieces(gameboard, x, y, playerOne);
								isFirstPlayersTurn = false;
								if (!hasMovesAvailable(playerOne) && 
										!hasMovesAvailable(playerTwo))
									playAgain();
							}
							else if (isValidMove(x, y, playerTwo) && !isFirstPlayersTurn) {
								flipPieces(gameboard, x, y, playerTwo);
								isFirstPlayersTurn = true;
								if (!hasMovesAvailable(playerOne) && 
										!hasMovesAvailable(playerTwo))
									playAgain();
							}
							else if (!hasMovesAvailable(playerOne) && 
									isFirstPlayersTurn &&
									isValidMove(x, y, playerTwo)) {
								flipPieces(gameboard, x, y, playerTwo);
								isFirstPlayersTurn = true;
								if (!hasMovesAvailable(playerOne) && 
										!hasMovesAvailable(playerTwo))
									playAgain();
							}
							else if (!hasMovesAvailable(playerTwo) &&
									!isFirstPlayersTurn &&
									isValidMove(x, y, playerOne)) {
								flipPieces(gameboard, x, y, playerOne);
								isFirstPlayersTurn = false;
								if (!hasMovesAvailable(playerOne) && 
										!hasMovesAvailable(playerTwo))
									playAgain();
							}
						}
				});
			}
		}
	}
		
	public void initBoard(JPanel panel) {
		GridBagConstraints gbc;
		Border border = new LineBorder(Color.RED, 1);
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = j;
				int y = i;
				
				gameboard[y][x] = new JButton();
				gameboard[y][x].setBackground(forestGreen);
				gameboard[y][x].setBorder(border);
				gameboard[y][x].setPreferredSize(new Dimension(100, 100));
				gbc = new GridBagConstraints(); 
				gbc.fill = GridBagConstraints.BOTH;
				//gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridx = j;
				gbc.gridy = i + 1;
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
				
				panel.add(gameboard[y][x], gbc);
			}
		}
		
		// set background color for beginning pieces
		gameboard[2][2].setBackground(Color.WHITE);
		gameboard[2][3].setBackground(Color.BLACK);
		gameboard[3][2].setBackground(Color.BLACK);
		gameboard[3][3].setBackground(Color.WHITE);
	}
	
	public void initConsole(JPanel panel) {
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 6;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		panel.add(console, gbc);
	}
	
	/**
	 * @return 0 if playing first, 1 if playing second
	 */
	public int getPlayOrder() {
		String[] options = new String[] { "First", "Second" };
		int response = JOptionPane.showOptionDialog(null,	
				"Play first or second?", "Othello",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		
		return response;
	}

	public void playAgain() {
		String[] options = new String[] { "Play Again", "Exit" };
		int response = JOptionPane.showOptionDialog(null,	
				getScore(), "Othello",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		if (response == 0) {
			Application.startGame();
		}
		else System.exit(0);
	}
	
	public boolean isValidMove(int posX, int posY, int player) {
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
		
		
		if (gameboard[posY][posX].getBackground() == forestGreen) {	
			// north
			if (posY > 0 && 
					gameboard[posY-1][posX].getBackground() == opposingColor) {
				for (int i = posY-2; i >= 0; i--) {
					if (gameboard[i][posX].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][posX].getBackground() == playerColor) {
						validDirections.add(Cardinals.NORTH);
						validMove = true;
						break;
					}
				}
			}
			// northwest
			if (posY > 0 && posX > 0 &&
					gameboard[posY-1][posX-1].getBackground() == opposingColor) {
				for (int i = posY-2, j = posX-2; i >= 0 && j >= 0; i--, j--) {
					
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						validDirections.add(Cardinals.NORTHWEST);
						validMove = true;
						break;
					}
				}
			}
			// northeast
			if (posY > 0 && posX < SIZE - 1 &&
					gameboard[posY-1][posX+1].getBackground() == opposingColor) {
				for (int i = posY-2, j = posX+2; i >= 0 && j < SIZE; i--, j++) {
					
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						validDirections.add(Cardinals.NORTHEAST);
						validMove = true;
						break;
					}
				}
			}
			// south
			if (posY < SIZE - 1 &&
					gameboard[posY+1][posX].getBackground() == opposingColor) {
				for (int i = posY+2; i < SIZE; i++) {
					if (gameboard[i][posX].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][posX].getBackground() == playerColor) {
						validDirections.add(Cardinals.SOUTH);
						validMove = true;
						break;
					}
				}
			}
			// southwest
			if (posY < SIZE - 1 && posX > 0 &&
					gameboard[posY+1][posX-1].getBackground() == opposingColor) {
				for (int i = posY+2, j = posX-2; i < SIZE && j >= 0; i++, j--) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						validDirections.add(Cardinals.SOUTHWEST);
						validMove = true;
						break;
					}
				}
			}
			// southeast
			if (posY < SIZE - 1 && posX < SIZE - 1 &&
					gameboard[posY+1][posX+1].getBackground() == opposingColor) {
				for (int i = posY+2, j = posX+2; i < SIZE && j < SIZE - 1; i++, j++) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						validDirections.add(Cardinals.SOUTHEAST);
						validMove = true;
						break;
					}
				}
			}
			// west
			if (posX > 0 &&
					gameboard[posY][posX-1].getBackground() == opposingColor) {
				for (int i = posX-2; i >= 0; i--) {
					
					if (gameboard[posY][i].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[posY][i].getBackground() == playerColor) {
						validDirections.add(Cardinals.WEST);
						validMove = true;
						break;
					}
				}
			}
			// east
			if (posX < SIZE - 1 &&
					gameboard[posY][posX+1].getBackground() == opposingColor) {
				for (int i = posX+2; i < SIZE; i++) {
					if (gameboard[posY][i].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[posY][i].getBackground() == playerColor) {
						validDirections.add(Cardinals.EAST);
						validMove = true;
						break;
					}
				}
			}
		}
		
		return validMove;
	}
	
	public void flipPieces(JButton[][] gameboard, int x, int y, int player) {
		Color playerColor = null;
		Color opposingColor = null;
		
		if (player == 0) {
			playerColor = Color.BLACK; 
			opposingColor = Color.WHITE;
		}
		if (player == 1) {
			playerColor = Color.WHITE; 
			opposingColor = Color.BLACK;
		}
		
		JButton currPiece;
		
		for (Cardinals dir : validDirections) {
			currPiece = gameboard[y][x];
			currPiece.setBackground(playerColor);
			int i = 0;
			switch (dir) {
			case NORTH:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y - i > 0) currPiece = gameboard[y - i][x];
					
				} while (currPiece.getBackground() == opposingColor);
				break;
			case NORTHEAST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y - i > 0 && x+i < SIZE) currPiece = gameboard[y - i][x+i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case EAST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (x+i < SIZE) currPiece = gameboard[y][x+i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTHEAST:
				i = 0;
				 do {
					 i++;
					 currPiece.setBackground(playerColor);
					 if ((y+i) < SIZE && (x+i) < SIZE) currPiece = gameboard[y+i][x+i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTH:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y+i < SIZE) currPiece = gameboard[y+i][x];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case SOUTHWEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y+i < SIZE && x-i > 0) currPiece = gameboard[y+i][x-i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case WEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (x-i > 0) currPiece = gameboard[y][x-i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			case NORTHWEST:
				i = 0;
				do {
					i++;
					currPiece.setBackground(playerColor);
					if (y-i > 0 && x-i > 0) currPiece = gameboard[y - i][x-i];
				} while (currPiece.getBackground() == opposingColor);
				break;
			}
		}
		
		validDirections.clear();
	}

	public boolean isGameWon() {
		boolean isWon = true;
		
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (isValidMove(x, y, 0) || isValidMove(x, y, 1)) isWon = false;
			}
		}
		
		return isWon;
	}
	
	public boolean hasMovesAvailable(int player) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (isValidMove(x, y, player)) return true;
			}
		}
		
		return false;
	}
	
	public String getScore() {
		int playerOneScore = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (gameboard[i][j].getBackground() == Color.BLACK) {
					playerOneScore++;
				}
			}
		}
		
		if (playerOneScore > (SIZE*SIZE - playerOneScore)) 
			return "Player 1 wins, " + playerOneScore + " to " + (SIZE*SIZE - playerOneScore);
		else
			return "Player 2 wins, " + (SIZE*SIZE - playerOneScore) + " to " + playerOneScore;
	}
	
	public ArrayList<JButton[][]> getPossibleMoves(JButton[][] gameboard, int player) {
		JButton[][] gbCopy = gameboard.clone();
		ArrayList<JButton[][]> states = new ArrayList<>();
		
		if (hasMovesAvailable(player)) {
			for (int x = 0; x < SIZE; x++) {
				for (int y = 0; y < SIZE; y++) {
					if (isValidMove(x, y, player)) {
						flipPieces(gbCopy, x, y, player);
						states.add(gbCopy);
					}
					gbCopy = gameboard.clone();
				}
			}
		}
		
		return states;
		
	}
	
}
