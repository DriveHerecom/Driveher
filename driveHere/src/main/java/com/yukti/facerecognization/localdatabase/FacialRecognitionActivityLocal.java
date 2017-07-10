package com.yukti.facerecognization.localdatabase;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FP_MODES;
import com.yukti.driveherenew.R;

public class FacialRecognitionActivityLocal extends Activity {

	public static FacialProcessing faceObj;
	public final String TAG = "FacialRecognitionActivity";
	public final int confidence_value = 58;
	public static boolean activityStartedOnce = false;
	public static final String ALBUM_NAME = "serialize_deserialize_local";

	public static final int REQ_LIVE_REQ = 1200;

	String Email, password;
	Boolean isaddPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facial_recognition_local);
   
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isaddPhoto = extras.getBoolean("isaddPhoto");
			Email = extras.getString("Email");
			password = extras.getString("password");
		}

		if (!activityStartedOnce) {
			activityStartedOnce = true;
			faceObj = (FacialProcessing) FacialProcessing.getInstance();
			loadAlbum(); // De-serialize a previously stored album.
			if (faceObj != null) {
				faceObj.setRecognitionConfidence(confidence_value);
				faceObj.setProcessingMode(FP_MODES.FP_MODE_STILL);
			}
		}

		if (isaddPhoto == true) {
			addNewPerson();
		} else {
			liveRecognition();
		}

	}

	/*
	 * Method to handle adding a new person to the recognition album
	 */
	private void addNewPerson() {
		Intent intent = new Intent(this, AddPhotoLocal.class);
		intent.putExtra("Username", "null");
		intent.putExtra("PersonId", -1);
		intent.putExtra("UpdatePerson", false);
		intent.putExtra("IdentifyPerson", false);
		intent.putExtra("Email", Email);
		intent.putExtra("password", password);
		startActivityForResult(intent, 1);
	}

	/*
	 * Method to handle live identification of the people
	 */

	private void liveRecognition() {
		Intent intent = new Intent(this, LiveRecognitionLocal.class);
		startActivityForResult(intent, REQ_LIVE_REQ);
	}

	public void loadAlbum() {
		SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		String arrayOfString = settings.getString("albumArray", null);

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
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			setResult(Activity.RESULT_OK);
			finish();

		} else if (requestCode == 1 && resultCode == Activity.RESULT_CANCELED) {
			setResult(Activity.RESULT_CANCELED);
			finish();
		} else if (requestCode == REQ_LIVE_REQ) {
			if (resultCode == RESULT_OK) {
				
				Intent output = new Intent();
				output.putExtra("Email", data.getStringExtra("Email"));
				output.putExtra("pass", data.getStringExtra("pass"));
				setResult(RESULT_OK, output);
				finish();
				
			} else {
				setResult(Activity.RESULT_CANCELED);
				finish();	
			}

		}

	}

}
