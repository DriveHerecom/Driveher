package com.yukti.driveherenew;

import com.yukti.utils.AppSingleTon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity{

	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
    
    public void showUpdateProgressDialog(final String message) {
		
    	AppSingleTon.METHOD_BOX.hidekeyBoard(this);
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
			progressDialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					//resumeCamera();
				}
			});
		}
		if (progressDialog.isShowing()) {
			progressDialog.setMessage(message);

		} else {
			progressDialog.setMessage(message);
			progressDialog.show();
		}

	}
	
    public void dismissProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
	 
    public void showToast(final String msg) { 
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

}
