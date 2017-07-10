package com.yukti.facerecognization.localdatabase;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FP_MODES;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.PREVIEW_ROTATION_ANGLE;
import com.yukti.driveherenew.R;

public class LiveRecognitionLocal extends ActionBarActivity implements
		Camera.PreviewCallback {

	Camera cameraObj; // Accessing the Android native Camera.
	FrameLayout preview;
	CameraSurfacePreviewLocal mPreview;
	private int FRONT_CAMERA_INDEX = 1;
	private int BACK_CAMERA_INDEX = 0;
	private OrientationEventListener orientationListener;
	private FacialProcessing faceObj;
	private int frameWidth;
	private int frameHeight;
	private boolean cameraFacingFront = true;
	private static PREVIEW_ROTATION_ANGLE rotationAngle = PREVIEW_ROTATION_ANGLE.ROT_90;
	private DrawViewLocal drawView;
	private FaceData[] faceArray; // Array in which all the face data values
									// will be returned for each face detected.
	private ImageView switchCameraButton;
	private Vibrator vibrate;
	String URL, taskName, taskId;
	SettingStore ss;
	String userId, name, status;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_recognition_local);

		ss = new SettingStore(getBaseContext());
		
		DrawViewLocal.id = "";
		// Work Remain
		// URL = Constants.URL_LOGIN;
		faceObj = FacialRecognitionActivityLocal.faceObj;

		switchCameraButton = (ImageView) findViewById(R.id.camera_facing);
		vibrate = (Vibrator) LiveRecognitionLocal.this
				.getSystemService(Context.VIBRATOR_SERVICE);

		orientationListener = new OrientationEventListener(this) {
			@Override
			public void onOrientationChanged(int orientation) {

			}
		};

		switchCameraButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibrate.vibrate(80);
				if (cameraFacingFront) {
					switchCameraButton
							.setImageResource(R.drawable.camera_facing_back);
					cameraFacingFront = false;
				} else {
					switchCameraButton
							.setImageResource(R.drawable.camera_facing_front);
					cameraFacingFront = true;
				}

				stopCamera();
				startCamera();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.live_recognition, menu);
		return true;
	}

	/*
	 * Stops the camera preview. Releases the camera. Make the objects null.
	 */
	private void stopCamera() {

		if (cameraObj != null) {
			cameraObj.stopPreview();
			cameraObj.setPreviewCallback(null);
			preview.removeView(mPreview);
			cameraObj.release();
		}
		cameraObj = null;
	}

	/*
	 * Method that handles initialization and starting of camera.
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void startCamera() {

		if (cameraFacingFront) {
			try {
				cameraObj = Camera.open(FRONT_CAMERA_INDEX); // Open the Front
				// camera
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {

			cameraObj = Camera.open(BACK_CAMERA_INDEX); // Open the back camera

		}

		mPreview = new CameraSurfacePreviewLocal(LiveRecognitionLocal.this,
				cameraObj, orientationListener); // Create a new surface on
													// which Camera will be
													// displayed.
		preview = (FrameLayout) findViewById(R.id.cameraPreview2);

		preview.addView(mPreview);
		cameraObj.setPreviewCallback(LiveRecognitionLocal.this);
		frameWidth = cameraObj.getParameters().getPreviewSize().width;
		frameHeight = cameraObj.getParameters().getPreviewSize().height;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (carId == R.carId.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
		super.onPause();
		stopCamera();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onResume() {
		super.onResume();
		if (cameraObj != null) {
			stopCamera();
		}
		startCamera();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		boolean result = false;
		faceObj.setProcessingMode(FP_MODES.FP_MODE_VIDEO);
		if (cameraFacingFront) {
			result = faceObj.setFrame(data, frameWidth, frameHeight, true,
					rotationAngle);
		} else {
			result = faceObj.setFrame(data, frameWidth, frameHeight, false,
					rotationAngle);
		}
		if (result) {

			int numFaces = faceObj.getNumFaces();

			if (numFaces == 0) {
				Log.d("TAG", "No Face Detected");
				if (drawView != null) {
					preview.removeView(drawView);
					drawView = new DrawViewLocal(this, null, false);
					preview.addView(drawView);
				}
			} else {

				faceArray = faceObj.getFaceData();

				if (faceArray == null) {
					Log.e("TAG", "Face array is null");
				} else {
					int surfaceWidth = mPreview.getWidth();
					int surfaceHeight = mPreview.getHeight();
					faceObj.normalizeCoordinates(surfaceWidth, surfaceHeight);
					preview.removeView(drawView); // Remove the previously
													// created view to avoid
													// unnecessary stacking of
													// Views.
					drawView = new DrawViewLocal(this, faceArray, true);
					String s = drawView.id;
					Log.e("Id", s);

					String Email, password;

					if (s != "-111") {

							Log.e("Id-1", s);
							DBController dbController = new DBController(
									getApplicationContext());

							boolean faceExist = dbController
									.checkFaceidexist(s);

							if (faceExist) {

								int i = Integer.parseInt(s);

								HashMap<String, String> empDetails = dbController
										.get_user(s);
								Log.e("s", s);

								Email = empDetails.get("Email");
								password = empDetails.get("password");
								Log.e("Email", Email);
								Log.e("pass", password);

//								Toast.makeText(LiveRecognitionLocal.this,
//										Email + "  " + password,
//										Toast.LENGTH_SHORT).show();
							
								
							  Intent output = new Intent();
							  output.putExtra("Email", Email);
							  output.putExtra("pass", password);
							  setResult(RESULT_OK, output);
							  finish();

								// Work remain
								// new loginData(Email, password).execute("");
							}

						

					}

					preview.addView(drawView);
				}
			}

		}
	}

	// // ArrayList<TaskDetails> taskList = new ArrayList<TaskDetails>();
	// //
	// public class loginData extends AsyncTask<String, Integer, String> {
	//
	// ProgressDialog PDialog;
	// private String username, password;
	//
	// public loginData(String username, String password) {
	//
	// this.username = username;
	// this.password = password;
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	//
	// PDialog = ProgressDialog.show(LiveRecognition.this, "",
	// "Please wait...");
	// PDialog.show();
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	//
	// String param = JsonRequest.loginData(username, password);
	// String result = "";
	//
	// try {
	// String query = URLEncoder.encode(param, "utf-8");
	// String url = URL + "str=" + query;
	// Log.e("URL", url);
	// result = HttpProcess.postDataOnServer(url);
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// result = "-1";
	// }
	//
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	//
	// if (result.equals("-1") || result == ""
	// || result.trim().length() <= 0) {
	// PDialog.dismiss();
	// Common.showAlertDialog(LiveRecognition.this, Constants.MSG_TITLE_INFO,
	// Constants.MSG_BAD_RESPONSE, true, false);
	// return;
	// }
	// LoginSuccess loginSuccess = JsonParser
	// .readLoginSuccessResponse(result);
	// if (loginSuccess == null) {
	// PDialog.dismiss();
	// Common.showAlertDialog(LiveRecognition.this, Constants.MSG_TITLE_INFO,
	// Constants.MSG_BAD_RESPONSE, true, false);
	// return;
	// } else {
	// if (loginSuccess.getStatus().equals("0")) {
	// Common.showAlertDialog(LiveRecognition.this,
	// Constants.MSG_TITLE_INFO, loginSuccess.getMsg(),
	// false, false);
	// PDialog.dismiss();
	// } else {
	//
	// if (taskList == null || taskList.size() <= 0) {
	// taskList.add(JsonParser.readTaskDetailsResponse(result));
	// for (int i = 0; i < taskList.size(); i++) {
	// taskName = taskList.get(i).getType();
	// taskId = taskList.get(i).getTid();
	// }
	//
	// }
	//
	// PDialog.dismiss();
	// Intent intent1 = new Intent(LiveRecognition.this, StartClocking.class);
	// name = loginSuccess.getName();
	// userId = loginSuccess.getId();
	// status = loginSuccess.getStatus();
	//
	// ss.setPreUserName(name);
	// ss.setPreUserId(userId);
	// ss.setPRE_Status(status);
	// ss.setPreUserName(name);
	//
	// intent1.putExtra("username", name);
	// intent1.putExtra("taskName", taskName);
	// intent1.putExtra("taskId", taskId);
	// startActivity(intent1);
	// LiveRecognition.this.finish();
	// }
	// }
	//
	// }
	//
	// }

}
