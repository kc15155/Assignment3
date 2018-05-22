package bgu.spl181.net.impl.Json;

public class JsonMiniMovie {
	
	private String name;
	private int id;
	
	
	public JsonMiniMovie(String name, int id)
	{
		this.name=name;
		this.id=id;
	}
	
	public String getName ()
	{
		return name;
	}

}
