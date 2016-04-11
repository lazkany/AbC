/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.grpPredicate.GroupPredicate;
import org.sysma.abc.core.grpPredicate.Or;

/**
 * @author Yehia Abd Alrahman
 *
 */
public abstract class NonDeterminism extends AbCProcess {

	private Map<GroupPredicate, AbCProcess> choice = new HashMap<>();;
	private GroupPredicate Predicate;

	/**
	 * 
	 */

	public void SetProcess(Map<GroupPredicate, AbCProcess> x) {
		// TODO Auto-generated constructor stub
		Queue<GroupPredicate> pred = new LinkedList<>();
		for (GroupPredicate p : x.keySet()) {
			choice.put(p, x.get(p));
			pred.add(p);
		}
		Predicate = new Or(pred.poll(), pred.poll());
		while (!pred.isEmpty()) {
			Predicate = new Or(Predicate, pred.poll());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sysma.abc.core.AbCProcess#doRun()
	 */

	public synchronized void resolve() throws InterruptedException, AbCAttributeTypeException {
		Object xObject = receive(Predicate, null);
		for (GroupPredicate p : choice.keySet()) {
			if (p.evaluate(xObject)) {
				choice.get(p).setRecValue(xObject);
				exec(choice.get(p));
				break;
			}
		}

	}

}
