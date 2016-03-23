
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;

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
	
//	public int minimax(JButton[][] gameboard, int depth, boolean maximizingPlayer) {
//		DefaultMutableTreeNode nodeCopy = new DefaultMutableTreeNode(gameboard.clone());
//		int bestValue = 0;
//		int v = 0;
//
//		if (depth == 0 || nodeCopy.isLeaf()) {
//			bestAction = (JButton[][]) nodeCopy.getUserObject();
//			return heuristic(nodeCopy);
//		}
//		if (maximizingPlayer) {
//			bestValue = Integer.MIN_VALUE;
//			addChildren(nodeCopy, player);
//			for (int i = 0; i < nodeCopy.getChildCount(); i++) {
//				JButton[][] child = (JButton[][]) ((DefaultMutableTreeNode)nodeCopy.getChildAt(i)).getUserObject();
//				v = minimax(child, depth - 1, false);
//				bestValue = Integer.max(bestValue, v);
//			}
//			bestAction = (JButton[][]) nodeCopy.getUserObject();
//			return bestValue;
//		}
//		else {
//			bestValue = Integer.MAX_VALUE;
//			addChildren(nodeCopy, opposingPlayer);
//			for (int i = 0; i < nodeCopy.getChildCount(); i++) {
//				JButton[][] child = (JButton[][]) ((DefaultMutableTreeNode)nodeCopy.getChildAt(i)).getUserObject();
//				v = minimax(child, depth - 1, true);
//				bestValue = Integer.min(bestValue, v);
//			}
//			bestAction = (JButton[][]) nodeCopy.getUserObject();
//			return bestValue;
//		}
//	}
	
	public JButton[][] minimax(JButton[][] gameboard, int depth, boolean maximizingPlayer) {
		DefaultMutableTreeNode nodeCopy = new DefaultMutableTreeNode(gameboard.clone());
		JButton[][] bestMove = new JButton[SIZE][SIZE];
		int bestValue = 0;
		JButton[][] v;
		
		if (maximizingPlayer) addChildren(nodeCopy, player);
		else addChildren(nodeCopy, opposingPlayer);
		
		if (depth == 0 || nodeCopy.isLeaf()) {
			return (JButton[][]) nodeCopy.getUserObject();
		}
		if (maximizingPlayer) {
			bestValue = Integer.MIN_VALUE;
			for (int i = 0; i < nodeCopy.getChildCount(); i++) {
				JButton[][] child = (JButton[][]) ((DefaultMutableTreeNode)nodeCopy.getChildAt(i)).getUserObject();
				v = minimax(child, depth - 1, false);
				if (heuristic(new DefaultMutableTreeNode(v)) > bestValue)
						bestMove = v;
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
		JButton[][] move = new JButton[SIZE][SIZE];
		
		while (true) {
			depth++;
			
			move = minimax(gameboard, depth, true);
			
			return move;
		}
	}
	
//	public void makeMinMaxMove(JButton[][] gameboard) {
//		for (int y = 0; y < SIZE; y++) {
//			for (int x = 0; x < SIZE; x++) {
//				gameboard[y][x].setBackground(bestAction[y][x].getBackground());
//			}
//		}
//		
//	}
	
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
}
