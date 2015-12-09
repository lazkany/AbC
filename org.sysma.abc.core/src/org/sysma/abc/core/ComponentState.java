/**
 * 
 */
package org.sysma.abc.core;

/**
 * @author Yehia Abd Alrahman
 *
 */
public enum ComponentState {
	/**
	 * The context is ready to start. Execution of an agent in a READY context 
	 * is blocked until state RUNNING is reached.
	 */
	READY,
	
	/**
	 * Context is running and all the enclosing agents are involved in 
	 * their computations.
	 */
	RUNNING,
	
	/**
	 * A closing signal has been received. In this state, no action can be performed.
	 */
	CLOSING,
	
	/**
	 * Context execution is terminated.
	 */
	HALT
}