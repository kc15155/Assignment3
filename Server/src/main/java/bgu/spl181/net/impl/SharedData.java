package bgu.spl181.net.impl;

import java.util.HashMap;
import java.util.Map;

public class SharedData {
	
	private Map<Integer, String> userMap;
	
	public SharedData ()
	{
		userMap = new HashMap<>();
	}
	
	public Map<Integer, String> getUsers ()
	{
		return userMap;
	}
	
	synchronized public boolean isLoggedIn (int conId)
	{
			return userMap.containsKey(new Integer(conId));
	}
	
	synchronized public boolean isLoggedin (String user)
	{
		return userMap.containsValue(user);
	}
	
	synchronized public String getUser(Integer id)
	{
		if (userMap.containsKey(id))
			return userMap.get(id);
		else
			return null;
	}
	
	synchronized public void disconnect (Integer id)
	{
		userMap.remove(id);
	}
	
	synchronized public void connect (Integer id, String user)
	{
		userMap.put(id, user);
	}

}
