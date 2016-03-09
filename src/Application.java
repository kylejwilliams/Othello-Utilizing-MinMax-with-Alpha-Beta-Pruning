
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application extends JPanel {
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		Color gameboardColor = new Color(34, 139, 34);
		g2d.setColor(gameboardColor);
		g2d.fillRect(0, 0, 600, 600);
		
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < 600; i += 100) {
			g2d.drawRect(i, 0, 100, 600);
			g2d.drawRect(0, i, 600, 100);
		}
		
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 600, 600, 200);
		
		g2d.setColor(Color.WHITE);
		g2d.fillOval(200, 200, 100, 100);
		g2d.fillOval(300, 300, 100, 100);
		
		g2d.setColor(Color.BLACK);
		g2d.fillOval(200, 300, 100, 100);
		g2d.fillOval(300, 200, 100, 100);
	}

    public static void main(String[] args){
       JFrame frame = new JFrame("Othello");
       frame.add(new Application());
       frame.setSize(600, 800);
       frame.setVisible(true);
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}