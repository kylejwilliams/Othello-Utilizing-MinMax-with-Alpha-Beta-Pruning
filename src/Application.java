
import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Main entry point of the program. This class is responsible for creating the 
 * window for the game and running the main game loop. 
 * 
 * @author Kyle
 *
 */
public class Application {
	private static JPanel panel;
	static Game g;
	static JFrame frame;
	
	/**
	 * Initializes the game window. 
	 */
	public static void startGame() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		frame = new JFrame("Othello");
		
		g = new Game(); 
		g.initBoard(panel, Game.gameboard);
		
		frame.getContentPane().add(panel);
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(frame.getSize());
		frame.pack();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame.setVisible(true);
		
	}
	
	/**
	 * Handles the state of the program after the game has finished. Notifies
	 * the player of the final score and prompts them to close the window.
	 */
	public static void endGame() {
		String[] options = new String[] { "Exit" };
		int response = JOptionPane.showOptionDialog(null,	
				g.getWinningScore(), "Othello",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, options[0]);
		if (response == 0) {
			frame.dispose();
			System.exit(0); 
		}
	}
	
	/**
	 * Entry point in the program
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		startGame();
		g.run();
		endGame();
	}
}
