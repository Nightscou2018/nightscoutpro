package ufl.mc.nightscout.nightscoutpro.models;

import java.util.List;

public class ListResponse extends ClientResponse {
	
	private List<Glucose> glucoseInfo;
	private List<User> list;
	
	
	public ListResponse(int responseCode, String responseMessage,
			List<User> list) {
		super(responseCode, responseMessage);
		this.list = list;
	}
	
	public ListResponse(int responseCode, 
			List<Glucose> glucoseInfo, String responseMessage) {
		super(responseCode, responseMessage);
		this.glucoseInfo = glucoseInfo;
	}

	public List<Glucose> getGlucoseInfo() {
		return glucoseInfo;
	}
	public void setGlucoseInfo(List<Glucose> glucoseInfo) {
		this.glucoseInfo = glucoseInfo;
	}
	public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}
	
	
}
