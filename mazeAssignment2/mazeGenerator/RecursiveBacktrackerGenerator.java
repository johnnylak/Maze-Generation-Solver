package mazeGenerator;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	private Stack<Cell> cellsStack = new Stack<>();

	private ArrayList<Cell> cellsUnvisited = new ArrayList<>();

	@Override
	public void generateMaze(Maze maze) {

		ArrayList<Cell> cellNeighbours = new ArrayList<>();


		/**
		 * Put all Cells from to maze matrix into the cellsUnvisited ArrayList
		 * for each Cell array "c" in the maze x axis
		 * 		for each Cell "c1" in the "c" row
		 * 			add "c1" to cellsUnvisited
		 */
		for(Cell[] c : maze.map)
			for(Cell c1 : c)
				cellsUnvisited.add(c1);

		/**
		 * Get a random Cell in the maze and assign to currentCell
		 */

		int RandomCellNumberX = new Random().nextInt(maze.sizeC);
		int RandomCellNumberY = new Random().nextInt(maze.sizeR);


		Cell currentCell = maze.map[RandomCellNumberY][RandomCellNumberX];

		/**
		 * If the currentCell is null, keep trying until not null (usually 2 loops finds one)
		 */
		while(currentCell == null)
		{
			RandomCellNumberX = new Random().nextInt(maze.sizeC);
			RandomCellNumberY = new Random().nextInt(maze.sizeR);


			currentCell = maze.map[RandomCellNumberY][RandomCellNumberX];
		}


		/**
		 * Remove the currentCell from the cellsUnvisited list
		 */
		cellsUnvisited.remove(currentCell);


		/**
		 * Using Recursive Backtracker to generate maze
		 *
		 * while cellsVisited is not empty
		 * 		if the "currentCell" is a tunnel to another Cell
		 * 			if the "cellStack" is not empty
		 * 				if the top Cell in "cellStack" is the "currentCell" AND the "cellsUnvisited" contains the "currentCell"
		 * 					push "currentCell" to the stack
		 * 				end if
		 * 			end if
		 * 		end if
		 * 			assign "currentStack" as the other end of the tunnel
		 * 			if "cellsUnvisited" contains the "currentCell"
		 * 				remove "currentCell" from "cellsUnvisited"
		 * 			end if
		 * 		end if
		 *
		 * 		clear "cellNeighbours"
		 *
		 * 		for each Cell "c" that is a neighbour of "currentCell"
		 * 			if "c" is unvisited
		 * 				add "c" to "cellNeighbours"
		 * 			end if
		 * 		end for loop
		 *
		 * 		shuffle "cellNeighbours"
		 *
		 * 		if "cellNeighbours" is not empty AND has at least one not null element
		 * 			new Cell "nextCell"
		 * 			for each Cell "c" in "cellNeighbours"
		 * 				if "c" is null
		 * 					move to the next "c"
		 * 				end if
		 * 				assign "nextCell" as "c"
		 * 			end for loop
		 *
		 * 			push "currentCell" onto "cellStack"
		 * 			remove "nextCell" from "cellsUnvisited"
		 *
		 * 			carveWall between "currentCell" and "nextCell"
		 * 			assign "currentCell" as "nextCell"
		 * 			goto top of while
		 * 		end if
		 * 		else if "cellStack" is not empty"
		 * 			pop "cellStack" and assign to "currentCell"
		 * 		end if
		 * 		else
		 * 			if all elements in "cellNeighbours" are null
		 * 				break from while loop and finish
		 * 			end if
		 * 			new Cell "nextCell"
		 * 			for each Cell "c" in "cellsUnvisited"
		 * 				assign "nextCell" as "c"
		 * 				break from for loop
		 * 			end for loop
		 * 			remove "nextCell" from "cellsUnvisited"
		 * 			assign "currentCell" as "nextCell"
		 * 			continue while loop
		 * 		end else
		 * 	end while
		 *
		 */

		while(!cellsUnvisited.isEmpty())
		{

			/**
			 * Deal with tunnels: line 58
			 */
			if(currentCell.tunnelTo != null)
			{
				{
					if(!cellsStack.isEmpty())
						if(!cellsStack.peek().equals(currentCell) && cellsUnvisited.contains(currentCell))
							cellsStack.push(currentCell);

					currentCell = currentCell.tunnelTo;

					if(cellsUnvisited.contains(currentCell))
						cellsUnvisited.remove(currentCell);
				}
			}

			/**
			 * Clear cellNeighbours: line 80
			 */
			cellNeighbours.clear();


			/**
			 * Add all unvisited neighbours of currentCell to cellsUnvisited: line 82
			 */
			for(Cell c : currentCell.neigh) {
				if(cellsUnvisited.contains(c))
					cellNeighbours.add(c);
			}

			/**
			 * Shuffle cellNeighbours: line 88
			 */
			Collections.shuffle(cellNeighbours, new Random(System.nanoTime()));

			/**
			 * If cell neighbours is not empty and any element in cellNeighbours is not null: line 90
			 * function testIfAllNull: line 260
			 */
			if(!cellNeighbours.isEmpty() && !testIfAllNull(cellNeighbours))
			{

				/**
				 * Get next cell to process that has not been visited: line 91
				 */
				Cell nextCell = null;
				for(Cell c : cellNeighbours)
				{
					if(c != null)
					{
						nextCell = c;
						break;
					}
				}

				/**
				 * Push current cell onto the cellsStack and remove nextCell from unvisited: line 99
				 */
				cellsStack.push(currentCell);
				cellsUnvisited.remove(nextCell);

				/**
				 * Carve wall between currentCell and nextCell: line 102
				 * function carveWall: line 290
				 */
				carveWall(currentCell, nextCell);

				/**
				 * Assign currentCell from nextCell and continue from top of while loop: line 103
				 */
				currentCell = nextCell;
				continue;
			}

			/**
			 * If cellStack is not empty: line 97
			 */
			else if(!cellsStack.isEmpty())
			{
				currentCell = cellsStack.pop();
			}

			/**
			 * Otherwise check if all neighbours are null or there is more to process: line 100
			 */
			else
			{
				if(testIfAllNull(cellNeighbours))
					break;

				/**
				 * Get next Cell to process: line 114
				 */
				Cell nextCell = null;
				for(Cell c : cellsUnvisited)
				{
					nextCell = c;
					break;
				}

				/**
				 * Remove nextCell from cellsUnvisited and make nextCell the currentCell: line 118
				 */
				cellsUnvisited.remove(nextCell);
				currentCell = nextCell;
			}
		}




	} // end of generateMaze()

	/**
	 * Method to test if all elements in an ArrayList are null
	 *
	 * Input: ArrayList
	 * Output: boolean: true if all values are null, false if at least one element is not null
	 *
	 * function: testIfAllNull(ArrayList list)
	 * 		for each element "e" in list
	 * 			if "e" is null
	 * 				continue to next element
	 * 			else if "e" is not null
	 * 				return false
	 * 		return true
	 *
	 * @param list
	 * @return
     */
	private boolean testIfAllNull(ArrayList list)
	{
		for(Object o : list)
			if(o != null)
				return false;

		return true;
	}

	/**
	 * Method to remove a wall between 2 adjacent cells
	 *
	 * Input: 2 adjacent cells
	 * Result: Removed wall between the cells
	 *
	 * function carveWall(Cell c1, Cell c2)
	 * 		for each wall "w" in c1
	 * 			if "w" does not exist
	 * 				move to next "w"
	 * 			for each wall "w2" in c2
	 * 				if "w2" does not exist
	 * 					move to next "w2"
	 * 				if "w" is the same as "w2" (meaning they share the same wall)
	 * 					remove the presence of the shared wall (carve path)
	 * 					return
	 *
	 *
	 * @param c1 - Cell 1
	 * @param c2 - Adjacent Cell 2
     */
	private void carveWall(Cell c1, Cell c2)
	{
		for(Wall w : c1.wall)
		{
			if(w == null)
				continue;
			for (Wall w2 : c2.wall) {
				if(w2 == null)
					continue;
				if (w.equals(w2)) {
					w.present = false;
					return;
				}
			}
		}
	}

} // end of class RecursiveBacktrackerGenerator

