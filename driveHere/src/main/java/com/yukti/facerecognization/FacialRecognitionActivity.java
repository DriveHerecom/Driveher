/*
 * =========================================================================
 * Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 * Qualcomm Technologies Proprietary and Confidential.
 * =========================================================================
 * @file FacialRecognitionActivity.java
 */

package com.yukti.facerecognization;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FEATURE_LIST;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FP_MODES;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;

public class FacialRecognitionActivity extends Activity {

	public static FacialProcessing faceObj;
	public final String TAG = "FacialRecognitionActivity";
	public final int confidence_value = 58;
	public static boolean activityStartedOnce = false;
	public static final String ALBUM_NAME = "serialize_deserialize";
	public static final int ADD_PHOTO_REQUEST = 305;

	public static final int LIVE_RECG_REQUEST = 405;
	boolean isFromEditCar = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facial_recognition);

		Bundle extras = getIntent().getExtras();
		boolean isAddPhoto = extras.getBoolean("isaddPhoto", false);

		isFromEditCar = extras.getBoolean("isFromEditCar", false);

		if (!activityStartedOnce) // Check to make sure FacialProcessing object
									// is not created multiple times.
		{
			activityStartedOnce = true;
			// Check if Facial Recognition feature is supported in the device
			boolean isSupported = FacialProcessing
					.isFeatureSupported(FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
			if (isSupported) {
				Log.d(TAG, "Feature Facial Recognition is supported");
				faceObj = (FacialProcessing) FacialProcessing.getInstance();

				if (isAddPhoto) {

				} else {
					// faceObj.resetAlbum();
					// loadAlbumForLiveRecg(AppSingleTon.SHARED_PREFERENCE
					// .getUserFace());
					// loadAlbum(); // De-serialize a previously stored album.
				}

				if (faceObj != null) {

					faceObj.setRecognitionConfidence(confidence_value);
					faceObj.setProcessingMode(FP_MODES.FP_MODE_STILL);

				}

			} else {

				Log.e(TAG, "Feature Facial Recognition is NOT supported");
				new AlertDialog.Builder(FacialRecognitionActivity.this)
						.setMessage(
								"Your device does NOT support Qualcomm's Facial Recognition feature. ")
						.setCancelable(false)
						.setNegativeButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								}).show();
			}

		}

		if (isAddPhoto) {
			addNewPerson();
		} else {

			faceObj.resetAlbum();
			loadAlbumForLiveRecg(AppSingleTon.SHARED_PREFERENCE.getUserFace());
			liveRecognition();

		}

		// Vibrator for button press
		// final Vibrator vibrate = (Vibrator) FacialRecognitionActivity.this
		// .getSystemService(Context.VIBRATOR_SERVICE);
	}

	/*
	 * Method to handle adding a new person to the recognition album
	 */
	private void addNewPerson() {

		Intent intent = new Intent(this, AddPhoto.class);
		intent.putExtra("isaddPhoto", true);
		intent.putExtra("isFromEditCar", isFromEditCar);
		// startActivity(intent);
		startActivityForResult(intent, ADD_PHOTO_REQUEST);
	}

	/*
	 * Method to handle live identification of the people
	 */
	private void liveRecognition() {
		Intent intent = new Intent(this, LiveRecognition.class);
		startActivityForResult(intent, LIVE_RECG_REQUEST);
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();

		activityStartedOnce = false;
		Log.d(TAG, "Destroyed");
		if (faceObj != null) // If FacialProcessing object is not released, then
								// release it and set it to null
		{
			faceObj.release();
			faceObj = null;
			Log.d(TAG, "Face Recog Obj released");
		} else {
			Log.d(TAG, "In Destroy - Face Recog Obj = NULL");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint("NewApi")
	protected void onResume() {
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() { // Destroy the activity to avoid stacking of
		super.onBackPressed();
		FacialRecognitionActivity.this.finishAffinity();
		activityStartedOnce = false;
	}

	public void loadAlbumForLiveRecg(String face) {

		// SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		String arrayOfString = face;

		byte[] albumArray = null;

		if (arrayOfString != null) {
			Log.e("arrayOfString", "" + arrayOfString.toString());
			String[] splitStringArray = arrayOfString.substring(1,
					arrayOfString.length() - 1).split(", ");

			albumArray = new byte[splitStringArray.length];
			for (int i = 0; i < splitStringArray.length; i++) {
				albumArray[i] = Byte.parseByte(splitStringArray[i]);
			}
			faceObj.deserializeRecognitionAlbum(albumArray);
			Log.e("TAG", "De-Serialized my album");
		}

	}

	public void loadAlbumForLiveRecgOfLogin(String face) {

		// SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		String arrayOfString = face;

		byte[] albumArray = null;

		if (arrayOfString != null) {
			Log.e("arrayOfString", "" + arrayOfString.toString());
			String[] splitStringArray = arrayOfString.substring(1,
					arrayOfString.length() - 1).split(", ");

			albumArray = new byte[splitStringArray.length];
			for (int i = 0; i < splitStringArray.length; i++) {
				albumArray[i] = Byte.parseByte(splitStringArray[i]);
			}
			faceObj.deserializeRecognitionAlbum(albumArray);
			Log.e("TAG", "De-Serialized my album");
		}

	}

	/*
	 * Function to retrieve the byte array from the Shared Preferences.
	 */
	public void loadAlbum() {

		SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		String arrayOfString = settings.getString("albumArray", null);

		if (arrayOfString != null && !arrayOfString.equals("")) {
			Log.e("arrayOfString", arrayOfString.toString());
		}

		byte[] albumArray = null;
		if (arrayOfString != null) {
			String[] splitStringArray = arrayOfString.substring(1,
					arrayOfString.length() - 1).split(", ");

			albumArray = new byte[splitStringArray.length];
			for (int i = 0; i < splitStringArray.length; i++) {
				albumArray[i] = Byte.parseByte(splitStringArray[i]);
			}

			faceObj.deserializeRecognitionAlbum(albumArray);
			Log.e("TAG", "De-Serialized my album");
		}

	}

	/*
	 * Method to save the recognition album to a permanent device memory
	 */
	public void saveAlbum() {

		byte[] albumBuffer = faceObj.serializeRecogntionAlbum();
		SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("albumArray", Arrays.toString(albumBuffer));
		editor.commit();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// user has signed up successfully and come back to login activity.*/
		if (requestCode == LIVE_RECG_REQUEST
				&& resultCode == Activity.RESULT_OK) {
			setResult(Activity.RESULT_OK);
			finish();

		} else if (requestCode == LIVE_RECG_REQUEST
				&& resultCode == Activity.RESULT_CANCELED) {

			setResult(Activity.RESULT_CANCELED);
			finish();
		} else if (requestCode == ADD_PHOTO_REQUEST
				&& resultCode == Activity.RESULT_OK) {
			setResult(Activity.RESULT_OK);
			finish();
		} else if (requestCode == ADD_PHOTO_REQUEST
				&& resultCode == Activity.RESULT_CANCELED) {
			setResult(Activity.RESULT_OK);
			finish();
		}

	}

}
