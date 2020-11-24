package tests;

import ex1.*;

import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class WGraph_Tests {

    @BeforeAll
    static void before(){
        System.out.println("Start testing, please stand by");
    }
    @AfterAll
    static void after(){
        System.out.println("Done");
    }

    @Test
    void duplicateAddRemove(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 3; i++) {
            g.addNode(i);
            g.addNode(i+1);
            g.connect(i,i+1,1.0);
        }

        for (int i = 0; i < 5; i++) {
            g.removeNode(i);
        }
        assertEquals(0, g.nodeSize());
        assertEquals(14,g.getMC() );
    }

    @Test
    void linkedList(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
            g.addNode(i+1);
            g.connect(i,i+1,1.0);
        }
        assertEquals(10, g.edgeSize());
        assertEquals(10,g.edgeSize());
        for (int i = 0; i < 10; i++) {
            assertFalse(g.hasEdge(10-i,i));
        }
    }

    @Test
    void edgeTester(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
            g.addNode(i+1);
            g.connect(i,i+1,1.0);
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                double ij = g.getEdge(i,j);
                double ji = g.getEdge(j,i);
                assertEquals(ij,ji);
            }
        }
    }

    @Test
    void starRemover(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(i);
            g.connect(i,0,1.0);
        }
        assertFalse(g.hasEdge(0,0));
        for (int i = 1; i < 100; i++) {
            assertTrue(g.hasEdge(0,i));
            assertTrue(g.hasEdge(i,0));
        }
        g.removeNode(0);
        for (int i = 0; i < 105; i++) {
            assertFalse(g.hasEdge(0,i));
            assertFalse(g.hasEdge(i,0));
        }
    }

    @Test
    void smallGraph(){
        weighted_graph g = new WGraph_DS();
        g.addNode(20);
        g.addNode(50);
        g.addNode(80);
        assertEquals(-1,g.getEdge(20,20));
        assertEquals(-1,g.getEdge(20,50));
        assertEquals(-1,g.getEdge(20,80));
        assertEquals(-1,g.getEdge(50,80));
        g.connect(20,50,70);
        g.connect(50,80,130);
        assertEquals(70,g.getEdge(20,50));
        assertEquals(70,g.getEdge(50,20));
        assertEquals(130,g.getEdge(80,50));
        g.removeEdge(50,80);
        assertEquals(-1,g.getEdge(50,80));
        g.connect(50,80,200);
        g.connect(50,80,100);
        assertEquals(100,g.getEdge(80,50));
        g.removeNode(50);
        assertEquals(-1,g.getEdge(20,50));
        assertEquals(-1,g.getEdge(20,80));
        assertEquals(-1,g.getEdge(50,80));
        assertEquals(-1,g.getEdge(50,20));
    }

    @Test
    void collection(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(i);
            g.connect(i,(int)i/2,1.0);
        }
        Collection<node_info> col = g.getV();
        for (node_info node:col) {
            int key = node.getKey();
            assertNotNull(node);
            assertEquals(g.getNode(key), node);
        }
    }

    @Test
    void oneMillion(){
        weighted_graph g = new WGraph_DS();
        int mil = (int) Math.pow(10,6);
        System.out.println("Generating 1 Million Nodes In Graph, And 10 Million Edges");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < mil; i++) {
            g.addNode(i);
            for (int j = 0; j < 10; j++) {
                g.connect(i,i-j,1);
            }
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Done, It Took " + estimatedTime/1000.0 + " seconds");
        //on my machine it took about 1.7 seconds
    }

    @Test
    void connected(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(i);
            g.connect(i,(int)i/2,1.0);
        }
        WGraph_Algo f = new WGraph_Algo();
        f.init(g);
        assertTrue(f.isConnected());
    }

    @Test
    void linkedListDist(){
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(i);
            g.connect(i,i-1,1.0);
        }
        WGraph_Algo f = new WGraph_Algo();
        f.init(g);
        assertTrue(f.isConnected());
        //assertEquals(99,f.shortestPathDist(0,99));
    }
    @Test
    void copy() {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 5; i++) {
            g.addNode(i);
        }
        weighted_graph f = new WGraph_DS();
        weighted_graph_algorithms h = new WGraph_Algo();
        weighted_graph_algorithms e = new WGraph_Algo();
        assertNotNull(f);
        h.init(f);
        assertEquals(f, h.copy());
        assertNotEquals(e, h.copy());
        h.init(g);
        assertEquals(g, h.copy());
        assertNotEquals(e, h.copy());
    }
    @Test
    void saveAndLoad(){
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 100; i++) {
            g.addNode(i);
            g.connect(i,(int)i/2,1);
        }
        WGraph_Algo f = new WGraph_Algo();
        f.init(g);
        assertTrue(f.save("Test"));
        WGraph_Algo e = new WGraph_Algo();
        assertTrue(e.load("Test"));
        assertEquals(e,f);
    }

}
