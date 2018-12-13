%%%%Urban Planning%%%%
include "globals.mzn";
int: n;
int: n_commercial;
int: n_residential;
array[0..n] of int: point_distribution;
array[1..n*n] of var 0..1: lots_array;
array[1..n, 1..n] of var 0..1: lots = array2d(1..n, 1..n, lots_array);
array[1..2*n] of var int: points;

% lots matrix must have n_commerical commercial spaces and n_residential residential spaces
% 0 represents a commercial lot and 1 a residential lot
constraint sum(lots_array) = n_residential;

% calculate points for each row and col
constraint forall(i in 1..n)(points[i] = point_distribution[sum([lots[i,j] | j in 1..n])]);
constraint forall(i in 1..n)(points[i+n] = point_distribution[sum([lots[j,i] | j in 1..n])]);

% symmetry break: lex2 requires adjecent rows and columns to be lexicographically ordered
constraint lex2(lots);

% optimize for placements with highest point score
var int: point_total = sum(points);
solve :: int_search(lots_array, first_fail, indomain_min, complete) 
    maximize point_total;
output["point total = \(point_total)\npoint distribution = \(points)\n"];
