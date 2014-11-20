package ufl.mc.nightscout.nightscoutpro.models;

public class EmergencyInfo {
	
	private long phoneNum;
	private String fullName;
	private String address;
	
	public EmergencyInfo() {
		super();
	}

	public EmergencyInfo(long phoneNum, String fullName, String address) {
		super();
		this.phoneNum = phoneNum;
		this.fullName = fullName;
		this.address = address;
	}

	public long getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(long phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
