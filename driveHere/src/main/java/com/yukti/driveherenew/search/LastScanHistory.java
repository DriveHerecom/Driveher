package com.yukti.driveherenew.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yukti.driveherenew.R;
import com.yukti.utils.GetAddress;

import java.util.ArrayList;

public class LastScanHistory extends AppCompatActivity
{
    ArrayList<Location> locationArrayList = new ArrayList<>();
    String carID;
    CarInventory car;
    Toolbar toolbar;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    TextView txt_noData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_scan_history);
        initToolbar();
        txt_noData = (TextView) findViewById(R.id.txt_noData);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewLastScan);
        car = (CarInventory) getIntent().getExtras().getSerializable("each_car" + "");
        locationArrayList = car.locations;
        carID = car.carId;
        Log.e("Size",locationArrayList.size()+"");
        if (locationArrayList == null || locationArrayList.size()<1)
        {   Log.e("Size1",locationArrayList.size()+"");
            txt_noData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            txt_noData.setVisibility(View.GONE);
            Log.e("Size2",locationArrayList.size()+"");
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new CustomAdapter(locationArrayList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLastScan);
        toolbar.setTitle("Scan History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            View.OnClickListener
    {
        public ArrayList<Location> items;

        LayoutInflater inflater;

        public CustomAdapter(ArrayList<Location> items)
        {
            this.items = items;
            inflater = LayoutInflater.from(LastScanHistory.this);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView txt_carVIN,txt_date,txt_time,txt_address;

            public ViewHolder(View v)
            {
                super(v);
                txt_address = (TextView) v.findViewById(R.id.txt_address);
                txt_carVIN = (TextView) v.findViewById(R.id.txt_carVin);
                txt_date = (TextView) v.findViewById(R.id.txt_date);
                txt_time = (TextView) v.findViewById(R.id.txt_time);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            View v = inflater.inflate(R.layout.cardview_lastscanhistory, viewGroup,false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position)
        {
            Location location = items.get(position);

            viewHolder.txt_carVIN.setText(car.vin);
            GetAddress getAddress = new GetAddress();

            if (location.Address!=null && location.Address.length()>3)
            {
                viewHolder.txt_address.setText(location.Address);
            }
            else
                viewHolder.txt_address.setText("N/A");

            if (location.CreatedDate!=null)
            {
                viewHolder.txt_date.setText(location.CreatedDate);
            }
            else
            {
                viewHolder.txt_date.setText("N/A");
            }


            if (location.CreatedTime!=null)
            {
                viewHolder.txt_time.setText(location.CreatedTime);
            }
            else
            {
                viewHolder.txt_time.setText("N/A");
            }


        }

        @Override
        public int getItemCount()
        {
            return items.size();
        }

        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            Log.e("Position",itemPosition +"test");
            // showToast("clicked "+itemPosition);
//			Intent intent = new Intent(HistoryOfEditCarActivity.this,
//					MapActivity.class);
//			intent.putExtra("latitude", ""
//					+ locations2.get(itemPosition).Latitude);
//			intent.putExtra("longitude", ""
//					+ locations2.get(itemPosition).Latitude);
//			startActivity(intent);
            if (items.get(itemPosition).Address!=null && items.get(itemPosition).Address.length()>3)
            {
                Intent intent = new Intent(LastScanHistory.this, com.yukti.driveherenew.search.MapActivity.class);
                intent.putExtra("location", items.get(itemPosition));
                startActivity(intent);
            }
            else
            {
                Toast.makeText(LastScanHistory.this, "Location not available ..", Toast.LENGTH_SHORT).show();
            }


            // int itemPosition = recyclerView.getChildAdapterPosition(v);
            // // showToast("clicked "+itemPosition);
            // Intent intent = new Intent(ReportActivity.this,
            // MissingCarDetailActivity.class);
            // intent.putExtra("each__missing_car",
            // adapter.items.get(itemPosition));
            // startActivity(intent);
        }
    }

}
