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
		// calculate row sums
		for(int i = 0; i < n; i++){
			store.impose(new SumInt(lots[i], "sum row" + i, sums[i]));
		}

		// calculate col sums
		for(int i = 0; i < n; i++){
			store.impose(new SumInt(getCol(lots, i), "sum col" + (i+n), sums[i+n]));
		}

		// constraint points to contain the right point calculation for
		// specified number of residential slots in row/col
		// might have to have constraints for entire domain somehow..
		for(int i = 0; i < sums.length; i++){
			store.impose(new XeqC(points[i], point_distribution[sums[i].value()]));
		}

		// total residential lots in all rows and cols MUST be
		// n_residential, one constraint for rows, one for cols
		IntVar n_res = new IntVar(store, n_residential, n_residential);
		store.impose(new SumInt(sums, "n_res", n_res));

		// symmetry breaking: rows and cols can be permuted to any other
		// row/col. Note that a row cannot change place with a col etc.
		// rows and cols can both be permuted


		//solve minimize 
 	}

	IntVar[] getCol(IntVar[][] matrix, int index){
		IntVar[] col = new IntVar[matrix.length];
		for(int row = 0; row < matrix.length; row++){ //only works for sqare matrix
			col[row] = matrix[row][index];
		}
		return col;
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


