package com.yukti.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class CamOperation {

	private String JPEG_FILE_SUFFIX = ".jpg";
	public static final int ACTION_TAKE_PHOTO = 1930;
	private Context context = null;
	
	public CamOperation(Context context){
		this.context = context;
	}
	
	public void createESPhotoFile(CameraResponse cameraResponse){
		
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			String timeStamp = String.valueOf(calendar.getTimeInMillis());
			String imageFileName =  timeStamp +JPEG_FILE_SUFFIX;
			
			File storageDir = new File(SDCardManager.sd_card_folder_path);
						
			boolean flag = storageDir.mkdirs(); 
			if (!flag) {
				if (!storageDir.exists()) {
					cameraResponse.onFileCreatingError();
				}else{
					File imgFile = new File(storageDir,imageFileName);
					cameraResponse.onCameraReady(imgFile);
				}
			}else{
				File imgFile = new File(storageDir,imageFileName);
				cameraResponse.onCameraReady(imgFile);	
			}	
		}else{
			cameraResponse.onNoSdcardFound();	
		}
	}
	
	public boolean isIntentAvailable(String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	public byte[] fileToByte(File file) {

		byte[] data = null;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while (is.available() > 0) {
				bos.write(is.read());
			}
			data = bos.toByteArray();
			if(is!=null)
				is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			data = null;
		} catch (IOException e) {
			e.printStackTrace();
			data = null;
		}

		return data;
	}
	
	public byte[] fileToByteOne(File f) {
		BitmapFactory.Options opts = new BitmapFactory.Options ();
		opts.inSampleSize = 2;  
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), opts);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		byte[] data = stream.toByteArray();
		return data;
	}
	
	
	public Bitmap fileToBitmap(File f){

		BitmapFactory.Options opts = new BitmapFactory.Options ();
		opts.inSampleSize = 2;  
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), opts);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		return bitmap;
		
	}
	
	public void  saveSmallerOne(File f) {
		// not working
		FileOutputStream out = null;
		try {
		    out = new FileOutputStream(f);
		    BitmapFactory.Options opts = new BitmapFactory.Options ();
			opts.inSampleSize = 2; 
			Bitmap bitmap = BitmapFactory.decodeFile(f.getPath(), opts);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	
	
	public interface CameraResponse{
		
		public void onNoSdcardFound();
		public void onFileCreatingError();
		public void onCameraReady(File file);
		
		//capture successful and ok by user
		public void onSuccess();
		//capture successful but cancel by user
		public void onFailure();
	}
}
