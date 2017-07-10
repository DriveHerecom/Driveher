package com.yukti.facerecognization.localdatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Administrator
 *
 *         Class for methods which save settings values
 *
 */
public class SettingStore {

	private String PRE_IsRunning = "isRunning";
	private String PRE_StartTime = "runningTime";
	private String PRE_BtnFlag = "btnFlag";
	private String PRE_LiftSelection = "liftSelection";
	private String PRE_TaskId = "taskId";
	private String PRE_Status = "status";

	private String PRE_UserId = "user_id";
	private String PRE_UserName = "user_name";
	private String PRE_IsFirst = "isfirst";
	private String PRE_Email = "email";
	private String PRE_Login = "login";
	private String PRE_Password = "password";

	private String PRE_TrainerUserName = "Tuser_name";
	private String PRE_TrainerPassword = "Tpassword";

	private String PRE_LearnerUserName = "Luser_name";
	private String PRE_LearnerPassword = "Lpassword";

	private String PRE_DrawerLearned = "drawer_learned";
	private String PRE_ServerApp_Version="ServerApp_Version";

	SharedPreferences pref;
	public static SettingStore store;

	// *******************************************************

	public boolean getPRE_IsRunning() {
		return pref.getBoolean(PRE_IsRunning, false);
	}

	public void setPRE_IsRunning(Boolean value) {
		pref.edit().putBoolean(PRE_IsRunning, value).commit();
	}

	public boolean getPRE_BtnFlag() {
		return pref.getBoolean(PRE_BtnFlag, false);
	}

	public void setPRE_BtnFlag(Boolean value) {
		pref.edit().putBoolean(PRE_BtnFlag, value).commit();
	}

	public String getPRE_StartTime() {
		return pref.getString(PRE_StartTime, "");
	}

	public void setPRE_StartTime(String startTime) {
		pref.edit().putString(PRE_StartTime, startTime).commit();
	}

	public int getPRE_LiftSelection() {
		return pref.getInt(PRE_LiftSelection, 0);
	}

	public void setPRE_LiftSelection(int liftSelection) {
		pref.edit().putInt(PRE_LiftSelection, liftSelection).commit();
	}

	public String getPRE_TaskId() {
		return pref.getString(PRE_TaskId, "");
	}

	public void setPRE_TaskId(String taskId) {
		pref.edit().putString(PRE_TaskId, taskId).commit();
	}

	public String getPRE_Status() {
		return pref.getString(PRE_Status, "");
	}

	public void setPRE_Status(String status) {
		pref.edit().putString(PRE_Status, status).commit();
	}

	public int getPRE_ServerApp_Version() {
		return pref.getInt(PRE_ServerApp_Version, 0);
	}

	public void setPRE_ServerApp_Version(int value) {
		pref.edit().putInt(PRE_ServerApp_Version, value).commit();
	}


	// *******************************************************

	public String getPRE_TrainerLogin() {
		return pref.getString(PRE_TrainerLogin, "");
	}

	public void setPRE_TrainerLogin(String value) {
		pref.edit().putString(PRE_TrainerLogin, value).commit();
	}

	public String getPRE_LearnerLogin() {
		return pref.getString(PRE_LearnerLogin, "");
	}

	public void setPRE_LearnerLogin(String value) {
		pref.edit().putString(PRE_LearnerLogin, value).commit();
	}

	private String PRE_TrainerLogin = "false";
	private String PRE_LearnerLogin = "false";

	public void setPRE_TrainerUserName(String value) {
		pref.edit().putString(PRE_TrainerUserName, value).commit();
	}

	public String getPRE_TrainerUserName() {
		return pref.getString(PRE_TrainerUserName, "");
	}

	public void setPRE_TrainerPassword(String value) {
		pref.edit().putString(PRE_TrainerPassword, value).commit();
	}

	public String getPRE_TrainerPassword() {
		return pref.getString(PRE_TrainerPassword, "");
	}

	public void setPRE_LearnerUserName(String value) {
		pref.edit().putString(PRE_LearnerUserName, value).commit();
	}

	public String getPRE_LearnerUserName() {
		return pref.getString(PRE_LearnerUserName, "");
	}

	public void setPRE_LearnerPassword(String value) {
		pref.edit().putString(PRE_LearnerPassword, value).commit();
	}

	public String getPRE_LearnerPassword() {
		return pref.getString(PRE_LearnerPassword, "");
	}

	public static SettingStore getInstance(Context context) {
		if (store == null)
			store = new SettingStore(context);
		return store;
	}

	public SettingStore(Context context) {
		pref = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public boolean getPreLogin() {
		return pref.getBoolean(PRE_Login, false);
	}

	public boolean getPreDrawerLearned() {
		return pref.getBoolean(PRE_DrawerLearned, false);
	}

	public boolean getPRE_IsFirst() {
		return pref.getBoolean(PRE_IsFirst, true);
	}

	public String getPreUserId() {
		return pref.getString(PRE_UserId, "0");
	}



	public String getPreUserName() {
		return pref.getString(PRE_UserName, "");
	}

	public String getPre_Email() {
		return pref.getString(PRE_Email, "");
	}

	public String getPRE_Password() {
		return pref.getString(PRE_Password, "");
	}

	// *******************************************************

	public void setPreLogin(Boolean value) {
		pref.edit().putBoolean(PRE_Login, value).commit();
	}

	public void setPreDrawerLearned(Boolean value) {
		pref.edit().putBoolean(PRE_DrawerLearned, value).commit();
	}

	public void setPRE_IsFirst(Boolean value) {
		pref.edit().putBoolean(PRE_IsFirst, value).commit();
	}

	public void setPreUserId(String value) {
		pref.edit().putString(PRE_UserId, value).commit();
	}

	public void setPreUserName(String value) {
		pref.edit().putString(PRE_UserName, value).commit();
	}

	public void setPre_Email(String value) {
		pref.edit().putString(PRE_Email, value).commit();
	}

	public void setPRE_Password(String value) {
		pref.edit().putString(PRE_Password, value).commit();
	}

}
