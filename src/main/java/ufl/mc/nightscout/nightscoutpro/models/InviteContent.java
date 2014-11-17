package ufl.mc.nightscout.nightscoutpro.models;

import java.util.HashMap;
import java.util.Map;

public class InviteContent extends Content{
	
	private Map<String,Invite> data;
	
	public void createData(String title,Invite invite){
		if(data == null)
			data = new HashMap<String,Invite>();
		data.put(title, invite);
	}

}
