package ufl.mc.nightscout.nightscoutpro.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlucoseContent extends Content {
	
private Map<String,List<Glucose>> data;
	
	public void createData(String title,List<Glucose> list){
		if(data == null)
			data = new HashMap<String,List<Glucose>>();
		data.put(title, list);
	}
}
