package com.yukti.utils;

import java.util.Calendar;
import java.util.Locale;

public class AppCalender {
	
	//private String[] months = {"January","February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	//month starts from January = 0
	//sunday is 1 and saturday is 7
	//hour starts 0 to 11
	
	private String[] days = {"null", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	
	public CalendarBundle getDefualt(){
		
		CalendarBundle bundle = new CalendarBundle();
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		
		bundle.timeinMilis = String.valueOf(calendar.getTimeInMillis());
		bundle.year = String.valueOf(calendar.get(Calendar.YEAR));
		bundle.month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		bundle.dayOfTheMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		bundle.dayOfTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK)];
		
		int tempHour = calendar.get(Calendar.HOUR);
		bundle.hour = (tempHour== 0)? "12":String.valueOf(tempHour);
		
		bundle.minute = String.valueOf(calendar.get(Calendar.MINUTE));
		
		int tempAmPm = calendar.get(Calendar.AM_PM);
		bundle.amPm = (tempAmPm == 0) ? "AM":"PM";
		
		return bundle;
	}
	
	public CalendarBundle getDatePlus(int addValue){
		
		// addValue will be any positive value.
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.add(Calendar.DATE, addValue);
		
		CalendarBundle bundle = new CalendarBundle();
		bundle.timeinMilis = String.valueOf(calendar.getTimeInMillis());
		bundle.year = String.valueOf(calendar.get(Calendar.YEAR));
		
		bundle.month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		bundle.dayOfTheMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		bundle.dayOfTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK)];
		
		int tempHour = calendar.get(Calendar.HOUR);
		bundle.hour = (tempHour== 0)? "12":String.valueOf(tempHour);
		
		bundle.minute = String.valueOf(calendar.get(Calendar.MINUTE));
		
		int tempAmPm = calendar.get(Calendar.AM_PM);
		bundle.amPm = (tempAmPm == 0) ? "AM":"PM";
		
		return bundle;
	}
	
	public CalendarBundle getDateMinus(int minusValue){
		
		// addValue will be any negative value.
		
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.add(Calendar.DATE, minusValue);
		
		CalendarBundle bundle = new CalendarBundle();
		bundle.timeinMilis = String.valueOf(calendar.getTimeInMillis());
		bundle.year = String.valueOf(calendar.get(Calendar.YEAR));
		bundle.month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		bundle.dayOfTheMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		bundle.dayOfTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK)];
		
		int tempHour = calendar.get(Calendar.HOUR);
		bundle.hour = (tempHour== 0)? "12":String.valueOf(tempHour);
		
		bundle.minute = String.valueOf(calendar.get(Calendar.MINUTE));
		
		int tempAmPm = calendar.get(Calendar.AM_PM);
		bundle.amPm = (tempAmPm == 0) ? "AM":"PM";
		
		return bundle;
	}

	public CalendarBundle getHourPlus(int addValue) {

		// addValue will be any positive value.

		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		calendar.add(Calendar.HOUR, addValue);
		
		CalendarBundle bundle = new CalendarBundle();
		bundle.timeinMilis = String.valueOf(calendar.getTimeInMillis());
		bundle.year = String.valueOf(calendar.get(Calendar.YEAR));

		bundle.month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		bundle.dayOfTheMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		bundle.dayOfTheWeek = days[calendar.get(Calendar.DAY_OF_WEEK)];

		int tempHour = calendar.get(Calendar.HOUR);
		bundle.hour = (tempHour == 0) ? "12" : String.valueOf(tempHour);

		bundle.minute = String.valueOf(calendar.get(Calendar.MINUTE));

		int tempAmPm = calendar.get(Calendar.AM_PM);
		bundle.amPm = (tempAmPm == 0) ? "AM" : "PM";

		return bundle;
	}
	
	public class CalendarBundle{
		public String timeinMilis, year, month, dayOfTheMonth, dayOfTheWeek, hour, minute, amPm;
	}
}
