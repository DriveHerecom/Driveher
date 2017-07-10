package com.yukti.driveherenew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yukti.utils.AppSingleTon;

public class ProfileActivity extends BaseActivity {


	TextView name, email;
	LinearLayout logout;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
	Toolbar	toolbar = (Toolbar) findViewById(R.id.activity_profile_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		name = (TextView) findViewById(R.id.name);
		email = (TextView) findViewById(R.id.email);
		logout = (LinearLayout) findViewById(R.id.logout);
		
		name.setText(AppSingleTon.SHARED_PREFERENCE.getUserName());
		email.setText(AppSingleTon.SHARED_PREFERENCE.getUserEmail());

		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AppSingleTon.SHARED_PREFERENCE.logout();
				Intent intent = new Intent(ProfileActivity.this, com.yukti.driveherenew.LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public interface LogoutListener{
		
		public void onLogout();
	}
	
}
