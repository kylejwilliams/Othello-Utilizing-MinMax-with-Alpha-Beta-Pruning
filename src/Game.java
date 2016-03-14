import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
		System.out.println();
		System.out.println("On your turn, make a move by clicking on one of "
				+ "the squares");
		
		int playOrder = getPlayOrder();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = i;
				int y = j;
				gameboard[i][j].addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (isValidMove(x, y) && isFirstPlayersTurn) {
							gameboard[x][y].setBackground(Color.BLACK);
							isFirstPlayersTurn = false;
							System.out.println("Player 1 made a move at " 
									+ "(" + x + ", " + y + ")");
						}
						else if (isValidMove(x, y) && !isFirstPlayersTurn) {
							gameboard[x][y].setBackground(Color.WHITE);
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
		Border border = new LineBorder(Color.BLACK, 1);
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				gameboard[i][j] = new JButton();
				gameboard[i][j].setBackground(forestGreen);
				gameboard[i][j].setBorder(border);
				gbc = new GridBagConstraints(); 
				gbc.fill = GridBagConstraints.BOTH;
				//gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridx = j;
				gbc.gridy = i + 1;
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
				
				panel.add(gameboard[i][j], gbc);
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
	
	public boolean isValidMove(int posX, int posY) {
		return (gameboard[posX][posY].getBackground() == forestGreen);
	}
}
