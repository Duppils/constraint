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
array[1..n_edges] of var 0..1: solution;

%%CONSTRAINTS%%
var 0..100: total_cost; %%temporary max value until i learn how to describe INTMAX

solve minimize total_cost;
