# READEME:

EX1 OOP in java

This project is about weighted undirected graphs.
One of the tasks is how to find the shortest path between two given nodes,
Check if a given graph is connected, copy the graph, save and load a graph from a file.
One of the algorythms that is used during the project is "Dijkstra Algorithm".

## WGraph_DS - 
* this class is all about the data and storing it.
* each node contains a key and data.
* the interface thats WGraph_DS implements is called 'weighted_graph'.

It contains the folowing public functions:
1.  getNode - Return the node_data by the node_id.
2.  hasEdge - Return if there is an edge between node1 and node2.
3.  getEdge - Return the weight if the edge between node1 and node1.
4.  addNode - Add a new node to the graph with the given key.
5.  connect - Connect an edge between node1 and node2, with an edge with weight >=0.
6.  getV - This method return a pointer for a Collection representing all the nodes in the graph/ connected to the given node.
7.  removeNode - Delete the node from the graph and returns the node.
8.  removeEdge - Delete the edge from the graph
9.  nodeSize - Return the number of vertices (nodes) in the graph.
10. edgeSize - Return the number of edges.
11. getMC - Return the Mode Count - for testing changes in the graph.
12. setMC - Sets the Mode Count
13. equals - For testing purpuses
14. hashCode - For testing purpuses

## WGraph_Algo
* this class is all about the algorythms and the graph itself. 
* including save and load of graph to file.
* some of the algorythms are BFS and Dijkstra algorithm .
* the interface thats WGraph_Algo implements is called 'weighted_graph_algorithms'.

It contains the folowing public functions:
1. init - Initialize the graph on which this set of algorithms operates on.
2. getGraph - Return the underlying graph of which this class works.
3. copy - Compute a deep copy of this weighted graph.
4. isConnected - Returns if there is a valid path from EVERY node to each other node. Uses BFS algorithm.
5. shortestPathDist - Returns the length of the shortest path between two given nodes. Uses Dijkstra algorithm.
6. shortestPath - Returns the the shortest path between src to dest - as an ordered List of nodes (src--> n1-->n2-->...dest).
7. save - Saves this graph to the given file name. return if save succeeded.
8. load - This method load a graph to this graph algorithm. return if load succeeded.
9. equals - For testing purpuses
10. hashCode - For testing purpuses

It contains the folowing private function:
dijkstra - an algorithm for finding the shortest paths between nodes in a graph
link to wikipedia about the algo - https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm


