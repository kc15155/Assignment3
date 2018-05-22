package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.impl.MovieRentalProtocol;
import bgu.spl181.net.impl.NewEncDec;
import bgu.spl181.net.impl.SharedData;
import bgu.spl181.net.impl.Interfaces.Server;

public class TPCMain {
	
	    public static void main(String[] args) {
	        SharedData sharedData = new SharedData();
	        Server.threadPerClient(
	                Integer.parseInt(args[0]),
	                () -> new MovieRentalProtocol(sharedData),
	                () -> new NewEncDec()
	        ).serve();
	    }
	
}
