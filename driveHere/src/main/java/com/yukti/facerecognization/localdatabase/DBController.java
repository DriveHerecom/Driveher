package com.yukti.facerecognization.localdatabase;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "Mechanical.db";
	private static final String TABLE_User = "User";

	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";
	private static final String FACE_ID = "Faceid";
	private static final String _ID = "Id";

	public DBController(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		// DB_PATH = "/data/data/" + getPackageName() + "/" + "databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_User + "( "
				+ _ID + " INTEGER PRIMARY KEY, " + EMAIL + " TEXT , "
				+ PASSWORD + " TEXT," + FACE_ID + " TEXT)";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
		// Create tables again
		onCreate(db);
	}

	public void add_user(String email, String password, String faceid) {
		SQLiteDatabase database = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(EMAIL, email);
		values.put(PASSWORD, password);
		values.put(FACE_ID, faceid);

		database.insert(TABLE_User, null, values);

		//Cursor c = database.rawQuery("select * from " + TABLE_User, null);

		//int count = c.getCount();

		database.close();

		//return count;
	}

	public HashMap<String, String> get_user(String f_id) {
		HashMap<String, String> emp_credentials = new HashMap<String, String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String query = "select Email , Password from "+TABLE_User+" where Faceid='"+ f_id+"'";
		Cursor c = database.rawQuery(query, null);

		if (c.moveToFirst())
			do {

				// String name=c.getString(c.getColumnIndex("Email"));
				// c.getString(c.getColumnIndex("memberIdOnServer"));
				emp_credentials.put("Email", c.getString(0));
				emp_credentials.put("password", c.getString(1));

			} while (c.moveToNext());

		return emp_credentials;

	}

	public int tableRows() {

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor c = database.rawQuery("select * from User", null);
		int count = c.getCount();

		return count;
	}

	// ///////////////////////////////////////
	// getFaceID

	public boolean getFaceID(String email, String pass) {

		SQLiteDatabase database = this.getReadableDatabase();

		//select Face from User where Email='s@s.com' AND password='s@s.com'
		
		
		String query = "select " + FACE_ID + " from "+TABLE_User+" where " + EMAIL
				+ "='" + email + "' AND " + PASSWORD + "='" + pass+"'";
		
		 //select Face from User where Email='s@s.com' AND Password='s@s.com'
		// String query =
		// "select Email , Password from Employees where face_Id =" +f_id;
		Cursor c = database.rawQuery(query, null);

		if (c.moveToFirst()) {

			return true;

		}

		return false;

	}
	
	
	public boolean checkFaceidexist(String selectedPersonId) {

		SQLiteDatabase database = this.getReadableDatabase();
		Log.e("database", "database");
		
		 String query =
		 "select Email , Password from "+TABLE_User+" where Faceid ='" +selectedPersonId+"'";
		 
		 Cursor c = database.rawQuery(query, null);

		if (c.moveToFirst()) {

			return true;

		}

		return false;

	}
	
		
	
	

}
