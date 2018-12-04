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

%array[1..n] of var 0..1: a_row = [row(lots, i)[0] | i in 1..n];%row(lots, 1);
%int: aa = sum(point_distribution);
%var int: something = lots[0][0]; %2-d array accessed with 1 expression

% calculate points for each row and col
%constraint forall(i in 1..n)(points[i] = point_distribution[sum([lots[i][j] | j in 1..n])]);
%constraint forall(i in 1..n)(points[i] = point_distribution[sum(row(lots, i)[0])]);
%constraint forall(i in 1..n)(points[i] = point_distribution[sum([row((j in 1..n)(lots[i,j]))]);
%constraint forall(i in n+1..2*n)(points[i] = point_distribution[sum(col(lots, i))]);
%constraint forall(i in 1..n, j in 1..n)(sum([


var int: point_total = sum(points);
solve maximize point_total;
output["point total = \(point_total)\npoint distribution = \(points)\nrow = \(row(lots, 1))"];
