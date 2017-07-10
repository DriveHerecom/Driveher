package com.yukti.driveherenew;

import java.io.File;
import java.util.ArrayList;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yukti.utils.Common;

public class ShowPhotoActivity extends BaseActivity {

	//ArrayList<File> photolist;
	private Toolbar toolbar;
	ArrayList<String> list;

	LinearLayout ll_container;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_photo);

		initToolBar();
		initLinearLayout();
		initphotolist();
		// testReplaceArrylist();

	}

	private void initLinearLayout() {

		ll_container = (LinearLayout) findViewById(R.id.ll_container_s);
	}

	private void testReplaceArrylist() {
		list = new ArrayList<String>();

		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");

		for (int i = 0; i < list.size(); i++) {
			Log.e("list= " + i, list.get(i));
		}

		list.set(2, "second");
		for (int i = 0; i < list.size(); i++) {
			Log.e("setlist= " + i, list.get(i));
		}

	}

	private void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.activity_show_photo_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	void initphotolist() {
		if (AddNewCarActivity_old.photoList != null && AddNewCarActivity_old.photoList.size() > 0) {
			for (int i = 0; i < AddNewCarActivity_old.photoList.size(); i++) {
				Log.e("photo= " + i, AddNewCarActivity_old.photoList.get(i).getPath());
				addView(i);
			}
		}

	}

	ImageView img;
	View itemList;

	void addView(int pos) {
		itemList = null;
		itemList = ShowPhotoActivity.this.getLayoutInflater().inflate(R.layout.row_show_photo, ll_container, false);
		img = (ImageView) itemList.findViewById(R.id.iv_photo);
		img.setTag(pos);
		img.setImageBitmap(Common.filepathTobitmap(AddNewCarActivity_old.photoList.get(pos).getPath()));

		img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RotateImage((Integer) v.getTag());
			}
		});

		ll_container.addView(itemList);

	}

	Dialog dialog;
	ImageView dialogImage;
	Button RotateButton;
	Button DoneButton;
	int bitmapWidth;
	int bitmapHeight;

	public void RotateImage(final int pos) {

		dialog = new Dialog(ShowPhotoActivity.this);
		dialog.setTitle("Choose Action");
		@SuppressWarnings("static-access")
		LayoutInflater inflater = getLayoutInflater().from(getApplicationContext());

		View view = inflater.inflate(R.layout.activity_custom_dialog, null);

		dialog.setContentView(view);
		// dialog.setCancelable(false);
		dialogImage = (ImageView) view.findViewById(R.id.selectedImage);
		RotateButton = (Button) view.findViewById(R.id.RotateButton);
		DoneButton = (Button) view.findViewById(R.id.DoneButton);
		dialogImage.setImageBitmap(Common.filepathTobitmap(AddNewCarActivity_old.photoList.get(pos)
				.getPath()));

		DoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (resizedBitmap != null) {
					ImageView img = (ImageView) ll_container
							.findViewWithTag(pos);
					img.setImageBitmap(resizedBitmap);
					File file;
					file = Common.bitmapToFilePath(resizedBitmap);
					if (file != null) {
						AddNewCarActivity_old.photoList.set(pos, file);
						Log.e("rotate image"+ pos, file.getPath());
					}
				}
				dialog.cancel();
			}
		});
		RotateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
				bitmapWidth = dialog_imageBitmap.getWidth();
				bitmapHeight = dialog_imageBitmap.getHeight();
				drawMatrix(dialog_imageBitmap);

			}
		});
		dialog.show();
	}

	Bitmap resizedBitmap = null;

	private void drawMatrix(Bitmap dialog_imageBitmap) {
		Matrix matrix = new Matrix();
		matrix.preRotate(90);
		resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0, 0,
				bitmapWidth, bitmapHeight, matrix, true);
		BitmapDrawable b = new BitmapDrawable(resizedBitmap);
		dialogImage.setImageBitmap(resizedBitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}