package ufl.mc.nightscout.nightscoutpro.dao;

import ufl.mc.nightscout.nightscoutpro.models.*;

public interface HomeDao {
	
	public ClientResponse nsValidateCredentials(User user) throws Exception;
	public ClientResponse gaValidateCredentials(User user) throws Exception;
	public ClientResponse isExistingNSUser(String email) throws Exception;
	public ClientResponse addNSUser(User user) throws Exception;
	public ClientResponse isExistingGAUser(String email) throws Exception;
	public ClientResponse addGAUser(User user) throws Exception;
	public ClientResponse sendInvite(Invite invite) throws Exception;
	public ClientResponse sendRecoveredPassword(String username) throws Exception;
	public boolean checkUsername(String username) throws Exception;
	public void addGlucose(Glucose glucose) throws Exception;
	public void addLocation(Location location) throws Exception;
	public ClientResponse acceptInvite(Invite invite) throws Exception;
	public void glucoseRequest(Invite invite) throws Exception;
	public void glucoseAccept(Invite invite)throws Exception;
	public ClientResponse getGlucoseResponse(String patientUsername, int guardianId)throws Exception;
	public ClientResponse nsRefresh(int patientUserId) throws Exception;
	public ClientResponse gaRefresh(int guardianUserId) throws Exception;
	public boolean nsVerify(int patientUserId) throws Exception;
	public Location getLocation(int userId) throws Exception;
	public void addRegId(int userId, int regId) throws Exception;
	public String sample(sample samp) throws Exception;
	
}
