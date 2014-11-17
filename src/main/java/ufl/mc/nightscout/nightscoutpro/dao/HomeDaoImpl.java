package ufl.mc.nightscout.nightscoutpro.dao;



import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;

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
		
	@Autowired
	private MailSender mailSender;
	
	@Override
	public String sample(sample samp) throws Exception {
		System.out.println("came3");
		String sql = QueryConstants.ADD_GLUCOSE;
		//this.getJdbcTemplate().update(sql,new Object[]{samp.getPatientId(),samp.getGlucose(),
			//	samp.getDate(),samp.getTime()});
		//this.getJdbcTemplate().update(sql,new Object[]{samp.getPatientId(),samp.getGlucose()});
		System.out.println("came4");
		return sql;
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
		/*int userId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{username}, Integer.class);*/
		if(userId.size() == 0){
			return new ClientResponse(400,"Invalid username. Please register!");
		}
		sql = QueryConstants.GET_PASSWORD;
		String encodedPassword = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{username}, String.class);
		System.out.println(encodedPassword);
		sql = QueryConstants.GET_EMAILID_USERID;
		String emailId = this.getJdbcTemplate().queryForObject
				(sql, new Object[]{userId.get(0)}, String.class);
		System.out.println(emailId);
		String decodedPassword = new String(
				Base64.decodeBase64(encodedPassword));
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("nightscoutpro@gmail.com");
		System.out.println("hi");
		mail.setTo(emailId);
		mail.setSubject("Password Recovery!");
		mail.setText("Hi " + username + " your password is " + decodedPassword);
		mailSender.send(mail);
		System.out.println("hi1");
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
			
			this.getJdbcTemplate().update(sql,new Object[]{location.getLocation(),
					location.getDate(),location.getTime(),list.get(0)});
		}else{
			sql = QueryConstants.ADD_LOCATION;		
		
			this.getJdbcTemplate().update(sql,new Object[]{location.getUserId(),location.getLocation(),
					location.getDate(),location.getTime()});
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
			loc.setLocation(resultSet.getString("last_seen_loc"));
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
}
