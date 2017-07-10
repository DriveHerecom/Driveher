/*
 * =========================================================================
 * Copyright (c) 2014 Qualcomm Technologies, Inc. All Rights Reserved.
 * Qualcomm Technologies Proprietary and Confidential.
 * =========================================================================
 * @file: DrawView.java
 */

package com.yukti.facerecognization;

import java.util.HashMap;

import com.qualcomm.snapdragon.sdk.face.FaceData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.view.SurfaceView;

public class DrawView extends SurfaceView {

	private Paint paintForTextBackground = new Paint(); // Draw the black
														// background
	// behind the text
	private Paint paintForText = new Paint(); // Draw the text
	private FaceData[] mFaceArray;
	private boolean _inFrame; // Boolean to see if there is any faces in the
								// frame
	private HashMap<String, String> hash;
	private FacialRecognitionActivity faceRecog;
	Context context;

	public static boolean isidentified = false;

	public DrawView(Context context, FaceData[] faceArray, boolean inFrame) {
		super(context);
		setWillNotDraw(false); // This call is necessary, or else the draw
								// method will not be called.
		mFaceArray = faceArray;
		_inFrame = inFrame;
		faceRecog = new FacialRecognitionActivity();
		// hash = faceRecog.retrieveHash(getContext());
		this.context = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (_inFrame) // If the face detected is in frame.
		{
			for (int i = 0; i < mFaceArray.length; i++) {

				if (mFaceArray[i].getPersonId() >= 0) {
//					Toast.makeText(context, "identified", Toast.LENGTH_SHORT)
//							.show();
					isidentified = true;
				}

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
			}
		} else {
			canvas.drawColor(0, Mode.CLEAR);
		}
	}

}
