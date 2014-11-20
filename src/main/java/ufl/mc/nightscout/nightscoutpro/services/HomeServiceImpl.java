package ufl.mc.nightscout.nightscoutpro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  ufl.mc.nightscout.nightscoutpro.models.*;
import  ufl.mc.nightscout.nightscoutpro.dao.*;

import com.google.gson.Gson;

@Service
public class HomeServiceImpl implements HomeService {
	
	@Autowired
	HomeDao homeDao;
	
	@Override
	public String sample(String sampJson) throws Exception {
		
		Gson gson = new Gson();
		System.out.println(sampJson + "hello");
		sample s = gson.fromJson(sampJson, sample.class);
		System.out.println("came2");
		return homeDao.sample(s);
	}
	
	@Override
	public ClientResponse nsValidateCredentials(String userJson)
			throws Exception {
		Gson gson = new Gson();
		User user = gson.fromJson(userJson, User.class);

		return homeDao.nsValidateCredentials(user);
	}

	@Override
	public ClientResponse gaValidateCredentials(String userJson)
			throws Exception {
		Gson gson = new Gson();
		User user = gson.fromJson(userJson, User.class);

		return homeDao.gaValidateCredentials(user);
	}

	@Override
	public ClientResponse isExistingNSUser(String emailId) throws Exception {
		
		return homeDao.isExistingNSUser(emailId);
	}

	@Override
	public ClientResponse addNSUser(String newUserJson) throws Exception {
		Gson gson = new Gson();
		User newUser = gson.fromJson(newUserJson, User.class);

		return homeDao.addNSUser(newUser);
	}

	@Override
	public ClientResponse isExistingGAUser(String emailId) throws Exception {
		
		return homeDao.isExistingGAUser(emailId);
	}

	@Override
	public ClientResponse addGAUser(String newUserJson) throws Exception {
		Gson gson = new Gson();
		User newUser = gson.fromJson(newUserJson, User.class);

		return homeDao.addGAUser(newUser);
	}

	@Override
	public ClientResponse sendInvite(String inviteJson) throws Exception {
		Gson gson = new Gson();
		Invite invite = gson.fromJson(inviteJson, Invite.class);

		return homeDao.sendInvite(invite);
	}

	@Override
	public ClientResponse recoverPassword(String username) throws Exception {
		
		return homeDao.sendRecoveredPassword(username);
	}

	@Override
	public boolean checkUsername(String username) throws Exception {

		return homeDao.checkUsername(username);
	}

	@Override
	public void glucoseUpload(String glucoseJson) throws Exception {
	
		Gson gson = new Gson();		
		Glucose glucose = gson.fromJson(glucoseJson,Glucose.class);
		homeDao.addGlucose(glucose);
	}

	@Override
	public void locationUpload(String locationJson) throws Exception {
		Gson gson = new Gson();
		Location location = gson.fromJson(locationJson,Location.class);
		homeDao.addLocation(location);
	}

	@Override
	public ClientResponse acceptInvite(String inviteJson) throws Exception {
		Gson gson = new Gson();
		Invite invite = gson.fromJson(inviteJson,Invite.class);
		return homeDao.acceptInvite(invite);
	}

	@Override
	public void glucoseRequest(String inviteJson) throws Exception {
		Gson gson = new Gson();
		Invite invite = gson.fromJson(inviteJson,Invite.class);
		homeDao.glucoseRequest(invite);
		
	}

	@Override
	public void glucoseAccept(String inviteJson) throws Exception {
		Gson gson = new Gson();
		Invite invite = gson.fromJson(inviteJson,Invite.class);
		homeDao.glucoseAccept(invite);
		
	}

	@Override
	public ClientResponse getGlucoseResponse(String patientUsername,int guardianId) throws Exception {
		
		return homeDao.getGlucoseResponse(patientUsername,guardianId);
	}

	@Override
	public ClientResponse nsRefresh(int patientUserId) throws Exception {
		return homeDao.nsRefresh(patientUserId);
	}

	@Override
	public ClientResponse gaRefresh(int guardianUserId) throws Exception {
		return homeDao.gaRefresh(guardianUserId);
	}

	@Override
	public boolean nsVerify(int patientUserId) throws Exception {
		System.out.println("hello2");
		return homeDao.nsVerify(patientUserId);
	}

	@Override
	public Location getLocation(int userId) throws Exception {

		return homeDao.getLocation(userId);
	}

	@Override
	public void addRegId(int userId, int regId) throws Exception {
		
		homeDao.addRegId(userId,regId);
		
	}

	@Override
	public ClientResponse addUniversalGuardian(int userId) throws Exception {
		
		return homeDao.addUniversalGuardian(userId);
	}
	
	@Override
	public ClientResponse removeUniversalGuardian(int userId) throws Exception {
		
		return homeDao.removeUniversalGuardian(userId);
	}

	@Override
	public ClientResponse updatePassword(String updatePasswordJson)
			throws Exception {
		Gson gson = new Gson();
		User updatePasswordUser = gson.fromJson(updatePasswordJson, User.class);
		return homeDao.updatePassword(updatePasswordUser);
	}
	
	@Override
	public ClientResponse updateProfile(String updateProfileJson)
			throws Exception {
		Gson gson = new Gson();
		User updateProfileUser = gson.fromJson(updateProfileJson, User.class);

		return homeDao.updateProfile(updateProfileUser);
	}

	@Override
	public ClientResponse emergency(String emergencyJson) throws Exception {
		
		Gson gson = new Gson();
		Emergency emergency = gson.fromJson(emergencyJson, Emergency.class);

		return homeDao.emergency(emergency);
	}

	@Override
	public void acknowledge(String ackJson) throws Exception {

		Gson gson = new Gson();
		Acknowledge acknowledge = gson.fromJson(ackJson, Acknowledge.class);

		homeDao.acknowledge(acknowledge);
	}
}
