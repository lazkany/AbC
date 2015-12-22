/**
 * 
 */
package org.sysma.abc.core.abcfactoy;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.sysma.abc.core.AbCStore;
import org.sysma.abc.core.Attribute;

import org.sysma.abc.core.json.AttributeDeserializer;
import org.sysma.abc.core.json.AttributeSerializer;
import org.sysma.abc.core.json.StoreDeserializer;
import org.sysma.abc.core.json.StoreSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class AbCFactory {
	private static GsonBuilder builder;

	/**
	 * Creates a new instance of Gson
	 * 
	 * @return a new instance of Gson
	 */
	public static Gson getGSon() {
		if (builder == null) {
			createBuilder();
		}
		return builder.create();
	}

	/**
	 * Creates the GsonBuilder
	 */
	private static void createBuilder() {
		builder = new GsonBuilder();
		//
		// JSon Deserializers
		//
		// builder.registerTypeAdapter(ActualTemplateField.class, new
		// ActualTemplateFieldDeserializer());
		// builder.registerTypeHierarchyAdapter(Address.class, new
		// AddressDeserializer());
		 builder.registerTypeAdapter(Attribute.class, new
		 AttributeDeserializer());
		 builder.registerTypeAdapter(AbCStore.class, new
				 StoreDeserializer());
		// builder.registerTypeAdapter(FormalTemplateField.class, new
		// FormalTemplateFieldDeserializer());
		// builder.registerTypeHierarchyAdapter(jRESPMessage.class, new
		// MessageDeserializer());
		// builder.registerTypeHierarchyAdapter(Template.class, new
		// TemplateDeserializer());
		// builder.registerTypeAdapter(Tuple.class, new TupleDeserializer());
		// builder.registerTypeHierarchyAdapter(GroupPredicate.class, new
		// GroupPredicateDeserializer());
		//
		//
		// //
		// // JSon Serializers
		// //
		// builder.registerTypeAdapter(ActualTemplateField.class, new
		// ActualTemplateFieldSerializer());
		 builder.registerTypeAdapter(Attribute.class, new
		 AttributeSerializer());
		 builder.registerTypeAdapter(AbCStore.class, new
				 StoreSerializer());
		// builder.registerTypeAdapter(FormalTemplateField.class, new
		// FormalTemplateFieldSerializer());
		// builder.registerTypeHierarchyAdapter(Template.class, new
		// TemplateSerializer());
		// builder.registerTypeHierarchyAdapter(Address.class, new
		// AddressSerializer());
		// builder.registerTypeAdapter(Tuple.class, new TupleSerializer());
		// builder.registerTypeHierarchyAdapter(GroupPredicate.class , new
		// GroupPredicateSerializer());
		 
		 // CLASS TYPE ADAPTER and CLASS FACTORY
		 
//		 builder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
//		 builder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
	}

	/**
	 * Register a json serializer/deserializer for type <code>t</code>.
	 * 
	 * @param t
	 *            a type
	 * @param typeAdapter
	 *            adapter that will be used for serialization/deserialization of
	 *            elements that are instances type t
	 */
	public static void registerTypeAdapter(Type t, Object typeAdapter) {
		if (builder == null) {
			createBuilder();
			
		}
		builder.registerTypeAdapter(t, typeAdapter);
	}

	/**
	 * Register a json serializer/deserializer for all the sub-types of
	 * <code>t</code>.
	 * 
	 * @param t
	 *            a type
	 * @param typeAdapter
	 *            adapter that will be used for serialization/deserialization of
	 *            elements that has type <code>t</code>
	 */
	public static void registerTypeHierarchyAdapter(Type t, Object typeAdapter) {
		if (builder == null) {
			createBuilder();
		}
		builder.registerTypeAdapter(t, typeAdapter);
	}

	/**
	 * Find or create a logger for a named subsystem.
	 * 
	 * @param name
	 *            A name for the logger
	 * @return a suitable Logger
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name);
	}
}
