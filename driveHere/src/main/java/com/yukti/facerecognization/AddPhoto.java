/*
 * =========================================================================
 * Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 * Qualcomm Technologies Proprietary and Confidential.
 * =========================================================================
 * @file AddPhoto.java
 */

package com.yukti.facerecognization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yukti.driveherenew.R;

public class AddPhoto extends Activity implements Camera.PreviewCallback {

	private ImageView cameraButton; // ImageView to click a photo
	private ImageView settingsButton; // ImageView to access settings like flash
										// and
	// camera switch
	private ImageView menuBar; // ImageView when settings button is clicked
	private ImageView switchCameraButton; // ImageView to switch the camera back
											// and
	// front
	private ImageView flashButton; // ImageView to access device flash
	private Bitmap bitmap;
	private Camera cameraObj; // Accessing the Android native Camera.
	private FrameLayout preview; // Layout on which camera surface is displayed
	private CameraSurfacePreview mPreview;
	private Animation animationFadeOut; // Fade out animation
	private Vibrator vibrate; // Vibrate on button click
	private OrientationEventListener orientationListener; // Accessing device
	// orientation
	private int FRONT_CAMERA_INDEX = 1;
	private int BACK_CAMERA_INDEX = 0;
	private int lastAngle = 0;
	private int rotationAngle = 0;

	private static int displayAngle;
	private static boolean cameraFacingFront = true;
	private static boolean activityStartedOnce = false;

	private static boolean settingsButtonClicked = false;
	private static boolean flashButtonClicked = false;
	private static boolean shutterButtonClicked = false;

	private final String TAG = "AddPhoto";
	boolean isFromEditCar = false;

	public static final int ADD_PHOTO_REQUEST = 105;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_photo);

		Bundle extras = getIntent().getExtras();
		isFromEditCar = extras.getBoolean("isFromEditCar", false);

		vibrate = (Vibrator) AddPhoto.this
				.getSystemService(Context.VIBRATOR_SERVICE);

		initializeImageButtons(); // Initializes the Image buttons
		setAppropriateTitle(); // This method sets the title based on which
								// operation you are performing.
		animationFadeOut = AnimationUtils.loadAnimation(AddPhoto.this,
				R.anim.fadeout); // For fade out animation

		// Rotate the buttons based on the display to avoid expensive camera
		// open/close operations
		orientationListener = new OrientationEventListener(this,
				SensorManager.SENSOR_DELAY_NORMAL) {
			@Override
			public void onOrientationChanged(int arg0) {
				int prevAngle = lastAngle * 90;
				if (arg0 - prevAngle < 0) {
					arg0 += 360;
				}

				// Only shift if > 60 degree deviance
				if (Math.abs(arg0 - prevAngle) < 60)
					return;

				int angle = ((arg0 + 45) % 360) / 90;

				if (lastAngle == angle) {
					return;
				}
				lastAngle = angle;

				switch (angle) {
				case 0: // portrait
					rotateButtons(0);
					displayAngle = 0;
					break;
				case 1: // landscape right
					rotateButtons(-90);
					displayAngle = 270;
					break;
				case 2: // upside-down
					rotateButtons(180);
					displayAngle = 180;
					break;
				case 3: // landscape left
					rotateButtons(90);
					displayAngle = 90;
					break;
				}
			}
		};

		if (!activityStartedOnce) {
			startCamera();
		}

		cameraButtonActionListener();
		settingsButtonActionListener();
		switchCameraActionListener();
		flashButtonActionListener();
		activityStartedOnce = true;
	}

	/*
	 * Method to initialize all the necessary image buttons
	 */
	public void initializeImageButtons() {

		menuBar = (ImageView) findViewById(R.id.menu_bar);
		menuBar.setVisibility(View.GONE);
		switchCameraButton = (ImageView) findViewById(R.id.switch_camera);
		switchCameraButton.setVisibility(View.GONE);
		flashButton = (ImageView) findViewById(R.id.flash);
		flashButton.setVisibility(View.GONE);
		if (flashButtonClicked) {
			flashButton.setImageResource(R.drawable.flash_on);
		} else {
			flashButton.setImageResource(R.drawable.flash_off);
		}

		settingsButton = (ImageView) findViewById(R.id.settings_button);
	}

	/*
	 * Method to rotate the buttons when the device orientation changes.
	 */
	@SuppressLint("NewApi")
	private void rotateButtons(int angle) {

		rotateButton(cameraButton, angle, true);
		rotateButton(settingsButton, angle, true);
		switchCameraButton.setRotation(angle);
		flashButton.setRotation(angle);
	}

	/*
	 * Method to rotate the button with animation.
	 */
	private void rotateButton(View buttonView, int angle, boolean animated) {
		int start = rotationAngle % 360;
		int finish = angle;

		while (Math.abs(finish - start) > 180) {
			if (finish > 0) {
				finish -= 360;
			} else {
				finish += 360;
			}
		}

		RotateAnimation rotate = new RotateAnimation(start, finish,
				buttonView.getWidth() / 2, buttonView.getHeight() / 2);
		rotate.setRepeatMode(Animation.REVERSE);
		rotate.setRepeatCount(0);
		if (animated) {
			rotate.setDuration(250L);
		} else {
			rotate.setDuration(0L);
		}
		rotate.setInterpolator(new AccelerateDecelerateInterpolator());
		rotate.setFillAfter(true);
		buttonView.startAnimation(rotate);
	}

	/*
	 * Action listener method for the switch camera button.
	 */
	private void switchCameraActionListener() {

		switchCameraButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vibrate.vibrate(80);
				fadeOutAnimation();
				if (cameraFacingFront) {
					switchCameraButton
							.setImageResource(R.drawable.camera_facing_back);
					cameraFacingFront = false;
				} else {
					switchCameraButton
							.setImageResource(R.drawable.camera_facing_front);
					flashButton.setVisibility(View.GONE);
					cameraFacingFront = true;
				}
				stopCamera();
				startCamera();
			}
		});
	}

	/*
	 * Method to handle the fade out animation when any of the menu items is
	 * clicked.
	 */
	private void fadeOutAnimation() {

		menuBar.startAnimation(animationFadeOut);
		switchCameraButton.startAnimation(animationFadeOut);

		if (!cameraFacingFront) {
			flashButton.startAnimation(animationFadeOut);
		}
		settingsButtonClicked = false;
		menuBar.setVisibility(View.GONE);
		switchCameraButton.setVisibility(View.GONE);
		flashButton.setVisibility(View.GONE);
	}

	/*
	 * Action listener method for the flash button.
	 */
	private void flashButtonActionListener() {

		flashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vibrate.vibrate(80);
				Camera.Parameters params = cameraObj.getParameters();
				String flashMode = params.getFlashMode();
				if (flashMode == null) {
					return;
				} else {
					if (!flashButtonClicked) // If flash OFF then turn it ON
					{
						params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
						flashButton.setImageResource(R.drawable.flash_on);
						cameraObj.setParameters(params);
						fadeOutAnimation();
						flashButtonClicked = true;
						settingsButtonClicked = false;
						return;
					} else // If already ON then, make it OFF and change the
							// icon to OFF
					{

						params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						flashButton.setImageResource(R.drawable.flash_off);
						cameraObj.setParameters(params);
						fadeOutAnimation();
						flashButtonClicked = false;
						settingsButtonClicked = false;
						return;

					}
				}
			}
		});

	}

	/*
	 * Action listener method for the gallery button.
	 */
	private void cameraButtonActionListener() {
		cameraButton = (ImageView) findViewById(R.id.shutter_button);
		cameraButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vibrate.vibrate(80);
				shutterButtonClicked = true;
			}
		});
	}

	/*
	 * Action listener method for the gallery button.
	 */
	private void settingsButtonActionListener() {

		settingsButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				vibrate.vibrate(80);
				if (!settingsButtonClicked) {
					menuBar.setVisibility(View.VISIBLE);
					switchCameraButton.setVisibility(View.VISIBLE);
					switchCameraButton.setRotation(displayAngle);
					if (cameraFacingFront) {
						flashButton.setVisibility(View.GONE);
					} else {
						flashButton.setVisibility(View.VISIBLE);
					}

					settingsButtonClicked = true;
				} else {
					menuBar.setVisibility(View.GONE);
					switchCameraButton.setVisibility(View.GONE);
					flashButton.setVisibility(View.GONE);
					settingsButtonClicked = false;
				}
			}
		});
	}

	protected void onPause() {
		super.onPause();
		stopCamera();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (orientationListener != null)
			orientationListener.disable();
	}

	protected void onResume() {
		super.onResume();
		if (cameraObj != null) {
			stopCamera();
		}
		startCamera();
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
	private void startCamera() {
		
		if (cameraFacingFront) {
			cameraObj = Camera.open(FRONT_CAMERA_INDEX); // Open the Front
		} else {
			cameraObj = Camera.open(BACK_CAMERA_INDEX); // Open the back camera
		}
		mPreview = new CameraSurfacePreview(AddPhoto.this, cameraObj,
				orientationListener); // Create a new surface on which Camera
										// will be displayed.
		preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);
		cameraObj.setPreviewCallback(AddPhoto.this);
	}

	/*
	 * Method to set the appropriate header title based on which parent class it
	 * is coming from.
	 */
	private void setAppropriateTitle() {
		setTitle("Add Person");
	}

	@Override
	public void onPreviewFrame(byte[] buffer, Camera camera) {

		if (shutterButtonClicked) {
			shutterButtonClicked = false;
			camera.takePicture(shutterCallback, rawCallback, jpegCallback);
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d("TAG", "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("TAG", "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			usePicture(data);
		}

	};

	private void usePicture(byte[] data) {
		Intent intent = new Intent(this, ImageConfirmation.class);
		if (data != null) {
			intent.putExtra(
					"com.qualcomm.sdk.recognition.sample.ImageConfirmation",
					data);
		}
		intent.putExtra(
				"com.qualcomm.sdk.recognition.sample.ImageConfirmation.switchCamera",
				cameraFacingFront);
		intent.putExtra(
				"com.qualcomm.sdk.recognition.sample.ImageConfirmation.orientation",
				displayAngle);

		intent.putExtra("isFromEditCar", isFromEditCar);

		startActivityForResult(intent, ADD_PHOTO_REQUEST);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		if (isFromEditCar) {

			
		} else {

//			Intent intent = new Intent(AddPhoto.this,
//					com.yukti.drivehere.MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//			startActivity(intent);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/* user has signed up successfully and come back to login activity. */
		if (requestCode == ADD_PHOTO_REQUEST
				&& resultCode == Activity.RESULT_OK) {
			// AppSingleTon.SHARED_PREFERENCE.login(true);
//			Intent intent = new Intent(AddPhoto.this,
//					com.yukti.drivehere.MainActivity.class);
//
//			startActivity(intent);
			setResult(Activity.RESULT_OK);
			finish();
		}
	}
}
