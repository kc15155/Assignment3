package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import java.util.List;

import bgu.spl181.net.impl.NewProtocol;
import bgu.spl181.net.impl.Json.*;
public class RequestRentCommand extends BaseCommand{

	public RequestRentCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
	}

	@Override
	public boolean execute() {
		try {
		if (!this.myProtocol.getData().isLoggedIn(this.id))
			return false;
		char ch='"';
		String movieName = args.substring(args.indexOf(ch)+1, args.lastIndexOf(ch));
		JsonMovie movie = JsonDB.getMovieSpecifications(movieName);
		String username = this.myProtocol.getData().getUsers().get(new Integer(id));
		JsonUser user = JsonDB.getUserSpecifications(username);
			if (!JsonDB.movieExists(movieName))
			{
				this.commandName="request rent";
				return false;
			}
			if (movie.getAvailable()<1)
			{
				this.commandName="request rent";
				return false;
			}
			if (user.getBalance()<movie.getPrice())
			{
				this.commandName="request rent";
				return false;
			}
			for (String temp : movie.getCountries())
			{
				if (temp.equals(user.getCountry()))
				{
					this.commandName="request rent";
					return false;
				}
			}
			for (JsonMiniMovie temp: user.getMovies())
			{
				if (temp.getName().equals(movieName))
				{
					this.commandName="request rent";
					return false;
				}
			}
			UserLibrary currentUsers = JsonDB.getUsers();
			for (JsonUser temp : currentUsers.getUsers())
			{
				if (temp.getUser().equals(username))
				{
					temp.addMovie(movie);
					temp.setBalance(temp.getBalance()-movie.getPrice());
				}
			}
			JsonDB.updateUsers(currentUsers);
			MovieLibrary currentMovies = JsonDB.getMovies();
			for (JsonMovie temp : currentMovies.getMovies())
			{
				if (temp.getName().equals(movieName))
				{
					temp.rent();
				}
			}
			JsonDB.updateMovies(currentMovies);
			this.commandName="request";
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	

}
