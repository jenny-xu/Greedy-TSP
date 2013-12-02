import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class GreedyTSP {
	
	private static int[][] weightedMatrix;
	private static ArrayList<Integer> node1;
	private static ArrayList<Integer> node2;
	private static ArrayList<Integer> selectedNode1;
	private static ArrayList<Integer> selectedNode2;
	
	public static void main(String[] argv)
	{
		final long startTime = System.nanoTime();
		Scanner s = null;
		node1 = new ArrayList<Integer>();
		node2 = new ArrayList<Integer>();
		selectedNode1 = new ArrayList<Integer>();
		selectedNode2 = new ArrayList<Integer>();
		
		try
	    {
			s = new Scanner(new File("P01_matrix.txt"));
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Can't find file.");
	    }
		
		createMatrix(s);
		System.out.println("Pathcost: " + solveTSP(weightedMatrix));
		
		final long endTime = System.nanoTime();
		System.out.println("Runtime: " + (endTime - startTime));
	}
	
	private static void createMatrix(Scanner s)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		while(s.hasNextInt())
		{
			ints.add(s.nextInt());
		}
		
		int listSize = (int)Math.sqrt(ints.size());
		weightedMatrix = new int[listSize][listSize];
		int index = 0;
		for(int i = 0; i < listSize; i++)
		{
			for(int j = 0; j < listSize; j++)
			{
				weightedMatrix[i][j] = ints.get(index);
				index++;
			}
		}
	}
	
	//sorts the edges, doesn't add edges of weight 0.
	private static ArrayList<Integer> sort(int[][] matrix)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 1 + i; j < matrix[0].length; j++)
			{
				if(matrix[i][j] != 0)
				{
					if(ints.isEmpty() || (ints.get(ints.size() - 1) <= matrix[i][j]))
					{
						ints.add(matrix[i][j]);
						node1.add(i);
						node2.add(j);
					}
					else if(ints.size() == 1)
					{
						ints.add(0, matrix[i][j]);
						node1.add(0, i);
						node2.add(0, j);						
					}
					else
					{
						for(int k = ints.size() - 2; k >= 0; k--)
						{
							if(ints.get(k) <= matrix[i][j])
							{
								ints.add(k + 1, matrix[i][j]);
								node1.add(k + 1, i);
								node2.add(k + 1, j);
								break;
							}
							else if(k == 0)
							{
								//System.out.println(matrix[i][j]);
								ints.add(0, matrix[i][j]);
								node1.add(0, i);
								node2.add(0, j);
								break;
							}
						}
					}
				}
			}
		}
		return ints;	
	}
	
	private static boolean hasCycle(int first, int second) 
	{
		boolean checkFirst = false;
		boolean checkSecond = false;
		
//		for(int i = 0; i < selectedNode1.size(); i++)
//		{
//			System.out.print("SelectedNode1: " + (selectedNode1.get(i) + 1));
//			System.out.println(" SelectedNode2: " + (selectedNode2.get(i) + 1));
//		}
		
		for(int i = 0; i < selectedNode1.size(); i++)
		{
			if(first == selectedNode1.get(i) || first == selectedNode2.get(i))
			{
				checkFirst = true;
				for(int j = 0; j < selectedNode2.size(); j++)
				{
					if(second == selectedNode2.get(j) || second == selectedNode1.get(j))
					{
						checkSecond = true;
						break;
					}
				}
				break;
			}
		}
		
		return checkFirst && checkSecond;
	}
	
	public static int solveTSP(int[][] matrix) 
	{
		ArrayList<Integer> sortedList = sort(matrix);
		int[] degree = new int[matrix.length];
		int pathCost = 0;
		int firstNode = 0;
		int secondNode = 0;
		int vertexCount = 0;
//		for(int i = 0; i < sortedList.size(); i++)
//		{
//			System.out.println("Node1: " + (node1.get(i) + 1));
//			System.out.println("Node2: " + (node2.get(i) + 1));
//			System.out.println("Weight: " + sortedList.get(i));
//		}
		
		
		while(!sortedList.isEmpty())
		{
//			System.out.print("Node1: " + (node1.get(0) + 1));
//			System.out.println(" Node2: " + (node2.get(0) + 1));
			if(hasCycle(node1.get(0), node2.get(0)) && vertexCount < matrix.length)
			{
//				System.out.println("Cycle error");
//				System.out.println("Node1: " + (node1.get(0) + 1));
//				System.out.println("Node2: " + (node2.get(0) + 1));
				sortedList.remove(0);
				node1.remove(0);
				node2.remove(0);	
				
			}
			else if((degree[node1.get(0)] >= 2) || (degree[node2.get(0)] >= 2))
			{
//				System.out.println("Degree error");
//				System.out.println("Node1: " + (node1.get(0) + 1) + " degree: " + degree[node1.get(0)]);
//				System.out.println("Node2: " + (node2.get(0) + 1) + " degree: " + degree[node2.get(0)]);
				sortedList.remove(0);
				node1.remove(0);
				node2.remove(0);
			}
			else
			{
//				System.out.println(sortedList.size());
//				System.out.println(vertexCount);
				firstNode = node1.remove(0);
				secondNode = node2.remove(0);
				selectedNode1.add(firstNode);
				selectedNode2.add(secondNode);
				sortedList.remove(0);
				degree[firstNode]++;
				degree[secondNode]++;
			}
			vertexCount++;
		}
		
//		System.out.println("Path Size: " + selectedNode1.size());
		for(int i = 0; i < selectedNode1.size(); i++)
		{
			System.out.print("Edge from " + (selectedNode1.get(i) + 1));
			System.out.println(" to " + (selectedNode2.get(i) + 1));
			pathCost += weightedMatrix[selectedNode1.get(i)][selectedNode2.get(i)];
		}
		return pathCost;
	}
}
