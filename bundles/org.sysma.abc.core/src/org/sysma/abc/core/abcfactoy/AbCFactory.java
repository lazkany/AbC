/**
 * 
 */
package org.sysma.abc.core.abcfactoy;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.sysma.abc.core.AbCComponent;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Attribute;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;
import org.sysma.abc.core.json.AttributeDeserializer;
import org.sysma.abc.core.json.AttributeSerializer;
import org.sysma.abc.core.json.ComponentDeserializer;
import org.sysma.abc.core.json.ComponentSerializer;
import org.sysma.abc.core.json.MessageDeserializer;
import org.sysma.abc.core.json.MessageSerializer;
import org.sysma.abc.core.json.MsgCenterDeserializer;
import org.sysma.abc.core.json.MsgCenterSerializer;
import org.sysma.abc.core.json.NetworkPacketDeserializer;
import org.sysma.abc.core.json.NetworkPacketSerializer;
import org.sysma.abc.core.json.PredicateDeserializer;
import org.sysma.abc.core.json.PredicateSerializer;
import org.sysma.abc.core.json.TupleDeserializer;
import org.sysma.abc.core.json.TupleSerializer;
import org.sysma.abc.core.predicates.AbCPredicate;

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
		builder.registerTypeAdapter(Attribute.class, new AttributeDeserializer<Object>());

		builder.registerTypeAdapter(AbCComponent.class, new ComponentDeserializer());
		builder.registerTypeAdapter(AbCMessage.class, new MessageDeserializer());
		 builder.registerTypeAdapter(AbCPacket.class, new MsgCenterDeserializer());
		 builder.registerTypeAdapter(NetworkPacket.class, new NetworkPacketDeserializer());
		 builder.registerTypeHierarchyAdapter(AbCPredicate.class, new
				 PredicateDeserializer());
		 builder.registerTypeAdapter(Tuple.class, new TupleDeserializer());
		//
		//
		// //
		// // JSon Serializers
		// //
		 builder.registerTypeAdapter(AbCPacket.class, new
				 MsgCenterSerializer());
		builder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		builder.registerTypeAdapter(AbCComponent.class, new ComponentSerializer());
		 builder.registerTypeAdapter(NetworkPacket.class, new
				 NetworkPacketSerializer());
		// builder.registerTypeHierarchyAdapter(Template.class, new
		// TemplateSerializer());
		// builder.registerTypeAdapter(Tuple.class, new TupleSerializer());
		builder.registerTypeAdapter(AbCMessage.class, new MessageSerializer());
		 builder.registerTypeHierarchyAdapter(AbCPredicate.class , new
				 PredicateSerializer());
		 builder.registerTypeAdapter(Tuple.class, new TupleSerializer());

		// CLASS TYPE ADAPTER and CLASS FACTORY

		// builder.registerTypeAdapterFactory(new ClassTypeAdapterFactory());
		// builder.registerTypeAdapter(Class.class, new ClassTypeAdapter());
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
