
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Application {
	private JPanel panel;
	Game g = new Game();
	
	public Application() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		g.initConsole(panel);
		g.initBoard(panel);
	}
	
	public JPanel getUI() {
		return panel;
	}
	

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame("Othello");
				Application app = new Application();
				frame.getContentPane().add(app.getUI());
				frame.setLocationByPlatform(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setPreferredSize(new Dimension(800, 600));
				frame.setMinimumSize(frame.getSize());
				frame.pack();
				
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				frame.setVisible(true);
				
				app.g.run(); 
			}
		});
	}
	
}
