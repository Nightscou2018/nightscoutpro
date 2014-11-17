package ufl.mc.nightscout.nightscoutpro.controller;

import ufl.mc.nightscout.nightscoutpro.models.*;
import ufl.mc.nightscout.nightscoutpro.utils.*;
import ufl.mc.nightscout.nightscoutpro.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {
	
	@Autowired
	HomeService homeService;
	
	@RequestMapping(value="sample", method = RequestMethod.POST)
	public @ResponseBody String sample(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String sampleJson) throws Exception{
		return homeService.sample(sampleJson);
	}
	
	@RequestMapping(value=URI_Constants.NS_LOGIN, method = RequestMethod.POST)
	public @ResponseBody ClientResponse nsLogin(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String userJson) throws Exception{
		
		return homeService.nsValidateCredentials(userJson);
	}
	
	@RequestMapping(value=URI_Constants.GA_LOGIN, method = RequestMethod.POST)
	public @ResponseBody ClientResponse gaLogin(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String userJson) throws Exception{
		
		
		return homeService.gaValidateCredentials(userJson);
	}
	
	@RequestMapping(value=URI_Constants.NS_REGISTER1, method = RequestMethod.GET)
	public @ResponseBody ClientResponse nsRegister1(
			@PathVariable("emailid") String emailId) throws Exception{
		
		return homeService.isExistingNSUser(emailId);
	}
	
	
	@RequestMapping(value=URI_Constants.NS_REGISTER2, method = RequestMethod.POST)
	public @ResponseBody ClientResponse nsRegister2(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String newUserJson) throws Exception{
		
		return homeService.addNSUser(newUserJson);
	}
	
	@RequestMapping(value=URI_Constants.GA_REGISTER1, method = RequestMethod.GET)
	public @ResponseBody ClientResponse gaRegister1(
			@PathVariable("emailid") String emailId) throws Exception{
		
		return homeService.isExistingGAUser(emailId);
	}
	
	@RequestMapping(value=URI_Constants.GA_REGISTER2, method = RequestMethod.POST)
	public @ResponseBody ClientResponse gaRegister2(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String newUserJson) throws Exception{
		
		return homeService.addGAUser(newUserJson);
	}
	
	@RequestMapping(value=URI_Constants.SEND_INVITE, method = RequestMethod.POST)
	public @ResponseBody ClientResponse sendInvite(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String inviteJson) throws Exception{
		
		return homeService.sendInvite(inviteJson);
	}
	
	@RequestMapping(value=URI_Constants.ACCEPT_INVITE, method = RequestMethod.POST)
	public @ResponseBody ClientResponse acceptInvite(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String inviteJson) throws Exception{
		
		return homeService.acceptInvite(inviteJson);
	}
	
	@RequestMapping(value = URI_Constants.RECOVER_PASSWORD, method = RequestMethod.GET)
	public @ResponseBody ClientResponse recoverForgottenPassword(
			@PathVariable("username") String username) throws Exception {
		return homeService.recoverPassword(username);
	}
	
	
	@RequestMapping(value = URI_Constants.CHECK_USERNAME, method = RequestMethod.GET)
	public @ResponseBody boolean checkUsername(
			@PathVariable("username") String username) throws Exception {
		return homeService.checkUsername(username);
	}

	//public static final String GET_GLUCOSE = "showGlucose/guardianid/{guardianid}/patientusername/{patientusername}";
	@RequestMapping(value = URI_Constants.GET_GLUCOSE, method = RequestMethod.GET)
	public @ResponseBody ClientResponse getGlucoseResponse(
			@PathVariable("guardianid") int guardianId,
			@PathVariable("patientusername") String patientUsername) throws Exception {
		return homeService.getGlucoseResponse(patientUsername,guardianId);
	}
	
	@RequestMapping(value=URI_Constants.GLUCOSE_UPLOAD, method = RequestMethod.POST)
	public @ResponseBody void glucoseUpload(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String glucoseJson) throws Exception{
		System.out.println("hello1");
		 homeService.glucoseUpload(glucoseJson);
		 
	}
	
	@RequestMapping(value=URI_Constants.GLUCOSE_REQUEST, method = RequestMethod.POST)
	public @ResponseBody void glucoseRequest(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String inviteJson) throws Exception{
		
		 homeService.glucoseRequest(inviteJson);
	}
	
	@RequestMapping(value=URI_Constants.GLUCOSE_ACCEPT, method = RequestMethod.POST)
	public @ResponseBody void glucoseAccept(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String inviteJson) throws Exception{
		
		 homeService.glucoseAccept(inviteJson);
	}
	
	@RequestMapping(value=URI_Constants.LOCATION_UPLOAD, method = RequestMethod.POST)
	public @ResponseBody void locationUpload(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String locationJson) throws Exception{
		
		 homeService.locationUpload(locationJson);
	}
	
	@RequestMapping(value = URI_Constants.NS_REFRESH, method = RequestMethod.GET)
	public @ResponseBody ClientResponse nsRefresh(
			@PathVariable("userid") int patientUserId) throws Exception {
		return homeService.nsRefresh(patientUserId);
	}
	
	@RequestMapping(value = URI_Constants.GA_REFRESH, method = RequestMethod.GET)
	public @ResponseBody ClientResponse gaRefresh(
			@PathVariable("userid") int guardianUserId) throws Exception {
		return homeService.gaRefresh(guardianUserId);
	}
	
	@RequestMapping(value = URI_Constants.GET_LOCATION, method = RequestMethod.GET)
	public @ResponseBody Location getLocation(
			@PathVariable("userid") int userId) throws Exception {
		return homeService.getLocation(userId);
	}
	@RequestMapping(value = URI_Constants.NS_VERIFY, method = RequestMethod.GET)
	public @ResponseBody boolean nsVerify(
			@PathVariable("userid") int patientUserId) throws Exception {
		System.out.println("hello");
		return homeService.nsVerify(patientUserId);
	}
	
	//public static final String GET_REGID = "userid/{userid}/regid/{regid}";
	@RequestMapping(value = URI_Constants.ADD_REGID, method = RequestMethod.GET)
	public @ResponseBody void addRegId(
			@PathVariable("userid") int userId, @PathVariable("regid") int regId) throws Exception {
		 homeService.addRegId(userId,regId);
	}
}

