/**
 * 
 */
package org.sysma.abc.core;

import java.util.HashMap;
import java.util.Map;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;

/**
 * @author loreti
 *
 */
public class AbCStore {
	
	private Map<String,Object> data;
	
	private Map<String,Attribute<?>> attributes;

	public AbCStore( ) {
		this.data = new HashMap<>();
		this.attributes = new HashMap<>();
	}
	
	
	protected boolean isConsistent( Attribute<?> attribute ) {
		Attribute<?> old = attributes.get(attribute.getName());
		if (old == null) {
			attributes.put(attribute.getName(), attribute);
			return true;
		} 
		return old.equals(attribute);
	}
	
	public <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		Object o = data.get(attribute.getName());
		if (attribute.check(o)) {
			return attribute.castTo( o );
		}
		throw new AbCAttributeTypeException();
	}
	
	/**
	 * @param n
	 * @return an attribute with a name "n"
	 */
	public Attribute<?>  getAttribute(String n){
		return  attributes.get(n);
		
	}
	
	public <T> void setValue( Attribute<T> attribute , T value ) throws AbCAttributeTypeException {
		if (isConsistent(attribute)) {
			data.put(attribute.getName(), value);		
		} else {
			throw new AbCAttributeTypeException();
		}
	}
	@Override
	public String toString() {
		return data.toString();
	}
	
}
