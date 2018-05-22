package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.Json.*;
import bgu.spl181.net.impl.NewProtocol;

public class RemoveMovieCommand extends BaseCommand{

	public RemoveMovieCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
	}

	@Override
	public boolean execute() {
		try {
			if (!this.myProtocol.getData().isLoggedIn(this.id))
			return false;
			 String username = myProtocol.getData().getUsers().get(new Integer(id));
		   	 JsonUser user = JsonDB.getUserSpecifications(username);
		   	 if (user.getType().equals("normal"))
		   	 {
		   		 this.commandName="request remmovie";
		   		 return false;
		   	 }
		   	 char ch = '"';
		   	 String movieName = args.substring(args.indexOf(ch)+1, args.lastIndexOf(ch));
		   	 JsonMovie movie = JsonDB.getMovieSpecifications(movieName);
		   	 if (!JsonDB.movieExists(movieName))
		   	 {
		   		this.commandName="request remmovie";
		   		return false; 
		   	 }
		   	 if (movie.getAvailable()!=movie.getAmount())
		   	 {
		   		this.commandName="request remmovie";
		   		return false; 
		   	 }
		   	 JsonDB.removeMovie(movie);
		   	 this.commandName="remmovie";
		   	 return true;
			
		} catch (IOException e) {
		}
		return false;
	}

}
