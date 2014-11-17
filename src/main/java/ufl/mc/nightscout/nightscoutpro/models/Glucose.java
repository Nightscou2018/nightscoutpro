package ufl.mc.nightscout.nightscoutpro.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Glucose {

	private int patientId;
	private int glucose;
	private String date;
	private String time;
	
	public Glucose(int patientId, int glucose, String date,String time) {
		super();
		this.patientId = patientId;
		this.glucose = glucose;
		this.setDate(date);
		this.setTime(time);
	}
	public Glucose() {
		
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
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
	/*public void setDate(Date date) {
		DateFormat df = new SimpleDateFormat("MM:dd:yyyy");
		this.date = df.format(date) ;
	}*/
	public void setDate(String date) {
		
		this.date = date ;
	}
	
	public String getTime() {
		return time;
	}
	/*public void setTime(Date date) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		this.time = df.format(date) ;
	}*/
	public void setTime(String time) {
		
		this.time = time ;
	}
	
	
}
