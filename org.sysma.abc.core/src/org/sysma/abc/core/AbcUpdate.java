/**
 * 
 */
package org.sysma.abc.core;

import java.util.Map;

/**
 * @author Yehia Abd Alrahman
 *
 */
public interface AbcUpdate {

	/**
	 * 
	 */
	public Map<Attribute<?>,Object> eval( Object o );

}
