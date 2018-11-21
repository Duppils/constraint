%%%%%%%%LOGISTICS%%%%%%%%%

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
set of int: edges = 1..n_edges;
var set of edges: route;

%sol[i] = j specifies from node i to node j is the path taken
array[1..graph_size] of var 1..graph_size: sol;

%%TRANSITION MATRIX%%
%array[graph_size, graph_size] of int: trans; %not sure if useful yet..

%stops is a subset of the bigger graph of nodes
set of int: graph = 1..graph_size;
var set of graph: stops;

var set of graph: required;
constraint start in required;
constraint forall(i in dest)(i in stops);
% n_dests + 1 of the values in required must be present in solution, in this
% case, all destinations + start must be part of solution
%constraint among(n_dests + 1, sol, required);

%%CONSTRAINTS%%
%must start in start node

%all edges must have both to and from nodes as part of route
%constraint forall(i in route)(to[i] in stops /\ from[i] in stops);

%array[1..n_edges] of var 1..1: balance;
%constraint network_flow(my_edges, balance, cost);

constraint subcircuit(sol);
%constraint forall(i in dest)(has_element(i, sol));
%constraint has_element(start, sol);

%all edges must be connected
%constraint forall(i in col(edges, 1))(my_edges[i,2] = my_edges[i+1, 1]);
%constraint forall(i in stops)(to[i]
%constraint alldifferent(row(my_edges, 1));

%all edges must connect to another edge
%all nodes in stops must be connected
%constraint forall(i in stops) forall(x in route)(x in to[x] or x in from[x]);

%require the stops to contain start and all destinations
%constraint forall(i in dest)(has_element(i, my_edges)); %for array route
constraint forall(i in dest)(i in stops);
constraint start in stops;

solve minimize sum (i in route)(cost[i]);
