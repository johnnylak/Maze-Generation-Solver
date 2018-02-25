package mazeSolver;

import maze.Cell;
import maze.Maze;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {
	boolean mazeSolved = false;
	// Keep track of cells we have visited
	int cellsVisited = 0;
	
	@Override
	public void solveMaze(Maze maze) {
		// Keep track of cells we have visited
		boolean[][] visited = null;
		
		if (maze.type == 2) {
			// If maze is of type hex
			visited = new boolean[maze.sizeR][maze.sizeC + (maze.sizeR + 1) / 2];
		} else {
			visited = new boolean[maze.sizeR][maze.sizeC];			
		}
		
		// Start at the entrance
		Cell currCell = maze.entrance;
		maze.drawFtPrt(currCell);
		cellsVisited++;
		
		// Go down a path using one of the entrance's neighbour
		for (int i = 0; i < currCell.neigh.length; i++) {
			if (currCell.neigh[i] != null && !visited[currCell.neigh[i].r][currCell.neigh[i].c] && !currCell.wall[i].present) {
				dfs(maze, currCell.neigh[i], visited);
				
				break;
			}
		}
	} // end of solveMaze()

	/*
	* ******************************************************************************************
    * 
    * Recursively visit all neighbours of a cell
    * 1: mark cell as visited
    * 2: maze.drawFtPrt(cell)
    * 3: for Cell c : neighbours
    * 4:     if not visited[ currCell ] then
    * 5:         dfs (maze, currCell, visited)
    * 6:     end if
    * 7: end for
    * 
    * ******************************************************************************************
    */
	public void dfs(Maze maze, Cell cell, boolean[][] visited) {
		// Check if we have reached the exit
		if (cell == maze.exit) {
			maze.drawFtPrt(cell);
			cellsVisited++;
			
			mazeSolved = true;
		}
		else {
			// Mark cell as visited
			visited[cell.r][cell.c] = true;
			
			maze.drawFtPrt(cell);
			cellsVisited++;
	        
			if (cell.tunnelTo != null && !visited[cell.tunnelTo.r][cell.tunnelTo.c]) {
				cell = cell.tunnelTo;
				
				visited[cell.r][cell.c] = true;
				
				maze.drawFtPrt(cell);
				cellsVisited++;
			}
			
			// Get a random neighbour that has no walls in the way
			for (int i = 0; i < cell.neigh.length; i++) {
				if (cell.neigh[i] != null && !visited[cell.neigh[i].r][cell.neigh[i].c] && !cell.wall[i].present) {
					// If maze is already solved, stop our recursion
					if (mazeSolved) {
						break;
					}
					else {
						// Keep going down a path until we hit a dead end
						dfs(maze, cell.neigh[i], visited);
					}
				}
			}
			
		}
	}
	
	@Override
	public boolean isSolved() {
		return mazeSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return cellsVisited;
	} // end of cellsExplored()

} // end of class RecursiveBackTrackerSolver