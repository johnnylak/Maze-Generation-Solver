package mazeSolver;

import maze.Cell;
import maze.Maze;
import maze.Wall;
import mazeSolver.MazeSolver;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements Bi-directional BFS maze solving algorithm.
 */
public class BiDirectionalBFSSolver implements MazeSolver {

//	Queue to keep track of the front coming from the entrance
	private Queue<Cell> entranceQueue = new LinkedList<Cell>();
//	Queue to keep track of the front coming from the exit
	private Queue<Cell> exitQueue = new LinkedList<Cell>();

//	Boolean to tell if maze is solved by this algorithm or not
	private boolean solved = false;

//	Matrix to keep track of the visited cells
	private int[][] visitedCells;

//	Class wide reference to the input maze
	private Maze maze;
	private static final int NOT_VISITED = 0;
	private static final int ENTRANCE_VISITED = 1;
	private static final int EXIT_VISITED = 2;


	@Override
	public void solveMaze(Maze maze) {

//		Create instance variable for maze
		this.maze = maze;

//		Setup the visitedCells matrix depending on the maze type
		if (this.maze.type == 2) {
			this.visitedCells = new int[this.maze.sizeR][this.maze.sizeC + (maze.sizeR + 1) / 2];
		} else {
			this.visitedCells = new int[this.maze.sizeR][this.maze.sizeC];
		}

		/**
		 * For loop to mark all the cells as unvisited.
		 *
		 * Input: Matrix of integers representing cells
		 *
		 * Output: Matrix set to all cells UNVISITED
		 *
		 * for: every value int[i][j] in the cell marker matrix vistitedCells
		 * 		set int[i][j] to unvisited
		 */
		for (int i = 0; i < this.visitedCells.length; ++i) {
			for (int j = 0; j < this.visitedCells[i].length; ++j) {
				visitedCells[i][j] = NOT_VISITED;
			}
		}

//		Get the entrance point of the maze and make it the starting point for the entry queue
		entranceQueue.add(maze.entrance);
//		Mark the entrance cell as visited by the entrance queue
		visitedCells[maze.entrance.r][maze.entrance.c] = ENTRANCE_VISITED;

//		Get the exit point of the maze and make it the starting point for the exit queue
		exitQueue.add(maze.exit);
//		Mark the exit cell as visited by the exit queue
		visitedCells[maze.exit.r][maze.exit.c] = EXIT_VISITED;

		/**
		 * Using BFS from the entry and exit points, find the point the first intersect, this will be their shortest path
		 *
		 * 		while entry and exit queues are NOT empty
		 * 			take a look at the entries at the front of entry and exit queues (AKA fronts), entranceN and exitN
		 * 			draw the entranceN and exitN points on the GUI
		 * 			if entranceN or exitN are a tunnel to a cell
		 * 				move to the other end of the tunnel
		 * 				mark the end of the tunnel as visited by the appropriate front
		 * 				add the end of the tunnel to the front queue
		 * 				draw the end of the tunnel on the GUI
		 *
		 *			get a neighbouring Cell "c" of "n" that has not been marked as visited by "n" front
		 *			if there is such a "c"
		 *				check if "c" has been visited by another front
		 *					if yes then maze has been solved
		 *					otherwise mark "c" as visited by the selected front
		 *						add "c" to the queue
		 *			if no "c" exists from "n"
		 *				dequeue "n"
		 *
		 */

		while (!entranceQueue.isEmpty() && !exitQueue.isEmpty())
		{

			/**
			 * Line 74
			 */
			Cell entranceN = entranceQueue.peek();
			Cell exitN = exitQueue.peek();

			/**
			 * Line 75
			 */
			this.maze.drawFtPrt(entranceN);
			this.maze.drawFtPrt(exitN);

			/**
			 * Entrance tunnel: line 76
			 */
			if(entranceN.tunnelTo != null)
			{
				entranceN = entranceN.tunnelTo;
				visitedCells[entranceN.r][entranceN.c] = ENTRANCE_VISITED;
				entranceQueue.add(entranceN);
				this.maze.drawFtPrt(entranceN);
			}

			/**
			 * Exit tunnel: line 76
			 */
			if(exitN.tunnelTo != null)
			{
				exitN = exitN.tunnelTo;
				visitedCells[exitN.r][exitN.c] = EXIT_VISITED;
				exitQueue.add(exitN);
				this.maze.drawFtPrt(exitN);
			}

			/**
			 * Entrance BFS : line 82
			 */
			Cell entranceChild = this.getUnvisitedNeighbour(entranceN, ENTRANCE_VISITED);

			if(entranceChild != null) {

				if(visitedCells[entranceChild.r][entranceChild.c] == EXIT_VISITED)
				{
					solved = true;
					break;
				}

				visitedCells[entranceChild.r][entranceChild.c] = ENTRANCE_VISITED;
				this.maze.drawFtPrt(entranceChild);

//				Queue the current child
				entranceQueue.add(entranceChild);
			} else {
//				Dequeue entranceN
				entranceQueue.remove();
			}

			/**
			 * Exit BFS: line 82
			 */

			Cell exitChild = this.getUnvisitedNeighbour(exitN, EXIT_VISITED);

			if(exitChild != null)
			{

				if(visitedCells[exitChild.r][exitChild.c] == ENTRANCE_VISITED)
				{
					solved = true;
					break;
				}

				visitedCells[exitChild.r][exitChild.c] = EXIT_VISITED;
				this.maze.drawFtPrt(exitChild);

//				Queue the current child
				exitQueue.add(exitChild);
			} else {
//				Dequeue entranceN
				exitQueue.remove();
			}

		}

	}

	/**
	 * Getter to show if the maze has been solved
	 * @return - boolean value: true for solved, false for unsolved
     */
	@Override
	public boolean isSolved()
	{
		return solved;
	}

	/**
	 * Count in total how many cells where marked as visited at any time
	 *
	 * Input - nothing
	 * Output - Count of how many cells have been used by the solver at any point
	 *
	 * procedure cellsExplored:
	 *
	 * 	counter set to 0
	 *
	 * 	for every row "i" in the visitedCells matrix
	 * 		for every column "j" in the visitedCells matrix
	 * 			if the cell visitedCells[i][j] is marked visited
	 * 				add 1 to counter
	 * 	return the counter
	 *
	 * @return - The number of cells visited by the solver
     */
	@Override
	public int cellsExplored()
	{
		// TODO Auto-generated method stub

		int count = 0;

		for(int i = 0; i < maze.sizeR; i++)
			for(int j = 0; j < maze.sizeC; j++)
				if(visitedCells[i][j] > 0)
					count++;

		return count;
	} // end of cellsExplored()


	/**
	 * Method to get the next unvisited cell around a cell c that has not been visited by its front.
	 * Also checks that the neighbours that can be selected DONT have a wall before being selected.
	 *
	 * Input: Cell "c" that has neighbours to check, the cells front "visited"
	 * Output: Cell "neighbour" that is the next unvisited neighbouring cell, null if all neighbours visited.
	 *
	 * procedure getUnvisitedNeighbour(Cell "c" neghbours to check, Cells front "visited")
	 * 		for every neighbour of c "neighbourC"
	 * 			check if "neighbourC" is null, go to start of loop if so
	 *
	 * 			for every Wall "neighbourCWall" of "neighbourC"
	 * 					if the "neighbourCWall" is null, try this loop again
	 *
	 * 					for every Wall of c "cWall"
	 * 						if "cWall" is null, try this loop again
	 * 						if the Walls of "c" and "neighbourC" are the same
	 * 							check if the common wall is present
	 * 									if yes then move onto another cell
	 * 									if no, check if it has already been visited by the selected front
	 * 										if yes then move onto another cell
	 * 										if no return the the neighbouring Cell "neighbourC"
	 *
	 * 		return null if no cell is found with the above criteria
	 *
	 * @param c - Cell to check if neighbours are accesable or have been visited by its front
	 * @param visited - Front to select
     * @return - The next accesible Cell if found, otherwise null
     */
	private Cell getUnvisitedNeighbour(Cell c, int visited)
	{
		Cell neighbour = null;

		for(Cell neighbourC : c.neigh)
		{
			if(neighbourC == null)
				continue;

			for(Wall neighbourCWall : neighbourC.wall)
			{
				if(neighbourCWall == null)
					continue;

				for(Wall cWall : c.wall)
				{
					if(cWall == null)
						continue;

					if(cWall.equals(neighbourCWall))
					{
						if(neighbourCWall.present)
							continue;

						if(visitedCells[neighbourC.r][neighbourC.c] == visited)
							continue;

						return neighbourC;
					}
				}
			}
		}

		return neighbour;
	}
}

