import java.awt.*;
import java.awt.event.*;

public class Application {
	private Frame mainFrame;
	private Label headerLabel;
	private Label statusLabel;
	private Panel controlPanel;
	private Label msglabel;

	public Application(){
		prepareGUI();
	}

    public static void main(String[] args){
       Application Application = new Application();
    }

    private void prepareGUI() {
    	mainFrame = new Frame("Othello");
        mainFrame.setSize(800,600);
        mainFrame.setBackground(Color.YELLOW);
        
        mainFrame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent windowEvent){
        		System.exit(0);
        		}
        	});    
      
        mainFrame.setVisible(true);
    }
    
    public void paint(Graphics g) {
    	Graphics 2D 
    }
}