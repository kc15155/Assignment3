package bgu.spl181.net.impl.Json;

import java.util.ArrayList;
import java.util.List;

public class JsonUser {
	
	private String username, password, type, country;
	private List<JsonMiniMovie> movies;
	private Integer balance;
	
	public JsonUser(String user, String pass)
	{
		username=user;
		password=pass;
		this.type="normal";
		country="";
		movies = new ArrayList<>();
		balance=0;
	}
	
	public JsonUser(String user, String pass, String country)
	{
		username=user;
		password=pass;
		this.type="normal";
		this.country=country;
		movies = new ArrayList<>();
		balance=0;
	}
	
	public String getUser ()
	{
		return username;
	}
	
	public String getPass ()
	{
		return password;
	}
	
	public String getCountry ()
	{
		return country;
	}
	
	public String getType ()
	{
		return type;
	}
	
	public Integer getBalance ()
	{
		return balance;
	}
	
	public void setBalance(Integer newBalance)
	{
		balance=newBalance;
	}
	
	public List<JsonMiniMovie> getMovies()
	{
		return movies;
	}
	
	public void addMovie (JsonMovie toAdd)
	{
		movies.add(JsonDB.minimize(toAdd));
	}
	
	public void removeMovie (JsonMovie toRemove)
	{
		List<JsonMiniMovie> newMovies = new ArrayList<>();
		for (JsonMiniMovie temp : movies)
		{
			if (!temp.getName().equals(toRemove.getName()))
				newMovies.add(temp);
		}
		movies=newMovies;
	}
	
	

}
