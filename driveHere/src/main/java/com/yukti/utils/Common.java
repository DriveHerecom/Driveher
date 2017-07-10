package com.yukti.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.yukti.driveherenew.R;

public class Common {

	static ProgressDialog progressDialog;

	@SuppressWarnings("deprecation")
	public static boolean isFrontCameraAvailable() {

		int cameraCount = 0;
		boolean isFrontCameraAvailable = false;
		cameraCount = Camera.getNumberOfCameras();

		while (cameraCount > 0) {
			CameraInfo cameraInfo = new CameraInfo();
			cameraCount--;
			Camera.getCameraInfo(cameraCount, cameraInfo);

			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
				isFrontCameraAvailable = true;
				break;
			}
		}
		return isFrontCameraAvailable;
	}
	public static boolean isNetworkConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static String Encode_String(String stringData)
	{
		byte[] data = null;
		try
		{
			data = stringData.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		String base64_String = Base64.encodeToString(data, Base64.DEFAULT);
		return base64_String;
	}
	public static String Decode_String(String base64_string)
	{
		byte[] data = Base64.decode(base64_string, Base64.DEFAULT);
		String converted_string = null;
		try
		{
			converted_string = new String(data, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return converted_string;
	}
	public static Bitmap filepathTobitmap(String filepath)
	{
		// filepath
		// File imgFile = new File("/sdcard/Images/test_image.jpg");
		File imgFile = new File(filepath);
		Bitmap myBitmap = null;
		if (imgFile.exists())
		{
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			// //Drawable d = new BitmapDrawable(getResources(), myBitmap);
			// ImageView myImage = (ImageView) findViewById(R.carId.imageviewTest);
			// myImage.setImageBitmap(myBitmap);
		}
		return myBitmap;
	}

	// public static File bitmapToFilePath(Bitmap _bitmap) throws IOException {
	// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	// _bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
	// // you can create a new file name "test.jpg" in sdcard folder.
	// File file = new File(Environment.getExternalStorageDirectory()
	// + File.separator + System.currentTimeMillis() + "dh.jpg");
	//
	// if (file != null) {
	// file.createNewFile();
	// // write the bytes in file
	// FileOutputStream fo = new FileOutputStream(file);
	// fo.write(bytes.toByteArray());
	// //remember close de FileOutput
	// fo.close();
	// }
	// return file;
	//
	// }
	public static File bitmapToFilePath(Bitmap bitmap)
	{
		File RotateImages = new File(Environment.getExternalStorageDirectory() + File.separator + "MYImage");
		if (!RotateImages.exists())
		{
			RotateImages.mkdir();
		}
		else
		{
			// Toast.makeText(getApplicationContext(), "Exist..",
			// Toast.LENGTH_LONG).show();
		}
		String name = System.currentTimeMillis() + "Image.jpeg";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "MYImage" + File.separator + name);
		FileOutputStream fo;
		try
		{
			f.createNewFile();
			fo = new FileOutputStream(f);
			fo.write(baos.toByteArray());
			fo.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}

	public static void showUpdateProgressDialog(final String message,
			Context context) {

		if (progressDialog == null)
		{
			progressDialog = new ProgressDialog(context);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
		}
		progressDialog.setMessage(message);
		progressDialog.show();

//		if (progressDialog.isShowing()) {
//			progressDialog.setMessage(message);
//		} else {
//			progressDialog.setMessage(message);
//			progressDialog.show();
//			if (!((AppCompatActivity) context).isFinishing()) {
//				progressDialog.show();
//			}
		}


	public static void dismissProgressDialog()
	{
		if (progressDialog != null)
			progressDialog.dismiss();
	}
	public static void showToast(final String msg, Context context)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static Bitmap resize(Bitmap btmp)
	{
		int bitmapWidth = btmp.getWidth();
		Bitmap bitmap = null;

		// scale According to WIDTH
		int scaledWidth = bitmapWidth / 4;
		int scaledHeight = (scaledWidth * btmp.getHeight()) / bitmapWidth;
		try
		{
			bitmap = Bitmap.createScaledBitmap(btmp, scaledWidth, scaledHeight, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}

	public static void clearstaticvalues()
	{
//		AddNewCarActivity.strMake = "";
//		AddNewCarActivity.strModel = "";
//		AddNewCarActivity.strModelNumber = "";
//		AddNewCarActivity.strModelYear = "";
//		AddNewCarActivity.strVehicleType = "";
//		AddNewCarActivity.strDriveType = "";
//		AddNewCarActivity.strMaxHp = "";
//		AddNewCarActivity.strMaxTorque = "";
//
//		AddNewCarActivity.strOilCapacity = "";
//		AddNewCarActivity.strFuelType = "";
//		AddNewCarActivity.strColor = "";
//		AddNewCarActivity.strMiles = "";
//		AddNewCarActivity.strSalesType = "";
//
//		AddNewCarActivity.strCylinder = "";
//		AddNewCarActivity.strSalesPrice = "";
//		AddNewCarActivity.strType = "";
//
//		AddNewCarActivity.strStockNumber = "";
//		AddNewCarActivity.strVehicleProblem = "";
//		AddNewCarActivity.strVehicleNote = "";
//
//		AddNewCarActivity.strCompany = "";
//		AddNewCarActivity.strPurchaseForm = "";
//		AddNewCarActivity.strInspectionDate = "";
//
//		AddNewCarActivity.strRegistrationDate = "";
//		AddNewCarActivity.strInsuranceDate = "";
//		AddNewCarActivity.strGasTank = "";
//		AddNewCarActivity.strHasTitle = "";
//		AddNewCarActivity.strLocationTitle = "";
//		AddNewCarActivity.strMechanic = "";
//		AddNewCarActivity.strCompanyInsurance = "";
//		AddNewCarActivity.companyInsuranceFile = null;
//		AddNewCarActivity.strcolorcode = null;
//		AddNewCarActivity.strLotCode="";
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth)
			{
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
			int reqHeight)
	{
		Log.e("decode ", "SampledBitmapFromPath reqHeight: " + reqHeight
				+ " reqWidth: " + reqWidth);
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		if (reqWidth > 0) {
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
		} else {
			options.inSampleSize = 4;
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static void showAlertdialog(Context context, String msg, String title)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(R.drawable.alert);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog d = builder.create();
		d.show();
	}
}
