//Dharmini Saravanan
package edu.uwm.cs351.sim;

/**
 * Represent something that is possibly subject to being infected.
 */
public class Host {
	public final String id;
	public final double immunity;
	private boolean infected;
	
	/**
	 * Create a host with the given name and immunity.
	 * The immunity prevents viruses with low virulence from infecting.
	 * @param ident
	 * @param imm
	 */
	public Host(String ident, double imm) {
		super();
		id = ident;
		immunity = imm;
		infected = false;
	}
	
	/**
	 * The host is presented with a virus with the given infectiousness
	 * (virulence).  If the hosts immunity is insufficient, it is infected and
	 * the method returns true.
	 * @param infectiousness virulence of virus
	 * @return whether the host was infected
	 */
	public boolean infect(double infectiousness) {
		infected = infectiousness >= immunity;
		return infected;
	}
	
	/**
	 * Indicates whether the host is currently infected.
	 * @return whether it is infected.
	 */
	public boolean isInfected() { return this.infected; }
	
	@Override
	public String toString() { return "host("+id+", "+immunity+", "+(infected ? "Infected" : "Healthy")+")"; }
}

