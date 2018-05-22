package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.Json.*;
import bgu.spl181.net.impl.NewProtocol;

public class RequestInfoCommand extends BaseCommand{

	public RequestInfoCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
		this.commandName="info";
	}
	
	public boolean execute ()
	{
		if (!this.myProtocol.getData().isLoggedIn(id))
			return false;
		String [] parts = args.split(" ");
		if (parts.length==1)
			return true;
		else
		{
			char ch='"';
			String movieRequested = args.substring(args.indexOf(ch)+1, args.lastIndexOf(ch));
			try {
				if (!JsonDB.movieExists(movieRequested))
					return false;
				else
				{
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}

}
