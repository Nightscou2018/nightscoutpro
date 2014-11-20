package ufl.mc.nightscout.nightscoutpro.services;

import ufl.mc.nightscout.nightscoutpro.models.*;

public interface HomeService {

	public ClientResponse nsValidateCredentials(String userJson) throws Exception;
	public ClientResponse gaValidateCredentials(String userJson) throws Exception;
	public ClientResponse isExistingNSUser(String emailId) throws Exception;
	public ClientResponse addNSUser(String newUserJson) throws Exception;
	public ClientResponse isExistingGAUser(String emailId) throws Exception;
	public ClientResponse addGAUser(String newUserJson) throws Exception;
	public ClientResponse sendInvite(String inviteJson) throws Exception;
	public ClientResponse recoverPassword(String username) throws Exception;
	public boolean checkUsername(String username) throws Exception;
	public void glucoseUpload(String glucoseJson) throws Exception;
	public void locationUpload(String locationJson) throws Exception;
	public ClientResponse acceptInvite(String inviteJson) throws Exception;
	public void glucoseRequest(String inviteJson) throws Exception;
	public void glucoseAccept(String inviteJson) throws Exception;
	public ClientResponse getGlucoseResponse(String patientUsername, int guardianId)throws Exception;
	public ClientResponse nsRefresh(int patientUserId) throws Exception;
	public ClientResponse gaRefresh(int guardianUserId) throws Exception;
	public boolean nsVerify(int patientUserId) throws Exception;
	public Location getLocation(int userId) throws Exception;
	public void addRegId(int userId, int regId) throws Exception;
	public String sample(String sampJson) throws Exception;
	public ClientResponse addUniversalGuardian(int userId) throws Exception;
	public ClientResponse removeUniversalGuardian(int userId) throws Exception;
	public ClientResponse updatePassword(String updatePasswordJson) throws Exception;
	public ClientResponse updateProfile(String updateProfileJson) throws Exception;
	public ClientResponse emergency(String emergencyJson) throws Exception;
	public void acknowledge(String ackJson) throws Exception;
	
}
