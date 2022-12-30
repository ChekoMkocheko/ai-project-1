
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;

/*
 * Solves the 8-Puzzle Game (can be generalized to n-Puzzle)
 */

public class EightPlayer {

	static Scanner scan = new Scanner(System.in);
	static int size=3; //size=3 for 8-Puzzle. 
	static int numnodes; //number of nodes generated
	static int nummoves; //number of moves required to reach goal
	
	
	public static void main(String[] args)
	{	
		int numsolutions = 0;
		
		int boardchoice = getBoardChoice();
		int algchoice = getAlgChoice();
			
		//determine numiterations based on user's choices
		int numiterations=0;
		
		if(boardchoice==0)
			numiterations = 1;
		else {
		
			switch (algchoice){
			case 0: 
				numiterations = 100;//BFS
				break;
			case 1: 
				numiterations = 1000;//A* with Manhattan Distance heuristic
				break;
			case 2: 
				numiterations = 1000;//A* with your new heuristic
				break;
			}
		}
		
	
		
		Node initNode;
		
		for(int i=0; i<numiterations; i++){
		
			if(boardchoice==0)
				initNode = getUserBoard();
			else
				initNode = generateInitialState();//create the random board for a new puzzle
			
			boolean result=false; //whether the algorithm returns a solution
			
			switch (algchoice){
				case 0: 
					result = runBFS(initNode); //BFS
					break;
				case 1: 
					result = runAStar(initNode, 0); //A* with Manhattan Distance heuristic
					break;
				case 2: 
					result = runAStar(initNode, 1); //A* with your new heuristic
					break;
			}
			
			
			//if the search returns a solution
			if(result){
				
				numsolutions++;
				
				
				System.out.println("Number of nodes generated to solve: " + numnodes);
				System.out.println("Number of moves to solve: " + nummoves);			
				System.out.println("Number of solutions so far: " + numsolutions);
				System.out.println("_______");		
				
			}
			else
				System.out.print(".");
			
		}//for

		
		
		System.out.println();
		System.out.println("Number of iterations: " +numiterations);
		
		if(numsolutions > 0){
			System.out.println("Average number of moves for "+numsolutions+" solutions: "+nummoves/numsolutions);
			System.out.println("Average number of nodes generated for "+numsolutions+" solutions: "+numnodes/numsolutions);
		}
		else
			System.out.println("No solutions in "+numiterations+"iterations.");
		
	}
	
	
	public static int getBoardChoice()
	{
		
		System.out.println("single(0) or multiple boards(1)");
		int choice = Integer.parseInt(scan.nextLine());
		
		return choice;
	}
	
	public static int getAlgChoice()
	{
		
		System.out.println("BFS(0) or A* Manhattan Distance(1) or A* Linear Conflict (2)");
		int choice = Integer.parseInt(scan.nextLine());
		
		return choice;
	}

	
	public static Node getUserBoard()
	{
		
		System.out.println("Enter board: ex. 012345678");
		String stbd = scan.nextLine();
		
		int[][] board = new int[size][size];
		
		int k=0;
		
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(stbd.charAt(k));
				board[i][j]= Integer.parseInt(stbd.substring(k, k+1));
				k++;
			}
		}
		
		
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				//System.out.println(board[i][j]);
			}
			//System.out.println();
		}
		
		
		Node newNode = new Node(null,0, board);

		return newNode;
		
		
	}

    
	
	
	/**
	 * Generates a new Node with the initial board
	 */
	public static Node generateInitialState()
	{
		int[][] board = getNewBoard();
		
		Node newNode = new Node(null,0, board);

		return newNode;
	}
	
	
	/**
	 * Creates a randomly filled board with numbers from 0 to 8. 
	 * The '0' represents the empty tile.
	 */
	public static int[][] getNewBoard()
	{
		
		int[][] brd = new int[size][size];
		Random gen = new Random();
		int[] generated = new int[size*size];
		for(int i=0; i<generated.length; i++)
			generated[i] = -1;
		
		int count = 0;
		
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				int num = gen.nextInt(size*size);
				
				while(contains(generated, num)){
					num = gen.nextInt(size*size);
				}
				
				generated[count] = num;
				count++;
				brd[i][j] = num;
			}
		}
		
		/*
		//Case 1: 12 moves
		brd[0][0] = 1;
		brd[0][1] = 3;
		brd[0][2] = 8;
		
		brd[1][0] = 7;
		brd[1][1] = 4;
		brd[1][2] = 2;
		
		brd[2][0] = 0;
		brd[2][1] = 6;
		brd[2][2] = 5;
		*/
		
		return brd;
		
	}
	
	/**
	 * Helper method for getNewBoard()
	 */
	public static boolean contains(int[] array, int x)
	{ 
		int i=0;
		while(i < array.length){
			if(array[i]==x)
				return true;
			i++;
		}
		return false;
	}
	
	
	/**
	 * TO DO:
     * Prints out all the steps of the puzzle solution and sets the number of moves used to solve this board.
     */
    public static void printSolution(Node node) {
    	
		ArrayList<Node> solnlist = new ArrayList<Node>();
		Node currnode = node;
		
    	while(currnode.getparent()!=null){ // retraces solution until the root node is reached
			solnlist.add(currnode);
			currnode = currnode.getparent();
			nummoves++;
		}
		solnlist.add(currnode); // adds the root node to solnlist 

		for(int i=solnlist.size()-1; i>=0; i--){ // reverses solnlist (starts with root node and goes to goal)
			solnlist.get(i).print();
		}
			
    }
	
	
	
	
	/**
	 * Runs Breadth First Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 */
	public static boolean runBFS(Node initNode)
	{
		// return true if the board is already solved
		if (initNode.isGoal()){
			return true;
		}

		Queue<Node> Frontier = new LinkedList<Node>();
		ArrayList<Node> Explored = new ArrayList<Node>();
		
		Frontier.add(initNode); 
		int maxDepth = 13;

		while(!Frontier.isEmpty()){
			Node cur_state = Frontier.remove();
			Explored.add(cur_state);
			if(cur_state.isGoal()){ // solved puzzle 
				printSolution(cur_state);
				return true;
			}
			else{
				ArrayList<int[][]> nodeslist = cur_state.expand();
				if(cur_state.getdepth()+1 >= maxDepth){ // expanding nodes will reach max depth so function should terminate 
					return false;
				}
				for(int i=0; i<nodeslist.size(); i++){
					Node successor = new Node(cur_state, cur_state.getdepth()+1, nodeslist.get(i));
					if(!Frontier.contains(successor) & !Explored.contains(successor)){ // node has never been looked at before
						Frontier.add(successor);
						numnodes++;
					}
				}
			}
		}
		return false; 
	}
	
	
	
	/***************************A* Code Starts Here ***************************/
	
	/**
	 * Runs A* Search to find the goal state.
	 * Return true if a solution is found; otherwise returns false.
	 * heuristic = 0 for Manhattan Distance, heuristic = 1 for your new heuristic
	 */
	public static boolean runAStar(Node initNode, int heuristic)
	{
		// return true if the board is already solved
		
		if (initNode.isGoal()){
			return true;
		}
	NodeComparator comparator = new NodeComparator(); // used to order PriorityQueue
		PriorityQueue<Node> Frontier = new PriorityQueue<Node>(comparator);
		ArrayList<Node> Explored = new ArrayList<Node>();
		initNode.setgvalue(0);
		
		if(heuristic == 0){ // Manhattan Distance
			initNode.sethvalue(initNode.evaluateHeuristic());
		}
		else if(heuristic == 1){ // Manhattan Distance + Linear Conflict 
			initNode.sethvalue(initNode.evaluateHeuristic() + initNode.linearConflict());
		}
		else{
			System.out.println("invalid heuristic choice");
		}
		
		Frontier.add(initNode); 
		int maxDepth = 13;

		while(!Frontier.isEmpty()){
			Node x = Frontier.remove();
			Explored.add(x);
			if(x.isGoal()){ // found solution
				printSolution(x);
				return true;
			}
			else{
				if(x.getdepth()+1 >= maxDepth){ // terminate if the expanded nodes lead to a depth more than 13
					return false;
				}
				ArrayList<int[][]> nodeslist = x.expand();
				for(int i=0; i<nodeslist.size(); i++){ // for each child of x 
					Node successor = new Node(x, x.getgvalue() + 1, 0, nodeslist.get(i));
					successor.setdepth(x.getdepth() + 1); // update the depth of the child
					if(heuristic==0){ // Manhattan Distance
						successor.sethvalue(successor.evaluateHeuristic());
					}
					else{ // Manhattan Distance + Linear conflict
						successor.sethvalue(successor.evaluateHeuristic() + successor.linearConflict());
					}
					
					if(!Explored.contains(successor)){
						if(!Frontier.contains(successor)){
							Frontier.add(successor);
							numnodes++;
						}
						else{ // Frontier contains node with the same board
							boolean removed = false; 
							Node tempNode = null; 
							for(Node node : Frontier){ // checks every node in Frontier 
								if(node.equals(successor)){ // node has same board as successor
									if(heuristic==0){ // Manhattan Distance
										if((node.getgvalue() + node.gethvalue()) > (successor.getgvalue() + successor.gethvalue())){ // succesor has more efficient f of n
											tempNode = node; 
											removed = true; // flag that we need to remove old node and replace with successor ie update f of n
										}
									}
									else{ // Manhattan Distance + Linear Conflict
										if((node.getgvalue() + node.gethvalue() + node.linearConflict()) > (successor.getgvalue() + successor.gethvalue()+ successor.linearConflict())){ // succesor has more efficient f of n
											tempNode = node;
											removed = true;  // flag that we need to remove old node and replace with successor ie update f of n 
										}
									}
								}
							}
							
							if(removed){ // if we have flagged the Frontier for removal of a node
								Frontier.remove(tempNode); // remove old node with same board as successor
								Frontier.add(successor); // replace old node with successor which has better f of n but same board
							}
						}
					}
				}
         	}    
		return false;
	}
}

/*
*Comparator class for PriorityQueue for Frontier
*/
class NodeComparator implements Comparator<Node>{
	@Override
	public int compare(Node n1, Node n2){

		if((n1.gethvalue() + n1.getgvalue()) < (n2.gethvalue() + n2.getgvalue())){ // n1 has better f of n
			return -1;
		}
		else if((n1.gethvalue() + n1.getgvalue()) > (n2.gethvalue() + n2.getgvalue())){ // n2 has better f of n
			return 1;
		}
		return 0; // the two nodes have equal f of n value
	}
}
