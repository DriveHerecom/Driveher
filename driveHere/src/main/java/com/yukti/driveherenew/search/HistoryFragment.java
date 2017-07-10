package com.yukti.driveherenew.search;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yukti.driveherenew.R;

public class HistoryFragment extends Fragment {
    LinearLayoutManager layoutManager;
    List<Address> addresses = null;
    LinearLayout ll_container;
    static ArrayList<Location> locations;

    public static HistoryFragment newInstance(String catName,
                                              ArrayList<Location> locationx) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", catName);
        fragment.setArguments(bundle);
        locations = locationx;
        return fragment;
    }

    ArrayList<String> historyDetailDate;
    ArrayList<String> historyDetailAddress;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_car_history,
                container, false);
        historyDetailDate = new ArrayList<String>();
        historyDetailAddress = new ArrayList<String>();
        // initRecyclerView(view);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Geocoder    geocoder = new Geocoder(getActivity(), Locale.getDefault());
        ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
        Log.e("Location size..", locations.size() + "");
        if (locations != null && locations.size() > 0) {
            for (int i = locations.size() - 1; i >= 0; i--) {
                Location location = locations.get(i);
                try {
                    addresses = geocoder
                            .getFromLocation(Double.valueOf(location.Latitude)
                                            .doubleValue(),
                                    Double.valueOf(location.Longitude)
                                            .doubleValue(), 1);
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
            setAdapter(historyDetailDate, historyDetailAddress);
            ((TextView) view.findViewById(R.id.tv_no_data)).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.tv_no_data)).setVisibility(View.VISIBLE);
        }
        return view;
    }

    void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }
    RecyclerView recyclerView;
    CustomAdapter adapter;

    void setAdapter(ArrayList<String> historyDetail2,
                    ArrayList<String> historyDetailAddress2) {
        if (adapter == null) {
            adapter = new CustomAdapter(historyDetail2, historyDetailAddress2);
            recyclerView.setAdapter(adapter);
        } else {
            // adapter.add(carList);
        }
    }

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        private static final String TAG = "CustomAdapter";

        public List<String> items;
        public List<String> itemsAdress;
        LayoutInflater inflater;

        public CustomAdapter(ArrayList<String> historyDetail2,
                             ArrayList<String> historyDetailAddress2) {

            this.items = historyDetail2;
            this.itemsAdress = historyDetailAddress2;

            inflater = LayoutInflater.from(getActivity());
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
        public int getItemCount() {
            return items.size();
        }

    }

    private void addViewinList(Location location) throws ParseException {
        // TODO Auto-generated method stub
        // View itemList = null;
        // itemList = getActivity().getLayoutInflater()
        // .inflate(R.layout.custom_row_for_add_car_history,
        // ll_container, false);
        // TextView tv_history = (TextView)
        // itemList.findViewById(R.carId.tv_history);
        // 2015-04-21 10:04:54

        String createddate = location.CreatedDate;
        String date = "";

        date = createddate.substring(0, createddate.indexOf(' '));
        String year = date.substring(0, 4);
        date = date.substring(5);
        date = date.concat("-" + year);

        Log.e("date", date);
        String time = createddate.substring(createddate.indexOf(' ')).replace(
                createddate.substring(createddate.length() - 3), "");

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
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if
        // available
        // else return
        // NULL

        Log.e("address", address);
        Log.e("city", city);
        Log.e("address", address);
        Log.e("state", state);
        Log.e("country", country);
        Log.e("postalcode", postalCode);
        Log.e("knownName", knownName);

        String history = "Time: " + time + "  Address: " + address + ",  "
                + city + ",  " + "PA " + date;

        String history2 = "<font color='"
                + getResources().getColor(R.color.red) + "'>" + "Date: "
                + "</font>" + date + " " + time + " <font color='"
                + getResources().getColor(R.color.red) + "'>" + "  Address:"
                + "</font>" + address + ",  " + city + "";

//		Toast.makeText(getActivity(), Html.fromHtml(history2),
//				Toast.LENGTH_LONG).show();

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
}
