package com.yukti.driveherenew;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.zxing.Result;

public class  BarCodeScannerActivity extends BaseActivity implements
		ZXingScannerView.ResultHandler {

	private static final String FLASH_STATE = "FLASH_STATE";
	private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
	private ZXingScannerView mScannerView;
	private boolean mFlash;
	private boolean mAutoFocus;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		if (state != null) {
			mFlash = state.getBoolean(FLASH_STATE, false);
			mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
		} else {
			mFlash = false;
			mAutoFocus = true;
		}
		mScannerView = new ZXingScannerView(this);
		setContentView(mScannerView);
	}

	@Override
	public void onResume() {
		super.onResume();

		mScannerView.setResultHandler(this);
		resumeCamera();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(FLASH_STATE, mFlash);
		outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem menuItem;
		if (mFlash) {
			menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0,
					R.string.flash_on);
		} else {
			menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0,
					R.string.flash_off);
		}

		MenuItemCompat
				.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

		if (mAutoFocus) {
			menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0,
					R.string.auto_focus_on);
		} else {
			menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0,
					R.string.auto_focus_off);
		}

		MenuItemCompat
				.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.menu_flash:
			mFlash = !mFlash;
			if (mFlash) {
				item.setTitle(R.string.flash_on);
			} else {
				item.setTitle(R.string.flash_off);
			}
			mScannerView.setFlash(mFlash);

			return true;
		case R.id.menu_auto_focus:
			mAutoFocus = !mAutoFocus;
			if (mAutoFocus) {
				item.setTitle(R.string.auto_focus_on);
			} else {
				item.setTitle(R.string.auto_focus_off);
			}
			mScannerView.setAutoFocus(mAutoFocus);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void handleResult(Result rawResult) {
		try {
			Uri notification = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
					notification);
			r.play();
		} catch (Exception e) {

		}
		Log.d("Barcode", "Barcode " + rawResult.getText() + " Type "
				+ rawResult.getBarcodeFormat());
		// showToast(rawResult.getText());

		String code = rawResult.getText();

		// showToast(code);
		// if (code.length() == 17 || code.length() == 7) {
		if (code.length() != 0) {
			Intent intent = new Intent();
			intent.putExtra("code", code);
			setResult(RESULT_OK, intent);

			Log.e("RFID OR VIN", code);
		} else {
			Intent intent = new Intent();
			intent.putExtra("code", "");
			setResult(RESULT_CANCELED, intent);
			showToast("Invalid Code!");

			Log.e("Result Cancle", "cancle");
		}
		finish();
	}

	// Resume the camera
	void resumeCamera() {
		mScannerView.startCamera();
		mScannerView.setFlash(mFlash);
		mScannerView.setAutoFocus(mAutoFocus);
	}

	@Override
	public void onPause() {
		super.onPause();
		mScannerView.stopCamera();
	}

}