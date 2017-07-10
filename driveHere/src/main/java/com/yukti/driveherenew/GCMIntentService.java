package com.yukti.driveherenew;

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.http.Header;
import org.json.JSONObject;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.jsonparser.Login;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.RestClient;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */

public class GCMIntentService extends GCMBaseIntentService {

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		// displayMessage(context, getString(R.string.gcm_registered));
		// SettingStore setting=SettingStore.getInstance(context);
		// setting.setGCM_TOKEN(registrationId);
		GCMRegistrar.setRegisteredOnServer(this, false);
		sendGCM(registrationId);
	}

	private void sendGCM(String registrationId) {
		// new registerInBackground(registrationId).execute("");

		registerInBackground(registrationId);
		Log.e("GCM ID", "GCM ID :: " + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		// displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			// ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */

	// public class registerInBackground extends
	// AsyncTask<String, Integer, String> {
	//
	// String regId;
	//
	// public registerInBackground(String regId) {
	// this.regId = regId;
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	//
	// String result = "";
	// try {
	//
	// SettingStore settingStore = new SettingStore(
	// GCMIntentService.this);
	// if (settingStore.getPreLogin()
	// && settingStore.getPreUserId() != "") {
	// String uriGcm = Constants.URL_GCM_REGISTRATION + "did="
	// + settingStore.getPreUserId().trim() + "&gcmid="
	// + regId;
	// result = "";
	// try {
	// result = HttpProcess.postDataOnServer(uriGcm);
	// } catch (IOException e) {
	// e.printStackTrace();
	// result = "";
	// }
	// return result;
	// } else {
	// return "";
	// }
	// } catch (Exception ex) {
	// }
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // mDisplay.append(result + "\n");
	// Log.i(TAG,
	// "gcm response-------------------------------------------- "
	// + result);
	// if (result.equalsIgnoreCase("1")) {
	// GCMRegistrar.setRegisteredOnServer(GCMIntentService.this, true);
	// }
	// storeRegistrationId(GCMIntentService.this, regId);
	// super.onPostExecute(result);
	// }
	// }
	Context context;

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.e(TAG,
				"-------------------------------------------- Received message");
		this.context = context;
		// Toast.makeText(getApplicationContext(),
		// "yooooo",Toast.LENGTH_LONG).show();
		// ArrayList<String> s = null;
		// s.add("1");
		String not = "";
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Log.e("Notification received",
					"" + (String) extras.get("message"));
			not = "" + (String) extras.get("message");
		}

		// String message = getString(R.string.gcm_message);
		Log.e(TAG, "Received message :: " + not);
		// displayMessage(context, message);
		// notifies user
		// generateNotification(context, not);
		// Notify("DriveHere", "New Version Update");
		//		notification();
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		// String message = getString(R.string.gcm_deleted, total);
		// displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		// displayMessage(context,
		// getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		// int appVersion = Common.getAppVersionInt(context);
		// Log.i("storeRegistrationId", "Saving regId on app version "
		// + appVersion);
		// SettingStore settingStore = new SettingStore(GCMIntentService.this);
		// settingStore.setPRE_REG_ID(regId);
		// settingStore.setPRE_REG_APP_VERSION(appVersion);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static final int NOTIFICATION_ID = 1;

	// private void generateNotification(Context context, String message) {
	//
	// CommonObject common = null;
	// try {
	// common = JsonParser.getCommonResponse(message);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// if (common != null) {
	// mNotificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// String msg_display = common.getMsg();
	// if (common.getPushType().equals("11")) {
	// Intent newTripIntent = new Intent(context,
	// TaxiPassengerRequestActivity.class);
	// newTripIntent.putExtra("RES_GCM", common.toString());
	// PendingIntent contentIntent = PendingIntent.getActivity(
	// context, (int) System.currentTimeMillis(),
	// newTripIntent, 0);
	// Uri uri = Uri.parse("android.resource://" + getPackageName()
	// + "/raw/taxi_whistle");
	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
	// context)
	// .setSmallIcon(R.drawable.ic_launcher)
	// .setContentTitle(
	// context.getResources().getString(
	// R.string.new_trip))
	// .setAutoCancel(true)
	// .setStyle(
	// new NotificationCompat.BigTextStyle()
	// .bigText(context
	// .getResources()
	// .getString(
	// R.string.you_got_new_trip_notification_)))
	// .setContentText(
	// context.getString(R.string.you_got_new_trip_notification_))
	// .setSound(uri);
	//
	// mBuilder.setContentIntent(contentIntent);
	// mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	// } else if (common.getPushType().equals("12")) {
	// Intent newTripIntent = new Intent(context,
	// TripListActivity.class);
	// newTripIntent.putExtra("RES_GCM", common.toString());
	// PendingIntent contentIntent = PendingIntent.getActivity(
	// context, (int) System.currentTimeMillis(),
	// newTripIntent, 0);
	// Uri uri = Uri.parse("android.resource://" + getPackageName()
	// + "/raw/taxi_whistle");
	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
	// context)
	// .setSmallIcon(R.drawable.ic_launcher)
	// .setContentTitle(getString(R.string.app_name))
	// .setAutoCancel(true)
	// .setStyle(
	// new NotificationCompat.BigTextStyle()
	// .bigText(msg_display))
	// .setContentText(msg_display).setSound(uri);
	//
	// mBuilder.setContentIntent(contentIntent);
	// mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	// }
	// } else {
	// try {
	// Uri uri = Uri.parse("android.resource://" + getPackageName()
	// + "/raw/taxi_whistle");
	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
	// context)
	// .setSmallIcon(R.drawable.ic_launcher)
	// .setContentTitle(getString(R.string.app_name))
	// .setAutoCancel(true)
	// .setStyle(
	// new NotificationCompat.BigTextStyle()
	// .bigText(message))
	// .setContentText(message).setSound(uri);
	// mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	// } catch (Exception e) {
	// }
	// }
	// }

	protected void registerInBackground(String regId) {

		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.d("regID error", responseString);
				// showToast("regID error");
				// dismissProgressDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.d("regID_response", response.toString());

				Login orm = AppSingleTon.APP_JSON_PARSER.login(response);

				if (orm.status_code.equals("1")) {
					Log.e("Home fragment",
							"gcm response-------------------------------------------- "
									+ orm.msg);
					// showToast("yooo");
					GCMRegistrar.setRegisteredOnServer(getApplication(), true);

				} else if (orm.status_code.equals("2")) {
					// showEmailErrorText(orm.msg);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				// showUpdateProgressDialog("regID......");
			}

			@Override
			public void onFinish() {
				super.onFinish();
				// dismissProgressDialog();

			}

		};

		// setgcmid.php
		// carId,gcmid

		String url = AppSingleTon.APP_URL.URL_REG_ID;
		RequestParams params = new RequestParams();

		Log.e("uid", AppSingleTon.SHARED_PREFERENCE.getUserId());
		params.put("carId", AppSingleTon.SHARED_PREFERENCE.getUserId());
		params.put("gcmid", regId);

		RestClient.post(this, url, params, handler);

	}

	// private void Notify(String notificationTitle, String notificationMessage)
	// {
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(NOTIFICATION_SERVICE);
	// @SuppressWarnings("deprecation")
	// Notification notification = new Notification(R.drawable.app_icon,
	// "New Message", System.currentTimeMillis());
	// Intent notificationIntent = new Intent(
	// Intent.ACTION_VIEW,
	// Uri.parse("https://play.google.com/store/apps/details?id=com.yukti.drivehere"));
	//
	// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
	// notificationIntent, 0);
	// notification.setLatestEventInfo(context, notificationTitle,
	// notificationTitle, contentIntent);
	// notificationManager.notify(970970, notification);
	//
	// // Intent notificationIntent = new Intent(this,NotificationView.class);
	// // PendingIntent pendingIntent = PendingIntent.getActivity(this,
	// // 0,notificationIntent, 0);
	// //
	// // notification.setLatestEventInfo(MainActivity.this,
	// // notificationTitle,notificationTitle, pendingIntent);
	// // notificationManager.notify(9999, notification);
	// }


//	public void notification() {
//		NotificationManager mgr = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		// if (!distanceFromJson.contains("stop") ||
//		// !distanceFromJson.contains("stops")) {
//		Notification note = new Notification(R.drawable.app_icon,
//				"Tap Here to get update.", System.currentTimeMillis());
//		// } else if (milesReminder != null && milesReminder.length() > 0) {
//		// note = new Notification(R.drawable.ic_launcher, "Your Bus is " +
//		// milesReminder, System.currentTimeMillis());
//		// }
//
//		note.defaults |= Notification.DEFAULT_VIBRATE;
//		note.defaults |= Notification.DEFAULT_SOUND;
//
//		Intent notificationIntent = new Intent(
//				Intent.ACTION_VIEW,
//				Uri.parse("https://play.google.com/store/apps/details?id=com.yukti.drivehere"));
//
//		PendingIntent pi = PendingIntent.getActivity(this, 0,
//				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		// if (stopReminder != null && stopReminder.length() > 0) {
//		note.setLatestEventInfo(getApplicationContext(), getResources()
//				.getString(R.string.app_name), "Tap Here to get update.", pi);
//		// } else if (milesReminder != null && milesReminder.length() > 0) {
//		// note.setLatestEventInfo(context, "NYC Bus Time Pro", "Your Bus is " +
//		// milesReminder, i);
//		// }
//
//		note.flags |= Notification.FLAG_AUTO_CANCEL;
//		mgr.notify(0, note);
//		Log.e("Tap Here to get update.", "" + 0);
//
//	}

}
