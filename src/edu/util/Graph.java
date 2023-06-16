package edu.uwm.cs351.util;

import java.util.Set;

/**
 * An undirected graph.
 * @param T type of vertices.
 */
public interface Graph<T> {

	/**
	 * Return the number of vertices in the graph.
	 * @return number of vertices in the graph.
	 */
	int order();

	/**
	 * Return the number of edges in the graph.
	 * @return number of edges in the graph.
	 */
	int size();

	/**
	 * Add a vertex to the graph if it is not already in the graph.
	 * A newly added vertex will not be connected to anything.  If the value
	 * is already a vertex in the graph, do nothing and return false.
	 * 
	 * @param vertex vertex to add
	 * @return true if vertex was added
	 * @throws IllegalArgumentException if vertex parameter is null
	 */
	boolean addVertex(T x);

	/**
	 * Return true if the argument is a vertex of the graph.
	 * NB: It is possible that a vertex in the graph is not
	 * 		connected to any other vertex.
	 * 
	 * @param vertex vertex to check for
	 * @return true if vertex exists in the graph
	 */
	boolean containsVertex(T x);

	/**
	 * Remove vertex from graph and all edges connected to it. If the value
	 * was not a vertex in the graph, do nothing and return false.
	 * 
	 * @param vertex vertex to remove
	 * @return true if vertex was removed
	 */
	boolean removeVertex(T a);

	/**
	 * Add an edge to the graph.  If necessary the vertices that serve as
	 * its end points are added as vertices in the graph first.  If the edge
	 * is already present in the graph, do nothing and return false.
	 * 
	 * @param a vertex, must not be null
	 * @param b vertex, must not be null and must not be equal to a
	 * @return true if edge was added
	 * @throws IllegalArgumentException if either a or b vertex is null
	 * @throws IllegalArgumentException if a and b are the same vertex
	 */
	boolean addEdge(T a, T b) throws IllegalArgumentException;

	/**
	 * Remove an edge from the graph.
	 * If the edge is not in the graph, do nothing and return false.
	 * 
	 * @param a vertex (may be null)
	 * @param b vertex (may be null)
	 * @return true if edge was removed
	 */
	boolean removeEdge(T a, T b);

	/**
	 * Return whether the edge is present in the graph.
	 * 
	 * @param a vertex
	 * @param b vertex
	 * @return true if the edge exists in the graph
	 */
	boolean containsEdge(T a, T b);

	/**
	 * Return a set of vertices that the argument vertex is connected to by edges.
	 * The result is null if and only if the argument is not a vertex of the graph.
	 * The set should not be modifiable and its contents are undefined if the graph is modified.
	 * @param vertex of which to get set of connected vertices, must not be null
	 * @return unmodifiable set of vertices reached by one edge from the argument.
	 */
	Set<T> getConnected(T a);

	/** Return the set of vertices of this graph.
	 * The result is a set backed by the graph: any change in the set
	 * or graph will be reflected in the other.
	 * @return set of vertices in the graph (never null)
	 */
	Set<T> vertexSet();

	/**
	 * Return the set of edges of this graph.
	 * The result is a set backed by the graph: any change in the
	 * set or graph will be reflected in the other.
	 * @return set of edges in the graph. (never null)
	 */
	Set<Edge<T>> edgeSet();

}