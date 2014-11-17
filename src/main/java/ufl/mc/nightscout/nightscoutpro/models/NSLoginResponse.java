package ufl.mc.nightscout.nightscoutpro.models;

import java.util.List;

public class NSLoginResponse extends ClientResponse {
	
	List<User> guardianList;
	int userId;
	
	public NSLoginResponse(int responseCode, String responseMessage,
			List<User> guardianList,int userId) {
		super(responseCode, responseMessage);
		this.guardianList = guardianList;
		this.userId = userId;
	}
	
	
	public NSLoginResponse(int responseCode, String responseMessage, int userId) {
		super(responseCode, responseMessage);
		this.userId = userId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public List<User> getGuardianList() {
		return guardianList;
	}

	public void setGuardianList(List<User> guardianList) {
		this.guardianList = guardianList;
	}
	
}
