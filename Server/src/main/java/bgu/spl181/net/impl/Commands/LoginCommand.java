package bgu.spl181.net.impl.Commands;

import java.io.IOException;

import bgu.spl181.net.impl.Json.*;
import bgu.spl181.net.impl.NewProtocol;

public class LoginCommand extends BaseCommand {

	public LoginCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
		this.commandName="login";
	}

	@Override
	public boolean execute() {
		String [] parts = args.split(" ");
		String username = parts[0];
		String password=parts[1];
		if (this.myProtocol.getData().isLoggedIn(this.id))
			return false;
		if (this.myProtocol.getData().isLoggedin(username))
			return false;
		try {
			if (!JsonDB.verifyUser(username, password))
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.myProtocol.getData().connect(new Integer(id),username);
		return true;
		
	}

}