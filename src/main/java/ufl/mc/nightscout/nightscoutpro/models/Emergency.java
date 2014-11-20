package ufl.mc.nightscout.nightscoutpro.models;

public class Emergency {
	
	private int userId;
	private String longitude;
	private String latitude;
	private int glucose;
	private String date;
	private String time;
	
	
	public Emergency() {
		super();
	}
	
	public Emergency(int userId, String longitude, String latitude,
			int glucose, String date, String time) {
		super();
		this.userId = userId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.glucose = glucose;
		this.date = date;
		this.time = time;
	}

	public Emergency(int userId) {
		super();
		this.userId = userId;
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

	public int getGlucose() {
		return glucose;
	}

	public void setGlucose(int glucose) {
		this.glucose = glucose;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
