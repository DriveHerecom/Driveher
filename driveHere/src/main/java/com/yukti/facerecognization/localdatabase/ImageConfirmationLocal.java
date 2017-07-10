package com.yukti.facerecognization.localdatabase;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.yukti.driveherenew.R;

public class ImageConfirmationLocal extends ActionBarActivity {

	public static final String HASH_NAME = "HashMap";

	private ImageView trashButton; // ImageView for discarding the presently
									// loaded
	// image
	private ImageView confirmButton; // ImageView to confirm the presently
										// loaded image
	// private ImageView homeButton; // ImageView to go to Home Screen
	// (FacialRecognitionActivity)
	private ImageView confirmationView; // ImageView to display the selected
										// image

	public FacialRecognitionActivityLocal faceRecog;
	private static Bitmap storedBitmap;
	private Bitmap mutableBitmap; // Temporary mutable bitmap
	private static FacialProcessing faceObj;
	private static Rect[] arrayOfRects; // A temporary array that will store the
										// face rects.

	private FaceData[] faceDataArray;
	private Vibrator vibrate;
	private int arrayPosition;
	private int personId;
	private boolean identifyPerson = false;
	private boolean inputNameFlag = true;
	private boolean updatePerson = false;
	private static boolean faceFoundFlag = false;
	private String userName;
	private final String TAG = "ImageConfirmation.java";
	private static final String ALBUM_NAME = "serialize_deserialize_local";

	DBController dbcontroller;

	public String Email, password;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_confirmation_local);
		Bundle extras = getIntent().getExtras();
		dbcontroller = new DBController(getApplicationContext());

		byte[] data = getIntent().getByteArrayExtra(
				"com.creadigol.mekanikal.ImageConfirmation");
		int angle = extras
				.getInt("com.creadigol.mekanikal.ImageConfirmation.orientation");
		boolean cameraFacingFront = extras
				.getBoolean("com.creadigol.mekanikal.ImageConfirmation.switchCamera");
		/*
		 * boolean throughGallery = extras // If the image is coming through the
		 * gallery .getBoolean(
		 * "com.qualcomm.sdk.recognition.sample.ImageConfirmation.through.gallery"
		 * );
		 */
		Email = extras.getString("Email");
		password = extras.getString("password");
		updatePerson = extras.getBoolean("UpdatePerson");
		personId = extras.getInt("PersonId");
		userName = extras.getString("Username");
		identifyPerson = extras.getBoolean("IdentifyPerson");
		// New view on which the image will be displayed
		confirmationView = (ImageView) findViewById(R.id.confirmationView);
		faceObj = FacialRecognitionActivityLocal.faceObj;
		storedBitmap = BitmapFactory
				.decodeByteArray(data, 0, data.length, null);
		vibrate = (Vibrator) ImageConfirmationLocal.this
				.getSystemService(Context.VIBRATOR_SERVICE);
		faceRecog = new FacialRecognitionActivityLocal();

		Options bitmapOptions = new Options();
		bitmapOptions.inMutable = true;
		Matrix mat = new Matrix();

		if (cameraFacingFront) // Rotate the bitmap image based on the device
		// orientation
		{
			/*
			 * if (throughGallery) { mat.postRotate(angle == 90 ? 90 : (angle ==
			 * 180 ? 180 : 0)); storedBitmap = Bitmap.createBitmap(storedBitmap,
			 * 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), mat,
			 * true); } else
			 */
			{
				mat.postRotate(angle == 0 ? 270 : angle == 270 ? 180
						: (angle == 180 ? 180 : 0));
				mat.postScale(-1, 1);
				storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0,
						storedBitmap.getWidth(), storedBitmap.getHeight(), mat,
						true);
			}
		} else {
			/*
			 * if (throughGallery) { mat.postRotate(angle == 90 ? 90 : (angle ==
			 * 180 ? 180 : 0)); } else
			 */
			{
				mat.postRotate(angle == 0 ? 90 : angle == 270 ? 180
						: (angle == 180 ? 180 : 0));
			}
			storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0,
					storedBitmap.getWidth(), storedBitmap.getHeight(), mat,
					true);
		}

		Bitmap tempBitmap = Bitmap.createScaledBitmap(storedBitmap,
				(storedBitmap.getWidth() / 2), (storedBitmap.getHeight() / 2),
				false);
		confirmationView.setImageBitmap(tempBitmap); // Setting the view with
														// the bitmap image that
														// came in.
		// If selected image is landscape then change the display view to
		// landscape or else change to portrait.
		if (storedBitmap.getWidth() > storedBitmap.getHeight()) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		homeButtonOnClickListener();
		confirmButtonOnClickListener();
		trashButtonOnClickListener();
		confirmationViewOnTouchListener();
	}

	/*
	 * Action listener button for the trash button on the screen
	 */
	private void confirmButtonOnClickListener() {
		confirmButton = (ImageView) findViewById(R.id.approve);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				// vibrate.vibrate(85);
				int imageViewSurfaceWidth = confirmationView.getWidth();
				int imageViewSurfaceHeight = confirmationView.getHeight();

				Bitmap workingBitmap = Bitmap.createScaledBitmap(storedBitmap,
						imageViewSurfaceWidth, imageViewSurfaceHeight, false);
				mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888,
						true);

				boolean result = faceObj.setBitmap(storedBitmap);
				// Normalize the face data coordinates based on the image that
				// is fed in.
				faceObj.normalizeCoordinates(imageViewSurfaceWidth,
						imageViewSurfaceHeight);
				if (result) // If setBitmap was successful
				{
					faceDataArray = faceObj.getFaceData();
					// If one or more face is detected
					if (faceDataArray != null) {
						// Creating a temporary rect to store the faceRects
						// returned from the faceData array
						arrayOfRects = new Rect[faceDataArray.length];
						Canvas canvas = null;
						for (int i = 0; i < faceDataArray.length; i++) {
							Rect rect = faceDataArray[i].rect;

							// Extra padding around the faeRects
							rect.set(rect.left -= 20, rect.top -= 20,
									rect.right += 20, rect.bottom += 20);
							canvas = new Canvas(mutableBitmap);

							Paint paintForRectFill = new Paint(); // Draw rect
																	// fill
							paintForRectFill.setStyle(Paint.Style.FILL);
							paintForRectFill.setColor(Color.WHITE);
							paintForRectFill.setAlpha(80);

							// Draw rect strokes
							Paint paintForRectStroke = new Paint();
							paintForRectStroke.setStyle(Paint.Style.STROKE);
							paintForRectStroke.setColor(Color.GREEN);
							paintForRectStroke.setStrokeWidth(5);
							canvas.drawRect(rect, paintForRectFill);
							canvas.drawRect(rect, paintForRectStroke);

							// Update the temporary rect array with the given
							// face rect
							// so that we can use this in the future to get the
							// corresponsing
							// faceIndex of the faces.
							arrayOfRects[i] = rect;
							if (identifyPerson) {
								String selectedPersonId = Integer
										.toString(faceDataArray[i]
												.getPersonId());
								// String personName = null;
								// Iterator<HashMap.Entry<String, String>> iter
								// = hash
								// .entrySet().iterator();
								// while (iter.hasNext()) {
								// HashMap.Entry<String, String> entry = iter
								// .next();
								// if (entry.getValue().equals(
								// selectedPersonId)) {
								// personName = entry.getKey();
								// }
								// }

								float pixelDensity = getResources()
										.getDisplayMetrics().density;
								int textSize = (int) (rect.width() / 25 * pixelDensity);
								Paint paintForText = new Paint();
								paintForText.setColor(Color.WHITE);
								paintForText.setTextSize(textSize);
								Typeface tp = Typeface.SERIF;
								Rect backgroundRect = new Rect(rect.left,
										rect.bottom, rect.right,
										(rect.bottom + textSize));
								Paint paintForTextBackground = new Paint();
								paintForTextBackground
										.setStyle(Paint.Style.FILL);
								paintForTextBackground.setColor(Color.BLACK);
								;
								paintForText.setTypeface(tp);
								paintForTextBackground.setAlpha(80);
								// if (personName != null) {
								// canvas.drawRect(backgroundRect,
								// paintForTextBackground);
								// canvas.drawText(personName, rect.left,
								// rect.bottom + (textSize),
								// paintForText);
								// } else {
								// canvas.drawRect(backgroundRect,
								// paintForTextBackground);
								// canvas.drawText("Not identified",
								// rect.left,
								// rect.bottom + (textSize),
								// paintForText);
								// }
							}
						}
						confirmButton.setVisibility(View.GONE);
						// homeButton.setVisibility(View.VISIBLE);
						// Setting the view with the bitmap image that came in
						confirmationView.setImageBitmap(mutableBitmap);

					} else {
						Toast.makeText(getApplicationContext(),
								"No Faces detected", Toast.LENGTH_SHORT).show();
					}
				} else {
					Log.e(TAG, "Set Bitmap failed");
				}
			}
		});

		confirmButton.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					confirmButton
							.setImageResource(R.drawable.confirm_highlighted);
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					confirmButton.setImageResource(R.drawable.confirm);
				}

				return false;
			}
		});

	}

	/*
	 * Action listener method for the Home button on the screen
	 */
	private void homeButtonOnClickListener() {

		// homeButton = (ImageView) findViewById(R.carId.home_button);
		// homeButton.setVisibility(View.GONE);
		// homeButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // vibrate.vibrate(85);
		// Intent intent = new Intent(ImageConfirmationLocal.this,
		// FacialRecognitionActivityLocal.class);
		// startActivity(intent);
		// }
		// });
		//
		// homeButton.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// homeButton
		// .setImageResource(R.drawable.home_button_highlighted);
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {
		// homeButton.setImageResource(R.drawable.home_button);
		// }
		// return false;
		// }
		//
		// });

	}

	/*
	 * Action listener button for the action listener on the screen
	 */
	private void trashButtonOnClickListener() {
		// Trash the image and return back to the camera preview.
		trashButton = (ImageView) findViewById(R.id.cancel);
		trashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent resultIntent = new Intent();
				// resultIntent.putExtra("delete", true);
				setResult(RESULT_CANCELED, resultIntent);
				ImageConfirmationLocal.this.finish();
			}

		});

		trashButton.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					trashButton.setImageResource(R.drawable.trash_highlighted);
				} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
					trashButton.setImageResource(R.drawable.trash);
				}

				return false;
			}
		});

	}

	/*
	 * Action Listener method for taking the touch events on the image/face and
	 * performing appropriate action on it
	 */
	private void confirmationViewOnTouchListener() {

		confirmationView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					int xCoord = (int) event.getX();
					int yCoord = (int) event.getY();

					faceDataArray = faceObj.getFaceData();

					if (faceDataArray != null) {

						for (int i = 0; i < arrayOfRects.length; i++) {
							if (arrayOfRects[i].contains(xCoord, yCoord)) {
								// If update person flag is true then update the
								// person with its
								// respective face index.
								if (updatePerson) {
									// int result =
									// faceObj.updatePerson(personId,
									// i);
									// if (result == 0) {
									// Toast.makeText(
									// getApplicationContext(),
									// "'"
									// + userName
									// + "' updated successfully ",
									// Toast.LENGTH_SHORT).show();
									// confirmationView
									// .setOnClickListener(null);
									// } else {
									// Toast.makeText(
									// getApplicationContext(),
									// "Maximum face limit for " + "'"
									// + userName
									// + "' reached.",
									// Toast.LENGTH_SHORT).show();
									// }
									// saveAlbum();
								} else if (identifyPerson) // Toast the name of
															// the person
								{
									// String selectedPersonId = Integer
									// .toString(faceDataArray[i]
									// .getPersonId());
									// Iterator<HashMap.Entry<String, String>>
									// iter = hash
									// .entrySet().iterator();
									// // Default name is the person is unknown
									// String selectedPersonName =
									// "Not Identified";
									// while (iter.hasNext()) {
									// Log.e(TAG, "In");
									// HashMap.Entry<String, String> entry =
									// iter
									// .next();
									// if (entry.getValue().equals(
									// selectedPersonId)) {
									// selectedPersonName = entry.getKey();
									// }
									// }
									// Toast.makeText(getApplicationContext(),
									// selectedPersonName,
									// Toast.LENGTH_SHORT).show();
								} else {
									if (faceDataArray[i].getPersonId() < 0) {
										// Check the array position
										// corresponding the rect and add that
										// index
										arrayPosition = i;
										createAlert(); // Alert Box for getting
														// the user name
									} else {
										Toast.makeText(
												getApplicationContext(),
												"Similar face already exists. Try updating that person. Confidence= +"
														+ Integer
																.toString(faceDataArray[i]
																		.getRecognitionConfidence()),
												Toast.LENGTH_SHORT).show();
									}
								}
								faceFoundFlag = true;
							}
						}
						if (!faceFoundFlag) {
							Toast.makeText(getApplicationContext(),
									"No face found", Toast.LENGTH_SHORT).show();
						} else {
							faceFoundFlag = false;
						}
					}

				}

				return true;
			}
		});

	}

	/*
	 * Method to save the recognition album to a permanent device memory
	 */
	public void saveAlbum() {

		byte[] albumBuffer = faceObj.serializeRecogntionAlbum();
		Log.e(TAG, "Size of byte Array =" + albumBuffer.length);
		SharedPreferences settings = getSharedPreferences(ALBUM_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("albumArray", Arrays.toString(albumBuffer));
		editor.commit();

	}

	/*
	 * Method to pop - up an alert box when a face is clicked to be added
	 */
	private boolean createAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ImageConfirmationLocal.this);
		builder.setMessage("Are You Sure You want to add this Face");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

				int result = faceObj.addPerson(arrayPosition);

				// store into local database.

				saveAlbum();

				dbcontroller.add_user(Email, password, Integer.toString(result));

				Toast.makeText(getApplicationContext(), "added successfully",
						Toast.LENGTH_SHORT).show();

				setResult(Activity.RESULT_OK);
				finish();

			}

		});
		builder.show();
		return inputNameFlag;
	}

}
