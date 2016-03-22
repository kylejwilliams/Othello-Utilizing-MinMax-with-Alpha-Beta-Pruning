
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;

public class MiniMax {
	JButton[][] gb;
	Color playerColor;
	int player;
	DefaultMutableTreeNode root;
	Game game;
	
	public MiniMax(JButton[][] gameboard, int player) {
		gb = gameboard.clone();
		root = new DefaultMutableTreeNode(gb);
		
		if (player == 0) {
			playerColor = Color.BLACK;
			player = 0;
		}
		else {
			playerColor = Color.WHITE;
			player = 0;
		}
	}
	
	// function minimax(node, depth, maximizingPlayer)
	int minimax(DefaultMutableTreeNode node, int depth, boolean maximizingPlayer) {
		int bestValue = 0;
		int v = 0;
		addChildren(node, game);
		// if depth = 0 or node is a terminal node
		if (depth == 0 || node.isLeaf()) {
			// return the heuristic value of node
			return heuristic(node);
		}
		// if maximizingPlayer
		if (maximizingPlayer) {
			// bestValue = -INF
			bestValue = Integer.MIN_VALUE;
			// for each child of node
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
				// v = minimax(child, depth - 1, FALSE)
				v = minimax(child, depth - 1, false);
				// bestValue = max(bestValue, v)
				bestValue = Integer.max(bestValue, v);
			}
			return bestValue;
		}
		// else
		else {
			// bestValue = +INF
			bestValue = Integer.MAX_VALUE;
			// for each child of node
			for (int i = 0; i < node.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
				// v = minimax(child, depth - 1, TRUE)
				v = minimax(child, depth - 1, true);
				// bestValue = min(bestValue, v)
				bestValue = Integer.min(bestValue, v);
			}
			// return bestValue
			return bestValue;
		}
	}
	
	private int heuristic(DefaultMutableTreeNode node) {
		JButton[][] board = (JButton[][])node.getUserObject();
		int count = 0;
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				if (board[y][x].getBackground() == playerColor) count++; 
			}
		}
		
		return count;
	}
	
	private void addChildren(DefaultMutableTreeNode parent, Game game) {
		for (JButton[][] child : 
			game.getPossibleMoves((JButton[][])parent.getUserObject(), player)) {
			parent.add(new DefaultMutableTreeNode(child));
		}
	}
}


