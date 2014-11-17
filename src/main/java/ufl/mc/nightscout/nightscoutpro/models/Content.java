package ufl.mc.nightscout.nightscoutpro.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Content implements Serializable{
	
	private List<String> registration_ids;
	
	public void addRegId(String regId){
		if(registration_ids == null)
			registration_ids = new LinkedList<String>();
		registration_ids.add(regId);
	}

}
