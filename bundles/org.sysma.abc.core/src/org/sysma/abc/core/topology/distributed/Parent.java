/**
 * 
 */
package org.sysma.abc.core.topology.distributed;

/**
 * @author Yehia Abd Alrahman
 *
 */
import java.util.Map;

public class Parent<K, V> implements Map.Entry<K, V>
{
    private K key;
    private V value;

    public Parent(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    public K getKey()
    {
        return this.key;
    }

    public V getValue()
    {
        return this.value;
    }

    public K setKey(K key)
    {
        return this.key = key;
    }

    public V setValue(V value)
    {
        return this.value = value;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Parent [key=" + key + ", value=" + value + "]";
	}
    
}
