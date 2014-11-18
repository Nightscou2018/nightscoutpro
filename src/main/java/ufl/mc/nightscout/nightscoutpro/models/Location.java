package ufl.mc.nightscout.nightscoutpro.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Location {
	
	private int userId;
	private String longitude;
	public String latitude;
	private String date;
	private String time;
	
	
	public Location(int userId, String longitude, String latitude, Date date) {
		super();
		this.userId = userId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.setDate(date);
		this.setTime(date);
		
	}
	
	public Location(int userId, String longitude,String latitude) {
		super();
		this.userId = userId;
		this.longitude = longitude;
		this.latitude = latitude;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
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
