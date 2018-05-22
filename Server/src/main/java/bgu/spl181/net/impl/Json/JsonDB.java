package bgu.spl181.net.impl.Json;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
public class JsonDB {
	
	public static int movieID=1;
	private final static ReadWriteLock userLock = new ReentrantReadWriteLock();
	private final static ReadWriteLock movieLock = new ReentrantReadWriteLock();
	
	public static void updateUsers (UserLibrary toUpdate) throws IOException
	{
		userLock.writeLock().lock();
		Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class,
    			(JsonSerializer<Integer>)(integer, type, jsonSerializationContext) -> new JsonPrimitive(integer.toString())).setPrettyPrinting().create();
        Writer write = new FileWriter("Database/Users.json");
        gson.toJson(toUpdate, write);
        write.close();
        userLock.writeLock().unlock();
	}
	
	public static UserLibrary getUsers () throws IOException
	{
		userLock.readLock().lock();
		Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class,
    			(JsonSerializer<Integer>)(integer, type, jsonSerializationContext) -> new JsonPrimitive(integer.toString())).setPrettyPrinting().create();
		UserLibrary output;
        Reader reader = new FileReader("Database/Users.json");
        output = gson.fromJson(reader, UserLibrary.class);
        if (output == null) {
            output = new UserLibrary();
        }
        reader.close();
        userLock.readLock().unlock();
        return output; 
	}
	
	 public static boolean addUser (JsonUser newUser) throws IOException
	{
		 userLock.writeLock().lock();
		if (userExists(newUser))
		{
			userLock.readLock().unlock();
			return false;
		}
		else
		{
			UserLibrary currentUsers =getUsers();
			currentUsers.addUser(newUser);
			updateUsers(currentUsers);
			userLock.writeLock().unlock();
	        return true;
		}
	}
	
	 public static boolean userExists (JsonUser newUser) throws IOException
	{
		for (JsonUser temp : getUsers().getUsers())
		{
			if (temp.getUser().equals(newUser.getUser()))
				return true;
		}
		return false;
	}
	
	 public static int addBalance (String user,int toAdd) throws IOException
	{
		userLock.writeLock().lock();
		UserLibrary currentUsers = getUsers();
		for (JsonUser temp : currentUsers.getUsers())
		{
			if (temp.getUser().equals(user))
			{
				temp.setBalance(toAdd+temp.getBalance().intValue());
				updateUsers(currentUsers);
				userLock.writeLock().unlock();
				return temp.getBalance().intValue();
			}
		}
		userLock.writeLock().unlock();
		return -1;			
	}
	
	 public static boolean verifyUser (String user, String pass) throws IOException
	{
		for (JsonUser temp : getUsers().getUsers())
		{
			if (temp.getUser().equals(user))
			{
				if (temp.getPass().equals(pass))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	 public static boolean checkType (String user) throws IOException
	{
		for (JsonUser temp : getUsers().getUsers())
		{
			if (temp.getUser().equals(user))
			{
				if (temp.getType().equals("admin"))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	 public static JsonUser getUserSpecifications (String user) throws IOException
	{
		for (JsonUser temp: getUsers().getUsers())
		{
			if (temp.getUser().equals(user))
				return temp;
		}
		return null;
	}
	
	 public static void updateMovies (MovieLibrary toUpdate) throws IOException
	{
		movieLock.writeLock().lock();
		Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class,
    			(JsonSerializer<Integer>)(integer, type, jsonSerializationContext) -> new JsonPrimitive(integer.toString())).setPrettyPrinting().create();
        Writer write = new FileWriter("Database/Movies.json");
        gson.toJson(toUpdate, write);
        write.close();
        movieLock.writeLock().unlock();
	}
	
	 public static MovieLibrary getMovies () throws IOException
	{
		movieLock.readLock().lock();
		Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class,
    			(JsonSerializer<Integer>)(integer, type, jsonSerializationContext) -> new JsonPrimitive(integer.toString())).setPrettyPrinting().create();
		MovieLibrary output;
        Reader reader = new FileReader("Database/Movies.json");
        output = gson.fromJson(reader, MovieLibrary.class);
        if (output == null) {
            output = new MovieLibrary();
        }
        reader.close();
        movieLock.readLock().unlock();
        return output;
	}  
	
	 public static boolean addMovie (JsonMovie newMovie) throws IOException
	{
		 movieLock.writeLock().lock();
		if (movieExists(newMovie.getName()))
		{
			movieLock.writeLock().unlock();
			return false;
		}
		else
		{
			MovieLibrary currentMovies = getMovies();
			currentMovies.addMovie(newMovie);
			updateMovies(currentMovies);
			movieLock.writeLock().unlock();
	        return true;
		}
	}
	
	 public static boolean movieExists (String newMovie) throws IOException
	{
		for (JsonMovie temp : getMovies().getMovies())
		{
			if (temp.getName().equals(newMovie))
				return true;
		}
		return false;
	}
	
	 public static boolean removeMovie (JsonMovie toRemove) throws IOException
	{
		movieLock.writeLock().lock();
		if (!movieExists(toRemove.getName()))
		{
			movieLock.writeLock().unlock();
			return false;
		}
		MovieLibrary currentMovies = getMovies();
		MovieLibrary newMovies= new MovieLibrary();
		for (JsonMovie temp : currentMovies.getMovies())
		{
			if (!temp.getName().equals(toRemove.getName()))
				newMovies.addMovie(temp);
		}
		updateMovies(newMovies);
		movieLock.writeLock().unlock();
        return true;
	}
	
	 public static JsonMovie getMovieSpecifications (String movie) throws IOException
	{
		for (JsonMovie temp: getMovies().getMovies())
		{
			if (temp.getName().equals(movie))
				return temp;
		}
		return null;
	}
	
	 public static void changePrice (String movieName, int newPrice) throws IOException
	{
		 movieLock.writeLock().lock();
		MovieLibrary currentMovies = getMovies();
		for (JsonMovie temp : currentMovies.getMovies())
		{
			if (temp.getName().equals(movieName))
				temp.setPrice(newPrice);
		}
		updateMovies(currentMovies);
		movieLock.writeLock().unlock();
	}
	
	 public static int raiseMovieID ()
	{
		int output=movieID;
		movieID++;
		return output;
	}
	
	public static JsonMiniMovie minimize (JsonMovie toMinimize)
	{
		JsonMiniMovie output = new JsonMiniMovie(toMinimize.getName(), toMinimize.getId());
		return output;
	}
	
	

}
