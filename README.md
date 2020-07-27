# About

The NearestNeighbor visualizer and RangeSearchVisualizer compares brute force algo with kdtree algo to find nearest neighbor and point in a given range.

The red circle is of brute force algo and blue is of kdtree algo.

## Steps to run the program
First download the external [lib](http://algs4.cs.princeton.edu/code/algs4.jar) required to run the program.

then compile all files
```
javac -cp path_to_lib *.java
````

then to run NearestNeighborVisualizer, do
```
java -cp .:path_to_lib NearestNeighborVisualizer inputs/input_file_name
```

to run RangeSearchVisualizer, do
```
java -cp .:path_to_lib RangeSearchVisualizer inputs/input_file_name
```