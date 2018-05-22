package bgu.spl181.net.impl;

import java.util.HashMap;


import java.util.Map;

import bgu.spl181.net.impl.Interfaces.Connections;
import bgu.spl181.net.impl.Interfaces.ConnectionHandler;
import bgu.spl181.net.impl.Interfaces.*;

public class ConnectionsImpl<T> implements Connections<T> {
	
	private Map<Integer, ConnectionHandler<T>> compMap;
	
	public ConnectionsImpl() {
		compMap = new HashMap<>();
	}

	@Override
	synchronized public boolean send(int connectionId, T msg) {
		Integer temp = new Integer(connectionId);
		if (compMap.containsKey(temp))
		{
		compMap.get(temp).send(msg);
		return true;
		}
		else
		return false;
	}

	@Override
	synchronized public void broadcast(T msg) {
		for (Integer temp : compMap.keySet())
		{
			compMap.get(temp).send(msg);
		}
	}

	@Override
	synchronized public void disconnect(int connectionId) {
		if (compMap.containsKey(connectionId))
			compMap.remove(connectionId);
	}
	
	synchronized public Map<Integer, ConnectionHandler<T>> getComp ()
	{
		return compMap;
	}
	
	
	
	

}
