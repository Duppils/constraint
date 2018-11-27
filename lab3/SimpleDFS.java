/**
 *  SimpleDFS.java 
 *  This file is part of JaCoP.
 *
 *  JaCoP is a Java Constraint Programming solver. 
 *	
 *	Copyright (C) 2000-2008 Krzysztof Kuchcinski and Radoslaw Szymanek
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  Notwithstanding any other provision of this License, the copyright
 *  owners of this work supplement the terms of this License with terms
 *  prohibiting misrepresentation of the origin of this work and requiring
 *  that modified versions of this work be marked in reasonable ways as
 *  different from the original version. This supplement of the license
 *  terms is in accordance with Section 7 of GNU Affero General Public
 *  License version 3.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import org.jacop.constraints.Not;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.XeqC;
import org.jacop.constraints.XlteqC;
import org.jacop.constraints.XgteqC;
import org.jacop.core.FailException;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

/**
 * Implements Simple Depth First Search .
 * 
 * @author Krzysztof Kuchcinski
 * @version 4.1
 */

public class SimpleDFS  {
	static final int INPUT_ORDER = 0;
	static final int MIN_DOMAIN = 1;
	static final int MAX_DOMAIN = 2;
	int selection = MIN_DOMAIN;

	boolean trace = false;

	/**
	 * Store used in search
	 */
	Store store;

	/**
	 * Defines varibales to be printed when solution is found
	 */
	IntVar[] variablesToReport;

	/**
	 * It represents current depth of store used in search.
	 */
	int depth = 0;

	/**
	 * It represents the cost value of currently best solution for FloatVar cost.
	 */
	public int costValue = IntDomain.MaxInt;

	/**
	 * It represents the cost variable.
	 */
	public IntVar costVariable = null;

	public int visited, mistakes;

	public SimpleDFS(Store s, int selection) {
		store = s;
		visited = 0;
		mistakes = 0;
		this.selection = selection;
		if (selection > 3 || selection < 0){
			this.selection = 0;
			System.err.println("Wrongful configuration of selection type for DFS");
		}
	}


	/**
	 * This function is called recursively to assign variables one by one.
	 */
	public boolean label(IntVar[] vars) {
		visited++;

		//specify if the program should print helpful debugging output
		if (trace) {
			for (int i = 0; i < vars.length; i++) 
				System.out.print (vars[i] + " ");
			System.out.println ();
		}

		ChoicePoint choice = null;
		boolean consistent;

		// Instead of imposing constraint just restrict bounds
		// -1 since costValue is the cost of last solution
		if (costVariable != null) {
			try {
				if (costVariable.min() <= costValue - 1)
					costVariable.domain.in(store.level, costVariable, costVariable.min(), costValue - 1);
				else
					return false;
			} catch (FailException f) {
				return false;
			}
		}

		consistent = store.consistency();

		if (!consistent) {
			// Failed leaf of the search tree
			return false;
		} else { // consistent

			if (vars.length == 0) {
				// solution found; no more variables to label

				// update cost if minimization
				if (costVariable != null)
					costValue = costVariable.min();

				reportSolution();

				return costVariable == null; // true is satisfiability search and false if minimization
			}

			choice = new ChoicePoint(vars);

			levelUp();

			store.impose(choice.getConstraint());

			// choice point imposed.

			//constrain the remaining variables before constraining this one
			consistent = label(choice.getSearchVariables());

			if (consistent) {
				levelDown();
				return true;
			} else {
				//the variable's constraint cannot be fullfilled, try other domain
				restoreLevel();
				store.impose(new Not(choice.getConstraint()));

				// negated choice point imposed.

				consistent = label(vars);

				levelDown();

				if (consistent) {
					return true;
				} else {
					//no solution found, not consistent
					mistakes++;
					return false;
				}
			}
		}
	}

	void levelDown() {
		store.removeLevel(depth);
		store.setLevel(--depth);
	}

	void levelUp() {
		store.setLevel(++depth);
	}

	void restoreLevel() {
		store.removeLevel(depth);
		store.setLevel(store.level);
	}

	public void reportSolution() {
		if (costVariable != null)
			System.out.println ("Cost: " + costVariable);

		for (int i = 0; i < variablesToReport.length; i++) 
			System.out.println (variablesToReport[i] + " ");
		System.out.print("Nodes Visited: " + visited + " | Mistakes: " + mistakes);
		System.out.println ("\n---------------");
	}

	public void setVariablesToReport(IntVar[] v) {
		variablesToReport = v;
	}

	public void setCostVariable(IntVar v) {
		costVariable = v;
	}

	public class ChoicePoint {

		IntVar var;
		IntVar[] searchVariables;
		int value;

		public ChoicePoint (IntVar[] v) {
			var = selectVariable(v);
			value = selectValue(var);
		}

		public IntVar[] getSearchVariables() {
			return searchVariables;
		}

		/**
		 * selects an FDV to constrain based on different algorithms
		 */
		IntVar selectVariable(IntVar[] v){
			switch(selection){
				case INPUT_ORDER: return selectVariableInputOrder(v);
				case MIN_DOMAIN: return selectVariableMinDom2(v);
				case MAX_DOMAIN: return selectVariableMinDom2(v);
				default: return selectVariableInputOrder(v);
			}
		}

		/**
		 * select a value based on type of algorithm
		 */
		int selectValue(IntVar v){
			switch(selection){
				case INPUT_ORDER: return v.min();
				case MIN_DOMAIN: return (v.max() + v.min())/2;
				case MAX_DOMAIN: return (int) Math.ceil((double)(v.max() + v.min())/2.0);
				default: return v.min();
			}
		}

		public PrimitiveConstraint getConstraint(){
			switch(selection){
				case INPUT_ORDER: return new XeqC(var, value);
				case MIN_DOMAIN: return new XlteqC(var, value);
				case MAX_DOMAIN: return new XgteqC(var, value);
				default: return new XeqC(var, value);
			}
		}

		/**
		 * example variable selection; input order
		 */ 
		IntVar selectVariableInputOrder(IntVar[] v) {
			if (v.length != 0) {

				searchVariables = new IntVar[v.length-1];
				for (int i = 0; i < v.length-1; i++) {
					searchVariables[i] = v[i+1]; 
				}

				return v[0];

			}
			else {
				System.err.println("Zero length list of variables for labeling");
				return new IntVar(store);
			}
		}

		/**
		 *  variable selection: min domain order, not intended solution,
		 *  deprecated method--
		 */ 
		IntVar selectVariableMinDom(IntVar[] v) {
			if (v.length != 0) {
				int minDom = Integer.MAX_VALUE;
				int pos = 0;
				//find smallest domain
				for (int i = 0; i < v.length; i++) {
					int newDom = v[i].domain.getSize();
					if (newDom < minDom) {
						minDom = newDom;
						pos = i;
					}
				}

				//check if smallest domain is single value
				if (minDom == 1){
					searchVariables = new IntVar[v.length-1];
					//remove var and save remaining of vars
					for(int i = 0; i < v.length-1; i++){
						if (i < pos){
							searchVariables[i] = v[i];
						} else {
							searchVariables[i] = v[i+1];
						}
					}
				} else {//save all vars
					searchVariables = new IntVar[v.length];
					for(int i = 0; i < v.length; i++) searchVariables[i] = v[i];
				}
				return v[pos];
			} else {
				System.err.println("Zero length list of variables for labeling");
				return new IntVar(store);
			}
		}
		/**
		 *  variable selection: min domain with input order
		 */ 
		IntVar selectVariableMinDom2(IntVar[] v) {
			//if only one variable, there is only one choice
			if (v.length == 1){
				searchVariables = new IntVar[0];
				return v[0];
			}
			if (v.length != 0) {
				int minDom = Integer.MAX_VALUE;
				//check for singular domain
				for (int i = 0; i < v.length; i++) {
					if(v[i].domain.getSize() == 1){
						searchVariables = new IntVar[v.length-1];
						//remove singular var and save remaining of vars
						for(int j = 0; j < v.length-1; j++){
							if (j < i){
								searchVariables[j] = v[j];
							} else {
								searchVariables[j] = v[j+1];
							}
						}
						return v[0];
					}
				}
				searchVariables = new IntVar[v.length];
				for(int i = 0; i < v.length; i++) searchVariables[i] = v[i];
				return searchVariables[0];
			} else {
				System.err.println("Zero length list of variables for labeling");
				return new IntVar(store);
			}
		}
	}
}
