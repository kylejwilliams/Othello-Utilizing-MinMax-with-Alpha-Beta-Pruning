import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JButton;


/**
 * This class acts as a container for determining where to place a piece.
 * Each instance corresponds to a board position and it's associated score
 * 
 * @author Kyle
 *
 */
class MoveAndScore {
	Point move;
	int score;
	
	/**
	 * Constructor
	 * 
	 * @param score			heuristic of the given position
	 * @param coordinate	location on the board
	 */
	MoveAndScore(int score, Point coordinate) {
		this.score = score;
		this.move = coordinate;
	}
}


/**
 * This class handles the logic for the computer player
 * 
 * @author Kyle
 *
 */
public class MiniMax {
	ArrayList<Point> availablePoints; // list of possible moves
	
	// copy of the actual gameboard to prevent accidently altering the state
	ArrayList<ArrayList<JButton>> tmpGameboard; 
	
	public int aiPlayer; // turn order of the players. 0 = first, 1 = second
	public int opponentPlayer;
	
	static Point move; // initial starting position
	
	// represents the best move
	MoveAndScore curBestMove = new MoveAndScore(-1, new Point(-1, -1));
	
	boolean foundBestMove = false;
	
	/**
	 * This method recursively calls the alpha-beta pruning at increasing 
	 * depths to determine the most fit move
	 * 
	 * @param playOrder		which turn the AI is taking
	 * @return				the location of the best move to take
	 */
	public Point iterativeDeepeningMiniMax(int playOrder) {
		aiPlayer = playOrder;
		opponentPlayer = Math.abs(1 - aiPlayer);
		tmpGameboard = makeTempGameboard(Game.gameboard);
		
		// select the first possible move as the initial move
		move = Game.getPossibleMoves(aiPlayer, tmpGameboard).get(0);
		
		
		// generate the combined position and score of the selected initial move
		ArrayList<ArrayList<JButton>> initialState = makeTempGameboard(tmpGameboard);
		Game.flipPieces(initialState, move.x, move.y, aiPlayer);
		MoveAndScore node = new MoveAndScore(Game.heuristic(initialState, aiPlayer), move);
		
		int depth = 0;
		
		// for determining when the computer should stop computing it's move
		long startTime = System.currentTimeMillis();
		long waitTime = 5000;
		long endTime = startTime + waitTime;
		
		// loop until out of time
		while (System.currentTimeMillis() < endTime) {
			depth++;
			
			alphaBetaMinimax(node, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, aiPlayer, endTime, tmpGameboard);
		}
		
		foundBestMove = false;
		return curBestMove.move;
	}
	
	/**
	 * Alpha Beta pruning. Searches each child move down until it finds a worse
	 * move than the current one, and then sends that move up recursively to be
	 * compared to other moves. It then searches every other child, up unitl the 
	 * time cutoff.
	 * 
	 * @param node		the current move/score combination
	 * @param depth		the current depth to search to
	 * @param alpha		largest score
	 * @param beta		smallest score
	 * @param player	turn order in which the computer goes
	 * @param endTime	time cutoff
	 * @param board		board to search for moves
	 * @return			score of the best move found
	 */
	private int alphaBetaMinimax(MoveAndScore node, int depth, int alpha, int beta, int player, long endTime, ArrayList<ArrayList<JButton>> board) {		
		// breaking conditions
		if (depth == 0 || Game.isGameWon(tmpGameboard) || System.currentTimeMillis() > endTime) {
			if (!foundBestMove) {
				curBestMove.move = node.move;
				curBestMove.score = node.score;
				foundBestMove = true;
			}
			
			// temporarily make the move and return the score that results
			Game.flipPieces(makeTempGameboard(tmpGameboard), curBestMove.move.x, curBestMove.move.y, player);
			return Game.heuristic(tmpGameboard, aiPlayer);
		}
		
		if (player == aiPlayer) {
			int v = Integer.MIN_VALUE;
			
			availablePoints = Game.getPossibleMoves(player, board);
			for (Point curMove : availablePoints) {
				ArrayList<ArrayList<JButton>> boardStateAfterChildMove = makeTempGameboard(board);
				Game.flipPieces(boardStateAfterChildMove, curMove.x, curMove.y, player);
				MoveAndScore child = new MoveAndScore(Game.heuristic(boardStateAfterChildMove, aiPlayer), curMove);
				
				v = Math.max(alphaBetaMinimax(child, depth - 1, alpha, beta, opponentPlayer, endTime, boardStateAfterChildMove), curBestMove.score);
				alpha = Math.max(alpha, v);
				if (beta <= alpha) break;
			}
			return v;
		}
		else {
			int v = Integer.MAX_VALUE;
			
			availablePoints = Game.getPossibleMoves(player, board);
			for (Point curMove : availablePoints) {
				ArrayList<ArrayList<JButton>> boardStateAfterChildMove = makeTempGameboard(board);
				Game.flipPieces(boardStateAfterChildMove, curMove.x, curMove.y, player);
				MoveAndScore child = new MoveAndScore(Game.heuristic(boardStateAfterChildMove, aiPlayer), curMove);
				v = Math.max(alphaBetaMinimax(child, depth - 1, alpha, beta, aiPlayer, endTime, boardStateAfterChildMove), curBestMove.score);
				beta = Math.min(beta, v);
				if (beta <= alpha) break;
			}
			return v;
		}
	}

	/**
	 * Used to make a deep copy of the gameboard.
	 * (couldn't figure out another way to temporarily change it)
	 * 
	 * @param old
	 * @return
	 */
	private ArrayList<ArrayList<JButton>> makeTempGameboard(ArrayList<ArrayList<JButton>> old) {
		ArrayList<ArrayList<JButton>> newGameboard = new ArrayList<ArrayList<JButton>>(old.size());
		ArrayList<JButton> inner = new ArrayList<>();
		for (int i = 0; i < old.size(); i++) {
			for (int j = 0; j < old.size(); j++) {
				JButton button = new JButton();
				button.setBackground(old.get(i).get(j).getBackground());
				inner.add(button);
			}
			newGameboard.add(new ArrayList<>(inner));
			inner = new ArrayList<>();
		}
		
		return newGameboard;
	}
}
