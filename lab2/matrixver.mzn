%%%%%%%%LOGISTICS%%%%%%%%%
%include "minizinc.org";
include "globals.mzn";
include "input1.dzn";

%%READ INPUT DATA%%
int: graph_size;
int: start;
int: n_dests;
int: n_edges;

array[1..n_dests] of int: dest;
array[1..n_edges] of int: from;
array[1..n_edges] of int: to;
array[1..n_edges] of int: cost; 

%%CREATE SOLUTION ARRAY%%
%each element has index of edge
array[1..n_edges, 1..2] of var 1..graph_size: my_edges;
array[1..n_edges] of var 0..1: solution;

%%TRANSITION MATRIX%%
array[1..graph_size, 1..graph_size] of int: trans;

%%CONSTRAINTS%%
%must start in start node

%all edges must have both to and from nodes as part of route
%constraint forall(i in route)(to[i] in stops /\ from[i] in stops);

%array[1..n_edges] of var 1..1: balance;
%constraint network_flow(my_edges, balance, cost);

%constraint subcircuit(sol);
%constraint forall(i in dest)(has_element(i, sol));
%constraint has_element(start, sol);

%all edges must be connected
%constraint forall(i in col(edges, 1))(my_edges[i,2] = my_edges[i+1, 1]);
%constraint forall(i in stops)(to[i]
%constraint alldifferent(row(my_edges, 1));

%connected can be seen as each node needs to be present in 
%at least 2 edges except the required nodes

%all edges must connect to another edge
%all nodes in stops must be connected
%constraint forall(i in stops) forall(x in route)(x in to[x] or x in from[x]);

%require the stops to contain start and all destinations
%constraint forall(i in dest)(has_element(i, my_edges)); %for array route

solve minimize sum (i in solution)(cost[i]);
