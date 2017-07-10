package com.yukti.utils;

public class AppListeners {

	public interface HttpListener{
		
		public void onSuccess(String response);
		public void onFailure();
	}
}
