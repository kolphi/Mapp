package at.fhooe.mc.linzbrowser.android;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DataHotspot implements Serializable{

	public int latitude;
	public int longitude;
	public String name;
	public String shortText;
	public int yearStart;
	public int yearEnd;
	public String city;
	public int zip;
	public String street;
	public String homepage;

	public DataHotspot(String latitude, String longitude, String name, String shortText, String yearStart, String yearEnd, String city, String zip, String street, String homepage){
		try {
			
		this.latitude = (int) (Double.valueOf(latitude.replaceAll(",", ".")) * 1E6);
		this.longitude = (int) (Double.valueOf(longitude.replaceAll(",", ".")) * 1E6);
		this.name = URLDecoder.decode(name, "ISO-8859-1");
		this.shortText = URLDecoder.decode(shortText, "ISO-8859-1");
		this.yearStart = Integer.valueOf(yearStart);
		this.yearEnd = Integer.valueOf(yearEnd);
		this.city = URLDecoder.decode(city, "ISO-8859-1");
		this.zip = Integer.valueOf(zip);
		this.street = URLDecoder.decode(street, "ISO-8859-1");
		this.homepage = URLDecoder.decode(homepage, "ISO-8859-1");
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
