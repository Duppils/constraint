import org.jacop.constraints.*;
import org.jacop.core.*;
import org.jacop.search.*;

public class GoodBurger {
    public static void main(String[] args){
    Store store = new Store();
	IntVar beef = new IntVar(store, "beef", 1, 5000);
	IntVar bun = new IntVar(store, "bun", 1, 5000);
	IntVar cheese = new IntVar(store, "cheese", 1, 5000);
	IntVar onions = new IntVar(store, "onions", 1, 5000);
	IntVar pickles = new IntVar(store, "pickles", 1, 5000);
	IntVar lettuce = new IntVar(store, "lettuce", 1, 5000);
	IntVar ketchup = new IntVar(store, "ketchup", 1, 5000);
	IntVar tomato = new IntVar(store, "tomato", 1, 5000);
	IntVar[] burger = new IntVar[]{beef,bun,cheese,onions,pickles,lettuce,ketchup,tomato};
	
	/**
	 * Constraints
	 * Sodium < 3000 (mg)
	 * Fat < 150
	 * Calories < 3000 (cal)
	 */
	store.impose(new LinearInt(burger, new int[] {25, 15, 10, 9, 3, 4, 2, 4}, ">", 2000)); //maximize money, bad solution
	store.impose(new LinearInt(burger, 
				new int[] {50,330,310,1,260,3,160,3}, "<", 3000)); //Sodium constraint
	store.impose(new LinearInt(burger, new int[] {17, 9, 6, 2, 0, 0, 0, 0}, "<", 150));
	store.impose(new LinearInt(burger, new int[] {220, 260, 70, 10, 5, 4, 20, 9}, "<", 3000));
	System.out.println("Number of variables: " + store.size() + "\nNumber of constraints: " 
			+ store.numberConstraints());

	Search<IntVar> search = new DepthFirstSearch<IntVar>();
	SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(burger, null, new IndomainMax<IntVar>());
	search.setSolutionListener(new PrintOutListener<IntVar>());
	
	boolean Result = search.labeling(store, select);

	if (Result) {
		System.out.println("Solution : " + java.util.Arrays.asList(burger));
	}
	}
}
