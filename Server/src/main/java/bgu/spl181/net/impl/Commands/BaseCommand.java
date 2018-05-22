package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.NewProtocol;

public abstract class BaseCommand {
	
	protected String args;
	protected String commandName;
	protected NewProtocol myProtocol;
	protected int id;
	
	
	public BaseCommand(String args, NewProtocol myProtocol, int id)
	{
		if (args.indexOf(' ')!=-1)
			this.args=args.substring(args.indexOf(' ')+1);
		else
			this.args=args;	
		this.myProtocol=myProtocol;
		this.id=id;
	}
	

	public abstract boolean execute();
	
	public String getName()
    {
    	return commandName;
    }

}
