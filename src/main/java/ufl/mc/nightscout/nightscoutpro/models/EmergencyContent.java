package ufl.mc.nightscout.nightscoutpro.models;

import java.util.HashMap;
import java.util.Map;

public class EmergencyContent extends Content{
	
private Map<String,EmergencyInfo> data;
	
	public void createData(String title,EmergencyInfo eInfo){
		if(data == null)
			data = new HashMap<String,EmergencyInfo>();
		data.put(title, eInfo);
	}

}
