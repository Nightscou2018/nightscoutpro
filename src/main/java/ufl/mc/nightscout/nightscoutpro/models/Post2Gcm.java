package ufl.mc.nightscout.nightscoutpro.models;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Post2Gcm {
	
	private static final String API_KEY = "AIzaSyB5peki-_dGUedmlBm2WWhMmkB2O46saVU";
	private static final String GCM_URL = "https://android.googleapis.com/gcm/send";
	
	public static void 	post(Content content) throws IOException{
		
		URL url = new URL(GCM_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		 conn.setRequestProperty("Content-Type", "application/json");
	     conn.setRequestProperty("Authorization", "key="+API_KEY);
	     conn.setDoOutput(true);
	     
	     ObjectMapper mapper = new ObjectMapper();
	     DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
	     mapper.writeValue(wr, content);
	     wr.flush();
	     wr.close();
	}
}
