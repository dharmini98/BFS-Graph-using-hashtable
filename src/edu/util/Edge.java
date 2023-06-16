package edu.uwm.cs351.util;

/**
 * An undirected edge between two non-null vertices.
 * 
 * The Edge (a,b) is equal to the Edge (b,a).
 * 
 * @param T type of vertices
 */
public class Edge<T> {
	public final T a, b;

	/**
	 * Create an edge between the two vertices
	 * @param a must not be null
	 * @param b must not be null
	 */
	public Edge(T a, T b) {
		if (a == null || b == null) throw new NullPointerException();
		this.a = a;
		this.b = b;
	}

	/**
	 * Check if this edge contains a specific vertex.
	 * 
	 * @param vertex
	 * @return true if this edge contains the vertex
	 */
	public boolean contains(T vertex) {
		return (vertex == null)
				? false
						: vertex.equals(a) || vertex.equals(b);
	}

	public boolean equals(Object o) {
		if (!(o instanceof Edge<?>)) return false;
		Edge<?> other = (Edge<?>) o;
		return eq(a,other.a) && eq(b,other.b) ||
				eq(a,other.b) && eq(b,other.a);
	}

	private static boolean eq(Object o1, Object o2) {
		return o1 == o2 || o1 != null && o1.equals(o2);
	}

	@Override
	public int hashCode() { return a.hashCode() + b.hashCode(); }

	@Override
	public String toString() { return "edge("+a+"-"+b+")"; }
}
