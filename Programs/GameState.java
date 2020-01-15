import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState{
	private int size; // The number of stones
	private boolean[] stones; // Game state: true for available stones, false for taken ones
	private int lastMove; // The last move

	/**
	 * Class constructor specifying the number of stones.
	 */
	public GameState(int size) {

		this.size = size;

		// For convenience, we use 1-based index, and set 0 to be unavailable
		this.stones = new boolean[this.size + 1];
		this.stones[0] = false;

		// Set default state of stones to available
		for (int i = 1; i <= this.size; ++i) {
			this.stones[i] = true;
		}

		// Set the last move be -1
		this.lastMove = -1;
	}

	/**
	 * Copy constructor
	 */
	public GameState(GameState other) {
		this.size = other.size;
		this.stones = Arrays.copyOf(other.stones, other.stones.length);
		this.lastMove = other.lastMove;
	}

	/**
	 * This method is used to compute a list of legal moves
	 *
	 * @return This is the list of state's moves
	 */
	public List<Integer> getMoves() {
		List<Integer> list = new ArrayList<Integer>();
		if (lastMove == -1) {
			int limit = 0;
			if(size%2 == 0)
				limit = size/2 - 1;
			else
				limit = size/2;
			for (int i = 1; i <= limit; i++) {
				if (i % 2 != 0)
					list.add(i);
			}
		}
		else
			for (int i = 1; i < stones.length; i++) {
				if (stones[i] && (lastMove % i == 0 || i % lastMove == 0))
					list.add(i);
			}
		return list;
	}

	/**
	 * This method is used to generate a list of successors using the getMoves()
	 * method
	 *
	 * @return This is the list of state's successors
	 */
	public List<GameState> getSuccessors() {
		return this.getMoves().stream().map(move -> {
			GameState state = new GameState(this);
			state.removeStone(move);
			return state;
		}).collect(Collectors.toList());
	}

	/**
	 * This method is used to evaluate a game state based on the given heuristic
	 * function
	 *
	 * @return int This is the static score of given state
	 */
	public double evaluate() {
		Helper help = new Helper();
		List<Integer> moves = getMoves();
		int taken = 0;
		int count = 0;
		for (int i = 1; i < stones.length; i++)
			if (!stones[i])
				taken+=1;
		
		if (taken % 2 == 0) {
			if (moves.size() == 0) {
				return -1.0;
			}
			if (lastMove == -1)
				return 0;
			if (lastMove == 1) {
				if (moves.size() % 2 != 0)
					return 0.5;
				return -0.5;
			}
			if (help.isPrime(lastMove)) {
				for (int i = 0; i < moves.size(); i++) {
					if (moves.get(i) % lastMove == 0)
						count+=1;
				}
				if (count % 2 != 0)
					return 0.7;
				return -0.7;
			} else {
				int pf = help.getLargestPrimeFactor(lastMove);
				for (int i = 0; i < moves.size(); i++) {
					if (moves.get(i) % pf == 0)
						count+=1;
				}
				if (count % 2 != 0)
					return 0.6;
				return -0.6;
			}
		}

		else {
			if (moves.size() == 0) {
				return 1.0;
			}
			if (lastMove == -1)
				return 0;
			if (lastMove == 1) {
				if (moves.size() % 2 != 0)
					return -0.5;
				return 0.5;
			}
			if (help.isPrime(lastMove)) {
				for (int i = 0; i < moves.size(); i++) {
					if (moves.get(i) % lastMove == 0)
						count+=1;
				}
				if (count % 2 != 0)
					return -0.7;
				return 0.7;
			} else {
				int pf = help.getLargestPrimeFactor(lastMove);
				for (int i = 0; i < moves.size(); i++) {
					if (moves.get(i) % pf == 0)
						count+=1;
				}
				if (count % 2 != 0)
					return -0.6;
				return 0.6;
			}
		}
	}

	/**
	 * This method is used to take a stone out
	 *
	 * @param idx
	 *            Index of the taken stone
	 */
	public void removeStone(int idx) {
		this.stones[idx] = false;
		this.lastMove = idx;
	}

	/**
	 * These are get/set methods for a stone
	 *
	 * @param idx
	 *            Index of the taken stone
	 */
	public void setStone(int idx) {
		this.stones[idx] = true;
	}

	public boolean getStone(int idx) {
		return this.stones[idx];
	}

	/**
	 * These are get/set methods for lastMove variable
	 *
	 * @param move
	 *            Index of the taken stone
	 */
	public void setLastMove(int move) {
		this.lastMove = move;
	}

	public int getLastMove() {
		return this.lastMove;
	}

	/**
	 * This is get method for game size
	 *
	 * @return int the number of stones
	 */
	public int getSize() {
		return this.size;
	}

}
