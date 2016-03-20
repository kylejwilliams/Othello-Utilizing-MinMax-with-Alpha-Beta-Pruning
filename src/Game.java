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
	public ArrayList[] flippedPieces;
	
	public Game() {
		gameboard = new JButton[SIZE][SIZE];
		gameOutput = new JTextArea(2, 50);
		gameOutput.setEditable(false);
		
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
		
		int playOrder = getPlayOrder();
		
		for (int j = 0; j < SIZE; j++) {
			for (int i = 0; i < SIZE; i++) {
				int x = i;
				int y = j;
				gameboard[y][x].addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO implement flipping pieces
						if (isValidMove(x, y, 0) && isFirstPlayersTurn) {
							gameboard[y][x].setBackground(Color.BLACK);
							//flipPieces(gameboard, x, y, 0);
							isFirstPlayersTurn = false;
							System.out.println("Player 1 made a move at " 
									+ "(" + (x+1) + ", " + (y+1) + ")");
						}
						else if (isValidMove(x, y, 1) && !isFirstPlayersTurn) {
							gameboard[y][x].setBackground(Color.WHITE);
							//flipPieces(gameboard, x, y, 1);
							isFirstPlayersTurn = true;
							System.out.println("player 2 made a move at " 
									+ "(" + x + ", " + y + ")");
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
	
	public boolean isValidMove(int posX, int posY, int player) {
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
		
		
		if (gameboard[posY][posX].getBackground() == forestGreen) {	
			// north
			if (posY > 0 && 
					gameboard[posY-1][posX].getBackground() == opposingColor) {
				for (int i = posY-2; i > 0; i--) {
					if (gameboard[i][posX].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][posX].getBackground() == playerColor) {
						return true;
					}
				}
			}
			// northwest
			if (posY > 0 && posX > 0 &&
					gameboard[posY-1][posX-1].getBackground() == opposingColor) {
				for (int i = posY-2, j = posX-2; i > 0 && j > 0; i--, j--) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						return true;
					}
				}
			}
			// northeast
			if (posY > 0 && posX < SIZE - 1 &&
					gameboard[posY-1][posX+1].getBackground() == opposingColor) {
				for (int i = posY-2, j = posX+2; i > 0 && j < SIZE - 1; i--, j++) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						return true;
					}
				}
			}
//			// south
			if (posY < SIZE - 1 &&
					gameboard[posY+1][posX].getBackground() == opposingColor) {
				for (int i = posY+2; i < SIZE - 1; i++) {
					if (gameboard[i][posX].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][posX].getBackground() == playerColor) {
						return true;
					}
				}
			}
//			// southwest
			if (posY < SIZE - 1 && posX > 0 &&
					gameboard[posY+1][posX-1].getBackground() == opposingColor) {
				for (int i = posY+2, j = posX-2; i < SIZE - 1 && j > 0; i++, j--) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						return true;
					}
				}
			}
//			// southeast
			if (posY < SIZE - 1 && posX < SIZE - 1 &&
					gameboard[posY+1][posX+1].getBackground() == opposingColor) {
				for (int i = posY+2, j = posX+2; i < SIZE - 1 && j < SIZE - 1; i++, j++) {
					if (gameboard[i][j].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[i][j].getBackground() == playerColor) {
						return true;
					}
				}
			}
//			// west
			if (posX > 0 &&
					gameboard[posY][posX-1].getBackground() == opposingColor) {
				for (int i = posX-2; i > 0; i--) {
					if (gameboard[posY][i].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[posY][i].getBackground() == playerColor) {
						return true;
					}
				}
			}
//			// east
			if (posX < SIZE - 1 &&
					gameboard[posY][posX+1].getBackground() == opposingColor) {
				for (int i = posX+2; i < SIZE - 1; i++) {
					if (gameboard[posY][i].getBackground() == forestGreen) {
						break;
					}
					else if (gameboard[posY][i].getBackground() == playerColor) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void flipPieces(JButton[][] gameboard, int posX, int posY, int player) {
		Color playerColor;
		Color opposingColor;
		if (player == 0) {
			playerColor = Color.BLACK;
			opposingColor = Color.WHITE;
		}
		else if (player == 1) {
			playerColor = Color.WHITE;
			opposingColor = Color.BLACK;
		}
		
		// flip north pieces
		
	}
}
