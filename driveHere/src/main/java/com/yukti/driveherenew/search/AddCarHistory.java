package com.yukti.driveherenew.search;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yukti.driveherenew.R;

public class AddCarHistory extends Activity {


	ArrayList<Location> locations;
	Geocoder geocoder;
	List<Address> addresses = null;
	LinearLayout ll_container;

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_car_history);

		locations = (ArrayList<Location>) getIntent().getExtras()
				.getSerializable("location_list");
		
		geocoder = new Geocoder(this, Locale.getDefault());
		
		
		ll_container =(LinearLayout)findViewById(R.id.ll_container);

		if(locations != null && locations.size()>0){
			
			for(int i=locations.size()-1; i >=0 ; i--){

				Location location = locations.get(i);
				
				try {
					
					addresses = geocoder.getFromLocation(Double.valueOf(location.Latitude).doubleValue(), Double.valueOf(location.Longitude).doubleValue(), 1);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Here 1 represent max location result to returned, by documents it recommended 1 to 5
				
				if(addresses != null && addresses.size()>0){
			
					try {
						addViewinList(location);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			((TextView)findViewById(R.id.tv_no_data)).setVisibility(View.VISIBLE);
		}
	}
	private void addViewinList(Location location) throws ParseException {
		View itemList = null;
		itemList = AddCarHistory.this.getLayoutInflater().inflate(R.layout.custom_row_for_add_car_history, ll_container, false);
		TextView tv_history = (TextView) itemList.findViewById(R.id.tv_history);
		
		String createddate =location.CreatedDate;
		String date="";
		
		date = createddate.substring(0, createddate.indexOf(' '));
		String year = date.substring(0,4);
		date = date.substring(5);
		date = date.concat("-"+year);
		String time = createddate.substring(createddate.indexOf(' ')).replace(createddate.substring(createddate.length()-3), "");
		String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		String city = addresses.get(0).getLocality();
		tv_history.setText("Time: "+ time + "  Address: "+ address +",  "+ city + ",  " + "PA " + date);

		ll_container.addView(itemList);
	}
}
