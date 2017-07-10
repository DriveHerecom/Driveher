package com.yukti.driveherenew.search;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.R;

public class HistoryOfEditCarActivity extends BaseActivity {


    Geocoder geocoder;
    List<Address> addresses = null;
    static ArrayList<Location> locations;
    RecyclerView recyclerView;
    ArrayList<String> historyDetailDate;
    ArrayList<String> historyDetailAddress;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_edit_car);
     Toolbar   toolbar = (Toolbar) findViewById(R.id.activity_history_of_edit_car_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         geocoder = new Geocoder(HistoryOfEditCarActivity.this,
                Locale.getDefault());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        locations = (ArrayList<Location>) getIntent().getExtras()
                .getSerializable("location_list");

        Log.e("sds", locations.get(0).Longitude);


        // initRecyclerView(view);


        historyDetailDate = new ArrayList<String>();
        historyDetailAddress = new ArrayList<String>();
        Log.e("Location sizes...", locations.size() + "");
        if (locations != null && locations.size() > 0) {

            new Gethistrory().execute("");
        }
//		geocoder = new Geocoder(HistoryOfEditCarActivity.this,
//				Locale.getDefault());
//
//		ll_container = (LinearLayout) findViewById(R.carId.ll_container);
//
//		if (locations != null && locations.size() > 0) {
//
//			for (int i = locations.size() - 1; i >= 0; i--) {
//
//				Location location = locations.get(i);
//
//				try {
//
//					addresses = geocoder
//							.getFromLocation(Double.valueOf(location.Latitude)
//									.doubleValue(),
//									Double.valueOf(location.Longitude)
//											.doubleValue(), 1);
//				} catch (NumberFormatException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // Here 1 represent max location result to returned, by
//					// documents it recommended 1 to 5
//
//				if (addresses != null && addresses.size() > 0) {
//					try {
//						addViewinList(location);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//			}
//
//			setAdapter(historyDetailDate, historyDetailAddress, locations);
//
//		} else {
//
//			// ((TextView)findViewById(R.carId.tv_no_data)).setVisibility(View.VISIBLE);
//
//		}

    }

    CustomAdapter adapter;

    void setAdapter(ArrayList<String> historyDetail2,
                    ArrayList<String> historyDetailAddress2,
                    ArrayList<Location> locations2) {

        if (adapter == null) {
            adapter = new CustomAdapter(historyDetail2, historyDetailAddress2,
                    locations2);
            recyclerView.setAdapter(adapter);
        } else {
            // adapter.add(carList);
            ((TextView) findViewById(R.id.tv_no_data)).setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            OnClickListener {

        private static final String TAG = "CustomAdapter";
        public List<String> items;
        public List<String> itemsAdress;
        public List<Location> locations2;
        LayoutInflater inflater;

        public CustomAdapter(ArrayList<String> historyDetail2,
                             ArrayList<String> historyDetailAddress2,
                             ArrayList<Location> locations2) {

            this.items = historyDetail2;
            this.itemsAdress = historyDetailAddress2;
            this.locations2 = locations2;

            inflater = LayoutInflater.from(HistoryOfEditCarActivity.this);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView date, adress;

            public ViewHolder(View v) {
                super(v);

                date = (TextView) v.findViewById(R.id.date);
                adress = (TextView) v.findViewById(R.id.adress);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = inflater.inflate(R.layout.row_history, viewGroup, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");

            String historydate = items.get(position);
            String historyAdress = itemsAdress.get(position);

            viewHolder.date.setText(historydate);
            viewHolder.adress.setText(historyAdress);
        }

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildAdapterPosition(v);
            // showToast("clicked "+itemPosition);
//			Intent intent = new Intent(HistoryOfEditCarActivity.this,
//					MapActivity.class);
//			intent.putExtra("latitude", ""
//					+ locations2.get(itemPosition).Latitude);
//			intent.putExtra("longitude", ""
//					+ locations2.get(itemPosition).Latitude);
//			startActivity(intent);
            Intent intent = new Intent(HistoryOfEditCarActivity.this,
                    com.yukti.driveherenew.search.MapActivity.class);
            intent.putExtra("location", locations2.get(itemPosition));
            startActivity(intent);

            // int itemPosition = recyclerView.getChildAdapterPosition(v);
            // // showToast("clicked "+itemPosition);
            // Intent intent = new Intent(ReportActivity.this,
            // MissingCarDetailActivity.class);
            // intent.putExtra("each__missing_car",
            // adapter.items.get(itemPosition));
            // startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private void addViewinList(Location location) throws ParseException {

        // View itemList = null;
        // itemList = getActivity().getLayoutInflater()
        // .inflate(R.layout.custom_row_for_add_car_history,
        // ll_container, false);
        // TextView tv_history = (TextView)
        // itemList.findViewById(R.carId.tv_history);
        // 2015-04-21 10:04:54

        String createddate = location.CreatedDate;
        String date = location.CreatedDate;
        Log.e("Date",createddate + "test" + location.CreatedTime);

        /*date = createddate.substring(0, createddate.indexOf(' '));
        String year = date.substring(0, 4);
        date = date.substring(5);
        date = date.concat("-" + year);
*/
        //Log.e("date", date);
        String time = location.CreatedTime;

        // Date cdate = null;
        // cdate = sdf.parse(date);

        String address = addresses.get(0).getAddressLine(0); // If any
        // additional
        // address line
        // present than
        // only, check
        // with max
        // available
        // address lines
        // by
        // getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); // Only if
        // available
        // else return
        // NULL

//		Log.e("address", address);
//		Log.e("city", city);
//		Log.e("state", state);
//		Log.e("country", country);
//		Log.e("postalcode", postalCode);
//		Log.e("knownName", knownName);

        String history = "Time: " + time + "  Address: " + address + ",  "
                + city + ",  " + "PA " + date;

        String history2 = "<font color='"
                + getResources().getColor(R.color.red) + "'>" + "Date: "
                + "</font>" + date + " " + time + " <font color='"
                + getResources().getColor(R.color.red) + "'>" + "  Address:"
                + "</font>" + address + ",  " + city + "";

        // Toast.makeText(getActivity(), Html.fromHtml(history2),
        // Toast.LENGTH_LONG).show();

        // " <font color='"
        // + getResources().getColor(R.color.gray) + "'>"
        // + "  Address:" + "</font>"+
        // Spanned h =Html.fromHtml(history2);
        String d = date + " " + time;
        String a = address + ",  " + city + "";

        historyDetailDate.add(d);
        historyDetailAddress.add(a);

        // tv_history.setText("Time: "+ time + "  Address: "+ address +",  "+
        // city + ",  " + "PA " + date);
        // ll_container.addView(itemList);
    }

    @SuppressLint("SimpleDateFormat")
    private Date converToDateFromString(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/mm/dd");
        Date mydate = sdf.parse(date);
        Log.e("mydate", mydate + "");
        return mydate;
    }

    public class Gethistrory extends AsyncTask<String, Integer, String> {

        ProgressDialog PDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PDialog = ProgressDialog.show(HistoryOfEditCarActivity.this, "",
                    "Please wait...");
            PDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            Log.e("Loacation size", locations.size() + "");
            if (locations != null && locations.size() > 0) {
                for (int i = 0; i < locations.size(); i++) {
                    Location location = locations.get(i);
                    try {
                        addresses = geocoder.getFromLocation(Double.valueOf(location.Latitude).doubleValue(), Double.valueOf(location.Longitude).doubleValue(), 1);
                        Log.e("geolati", location.Latitude);
                        Log.e("geolong", location.Longitude);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } // Here 1 represent max location result to returned, by
                    // documents it recommended 1 to 5
                    if (addresses != null && addresses.size() > 0) {
                        try {
                            addViewinList(location);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                    }
                }
            } else {
//				 ((TextView)findViewById(R.carId.tv_no_data)).setVisibility(View.VISIBLE);

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PDialog.dismiss();
            setAdapter(historyDetailDate, historyDetailAddress, locations);
        }
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
