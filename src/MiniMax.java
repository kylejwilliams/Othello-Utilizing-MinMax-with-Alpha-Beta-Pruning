import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JButton;



class CoordinatesAndScores {
	Point coordinate;
	int score;
	
	CoordinatesAndScores(int score, Point coordinate) {
		this.score = score;
		this.coordinate = coordinate;
	}
}

public class MiniMax {
	ArrayList<Point> availablePoints;
	ArrayList<CoordinatesAndScores> childrensScores;
	JButton[][] tmpGameboard = Game.gameboard.clone();
	int originalPlayOrder;
	int opposingPlayOrder;
	boolean outOfTime;
	int iter;
	
	public void iterativeDeepeningMiniMax(int playerOrder) {
		int depth = 0;
		originalPlayOrder = playerOrder;
		if (originalPlayOrder == 0) opposingPlayOrder = 1;
		else opposingPlayOrder = 0;
		
		long startTime = System.currentTimeMillis();
		long waitTime = 5000;
		long endTime = startTime + waitTime;
		
		while (System.currentTimeMillis() < endTime) {
			depth++;
			minimax(depth, originalPlayOrder, endTime);
		}
	}

	private int minimax(int depth, int playerOrder, long endTime) {
		if (System.currentTimeMillis() > endTime) outOfTime = true;
		//if (outOfTime) break;
		if (Game.hasBlackWon() && originalPlayOrder == 0) return Integer.MAX_VALUE;
		if (Game.hasWhiteWon() && originalPlayOrder == 1) return Integer.MAX_VALUE;
		if (Game.hasBlackWon() && originalPlayOrder == 1) return Integer.MIN_VALUE;
		if (Game.hasWhiteWon() && originalPlayOrder == 0) return Integer.MIN_VALUE;
		
		availablePoints = Game.getPossibleMoves(playerOrder);
		//tmpGameboard = Game.gameboard.clone(); // so that we can reset the gameboard at the end
		int curScore;
		
		if (availablePoints.isEmpty()) return 0;
		
		ArrayList<Integer> scores = new ArrayList<>();
		
		for (Point curMove : availablePoints) {
			// AI's turn
			if (playerOrder == originalPlayOrder) {
				Game.flipPieces(tmpGameboard, curMove.x, curMove.y, playerOrder);
				
				for (int i = 0; i < tmpGameboard.length; i++) {
					for (int j = 0; j < tmpGameboard.length; j++) {
						System.out.println("(" + j + ", " + i + "): " + tmpGameboard[i][j].getBackground().toString());
					}
				}
				curScore = minimax(depth + 1, 1, endTime);
				scores.add(curScore);
				
				if (depth == 0) childrensScores.add(new CoordinatesAndScores(curScore, curMove));
			}
			// human turn
			else {
				Game.flipPieces(tmpGameboard, curMove.x, curMove.y, opposingPlayOrder);
				for (int i = 0; i < tmpGameboard.length; i++) {
					for (int j = 0; j < tmpGameboard.length; j++) {
						System.out.println("(" + j + ", " + i + "): " + tmpGameboard[i][j].getBackground().toString());
					}
				scores.add(minimax(depth + 1, 0, endTime));
			}
			tmpGameboard = new JButton[6][6]; // need to reset the board to previous state
			for (int i = 0; i < tmpGameboard.length; i++) {
				for (int j = 0; j < tmpGameboard.length; j++) {
					System.out.println("(" + j + ", " + i + "): " + tmpGameboard[i][j].getBackground().toString());
				}
		}
		if (playerOrder == 0) return Max(scores);
		else return Min(scores);
		
	}
	
	private int Min(ArrayList<Integer> scores) {
		int min = Integer.MAX_VALUE;
		int index = -1;
		
		for (Integer i : scores) {
			if (i < min) {
				min = i;
				//index = scores.indexOf(i);
			}
		}
		return min;
	}
	
	private int Max(ArrayList<Integer> scores) {
		int max = Integer.MIN_VALUE;
		int index = -1;
		
		for (Integer i : scores) {
			if (i > max) {
				max = i;
				//index = scores.indexOf(i);
			}
		}
		return max;
	}
	
	public Point bestMoveCoordinates() {
		int max = Integer.MIN_VALUE;
		int best = -1;
		
		for (CoordinatesAndScores cas : childrensScores) {
			if (max < cas.score) max = cas.score;
			best = childrensScores.indexOf(max);
		}
		
		return childrensScores.get(best).coordinate;
	}
}
