package com.yukti.driveherenew;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.Simple;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StoreDataOneDataService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onStart(Intent intent, int startId) {
		String dataOneData = intent.getStringExtra(Constant.EXTRA_KEY_DATAONE_INFO);
		String Vin = intent.getStringExtra(Constant.EXTRA_KEY_VIN);
		storeData(dataOneData,Vin);
	}

	 void storeData(String dataOneData, String vin) {


			JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					super.onFailure(statusCode, headers, responseString, throwable);
					Log.d("response", "dataone response : " +"service failed");
					stopSelf();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
					super.onSuccess(statusCode, headers, response);
					stopSelf();
				}

				@Override
				public void onFinish() {
					super.onFinish();
					//dismissProgressDialog();
				}

				@Override
				public void onStart() {
					super.onStart();
					//showUpdateProgressDialog("Getting Dataone Data...");
				}
			};

			RequestParams params = new RequestParams();
			params.put(ParamsKey.KEY_vin,vin);
			params.put(ParamsKey.KEY_DATA,Common.Encode_String(dataOneData));
			params.put(ParamsKey.KEY_userId,AppSingleTon.SHARED_PREFERENCE.getUserId());
			params.put(ParamsKey.KEY_type,Constant.APP_TYPE);
			RestClient.post(this, AppSingleTon.APP_URL.URL_UPDATE_DATAONE, params, handler);
	}

	@Override
	public void onDestroy() {
	}
}