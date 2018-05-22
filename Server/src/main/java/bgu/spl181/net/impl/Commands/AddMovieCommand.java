package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.Json.*;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.PageRanges;
import javax.xml.transform.Templates;
import bgu.spl181.net.impl.NewProtocol;

public class AddMovieCommand extends BaseCommand {

	public AddMovieCommand(String args, NewProtocol myProtocol, int id) {
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
	   		 this.commandName="request addmovie";
	   		 return false;
	   	 }
	   	 char ch='"';
	   	 String movieName=args.substring(args.indexOf(ch)+1, args.indexOf(ch, args.indexOf(ch)+1));
	   	JsonMovie toAdd;
	   	 if (JsonDB.movieExists(movieName))
	   	 {
	   	   	this.commandName="request addmovie";
	   		return false;  
	   	 }
	   	 int counter=0;
	   	 int i;
	   	 boolean flag=false;
	   	 for (i=0 ; i <args.length() & !flag ; i++)
	   	 {
	   		 if (args.charAt(i)=='"')
	   		 {
	   			 counter++;
	   			 if (counter==2)
	   				 flag=true;
	   		 }
	   	 }
	   	 String [] parts =(args.substring(i+1)).split(" ");
	   	 int amount = Integer.parseInt(parts[0]);
	   	 int price = Integer.parseInt(parts[1]);
	   	 	if (price<1 | amount<1)
	   	 	{
	   	 		this.commandName="request addmovie";
	   	 		return false; 
	   	 	}
	   	 int id = JsonDB.raiseMovieID();
	   	 if (parts.length==2)
	   	 {
	  	 toAdd = new JsonMovie(movieName, id, price, amount);
	   	 }
	   	 else
	   	 {
	   	 String argsCut="";
	   	 for (int j=2 ; j<parts.length ; j++)
	   	 {
	   		 argsCut=argsCut+parts[j]+" ";
	   	 }
	   	 argsCut=argsCut.substring(1);
	   	 String s = Character.toString(ch);
	   	 String [] countries = argsCut.split(s);
	   	 List <String> newCountries = new ArrayList<>();
	   	 for (String temp : countries)
	   	 {
	   		 if (!temp.equals(" "))
	   			 newCountries.add(temp);
	   	 }
	   	 toAdd = new JsonMovie(movieName, id, price, amount, newCountries);
	   	 }
	   	 JsonDB.addMovie(toAdd);
	   	 this.commandName="addmovie";
	   	 return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		 
	}

}
