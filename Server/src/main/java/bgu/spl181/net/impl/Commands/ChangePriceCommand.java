package bgu.spl181.net.impl.Commands;

import java.io.IOException;

import bgu.spl181.net.impl.NewProtocol;
import bgu.spl181.net.impl.Json.*;
public class ChangePriceCommand extends BaseCommand{

	public ChangePriceCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
	}

	@Override
	public boolean execute() {
		try
		{
		if (!this.myProtocol.getData().isLoggedIn(this.id))
			return false;
		String username = myProtocol.getData().getUsers().get(new Integer(id));
	   	 JsonUser user = JsonDB.getUserSpecifications(username);
	   	 if (user.getType().equals("normal"))
	   	 {
	   		 this.commandName="request changeprice";
	   		 return false;
	   	 }
	   	 char ch = '"';
	   	 String movieName = args.substring(args.indexOf(ch)+1, args.lastIndexOf(ch));
	   	 JsonMovie movie = JsonDB.getMovieSpecifications(movieName);
	   	 if (!JsonDB.movieExists(movieName))
	   	 {
	   		this.commandName="request changeprice";
	   		return false; 
	   	 }
	   	 String [] parts = args.split(" ");
	   	 int price = Integer.parseInt(parts[parts.length-1]);
	   	 if (price<1)
	   	 {
	   		this.commandName="request changeprice";
	   		return false;
	   	 }
	   	 JsonDB.changePrice(movieName, price);
	   	 this.commandName="changeprice";
	   	 return true;
		}
		catch (IOException e) {
		}
		return false;
	}

}
