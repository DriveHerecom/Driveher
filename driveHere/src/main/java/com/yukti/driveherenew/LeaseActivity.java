package com.yukti.driveherenew;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.LeaseDetail;
import com.yukti.dataone.model.Leasedata;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.RestClient;

public class LeaseActivity extends BaseActivity implements
        MessageDialogListener {

    private static final String TAG = "CustomAdapter";

    CustomAdapter adapter;
    RecyclerView recyclerView;
    String Vin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_report_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(LeaseActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Vin = intent.getStringExtra(CarDetailsActivity.EXTRAKEY_VIN);
        Log.e("Vin number", "Vinnumber" + Vin);
        getLeasedata();

    }

    void setAdapter(ArrayList<LeaseDetail> leasedetail) {
        if (adapter == null) {
            adapter = new CustomAdapter(leasedetail);
            recyclerView.setAdapter(adapter);
        } else {
            // adapter.add(carList);
        }
    }

    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            OnClickListener {

        public List<LeaseDetail> items;

        LayoutInflater inflater;

        public CustomAdapter(ArrayList<LeaseDetail> leasedetail) {

            this.items = leasedetail;
            inflater = LayoutInflater.from(LeaseActivity.this);

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtcustomername, txtcontractid, txtdatanotavailable;

            public ViewHolder(View v) {
                super(v);

                txtcustomername = (TextView) v.findViewById(R.id.customername);
                txtcontractid = (TextView) v.findViewById(R.id.contractid);
                txtdatanotavailable = (TextView) v
                        .findViewById(R.id.Datanotavailable);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = inflater.inflate(R.layout.row_leasecardetail, viewGroup,
                    false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Log.d(TAG, "Element " + position + " set.");
            LeaseDetail customername;

            try {
                customername = items.get(position);

                if (customername.custmername.equalsIgnoreCase("")) {

                    Log.e("data not available", "Result null");
                    viewHolder.txtdatanotavailable.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.txtcustomername
                            .setText(customername.custmername);
                }

                viewHolder.txtcontractid.setText(customername.stocknumber);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onClick(View v) {

        }

    }

    void getLeasedata() {

        final TextView nogetdata = (TextView) findViewById(R.id.tv_no_data);
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("get Lease ", "error response" + responseString);
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("GET_LEASE_response", response.toString());

                Log.e("get Response", "Response get");

                Leasedata leasdata = AppSingleTon.APP_JSON_PARSER
                        .leasedata(response);

                if (leasdata.status_code.equals("1")) {

                    setAdapter(leasdata.customers);

                } else if (leasdata.status_code.equals("2")) {

                    nogetdata.setVisibility(View.VISIBLE);
                    nogetdata.setText("No Data Available");
                }

            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("get Lease Data......");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };
        String url = AppSingleTon.APP_URL.URL_LEASEHISTORY;
        RequestParams params = new RequestParams();
        params.put("vin", Vin);

		/*
         * params.put("carId", car.carId); params.put("stage", selectedstage);
		 */

        RestClient.post(this, url, params, handler);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

}
