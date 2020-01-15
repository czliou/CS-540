import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		
		Square start = maze.getPlayerSquare();
		explored[start.X][start.Y] = true;
		State s = new State(start, null, 0, 0);
		queue.add(s);
		maxSizeOfFrontier = 1;
		maxDepthSearched = 0;
		
		while (!queue.isEmpty()) {
			s = queue.pop();
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
				if(!explored[child.getX()][child.getY()]) {
					explored[child.getX()][child.getY()] = true;
					queue.add(child);
					if(queue.size() > maxSizeOfFrontier)
						maxSizeOfFrontier = queue.size();
				}
			}
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
		}

		// TODO return false if no solution
		return false;
	}
}
