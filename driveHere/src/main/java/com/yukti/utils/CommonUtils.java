package com.yukti.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.boye.httpclientandroidlib.util.ByteArrayBuffer;

//import cz.msebera.android.httpclient.util.ByteArrayBuffer;

public class CommonUtils {

	private static ProgressDialog progressDialog;

	public static Bitmap img;

	public static boolean isConnectedToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		Toast.makeText(context, "Please turn on internet connection",
				Toast.LENGTH_LONG).show();
		return false;
	}

    public static boolean isNetworkAvailable(Context context) {
        boolean isNetworkAvailable = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isNetworkAvailable = true;
        }
        return isNetworkAvailable;
    }

    public static void showAlertWithNegativeButton(final Context context, String title, String message, DialogInterface.OnClickListener positiveButtonListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
		alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", positiveButtonListener);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ((Activity) context).finish();
            }
        });
        alertDialog.show();
    }

    public static void showAlertDialog(final Context context, String title, String message, DialogInterface.OnClickListener positiveButtonListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
		alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Try Again", positiveButtonListener);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ((Activity) context).finish();
            }
        });
        alertDialog.show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public static ByteArrayBuffer convertBitmapToByteArray(Bitmap bmp) {
		ByteArrayBuffer buffer = null;
		if (bmp != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

			buffer = new ByteArrayBuffer(100);
			buffer.append(stream.toByteArray(), 0, stream.toByteArray().length);
		}
		return buffer;
	}

	public static Bitmap rotateImage(Bitmap sourceBitmap, String path) {

		// 1. figure out the amount of degrees
		int rotation = getRotation(path);

		// 2. rotate matrix by postconcatination
		Matrix matrix = new Matrix();
		matrix.postRotate(rotation);

		// 3. create Bitmap from rotated matrix
		return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight(), matrix, true);
	}

	private static int getRotation(String path) {
		try {
			ExifInterface ei = new ExifInterface(path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) { // BEST
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize, Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		int inSampleSize = 1;

		if (height > reqHeight) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		}
		int expectedWidth = width / inSampleSize;

		if (expectedWidth > reqWidth) {
			// if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
			// If
			// bigger SampSize..
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}

		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);
	}

	public static void showUpdateProgressDialog(Context context,
			final String message) {

		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
		}
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public static void dismissProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	public static String getCapitalize(String input) {
		if (input != null && input.trim().length() > 0)
			return Character.toString(input.charAt(0)).toUpperCase() + input.substring(1);
		else
			return "";
	}
}
