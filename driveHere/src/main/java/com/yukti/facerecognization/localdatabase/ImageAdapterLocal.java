package com.yukti.facerecognization.localdatabase;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yukti.driveherenew.R;

public class ImageAdapterLocal extends BaseAdapter {

	private Context mContext;
	private boolean isaddPhoto;

	public ImageAdapterLocal(Context context , boolean isaddPic ) {
		mContext = context;
		isaddPhoto = isaddPic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View gridView;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes

			gridView = new View(mContext);
			gridView = inflater.inflate(R.layout.images, null);

		}else {
			gridView = (View) convertView;
		} 
		
		ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);
		TextView tv = (TextView) gridView.findViewById(R.id.textView);
		
		
		if(isaddPhoto == true)
		{
			imageView.setImageResource(R.drawable.add_user);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			tv.setBackgroundColor(Color.BLACK);
			tv.setText("Add New User");
		}
		else
		{
			imageView.setImageResource(R.drawable.live_recognition);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			tv.setBackgroundColor(Color.BLACK);
			tv.setText("Live Recogniton");
		}
		
		return gridView;
	}
	
	// references to our images
		private Integer[] mThumbIds = { R.drawable.add_user,
				R.drawable.update_user, R.drawable.identify_user,
				R.drawable.live_recognition, R.drawable.reset_album,
				R.drawable.delete_user, };
		
		// references to our images
		private String[] texts = { "Add New User", "Update Existing Person",
				"Identify People", "Live Recogniton", "Reset Album",
				"Delete Existing User",
		
		};

}
