package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.NewProtocol;

public class SignoutCommand extends BaseCommand {

	public SignoutCommand(String args, NewProtocol myProtocol, int id) {
		super(args, myProtocol, id);
		this.commandName="signout";
	}

	@Override
	public boolean execute() {
		if (!this.myProtocol.getData().isLoggedIn(id))
			return false;
		String username=this.myProtocol.getData().getUser(new Integer(id));
		if (username==null)
			return false;
		this.myProtocol.getData().disconnect(new Integer(id));
		return true;
	}

}