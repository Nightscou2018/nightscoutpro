package ufl.mc.nightscout.nightscoutpro.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Location {
	
	private int userId;
	private String Location;
	private String date;
	private String time;
	
	
	public Location(int userId, String location, Date date) {
		super();
		this.userId = userId;
		Location = location;
		this.setDate(date);
		this.setTime(date);
		
	}
	
	public Location(int userId, String location) {
		super();
		this.userId = userId;
		Location = location;
	}

	public Location() {
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getTime() {
		return time;
	}
	public void setTime(Date date) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		this.time = df.format(date) ;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public void setDate(Date date) {
		DateFormat df = new SimpleDateFormat("MM:dd:yyyy");
		this.date = df.format(date) ;
	}
}
