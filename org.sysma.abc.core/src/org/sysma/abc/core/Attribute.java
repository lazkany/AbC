/**
 * 
 */
package org.sysma.abc.core;

/**
 * @author loreti
 *
 */
public class Attribute<T> {

	private String name;
	
	private Class<T> clazz;
	
	public Attribute( String name , Class<T> clazz ) {
		this.name = name;
		this.clazz = clazz;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<T> getAttributeType( ) {
		return clazz;
	}
	
	public boolean check( Object o ) {
		return clazz.isInstance(o);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Attribute<?>) {
			Attribute<?> other = (Attribute<?>) obj;
			return this.name.equals(other.name)&&this.clazz.equals(other.clazz);
		}
		return false;
	}

	@Override
	public String toString() {
		return name+"<"+clazz.getCanonicalName()+">";
	}

	public T castTo(Object o) {
		return clazz.cast(o);
	}

	public boolean isValidValue(Object value) {
		return clazz.isInstance(value);
	}
	
	
	
}
