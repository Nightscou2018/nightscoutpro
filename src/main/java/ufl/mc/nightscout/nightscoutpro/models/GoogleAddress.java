package ufl.mc.nightscout.nightscoutpro.models;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleAddress {
	
	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
	
	public static  GoogleResponse convertFromLatLong(String latlongString) throws IOException {

		  URL url = new URL(URL + "?latlng="
		    + URLEncoder.encode(latlongString, "UTF-8") + "&location_type=ROOFTOP&result_type=street_address&sensor=false");
		  // Open the Connection
		  URLConnection conn = url.openConnection();

		  InputStream in = conn.getInputStream() ;
		  ObjectMapper mapper = new ObjectMapper();
		  GoogleResponse response = (GoogleResponse)mapper.readValue(in,GoogleResponse.class);
		  in.close();
		  return response;
		  

		 }
}
