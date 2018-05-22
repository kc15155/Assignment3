package bgu.spl181.net.impl.Json;

import java.util.ArrayList;
import java.util.List;

public class MovieLibrary {
	
	private List <JsonMovie> movies;
	
	public MovieLibrary ()
	{
		movies = new ArrayList<>();
	}
	
	public List<JsonMovie> getMovies ()
	{
		return movies;
	}
	
	public void addMovie (JsonMovie toAdd)
	{
		movies.add(toAdd);
	}

}
