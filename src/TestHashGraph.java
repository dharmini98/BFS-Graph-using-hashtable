import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.util.Edge;
import edu.uwm.cs351.util.Graph;
import edu.uwm.cs351.util.HashGraph;


public class TestHashGraph extends LockedTestCase {

	private static final int SEED = 351351351;
	private static final Random r = new Random(SEED);
	private Graph<Integer> g1;
	private Graph<String> g2;
	private Set<Integer> vertexSet;
	private Set<Edge<Integer>> edgeSet;
	
	@Override
	protected void setUp()  {
		try {
			assert g1.vertexSet().isEmpty();
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		g1 = new HashGraph<Integer>();
		g2 = new HashGraph<String>();
		vertexSet = g1.vertexSet();
		edgeSet = g1.edgeSet();
	}
	
	/** 
	 * 0x: Tests for addVertex/containsVertex/addEdge/containsEdge/order/size
	 * 
	 * Dependencies: constructor
	 */
	
	public void test00() {
		assertEquals(Tb(1671692984), g1.addVertex(1));
		assertEquals(Tb(603451421), g1.addVertex(1));
		assertEquals(Ti(1116006038), g1.order());
		assertEquals(true, g1.addVertex(2));
		assertEquals(2, g1.order());
		assertEquals(true, g1.containsVertex(1));
		assertEquals(Tb(961925521), g1.containsVertex(null));
	}

	public void test01() {
		addVertices(g1, 1, 2, 3, 4);
		assertEquals(false, g1.addVertex(1));
		assertEquals(4, g1.order());
		testVertices(g1, true, 1, 2, 3, 4);
		assertFalse(g1.containsVertex(0));
		assertFalse(g1.containsVertex(null));
	}

	public void test02() {
		addVertices(g1, 1, 2, 3, 4);
		assertEquals(Tb(1576502579), g1.addEdge(1, 2));
		assertEquals(Tb(1226122612), g1.addEdge(2, 1));
		assertEquals(true, g1.addEdge(2, 3));
		assertEquals(true, g1.addEdge(1, 4));
		assertEquals(true, g1.containsEdge(2, 3));
		assertEquals(Tb(1183174622), g1.containsEdge(2, 1));
		assertEquals(false, g1.containsEdge(null, 1));
		assertEquals(false, g1.containsEdge(1, null));
		assertEquals(false, g1.containsEdge(null, null));
		assertEquals(false, g1.addEdge(1, 2));
		
		testVertices(g1, true, 1, 2, 3, 4);
	}
	
	public void test03() {
		addVertices(g1, 1, 2, 3);
		assertEquals(true, g1.addEdge(1, 2));
		assertEquals(true, g1.addEdge(2, 3));
		assertEquals(Ti(1351759991), g1.order());
		assertEquals(Ti(1419945420), g1.size());
		testEdges(g1, true, e(1,2), e(2,3));
	}

	public void test04() {
		// fresh graph
		assertEquals(true, g1.addEdge(1, 2));
		assertEquals(true, g1.containsVertex(1));
		assertEquals(true, g1.containsVertex(2));
		assertEquals(true, g1.addEdge(2, 3));
		assertEquals(false, g1.addEdge(3,2));
		assertEquals(true, g1.addEdge(3, 4));
		testVertices(g1, true, 1, 2, 3, 4);
		assertEquals(Ti(1563935988), g1.order());
		assertEquals(Ti(1215344712), g1.size());
		testEdges(g1, true, e(1,2), e(2,3), e(3,4));
	}

	public void test05() {
		ArrayList<Integer> vertices = new ArrayList<>();
		ArrayList<Edge<Integer>> edges = new ArrayList<>();
		
		for (int i=0; i<100; ++i) {
			int a = r.nextInt(10000);
			int b = r.nextInt(10000);

			// compare against lists
			assertEquals(vertices.contains(a), g1.containsVertex(a));
			assertEquals(vertices.contains(b), g1.containsVertex(b));
			assertEquals(edges.contains(e(a,b)), g1.containsEdge(a, b));
			assertEquals(edges.contains(e(b,a)), g1.containsEdge(b, a));
			
			// perform add
			g1.addEdge(a, b);
			
			// check resulting graph
			assertTrue(g1.containsVertex(a));
			assertTrue(g1.containsVertex(b));
			assertTrue(g1.containsEdge(a,b));
			assertTrue(g1.containsEdge(b,a));
			
			// update lists
			vertices.add(a); vertices.add(b);
			edges.add(e(a,b));edges.add(e(b,a));
		}
	}

	public void test06() {
		testException(() -> g1.addVertex(null), "g.addVertex(null)", IllegalArgumentException.class);
		testException(() -> g1.addEdge(null, null), "g.addEdge(null, null)", IllegalArgumentException.class);
		testException(() -> g1.addEdge(1, null), "g.addEdge(1, null)", IllegalArgumentException.class);
		testException(() -> g1.addEdge(null, 1), "g.addEdge(null, 1)", IllegalArgumentException.class);
		testException(() -> g1.addEdge(1, 1), "g.addEdge(1, 1)", IllegalArgumentException.class);
	}
	
	/** 
	 * 1x: Tests for getConnected
	 * 
	 * Dependencies: addVertex/containsVertex/addEdge/containsEdge/order/size
	 */

	public void test10() {
		addVertices(g1, 1, 2, 3, 4);
		assertNull(g1.getConnected(null));
		testConnected(g1, 1);
		testConnected(g1, 2);
		testConnected(g1, 3);
		testConnected(g1, 4);
	}
	
	public void test11() {
		addEdges(g1, e(1,2), e(4,5), e(4,3));
		testConnected(g1, 1, 2);
		testConnected(g1, 2, 1);
		testConnected(g1, 4, 3, 5);
		testConnected(g1, 3, 4);
		testConnected(g1, 5, 4);
	}
	
	public void test12() {
		addEdges(g1, e(1,2), e(1,3), e(1,4), e(1,5));
		testConnected(g1, 1, 2, 3, 4, 5);
		testConnected(g1, 2, 1);
		testConnected(g1, 3, 1);
		testConnected(g1, 4, 1);
		testConnected(g1, 5, 1);
	}
	
	public void test13() {
		addEdges(g1, e(1,2), e(1,3), e(1,4), e(1,5));
		Set<Integer> connected = g1.getConnected(1);
		testException(() -> connected.remove(2), "set of connections should not be modifiable", UnsupportedOperationException.class);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void testConnected(Graph<T> graph, T vertex, T... expectedConnections) {
		Set<T> actualConnections = graph.getConnected(vertex);
		for (T v: expectedConnections)
			assertTrue("vertex "+v+" should be connected to "+vertex, actualConnections.contains(v));
		assertEquals("wrong number of connections", actualConnections.size(), expectedConnections.length);
		
	}
	
	
	/** 
	 * 2x: Tests for removeVertex/removeEdge
	 * 
	 * Dependencies: All other native graph mutators (above)
	 */

	public void test20() {
		addVertices(g2, "a", "b", "c", "d");
		assertEquals(true, g2.removeVertex("a"));
		assertEquals(false, g2.removeVertex("b c"));
		assertEquals(3, g2.order());
		g2.addVertex("g");
		g2.addVertex("t");
		g2.removeVertex("c");
		assertEquals(4, g2.order());
		assertEquals(0, g2.size());
	}

	public void test21() {
		addVertices(g1, 1, 2, 3, 4, 5);
		assertEquals(true, g1.removeVertex(1));
		assertEquals(Tb(1833110037), g1.removeVertex(2+2));
		assertEquals(true, g1.removeVertex(2));
		assertEquals(2, g1.order());
	}

	public void test22() {
		addEdges(g1, e(1,2), e(1,3), e(2,3));
		assertEquals(Tb(1264864469), g1.order() == g1.size());
		g1.removeVertex(2);
		assertEquals(Tb(810348013), g1.order() == g1.size());
		assertEquals(Ti(218204417), g1.order());
		assertEquals(Ti(822093892), g1.size());
	}
	
	public void test23() {
		addEdges(g2, e("art", "bat"));
		assertEquals(true, g2.removeEdge("bat", "art"));
		assertEquals(2, g2.order());
		assertEquals(0, g2.size());
	}

	public void test24() {
		addEdges(g1, e(1,2), e(1,3), e(2,3));
		assertFalse(g1.removeEdge(2, 4));
		assertEquals(true, g1.removeEdge(2,1));
		assertEquals(3, g1.order());
		g1.removeEdge(1, 3);
		assertEquals(1, g1.size());
		g1.removeVertex(2);
		assertEquals(0, g1.size());
		assertEquals(2, g1.order());
	}
	

	public void test25() {  
		  ArrayList<Integer> vertices = new ArrayList<>();  
		  ArrayList<Edge<Integer>> edges = new ArrayList<>();  
		    
		  for (int i=0; i<100; ++i) {  
		   int a = r.nextInt(10000);  
		   int b = r.nextInt(10000);  
		   g1.addEdge(a, b);  
		   vertices.add(a); vertices.add(b);  
		   edges.add(e(a,b));edges.add(e(b,a));  
		  }  
		    
		  for (int i=0; i<100; ++i) {  
		   if (r.nextBoolean() || edges.isEmpty()) {  
		    // Remove Vertex  
		    Integer toRemove = vertices.get(r.nextInt(vertices.size()));  
		    testVertices(g1, true, toRemove);  
		    assertTrue(g1.removeVertex(toRemove));  
		    testVertices(g1, false, toRemove);  
		    assertNull(g1.getConnected(toRemove));  
		    for (Iterator<Edge<Integer>> it = edges.iterator(); it.hasNext();) {  
		     Edge<Integer> edge = it.next();  
		     if (edge.contains(toRemove)) {  
		      it.remove();  
		      testEdges(g1, false, edge);  
		     }  
		    }  
		    vertices.remove(toRemove);  
		   }  
		   else {  
		    // Remove Edge  
		    Edge<Integer> edge = edges.get(r.nextInt(edges.size()));  
		    assertTrue(g1.containsEdge(edge.a, edge.b));  
		    assertTrue(g1.containsEdge(edge.b, edge.a));  
		      
		    assertTrue(g1.removeEdge(edge.a, edge.b));  
		    assertFalse(g1.containsEdge(edge.a, edge.b));  
		    assertFalse(g1.containsEdge(edge.b, edge.a));  
		    assertFalse(g1.getConnected(edge.a).contains(edge.b));  
		    assertFalse(g1.getConnected(edge.b).contains(edge.a));  
		    edges.remove(e(edge.a, edge.b));  
		    edges.remove(e(edge.b, edge.a));  
		   }  
		  }  
		 }
	/** 
	 * 3x: Tests for vertexSet/edgeSet
	 * 
	 * Dependencies: All graph operations (except iterators)
	 */
	
	public void test30() {
		addVertices(g1, 1, 2, 3, 4, 5);
		assertEquals(vertexSet.size(), g1.order());
		vertexSet.remove(4);
		assertEquals(false, g1.containsVertex(4));
		assertEquals(Tb(1692735290), vertexSet.size() == g1.order());
		vertexSet.clear();
		assertEquals(Ti(1750712679), g1.order());
	}

	public void test31() {
		addVertices(g1, 1, 2, 3, 4, 5);
		assertEquals(vertexSet.size(), g1.order());
		g1.removeVertex(1);
		assertEquals(false, vertexSet.contains(1));
		assertEquals(true, vertexSet.size() == g1.order());
	}

	public void test32() {
		addEdges(g1, e(1,2), e(1,3), e(2,3));
		assertEquals(edgeSet.size(), g1.order());
		edgeSet.remove(e(1,2));
		assertEquals(false, g1.containsEdge(1,2));
		assertEquals(false, g1.containsEdge(2,1));
		assertEquals(Tb(1587315219), edgeSet.size() == g1.size());
		edgeSet.clear();
		assertEquals(0, g1.size());
	}

	public void test33() {
		addEdges(g1, e(1,2), e(1,3), e(2,3));
		assertEquals(edgeSet.size(), g1.order());
		g1.removeEdge(3, 1);
		assertEquals(Tb(1643333223), edgeSet.contains(e(1,3)));
		assertEquals(false, edgeSet.contains(e(3,1)));
		assertEquals(true, edgeSet.size() == g1.size());
	}

	public void test34() {
		addEdges(g1, e(1,2), e(1,3), e(2,3), e(1,5), e(5,4), e(4,6), e(6,7));
		assertTrue(edgeSet.add(e(5,7)));
		
		testEdges(g1, true, e(5,7), e(6,7));
		testEdges(edgeSet, true, e(5,7), e(6,7));
		assertTrue(vertexSet.remove(7));
		testVertices(g1, false, 7);
		testEdges(g1, false, e(5,7), e(6,7));
		testEdges(edgeSet, false, e(5,7), e(6,7));
		
		testEdges(g1, true, e(1,2), e(1,3), e(1,5));
		testEdges(edgeSet, true, e(1,2), e(1,3), e(1,5));
		assertTrue(g1.removeVertex(1));
		testVertices(g1, false, 1);
		testEdges(g1, false, e(1,2), e(1,3), e(1,5));
		testEdges(edgeSet, false, e(1,2), e(1,3), e(1,5));
		
		// {2,3,4,5,6}, {2-3, 4-5, 4-6}
		assertEquals(3, g1.size());
		assertEquals(3, edgeSet.size());
		assertEquals(5, g1.order());
		assertEquals(5, vertexSet.size());
		
		g1.removeVertex(1);
		g1.removeVertex(5);
		g1.removeVertex(7);
		testVertices(g1, false, 1, 5, 7);
		testVertices(vertexSet, false, 1, 5, 7);
		testEdges(g1, false, e(4,5));
		testEdges(edgeSet, false, e(4,5));
	}

	public void test35() {
		ArrayList<Integer> vertices = new ArrayList<>();
		ArrayList<Edge<Integer>> edges = new ArrayList<>();
		
		for (int i=0; i<200; ++i) {
			int a = r.nextInt(10000);
			int b = r.nextInt(10000);
			if (r.nextBoolean()) {
				g1.addEdge(a, b);
				testVertices(g1, true, a, b);
				testVertices(vertexSet, true, a, b);
				testEdges(edgeSet, true, e(a,b));}
			else {
				edgeSet.add(e(a,b));
				testVertices(g1, true, a, b);
				testVertices(vertexSet, true, a, b);
				testEdges(g1, true, e(a,b));}
			vertices.add(a); vertices.add(b);
			edges.add(e(a,b));edges.add(e(b,a));
		}
		
		for (int i=0; i<200; ++i) {
			if (r.nextBoolean() || edges.isEmpty()) {
				// Remove Vertex
				Integer toRemove = vertices.get(r.nextInt(vertices.size()));
				testVertices(g1, true, toRemove);
				testVertices(vertexSet, true, toRemove);
				Set<Integer> prevConnected = g1.getConnected(toRemove);
				
				assertTrue(r.nextBoolean() ? g1.removeVertex(toRemove) : vertexSet.remove(toRemove));
				testVertices(g1, false, toRemove);
				testVertices(vertexSet, false, toRemove);
				assertNull(g1.getConnected(toRemove));
				
				for (Integer vertex: prevConnected) {
					testEdges(g1, false, e(vertex,toRemove));
					testEdges(edgeSet, false, e(vertex,toRemove));
					edges.remove(e(vertex, toRemove));
					edges.remove(e(toRemove, vertex));
				}
				
				vertices.remove(toRemove);
			}
			else {
				// Remove Edge
				Edge<Integer> edge = edges.get(r.nextInt(edges.size()));
				testEdges(g1, true, edge);
				testEdges(edgeSet, true, edge);
				
				assertTrue(r.nextBoolean() ? g1.removeEdge(edge.a, edge.b) : edgeSet.remove(edge));
				
				testEdges(g1, false, edge);
				testEdges(edgeSet, false, edge);
				assertFalse(g1.getConnected(edge.a).contains(edge.b));
				assertFalse(g1.getConnected(edge.b).contains(edge.a));
				edges.remove(e(edge.a, edge.b));
				edges.remove(e(edge.b, edge.a));
			}
		}
	}
	
	/** 
	 * 4x: Tests for iterators
	 * 
	 * Dependencies: All non-iterator behavior
	 */

	public void test40() {
		addVertices(g2, "t", "u", "v", "x", "y");
		testIteration(g2.vertexSet(), "t", "u", "v", "x", "y");
		
		while (g2.order() > 0)
			removeOne(g2, false);
	}

	public void test41() {
		addEdges(g2, e("t","u"), e("u","v"), e("t","x"), e("y","u"));
		testIteration(g2.vertexSet(), "t", "u", "v", "x", "y");
		
		while (g2.size() > 0)
			removeOne(g2, false);
	}

	public void test42() {
		addEdges(g2, e("t","u"), e("u","v"));
		testIteration(g2.edgeSet(), e("t","u"), e("u","v"));
		
		while (g2.size() > 0)
			removeOne(g2, true);
	}

	public void test43() {
		addEdges(g2, e("t","u"), e("t","v"), e("u","v"));
		testIteration(g2.edgeSet(), e("t","u"), e("t","v"), e("u","v"));
		
		while (g2.size() > 0)
			removeOne(g2, true);
	}

	public void test44() {
		addVertices(g2, "t", "u", "v", "x", "y");
		Iterator<String> it = g2.vertexSet().iterator();
		
		it.next();
		assertFalse(g2.addVertex("x"));
		assertTrue("graph was not modified", it.hasNext());
		it.next();
		
		g2.addVertex("z");
		testException(() -> it.hasNext(), "iterator is stale, hasNext() should throw", ConcurrentModificationException.class);
		testException(() -> it.next(), "graph was modified, next should throw", ConcurrentModificationException.class);
		testException(() -> it.remove(), "graph was modified, remove should throw", ConcurrentModificationException.class);
		
		Iterator<String> it2 = g2.vertexSet().iterator();
		testException(() -> it2.remove(), "remove before calling next should throw", IllegalStateException.class);
		it2.next();
		it2.remove();
		testException(() -> it2.remove(), "remove twice in a row should throw", IllegalStateException.class);
		
		Iterator<String> it3 = g2.vertexSet().iterator();
		g2.addEdge("t", "u"); // t was previously removed
		testException(() -> it3.next(), "graph was modified, next should throw", ConcurrentModificationException.class);
	}

	public void test45() {
		addEdges(g2, e("t","u"), e("t","v"), e("u","v"));
		Iterator<Edge<String>> it = g2.edgeSet().iterator();
		
		it.next();
		assertFalse(g2.addEdge("u","v"));
		assertTrue("graph was not modified", it.hasNext());
		it.next();
		
		g2.addEdge("t","z");
		testException(() -> it.hasNext(), "graph was modified, hasNext should throw", ConcurrentModificationException.class);
		testException(() -> it.next(), "graph was modified, next should throw", ConcurrentModificationException.class);
		testException(() -> it.remove(), "graph was modified, remove should throw", ConcurrentModificationException.class);
		
		Iterator<Edge<String>> it2 = g2.edgeSet().iterator();
		testException(() -> it2.remove(), "remove before calling next should throw", IllegalStateException.class);
		it2.next();
		it2.remove();
		testException(() -> it2.remove(), "remove twice in a row should throw", IllegalStateException.class);
	}
	
	public void test46() {
		addEdges(g2, e("t","u"), e("t","v"), e("u","v"));
		
		Iterator<Edge<String>> it3 = g2.edgeSet().iterator();
		Edge<String> e1 = it3.next();
		Edge<String> e2 = it3.next();
		
		g2.addVertex("a"); // doesn't affect edges
		
		assertTrue(it3.hasNext());
		
		Edge<String> e3 = it3.next();
		assertFalse(e1.equals(e3));
		assertFalse(e2.equals(e3));
		
		it3.remove();
		assertEquals(2, g2.size());
	}
	
	protected void assertContains(Set<?> set, Object x) {
		if (!set.contains(x)) {
			assertTrue(x + " is not a member of " + set, set.contains(x));
		}
	}
	
	public void test48() {
		
		Set<Edge<String>> edges = new HashSet<>(
				Arrays.asList(e("a","d"), e("b","e"), e("c","f"),
							  e("a","c"), e("b","d"), e("c","e"),
						      e("a","b"), e("b","c"), e("c","d")));
		addEdges(g2, 
				e("a","d"), e("b","e"), e("c","f"),
				e("a","c"), e("b","d"), e("c","e"),
				e("a","b"), e("b","c"), e("c","d"));
		
		Iterator<Edge<String>> it3 = g2.edgeSet().iterator();
		Edge<String> e1 = it3.next(); 
		assertContains(edges,e1);
		
		Edge<String> e2 = it3.next(); 
		assertContains(edges,e2);
		assertFalse("Duplicate: "+e2, e1.equals(e2)); 
		
		Edge<String> e3 = it3.next(); 
		assertContains(edges,e3);
		assertFalse("Duplicate: "+e3, e1.equals(e3)); 
		assertFalse("Duplicate: "+e3, e2.equals(e3)); 

		Edge<String> e4 = it3.next(); 
		assertContains(edges,e4);
		assertFalse("Duplicate: "+e4, e1.equals(e4)); 
		assertFalse("Duplicate: "+e4, e2.equals(e4)); 
		assertFalse("Duplicate: "+e4, e3.equals(e4)); 
		
		Edge<String> e5 = it3.next(); assertContains(edges,e5);
		assertFalse("Duplicate: "+e5, e1.equals(e5)); 
		assertFalse("Duplicate: "+e5, e2.equals(e5)); 
		assertFalse("Duplicate: "+e5, e3.equals(e5)); 
		assertFalse("Duplicate: "+e5, e4.equals(e5)); 
		
		Edge<String> e6 = it3.next(); 
		assertContains(edges,e6);
		assertFalse("Duplicate: "+e6, e1.equals(e6)); 
		assertFalse("Duplicate: "+e6, e2.equals(e6)); 
		assertFalse("Duplicate: "+e6, e3.equals(e6)); 
		assertFalse("Duplicate: "+e6, e4.equals(e6)); 
		assertFalse("Duplicate: "+e6, e5.equals(e6)); 
		
		Edge<String> e7 = it3.next(); 
		assertContains(edges,e7);
		assertFalse("Duplicate: "+e7, e1.equals(e7)); 
		assertFalse("Duplicate: "+e7, e2.equals(e7)); 
		assertFalse("Duplicate: "+e7, e3.equals(e7)); 
		assertFalse("Duplicate: "+e7, e4.equals(e7)); 
		assertFalse("Duplicate: "+e7, e5.equals(e7)); 
		assertFalse("Duplicate: "+e7, e6.equals(e7)); 
		
		Edge<String> e8 = it3.next(); 
		assertContains(edges,e8);
		assertFalse("Duplicate: "+e8, e1.equals(e8)); 
		assertFalse("Duplicate: "+e8, e2.equals(e8)); 
		assertFalse("Duplicate: "+e8, e3.equals(e8)); 
		assertFalse("Duplicate: "+e8, e4.equals(e8)); 
		assertFalse("Duplicate: "+e8, e5.equals(e8)); 
		assertFalse("Duplicate: "+e8, e6.equals(e8)); 
		assertFalse("Duplicate: "+e8, e7.equals(e8)); 

		Edge<String> e9 = it3.next(); assertContains(edges,e9);
		assertFalse("Duplicate: "+e9, e1.equals(e9)); 
		assertFalse("Duplicate: "+e9, e2.equals(e9)); 
		assertFalse("Duplicate: "+e9, e3.equals(e9)); 
		assertFalse("Duplicate: "+e9, e4.equals(e9)); 
		assertFalse("Duplicate: "+e9, e5.equals(e9)); 
		assertFalse("Duplicate: "+e9, e6.equals(e9)); 
		assertFalse("Duplicate: "+e9, e7.equals(e9)); 
		assertFalse("Duplicate: "+e9, e8.equals(e9)); 
		
		assertFalse(it3.hasNext());
	}

	private <T> Edge<T> e(T a, T b) { return new Edge<T>(a,b); }
	
	@SafeVarargs
	private final <T> void addVertices(Graph<T> graph, T... vertices) {
		for (T v: vertices)
			assertTrue("vertex didn't exist in graph: should return true", graph.addVertex(v));
	}
	
	@SafeVarargs
	private final <T> void testVertices(Graph<T> graph, boolean contained, T... vertices) {
		String message = "expected graph to "+(contained?"":"not")+" contain vertex: ";
		for (T vertex: vertices)
			assertEquals(message+vertex, contained, graph.containsVertex(vertex));
	}
	
	@SafeVarargs
	private final <T> void testVertices(Set<T> vertexSet, boolean contained, T... vertices) {
		String message = "expected graph to "+(contained?"":"not")+" contain vertex: ";
		for (T vertex: vertices)
			assertEquals(message+vertex, contained, vertexSet.contains(vertex));
	}
	
	@SafeVarargs
	private final <T> void addEdges(Graph<T> graph, Edge<T>... edges) {
		for (Edge<T> edge: edges)
			assertTrue("vertex didn't exist in graph: should return true", graph.addEdge(edge.a, edge.b));
	}
	
	@SafeVarargs
	private final <T> void testEdges(Graph<T> graph, boolean contained, Edge<T>... edges) {
		String message = "expected graph to "+(contained?"":"not")+" contain edge: ";
		for (Edge<T> edge: edges){
			assertEquals(message+edge, contained, graph.containsEdge(edge.a, edge.b));
			assertEquals(message+edge, contained, graph.containsEdge(edge.b, edge.a));
		}	
	}
	
	@SafeVarargs
	private final <T> void testEdges(Set<Edge<T>> edgeSet, boolean contained, Edge<T>... edges) {
		String message = "expected graph to "+(contained?"":"not")+" contain edge: ";
		for (Edge<T> edge: edges){
			assertEquals(message+edge, contained, edgeSet.contains(e(edge.a, edge.b)));
			assertEquals(message+edge, contained, edgeSet.contains(e(edge.b, edge.a)));
		}	
	}
	
	@SafeVarargs
	private final <T> void testIteration(Set<T> set, T... elements) {
		Iterator<T> actual = set.iterator();
		Set<T> expected = new HashSet<>(Arrays.asList(elements));
		for (int i = 0; i < expected.size(); i++) {
			assertTrue("should have next element", actual.hasNext());
			assertTrue(expected.contains(actual.next()));
		}
		assertFalse("shouldn't have more elements to return", actual.hasNext());
	}
	
	@SuppressWarnings("unchecked")
	private final <T> void removeOne(Graph<T> graph, boolean removeEdge) {
		Supplier<Integer> count = () -> removeEdge ? graph.size() : graph.order();
		Supplier<Set<?>> set = () -> removeEdge ? graph.edgeSet() : graph.vertexSet();
		int prevCount = set.get().size();
		
		Iterator<?> it = set.get().iterator();
		Object removed = null;
		int i;
		for (i = -1; i < r.nextInt(count.get()); ++i)
			removed = it.next();
		it.remove();
		
		assertEquals("graph "+(removeEdge ? "size" : "order")+" should have decremented", prevCount - 1, set.get().size());
		assertEquals((removeEdge ? "edge" : "vertex")+"set size should have decremented", prevCount - 1, set.get().size());
		
		if (removeEdge) {
			Edge<T> e = (Edge<T>) removed;
			assertFalse("removing from set didn't remove from graph: "+e, graph.containsEdge(e.a, e.b));
			assertFalse("removing from set didn't remove from graph: "+e, graph.containsEdge(e.b, e.a));
		}
		else {
			T v = (T) removed;
			assertFalse("removing from set didn't remove from graph: "+v, graph.containsVertex(v));
		}

		if (i < count.get() - 1) {
			for (int j = i; j < count.get(); ++j) {
				assertEquals("expected more elements", j < count.get(), it.hasNext());
				assertFalse("removed element '" + removed + "' was returned in same iterator",
						removed.equals(it.next()));
			}
		}

		it = set.get().iterator();
		for (int j = 0; j < count.get(); ++j) {
			assertTrue("expected more elements", it.hasNext());
			Object current = it.next();
			assertFalse("removed element was returned in next iterator", removed.equals(current));
			if (!removeEdge)
				assertFalse("remove vertex "+removed+" should remove edge "+e(current,removed),
						graph.getConnected((T) current).contains(removed));
		}
	}
	
	private <T> void testException(Runnable throwsException, String description, Class<T> type) {
		try {
			throwsException.run();
			assertFalse(description+"should have thrown exception", true);
		}
		catch (Exception e) {
			String message = description+" threw wrong type of exception: "+e.getClass().getSimpleName();
			assertTrue(message, type.isInstance(e));
		}
	}
}
