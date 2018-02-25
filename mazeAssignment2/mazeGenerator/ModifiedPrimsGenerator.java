package mazeGenerator;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.*;

public class ModifiedPrimsGenerator implements MazeGenerator {

//	HashMap of Cells visited
	private HashMap<Cell, Integer> Z = new HashMap<>();
//	Frontier Array
	private ArrayList<Cell> F = new ArrayList<>();
//	c's Neighbours for processing
	private ArrayList<Cell> cNeighbours = new ArrayList<>();


	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub

		/**
		 * Procedure followed:
		 * 1. Pick a random starting cell and add it to
		 * 	Z (initially Z is empty, after addition it contains jus the starting cell).
		 * 	Put all neighbouring cells of starting cell into frontier set F
		 *
		 * 	2. Randomly select a cell c from the frontier set and remove it from F.
		 * 		Randomly select a cell b that is in Z and adjacent to the cell c.
		 * 		Carve a path between c and b.
		 *
		 * 	3. Add c to Z. Also check F and make sure that F and Z do not share any Cells.
		 * 		If they do, remove the common Cell from the Frontier.
		 *
		 * 	4. Repeat step 2 until Z includes ever cell in the maze.
		 *
		 */


		/**
		 * Select a random Cell "currentCell" from maze
		 */
//		Select a random cell
		int randomX = new Random().nextInt(maze.sizeC);
		int randomY = new Random().nextInt(maze.sizeR);

		Cell currentCell = maze.map[randomY][randomX];

		/**
		 * If "currentCell" is null
		 *
		 * while "currentCell" is null
		 * 		assign "randomX" from random "maze" sizeC
		 * 		assign "randomY" from random "maze" sizeR
		 *
		 * 		assign "currentCell" from "maze" using "randomX" and "randomY"
		 * 	end while
		 */
		while(currentCell == null)
		{
			randomX = new Random().nextInt(maze.sizeC);
			randomY = new Random().nextInt(maze.sizeR);

			currentCell = maze.map[randomY][randomX];
		}

		/**
		 * For each Cell "n" in "currentCell" neighbours
		 * 		if "n" is null
		 * 			move to next "n"
		 * 		end if
		 * 		add "n" to "F"
		 */
//		Add the current Cells neighbours to F (Frontier)
		for(Cell n : currentCell.neigh)
		{
			if(n == null)
				continue;

			F.add(n);
		}

//		Add the current cell to Z
		Z.put(currentCell, 0);

		/**
		 * new HashmMap "Z" contains cells visited
		 * new HashMap "F" is the collection of frontiers
		 *
		 * While Z does not contain all of the Cells in the maze, do steps 2 and 3 above.
		 *
		 * 	while size of Z does not equal size of maze
		 * 		shuffle F
		 *
		 * 		new Cell "c"
		 * 		for each Cell "randomFrontier" in "F"
		 * 			if "randomFrontier" is null
		 * 				go to next "randomFrontier"
		 * 			end if
		 * 			assign "c" from "randomFrontier"
		 * 			break from for loop
		 * 		end for loop
		 *
		 * 		new Cell "b"
		 * 		clear "cNeighbours"
		 *
		 * 		for each "cNeighbour" in "c" neighbours
		 * 			if "cNeighbour" is null
		 * 				move to next "cNeighbour"
		 * 			end if
		 * 			add "cNeighbour" to "cNeighbours"
		 * 		end for loop
		 *
		 * 		shuffle "cNeighbours"
		 *
		 * 		new Cell "randomCNeighbours"
		 * 		for each Cell "randCN" in "cNeighbours"
		 * 			if "randCN" is null
		 * 				move to next "randCN"
		 * 			end if
		 * 			assign "randomCNeighbour" from "randCN"
		 * 			break for loop
		 * 		end for loop
		 *
		 * 		if "Z" contains "randomCNeighbour"
		 * 			assign "b" from randomCNeighbour"
		 * 		end if
		 * 		else
		 * 			go to top of while loop
		 * 		end else
		 *
		 * 		carveWall between "c" and "b"
		 * 		add "c" to "Z"
		 *
		 * 		for each Cell "f" in "F"
		 * 			if "f" is null
		 * 				move to next "f"
		 * 			end if
		 * 			if "Z" contains "f"
		 * 				remove "f" from "F"
		 * 			end if
		 * 		end for loop
		 *
		 * 		for each Cel "cFrontier" in "c" neighbours
		 * 			if "cFrontier" is null
		 * 				move to next "cFrontier"
		 * 			end if
		 * 			if "Z" contains "cFrontier"
		 * 				move to next "cFrontier"
		 * 			end if
		 * 			if "F" contains "cFrontier"
		 * 				move to next "cFrontier"
		 * 			end if
		 * 			add "cFrontier" to "F"
		 * 		end for loop
		 *
		 */
		while(Z.size() != maze.sizeR * maze.sizeC )
		{

			/**
			 *Shuffle the F (Frontier) array : line 94
			 */
			Collections.shuffle(F, new Random(System.nanoTime()));

			/**
			 * Initialise the Cell c: line 96
			 */
			Cell c = null;

			/**
			 * Get the first value that is not null in the shuffled Frontier and assign it to c: line 97
			 */
			for(Cell randomFrontier : F)
			{
				if(randomFrontier == null)
					continue;

				c = randomFrontier;
				break;
			}

			/**
			 * Select a cell b that is in Z and adjacent to c: line 108
			 */

//			Initialise b
			Cell b = null;


			/**
			 * Clear the cNeighbours array: line 106
			 */
			cNeighbours.clear();

			/**
			 * Get all the neighbours of c and add them to cNeighbours: line 108
			 */
			for(Cell cNeighbour : c.neigh)
			{
				if(cNeighbour == null)
					continue;

				cNeighbours.add(cNeighbour);
			}

			/**
			 * Shuffle cNeighbours Array: line 114
			 */
			Collections.shuffle(cNeighbours, new Random(System.nanoTime()));

			/**
			 * Initialise a random c neighbour to explore: line 115
			 */
			Cell randomCNeighbour = null;

			/**
			 * Get the first random c neighbour that is not null from cNeighbour Array: line 117
			 */
			for(Cell randCN : cNeighbours)
			{
				if(randCN == null)
					continue;

				randomCNeighbour = randCN;
				break;
			}

			/**
			 * If the randomly selected c neighbour is already in Z, goto the top of while loop and start again
			 * Otherwise assign the Cell b : line 126
			 */
			if(Z.containsKey(randomCNeighbour))
			{
				b = randomCNeighbour;
			}

			else
				continue;

			/**
			 * Remove wall between b and c: line 133
			 * function carveWall: line 312
			 */
			carveWall(c, b);


			/**
			 * Add c to Z: line 134
			 */
			Z.put(c, 0);

			/**
			 * Clean up the F array removing any elements that have already been visited (i.e. in Z): line 136
			 */
			for(int i = 0; i < F.size(); i++)
			{
				Cell f = F.get(i);

				if(f == null)
					continue;

				if(Z.containsKey(f))
					F.remove(f);
			}

			/**
			 * Add the neighbours of c to F. If a neighbour of c is already in Z, ignore: line 145
			 */
			for(Cell cFrontier : c.neigh)
			{
				if(cFrontier == null)
					continue;

				if(Z.containsKey(cFrontier))
					continue;

				if(F.contains(cFrontier))
					continue;

				F.add(cFrontier);
			}


		}//end while loop


	} // end of generateMaze()

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



} // end of class ModifiedPrimsGenerator
