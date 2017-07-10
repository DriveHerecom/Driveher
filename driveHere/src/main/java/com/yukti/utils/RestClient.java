package com.yukti.utils;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestClient {
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
	      client.get(url, params, responseHandler);
	 }

	public static void post(Context context, String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
	      client.post(url, params, responseHandler);
	  }
	
	public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
	      client.post(url, params, responseHandler);
	  }
	
	public static void cancel(Context context,boolean mayInterruptIfRunning){
		client.cancelRequests(context, mayInterruptIfRunning);
	}
	
	public static void cancelAll(boolean mayInterruptIfRunning){
		client.cancelAllRequests(mayInterruptIfRunning);
	}
}
