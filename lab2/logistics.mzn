%%%%%%%%LOGISTICS%%%%%%%%%
%include "minizinc.org";
include "globals.mzn";


%%READ INPUT DATA%%
int: graph_size;
int: start;
int: n_dests;
int: n_edges;

array[1..n_dests] of int: dest;
array[1..n_edges] of int: from;
array[1..n_edges] of int: to;
array[1..n_edges] of int: cost; 
array[1..2*n_edges] of int: costs;
costs = cost ++ cost;

%edges, undirected:
array[1..2*n_edges, 1..2] of int: arcs;
array[1..2*n_edges] of int: arcsl = [if j = 1 then from[i] else to[i] endif| i in 1..n_edges, j in 1..2];
array[1..2*n_edges] of int: arcsr = [if j = 1 then to[i] else from[i] endif | i in 1..n_edges, j in 1..2];
arcs = array2d(1..2*n_edges, 1..2, arcsl ++ arcsr);
%array[1..n_edges, 1..2] of int: arcs1d = array2d(1..n_edges, 1..2, arcsl);

%verifies that my list comprehension works
%arcs = [|1,2|1,3|2,3|2,4|3,5|4,5|4,6|2,1|3,1|3,2|4,2|5,3|5,4|6,4|];

%all nodes except start and dests have flow 0 (undirected graph)
array[1..graph_size] of int: balance;
balance = [if i = start then -1*n_dests else if has_element(i, dest) then 1 else 0 endif endif | i in 1..graph_size]; 

array[1..2*n_edges] of var int: path;
constraint network_flow(arcs, balance, path);
%var int: tot_cost;
%constraint network_flow_cost(arcs1d, balance, cost, path, tot_cost);

%requires bool2int(i > 0) in previous ver of minizinc
%var int: total = sum (i in path)((i > 0)*costs[i]);
var int: total = sum (i in path)(costs[i]);
solve minimize total;
output[show(total), show(path), show(arcs), show(costs),  show(balance)];
