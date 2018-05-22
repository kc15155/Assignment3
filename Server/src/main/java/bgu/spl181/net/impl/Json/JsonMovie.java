package bgu.spl181.net.impl.Json;

import java.util.ArrayList;
import java.util.List;

public class JsonMovie {
	
	private String name;
	private List<String> bannedCountries;
	private int price,totalAmount,id,availableAmount;
	
	public JsonMovie (String name, int id, int price, int amount)
	{
		this.name=name;
		this.id=id;
		this.totalAmount=amount;
		this.price=price;
		bannedCountries = new ArrayList<>();
		availableAmount=amount;
	}
	
	public JsonMovie (String name, int id, int price, int amount, List <String> countries)
	{
		this.name=name;
		this.id=id;
		this.totalAmount=amount;
		this.price=price;
		this.bannedCountries = countries;
		availableAmount=amount;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public int getId ()
	{
		return id;
	}
	
	public int getPrice ()
	{
		return price;
	}
	
	public int getAvailable ()
	{
		return availableAmount;
	}
	
	public List<String> getCountries()
	{
		return bannedCountries;
	}
	
	public void rent ()
	{
		availableAmount--;
	}
	
	public void returnMovie ()
	{
		availableAmount++;
	}
	
	public int getAmount ()
	{
		return totalAmount;
	}
	
	public void setPrice (int newPrice)
	{
		price=newPrice;
	}

}
