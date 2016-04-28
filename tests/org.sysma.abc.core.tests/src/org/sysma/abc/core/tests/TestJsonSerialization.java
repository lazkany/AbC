package org.sysma.abc.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sysma.abc.core.AbCMessage;
import org.sysma.abc.core.Tuple;
import org.sysma.abc.core.NetworkMessages.AbCPacket;
import org.sysma.abc.core.abcfactoy.AbCFactory;
import org.sysma.abc.core.predicates.AbCPredicate;
import org.sysma.abc.core.predicates.And;
import org.sysma.abc.core.predicates.FalsePredicate;
import org.sysma.abc.core.predicates.HasValue;
import org.sysma.abc.core.predicates.IsGreaterOrEqualThan;
import org.sysma.abc.core.predicates.IsGreaterThan;
import org.sysma.abc.core.predicates.IsLessOrEqualThan;
import org.sysma.abc.core.predicates.IsLessThan;
import org.sysma.abc.core.predicates.Not;
import org.sysma.abc.core.predicates.TruePredicate;

import com.google.gson.Gson;

public class TestJsonSerialization {

	@Test
	public void testSerializeTrue() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new TruePredicate();
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testSerializeFalse() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new FalsePredicate();
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}
	
	@Test
	public void testHasValue() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new HasValue("test",34);
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testLessThan() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new IsLessThan("test",34);
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testLessOrEqualThan() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new IsLessOrEqualThan("test",34);
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testGreaterThan() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new IsGreaterThan("test",34);
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testGreaterOrEqualThan() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new IsGreaterOrEqualThan("test",34);
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testNot() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new Not( new IsGreaterOrEqualThan("test",34) );
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testAnd() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new And( new Not( new IsGreaterOrEqualThan("test",34) ) , new HasValue("test","34") );
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}

	@Test
	public void testOr() {
		Gson gson = AbCFactory.getGSon();
		AbCPredicate p = new And( new Not( new IsGreaterOrEqualThan("test",34) ) , new HasValue("test","34") );
		String data = gson.toJson(p);
		AbCPredicate q =  gson.fromJson(data, AbCPredicate.class);
		assertEquals(p, q);
	}
	
	@Test
	public void testAbCMessage() {
		Gson gson = AbCFactory.getGSon();
		AbCMessage m1 = new AbCMessage( (Integer) 10 , new And( new Not( new IsGreaterOrEqualThan("test",34) ) , new HasValue("test","34") ) );
		String data = gson.toJson(m1);
		AbCMessage m2 =  gson.fromJson(data, AbCMessage.class);
		assertEquals(m1, m2);
	}
	
	@Test 
	public void testAbCPacket() {
		Gson gson = AbCFactory.getGSon();
		AbCMessage m1 = new AbCMessage( "This is a test!" , new And( new Not( new IsGreaterOrEqualThan("test",34) ) , new HasValue("test","34") ) );
		AbCPacket p1 = new AbCPacket( m1 , "asender" );
		String data = gson.toJson(p1);
		AbCPacket p2 =  gson.fromJson(data, AbCPacket.class);
		assertEquals(p1, p2);
	}

	@Test
	public void testAbCMessageWithTuple() {
		Gson gson = AbCFactory.getGSon();
		AbCMessage m1 = new AbCMessage( new Tuple( "ciao" , (Integer) 10 ) , new And( new Not( new IsGreaterOrEqualThan("test",34) ) , new HasValue("test","34") ) );
		String data = gson.toJson(m1);
		AbCMessage m2 =  gson.fromJson(data, AbCMessage.class);
		assertEquals(m1, m2);
	}

	
}
