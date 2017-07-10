package com.yukti.driveherenew.search;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.R;

public class MapActivity extends BaseActivity {


	GoogleMap googleMap;
	Location locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		Toolbar	toolbar = (Toolbar) findViewById(R.id.activity_map_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		locations = (Location) getIntent().getExtras().getSerializable(
				"location");
		MapsInitializer.initialize(this);
		initMap();
	}

	void initMap() {

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		if (googleMap == null) {
			showToast("Google map currently unavailable...");
		} else {
			googleMap.getUiSettings().setZoomControlsEnabled(true);

			if (locations != null) {

				setMarker(locations);
			}

		}
	}

	private void setMarker(Location location) {

		Double latitude = Double.valueOf(location.Latitude);
		Double longitude = Double.valueOf(location.Longitude);
		LatLng latLng = new LatLng(latitude, longitude);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title(location.Title);
		markerOptions.snippet("Latitude : " + latitude + "\nLongitude : "
				+ longitude + "\nDate:" + location.CreatedDate);
		markerOptions.icon(BitmapDescriptorFactory
				.defaultMarker((BitmapDescriptorFactory.HUE_BLUE)));
		googleMap.addMarker(markerOptions);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
