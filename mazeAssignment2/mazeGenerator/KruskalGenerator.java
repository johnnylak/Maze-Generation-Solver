package mazeGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import maze.Cell;
import maze.Maze;
import maze.Wall;

public class KruskalGenerator implements MazeGenerator {

	/*
	* ******************************************************************************************
    * 
    * Kruskal¡¯s
    * 1: for every Cell
    * 2: 	// Maintain a set of trees
    * 3: 	new Tree
    * 4:	// Construct an edge
    * 5:	new Edge
    * 6: end for
    * 7:
    * 8:  while (setOfTrees.size != 1) 
    * 9:	Select random edge
    * 9:	If edge joins two disjoint trees, join trees and break wall, else discard
    * 10: end while
    * 
    * ******************************************************************************************
    */
	@Override
	public void generateMaze(Maze maze) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Tree> setOfTrees = new ArrayList<Tree>();
		
		switch (maze.type)
		{
			case 0:
				// Normal maze
				for (int row = 0; row < maze.sizeR; row++) {
					for (int column = 0; column < maze.sizeC; column++) {
						// Maintain a set of trees
						Tree tree = new Tree(maze.map[row][column]);
		
						setOfTrees.add(tree);
		
						// Set of all edges
						for (Cell c : maze.map[row][column].neigh) {
							if (c != null) {
								Edge edge = new Edge(maze.map[row][column], c);
								edges.add(edge);				
							}
						}
					}
				}
				break;
			
			case 1:
				// Tunnel maze
				for (int row = 0; row < maze.sizeR; row++) {
					for (int column = 0; column < maze.sizeC; column++) {
						// Maintain a set of trees
						Tree tree = new Tree(maze.map[row][column]);
		
						setOfTrees.add(tree);
		
						// Set of all edges
						for (Cell c : maze.map[row][column].neigh) {
							if (c != null) {
								Edge edge = new Edge(maze.map[row][column], c);
								edges.add(edge);				
							}
						}
						
						if (maze.map[row][column].tunnelTo != null) {
							Edge edge = new Edge(maze.map[row][column], maze.map[row][column].tunnelTo);
							edges.add(edge);
						}
					}
				}
				break;
				
			case 2: 
				// Hex maze
				for (int row = 0; row < maze.sizeR; row++) {
					for (int column = 0; column < maze.sizeC; column++) {
						int cc = column + (row + 1) / 2;
						
						// Maintain a set of trees
						Tree tree = new Tree(maze.map[row][cc]);
		
						setOfTrees.add(tree);
						
						// Set of all edges
						for (Cell c : maze.map[row][cc].neigh) {
							if (c != null) {
								Edge edge = new Edge(maze.map[row][cc], c);
								edges.add(edge);				
							}
						}
					}
				}

				break;
		}

		Random rand = new Random();
		int edge = rand.nextInt(edges.size()) + 0;
		
		while (setOfTrees.size() != 1) {
			// Select a random edge
			edge = rand.nextInt(edges.size()) + 0;
			Edge e = edges.get(edge);
			
			// Get the two adjacent cells from the edge
			Cell cell1 = e.cell1;
			Cell cell2 = e.cell2;
			
			// Handle tunnels
			if (maze.type == 1) {
				if (cell1.tunnelTo != null) {
					cell2 = cell1.tunnelTo;
				}					
			}
			
			// If the edge joins two disjoint trees in the tree set, join the trees. If not, discard the edge.
			Tree tree1 = getTree(setOfTrees, cell1);
			Tree tree2 = getTree(setOfTrees, cell2);
			
			if (tree2.cells.size() > tree1.cells.size()) {
				if (tree1 != tree2) {
					tree2.cells.addAll(tree1.cells);
					setOfTrees.remove(tree1);
					
					outerloop:
					for (Wall w1 : cell1.wall) {
						for (Wall w2 : cell2.wall) {
							if (w1 != null && w2 != null && w1 == w2) {
								w1.present = false;
								w2.present = false;
								
								break outerloop;
							}
						}
					}
					
					edges.remove(e);					
				}
			}
			else {
				if (tree1 != tree2) {
					tree1.cells.addAll(tree2.cells);
					setOfTrees.remove(tree2);
					
					outerloop:
					for (Wall w1 : cell1.wall) {
						for (Wall w2 : cell2.wall) {
							if (w1 != null && w2 != null && w1 == w2) {
								w1.present = false;
								w2.present = false;
								
								break outerloop;
							}
						}
					}
					
					edges.remove(e);
				}
			}
		}
	} // end of generateMaze()
	
	public class Tree {
		Set<Cell> cells;
		
		public Tree(Cell cell) {
			cells = new HashSet<Cell>();
			cells.add(cell);
		}
		
		public boolean treeContains(Cell cell) {
			if (cells.contains(cell))
				return true;
			
			return false;
		}
	}
	
	public Tree getTree(ArrayList<Tree> tree, Cell cell) {
		for (Tree t : tree) {
			if (t.treeContains(cell)) {
				return t;
			}
		}
		
		return null;
	}
	
	public class Edge {
		Cell cell1;
		Cell cell2;
		
		public Edge (Cell first, Cell second) {
			cell1 = first;
			cell2 = second;
		}
	}
} // end of class KruskalGenerator
