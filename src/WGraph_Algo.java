package ex1;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms , Serializable {
    //Variables of WGraph_Algo
    private weighted_graph graph;

    //constructors of WGraph_Algo
    public WGraph_Algo(){}

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g: graph to be initialize
     */
    @Override
    public void init(weighted_graph g) {
        this.graph = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return graph's pointer
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph g = new WGraph_DS();         //creates the copy g
        if (this.graph == null)
            return g;
        for (node_info n : this.graph.getV()) {     //go through all the nodes and transfer them into g
            if (n != null)
                g.addNode(n.getKey());
        }
        for (node_info n1 : this.graph.getV()) {    //go through all the neighbers of nodes and connect them
            int n1k = n1.getKey();
            for (node_info n2 : this.graph.getV(n1k)) {
                int n2k = n2.getKey();
                double w = this.graph.getEdge(n1k,n2k);
                if(w>=0)
                    g.connect(n1k,n2k,w);
            }
        }
        ((WGraph_DS)g).setMC(g.edgeSize() + g.getV().size());                    //set mc to 0 "like a new graph"
        return g;
        //g = copy of graph ; n,n1,n2 = temp nodes ; n1k,n2k = temp node keys
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVERY node to each
     * other node. NOTE: assume unidirectional graph.
     *
     * @return if graph is connected
     */
    @Override
    public boolean isConnected() {
        if(this.graph.getV().size()<2)           //if the graph contains 0/1 nodes that's considered connected
            return true;

        node_info first = null;                  //node for the first one it could find         //////////////////if not working remove null!!
        boolean foundFirst = false;              //a helper boolean for finding the first node
        for(node_info n : this.graph.getV()) {   //sets all the node's info to "0"
            if(n!= null){
                if (!foundFirst) {
                    first = n;
                    foundFirst = true;
                    first.setInfo("1");             //"1" = visited
                } else  n.setInfo("0");             //"0" = not visited
            }}

        Queue<node_info> q = new LinkedList<>();
        q.add(first);
        //travers on every node connected to 'first' and changes the info to '1'
        while(!q.isEmpty()){
            node_info nTemp = q.poll();
            for(node_info n : this.graph.getV(nTemp.getKey())){
                if(n.getInfo().equals("0")){
                    n.setInfo("1");
                    q.add(n);
                }
            }
        }

        for(node_info n : this.graph.getV()){           //checks if any node not visited
            if(n.getInfo().equals("0"))
                return false;
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return distance of src to dest
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        node_info ns = this.graph.getNode(src);
        node_info nd = this.graph.getNode(dest);
        if((ns==null) || (nd==null))            //for the case that one of the nodes arent in the graph
            return -1;
        if(src==dest)                           //for the case that one is the other
            return 0;
        dijkstra(ns);
        return nd.getTag();
    }

    private void dijkstra(node_info start){
        for (node_info iter:this.graph.getV()) {
            iter.setTag(Double.MAX_VALUE);
            ((WNodeInfo)iter).setParent(null);
            ((WNodeInfo) iter).setVisit(false);
        }
        start.setTag(0);
        PriorityQueue<node_info> q = new PriorityQueue<>();
        q.add(start);
        while (!q.isEmpty()){
            node_info min = q.poll();
            ((WNodeInfo)min).setVisit(true);
            for(node_info ni : graph.getV(min.getKey())){
                double w = min.getTag() + this.graph.getEdge(min.getKey(),ni.getKey());
                if(!((WNodeInfo)ni).getVisit()) {
                    ni.setTag(w);
                    ((WNodeInfo) ni).setParent((WNodeInfo) min);
                    ((WNodeInfo)ni).setVisit(true);
                    q.add(ni);
                }
                else {
                    if(ni != ((WNodeInfo) min).getParent())
                        if(w<ni.getTag()){
                            ni.setTag(w);
                            ((WNodeInfo) ni).setParent((WNodeInfo) min);
                        }
                }
            }
        }
    }


    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(shortestPathDist(src, dest) == -1)               //the other 'shortestPathDist' function does all the heavy thins
            return null;
        LinkedList<node_info> l = new LinkedList<>();
        node_info tempParent = ((WNodeInfo)this.graph.getNode(dest)).getParent();   //sets the temp parent to be the parent of dest
        l.add(this.graph.getNode(dest));
        while(tempParent!=null) {
            l.addFirst(tempParent);
            int t = tempParent.getKey();
            tempParent = ((WNodeInfo) graph.getNode(t)).getParent();     //sets the temp parent to be the parent of temp parent
        }
        return l;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this.graph);
            o.close();
            f.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream o = new ObjectInputStream(f);
            this.graph = (weighted_graph) o.readObject();
            o.close();
            f.close();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WGraph_Algo)) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(getGraph(), that.getGraph());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGraph());
    }
}