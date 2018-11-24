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
array[1..4*n_edges] of int: arcs1d;
array[1..2*n_edges, 1..2] of int: arcs;
array[1..2*n_edges] of int: arcsl;
array[1..2*n_edges] of int: arcsr;
arcsl = [if j = 1 then from[i] else to[i] endif| i in 1..n_edges, j in 1..2];
arcsr =[if j = 1 then to[i] else from[i] endif | i in 1..n_edges, j in 1..2];
arcs1d = arcsl ++ arcsr;
arcs = array2d(1..2*n_edges, 1..2, arcs1d);

%for each node, find difference between in and out flow:
%all nodes except start and dests have flow 0 (undirected graph)
%for start and dests, flow = n_dests or -n_dests
array[1..graph_size] of int: balance;

%dests have flow -n_dest, might be -1
balance = [if i = start then n_dests else if has_element(i, dest) then -1 else 0 endif endif | i in 1..graph_size]; 

array[1..2*n_edges] of var int: path;
constraint network_flow(arcs, balance, path);
%constraint network_flow(arcs, balance, cost, path, tot_cost);

%var int: total = sum (i in path)(costs[i] * path[i]);
var int: total = sum (i in path)(i > 0)*costs[i];
%var int: total = sum (i in path)(costs[i]);
solve minimize total;
output[show(total)];
