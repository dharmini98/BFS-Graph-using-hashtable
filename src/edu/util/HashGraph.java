//Dharmini Saravanan
package edu.uwm.cs351.util;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

import junit.framework.TestCase;

/**
 * An undirected graph structure.
 * The vertices may be from any class (but must not be null).
 * Adding an edge implicitly adds the vertices if needed.
 * @param T type of the vertices
 */
public class HashGraph<T> implements Graph<T> {

	private Map<T,Set<T>> edges;
	private int edgesVersion; // updated when number of edges changes
	private int verticesVersion; // updated when number of vertices changes
	private int size;

	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: " + s);
	
	private boolean report(String s) {
		reporter.accept(s);
		return false;
	}

	private boolean wellFormed() {
		// Invariant
		// 1. Edges map cannot be null
		// 2. Every entry in edges map has a non-null set value
		// 3. No vertex has an edge to itself
		// 4. Every edge is represented twice in edges map
		//		e.g. If edge (a-b) exists then it must also exist as (b-a)
		// 5. size contains the number of unique edges in the graph
		//		NB: edge (a-b) and edge (b-a) represent a single edge.
		// TODO
		if(edges==null)return report("Edges map is null");
		for(Iterator<T>itr=edges.keySet().iterator();itr.hasNext();)
		{
			T vertice=itr.next();
			if(edges.get(vertice)==null)
				return report("Null set in entry");
			
		}
		int checkSize=0;
		for(Iterator<T>itr=edges.keySet().iterator();itr.hasNext();)
		{
			T vertice=itr.next();
		
			if(edges.get(vertice).contains(vertice))
					return report("Vertex has an edge to itself");
			Set<T>s=edges.get(vertice);
			for(Iterator<T> it = s.iterator(); it.hasNext();)
			{
				T elem=it.next();
				if(!edges.get(elem).contains(vertice))
					return report("No reverse edge");
				checkSize++;
			}
		}
		if((checkSize/2)!=size)
			return report("The size is not correct");
		return true;
	}

	/**
	 * Create a new empty graph.
	 */
	public HashGraph() {
		// TODO
		edges=new HashMap<T,Set<T>>();
		size=0;
		edgesVersion=1;
		verticesVersion=1;
		this.edgeSet=edgeSet();
		this.vertexSet=vertexSet();
	}

	//Don't remove - used for invariant tests
	private HashGraph(boolean ignored) {}

	@Override
	public int order() {
		//return -1; // TODO
		return vertexSet().size();
	}

	@Override
	public int size() {
		//return -1; // TODO
		return size;
	}

	@Override
	public boolean addVertex(T x) {
		assert wellFormed() : "invariant broken at start of addVertex(" + x + ")";
		boolean result = false;
		// TODO: Implement this method.
		if(x == null) throw new IllegalArgumentException();
		if(!edges.containsKey(x)) {
			result = true;
			edges.put(x, new HashSet<T>());
			
			verticesVersion += 1;
		}
		assert wellFormed() : "invariant broken at end of addVertex(" + x + ")";
		return result;
	}

	@Override
	public boolean containsVertex(T x) {
		assert wellFormed() : "Invariant broken at start of containsVertex(" + x + ");";
		// TODO: Implement this method.
		return edges.containsKey(x);
	}

	/**
	 * Optional helper method so that code can be shared between 
	 * {@link #removeVertex(Object)} and {@link MyVertexIterator#remove()}.
	 * This method cleans up any edges referring to this node, and updates size accordingly.
	 * @param a vertex that was removed
	 * @param formerNeighbors vertices formerly reachable in one step from the removed vertex
	 */
	private void removeVertexHelper(T a, Set<T> formerNeighbors) {
		// TODO: Optional
		for(Iterator<T>itr=formerNeighbors.iterator();itr.hasNext();)
		{
			T elem=itr.next();
			Set<T> s=edges.get(elem);
			s.remove(a);
			edges.put(elem, s);
			size--;
			edgesVersion++;
		}
	}

	@Override
	public boolean removeVertex(T a) {
		assert wellFormed() : "invariant broken at start of removeVertex(" + a + ")";
		boolean result = false;
		// TODO: Implement this method.
		if(edges.containsKey(a))
		{
			result=true;
			removeVertexHelper(a,edges.get(a));
			edges.remove(a);
			verticesVersion++;
		}
		assert wellFormed() : "invariant broken at end of removeVertex(" + a + ")";
		return result;
	}

	@Override
	public boolean addEdge(T a, T b) {
		assert wellFormed() : "invariant broken at start of addEdge(" + a + "," + b + ")";
		boolean result = false;
		// TODO: Implement this method.
        if(a==null||b==null)throw new IllegalArgumentException("Vertex is null");
        if(a.equals(b))throw new IllegalArgumentException("Vertices are the same");
        Set<T>tempa=edges.get(a);
        Set<T>tempb=edges.get(b);
        if(tempa == null) {
			tempa = new HashSet<T>();
			verticesVersion++;
		}
		if(tempb == null) {
			tempb = new HashSet<T>();
			verticesVersion++;
		}
		if(!tempa.contains(b)){
        tempa.add(b);
        tempb.add(a);
        edges.put(a, tempa);
        edges.put(b, tempb);
        result=true;
        size++;
        edgesVersion++;
		}
		assert wellFormed() : "invariant broken at end of addEdge(" + a + "," + b + ")";
		return result;
	}

	@Override
	public boolean removeEdge(T a, T b) {
		assert wellFormed() : "invariant broken at start of removeEdge(" + a + "," + b + ")";
		boolean result = false;
		// TODO
		Set<T>seta=edges.get(a);
		Set<T>setb=edges.get(b);
		if(seta!=null && setb!=null)
		{
		if(seta.contains(b))
		{
		seta.remove(b);
		setb.remove(a);
		edges.put(a, seta);
		edges.put(b, setb);
		size--;
		result=true;
		edgesVersion++;
		}
		}
		assert wellFormed() : "invariant broken at end of removeEdge(" + a + "," + b + ")";
		return result;		
	}

	@Override
	public boolean containsEdge(T a, T b) {
		assert wellFormed() : "invariant broken at start of containsEdge(" + a + "," + b + ")";
		// TODO
		if(edges.containsKey(a)&&edges.get(a).contains(b))
		{
			if(edges.containsKey(b)&&edges.get(b).contains(a))
				return true;
		}
		
		return false;
	}

	@Override
	public Set<T> getConnected(T a) {
		assert wellFormed() : "invariant broken at start of getConnected(" + a + ")";
		// TODO: Implement this method.
		//	NB: You may find the java.util.Collections class helpful for
		// the various unmodifiable collection wrappers.
		if(a==null)return null;
		if(edges.get(a)==null)return null;
		return Collections.unmodifiableSet(edges.get(a));
	}


	private Set<T> vertexSet;

	@Override
	public Set<T> vertexSet() {
		if (vertexSet == null) vertexSet = new MyVertexSet();
		return vertexSet;
	}

	private class MyVertexSet extends AbstractSet<T> {

		@Override
		public int size() {
			assert wellFormed() : "invariant broken at start of size()";
			return edges.size();
		}

		@Override
		public boolean add(T x) {
			assert wellFormed() : "invariant broken at start of add(" + x + ")";
			return addVertex(x);
		}

		@Override
		public boolean contains(Object o) {
			assert wellFormed() : "invariant broken at start of contains(" + o + ")";
			return edges.containsKey(o);
		}

		@Override
		public boolean remove(Object o) {
			assert wellFormed() : "invariant broken at start of contains(" + o + ")";
			@SuppressWarnings("unchecked")
			T v = (T) o;
			return removeVertex(v);
		}

		public Iterator<T> iterator() {
			assert wellFormed() : "invariant broken at start of iterator()";
			return new MyVertexIterator();
		}

		private class MyVertexIterator implements Iterator<T> {
			private Iterator<T> iterator = edges.keySet().iterator();
			private T current;
			private int myVersion = verticesVersion;
			

			// TODO: Implement this class.
			// Unfortunately, remove() cannot call
			// removeVertex() (optional exercise: Why not?).
			// If an edge is added while iterating over vertices, the
			// vertex iterator shouldn't throw a CME (unless adding the edge
			// necessitated adding a vertex as well).

			protected void checkVersion() {
				if (verticesVersion != myVersion) throw new ConcurrentModificationException("stale");
			}
			
			public boolean hasNext() {
				// TODO: Implement this method.
				checkVersion();
				return iterator.hasNext();
			}

			public T next() {
				// TODO: Implement this method.
				checkVersion();
				current=null;
				if(iterator.hasNext())
					current=iterator.next();
				return current;
			}

			public void remove() {
				// TODO: Implement this method.
				checkVersion();
				removeVertexHelper(current,edges.get(current));
				iterator.remove();
				edges.remove(current);
				verticesVersion++;
				myVersion++;
			}
		}
	}

	private Set<Edge<T>> edgeSet;

	@Override
	public Set<Edge<T>> edgeSet() {
		if (edgeSet == null) edgeSet = new MyEdgeSet();
		return edgeSet;
	}
	
	private class MyEdgeSet extends AbstractSet<Edge<T>> {
		public int size() {
			return HashGraph.this.size();
		}

		@Override
		public boolean add(Edge<T> e) {
			return addEdge(e.a,e.b);
		}

		@Override
		public boolean contains(Object o) {
			assert wellFormed() : "invariant broken at start of contains(" + o + ")";
			// First check that we actually have an edge object
			// (The ? means we don't care what the type is.)
			if (!(o instanceof Edge<?>)) return false;
			Edge<?> e = (Edge<?>)o;
			// make use of the fact that Map.get handles any type
			Set<?> s = edges.get(e.a);
			return s != null && s.contains(e.b);
		}

		@Override
		@SuppressWarnings("unchecked")
		public boolean remove(Object o) {
			assert wellFormed() : "invariant broken at start of remove(" + o + ")";
			if (!contains(o)) return false; 
			Edge<T> edge = (Edge<T>)o;
			return removeEdge(edge.a, edge.b);
		}

		public Iterator<Edge<T>> iterator() {
			assert wellFormed() : "invariant broken at start of iterator()";
			return new MyEdgeIterator();
		}

		private class MyEdgeIterator implements Iterator<Edge<T>> {
			private Iterator<Map.Entry<T, Set<T>>> outer = edges.entrySet().iterator();
			private Iterator<T> inner = Collections.emptyIterator();
			private Set<T> seen = new HashSet<T>(); // vertices previously handled
			private int myVerticesVersion = verticesVersion;
			private int myVersion = edgesVersion;
			private int remaining = size; // number of edges still to return
			private T a, b; // latest vertices connected by an edge returned by next()

			// DESIGN:
			// We have an outer iterator that goes though the vertices
			// in the graph at the time the iterator was created,
			// and then an inner iterator that goes through
			// adjacent vertices in the set in the entry.
			//
			// The outer iterator can go stale if the number of vertices
			// changes, even if the number of edges does *not* change.
			// When that happens, we re-initialize the outer iterator,
			// which might then repeat vertices we've already seen.
			// So we keep a set of vertices that have already been seen.
			//
			// And we don't want to return the same edge twice,
			// which could easily happen (once in each direction).  So the
			// seen set has another use, we only return an edge whose second
			// vertex has already been seen.
			//
			// We also remember the last edge returned by next, which
			// helps for remove(), to handle the dual edge entry.
			// (If we're removing a -> b, we have to also remove b -> a.)

			private boolean wellFormed() {
				if (!HashGraph.this.wellFormed()) return false;
				if (myVersion == edgesVersion) {
					if (outer == null)
						return report("iterator outer is null");
					if (inner == null)
						return report("iterator inner is null");
					if (remaining < 0 || remaining > size)
						return report("remaining bad: " + remaining);
				}
				return true;
			}

			private void checkVersion() {
				if (myVersion != edgesVersion)
					throw new ConcurrentModificationException("versions mismatch");
				if (myVerticesVersion != verticesVersion) {
					// update the outer iterator
					outer = edges.entrySet().iterator();
					myVerticesVersion = verticesVersion;
				}
			}

			MyEdgeIterator() {
				assert wellFormed() : "invariant broken at the iterator constructor";
			}

			public boolean hasNext() {
				return(remaining>0);
				// TODO  (easy!)
			}

			public Edge<T> next() {
				assert wellFormed() : "invariant broken at the start of next()";
				// TODO: Implement this method.  (Not easy)
				boolean flag=false;
				while(outer.hasNext())
				{
					Map.Entry<T, Set<T>> vertice=outer.next();
					if(seen.contains(vertice.getKey()))
					  continue;
					seen.add(vertice.getKey());
					  inner=vertice.getValue().iterator();
						while(inner.hasNext())
						{
							T elem=inner.next();
							if(seen.contains(elem)) {
								a = vertice.getKey();
								b = elem;
								flag = true;
								break;
						}
					
				  }
				if(flag)
					break;
				}
				remaining--;
				assert wellFormed() : "invariant broken at end of next()";
				return new Edge<T>(a, b);
			}

			public void remove() {
				assert wellFormed() : "invariant broken at the start of remove()";
				// TODO: Implement this method. (just 6 lines)
				// NB: don't call removeEdge or you will get a CME in remove tests.
				Set<T>tempa=edges.get(a);
				Set<T>tempb=edges.get(b);
				if(!tempa.contains(b))
					throw new IllegalArgumentException();
				tempa.remove(b);
				tempb.remove(a);
				edges.put(a, tempa);
				edges.put(b, tempb);
				size--;
				edgesVersion++;
				myVersion++;
				assert wellFormed() : "invariant broken at the end of remove()";
			}
		}
	}
	
	public static class TestInvariant extends TestCase {
		private static String lastMessage;
		private static Consumer<String> saveMessage = (s) -> lastMessage = s;
		private static Consumer<String> errorMessage = (s) -> System.err.println("Erroneously reported problem: " + (lastMessage = s));

		private HashGraph<Integer> self;

		@Override
		protected void setUp() {
			self = new HashGraph<>(false);
		}

		protected void assertWellFormed(boolean expected) {
			reporter = expected ? errorMessage : saveMessage;
			lastMessage = null;
			assertEquals(expected, self.wellFormed());
			reporter = saveMessage;
			if (expected == false) {
				assertTrue("Didn't report problem", lastMessage != null && lastMessage.trim().length() > 0);
			}
		}

		public void testA() {
			self.edges = new TreeMap<>();
			assertWellFormed(true);
		}
		
		public void testB() {
			self.edges = null;
			assertWellFormed(false);
		}
		
		public void testC() {
			self.edges = new HashMap<>();
			self.edges.put(1, new TreeSet<>());
			assertWellFormed(true);
		}
		
		public void testD() {
			self.edges = new HashMap<>();
			self.edges.put(1, null);
			assertWellFormed(false);
		}
		
		public void testE() {
			self.edges = new HashMap<>();
			self.size = 1;
			assertWellFormed(false);
		}
		
		public void testF() {
			self.edges = new HashMap<>();
			self.edges.put(1, new TreeSet<>());
			self.size = 1;
			assertWellFormed(false);
		}
		
		public void testG() {
			self.edges = new HashMap<>();
			self.edges.put(1, new TreeSet<>(Collections.singleton(1)));
			self.size = 1;
			assertWellFormed(false);
		}
		
		public void testH() {
			self.edges = new HashMap<>();
			self.edges.put(1, new HashSet<>(Collections.singleton(2)));
			self.size = 1;
			assertWellFormed(false);
		}
		
		public void testI() {
			self.edges = new HashMap<>();
			self.edges.put(1, new TreeSet<>(Collections.singleton(2)));
			self.edges.put(2, new HashSet<>(Collections.singleton(1)));
			self.size = 1;
			assertWellFormed(true);
		}
		
		public void testJ() {
			testI();
			self.size = 2;
			assertWellFormed(false);
			self.size = 0;
			assertWellFormed(false);
		}
		
		public void testK() {
			testI();
			self.edges.put(0, Collections.emptySet());
			self.edges.put(3, new HashSet<>());
			assertWellFormed(true);
		}
		
		public void testL() {
			self.edges = new HashMap<>();
			self.edges.put(1, new HashSet<>(Collections.singleton(2)));
			self.edges.put(2, new HashSet<>(Collections.singleton(3)));
			self.size = 1;
			assertWellFormed(false);
			self.size = 2;
			assertWellFormed(false);
		}
		
		public void testM() {
			self.edges = new HashMap<>();
			self.edges.put(1, new TreeSet<>(Collections.singleton(2)));
			self.edges.put(2, new TreeSet<>(Arrays.asList(1,2)));
			self.size = 1;
			assertWellFormed(false);
			self.size = 2;
			assertWellFormed(false);
		}
		
		public void testN() {
			self.edges = new TreeMap<>();
			self.edges.put(1, new TreeSet<>(Collections.singleton(2)));
			self.edges.put(2, new HashSet<>(Collections.singleton(1)));
			self.edges.put(3, null);
			self.size = 1;
			assertWellFormed(false);
			self.size = 2;
			assertWellFormed(false);
		}
		
		public void testO() {
			self.edges = new TreeMap<>();
			self.edges.put(1, new TreeSet<>(Arrays.asList(2,3)));
			self.edges.put(2, new HashSet<>(Arrays.asList(1)));
			self.edges.put(3, new TreeSet<>(Arrays.asList(1)));
			self.size = 2;
			assertWellFormed(true);
			self.size = 3;
			assertWellFormed(false);
			self.size = 2;
			self.edges.get(3).clear();
			self.edges.put(3, null);
			assertWellFormed(false);
		}
	}
}
