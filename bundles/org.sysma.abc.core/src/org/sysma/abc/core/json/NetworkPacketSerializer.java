/**
 * 
 */
package org.sysma.abc.core.json;


import java.lang.reflect.Type;

import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.NetworkMessages.NetworkPacket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Yehia Abd Alrahman
 *
 */
public class NetworkPacketSerializer implements JsonSerializer<NetworkPacket> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(NetworkPacket src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
//		json.add("link", context.serialize(src.getIncomingLink()));
		json.add("serverId", context.serialize(src.getServerId()));
		json.add("id", context.serialize(src.getId()));
		json.add("type", context.serialize(src.getType()));
		json.add("packet", context.serialize(src.getPacket()));
		return json;
	}

}
