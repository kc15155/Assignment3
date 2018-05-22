package bgu.spl181.net.impl.Commands;

import java.io.IOException;
import bgu.spl181.net.impl.Json.*;
import bgu.spl181.net.impl.NewProtocol;

public class RequestBalanceCommand extends BaseCommand {

	public RequestBalanceCommand(String args, NewProtocol myProtocol, int id) {
		super(args,myProtocol,id);
	}

	@Override
	public boolean execute() {
		if (!this.myProtocol.getData().isLoggedIn(id))
		{
			this.commandName="request balance";
			return false;
		}
		String [] parts = args.split(" ");
		if (parts[1].equals("info"))
		{
			this.commandName="balance";
			return true;
		}
		if (parts[1].equals("add"))
		{
			int toAdd = Integer.parseInt(parts[2]);
			String user = myProtocol.getData().getUsers().get(new Integer(id));
			try {
				JsonDB.addBalance(user, toAdd);
				this.commandName="balance";
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.commandName="request balance";
		return false;
	}

}