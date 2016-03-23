
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class MiniMax {
	private int SIZE = 6;
	static Color playerColor;
	static Color opposingColor;
	public static int player;
	public static int opposingPlayer;
	DefaultMutableTreeNode root;
	
	public MiniMax(int player) {
		if (player == 0) {
			MiniMax.player = 0;
			playerColor = Color.BLACK;
			opposingPlayer = 1;
			opposingColor = Color.WHITE;
		}
		else {
			MiniMax.player = 1;
			playerColor = Color.WHITE;
			opposingPlayer = 0;
			playerColor = Color.BLACK;
		}
	}

	public DefaultMutableTreeNode minimax(JButton[][] gameboard, int depth, boolean maximizingPlayer) {
		DefaultMutableTreeNode nodeCopy = new DefaultMutableTreeNode(gameboard );
		DefaultMutableTreeNode bestMove = new DefaultMutableTreeNode();
		int bestValue = 0;
		DefaultMutableTreeNode v;
		
		if (maximizingPlayer) addChildren(nodeCopy, player);
		else addChildren(nodeCopy, opposingPlayer);
		
		if (depth == 0 || nodeCopy.isLeaf()) {
			return nodeCopy;
		}
		if (maximizingPlayer) {
			bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < nodeCopy.getChildCount(); i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)nodeCopy.getChildAt(i);
				v = minimax((JButton[][])child.getUserObject(), depth - 1, false);
				if (heuristic(new DefaultMutableTreeNode(v)) > bestValue) {
					bestMove = v;
				}
						
			}
			return bestMove;
		}
		else {
			bestValue = Integer.MAX_VALUE;
			for (int i = 0; i < nodeCopy.getChildCount(); i++) {
				JButton[][] child = (JButton[][]) ((DefaultMutableTreeNode)nodeCopy.getChildAt(i)).getUserObject();
				v = minimax(child, depth - 1, true);
				if (heuristic(new DefaultMutableTreeNode(v)) < bestValue) {
					bestMove = v;
				}
			}
			return bestMove;
		}
	}
	
	public JButton[][] iterativeDeepeningMinimax(JButton[][] gameboard) {
		int depth = 0;
		DefaultMutableTreeNode node;
		
		//while (true) {
			depth = 1;
			
			node = minimax(gameboard, depth, true);
			
			return getAIMove(node);
		//}
	}
	
	private static int heuristic(DefaultMutableTreeNode node) {
		JButton[][] board = (JButton[][]) node.getUserObject();
		int count = 0;
		
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				if (board[y][x].getBackground() == playerColor) count++; 
			}
		}
		
		return count;
	}
	
	private static void addChildren(DefaultMutableTreeNode parent, int player) {
		JButton[][] p = (JButton[][])parent.getUserObject();
		
		for (JButton[][] child : 
			Game.getPossibleMoves(p, player)) {
			parent.add(new DefaultMutableTreeNode(child));
		}
	}

	private static JButton[][] getAIMove(DefaultMutableTreeNode node) {
		
		DefaultMutableTreeNode parent = null;
		
		while (node.getParent() != null) parent = (DefaultMutableTreeNode) node.getParent();
		
		return (JButton[][])parent.getUserObject();
	}
}
