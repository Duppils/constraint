%%%%%AUTO REGRESSION FILTER%%%%%
include "globals.mzn";
% clock cycle cost per task(operation)
int: del_add;
int: del_mul;
% number of processors of each task type
int: number_add;
int: number_mul;
int: n_proc = number_add + number_mul;
int: n;
set of int: last;
set of int: add;
set of int: mul;
array[1..n] of set of int : dependencies;
var 0..(number_add + number_mul) * n: clock_cycles;

% s = start times | d = durations | r = resource requirements | b = resource bound
array[1..n] of var int: s;
array[1..n] of var int: d = [if i in add then del_add else del_mul endif | i in 1..n];
array[1..n] of var int: r = d;
% var int: b = 30; % maybe use clock_cycles?
array[1..n] of var int: dy = [ 1 | i in 1..n];
% p lists the processors each task can use
array[1..n] of var 1..n_proc: p;
var int: add_dom;
var int: mul_dom;
constraint add_dom >= 1 /\ add_dom <= number_add;
constraint mul_dom > number_add /\ mul_dom < n_proc;  
p = [if i in add then add_dom else mul_dom endif| i in 1..n];

%constraint cumulative(s,d,r,clock_cycles);
constraint diffn(s,p,d,dy); % d is wrong and gives issues

%dependancy task start times + their execution times
%s = [if j = 1 add[i] else mul[i] endif|i = 1..n, j = 1..2];

solve minimize clock_cycles;
output["clock cycles = \(clock_cycles)\n runtime = s seconds\n optimal = T/F"];