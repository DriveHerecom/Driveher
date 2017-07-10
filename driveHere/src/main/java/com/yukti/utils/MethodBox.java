package com.yukti.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MethodBox {

	public String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


	public byte[] fileToByte(File file) {

		byte[] data = null;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while (is.available() > 0) {
				bos.write(is.read());
			}
			data = bos.toByteArray();
			if(is!=null)
				is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			data = null;
		} catch (IOException e) {
			e.printStackTrace();
			data = null;
		}

		return data;
	}
	
	public void hidekeyBoard(Activity activity) {
	    View view = activity.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	public void showkeyBoard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}
	}
	
	public String getAppVersion(Context context) {
		String versionCode = "";
	    try {
	        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	        versionCode = String.valueOf(packageInfo.versionCode);
	        Log.d("version_code_parse", "success");
	    } catch (NameNotFoundException e) {
	    	Log.d("version_code_parse", "error");
	        //should never happen
	    }
	    Log.d("version_code", versionCode);
        return versionCode;
	}
	
	public String getMD5String(String password) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes("UTF-8"));
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(
						(0x000000ff & messageDigest[i]) | 0xffffff00)
						.substring(6));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean writeCameraImageToInternalStorage(byte[] data) {
		try {
			String FILENAME = "last_captured_photo.jpg";
			FileOutputStream outputStream = AppSingleTon.CONTEXT.openFileOutput(FILENAME,Context.MODE_PRIVATE);
			outputStream.write(data, 0, data.length);
			outputStream.close();
			return true;
		} catch (final Exception ex) {
			Log.e("camera data : ", "Exception while creating saving camera captured data!");
			ex.printStackTrace();
		}
		return false;
	}
	
	public byte[] readCameraImageFromInternalStorage() {
		int size = 0;
		byte[] data = null;

		try {
			String FILENAME = "last_captured_photo.jpg";
			File file = new File(AppSingleTon.CONTEXT.getFilesDir(), FILENAME);
			
			if (file.exists())
				size = (int) file.length();
			else
				return null;
			
			if (size != 0) {
				FileInputStream inputStream = AppSingleTon.CONTEXT.openFileInput(FILENAME);
				data = new byte[size];
				inputStream.read(data, 0, size);
				inputStream.close();
				return data;
			}
		} catch (final Exception ex) {
			Log.e("JAVA_DEBUGGING", "Exception while reading in saved data");
			ex.printStackTrace();
		}
		return null;
	}
	
	public void deleteCameraImageFromInternalStorage(){
		try {
			String FILENAME = "last_captured_photo.jpg";
			File file = new File(AppSingleTon.CONTEXT.getFilesDir(), FILENAME);
			if (file.exists())
				file.delete();
			
		}catch (Exception ex) {
		}
	}

	public void sendEmail(String subject, String message) {
		/* This method can not be called from UI thread */

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
		AppSingleTon.CONTEXT.startActivity(Intent.createChooser(emailIntent,"Send mail..."));
	}

	public void sendSms(String subject, String message) {
		/* This method can not be called from UI thread */
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:"));
		smsIntent.putExtra("sms_body", message);
		smsIntent.setType("vnd.android-dir/mms-sms");
		AppSingleTon.CONTEXT.startActivity(smsIntent);
	}

	public void callPhone(Context context, String number) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + number));
		context.startActivity(intent);
	}

	public boolean isServiceRunning(String serv) {
		ActivityManager manager = (ActivityManager) AppSingleTon.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serv.equals(service.service.getClassName()))
				return true;
		}
		return false;
	}

	public boolean isValidFormatEmailAddress(String email) {
		/*
		 * "goutom@juktecxdfghp.qwer" -> valid "goutom@juktecxdfghp.yqwer" ->
		 * invalid "goutom@juktecxdfghp.klqwer" -> invalid
		 * 
		 * "goutom@jukte.com"" -> valid "library@yahoo.com.bd" -> valid
		 * 
		 * "goutom@roy@jukte.com" -> invalid "library@yahoo.com_bd" -> invalid
		 * 
		 * Email address fromat check
		 */

		String expression = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}

	public int stringToResourceId(String strId, String resType) {
		int id = AppSingleTon.RESOURCES.getIdentifier(strId, "drawable",AppSingleTon.CONTEXT.getPackageName());
		return id;
	}
	
	public boolean isGpsEnabled(Context context){
		
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
        	return true;
        }
        return false;
	}
	
	public void startLocationSetting(Context context){
		Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		context.startActivity(callGPSSettingIntent);
	}
	
	public boolean isInternetConnected() {

		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) AppSingleTon.CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}
	
	public void startSetting(Context context){
		Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
		context.startActivity(intent);
	}

}
