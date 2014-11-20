package ufl.mc.nightscout.nightscoutpro.utils;

public class URI_Constants {

	public static final String NS_LOGIN = "/ns/login";
	public static final String GA_LOGIN = "/ga/login";
	
	public static final String NS_REGISTER1 = "ns/{emailid}/register1";
	public static final String NS_REGISTER2 = "ns/register2";
	public static final String GA_REGISTER1 = "ga/{emailid}/register1";
	public static final String GA_REGISTER2 = "ga/register2";
	public static final String CHECK_USERNAME = "/{username}/checkUsername";
	public static final String ADD_REGID = "userid/{userid}/regid/{regid}";
	
	public static final String ADD_UNIVERSAL = "addUniversalGuardian/{userid}";
	public static final String REMOVE_UNIVERSAL = "removeUniversalGuardian/{userid}";
	
	public static final String SEND_INVITE = "ns/invite";
	public static final String ACCEPT_INVITE = "ga/invite";
	
	public static final String GLUCOSE_UPLOAD = "glucose/upload";
	public static final String GLUCOSE_REQUEST = "glucose/request";
	public static final String GLUCOSE_ACCEPT = "glucose/accept";
	public static final String GET_GLUCOSE = "showGlucose/guardianid/{guardianid}/patientusername/{patientusername}";
	public static final String LOCATION_UPLOAD = "location/upload";
	
	public static final String NS_REFRESH = "ns/refresh/{userid}";
	public static final String GA_REFRESH = "ga/refresh/{userid}";
	public static final String GET_LOCATION = "location/{userid}"; 
	public static final String NS_VERIFY = "ns/verify/{userid}";
	
	public static final String RECOVER_PASSWORD ="/{username}/forgot";
	
	public static final String UPDATE_PASSWORD = "updatePassword";
	public static final String UPDATE_PROFILE = "updateProfile";
	
	public static final String EMERGENCY = "ns/emergency";
	public static final String ACKNOWLEDGE = "ga/acknowledge";
}
