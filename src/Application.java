
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Application {
	private static JPanel panel;
	static Game g;
	static JFrame frame;
	
	public Application() {	
	}
	
	public static JPanel getUI() {
		return panel;
	}
	
	public static void startGame() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		frame = new JFrame("Othello");
		
		g = new Game(); 
		g.initConsole(panel);
		g.initBoard(panel, Game.gameboard);
		
		frame.getContentPane().add(getUI());
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
		
		g.run();
	}
	
	public static void restart() {
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				startGame();
				
				
			}
		});
	}

}
