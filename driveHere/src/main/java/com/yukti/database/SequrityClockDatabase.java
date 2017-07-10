package com.yukti.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;

import com.yukti.dataone.model.SequrityModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SequrityClockDatabase {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase sqLiteDb;
	private Context hCtx = null;

	public static final String DATABASE_NAME = "drivehere_sequrityclock_db";
	private static final int DATABASE_VERSION = 20;

	// save route table..............//
	public static final String TABLE_TASKMANAGMENTDETAIL = "tbl_TaskManagmentDetail";
	public static final String _ID = "_id";
	public static final String START_TIME = "starttime";
	public static final String CLICK_TIME = "clicktime";
	public static final String DATE = "date";

	public static final String ADDRESS = "adress";
	public static final String LAT = "latitude";
	public static final String LONG = "longitude";
//	public static final String NOTE = "note";

	// public static final String Img = "ImageFile";

	public static final String CLICKABLE = "clickable";

	public static final String CREATE_TABLE_TASKMANAGMENTDETAIL = "CREATE TABLE "
			+ TABLE_TASKMANAGMENTDETAIL
			+ "("
			+ _ID
			+ " INTEGER PRIMARY KEY,"
			+ START_TIME
			+ " TEXT,"
			+ CLICK_TIME
			+ " TEXT,"
			+ DATE
			+ " TEXT,"
			+ ADDRESS + " TEXT," + LAT + " TEXT,"

			+ LONG + " TEXT," 
//			NOTE + " TEXT,"
			// + Img
			// + "BLOB,"

			+ CLICKABLE + " TEXT " + ")";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CREATE_TABLE_TASKMANAGMENTDETAIL);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKMANAGMENTDETAIL);

			onCreate(db);

		}
	}

	public SequrityClockDatabase(Context ctx) {
		hCtx = ctx;
		dbHelper = new DatabaseHelper(hCtx);
		sqLiteDb = dbHelper.getWritableDatabase();
	}

	public SequrityClockDatabase open() throws SQLException {
		dbHelper = new DatabaseHelper(hCtx);
		sqLiteDb = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public void addDetailToTaskManagementTable(String starttime,
			String clicktime, String date, String address, String latitude,
			String longitude,
//			String note,
			// String imgfile,
			String clickable) {

		ContentValues values = new ContentValues();
		values.put(START_TIME, starttime); // route carId
		values.put(CLICK_TIME, clicktime);
		values.put(DATE, date);

		values.put(ADDRESS, address);
		values.put(LAT, latitude);
		values.put(LONG, longitude);
//		values.put(NOTE, note);
		// values.put(Img, imgfile);

		values.put(CLICKABLE, clickable);

		// Inserting Row
		sqLiteDb.insert(TABLE_TASKMANAGMENTDETAIL, null, values);
		sqLiteDb.close(); // Closing database connection

	}

	ArrayList<SequrityModel> sequritydeataillist;
	SequrityModel sequrityModel;

	public ArrayList<SequrityModel> getDetailFromTaskManagementTable()

	{

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		String currentDate = sdf.format(new Date());

		Log.e("today", currentDate);

		sequritydeataillist = new ArrayList<SequrityModel>();
		String selectQuery = "select * from  tbl_TaskManagmentDetail where date='"
				+ currentDate + "'";
		Cursor cursor = sqLiteDb.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				// get the data into array,or class variable
				sequrityModel = new SequrityModel();
				sequrityModel.setStarttime(cursor.getString(1));
				Log.e("starttime", cursor.getString(1));
				sequrityModel.setClicktime(cursor.getString(2));
				Log.e("clicktime", cursor.getString(2));
				sequrityModel.setDate(cursor.getString(3));
				Log.e("date", cursor.getString(3));

				sequrityModel.setAddress(cursor.getString(4));
				Log.e("address", cursor.getString(4));

				sequrityModel.setLatitude(cursor.getString(5));
				Log.e("lattitude", cursor.getString(5));

				sequrityModel.setLongitude(cursor.getString(6));
				Log.e("longitude", cursor.getString(6));

//				sequrityModel.setNote(cursor.getString(7));
//				Log.e("note", cursor.getString(7));
				
				
				sequrityModel.setClickable(cursor.getString(7));
				Log.e("clickable", cursor.getString(7));

				sequritydeataillist.add(sequrityModel);

			} while (cursor.moveToNext());
		}

		return sequritydeataillist;

	}

	/*
	 * public ArrayList<SavedRouteModel> getallSavedRoute() {
	 * 
	 * savedRoutelist = new ArrayList<SavedRouteModel>();
	 * 
	 * String selectQuery = "select * from " + TABLE_SAVEROUTE; Cursor cursor =
	 * sqLiteDb.rawQuery(selectQuery, null);
	 * 
	 * if (cursor.moveToFirst()) { do { // get the data into array,or class
	 * variable routeModel = new SavedRouteModel();
	 * routeModel.setRtid(cursor.getString(1)); Log.e("rtid",
	 * cursor.getString(1)); routeModel.setRtName(cursor.getString(2));
	 * Log.e("RtName", cursor.getString(2));
	 * routeModel.setRtSavedRtName(cursor.getString(3)); Log.e("RtSavedRtName",
	 * cursor.getString(3));
	 * 
	 * savedRoutelist.add(routeModel);
	 * 
	 * } while (cursor.moveToNext()); } sqLiteDb.close(); return savedRoutelist;
	 * }
	 */
	public Long deleteDataFromTaskManagementTable() {

		String table_name = TABLE_TASKMANAGMENTDETAIL;

		long id = sqLiteDb.delete(table_name, null, null);
		return id;

	}

}
