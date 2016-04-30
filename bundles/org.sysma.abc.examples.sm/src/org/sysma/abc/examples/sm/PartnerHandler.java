/**
 * 
 */
package org.sysma.abc.examples.sm;

import java.util.LinkedList;

import org.sysma.abc.core.AbCProcess;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.Not;

/**
 * @author loreti
 *
 */
public class PartnerHandler extends AbCProcess {

	private Integer partner;

	public PartnerHandler( Integer partner ) {
		super();
		this.partner = partner;
	}

	/* (non-Javadoc)
	 * @see org.sysma.abc.core.AbCProcess#doRun()
	 */
	@Override
	protected void doRun() throws Exception {
		waitUnil(new Not(new HasValue(Environment.partnerAttribute.getName(), this.partner)) );
		send( new HasValue(Environment.idAttribute.getName(), this.partner),new Tuple("INVALID"));
	}

}
