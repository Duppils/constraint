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

% s = start times | d = durations | r = resource requirements | clock_cycles = resource bound
array[1..n] of var 0..200: s;
array[1..length(add)] of var 0..100: sa;
array[1..length(mul)] of var 0..100: sm;
var 0..n * del_add: clock_cycles_add;
var 0..n * del_mul: clock_cycles_mul;
array[1..length(add)] of var 1..number_add: pa;
array[1..length(mul)] of var 1..number_mul: pm;
array[1..length(add)] of int: ra = [1 | i in 1..length(add)];
array[1..length(mul)] of int: rm = [1 | i in 1..length(mul)];
array[1..length(add)] of int: dya = [1 | i in 1..length(add)];
array[1..length(mul)] of int: dym = [1 | i in 1..length(mul)];
array[1..length(add)] of int: da = [del_add | i in 1..length(add)];
array[1..length(mul)] of int: dm = [del_mul | i in 1..length(mul)];

% diffn constrains all blocks: defined by the start koordinate (s, p) and dimensions (dx ,dy), enforces nooverlap constraints
% enforces no overlap
constraint diffn(sa, pa, da, dya);
constraint diffn(sm, pm, dm, dym);

constraint cumulative(sa, da, ra, clock_cycles_add);
constraint cumulative(sm, dm, rm, clock_cycles_mul);

% s FDVs match sa / sm
constraint forall (i in 1..length(add))(s[add[i]] = sa[i]);
constraint forall (i in 1..length(mul))(s[mul[i]] = sm[i]);

% if ti dependent on tj then tj must precede ti
constraint forall (i in add, dep in dependencies[i])(if dep in mul then s[dep] + del_mul <= s[i] else s[dep] + del_add <= s[i] endif);
constraint forall (i in mul, dep in dependencies[i])(if dep in mul then s[dep] + del_mul <= s[i] else s[dep] + del_add <= s[i] endif);

clock_cycles = clock_cycles_add + clock_cycles_mul; % total clock cycles, might be useless
% all tasks must be completed in <= total clock cycles used
constraint forall (i in sa)(i + del_add <= clock_cycles);
constraint forall (i in sm)(i + del_mul <= clock_cycles);
solve minimize clock_cycles;
output["clock cycles = \(clock_cycles)\nschedule adds = \(sa)\nschedule muls = \(sm)\nschedule = \(s)\n"];% runtime = s seconds\n optimal = T/F"];
