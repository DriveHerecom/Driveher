package com.yukti.driveherenew.search;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.R;
import com.yukti.utils.Constant;

public class GalleryActivity extends BaseActivity {

	ViewPager viewPager;
	TextView photoIndex;
	ImagePagerAdapter imagePagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		initToolbar();
		
		photoIndex = (TextView) findViewById(R.id.index);
		viewPager = (ViewPager) findViewById(R.id.gallery_viewpager);

		ArrayList<String> photoUrlList = (ArrayList<String>) getIntent().getExtras().getSerializable(Constant.EXTRA_KEY_PHOTO_LIST);
		int currentItem = getIntent().getExtras().getInt(Constant.EXTRA_KEY_CURRENT_ITEM);
		photoIndex.setText(currentItem+1 + " / "+photoUrlList.size());
		getSupportActionBar().setTitle(getIntent().getExtras().getString(Constant.EXTRA_KEY_TITLE));
		initPager(currentItem, photoUrlList);
	}

	void initToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_gallery_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@SuppressWarnings("deprecation")
	void initPager(int currentItem, final ArrayList<String> photoUrlList){
		
		imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), photoUrlList);
		viewPager.setAdapter(imagePagerAdapter);
		viewPager.setCurrentItem(currentItem);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				photoIndex.setText(""+(position+1)+"/"+photoUrlList.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}
		});
		
	}
	public class ImagePagerAdapter extends FragmentStatePagerAdapter {
		
		ArrayList<String> photoUrlList;
		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> photoUrlList) {
			super(fm);
			this.photoUrlList = photoUrlList;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageHolderFragmentFull.newInstance(photoUrlList.get(position));
		}

		@Override
		public int getCount() {
			return photoUrlList.size();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
