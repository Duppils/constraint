import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class Urban {
	static int n;
	static int n_commercial;
	static int n_residential;
	int[] point_distribution;
	Store store;
	IntVar[][] lots;
	IntVar[] points;

	public static void main(String[] args){
		//load_ex(args);
	}

	public Urban(int n, int n_commercial, int n_residential, int[] point_distribution){
		this.n = n;
		this.n_commercial = n_commercial;
		this.n_residential = n_residential;
		this.point_distribution = point_distribution;
		store = new Store();
		lots = new IntVar[n][n];
		points = new IntVar[n*2];
	}
	
	public void solve(){
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				lots[i][j] = new IntVar(store, 0, 1);
			}
		}

		IntVar[] sums = new IntVar[2*n];
		// calculate row and col sums, rows are followed by cols
		for(int i = 0; i < n; i++){
			store.impose(new SumInt(lots[i], "sum row" + i, sums[i]));
			store.impose(new SumInt(lots[i+n], "sum row" + (i+n), sums[i+n]));
		}

		// constraint points to contain the right point calculation for
		// specified number of residential slots in row/col
		// might have to have constraints for entire domain somehow..
		for(int i = 0; i < sums.length; i++){
			store.impose(new XeqC(points[i], point_distribution[sums[i].value()]));
		}

		// total residential lots in all rows and cols MUST be
		// n_residential, one constraint for rows, one for cols


		// symmetry breaking: rows and cols can be permuted to any other
		// row/col. Note that a row cannot change place with a col etc.
		// rows and cols can both be permuted
 	}

	public void ex_1(){
		int n = 5;
		int n_commercial = 13;
		int n_residential = 12;
		int[] point_distribution = {-5, -4, -3, 3, 4, 5};
		Urban urban = new Urban(n, n_commercial, n_residential, point_distribution);
		urban.solve();
	}
}


