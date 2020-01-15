import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AlphaBetaPruning {
	public int bestMove;
	public double value;
	public int numVisited;
	public int numEvaluated;
	public int maxDepth;
	public double branchFactor;
	public double factor;

	public int depthCap;

	public AlphaBetaPruning() {
		bestMove = 0;
		value = 0;
		numVisited = 0;
		numEvaluated = 0;
		maxDepth = 0;
		branchFactor = 0;
	}

	/**
	 * This function will print out the information to the terminal, as specified in
	 * the homework description.
	 */
	public void printStats() {
		System.out.println("Move: " + bestMove);
		System.out.printf("Value: %.1f\n", value);
		System.out.println("Number of Nodes Visited: " + (numVisited + 1));
		System.out.println("Number of Nodes Evaluated: " + numEvaluated);
		System.out.println("Max Depth Reached: " + maxDepth);
		System.out.printf("Avg Effective Branching Factor: %.1f\n", numVisited / branchFactor);
	}

	/**
	 * This function will start the alpha-beta search
	 * 
	 * @param state
	 *            This is the current game state
	 * @param depth
	 *            This is the specified search depth
	 */
	public void run(GameState state, int depth) {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		int taken = 0;
		depthCap = depth;
		for (int x = 1; x < state.getSize() + 1; x++)
			if (!state.getStone(x))
				taken+=1;
		if (taken % 2 == 0)
			value = alphabeta(state, 0, alpha, beta, true, true);
		else
			value = alphabeta(state, 0, alpha, beta, false, true);

		ArrayList<GameState> list = (ArrayList<GameState>) state.getSuccessors();
		for (int i = 0; i < list.size(); i++) {
			if (Math.abs(alphabeta(list.get(i), 1, alpha, beta, taken % 2 == 0, false)) == Math.abs(value)) {
				bestMove = list.get(i).getLastMove();
				break;
			}
		}
	}

	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * 
	 * @param state
	 *            This is the current game state
	 * @param depth
	 *            Current depth of search
	 * @param alpha
	 *            Current Alpha value
	 * @param beta
	 *            Current Beta value
	 * @param maxPlayer
	 *            True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating score of the best next move
	 */
	private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer, boolean go) {
		if (depth > maxDepth && go)
			maxDepth = depth;
		if (depth == depthCap || state.evaluate() == 1 || state.evaluate() == -1) {
			if (go)
				numEvaluated += 1;
			return state.evaluate();
		}
		Double v = 0.0;
		if (maxPlayer) {
			v = Double.NEGATIVE_INFINITY;
			ArrayList<GameState> list = (ArrayList<GameState>) state.getSuccessors();
			for (int i = 0; i < list.size(); i++) {
				if (go)
					numVisited += 1;
				v = Math.max(v, alphabeta(list.get(i), depth + 1, alpha, beta, false, go));
				if (v >= beta) {
					if (go)
						branchFactor += 1;
					return v;
				}
				alpha = Math.max(alpha, v);
			}
			if (go)
				branchFactor += 1;
			return v;
		} else {
			v = Double.POSITIVE_INFINITY;
			ArrayList<GameState> list = (ArrayList<GameState>) state.getSuccessors();
			for (int i = 0; i < list.size(); i++) {
				if (go)
					numVisited += 1;
				v = Math.min(v, alphabeta(list.get(i), depth + 1, alpha, beta, true, go));
				if (v <= alpha) {
					if (go)
						branchFactor += 1;
					return v;
				}
				beta = Math.min(beta, v);
			}
			if (go)
				branchFactor += 1;
			return v;
		}
	}
}
