import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Photo {
	private int n;
	private int n_prefs;
	private int[][] prefs;

	public static void main(String[] args){
		// Group of n people
		// Each person has preferences for neighbors
		// Find optimal placement of people
		// Modified problem:
		// Minimize the distances between people on prefernce list.
		// i.e. minimize the maximal distance between two persons from the
		// preference list. Compare solutions.
		int out1 = 10;
		int out2 = 4;
		load_ex(1);
		
		out1 = 11;
		out2 = 4;
		
		out1 = 13;
		out2 = 3;
	}

	public Photo(int n, int n_prefs, int[][] prefs){
		this.n = n;
		this.n_prefs = n_prefs;
		this.prefs = prefs;
	}

	public void solve(){
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
		IntVar sum = new IntVar(store, "sum", 0, Integer.MAX_VALUE);
		for(int i = 0; i < n; i++){
			persons[i] = new IntVar(store, "" + i, 0, n-1);
		}
		store.impose(new Alldiff(persons));

		// Calculate current distance between persons in preference pairs
		// Neighbors have dist == abs(1)
		IntVar[] dist = new IntVar[n_prefs]; 
		for(int i = 0; i < n_prefs; i++){
			store.impose(new Distance(persons[prefs[i][0]], persons[prefs[i][1]], dist[i]));
			//store.impose(new Distance(pairs[i][0], pairs[i][1], dist[i]);
		}

		// Creates a constraint which defines a neighbor and imposes it on the
		// distance list.  
		PrimitiveConstraint c;
		IntVar[] neighbors = new IntVar[n_prefs];
		for(int i = 0; i < n_prefs; i++){
			c = new XeqC(dist[i], 1);
			store.impose(new Reified(c, neighbors[i]));
		}

		// Defines sum as the number of fullfilled preferences
		store.impose(new SumInt(neighbors, "==", sum));

		System.out.println("Number of variables: " + store.size() + 
				"\nNumber of constraints: " + store.numberConstraints());
		
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(persons, null, new IndomainMin<IntVar>());

		//boolean result = search.labeling(store, select);
		System.out.println("Solution: " + java.util.Arrays.asList(persons));
	}

	private static void load_ex(int i){
		switch(i){
			case 1:
				int n = 9;
				int n_prefs = 17;
				int[][] prefs = {{1,3}, {1,5}, {1,8}, {2,5}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,5}, {5,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,8}, {8,9}, {8,7}};
				Photo p = new Photo(n, n_prefs, prefs);
				p.solve();
				break;

			case 2:
				n = 11;
				n_prefs = 20;
				int[][] prefs2 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,5}, {4,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,5}, 
					{8,9}, {8,7}, {8,10}, {9,11}, {10,11}};
				p = new Photo(n, n_prefs, prefs2);
				p.solve();
				break;
			case 3:
				n = 15;
				n_prefs = 20;
				int[][] prefs3 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
					{4,1}, {4,15}, {4,13}, {5,1}, {6,10}, {6,9}, {7,3}, {7,5}, 
					{8,9}, {8,7}, {8,14}, {9,13}, {10,11}};
				p = new Photo(n, n_prefs, prefs3);
				p.solve();
				break;
			default:
				System.out.println("Example not found!");
				break;
		}
	}
}
