import java.util.Set;

import junit.framework.TestCase;
import edu.uwm.cs351.sim.Host;
import edu.uwm.cs351.sim.Virus;
import edu.uwm.cs351.util.Graph;
import edu.uwm.cs351.util.HashGraph;

public class TestVirus extends TestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	Graph<Host> graph;
	Virus virus;
	Virus superVirus;
	Host[] hosts;
	
	@Override
	protected void setUp()  {
		try {
			assert graph.vertexSet().isEmpty();
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		graph = new HashGraph<>();
		superVirus = new Virus(graph, 100); //virus will infect all hosts it can reach
		hosts = new Host[10];
		for (int i=0; i<10; i++)
			hosts[i] = new Host(i+"", 0);
	}
	
	public void test0() {
		// Single chain of hosts with zero immunity
		for (int i=0; i<9; i++)
			graph.addEdge(hosts[i], hosts[i+1]);

		testPropagation(superVirus.initialInfect(hosts[0]), 0);
		testPropagation(superVirus.propagate(), 1);
		testPropagation(superVirus.propagate(), 2);
		testPropagation(superVirus.propagate(), 3);
		testPropagation(superVirus.propagate(), 4);
		testPropagation(superVirus.propagate(), 5);
		testPropagation(superVirus.propagate(), 6);
		testPropagation(superVirus.propagate(), 7);
		testPropagation(superVirus.propagate(), 8);
		testPropagation(superVirus.propagate(), 9);
		testPropagation(superVirus.propagate());
	}
	
	public void test1() {
		// Circular chain of hosts with zero immunity
		for (int i = 0; i < 9; i++)
			graph.addEdge(hosts[i], hosts[i + 1]);
		graph.addEdge(hosts[0], hosts[9]);

		testPropagation(superVirus.initialInfect(hosts[5]), 5);
		testPropagation(superVirus.propagate(), 4,6);
		testPropagation(superVirus.propagate(), 3,7);
		testPropagation(superVirus.propagate(), 2,8);
		testPropagation(superVirus.propagate(), 1,9);
		testPropagation(superVirus.propagate(), 0);
		testPropagation(superVirus.propagate());
	}
	
	public void test2() {
		// Infect host connected to no others
		for (int i = 0; i < 8; i++)
			graph.addEdge(hosts[i], hosts[i + 1]);
		graph.addEdge(hosts[0], hosts[8]);
		
		graph.addVertex(hosts[9]);

		testPropagation(superVirus.initialInfect(hosts[9]), 9);
		testPropagation(superVirus.propagate());
	}
	
	public void test3() {
		// Infect host connected to all others
		for (int i = 1; i < 10; i++)
			graph.addEdge(hosts[0], hosts[i]);

		testPropagation(superVirus.initialInfect(hosts[0]), 0);
		testPropagation(superVirus.propagate(), 1,2,3,4,5,6,7,8,9);
		testPropagation(superVirus.propagate());
	}

	public void test4() {
		// Radially-increasing immunity; infect center
		hosts = new Host[13];
		hosts[0] = new Host("0",0);
		for (int i=1;i<=3;i++) {
			hosts[i] = new Host(i+"", 10);
			hosts[i+3] = new Host(i+"", 20);
			hosts[i+6] = new Host(i+"", 30);
			hosts[i+9] = new Host(i+"", 31);
			graph.addEdge(hosts[0], hosts[i]);
			graph.addEdge(hosts[i], hosts[i+3]);
			graph.addEdge(hosts[i+3], hosts[i+6]);
			graph.addEdge(hosts[i+6], hosts[i+9]);
		}
		
		virus = new Virus(graph, 30);
		testPropagation(virus.initialInfect(hosts[0]), 0);
		testPropagation(virus.propagate(), 1,2,3);
		testPropagation(virus.propagate(), 4,5,6);
		testPropagation(virus.propagate(), 7,8,9);
		testPropagation(virus.propagate()); // outermost ring is immune
	}
	
	public void test5() {
		// Varying immunity
		hosts = new Host[15];
		hosts[0] = new Host("0", 20);
		hosts[1] = new Host("0", 15);
		hosts[2] = new Host("0", 10);
		hosts[3] = new Host("0", 14);
		hosts[4] = new Host("0", 6);
		hosts[5] = new Host("0", 20);
		hosts[6] = new Host("0", 13);
		hosts[7] = new Host("0", 20);
		hosts[8] = new Host("0", 20);
		hosts[9] = new Host("0", 27);
		hosts[10] = new Host("0", 6);
		hosts[11] = new Host("0", 2);
		hosts[12] = new Host("0", 77);
		hosts[13] = new Host("0", 0);
		hosts[14] = new Host("0", 45);
		
		graph.addEdge(hosts[0], hosts[1]);
		graph.addEdge(hosts[0], hosts[14]);
		graph.addEdge(hosts[1], hosts[2]);
		graph.addEdge(hosts[1], hosts[3]);
		graph.addEdge(hosts[3], hosts[14]);
		graph.addEdge(hosts[3], hosts[5]);
		graph.addEdge(hosts[9], hosts[14]);
		graph.addEdge(hosts[5], hosts[9]);
		graph.addEdge(hosts[5], hosts[7]);
		graph.addEdge(hosts[9], hosts[10]);
		graph.addEdge(hosts[9], hosts[11]);
		graph.addEdge(hosts[2], hosts[4]);
		graph.addEdge(hosts[2], hosts[12]);
		graph.addEdge(hosts[4], hosts[6]);
		graph.addEdge(hosts[6], hosts[8]);
		graph.addEdge(hosts[8], hosts[12]);
		graph.addEdge(hosts[12], hosts[13]);

		virus = new Virus(graph, 30);
		testPropagation(virus.initialInfect(hosts[0]), 0);
		testPropagation(virus.propagate(), 1);
		testPropagation(virus.propagate(), 2, 3);
		testPropagation(virus.propagate(), 4, 5);
		testPropagation(virus.propagate(), 6, 7, 9);
		testPropagation(virus.propagate(), 8, 10, 11);
		testPropagation(virus.propagate());
	}
	
	public void test6() {
		assertException(IllegalArgumentException.class, () -> superVirus.initialInfect(null));
		
		superVirus.initialInfect(new Host("0",0));
		
		assertException(IllegalStateException.class, () -> superVirus.initialInfect(new Host("1",0)));
	}

	private void testPropagation(Set<Host> recentInfections, Integer... ids) {
		for (Integer id: ids) 
			assertTrue("expected "+id+" to be infected", recentInfections.contains(hosts[id]));
		assertFalse("should have more infections", recentInfections.size() < ids.length);
		assertFalse("should have less infections", recentInfections.size() > ids.length);
	}
}
