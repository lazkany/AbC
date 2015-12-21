/**
 * 
 */
package org.sysma.abc.core.abcfactoy;



import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class ClassTypeAdapterFactory implements TypeAdapterFactory{
	@Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if(!Class.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        return (TypeAdapter<T>) new ClassTypeAdapter();
    }
	

}
