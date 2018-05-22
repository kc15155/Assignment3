package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.NewProtocol;
import bgu.spl181.net.impl.Json.*;
public class RegisterCommand extends BaseCommand {

	public RegisterCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
		this.commandName="registration";
	}

	@Override
	public boolean execute() {
		if (args.indexOf(' ')==-1)
			return false;
		if (this.myProtocol.getData().isLoggedIn(this.id))
			return false;
		String username=this.args.substring(0, args.indexOf(' '));
		try {
			UserLibrary currentUsers = JsonDB.getUsers();
			for (JsonUser temp : currentUsers.getUsers())
			{
				if (temp.getUser().equalsIgnoreCase(username))
					return false;
			}
			if (args.indexOf(' ')==args.lastIndexOf(' '))
			{
				String password=this.args.substring(args.indexOf(' ')+1);
				JsonUser toAdd = new JsonUser(username, password);
				JsonDB.addUser(toAdd);
				return true;
			}
			else
			{
				String password=this.args.substring(args.indexOf(' ')+1,args.lastIndexOf(' '));
				String country=this.args.substring(args.lastIndexOf(' '));
				country=country.substring(country.indexOf('"')+1,country.lastIndexOf('"') );
				JsonUser toAdd = new JsonUser(username, password, country);
				JsonDB.addUser(toAdd);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
