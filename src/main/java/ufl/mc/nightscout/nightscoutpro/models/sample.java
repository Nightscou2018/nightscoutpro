package ufl.mc.nightscout.nightscoutpro.models;

public class sample {
	
	private int id;
	private String emailId;
	private String userName;
	private String fullName;
	private String password;
	private long phoneNum;
	private String address;
	
	
	public sample(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}
	public sample(String emailId, String userName, String fullName,
			String password, Long phoneNum, String address) {
		super();
		this.emailId = emailId;
		this.userName = userName;
		this.fullName = fullName;
		this.password = password;
		this.phoneNum = phoneNum;
		this.address = address;
	}
	public sample() {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(Long phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
