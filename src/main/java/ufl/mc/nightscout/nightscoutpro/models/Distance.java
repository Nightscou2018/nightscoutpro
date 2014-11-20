package ufl.mc.nightscout.nightscoutpro.models;

public class Distance {
	
	    static final double _eQuatorialEarthRadius = 6378.1370D;
	    static final double _d2r = (Math.PI / 180D);

	    public static int HaversineInM(double lat1, double long1, double lat2, double long2) {
	        return (int) (1000D * HaversineInKM(lat1, long1, lat2, long2));
	    }

	    public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
	        double dlong = (long2 - long1) * _d2r;
	        double dlat = (lat2 - lat1) * _d2r;
	        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
	                * Math.pow(Math.sin(dlong / 2D), 2D);
	        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
	        double d = _eQuatorialEarthRadius * c;

	        return d;
	    }

	    public static int HaversineInKM(String lat1, String long1, String lat2, String long2) {
	        
	    	return (int) (1000D * HaversineInKM(Double.parseDouble(lat1), Double.parseDouble(long1), 
	    			Double.parseDouble(lat2), Double.parseDouble(long2)));
	    }
}
