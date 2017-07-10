package com.yukti.facerecognization.localdatabase;

import com.qualcomm.snapdragon.sdk.face.FaceData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.SurfaceView;

public class DrawViewLocal extends SurfaceView {

	private Paint paintForTextBackground = new Paint(); // Draw the black
														// background
	// behind the text
	private Paint paintForText = new Paint(); // Draw the text
	private FaceData[] mFaceArray;
	private boolean _inFrame; // Boolean to see if there is any faces in the
								// frame

	private FacialRecognitionActivityLocal faceRecog;
	public static String id = "";

	DBController dbcontroller;

	public DrawViewLocal(Context context, FaceData[] faceArray, boolean inFrame) {
		super(context);
		setWillNotDraw(false); // This call is necessary, or else the draw
								// method will not be called.
		mFaceArray = faceArray;
		_inFrame = inFrame;
		faceRecog = new FacialRecognitionActivityLocal();
		dbcontroller = new DBController(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (_inFrame) // If the face detected is in frame.
		{
			Log.e("carId", "ccccccccccccccccccccc");

			for (int i = 0; i < mFaceArray.length; i++) {

				String selectedPersonId = Integer.toString(mFaceArray[i]
						.getPersonId());

				id = selectedPersonId;
				Log.e("selectedPersonId", id);

				Rect rect = mFaceArray[i].rect;
				float pixelDensity = getResources().getDisplayMetrics().density;
				int textSize = (int) (rect.width() / 25 * pixelDensity);

				paintForText.setColor(Color.WHITE);
				paintForText.setTextSize(textSize);
				Typeface tp = Typeface.SERIF;
				Rect backgroundRect = new Rect(rect.left, rect.bottom,
						rect.right, (rect.bottom + textSize));

				paintForTextBackground.setStyle(Paint.Style.FILL);
				paintForTextBackground.setColor(Color.BLACK);
				paintForText.setTypeface(tp);
				paintForTextBackground.setAlpha(80);

				// canvas.drawRect(backgroundRect, paintForTextBackground);
				// canvas.drawText(selectedPersonId, rect.left, rect.bottom
				// + (textSize), paintForText);

			}

			/*
			 * DBController dbcontroller = new DBController(getContext());
			 * dbcontroller.get_employee(Integer.parseInt(carId));
			 */

		} else {
			canvas.drawColor(0, Mode.CLEAR);
		}
	}
}
