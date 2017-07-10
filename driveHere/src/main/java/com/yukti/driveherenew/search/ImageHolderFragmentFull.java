package com.yukti.driveherenew.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.VolleySingleton;

public class ImageHolderFragmentFull extends Fragment {

	NetworkImageView image;
	String url;
	
	public static ImageHolderFragmentFull newInstance(String imageUrl) {
	
		ImageHolderFragmentFull fragment = new ImageHolderFragmentFull();
		Bundle args = new Bundle();
		args.putString("image_url", imageUrl);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		url = getArguments().getString("image_url");
//		url = AppSingleTon.APP_URL.URL_PHOTO_BASE+url;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_holder_full, container,false);
		
		image = (NetworkImageView) rootView.findViewById(R.id.image);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		image.setImageUrl(url, VolleySingleton.getInstance(getActivity()).getImageLoader());
	}

}
