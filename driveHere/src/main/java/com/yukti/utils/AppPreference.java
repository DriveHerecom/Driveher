package com.yukti.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {

	String LOGIN = "LOGIN";
	String VERIFY = "VERIFY";
	String USER_ID = "USER_ID";
	String USER_EMAIL = "USER_EMAIL";
	String USER_PASS = "USER_PASS";
	String USER_FACE = "USER_FACE";
	String USER_NAME = "USER_NAME";
	String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
	String APP_VERSION = "APP_VERSION";
	
	
	String NIGHTSHIFT_MODE = "NIGHTSHIFT_MODE";
	String IS_RUNNING = "IS_RUNNING";
	
	String EMPTY = "";
	SharedPreferences preference;
	SharedPreferences.Editor preferenceEditor;
	String prefsFileName = "APPLICATION_SHARED_PFERENCE";
	
	public AppPreference(){
		preference = AppSingleTon.CONTEXT.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE);
		preferenceEditor = preference.edit();
	}	
	
	public boolean isVerified(){
		return preference.getBoolean(VERIFY, false);
	}
	
	
	public void setVerify(boolean flag){
		preferenceEditor.putBoolean(VERIFY, flag);
		preferenceEditor.commit();
	}
	
	public void setAppVersion(String versionCode){
		preferenceEditor.putString(APP_VERSION, versionCode);
		preferenceEditor.commit();
	}
	
	public String getAppVersion(){
		return preference.getString(APP_VERSION, EMPTY);
	}
	
	
	public boolean isLoggedin(){
		return preference.getBoolean(LOGIN, false);
	}
	
	public void login(boolean isLogin){
		preferenceEditor.putBoolean(LOGIN, isLogin);
		preferenceEditor.commit();
	}
	
	public void logout(){
		
		setUserId(EMPTY);
		setUserName(EMPTY);
		seUserEmail(EMPTY);
		setUserFace(EMPTY);
		setPassword(EMPTY);

		login(false);
	}
	
	public void setUserId(String id){
		preferenceEditor.putString(USER_ID, id);
		preferenceEditor.commit();
	}
	
	public String getUserId(){
		return preference.getString(USER_ID, EMPTY);
	}
	
	public void setUserName(String name){
		preferenceEditor.putString(USER_NAME, name);
		preferenceEditor.commit();
	}
	
	public String getUserName(){
		return preference.getString(USER_NAME, EMPTY);
	}
	
	public void setPhoneNumber(String number){
		preferenceEditor.putString(USER_PHONE_NUMBER, number);
		preferenceEditor.commit();
	}
	
	public String getPhoneNumber(){
		return preference.getString(USER_PHONE_NUMBER, EMPTY);
	}
	
	public void seUserEmail(String email){
		preferenceEditor.putString(USER_EMAIL, email);
		preferenceEditor.commit();
	}
	
	public String getUserEmail(){
		return preference.getString(USER_EMAIL, EMPTY);
	}
	
	
	public void setPassword(String pass){
		preferenceEditor.putString(USER_PASS, pass);
		preferenceEditor.commit();
	}
	
	public String getPassword(){
		return preference.getString(USER_PASS, EMPTY);
	}
	
	
	
	public void setUserFace(String face){
		preferenceEditor.putString(USER_FACE, face);
		preferenceEditor.commit();
	}
	
	public String getUserFace(){
		return preference.getString(USER_FACE, EMPTY);
	}
	
	
	public boolean isServiceRunning(){
		return preference.getBoolean(IS_RUNNING,false);
	}
	public boolean isNightShiftMode(){
		return preference.getBoolean(NIGHTSHIFT_MODE,false);
	}
	
	public void setNightshiftMode(boolean flag){
		preferenceEditor.putBoolean(NIGHTSHIFT_MODE,flag);
		preferenceEditor.commit();
	}
	public void setServiceRunning(boolean flag){
		preferenceEditor.putBoolean(IS_RUNNING,flag);
		preferenceEditor.commit();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
