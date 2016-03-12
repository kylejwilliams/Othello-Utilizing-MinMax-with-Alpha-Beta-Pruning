import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

public class Gameboard {
	public void draw(Graphics2D g2d) {
		Color gameboardColor = new Color(34, 139, 34);
		g2d.setColor(gameboardColor);
		g2d.fillRect(0, 0, 600, 600);
		
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < 600; i += 100) {
			g2d.drawRect(i, 0, 100, 600);
			g2d.drawRect(0, i, 600, 100);
		}
	}
}
