% clock cycle cost per task(operation)
int : del_add;
int : del_mul;
% number of processors of each task type
int : number_add;
int : number_mul;
int : n; % number of tasks
set of int : last; % last task completed -> all tasks are finished
set of int : add; % add operations(tasks)
set of int : mul; % mul operations
% task dependencies | si is start time - di completion time | for task i,
% calculate dependency times: if sj dependant on si: si + di <= sj
array[1..n] of set of int : dependencies;
int var: clock_cycles; % clock cycles for scheduled tasks

% constraint cumulative(array[int] of var (opt -is optional) 
% int: s, array[int] of var int: d, array[int] of var int: r, var int: clock_cycles) % r: resource req for task i

% constraint diffn(array[int] of var int: x,y,dx,dy)
% x: start time of task(operation)
% y: operation processor used, either add processor or mul processor
% if more than one processor of same type, use processor (1..n_processors) FDV
% dx: always del_add or del_mul, this is the clock cycles to complete operation
% dy: should probably always be 0, an operation should never cost both add and mul processing

% output: clock_cycles, runtime(s), optimal(boolean)