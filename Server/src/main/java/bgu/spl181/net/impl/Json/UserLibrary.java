package bgu.spl181.net.impl.Json;

import java.util.ArrayList;
import java.util.List;

public class UserLibrary {
	
	private List<JsonUser> users;
	
	public UserLibrary ()
	{
		users= new ArrayList<>();
	}
	
	public List<JsonUser> getUsers ()
	{
		return users;
	}
	
	public void setUsers (List <JsonUser> toUpdate)
	{
		users=toUpdate;
	}
	
	public void addUser (JsonUser toAdd)
	{
		users.add(toAdd);
	}

}
