package com.yukti.driveherenew;

import java.io.File;
import java.util.ArrayList;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yukti.utils.Common;

public class ShowPhotoActivityEdit extends BaseActivity {
//
//	//ArrayList<File> photolist;
//	private Toolbar toolbar;
//	ArrayList<String> list;
//
//	LinearLayout ll_container;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_show_photo_edit);
//
//		initToolBar();
//		initLinearLayout();
//		initphotolist();
//	}
//
//	private void initLinearLayout() {
//		ll_container = (LinearLayout) findViewById(R.carId.ll_container_s);
//	}
//
//	private void initToolBar() {
//		toolbar = (Toolbar) findViewById(R.carId.activity_show_photo_edit_app_bar);
//		setSupportActionBar(toolbar);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//	}
//
//	void initphotolist()
//	{
//		for (int i=0;i<EditCarActivity.photoListWeb.size();i++)
//		{
//			Log.e("Web image size",EditCarActivity.photoListWeb.size()+"");
//			addView1(i);
//			Log.e("Web pic" + i ,EditCarActivity.photoListWeb.get(i).ImagePath);
//		}
//
//		if (EditCarActivity.photoList != null && EditCarActivity.photoList.size() > 0)
//		{
//			Log.e("SDCard image size",EditCarActivity.photoList.size()+"");
//			for (int i = 0; i < EditCarActivity.photoList.size(); i++) {
//				Log.e("photo= " + i, EditCarActivity.photoList.get(i).getPath());
//				Log.e("Image Path in edit ",EditCarActivity.photoList.get(i).getPath());
//				addView(i);
//			}
//		}
//
//	}
//
//	ImageView img;
//	View itemList;
//
//	void addView(final int pos)
//	{
//		itemList = null;
//
//		itemList = ShowPhotoActivityEdit.this.getLayoutInflater().inflate(R.layout.row_show_photo, ll_container, false);
//		img = (ImageView) itemList.findViewById(R.carId.iv_photo);
//
//		img.setTag(pos);
//
//		img.setImageBitmap(Common.filepathTobitmap(EditCarActivity.photoList.get(pos).getPath()));
//
//		img.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				RotateImage((Integer) v.getTag());
//			}
//		});
//
//		img.setOnLongClickListener(new View.OnLongClickListener()
//		{
//			@Override
//			public boolean onLongClick(final View v)
//			{
//				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowPhotoActivityEdit.this);
//				alertDialog.setTitle("Confirm Delete...");
//				alertDialog.setMessage("Are you sure you want delete this?");
//				alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int which)
//					{
//						EditCarActivity.photoList.remove(pos);
//						v.setVisibility(View.GONE);
//						Toast.makeText(getApplicationContext(), "Car image removed", Toast.LENGTH_SHORT).show();
//					}
//				});
//
//				alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface dialog, int which)
//					{
//						dialog.cancel();
//					}
//				});
//				alertDialog.show();
//
//				return true;
//			}
//		});
//
//		ll_container.addView(itemList);
//	}
//
//	void addView1(final int pos)
//	{
//		itemList = null;
//
//		itemList = ShowPhotoActivityEdit.this.getLayoutInflater().inflate(
//				R.layout.row_show_photo, ll_container, false);
//		img = (ImageView) itemList.findViewById(R.carId.iv_photo);
//
//		img.setTag(pos);
//
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.alto_lxi)
//				.showImageForEmptyUri(R.drawable.alto_lxi)
//				.showImageOnFail(R.drawable.alto_lxi)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.considerExifParams(true)
//				.bitmapConfig(Bitmap.Config.RGB_565)
//				.build();
//
//		ImageLoader imageLoader = ImageLoader.getInstance();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
//		imageLoader.getInstance().displayImage("http://drivehere.com/inventory2/app/webroot/files/"+EditCarActivity.photoListWeb.get(pos).ImagePath,
//				img, options);
//
//		img.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				RotateImage1(v,(Integer) v.getTag());
//			}
//		});
//
//		img.setOnLongClickListener(new View.OnLongClickListener()
//		{
//			@Override
//			public boolean onLongClick(final View v)
//			{
//				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowPhotoActivityEdit.this);
//				alertDialog.setTitle("Confirm Delete...");
//				alertDialog.setMessage("Are you sure you want delete this?");
//				alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int which)
//					{
//						EditCarActivity.deleteImage = EditCarActivity.deleteImage+","+"http://drivehere.com/inventory2/app/webroot/files/"+EditCarActivity.photoListWeb.get(pos).ImagePath;
//						Log.e("DEleted Image",EditCarActivity.deleteImage+"");
//						EditCarActivity.photoListWeb.remove(pos);
//						v.setVisibility(View.GONE);
//						Toast.makeText(getApplicationContext(), "Car image removed", Toast.LENGTH_SHORT).show();
//					}
//				});
//
//				alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface dialog, int which)
//					{
//						dialog.cancel();
//					}
//				});
//				alertDialog.show();
//				return true;
//			}
//		});
//		ll_container.addView(itemList);
//	}
//
//	Dialog dialog;
//	ImageView dialogImage;
//	Button RotateButton;
//	Button DoneButton;
//	int bitmapWidth;
//	int bitmapHeight;
//
//	public void RotateImage(final int pos)
//	{
//		dialog = new Dialog(ShowPhotoActivityEdit.this);
//		dialog.setTitle("Choose Action");
//		@SuppressWarnings("static-access")
//		LayoutInflater inflater = getLayoutInflater().from(getApplicationContext());
//		View view = inflater.inflate(R.layout.activity_custom_dialog, null);
//
//		dialog.setContentView(view);
//		dialogImage = (ImageView) view.findViewById(R.carId.selectedImage);
//		RotateButton = (Button) view.findViewById(R.carId.RotateButton);
//		DoneButton = (Button) view.findViewById(R.carId.DoneButton);
//		dialogImage.setImageBitmap(Common.filepathTobitmap(EditCarActivity.photoList.get(pos).getPath()));
//
//		DoneButton.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				if (resizedBitmap != null) {
//					ImageView img = (ImageView) ll_container.findViewWithTag(pos);
//					img.setImageBitmap(resizedBitmap);
//					File file;
//					file = Common.bitmapToFilePath(resizedBitmap);
//					if (file != null) {
//						EditCarActivity.photoList.set(pos, file);
//						Log.e("rotate image"+ pos, file.getPath());
//					}
//				}
//				dialog.cancel();
//			}
//		});
//
//		RotateButton.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
//				bitmapWidth = dialog_imageBitmap.getWidth();
//				bitmapHeight = dialog_imageBitmap.getHeight();
//				drawMatrix(dialog_imageBitmap);
//			}
//		});
//		dialog.show();
//	}
//
//	public void RotateImage1(final View v1,final int pos)
//	{
//		dialog = new Dialog(ShowPhotoActivityEdit.this);
//		dialog.setTitle("Choose Action");
//
//		@SuppressWarnings("static-access")
//		LayoutInflater inflater = getLayoutInflater().from(getApplicationContext());
//
//		View view = inflater.inflate(R.layout.activity_custom_dialog, null);
//
//		dialog.setContentView(view);
//		dialogImage = (ImageView) view.findViewById(R.carId.selectedImage);
//		RotateButton = (Button) view.findViewById(R.carId.RotateButton);
//		DoneButton = (Button) view.findViewById(R.carId.DoneButton);
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.alto_lxi)
//				.showImageForEmptyUri(R.drawable.alto_lxi)
//				.showImageOnFail(R.drawable.alto_lxi)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.considerExifParams(true)
//				.bitmapConfig(Bitmap.Config.RGB_565)
//				.build();
//
//		ImageLoader imageLoader = ImageLoader.getInstance();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
//		imageLoader.getInstance().displayImage("http://drivehere.com/inventory2/app/webroot/files/"+EditCarActivity.photoListWeb.get(pos).ImagePath,
//				dialogImage, options);
//
//		DoneButton.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				if (resizedBitmap != null)
//				{
//					File file;
//					file = Common.bitmapToFilePath(resizedBitmap);
//					if (file != null)
//					{
//						EditCarActivity.deleteImage = EditCarActivity.deleteImage+","+"http://drivehere.com/inventory2/app/webroot/files/"+EditCarActivity.photoListWeb.get(pos).ImagePath;
//						EditCarActivity.photoListWeb.remove(pos);
//						EditCarActivity.photoList.add(file);
//						v1.setVisibility(View.GONE);
//						addView(EditCarActivity.photoList.size()-1);
//						Log.e("rotate image"+ pos, file.getPath());
//					}
//				}
//				dialog.cancel();
//			}
//		});
//
//		RotateButton.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage.getDrawable()).getBitmap();
//				bitmapWidth = dialog_imageBitmap.getWidth();
//				bitmapHeight = dialog_imageBitmap.getHeight();
//				drawMatrix(dialog_imageBitmap);
//			}
//		});
//		dialog.show();
//	}
//
//	Bitmap resizedBitmap = null;
//
//	private void drawMatrix(Bitmap dialog_imageBitmap) {
//		Matrix matrix = new Matrix();
//		matrix.preRotate(90);
//		resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0, 0,
//				bitmapWidth, bitmapHeight, matrix, true);
//		BitmapDrawable b = new BitmapDrawable(resizedBitmap);
//		dialogImage.setImageBitmap(resizedBitmap);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.carId.home:
//			finish();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
