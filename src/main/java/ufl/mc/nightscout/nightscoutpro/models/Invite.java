package ufl.mc.nightscout.nightscoutpro.models;

public class Invite {
	
	private String patientUserName;
	private int patientId;
	private String guardianEmailId;
	private int accessType;
	private String guardianUserName;
	private int guardianId;
	
	public Invite(String patientUserName, String guardianEmailId, int accessType) {
		super();
		this.patientUserName = patientUserName;
		this.guardianEmailId = guardianEmailId;
		this.accessType = accessType;
	}
	
	
	
	public Invite(String patientUserName, int accessType) {
		super();
		this.patientUserName = patientUserName;
		this.accessType = accessType;
	}



	public Invite(int guardianId,String patientUserName) {
		super();
		this.patientUserName = patientUserName;
		this.guardianId = guardianId;
	}



	public Invite(String patientUserName, int guardianId, int accessType ) {
		super();
		this.patientUserName = patientUserName;
		this.accessType = accessType;
		this.guardianId = guardianId;
	}



	public Invite(int patientId, int accessType, String guardianUserName) {
		super();
		this.patientId = patientId;
		this.accessType = accessType;
		this.guardianUserName = guardianUserName;
	}



	public Invite() {
		super();
	}

	public Invite(int patientId, String guardianEmailId, int accessType) {
		super();
		this.patientId = patientId;
		this.guardianEmailId = guardianEmailId;
		this.accessType = accessType;
	}

	public String getGuardianUserName() {
		return guardianUserName;
	}

	public void setGuardianUserName(String guardianUserName) {
		this.guardianUserName = guardianUserName;
	}

	public int getGuardianId() {
		return guardianId;
	}

	public void setGuardianId(int guardianId) {
		this.guardianId = guardianId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getPatientUserName() {
		return patientUserName;
	}
	public void setPatientUserName(String patientUserName) {
		this.patientUserName = patientUserName;
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
