package com.yukti.driveherenew;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yukti.utils.AppSingleTon;


public class ForgotPasswordActivityTwo extends ActionBarActivity {

	Toolbar toolbar;
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_forgot_password_two);
		toolbar = (Toolbar) findViewById(R.id.activity_forgot_password_two_app_bar);
		toolbar.setTitle("Forgot Password");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String url = AppSingleTon.APP_URL.URL_FORGOT_PASS;
		//String url = "http://mywelcome.net/kajem/drive_here/users/forgot_password";
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new SSLTolerentWebViewClient());
		webView.loadUrl(url);

	}
	// SSL Error Tolerant Web View Client
	private class SSLTolerentWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed(); // Ignore SSL certificate errors
		}

	}
}
