package at.fhooe.mc.linzguide.android.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DataGeopoint implements Serializable{

	public String name;
	public String address;
	public String website;
	public String category;
	public String uniqueAddress;
	public int x;
	public int y;
	public int lat;
	public int lon;
	public int rooms;
	public double singlePrice;
	public double doublePrice;
	public String description;
	public String telephone;
	
	public int position;
	
	public DataGeopoint(String name, String address, String website, String category, String uniqueAddress, String x, String y, String lat, String lon, String rooms, String singlePrice, String doublePrice, String description, String telephone){
		try {
			
			this.name = URLDecoder.decode(name, "ISO-8859-1");
			this.address = URLDecoder.decode(address, "ISO-8859-1");
			this.website = URLDecoder.decode(website, "ISO-8859-1");
			this.category = URLDecoder.decode(category, "ISO-8859-1");
			this.uniqueAddress = URLDecoder.decode(uniqueAddress, "ISO-8859-1");
			this.x = (int) (Double.valueOf(x.replaceAll(",", ".")) * 1);
			this.y = (int) (Double.valueOf(y.replaceAll(",", ".")) * 1);
			this.lat = (int) (Double.valueOf(lat.replaceAll(",", ".")) * 1E6);
			this.lon = (int) (Double.valueOf(lon.replaceAll(",", ".")) * 1E6);
			this.rooms = (int) (Double.valueOf(x.replaceAll(",", ".")) * 1);
			this.doublePrice = (double) (Double.valueOf(doublePrice.replaceAll(",", ".")) * 1);
			this.singlePrice = (double) (Double.valueOf(singlePrice.replaceAll(",", ".")) * 1);
			this.description = URLDecoder.decode(description, "ISO-8859-1");
			this.telephone = URLDecoder.decode(telephone, "ISO-8859-1");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataGeopoint(int pos, String name, String address, String website, String category, String uniqueAddress, int x, int y, int lat, int lon, int rooms, double singlePrice, double doublePrice, String description, String telephone){
		this.name = name;
		this.address = address;
		this.website = website;
		this.category = category;
		this.uniqueAddress = uniqueAddress;
		this.x = x;
		this.y = y;
		this.lat = lat;
		this.lon = lon;
		this.rooms = rooms;
		this.singlePrice = singlePrice;
		this.doublePrice = doublePrice;
		this.description = description;
		this.telephone = telephone;
		
		this.position = pos;
	}
	
}
