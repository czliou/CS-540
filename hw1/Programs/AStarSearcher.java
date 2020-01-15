import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		Square root = maze.getPlayerSquare();
		explored[root.X][root.Y] = true;
		State r = new State(root, null, 0, 0);
		frontier.add(new StateFValuePair(r, heuristic(root.X, root.Y)));
		// TODO initialize the root state and add
		// to frontier list
		// ...

		
		
		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
			State s = frontier.poll().getState();
			noOfNodesExpanded += 1;
			if(s.getDepth() > maxDepthSearched)
				maxDepthSearched = s.getDepth();
			if(s.isGoal(maze)) {
				//cost = disMap[s.getX()][s.getY()];
				cost = s.getGValue();
				State temp = s.getParent();
				while(temp.getParent() != null) {
					maze.getMazeMatrix()[temp.getX()][temp.getY()] = '.';
					temp = temp.getParent();
				}
				return true;
			}
			
			ArrayList<State> children = s.getSuccessors(explored, maze);
			for(State child: children) {
				StateFValuePair c = new StateFValuePair(child, child.getGValue() + heuristic(child.getX(), child.getY()));
				if(!explored[child.getX()][child.getY()]) {
					explored[child.getX()][child.getY()] = true;
					frontier.add(c);
				}
				else {
					Iterator<StateFValuePair> states = frontier.iterator();
					while(states.hasNext()) {
						StateFValuePair mp = states.next();
						State m = mp.getState();
						if(m.getY() == child.getX() && m.getY() == child.getY() && m.getGValue() > child.getGValue()) {
							frontier.remove(mp);
							frontier.add(c);
							break;
						}
					}
				}
				if(frontier.size() > maxSizeOfFrontier)
					maxSizeOfFrontier = frontier.size();
			}
		}

		return false;
	}

	private double heuristic(int x, int y) {
		double a = x - maze.getGoalSquare().X;
		double b = y - maze.getGoalSquare().Y;
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}

}
