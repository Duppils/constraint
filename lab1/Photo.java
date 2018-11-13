import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Photo {
	private int n;
	private int n_prefs;
	private int[][] prefs;
	private int out1;
	private int out2;

	// Each person has preferences for neighbors
	// Find solution with most preferences satiated
	// Modified problem:
	// Minimize the distances between people on preference list.
	public static void main(String[] args){
		run_ex_max_pairs(1);
	}

	public Photo(int n, int n_prefs, int[][] prefs, int out1, int out2){
		this.n = n;
		this.n_prefs = n_prefs;
		this.prefs = prefs;
		this.out1 = out1;
		this.out2 = out2;
	}

	public void min_dist(){
		Store store = new Store();
		// Integrate the preference pairs into the program
		IntVar[] pairs = new IntVar[n_prefs];
		for(int i = 0; i < n_prefs; i++){
			pairs[i] = new IntVar(store, "pref" + i, prefs[i][0], prefs[i][0]);			
			pairs[i].addDom(new IntervalDomain(prefs[i][1], prefs[i][1]));
		}

		// Create our persons who will parttake in the photo
		// Impose each position in photo can have one person
		IntVar[] persons = new IntVar[n];
		for(int i = 0; i < n; i++){
			persons[i] = new IntVar(store, "" + i, 0, n-1);
		}
		store.impose(new Alldiff(persons));

		// Calculate current distance between persons in preference pairs
		// Neighbors have dist == abs(1), be careful, persons starts at 0,
		// prefs start at 1.
		IntVar[] dist = new IntVar[n_prefs]; 
		for(int i = 0; i < n_prefs; i++){
			dist[i] = new IntVar(store, "dist" + i, 1, n-1);
			store.impose(new Distance(persons[prefs[i][0] - 1], persons[prefs[i][1] - 1], dist[i]));
		}

		// Creates a constraint which defines a neighbor and imposes it on the
		// distance list.  
		PrimitiveConstraint c;
		IntVar[] neighbors = new IntVar[n_prefs];
		for(int i = 0; i < n_prefs; i++){
			neighbors[i] = new IntVar(store, "neighbor" + i, 0, 1);
			c = new XeqC(dist[i], 1);
			store.impose(new Reified(c, neighbors[i]));
		}

		// Defines sum as the number of fullfilled preferences
		IntVar sum = new IntVar(store, "sum", 0, Integer.MAX_VALUE);
		store.impose(new SumInt(neighbors, "==", sum));
		IntVar negatedSum = new IntVar(store, "negatedSum", n_prefs * -1, 0);
		IntVar[] minimize = new IntVar[2];
		minimize[0] = sum;
		minimize[1] = negatedSum;
		IntVar zero = new IntVar(store, "zero", 0, 0);
		PrimitiveConstraint pc = new SumInt(minimize, "==", zero);
		pc.impose(store);

		//System.out.println("Number of variables: " + store.size() + 
		//		"\nNumber of constraints: " + store.numberConstraints());
	
		// Specify select to optimize for preferences fullfilled
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(persons, null , new IndomainMin<IntVar>());

		boolean Result = search.labeling(store, select, negatedSum);
		System.out.println("Solution: " + java.util.Arrays.asList(persons));
	}

	public void max_pairs(){
		Store store = new Store();
		// Integrate the preference pairs into the program
		IntVar[] pairs = new IntVar[n_prefs];
		for(int i = 0; i < n_prefs; i++){
			pairs[i] = new IntVar(store, "pref" + i, prefs[i][0], prefs[i][0]);			
			pairs[i].addDom(new IntervalDomain(prefs[i][1], prefs[i][1]));
		}

		// Create our persons who will parttake in the photo
		// Impose each position in photo can have one person
		IntVar[] persons = new IntVar[n];
		for(int i = 0; i < n; i++){
			persons[i] = new IntVar(store, "" + i, 0, n-1);
		}
		store.impose(new Alldiff(persons));

		// Calculate current distance between persons in preference pairs
		// Neighbors have dist == abs(1), be careful, persons starts at 0,
		// prefs start at 1.
		IntVar[] dist = new IntVar[n_prefs]; 
		for(int i = 0; i < n_prefs; i++){
			dist[i] = new IntVar(store, "dist" + i, 1, n-1);
			store.impose(new Distance(persons[prefs[i][0] - 1], persons[prefs[i][1] - 1], dist[i]));
		}

		// Creates a constraint which defines a neighbor and imposes it on the
		// distance list.  
		PrimitiveConstraint c;
		IntVar[] neighbors = new IntVar[n_prefs];
		for(int i = 0; i < n_prefs; i++){
			neighbors[i] = new IntVar(store, "neighbor" + i, 0, 1);
			c = new XeqC(dist[i], 1);
			store.impose(new Reified(c, neighbors[i]));
		}

		// Defines sum as the number of fullfilled preferences
		IntVar sum = new IntVar(store, "sum", 0, Integer.MAX_VALUE);
		store.impose(new SumInt(neighbors, "==", sum));
		IntVar negatedSum = new IntVar(store, "negatedSum", n_prefs * -1, 0);
		IntVar[] minimize = new IntVar[2];
		minimize[0] = sum;
		minimize[1] = negatedSum;
		IntVar zero = new IntVar(store, "zero", 0, 0);
		PrimitiveConstraint pc = new SumInt(minimize, "==", zero);
		pc.impose(store);

		//System.out.println("Number of variables: " + store.size() + 
		//		"\nNumber of constraints: " + store.numberConstraints());
	
		// Specify select to optimize for preferences fullfilled
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(persons, null , new IndomainMin<IntVar>());

		boolean Result = search.labeling(store, select, negatedSum);
		System.out.println("Solution: " + java.util.Arrays.asList(persons));
	}
	
	/* Loads the input variables for a given example from the lab.
	 * n: number of persons
	 * n_prefs: number of preference pairs
	 * prefs: 2 * n_prefs matrix with preference pairs
	 */
	private static Photo load_ex(int i){
		switch(i){
			case 1:
				int n = 9;
				int n_prefs = 17;
				int[][] prefs = {{1,3}, {1,5}, {1,8}, {2,5}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,5}, {5,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,8}, {8,9}, {8,7}};
				int out1 = 10;
				int out2 = 4;
				return new Photo(n, n_prefs, prefs, out1, out2);
			case 2:
				n = 11;
				n_prefs = 20;
				int[][] prefs2 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,5}, {4,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,5}, 
					{8,9}, {8,7}, {8,10}, {9,11}, {10,11}};
				out1 = 11;
				out2 = 4;
				return new Photo(n, n_prefs, prefs2, out1, out2);
			case 3:
				n = 15;
				n_prefs = 20;
				int[][] prefs3 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,15}, {4,13}, {5,1}, {6,10}, {6,9}, {7,3}, {7,5}, 
					{8,9}, {8,7}, {8,14}, {9,13}, {10,11}};
				out1 = 13;
				out2 = 3;
				return  new Photo(n, n_prefs, prefs3, out1, out2);
			default:
				System.out.println("Example not found!");
				return null;
		}
	}

	private static void run_ex_max_pairs(int i){	
		Photo p;
		long start, time;
		p = load_ex(i);
		start = System.currentTimeMillis();
		p.max_pairs();
		time = System.currentTimeMillis() - start;
		System.out.println("Time to solve ex " + i + ":" + time + " ms");
	}

	private static void run_ex_min_dist(int i){
		Photo p;
		long start, time;
		p = load_ex(i);
		start = System.currentTimeMillis();
		p.min_dist();
		time = System.currentTimeMillis() - start;
		System.out.println("Time to solve ex " + i + ":" + time + " ms");
	}
}
