package com.yukti.driveherenew;

import com.yukti.utils.AppSingleTon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AlarmReciever extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "AlertService destroyed",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.e("service running",
				"" + AppSingleTon.SHARED_PREFERENCE.isServiceRunning());

		if (AppSingleTon.SHARED_PREFERENCE.isServiceRunning()) {
			Intent in = new Intent(this,DailogActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(in);
			stopSelf();
		}
		return START_STICKY;
	}
}
