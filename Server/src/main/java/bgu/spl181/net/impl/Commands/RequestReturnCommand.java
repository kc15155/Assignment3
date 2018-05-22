package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.Json.*;
import java.util.List;

import bgu.spl181.net.impl.NewProtocol;

public class RequestReturnCommand extends BaseCommand{

	public RequestReturnCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
	}

	@Override
	public boolean execute() {
		try {
			if (!this.myProtocol.getData().isLoggedIn(this.id))
			return false;
			char ch = '"';
			String movie = args.substring(args.indexOf(ch)+1,args.lastIndexOf(ch));
			String username = myProtocol.getData().getUsers().get(new Integer(id));
			JsonUser user = JsonDB.getUserSpecifications(username);
			boolean found=false;
			
			for (JsonMiniMovie temp : user.getMovies())
			{
				if (temp.getName().equals(movie))
					found=true;
			}
			if (!found)
			{
				this.commandName="request return";
				return false;
			}
			if (!JsonDB.movieExists(movie))
			{
				this.commandName="request return";
				return false;
			}
			MovieLibrary currentMovies = JsonDB.getMovies();
			for (JsonMovie temp : currentMovies.getMovies())
			{
				if (temp.getName().equals(movie))
					temp.returnMovie();
			}
			JsonDB.updateMovies(currentMovies);
			UserLibrary currentUsers = JsonDB.getUsers();
			for (JsonUser temp : currentUsers.getUsers())
			{
				if (temp.getUser().equals(username))
					temp.removeMovie(JsonDB.getMovieSpecifications(movie));
			}
			JsonDB.updateUsers(currentUsers);
			this.commandName="request";
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;



	}

}
