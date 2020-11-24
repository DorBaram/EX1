package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class WGraph_DS implements weighted_graph , Serializable{
    //Variables of WGraph_DS
    private HashMap<Integer,node_info> map;
    private int eC;
    private int mC;

    //default constructor
    public WGraph_DS(){
        map = new HashMap<>();
        eC = 0;
        mC = 0;
    }

    /**
     * return the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return this.map.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     *
     * @param node1 start of edge
     * @param node2 the other start of the edge
     * @return if edge exist between node1 and node2
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        WNodeInfo node = (WNodeInfo)map.get(node1);
        if(node == null)
            return false;
        return(node.isNi(node2));
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        node_info n1 = map.get(node1);
        node_info n2 = map.get(node2);
        if((n1!=null) && (n2!=null) && (this.hasEdge(node1,node2)))
            return ((WNodeInfo)n1).edge.get(node2);
        return -1;
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(!(map.containsKey(key))) {
            map.put(key, new WNodeInfo(key));
            mC++;
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     *
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if((w>0) && (node1!=node2) && (map.containsKey(node1)) && (map.containsKey(node2))){
            WNodeInfo n1 = (WNodeInfo)map.get(node1);
            WNodeInfo n2 = (WNodeInfo)map.get(node2);
            if(n1.isNi(node2))
                eC--;           //if there is already an edge it will not count it as a new one
            n1.addNi(n2,w);
            mC++;
            eC++;
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return map.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     *
     * @param node_id
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        Collection<node_info> v = new HashSet<>();
        if(map.get(node_id)==null)
            return null;
        for (Integer i:((WNodeInfo) map.get(node_id)).getNi())
            v.add(map.get(i));
        return v;
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        WNodeInfo rem = (WNodeInfo)map.get(key);
        if(rem == null)
            return null;

        for (node_info i : getV(key)) {
            removeEdge(key,i.getKey());
        }
        map.remove(key);
        mC++;
        return rem;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        node_info n1 = map.get(node1);
        node_info n2 = map.get(node2);
        if ((node1!=node2) && (hasEdge(node1,node2))){
            ((WNodeInfo)n1).removeNi((WNodeInfo) n2);
            eC--;
            mC++;
        }
    }

    /**
     * return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return map.size();
    }

    /**
     * return the number of edges (unidirectional graph).
     * Note: this method should run in O(1) time.
     *
     * @return eC: counter of edges
     */
    @Override
    public int edgeSize() {
        return eC;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return mC: counter of changes in graph
     */
    @Override
    public int getMC() {
        return mC;
    }

    public void setMC(int mC){
        this.mC = mC;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WGraph_DS)) return false;
        if (this == o) return true;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return eC == wGraph_ds.eC && Objects.equals(map, wGraph_ds.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, eC);
    }
}

//______________________________________________________________________________________________________________________
 class WNodeInfo implements node_info , Comparable, Serializable {
    //Variables of node_info
    private int ID;                 //node's ID
    private static int StatID = 0;  //global ID, starts with 1
    private String Info;            //node's info
    private double Tag;             //node's tag
    private boolean visit;          //for checking if visited during search
    private node_info parent;       //a pointer for the father
    HashMap<Integer,Double> edge;   //node's map of edges

    //constructors of WNodeInfo
    //default constructor
    public WNodeInfo(){
        this.ID = ++StatID;
        this.Info = "";
        this.Tag = 0.0;
        this.visit = false;
        this.parent = null;
        this.edge = new HashMap<>();
    }
    //copy constructor
    public WNodeInfo(WNodeInfo node){
        this.ID = node.getKey();
        this.Info = node.getInfo();
        this.Tag = node.getTag();
        this.visit = node.visit;
        this.parent = node.parent;
        this.edge = (HashMap<Integer, Double>) node.edge.values();
    }
    //given ID constructor
    public WNodeInfo(int ID){
        this.ID = ID;
        this.Info = "";
        this.Tag = 0.0;
        this.visit = false;
        this.parent = null;
        this.edge = new HashMap<>();
    }


    /**
     * Return the key (id) associated with this node.
     * Note: each node_data should have a unique key.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.ID;
    }

    /**
     * return the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.Info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.Info = s;
    }

    /**
     * Temporal data (aka distance, color, or state)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public double getTag() {
        return this.Tag;
    }

    /**
     * Allow setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(double t) {
        this.Tag = t;
    }

    /**
     * This method returns a collection with all the Neighbor nodes of this node_data
     *
     */
    public Collection<Integer> getNi(){
        if(this==null)
            return null;
        return this.edge.keySet();
    }

    /**
     * This method returns true if a Neighbor node has the given ID
     * @param ID
     * @return boolean
     */
    public boolean isNi(int ID){
        return edge.containsKey(ID);
    }

    @Override
    public int compareTo(Object other) {
            return Double.compare(this.getTag(),((node_info)other).getTag());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    @Override
    public boolean equals(Object other){
        if((other instanceof node_info) && (ID ==((node_info) other).getKey()))
            return this.edge.equals(((WNodeInfo) other).edge);
        return false;
    }

    /**
     * This method connect the node(other) to this node.
     * @param other: node to connect
     * @param w: weight / length of edge
     */
    public void addNi(node_info other, double w){
        if(other.getKey()!=this.getKey()) {
            this.edge.put(other.getKey(), w);
            ((WNodeInfo) other).edge.put(this.getKey(), w);
        }
    }

    /**
     * This method removes the edge this-node
     * @param other: other node to disconnect
     */
    public void removeNi(WNodeInfo other){
        edge.remove(other.getKey());
        other.edge.remove(this.getKey());
    }

    /**
     * This method returns the parent of the node, designed for search algo
     */
    public node_info getParent(){
        return this.parent;
    }

    /**
     * This method stores the parent of the node, designed for search algo
     */
    public void setParent(WNodeInfo parent){
        this.parent = parent;
    }

    /**
     * This method returns the visit state of the node, designed for search algo
     */
    public boolean getVisit(){
        return this.visit;
    }

    /**
     * This method stores the visit state of the node, designed for search algo
     */
    public void setVisit(boolean visit){
        this.visit=visit;
    }


}
