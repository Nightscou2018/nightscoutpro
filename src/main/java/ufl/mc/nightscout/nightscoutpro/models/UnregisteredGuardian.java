package ufl.mc.nightscout.nightscoutpro.models;

public class UnregisteredGuardian {
	
	private int patientId;
	private String guardianEmailId;
	private int accessType;
		
	public UnregisteredGuardian() {
		super();
	}
	public UnregisteredGuardian(int patientId, String guardianEmailId,
			int accessType) {
		super();
		this.patientId = patientId;
		this.guardianEmailId = guardianEmailId;
		this.accessType = accessType;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getGuardianEmailId() {
		return guardianEmailId;
	}
	public void setGuardianEmailId(String guardianEmailId) {
		this.guardianEmailId = guardianEmailId;
	}
	public int getAccessType() {
		return accessType;
	}
	public void setAccessType(int accessType) {
		this.accessType = accessType;
	}
	
	
}
