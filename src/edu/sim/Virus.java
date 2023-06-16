//Dharmini Saravanan
package edu.uwm.cs351.sim;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import edu.uwm.cs351.util.Graph;

/**
 * Model an infectious agent that attacks a network of
 * connected hosts.
 */
public class Virus {
	private Graph<Host> population;
	private final double infectiousness;
	private Set<Host> seen = new HashSet<>();
	private Set<Host> latest;
	private static final int MIN = 0, MAX = 100;
	
	public Virus(Graph<Host> pop, double infect) {
		if (pop == null)
			throw new IllegalArgumentException("population cannot be null");
		if (infect < MIN || infect > MAX)
			throw new IllegalArgumentException("infectiousness must be in range ["+MIN+","+MAX+"]");
		population = pop;
		infectiousness = infect;
	}
	
	/**
	 * Perform an initial infection on the given host.
	 * Set the latest set to include this host if it was infected,
	 * and otherwise to be empty.  In any case, this host has been seen.
	 * @param host initial host to infect
	 * @return set containing infected initial host (if infected)
	 * 			otherwise empty set
	 * @throws IllegalArgumentException if host is null
	 * @throws IllegalStateException if called more than once
	 */
	public Set<Host> initialInfect(Host host) {
		// TODO: Implement this method.
		//		You may not add any fields to virus.
		if(host==null)throw new IllegalArgumentException("The host is null");
		if(seen.contains(host))throw new IllegalStateException("This host has been seen");
		latest=new HashSet<>();
		boolean check=host.infect(infectiousness);
		if(check)
			latest.add(host);
		seen.add(host);
		return latest;
	}
	
	/**
	 * Attempt to infect any previously-unseen hosts that are connected to the hosts infected
	 * in the last round of propagation (latest). 
	 * If they are infected add them to the set of new infections to be returned.
	 * That set is the new latest set.
	 * Whether or not they were infected, they have now been seen and you should not try
	 * to infect them again.
	 * 
	 * @return set containing any new hosts that have been infected
	 */
	
	public Set<Host> propagate() {
		// TODO: Implement this method.
		Set<Host> newest=new HashSet<>();
		for(Host recent:latest)
		{
			Set<Host>nextHosts=population.getConnected(recent);
		
		    for(Host next:nextHosts)
			if(seen.add(next))
			{
				if(next.infect(infectiousness))
					newest.add(next);
			}
		}
		latest=newest;
	return latest;
	}
}
