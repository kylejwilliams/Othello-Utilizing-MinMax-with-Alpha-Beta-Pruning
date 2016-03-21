import javax.swing.JButton;

public class MiniMax {
	JButton[][] gb;
	
	public MiniMax(JButton[][] gameboard) {
		gb = gameboard;
	}
	
	// function MINIMAX-DECISION(state) returns an action
	public void minimaxDecision(JButton[][] gameboard) {
		// v <- MAX_VALUE(state)
		// return the action in SUCCESSORS(state) with value v
	}
		
	
	// function MAX-VALUE(state) returns a utility value
	public int maxValue(JButton[][] gameboard) {
		// if TERMINAL-TEST(state) then return UTILITY(state)
		// v <- -inf
		int v = Integer.MIN_VALUE;
		
		// for a,s in SUCCESSORS(state) do
		int index = -1;
		for (int i = 0; i < )
			// v <- MAX(v, MIN-VALUE(s))
		// return v
	}

	
	// function MIN-VALUE(state) returns a utility value
		// if TERMINAL-TEST(state) then return UTILITY(state)
		// v <- inf
		// for a,s in SUCCESSORS(state) do
			// v <- MIN(v, MAX-VALUE(s))
		// return v
}
