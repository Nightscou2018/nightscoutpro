package ufl.mc.nightscout.nightscoutpro.models;

import java.util.List;

public class GALoginResponse extends ClientResponse {
	
	List<User> patientList;
	int userId;

	public GALoginResponse(int responseCode, String responseMessage,
			List<User> patientList,int userId) {
		super(responseCode, responseMessage);
		this.patientList = patientList;
		this.userId = userId;
	}

	
	public GALoginResponse(int responseCode, String responseMessage, int userId) {
		super(responseCode, responseMessage);
		this.userId = userId;
	}


	public GALoginResponse(int responseCode, String responseMessage, 
			List<User> patientList) {
		super(responseCode, responseMessage);
		this.patientList = patientList;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public List<User> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<User> patientList) {
		this.patientList = patientList;
	}
	
	
}
