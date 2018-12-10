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
set of int: edges = 1..n_edges;
var set of edges: route;

%stops is a subset of the bigger graph of nodes
set of int: graph = 1..graph_size;
var set of graph: stops;

%%CONSTRAINTS%%
%start and destrinations must be in route
constraint forall(i in stops)(i in route);

%require the stops to contain start and all destinations
%constraint forall(i in dest)(has_element(i, my_edges)); %for array route
constraint forall(i in dest)(i in stops);
constraint start in stops;

constraint forall(r in route)(to[r] in stops /\ from[r] in stops);

var int: total = sum(i in route)(cost[i]);
solve minimize total;

%CANNOT USE SHOW, WHAT DO I IMPORT?????
%int: tot = 1;
%output(show_int(tot));
