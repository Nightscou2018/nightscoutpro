package ufl.mc.nightscout.nightscoutpro.models;

public class Acknowledge {
	
	private int patientId;
	private int guardianId;
	private String ack;
	
	public Acknowledge() {
		super();
	}
	public Acknowledge(int patientId, int guardianId, String ack) {
		super();
		this.patientId = patientId;
		this.guardianId = guardianId;
		this.ack = ack;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public int getGuardianId() {
		return guardianId;
	}
	public void setGuardianId(int guardianId) {
		this.guardianId = guardianId;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	
	
	
}
