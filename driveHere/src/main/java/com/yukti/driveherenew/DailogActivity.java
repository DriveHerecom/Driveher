
package com.yukti.driveherenew;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.yukti.database.SequrityClockDatabase;
import com.yukti.location.PlayManager;
import com.yukti.location.PlayManager.PlayCallback;
import com.yukti.utils.AppSingleTon;

public class DailogActivity extends Activity {
	MediaPlayer mediaPlayer;
	Button btnok;
	ImageView img;
	TextView txt;
	SequrityClockDatabase scdb;
	Time currentTime;
	Calendar c = Calendar.getInstance();
	String starttime, clicktime, date, clickable,imagefile;
//	,note;

	private String Latitude = "";
	private String Longitude = "";

	boolean flag = false;
	private Window wind;
	private PowerManager.WakeLock wl;
	private static boolean click = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_dailog);

		AppSingleTon.PLAY_MANAGER = new PlayManager(DailogActivity.this);
		connectPlayManager();
		
		btnok = (Button) findViewById(R.id.btnok);
		img = (ImageView) findViewById(R.id.imageDialog);
		txt = (TextView) findViewById(R.id.textDialog);
		
		geocoder = new Geocoder(this,Locale.getDefault());
		
		this.setFinishOnTouchOutside(false);

		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("My_App");
		kl.disableKeyguard();

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.FULL_WAKE_LOCK, "My_App");
		wl.acquire();

		mediaPlayer = MediaPlayer.create(this, R.raw.bestalarm);

		mediaPlayer.start();
		this.setFinishOnTouchOutside(false);

		StartTime();
		currentDate();
		scdb = new SequrityClockDatabase(DailogActivity.this);

		// scdb.open();
		// scdb.getDetailFromTaskManagementTable();
		// scdb.deleteDataFromTaskManagementTable();

		btnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * if(AppSingleTon.SHARED_PREFERENCE.isDialogOpen()) {
				 */
				
				click = true;

				clickable = "YES";

				Time time = new Time();
				time.setToNow();
				clicktime = +time.hour + ":" + time.minute + ":" + time.second;
				Log.e("clicktime", clicktime);
				Log.e("clickable", clickable);

				Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();
				String adress = "";
				
				if (location != null) {

					Latitude = location.getLatitude() + "";
					Longitude = location.getLongitude() + "";
					adress = getAdress(Latitude, Longitude);

				} else {
					Latitude = "";
					Longitude = "";
					adress = "";
				}

				scdb.open();
				scdb.addDetailToTaskManagementTable(starttime, clicktime, date,
						adress, Latitude, Longitude,
//						imagefile,
						clickable);
				setAlaram();
				// AppSingleTon.SHARED_PREFERENCE.setDialogOpen(true);
				finish();

			}
			// }
		});
		
		//600000
		new CountDownTimer(600000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

				if (click) {
					click = false;
				} else {
					clickable = "NO";

					clicktime = "N/A";
					Log.e("clicktime", clicktime);
					Log.e("clickable", clickable);

					Location location = AppSingleTon.PLAY_MANAGER
							.getLastLocation();
					String adress = "";
					if (location != null) {
						Latitude = location.getLatitude() + "";
						Longitude = location.getLongitude() + "";
						adress = getAdress(Latitude, Longitude);
					} else {
						
						Latitude = "";
						Longitude = "";
						
					}

					scdb.open();
					scdb.addDetailToTaskManagementTable(starttime, clicktime,
							date, adress, Latitude, Longitude,
//							note,
//							imagefile,
							clickable);

					setAlaram();
					// AppSingleTon.SHARED_PREFERENCE.setDialogOpen(false);
					DailogActivity.this.finish();

				}

			}
		}.start();

	}

	Geocoder geocoder;
	List<Address> addresses = null;
	String address = "";

	String getAdress(String latitude2, String longitude2) {

		try {

			addresses = geocoder
					.getFromLocation(Double.valueOf(latitude2).doubleValue(),
							Double.valueOf(longitude2).doubleValue(), 1);
			Log.e("geolati", latitude2);
			Log.e("geolong", longitude2);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Here 1 represent max location result to returned, by
			// documents it recommended 1 to 5

		if (addresses != null && addresses.size() > 0) {
			
			String addr = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			
			address = addr + "," + city + "," + state;

		}

		return address;

	}

	public void setAlaram() {
		
		long currenttime = System.currentTimeMillis();
		long time = 1200000;
		//long time = 40000;
		long finaltime = currenttime + time;
		PendingIntent pendingIntent;
		Intent alarmIntent = new Intent(DailogActivity.this,
				AlarmReciever.class);

		pendingIntent = PendingIntent.getService(DailogActivity.this, 1,
				alarmIntent, 0);
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		manager.set(AlarmManager.RTC_WAKEUP, finaltime, pendingIntent);
		Log.e("finaltime", "" + finaltime);

		Log.e("cuurenttime", "" + currenttime);
	}

	public void StartTime() {
		
		Time time = new Time();
		time.setToNow();
		// starttime = +time.hour + ":" + time.minute + ":" + time.second;

		starttime = "" + String.format("%02d", +time.hour) + ":"
				+ String.format("%02d", +time.minute) + ":"
				+ String.format("%02d", +time.second);
		// String stm=String.format("",starttime);
		Log.e("starttime", starttime);

	}

	public void currentDate() {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		date = df.format(c.getTime());
		Log.e("date", date);
	}
	
	@Override
	public void onBackPressed() {
	
	}
	
	void connectPlayManager() {

		if (AppSingleTon.PLAY_MANAGER.isPlayAvailable()) {
			AppSingleTon.PLAY_MANAGER.tryToConnect(new PlayCallback() {
				@Override
				public void onGoogleApiClientReady() {
					// ready to start continuous location fetching or GCM
					AppSingleTon.PLAY_MANAGER
							.startLocationListening(getApplication());
				}

				@Override
				public void onGoogleApiClientConnectionFail(
						ConnectionResult result) {
					AppSingleTon.PLAY_MANAGER
							.startConnectionFailResolution(result);
				}
			});
		} else {
			AppSingleTon.PLAY_MANAGER.startNoPlayResolution();
		}
	}
	
	
//	private class SaveData extends AsyncTask<String, Integer, String> {
//		  ProgressDialog PDialog;
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//		    PDialog = ProgressDialog.show(getApplication(), "", "Please wait...");
//            PDialog.show();
//
//		}
//		@Override
//		protected String doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			PDialog.dismiss();
//			finish();
//		}
//		
//	}
	

}
