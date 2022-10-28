package com.zezoo.emergency;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SingleShotLocationProvider {
	static String Holder;
	static Criteria criteria;
	 public static interface LocationCallback {
	      public void onNewLocationAvailable(GPSCoordinates location);
	  }

	  // calls back to calling thread, note this is for low grain: if you want higher precision, swap the 
	  // contents of the else and if. Also be sure to check gps permission/settings are allowed.
	  // call usually takes <10ms
	  public static void requestSingleUpdate(final Context context, final LocationCallback callback) {
	      final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	      criteria = new Criteria();
	      Holder = locationManager.getBestProvider(criteria, false);
	      boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	      if (isNetworkEnabled) {
	          Criteria criteria = new Criteria();
	          criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	          locationManager.requestSingleUpdate(criteria, new LocationListener() {
	              @Override
	              public void onLocationChanged(Location location) {
	                  callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
	              }

	              @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
	              @Override public void onProviderEnabled(String provider) { }
	              @Override public void onProviderDisabled(String provider) { }
	          }, null);
	      } else {
	          boolean isGPSEnabled = locationManager.isProviderEnabled(Holder);
	          if (isGPSEnabled) {
	              Criteria criteria = new Criteria();
	              criteria.setAccuracy(Criteria.ACCURACY_FINE);
	              locationManager.requestSingleUpdate(criteria, new LocationListener() {
	                  @Override
	                  public void onLocationChanged(Location location) {
	                      callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
	                  }

	                  @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
	                  @Override public void onProviderEnabled(String provider) { }
	                  @Override public void onProviderDisabled(String provider) { }
	              }, null);
	          }
	      }
	  }


	  // consider returning Location instead of this dummy wrapper class
	  public static class GPSCoordinates {
	      public float longitude = -1;
	      public float latitude = -1;

	      public GPSCoordinates(float theLatitude, float theLongitude) {
	          longitude = theLongitude;
	          latitude = theLatitude;
	      }

	      public GPSCoordinates(double theLatitude, double theLongitude) {
	          longitude = (float) theLongitude;
	          latitude = (float) theLatitude;
	      }
	  }  
}
