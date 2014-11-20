package ufl.mc.nightscout.nightscoutpro.dao;



import java.io.Serializable;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;  

import ufl.mc.nightscout.nightscoutpro.models.*;
import ufl.mc.nightscout.nightscoutpro.utils.*;



public class HomeDaoImpl extends NamedParameterJdbcDaoSupport implements HomeDao,Serializable{
	
	String message = null;
	ClientResponse response = null;
	private List<Integer> goingGuardians;
	private List<Integer> reachedGuardians;
	
	public void addGoing(int guardianId){
		if(goingGuardians == null)
			goingGuardians = new LinkedList<Integer>();
		goingGuardians.add(guardianId);
	}
	
	public void addReached(int guardianId){
		if(reachedGuardians == null)
			reachedGuardians = new LinkedList<Integer>();
		reachedGuardians.add(guardianId);
	}
	
	@Autowired
	private MailSender mailSender;
	
	@Override
	public String sample(sample samp) throws Exception {
		System.out.println("came3");
		List<Integer> list = new LinkedList();
		list.add(23);
		list.add(24);
		String sql = "select ga_regId from users where user_id IN ?";
		List<String> ans=this.getJdbcTemplate().queryForList(sql,
				new Object[]{list},String.class);
		//this.getJdbcTemplate().update(sql,new Object[]{samp.getPatientId(),samp.getGlucose()});
		System.out.println("came4");
		System.out.println(ans.get(0));
		System.out.println(ans.get(1));
		return null;
	}
	
	@Override
	public ClientResponse nsValidateCredentials(User user) throws Exception {
		
		String sql = QueryConstants.GET_PASSWORD;
		
		List<String> encodedPassword = this.getJdbcTemplate().queryForList
				(sql,new Object[]{user.getUserName()},String.class);
		
		if (encodedPassword.size() > 0) {	
			
			String decodedPassword = new String(
					Base64.decodeBase64(encodedPassword.get(0)));
			
			if (decodedPassword.equals(user.getPassword())) {
				
				message = user.getUserName();
				sql = QueryConstants.GET_USERID_USERNAME;
				int userId = this.getJdbcTemplate().queryForObject
						(sql, new Object[]{user.getUserName()}, Integer.class);
				sql = QueryConstants.GET_EMAILID_USERID;
				String emailId = this.getJdbcTemplate().queryForObject
						(sql, new Object[]{userId}, String.class);
				sql = QueryConstants.GET_NS_USERID;
				
				List<Integer> nsUserId = this.getJdbcTemplate().queryForList
						(sql,new Object[]{userId},Integer.class);
				
				
				if(nsUserId.size() > 0){
					return new NSLoginResponse(200,message,getGuardianList(userId),userId);
				
				}else{
					sql = QueryConstants.ADD_NS_USER;					
					this.getJdbcTemplate().update(sql,new Object[]{userId,emailId});
					
					/* Set  is_patient to 1 in user_type for this user */
					this.addNSUserType(userId);
					
					sql = QueryConstants.ADD_UNVERIFIED;					
					this.getJdbcTemplate().update(sql,new Object[]{userId,new Date()});
				
					return new ClientResponse(200,"Hi! Welcome to nightscout app!");
					
				}
			}else{
				return new ClientResponse(400,"The username or password " +
						"you entered is incorrect");
			}
		}else{
			return new ClientResponse(400,"Invalid user. Please register!");
		}
	}
	
	public List<User> getGuardianList(int patientId) throws Exception{
		String sql = QueryConstants.GET_GUARDIANLIST;
		return this.getJdbcTemplate().query(sql, 
				new Object[]{patientId},new UserMapper());
	}
	
	public List<User> getPatientList(int guardianId) throws Exception{
		String sql = QueryConstants.GET_PATIENTLIST;
		return this.getJdbcTemplate().query(sql, 
				new Object[]{guardianId},new UserMapper());
	}
	
	public List<UnregisteredGuardian> getPatientListFromUnregisteredTable(String guardianEmailId) throws Exception{
		String sql = QueryConstants.GET_PATIENTLIST_UNREGISTERED;
		return this.getJdbcTemplate().query(sql, 
				new Object[]{guardianEmailId},new UnregisteredGuardianMapper());
	}
	
	private static final class UnregisteredGuardianMapper implements RowMapper<UnregisteredGuardian>{

		@Override
		public UnregisteredGuardian mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			UnregisteredGuardian unregGuard = new UnregisteredGuardian();
			unregGuard.setPatientId(resultSet.getInt("user_id"));
			unregGuard.setGuardianEmailId(resultSet.getString("guardian_email"));
			unregGuard.setAccessType(resultSet.getInt("access_type"));
			return unregGuard;
		}
		
	}
	
	private static final class UserMapper implements RowMapper<User>{

		@Override
		public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			User user = new User();
			user.setUserName(resultSet.getString("user_name"));
			user.setEmailId(resultSet.getString("email_id"));
			//user.setFullName(resultSet.getString("full_name"));
			//user.setPhoneNum(resultSet.getLong("phone_no"));
			//user.setAddress(resultSet.getString("address"));
			return user;
		}
		
	}
	
	@Override
	public ClientResponse gaValidateCredentials(User user) throws Exception {
		
		String sql = QueryConstants.GET_PASSWORD;
		List<String> encodedPassword = this.getJdbcTemplate().queryForList
				(sql, new Object[]{user.getUserName()}, String.class);
		
		if ( encodedPassword.size() > 0 ) {
			String decodedPassword = new String(
					Base64.decodeBase64(encodedPassword.get(0)));
			if (decodedPassword.equals(user.getPassword())) {
				message = user.getUserName();
				sql = QueryConstants.GET_USERID_USERNAME;
				int userId = this.getJdbcTemplate().queryForObject
						(sql, new Object[]{user.getUserName()}, Integer.class);
							
					sql = QueryConstants.GET_GA_USERID;
				
					List<Integer> gaUserId = this.getJdbcTemplate().queryForList
							(sql,new Object[]{userId},Integer.class);
					if(gaUserId.size() > 0){
						return new GALoginResponse(200,message,getPatientList(userId),userId);
					}else{
						sql = QueryConstants.GET_UNVERIFIED_USERID;						
						List<Integer> unVerifiedUserId = this.getJdbcTemplate().queryForList
								(sql,new Object[]{userId},Integer.class);
						if(unVerifiedUserId.size() > 0){
							return new ClientResponse(400,"You can't login now as you are yet an unverified"
									+ "patient in nightscout app. So, please wait or register in guardian angel app");
						}else{
							sql = QueryConstants.GET_EMAILID_USERID;
							String emailId = this.getJdbcTemplate().queryForObject
									(sql, new Object[]{userId}, String.class);
						
							sql = QueryConstants.ADD_GA_USER;													
							this.getJdbcTemplate().update(sql,new Object[]{userId,emailId});
							
							/* Set  is_guardian to 1 in user_type for this user */
							this.addGAUserType(userId);
							
							List<UnregisteredGuardian> list = getPatientListFromUnregisteredTable(emailId);
							for(UnregisteredGuardian urg : list){
								sql = QueryConstants.ADD_GUARD_4_PATIENT;
								
								this.getJdbcTemplate().update(sql,new Object[]{urg.getPatientId(),userId});
								sql = QueryConstants.ADD_PATIENT_2_GUARD;
								
								this.getJdbcTemplate().update(sql,new Object[]{userId,urg.getPatientId()});
							}
							if(list.size()>0){
								sql = QueryConstants.REMOVE_UNREGISTERED;
								this.getJdbcTemplate().update(sql,new Object[]{emailId});
							}
							
							message = "Hi! Welcome to guardian angel app!";
							return new GALoginResponse(200,message,getPatientList(userId),userId);
						}
					
				}
			}else{
				return new ClientResponse(400,"The username or password " +
						"you entered is incorrect");
			}
		}else{
			return new ClientResponse(400,"Invalid user. Please register!");
		}
	}

	@Override
	public ClientResponse isExistingNSUser(String email) throws Exception {
				
		String sql = QueryConstants.COUNT_USERS_EMAILID;
		int count = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{email}, Integer.class);
		if(count > 0){
			
			sql = QueryConstants.COUNT_NS_USERS_EMAILID;
			 count = this.getJdbcTemplate().queryForObject
					(sql, new Object[]{email}, Integer.class);
			
			if(count > 0){ ;
				message = "You have already registered. Please login!";
				return new ClientResponse(400,message);
			}
			else{
				message = "You have registered in Guardian Angel app. Use the same details" +
						"to login here!";
				return new ClientResponse(400,message);
			}
		}else{
			message = "Please enter further details";
			return new ClientResponse(200,message);
		}
	}

	@Override
	public ClientResponse addNSUser(User user) throws Exception {
	
		String sql = QueryConstants.ADD_USER;		
		
		this.getJdbcTemplate().update(sql,new Object[]{user.getEmailId(),user.getUserName(),
			user.getPassword(),user.getFullName(),user.getPhoneNum(),user.getAddress()});
		
		sql = QueryConstants.GET_USERID_USERNAME;
		int userId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{user.getUserName()}, Integer.class);
		
		sql = QueryConstants.ADD_NS_USER;
		
		this.getJdbcTemplate().update(sql,new Object[]{userId,user.getEmailId()});
		
		this.addUserType(userId);
		this.addNSUserType(userId);
		
		sql = QueryConstants.ADD_UNVERIFIED;
		
		this.getJdbcTemplate().update(sql,new Object[]{userId,new Date()});
		message ="User successfully registered";		
		return new NSLoginResponse(200,message,userId);
		
	}

	@Override
	public ClientResponse isExistingGAUser(String email) throws Exception {
		
		String sql = QueryConstants.COUNT_USERS_EMAILID;
		int count = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{email}, Integer.class);
		
		if(count > 0){
			
			sql = QueryConstants.COUNT_GA_USERS_EMAILID;
			count = this.getJdbcTemplate().queryForObject
					(sql, new Object[]{email}, Integer.class);
			
			if(count > 0){
				
				message = "You have already registered. Please login!";
				return new ClientResponse(400,message);
			}
			else{
				message = "You have registered in NightScout app. Use the same details" +
						"to login here!";
				return new ClientResponse(400,message);
			}	
		}else{
			sql = QueryConstants.COUNT_UNREGISTERED_EMAILID;
			int count2 = this.getJdbcTemplate().queryForObject
					(sql, new Object[]{email}, Integer.class);
		
			if(count2 > 0){ //If atleast one person had sent him invite
				
				message = "Please enter further details";
				return new ClientResponse(200,message);
			}else{
				message = "No person has invited you. Your Registration denied!";
				return new ClientResponse(400,message);			
			}
		}	
		
	}

	@Override
	public ClientResponse addGAUser(User user) throws Exception {

		String sql = QueryConstants.ADD_USER;		
		
		this.getJdbcTemplate().update(sql,new Object[]{user.getEmailId(),user.getUserName(),
				user.getPassword(),user.getFullName(),user.getPhoneNum(),user.getAddress()});
				
		sql = QueryConstants.GET_USERID_USERNAME;
		int userId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{user.getUserName()}, Integer.class);
		
		sql = QueryConstants.ADD_GA_USER;
		
		this.getJdbcTemplate().update(sql,new Object[]{userId,user.getEmailId()});
		
		this.addUserType(userId);
		this.addGAUserType(userId);
				
		//Add code to remove all entries in unregistered table for that emailId
		List<UnregisteredGuardian> list = getPatientListFromUnregisteredTable(user.getEmailId());
		for(UnregisteredGuardian urg : list){
			sql = QueryConstants.ADD_GUARD_4_PATIENT;
			
			this.getJdbcTemplate().update(sql,new Object[]{urg.getPatientId(),userId});
			
			sql = QueryConstants.ADD_PATIENT_2_GUARD;
			
			this.getJdbcTemplate().update(sql,new Object[]{userId,urg.getPatientId(),urg.getAccessType()});
		}
		sql = QueryConstants.REMOVE_UNREGISTERED;
		
		this.getJdbcTemplate().update(sql,new Object[]{user.getEmailId()});
		
		message ="User successfully registered";
		return new GALoginResponse(200,message,getPatientList(userId),userId);
	}

	@Override
	public ClientResponse sendInvite(Invite invite) throws Exception {
		
		/* Create an entry in Unregistered Guardian table */
		
		String sql = QueryConstants.ADD_UNREGISTERED;
		
		this.getJdbcTemplate().update(sql,new Object[]{invite.getPatientId(),
				invite.getGuardianEmailId(),invite.getAccessType()});
		
		/* Check if that invitee is an existing guardian */
		sql = QueryConstants.GET_GA_USERID_EMAILID;
		
		List<Integer> gaUserId = this.getJdbcTemplate().queryForList(sql,new Object[]{invite.getGuardianEmailId()},Integer.class);
		if(gaUserId.size() > 0){ //Invited Recepient is an existing guarrdian in the system. Send him notification
			
			/* Get user_id of patient */
			sql = QueryConstants.GET_USERNAME_USERID;
			String patientUsername = this.getJdbcTemplate().queryForObject
					(sql, new Object[]{invite.getPatientId()}, String.class);
			invite.setPatientUserName(patientUsername);
			
			/* Get regn_id of guardian */
			sql = QueryConstants.GET_REGNID_USERID;
			String regId =  this.getJdbcTemplate().queryForObject
					(sql, new Object[]{gaUserId.get(0)}, String.class);
			
			/* Get invite object which stores patient user name and access type and put in content object */
			InviteContent ic = new InviteContent();
			ic.addRegId(regId);
			ic.createData("info", invite); /* store invite object in data */
			Post2Gcm.post(ic);	/* post to GCM */
			
			
		}else{//Invited Recepient is not an existing guardian. Send him URL via email
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setFrom("nightscoutpro@gmail.com");

			mail.setTo(invite.getGuardianEmailId());
			mail.setSubject("Join Guardian Angel");
			mail.setText("Hi " + invite.getPatientUserName() + "has invited you to join Guardian Angel App."
					+ "If you wish to join, please click on the below url\n"
					+"");
			mailSender.send(mail);
		}
		message = "Invite Successfully sent to " + invite.getGuardianEmailId();
		return new ClientResponse(200,message);
	}

	public ClientResponse sendRecoveredPassword(String username) {
		
		if (StringUtils.isBlank(username)) {
			return new ClientResponse(200,
					"Usename can not be blank..Please retry!!");
		}
		String sql = QueryConstants.GET_USERID_USERNAME;
		List<Integer> userId = this.getJdbcTemplate().queryForList
				(sql,new Object[]{username},Integer.class);
		
		if(userId.size() == 0){
			return new ClientResponse(400,"Invalid username. Please register!");
		}
		sql = QueryConstants.GET_PASSWORD;
		String encodedPassword = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{username}, String.class);
		
		sql = QueryConstants.GET_EMAILID_USERID;
		String emailId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{userId.get(0)}, String.class);
		
		String decodedPassword = new String(
				Base64.decodeBase64(encodedPassword));
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("nightscoutpro@gmail.com");
		
		mail.setTo(emailId);
		mail.setSubject("Password Recovery!");
		mail.setText("Hi " + username + " your password is " + decodedPassword);
		mailSender.send(mail);
		
		message = "Your password has been sent to your registered email";
		return new ClientResponse(200,message);
	}

	@Override
	public boolean checkUsername(String username) throws Exception {
		
		String sql = QueryConstants.COUNT_USERS_USERNAME;
		int count = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{username}, Integer.class);
		if(count == 0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void addGlucose(Glucose glucose) throws Exception {
		String sql = QueryConstants.ADD_GLUCOSE;		
		
		this.getJdbcTemplate().update(sql,new Object[]{glucose.getPatientId(),glucose.getGlucose(),
				glucose.getDate(),glucose.getTime()});

	}

	@Override
	public void addLocation(Location location) throws Exception {
		
		String sql = QueryConstants.GET_LOCATIONS_SLNO_USERID;
		
		List<Integer> list = this.getJdbcTemplate().queryForList(sql,new Object[]{location.getUserId()},Integer.class);
		
		if(list.size()>0){ System.out.println(list.size());
			sql = QueryConstants.UPDATE_LOCATION;
			
			this.getJdbcTemplate().update(sql,new Object[]{location.getLongitude(),
					location.getLatitude(),location.getDate(),location.getTime(),list.get(0)});
		}else{
			sql = QueryConstants.ADD_LOCATION;		
		
			this.getJdbcTemplate().update(sql,new Object[]{location.getUserId(),location.getLongitude(),
					location.getLatitude(),location.getDate(),location.getTime()});
		}
		
	}

	@Override
	public ClientResponse acceptInvite(Invite invite) throws Exception {
		
		String sql = QueryConstants.GET_USERID_USERNAME;
		int patientId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{invite.getPatientUserName()}, Integer.class);
		sql = QueryConstants.GET_EMAILID_USERID;
		String guardianEmailId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{invite.getGuardianId()}, String.class);
		sql = QueryConstants.GET_ACCESS;
		int access = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{patientId,guardianEmailId}, Integer.class);
		/* Add an entry in guardians_for_patient table */
		sql = QueryConstants.ADD_GUARD_4_PATIENT;
		
		this.getJdbcTemplate().update(sql,new Object[]{patientId,invite.getGuardianId()});
		/* Add an entry in patients_to_guardian table */
		sql = QueryConstants.ADD_PATIENT_2_GUARD;
		
		this.getJdbcTemplate().update(sql,new Object[]{invite.getGuardianId(),patientId,access});
				
		/* Delete entry from unregistered_guardians table */
		sql = QueryConstants.REMOVE_UNREGISTERED1;
		
		this.getJdbcTemplate().update(sql,new Object[]{patientId,guardianEmailId});
		message ="Invite accepted";
		return new GALoginResponse(200,message,getPatientList(invite.getGuardianId()));
	}

	@Override
	public void glucoseRequest(Invite invite) throws Exception {
		
		String sql = QueryConstants.GET_USERID_USERNAME;
		int patientId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{invite.getPatientUserName()}, Integer.class);
		sql = QueryConstants.GET_USERNAME_USERID;
		String guardianUserName = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{invite.getGuardianId()}, String.class);
		/* notify patientId about guardianUserName and his access type request via GCM */
		
		sql = QueryConstants.GET_REGNID_USERID;
		String regId =  this.getJdbcTemplate().queryForObject
				(sql, new Object[]{patientId}, String.class);
		invite.setGuardianUserName(guardianUserName);
		System.out.println("i came ");
		/* Get invite object which stores patient user name and access type and put in content object */
		InviteContent ic = new InviteContent();
		ic.addRegId(regId);
		ic.createData("info", invite); /* store invite object in data */
		Post2Gcm.post(ic);	/* post to GCM */
		
	}

	@Override
	public void glucoseAccept(Invite invite) throws Exception {
		
		String sql = QueryConstants.GET_USERID_USERNAME;
		int guardianId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{invite.getGuardianUserName()}, Integer.class);
		/* update entry in patients_to_guardian table */
		sql = QueryConstants.UPDATE_ACCESS;
		
		this.getJdbcTemplate().update(sql,new Object[]{invite.getAccessType(),
				guardianId,invite.getPatientId()});
		System.out.println("i too came");
		if(invite.getAccessType() > 0){
			/* Notify guardian(based on guardianId) glucose information of
			patient(based on patientId) via GCM */
			sql = QueryConstants.GET_REGNID_USERID;
			String regId =  this.getJdbcTemplate().queryForObject
					(sql, new Object[]{guardianId}, String.class);
			
			sql = QueryConstants.GET_USERNAME_USERID;
			String patientUsername =  this.getJdbcTemplate().queryForObject
					(sql, new Object[]{invite.getPatientId()}, String.class);
			
			List<Glucose> list =this.getGlucose(patientUsername, guardianId);
			/* Get invite object which stores patient user name and access type and put in content object */
			GlucoseContent gc = new GlucoseContent();
			gc.addRegId(regId);
			gc.createData("glucose-info", list); /* store invite object in data */
			Post2Gcm.post(gc);	/* post to GCM */
		}
	}

	@Override
	public ClientResponse getGlucoseResponse(String patientUsername,int guardianId) throws Exception {
		
		List<Glucose> list =this.getGlucose(patientUsername, guardianId);
		if (list == null)
			return new ClientResponse(400,"you don't have required access to see patients glucose details");
		return new ListResponse(200,list,patientUsername + " glucose info");
		
	}
	
	public List<Glucose> getGlucose(String patientUsername, int guardianId) throws Exception{
				
		String sql = QueryConstants.GET_USERID_USERNAME;
		int patientId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{patientUsername}, Integer.class);
		
		sql = QueryConstants.GET_ACCESS_PATIENTS2GUARD;
		int access_type = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{guardianId,patientId}, Integer.class);
		
		if(access_type > 0){
			sql = QueryConstants.GET_GLUCOSE_USERID;
			List<Glucose> list = this.getJdbcTemplate().query(sql, 
				new Object[]{patientId},new GlucoseMapper());
			return list;
		}
		else return null;
		
	}
	private static final class GlucoseMapper implements RowMapper<Glucose>{

		@Override
		public Glucose mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Glucose glucose = new Glucose();
			glucose.setPatientId(resultSet.getInt("user_id"));
			glucose.setGlucose(resultSet.getInt("glucose_value"));
			glucose.setDate(resultSet.getString("date"));
			glucose.setTime(resultSet.getString("time"));
			return glucose;
		}
		
	}
	@Override
	public ClientResponse nsRefresh(int patientUserId) throws Exception {
		
		return new ListResponse(200,"Refreshed list of guardians",
				getGuardianList(patientUserId));
	}

	@Override
	public ClientResponse gaRefresh(int guardianUserId) throws Exception {
		
		return new ListResponse(200,"Refreshed list of patients",
				getPatientList(guardianUserId));
	}

	@Override
	public boolean nsVerify(int patientUserId) throws Exception {
		
		String sql = QueryConstants.GET_COUNT_GLUCOSE;
		
		int count = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{patientUserId}, Integer.class);
		
		if(count > 5){
			sql = QueryConstants.REMOVE_UNVERIFIED;
			
			this.getJdbcTemplate().update(sql,new Object[]{patientUserId});
			return true;
		}
		return false;
	}

	@Override
	public Location getLocation(int userId) throws Exception {
		
		String sql = QueryConstants.GET_LOCATION_USERID;
		Location loc = this.getJdbcTemplate().queryForObject(sql, 
				new Object[]{userId},new LocationMapper());
		return loc;
		
	}
	private static final class LocationMapper implements RowMapper<Location>{

		@Override
		public Location mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Location loc = new Location();
			loc.setUserId(resultSet.getInt("user_id"));
			loc.setLongitude(resultSet.getString("last_seen_loc_lon"));
			loc.setLatitude(resultSet.getString("last_seen_loc_lat"));
			loc.setDate(resultSet.getString("last_seen_date"));
			loc.setTime(resultSet.getString("last_seen_time"));
			return loc;
		}
		
	}
	@Override
	public void addRegId(int userId, int regId) throws Exception {
		
		String sql = QueryConstants.ADD_REGID_USERID;
			
		this.getJdbcTemplate().update(sql,new Object[]{regId,userId});
	}

	@Override
	public ClientResponse addUniversalGuardian(int userId) throws Exception {
		
		String sql = QueryConstants.UPDATE_UNIVERSAL_GUARDIAN_USER_TYPE;
		this.getJdbcTemplate().update(sql,new Object[]{1,userId});
		return new ClientResponse(200,"You have successfully got added as universal guardian");
	}
	
	@Override
	public ClientResponse removeUniversalGuardian(int userId) throws Exception {
		
		String sql = QueryConstants.UPDATE_UNIVERSAL_GUARDIAN_USER_TYPE;
		this.getJdbcTemplate().update(sql,new Object[]{0,userId});
		return new ClientResponse(200,"You are no longer an universal guardian");
	}
	
	@Override
	public void addUserType(int userId) throws Exception {
		
		String sql = QueryConstants.ADD_USER_TYPE;
		this.getJdbcTemplate().update(sql,new Object[]{userId,0,0,0,0});
		
	}
	
	@Override
	public void addNSUserType(int userId) throws Exception {
		
		String sql = QueryConstants.UPDATE_NS_USER_TYPE;
		this.getJdbcTemplate().update(sql,new Object[]{1,userId});
		
	}
	
	@Override
	public void addGAUserType(int userId) throws Exception {
		
		String sql = QueryConstants.UPDATE_GA_USER_TYPE;
		this.getJdbcTemplate().update(sql,new Object[]{1,userId});
		
	}

	@Override
	public ClientResponse updatePassword(User updatePasswordUser) throws Exception {
		
		String sql = QueryConstants.UPDATE_PASSWORD;		
		this.getJdbcTemplate().update(sql,new Object[]{updatePasswordUser.getPassword(),updatePasswordUser.getUserId()});
		return new ClientResponse(200,"Your password has successfully updated!");
	}
	
	@Override
	public ClientResponse updateProfile(User updateProfileUser) throws Exception {
		
		User user = updateProfileUser;			
		String sql = QueryConstants.UPDATE_PROFILE;
		this.getJdbcTemplate().update(sql,new Object[]{user.getEmailId(),user.getUserName(),
				user.getFullName(),user.getPhoneNum(),user.getAddress(),user.getUserId()});
		return new ClientResponse(200,"Your profile details has successfully updated!");
	}

	@Override
	public ClientResponse emergency(Emergency emergency) throws Exception {
		
		/*	Get list of guardians who are nearer to emergency patient
		 *  Get list of family members of emergency patient
		 *  Push notify both the lists with patient info
		 *  Run a while loop to check for acknowledged guardians for 10 mins
		 *  Upon receiving first acknowledgment "GOING", Reset while loop for 10 mins
		 *  If 5 acknowledgments "GOING" received, Push notify all other guardians "no need to go"
		 *  	Push notify all the family members with GOING guardians details 
		 *  After 5 mins, push notify all GOING guardians, "Did you reach?",
		 *  		Reset while loop for 10 mins
		 *  Upon receiving acknowledgment "REACHED", Push notify all GOING guardians "patient attended"
		 *  Push notify all family members of emergency patient with REACHED guardian details
		 *  Exit from loop and service call
		 *  Otherwise(after finishing the loop), notify SOS. Reset new while loop for 10 mins
		 *  If guardian acknowledges "REACHED", notify SOS and all family members of REACHED guardian details
		 *  If SOS acknowledges, notify all GOING guardians 'patient attended' and family members of patient about SOS
		 *  Otherwise, run loop for 15 mins and notify family members of guardian details about the situation
		 *  
		 *  While running in all above while loops, upon receiving "REACHED" acknowledgment,
		 *  Push notify all family members of emergency patient with REACHED guardian details,
		 *  Push notify all other GOING guardians "patient attended"
		 *  Exit from loop and service call
		 */
		 Location loc = new Location(emergency.getUserId(),emergency.getLongitude(),
				 emergency.getLatitude(),emergency.getDate(),emergency.getTime());
		 this.addLocation(loc);
		 Glucose glucose = new Glucose(emergency.getUserId(),emergency.getGlucose(),
				 emergency.getDate(),emergency.getTime());
		 this.addGlucose(glucose);
		 
		String sql = QueryConstants.GET_GUARDIANLIST_PATIENT;		
		List<Integer> allGuardians = this.getJdbcTemplate().queryForList(sql,
				new Object[]{emergency.getUserId()},Integer.class);
		
		sql = QueryConstants.GET_UNIVERSAL_GUARDIANLIST;
		List<Integer> universalGuardians = this.getJdbcTemplate().queryForList(sql,Integer.class);
		
		/* Add all universal guardians who are not guardians to patient to list of all guardians */
		/*for(int guardianId: universalGuardians)
			if(! allGuardians.contains(guardianId))
				allGuardians.add(guardianId);*/
		allGuardians.addAll(universalGuardians);
		/* Get the list of nearest guardians to the patient */
		for(int guardianId: allGuardians){
			/* Get the location of each guardian within a specific time range
			 * Calculate distance of guardian from patient
			 * If it is greater than a particular distance, remove him from the list 
			 */
			Location loc1 = this.getLocation(guardianId);
			
			if(Distance.HaversineInKM(loc1.getLatitude(), loc1.getLongitude(),
					emergency.getLatitude(),emergency.getLongitude()) < 3){ //Remove guardians more than 3kms away
				if(loc1.getDate() == emergency.getDate() ){//Remove guardians whose last seen location is not on same day
					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
					Date d1 = format.parse(loc1.getTime());
					Date d2 = format.parse(emergency.getTime());
					long diff = (d2.getTime() - d1.getTime())/1000;
					if(Math.abs(diff) > 3600) //Remove guardians whose last see time is more than 1 hr away
						allGuardians.remove(guardianId);
				}else{	
					allGuardians.remove(guardianId);
				}
			}else{
				allGuardians.remove(guardianId);
			}
		}
		/* If nearest guardian list is empty, then notify SOS */
		/* Get the Registration_Id list of all nearest Guardians */
		List<String> regIdAllGuardians = this.getRegId(allGuardians);
		
		/* Get the list of family members of the patient */
		sql = QueryConstants.GET_FAMILY_MEMBERS;
		List<Integer> familyMembers = this.getJdbcTemplate().queryForList(sql,Integer.class);
		/* Get the Registration_Id list of all family members */
		List<String> regIdFamilyMembers = this.getRegId(familyMembers);
		
		EmergencyContent ec = new EmergencyContent();/* Object that stores regId and data to push*/
		EmergencyInfo patientInfo = this.getEmergencyInfo(emergency.getUserId());
		
		/* Notify all nearest Guardians in specific time range */
		ec.setRegistration_ids(regIdAllGuardians); /* Registration Ids of all Guardians*/
		ec.createData("patient Emergency Info", patientInfo); /* Emergency Info of patient  */
		Post2Gcm.post(ec); /* Post to GCM */
		
		/* Notify all family Members */
		ec.setRegistration_ids(regIdFamilyMembers); /* Registration Ids of all Guardians*/
		 /* Emergency Info of patient is already set to ec */
		Post2Gcm.post(ec); /* Post to GCM */
		
		int firstGuyCame = 0, allGuardiansNotified = 0, askGoingGuardians = 0;
		long stop,stop1 = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
		List<String> regIdGoingGuardians;
		for(stop=System.nanoTime()+TimeUnit.SECONDS.toNanos(10);stop>System.nanoTime();){
			if((firstGuyCame == 0) && goingGuardians.size() > 0){
				firstGuyCame = 1;
				stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);	
				stop1 = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
			}
			if(reachedGuardians.size() > 0){
				/* Push notify all family members with reached Guardian details */
				ec = new EmergencyContent();
				ec.setRegistration_ids(regIdFamilyMembers);
				EmergencyInfo guardianInfo = this.getEmergencyInfo(reachedGuardians.get(0));
				ec.createData("Reached Guardian Info", guardianInfo);
				Post2Gcm.post(ec);
				/* Push notify all guardians 'no need to go' */
				if(goingGuardians.size() > 5 ){ /* notify Going guardians 'patient attended' */
					/* Reached acknowledgment received after 5 GOING acknowledgments received */
					/* By this time, all other guardians were notified 'no need to go' */
					goingGuardians.remove(reachedGuardians.get(0));
					regIdGoingGuardians = this.getRegId(goingGuardians);
					Content content = new Content();
					content.setRegistration_ids(regIdGoingGuardians);
					content.createData("msg", "Patient attended by another guardian. Thanks!");
					Post2Gcm.post(content);
					
				}else{ /* notify all guardians 'no need to go' */
					/* Reached acknowledgment received before 5 GOING acknowledgments received */
					
					allGuardians.remove(reachedGuardians.get(0)); /*All guardians except reached guardian*/
					regIdAllGuardians = this.getRegId(allGuardians);
					Content content = new Content();
					content.setRegistration_ids(regIdAllGuardians);
					content.createData("msg", "no need to go! Patient is safe");
					Post2Gcm.post(content);
				}
				return null;
			}
			/* Notify all guardians after 5 GOING acknowledgments received */
			if((allGuardiansNotified == 0) && goingGuardians.size() > 5){
				
				allGuardiansNotified = 1;
				/* Remove all Going Guardians from All Guardians */
				allGuardians.removeAll(goingGuardians);
				regIdAllGuardians = this.getRegId(allGuardians);
				Content content = new Content();
				content.setRegistration_ids(regIdAllGuardians);
				content.createData("msg", "no need to go! Patient is safe");
				Post2Gcm.post(content);
				
				/* Notify all family members with the details of GOING guardians */
				ec = new EmergencyContent();
				ec.setRegistration_ids(regIdFamilyMembers);
				for(int guardianId : goingGuardians)
					ec.createData("Going Guardian Info", this.getEmergencyInfo(guardianId));
				Post2Gcm.post(ec);
			}
			/* After 5 mins of arrival of first GOING acknowledgment, ask guardians 'did they reach?' */
			if((askGoingGuardians == 0) && stop1 < System.nanoTime()){
				askGoingGuardians = 1;
				regIdGoingGuardians = this.getRegId(goingGuardians);
				Content content = new Content();
				content.setRegistration_ids(regIdGoingGuardians);
				content.createData("msg", "Did you reach patient?");
				Post2Gcm.post(content);
				/* To reset loop for 10 more mins upon asking the above qn */
				// stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
			}			
		}
		
		
		/* Notify SOS as no 'REACHED' acknowledgment received after 10 mins of arrival of first 'GOING' acknowledgment */
		/* or Notify SOS upon no acknowledgment received after 10 mins of emergency notification sent */
		
		/*
		 * 
		 *  CODE TO NOTIFY SOS
		 * 
		 * 
		 */
		
		return null;
	}
	
	public EmergencyInfo getEmergencyInfo(int userId) throws Exception{
		
		EmergencyInfo patientInfo = new EmergencyInfo();
		String sql = "select full_name from users where user_id = ?";
		patientInfo.setFullName(this.getJdbcTemplate().queryForObject(sql, 
				new Object[]{userId},String.class));
		sql = "select phone_no from users where user_id = ?";
		patientInfo.setPhoneNum(this.getJdbcTemplate().queryForObject(sql, 
				new Object[]{userId},Long.class));
		Location loc = this.getLocation(userId);
		/* Calculate current address of the patient based on the current location using google map api
		 */
		String latlang = loc.getLatitude()+","+loc.getLongitude();
		GoogleResponse response = GoogleAddress.convertFromLatLong(latlang);
		 String address = response.getResults()[0].getFormatted_address();
		 patientInfo.setAddress(address);
		
		return patientInfo;
	}
	public List<String> getRegId(List<Integer> listUserId){
		
		List<String> listRegId = new LinkedList();
		String sql;
		
		for(int userId : listUserId){
			sql = QueryConstants.GET_REGNID_USERID;
			listRegId.add(this.getJdbcTemplate().queryForObject(sql,
					new Object[]{userId},String.class));
		}
		
		return listRegId;
	}
	@Override
	public void acknowledge(Acknowledge ack) throws Exception {
		
		if(ack.getAck() == "GOING"){
			this.addGoing(ack.getGuardianId());
		}
		if(ack.getAck() == "REACHED"){
			this.addReached(ack.getGuardianId());
		}
	}
	
	
}
