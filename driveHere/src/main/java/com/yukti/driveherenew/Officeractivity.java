package com.yukti.driveherenew;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.http.Header;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.OfficerReport;
import com.yukti.utils.AppSingleTon;

public class Officeractivity extends BaseActivity {

	EditText editText;
	ImageView image;
	Button sendbutton;

	private Uri fileUri;
	File mediafile;
	String text;

	public static final int REQUEST_CAMERA = 100;
	public static final int SELECT_FILE = 10;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officer);

		editText = (EditText) findViewById(R.id.edittext);

		// image = (ImageView) findViewById(R.carId.img);
		sendbutton = (Button) findViewById(R.id.button1);

		text = editText.getText().toString();

		// image.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// selectImage();
		// }
		//
		// });

		sendbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Log.e("text", "text" + editText.getText().toString());
					sendEmail();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	// protected void selectImage() {
	// // TODO Auto-generated method stub
	//
	// final CharSequence[] items = { "Take Photo", "Cancel" };
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// Officeractivity.this);
	// builder.setTitle("Add Photo!");
	// // add items to the action Bar
	// builder.setItems(items, new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int item) {
	// if (items[item].equals("Take Photo")) {
	// // Open Camera using intent
	// openCamera();
	// }
	//
	// else if (items[item].equals("Cancel")) {
	// dialog.dismiss();
	// }
	// }
	// });
	// builder.show();
	// }

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				Officeractivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

				File destination = new File(
						Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");

				FileOutputStream fo;
				try {
					destination.createNewFile();
					fo = new FileOutputStream(destination);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				Log.e("Image", "Image" + thumbnail);
				image.setImageBitmap(thumbnail);

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = managedQuery(selectedImageUri, projection,
						null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();

				String selectedImagePath = cursor.getString(column_index);

				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);

				image.setImageBitmap(bm);
			}
		}
	}

	private void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// start the image capture Intent
		Log.e("Uri file", "uri file " + fileUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		// External sdcard location

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		Log.e("media file", "mediafile" + mediaFile);

		// BitmapFactory.Options options = new BitmapFactory.Options();
		image.setImageURI(fileUri);
		return mediaFile;
	}

	protected void sendEmail() throws FileNotFoundException {
		// TODO Auto-generated method stub
		// Log.e("mediafileok","mediafileok"+fileUri);
		// Log.e("edittext","edittext"+editText.getText().toString());
		
		RequestParams params = new RequestParams();
		params.put("image", mediafile);
		params.put("note", editText.getText().toString());

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(AppSingleTon.APP_URL.URL_OFFICER, params,
				new EmailResponseHandler());

		Log.e("params", "params" + params);

	}

	public class EmailResponseHandler extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			// TODO Auto-generated method stub

			try {

				String content = new String(arg2, "UTF-8");
				Log.e("Email Resp", "" + content);

				OfficerReport officeremail;
				Gson gson = new Gson();
				Type type = new TypeToken<OfficerReport>() {
				}.getType();
				officeremail = gson.fromJson(content, type);

				if (officeremail.status == 1) {

					Log.e("Suceess", "success");

					// Toast.makeText(getApplicationContext(),"Email Send Succesfully",Toast.LENGTH_SHORT).show();
				}

				Log.e("getdata", "sdsa");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			finish();
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			// TODO Auto-generated method stub

			arg3.printStackTrace();
		}

	}

}