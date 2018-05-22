package bgu.spl181.net.impl;

import java.io.IOException;

import java.util.List;
import bgu.spl181.net.impl.Json.*;
import bgu.spl181.net.impl.Commands.BaseCommand;
import bgu.spl181.net.impl.Commands.*;
public class MovieRentalProtocol extends NewProtocol{
	
	public MovieRentalProtocol(SharedData data)
	{
		super(data);
	}
	
	public void process(String message)
	{
		super.process(message);
		if (!(message.equals("SIGNOUT") || message.substring(0,message.indexOf(" ")).equals("LOGIN") || message.substring(0,message.indexOf(" ")).equals("REGISTER")))
		{
		BaseCommand newCommand=null;
		boolean result=false;
		String [] parts = message.split(" ");
		if (parts[1].equals("info"))
		{
			newCommand = new RequestInfoCommand(message, this.myProtocol, this.id);
			result=newCommand.execute();
			if (parts.length==2)
			{
				if (result)
				{
					try {
						MovieLibrary output=JsonDB.getMovies();
						String allMovies="";
						char c='"';
						for (JsonMovie temp : output.getMovies())
						{
							allMovies=allMovies+c+temp.getName()+c+" ";
						}
						this.myCon.getComp().get(id).send("ACK info "+allMovies);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
					this.myCon.getComp().get(id).send("ERROR info failed");
			}
			if (parts.length>2)
			{
				if (result)
				{
					try {
						char ch='"';
						String output = message.substring(message.indexOf(ch)+1, message.lastIndexOf(ch));
						JsonMovie desired = JsonDB.getMovieSpecifications(output);
						output=output+" ";
						String copiesLeft=Integer.toString(desired.getAvailable());
						String price = Integer.toString(desired.getPrice());
						output=output+copiesLeft+" "+price;
						if (desired.getCountries().size()==0)
							this.myCon.getComp().get(id).send("ACK info "+output);
						else
						{
							List<String> countries = desired.getCountries();
							char c='"';
							for (String temp : countries)
							{
								output=output+" "+c+temp+c;
							}
							this.myCon.getComp().get(id).send("ACK info "+output);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
					this.myCon.getComp().get(id).send("ERROR info failed");
			}
		}
		if (parts[1].equals("rent"))
		{
			newCommand = new RequestRentCommand(message, this.myProtocol, this.id);
			result=newCommand.execute();
			if (result)
			{
				char ch = '"';
				String movie=message.substring(message.indexOf(ch)+1,message.lastIndexOf(ch));
				this.myCon.getComp().get(id).send("ACK rent "+ch+movie+ch+" success");
				try {
					int copiesLeft = JsonDB.getMovieSpecifications(movie).getAvailable();
					int price = JsonDB.getMovieSpecifications(movie).getPrice();
					Broadcast("BROADCAST movie "+ch+movie+ch+" "+copiesLeft+" "+price);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				this.myCon.getComp().get(id).send("ERROR request rent failed");
		}
		
		if (parts[1].equals("return"))
		{
			newCommand = new RequestReturnCommand(message, myProtocol, id);
			result=newCommand.execute();
			try
			{
			
			if (result)
			{
				char ch = '"';
				String movieName = message.substring(message.indexOf(ch)+1,message.lastIndexOf(ch));	
				String copiesLeft = Integer.toString(JsonDB.getMovieSpecifications(movieName).getAvailable());
				String price = Integer.toString(JsonDB.getMovieSpecifications(movieName).getPrice());
				this.myCon.getComp().get(id).send("ACK return "+ch+movieName+ch+" success");
				Broadcast("BROADCAST movie "+ch+movieName+ch+" "+copiesLeft+" "+price);
			}
			else
			{
				this.myCon.getComp().get(id).send("ERROR request return failed");
			}
			}
			catch (IOException e) { }	
		}
		
		if (parts[1].equals("addmovie"))
		{
			newCommand = new AddMovieCommand(message, myProtocol, id);
			result=newCommand.execute();
			char ch='"';
			if (result)
			{
				String movieName=message.substring(message.indexOf(ch)+1, message.indexOf(ch, message.indexOf(ch)+1));
				this.myCon.getComp().get(id).send("ACK addmovie "+ch+movieName+ch+" success");
				try {
					String copies = Integer.toString(JsonDB.getMovieSpecifications(movieName).getAvailable());
					String price = Integer.toString(JsonDB.getMovieSpecifications(movieName).getPrice());
					Broadcast("BROADCAST movie "+ch+movieName+ch+" "+copies+" "+price);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
				this.myCon.getComp().get(id).send("ERROR request addmovie failed");
		}
		
		if (parts[1].equals("remmovie"))
		{
			newCommand = new RemoveMovieCommand(message, myProtocol, id);
			result=newCommand.execute();
			char ch='"';
			if (result)
			{
				String movieName = message.substring(message.indexOf(ch)+1,message.lastIndexOf(ch));
				this.myCon.getComp().get(id).send("ACK remmovie "+ch+movieName+ch+" success");
				Broadcast("BROADCAST movie "+ch+movieName+ch+" removed");
			}
			else
			{
				this.myCon.getComp().get(id).send("ERROR request remmovie failed");
			}
		}
		if (parts[1].equals("changeprice"))
		{
			newCommand = new ChangePriceCommand(message, myProtocol, id);
			result=newCommand.execute();
			char ch='"';
			if (result)
			{
				String movieName = message.substring(message.indexOf(ch)+1,message.lastIndexOf(ch));
				this.myCon.getComp().get(id).send("ACK changeprice "+ch+movieName+ch+" success");
				String copies="";
				String price="";
				try {
					copies = Integer.toString(JsonDB.getMovieSpecifications(movieName).getAvailable());
					price = Integer.toString(JsonDB.getMovieSpecifications(movieName).getPrice());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Broadcast("BROADCAST movie "+ch+movieName+ch+" "+copies+" "+price);
			}
			else
			{
				this.myCon.getComp().get(id).send("ERROR request changeprice failed");
			}
		}	
		
		}
	}
	
	public void Broadcast (String msg)
	{
		for (Integer temp : myData.getUsers().keySet())
		{
			myCon.getComp().get(temp).send(msg);
		}
	}
}
