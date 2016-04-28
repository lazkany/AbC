/**
 * 
 */
package org.sysma.abc.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.sysma.abc.core.exceptions.AbCAttributeTypeException;
import org.sysma.abc.core.predicates.AbCPredicate;

/**
 * @author loreti
 *
 */
public class AbCEnvironment implements Serializable {

	private HashMap<String, Object> data;

	private HashMap<String, Attribute<?>> attributes;

	/**
	 * @return the attributes
	 */
	public Map<String, Attribute<?>> getAttributes() {
		return attributes;
	}

	public AbCEnvironment(Map<String, Object> data, Map<String, Attribute<?>> attributes) {
		this.data = new HashMap<>(data);
		this.attributes = new HashMap<>(attributes);
	}

	public AbCEnvironment() {
		this.data = new HashMap<>();
		this.attributes = new HashMap<>();
	}

	protected boolean isConsistent(Attribute<?> attribute) {
		Attribute<?> old = attributes.get(attribute.getName());
		if (old == null) {
			attributes.put(attribute.getName(), attribute);
			return true;
		}
		return old.equals(attribute);
	}

	public synchronized <T> T getValue(Attribute<T> attribute) throws AbCAttributeTypeException {
		Object o = data.get(attribute.getName());
		if (o == null) {
			return null;
		}
		if (attribute.check(o)) {
			return attribute.castTo(o);
		}
		throw new AbCAttributeTypeException();
	}
	
	public Object getValue(String attribute) {
		Object o = data.get(attribute);
		if (o!=null) {
			return o;
		}
		return null;
		
	}

	/**
	 * @param n
	 * @return an attribute with a name "n"
	 */
	public Attribute<?> getAttribute(String n) {
		return attributes.get(n);

	}

	public synchronized void setValue(Attribute<?> attribute, Object value)  throws AbCAttributeTypeException {
		_setValue( attribute , value );
		notifyAll();
	}
	
	private void _setValue(Attribute<?> attribute, Object value) throws AbCAttributeTypeException {
		if (!attribute.isValidValue(value)) {
			throw new AbCAttributeTypeException();
		}
		if (isConsistent(attribute)) {
			data.put(attribute.getName(), value);
		} else {
			throw new AbCAttributeTypeException();
		}
	}

	public void setValue(String attribute, Object value) throws AbCAttributeTypeException {
		if (!attributes.get(attribute).isValidValue(value)) {
			throw new AbCAttributeTypeException();
		}
		if (isConsistent(attributes.get(attribute))) {
			data.put(attribute, value);
		} else {
			throw new AbCAttributeTypeException();
		}
	}

	@Override
	public String toString() {
		return data.toString();
	}

	public synchronized boolean satisfy( AbCPredicate p ) throws AbCAttributeTypeException {
		return p.isSatisfiedBy( this );
	}
	
	public synchronized boolean waitUntil( AbCPredicate p ) throws AbCAttributeTypeException, InterruptedException {
		while (!p.isSatisfiedBy(this)) {
			wait();
		}
		return true;
	}

	public synchronized void update(AbCEnvironment update) throws AbCAttributeTypeException {
		for (Attribute<?> a : update.attributes.values()) {
			_setValue(a, update.getValue(a));
		}
		notifyAll();
	}
}
