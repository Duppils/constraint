import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Photo {
	//private int n;
	//private int n_prefs;
	//private int[][] prefs;

	public static void main(String[] args){
		//TODO
		Store store = new Store();
		// Group of n people
		// Each person has preferences for neighbors
		// Find optimal placement of people
		//
		// Modified problem:
		// Minimize the distances between people on prefernce list.
		// i.e. minimize the maximal distance between two persons from the
		// preference list. Compare solutions.
		
		//Input set 1
		int n = 9;
		int n_prefs = 17;
		int[][] prefs = {{1,3}, {1,5}, {1,8}, {2,5}, {2,9}, {3,4}, {3,5}, 
			{4,1}, {4,5}, {5,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,8}, {8,9}, {8,7}};
		int out1 = 10;
		int out2 = 4;
		
		//Input set 2
		n = 11;
		n_prefs = 20;
		int[][] prefs2 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
			{4,1}, {4,5}, {4,6}, {5,1}, {6,1}, {6,9}, {7,3}, {7,5}, {8,9}, {8,7}, {8,10}, {9,11}, {10,11}};
		out1 = 11;
		out2 = 4;

		//Input set 3
		n = 15;
		n_prefs = 20;
		int[][] prefs3 = {{1,3}, {1,5}, {2,5}, {2,8}, {2,9}, {3,4}, {3,5}, 
			{4,1}, {4,15}, {4,13}, {5,1}, {6,10}, {6,9}, {7,3}, {7,5}, {8,9}, {8,7}, {8,14}, {9,13}, {10,11}};
		out1 = 13;
		out2 = 3;
	}

	/*public Photo(int n, int n_prefs, int[][] prefs){
	*	this.n = n;
	*	this.n_prefs = n_prefs;
	*	this.prefs = prefs;
	*}
	*/

	public int solve(){
		//TODO
		return 0;
	}
}
