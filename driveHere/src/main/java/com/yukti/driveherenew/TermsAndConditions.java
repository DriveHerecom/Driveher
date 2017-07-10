package com.yukti.driveherenew;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class TermsAndConditions extends ActionBarActivity {

	Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_terms_and_conditions);
		toolbar = (Toolbar) findViewById(R.id.activity_terms_and_conditions_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		WebView wv;  
        wv = (WebView) findViewById(R.id.webView);  
        //wv.loadUrl("file:///android_asset/terms_and_condition.html");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
