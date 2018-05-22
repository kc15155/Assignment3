package bgu.spl181.net.impl;

import java.io.IOException;
import bgu.spl181.net.impl.Commands.*;
import bgu.spl181.net.impl.Interfaces.BidiMessagingProtocol;
import bgu.spl181.net.impl.Interfaces.Connections;
import bgu.spl181.net.impl.Json.*;
public class NewProtocol implements BidiMessagingProtocol<String>{
	
	protected ConnectionsImpl<String> myCon;
	protected int id;
	protected boolean terminate;
	protected NewProtocol myProtocol;
	protected SharedData myData;
	
	
	
	public NewProtocol (SharedData data)
	{
		myData=data;
	}
	
	public void start(int connectionId, Connections connections) {
		myCon=(ConnectionsImpl<String>) connections;
		id=connectionId;
		terminate=false;
		this.myProtocol=this;
	}

	@Override
	public void process(String message) {
		BaseCommand newCommand=null;
		boolean result=false;;
		if (message.equals("SIGNOUT"))
		{
			newCommand = new SignoutCommand(message, this, id);
			result=newCommand.execute();
			if (result)
			{
				myCon.getComp().get(id).send("ACK "+newCommand.getName()+" succeeded");	
				finalTerminate();
			}
			else
				myCon.getComp().get(id).send("ERROR "+newCommand.getName()+" failed");
			return;
		}
		String firstWord = message.substring(0, message.indexOf(' '));
		if (firstWord.equals("REGISTER"))
		{
			newCommand = new RegisterCommand(message, this, id);
			result=newCommand.execute();
			if (result)
			{
				myCon.getComp().get(id).send("ACK "+newCommand.getName()+" succeeded");	
			}
			else
			{
				myCon.getComp().get(id).send("ERROR "+newCommand.getName()+" failed");
			}
			return;
		}
		if (firstWord.equals("LOGIN"))
		{
			newCommand = new LoginCommand(message, this, id);
			result=newCommand.execute();
			if (result)
				myCon.getComp().get(id).send("ACK "+newCommand.getName()+" succeeded");	
			else
				myCon.getComp().get(id).send("ERROR "+newCommand.getName()+" failed");
			return;
		}
		if (firstWord.equals("REQUEST"))
		{
			String [] parts = message.split(" ");
			if (parts[1].equals("balance"))
			{
				newCommand = new RequestBalanceCommand(message,this,id);
				result=newCommand.execute();
				if (parts[2].equals("info"))
				{
					if (result)
					{
						try {
							myCon.getComp().get(id).send("ACK balance "+JsonDB.getUserSpecifications(myData.getUsers().get(new Integer(id))).getBalance());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else
						myCon.getComp().get(id).send("ERROR request balance failed");
				}
				if (parts[2].equals("add"))
				{
					if (result)
					{
						int toAdd = Integer.parseInt(parts[3]);
						try {
							myCon.getComp().get(id).send("ACK balance "+JsonDB.getUserSpecifications(myData.getUsers().get(new Integer(id))).getBalance()+ " added "+toAdd);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else
						myCon.getComp().get(id).send("ERROR request balance failed");
				}
			}
		}
		
	}

	@Override
	public boolean shouldTerminate() {
		return terminate;
	}
	
	public ConnectionsImpl<String> getConnections ()
	{
		return myCon;
	}
	
	public void finalTerminate ()
	{
		terminate=true;
	}
	
	public SharedData getData ()
	{
		return myData;
	}
	
}
