package com.yukti.location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

public class PlayManager{

	Activity activity = null;
	GoogleApiClient googleApiClient;
	ConnectionCallbacks  connectionCallback;
	LocationListener locationListener;
	OnConnectionFailedListener connectionFailedListener;
	PlayCallback playCallback;
	
	public PlayManager(Activity activity){
		this.activity = activity;
	}
	
	public boolean isPlayAvailable() {
		
		int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (statusCode == ConnectionResult.SUCCESS) {
			return true;
		} 
		return false;
	}

	public void tryToConnect(PlayCallback pc){
		
		playCallback = pc;
		connectionCallback = new ConnectionCallbacks() {

			@Override
			public void onConnected(Bundle connectionHint) {
				playCallback.onGoogleApiClientReady();
			}
			
			@Override
			public void onConnectionSuspended(int cause) {
		
			}
		};
		
		connectionFailedListener = new OnConnectionFailedListener() {
			
			@Override
			public void onConnectionFailed(ConnectionResult result) {
				playCallback.onGoogleApiClientConnectionFail(result);
			}
		};
		
		
		googleApiClient = new GoogleApiClient.Builder(activity)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(connectionCallback)
        .addOnConnectionFailedListener(connectionFailedListener)
        .build();
		googleApiClient.connect();
	}
	
	// for no google play service 
	public void startNoPlayResolution(){
		
		int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		
		if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
			
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity,PlayConstants.PLAY_RESOLUTION_REQUEST, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					showToast("You have canceled play resolution! Dialog back button pressed.");
				}
			});
			
			if (errorDialog != null) {
				errorDialog.show();
			}
		}else {
			showToast("Something went wrong. Please make sure that you have the Play Store installed and that you are connected to the internet. Contact developer with details if this persists.");
		}
	}
	
	// for google play service connection failed listener
	public void startConnectionFailResolution(ConnectionResult connectionResult){
		
		int errorCode = connectionResult.getErrorCode();
		
		if (connectionResult.hasResolution()) {
			
			try {
				connectionResult.startResolutionForResult(activity,PlayConstants.PLAY_CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				showToast("Google play service connection resolution error.");
				
			}
			
		} else {
			
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity,PlayConstants.PLAY_CONNECTION_FAILURE_RESOLUTION_REQUEST, new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					   showToast("You have canceled !");
				}
			});
			
			if (errorDialog != null) {
				errorDialog.show();
			}
			else{
				showToast("Google play couldn't find any recovery process.");
			}
		}
	}
	
	// call this method after connecting to api client
	public void startLocationListening(final Context context) {
		
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				//showToast(location.getLatitude() + ","+ location.getLongitude());
			}
		};
		
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(PlayConstants.UPDATE_INTERVAL);
		locationRequest.setFastestInterval(PlayConstants.FASTEST_INTERVAL);
		locationRequest.setSmallestDisplacement(PlayConstants.SMALLEST_DISPLACEMENT);
		try {
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
		}catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context,"Not Connected To Goolgle, Try Again",Toast.LENGTH_SHORT).show();
		}
	}
	
	public Location getLastLocation(){
		Location location = null;
		try {
			location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
  		 }catch (Exception e)
		{
			e.printStackTrace();
		}

		return location;
	}
	
	public void stopListeningAndDisconnect(){
		if(googleApiClient!=null && googleApiClient.isConnected() && locationListener!=null){
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
			googleApiClient.disconnect();
		}
	}
	
	public void showToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}
	
	public interface PlayCallback {
		public void onGoogleApiClientReady();
		public void onGoogleApiClientConnectionFail(ConnectionResult result);
	}
}
