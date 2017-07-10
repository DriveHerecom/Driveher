package com.yukti.driveherenew.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.yukti.driveherenew.AllDetailActivity;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.Constant;
import com.yukti.utils.ContractData;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.SearchContract;
import com.yukti.utils.SearchContractData;

public class SearchResultPastContract extends BaseActivity
{
    TextView errorText;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    LinearLayout ll_searchProgress;
    boolean isSearching = false;
    ArrayList<SearchContractData> carList = null;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_search_result_past_contract);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_result_contract_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Result ");
        ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearchPC);
        errorText = (TextView) findViewById(R.id.errorTextPC);

        initRecyclerView();
        seachUsingVolley();

        recyclerView.setHasFixedSize(true);
    }

    void initRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPC);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    void showHideRecyclerView(boolean flag)
    {
        if (flag)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    void setAdapter()
    {
        if (adapter == null)
        {
            Log.e("setadapter", "in if" + carList.size());
            adapter = new CustomAdapter(carList);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            Log.e("setadapter", "in else");
        }
    }

    void seachUsingVolley()
    {
        isSearching = true;
        ll_searchProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_SEARCH_CONTRACT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        isSearching = false;
                        ll_searchProgress.setVisibility(View.GONE);
                        Log.e("Volley Response " , " " + response);

                        SearchContract search = AppSingleTon.APP_JSON_PARSER.searchcontract(response);

                        if (search.status_code.equals("0") || search.status_code.equals("2"))
                        {
                            Log.e("status code", search.status_code);
                            errorText.setVisibility(View.VISIBLE);
                            getSupportActionBar().setTitle("Found " + 0 + " items");
                            errorText.setText("Found Nothing !");
                            return;
                        }
                        else
                        if (search.status_code.equals("4"))
                        {
                            errorText.setVisibility(View.VISIBLE);
                            getSupportActionBar().setTitle("Authentication Error");
                            errorText.setText(search.message);
                            return;
                        }
                        else
                        if (search.status_code.equals("1")&&search.contracts != null && search.contracts.size() > 0)
                        {
                            Log.e("status code", search.status_code + " " + search.contracts.size());
                            carList = search.contracts;
                            setAdapter();
                            getSupportActionBar().setTitle("Found " + search.contracts.size() + " items");
                            showHideRecyclerView(true);
                        }
                        else
                        {
                            Toast.makeText(SearchResultPastContract.this, "" + search.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                isSearching = false;
                ll_searchProgress.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> params = new HashMap<String, String>();
                ContractData search_query = (ContractData) getIntent().getExtras().getSerializable("search_query");
                params.put(ParamsKey.KEY_vin, search_query.car_vin);
                params.put(ParamsKey.KEY_rfid, search_query.car_rfid);
                params.put(ParamsKey.KEY_customerName, search_query.customer_name);
                params.put(ParamsKey.KEY_stockNumber, search_query.stock_number);
                params.put(ParamsKey.KEY_TAG, search_query.tag);
                params.put(ParamsKey.KEY_contractId, search_query.contract_id);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                return params;
            }
        };
        stringRequest.setTag(com.yukti.utils.Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                com.yukti.utils.Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_result, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        ll_searchProgress.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest()
    {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null)
        {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(com.yukti.utils.Constants.REQUEST_TAG);
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

    public static final int REQUEST_ACTION_SEARCH = 2012;

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements View.OnClickListener
    {
        private static final String TAG = "CustomAdapter";

        public List<SearchContractData> items;
        LayoutInflater inflater;

        public CustomAdapter(List<SearchContractData> items)
        {
            this.items = items;
            inflater = LayoutInflater.from(SearchResultPastContract.this);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv_vin, tv_contract_id, tv_title, tv_days, tv_lot_code, tv_cust_name,tv_bookingdate,tv_expexteddate;
            ImageView img123;
            LinearLayout ll_lotcode_container;

            public ViewHolder(View v)
            {
                super(v);
                img123 = (ImageView) v.findViewById(R.id.img1);
                tv_vin = (TextView) v.findViewById(R.id.tv_vin1);
                tv_contract_id = (TextView) v.findViewById(R.id.tv_contract1);
                tv_cust_name = (TextView) v.findViewById(R.id.tv_cust_name1);
                tv_title = (TextView) v.findViewById(R.id.tv_title1);
                tv_lot_code = (TextView) v.findViewById(R.id.tv_lotcode1);
                tv_days = (TextView) v.findViewById(R.id.tv_days1);
                tv_bookingdate = (TextView) v.findViewById(R.id.tv_booking_date);
                tv_expexteddate = (TextView) v.findViewById(R.id.tv_expected_date);
                ll_lotcode_container = (LinearLayout) v.findViewById(R.id.ll_lotcode_container1);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            View v = inflater.inflate(R.layout.card_search_contract, viewGroup, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position)
        {
            SearchContractData car = items.get(position);

            if (car.imagePath != null && car.imagePath.length() > 0)
            {
                if(car.imagePath != null && car.imagePath.length() > 0)
                {
                    String path = (car.imagePath).replace(" ", "%20");
                    Picasso.with(SearchResultPastContract.this).load(path).into(viewHolder.img123);
                }
                else
                {
                    viewHolder.img123.setImageResource(R.drawable.ic_default_car);
                }
            }
            else
            {
                viewHolder.img123.setImageResource(R.drawable.ic_default_car);
            }

            if (car.vin.length() > 0 && !car.vin.equalsIgnoreCase("null"))
                viewHolder.tv_vin.setText(car.vin);
            else
                viewHolder.tv_vin.setText("N/A");

            String title = car.modelYear + " " + car.make + " " + car.model;
            String temp_title = title.replace(" ", "");
            if (temp_title.length() > 0 && !temp_title.equalsIgnoreCase("null"))
                viewHolder.tv_title.setText(title);
            else
                viewHolder.tv_title.setText("N/A");

            if (car.contractId.length() > 0 && !car.contractId.equalsIgnoreCase("null"))
                viewHolder.tv_contract_id.setText(car.contractId);
            else
                viewHolder.tv_contract_id.setText("N/A");

            if (car.lotCode.length() > 0 && !car.lotCode.equalsIgnoreCase("null"))
                viewHolder.tv_lot_code.setText(car.lotCode);
            else
                viewHolder.tv_lot_code.setText("N/A");


            if (car.receivedDate.length() > 0 && !car.receivedDate.equalsIgnoreCase("null"))
                viewHolder.tv_expexteddate.setText(car.receivedDate);
            else
                viewHolder.tv_expexteddate.setText("N/A");

            if (car.bookingDate.length() > 0 && !car.bookingDate.equalsIgnoreCase("null"))
                viewHolder.tv_bookingdate.setText(car.bookingDate);
            else
                viewHolder.tv_bookingdate.setText("N/A");

            viewHolder.tv_days.setText(car.days);
            viewHolder.tv_cust_name.setText(car.fname + " " + car.lname);
        }

        @Override
        public int getItemCount()
        {
            return items.size();
        }

        public void add(SearchContractData item, int position)
        {
            items.add(position, item);
            notifyItemInserted(position);
        }

        public void add(ArrayList<SearchContractData> elements)
        {
            for (int i = 0; i < elements.size(); i++)
            {
                items.add(elements.get(i));
                int position = items.indexOf(elements.get(i));
                notifyItemInserted(position);
            }
        }

        public void remove(SearchContractData item)
        {
            int position = items.indexOf(item);
            items.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onClick(View v)
        {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            if (Common.isNetworkConnected(getApplicationContext()))
            {
                Intent intent2 = new Intent(getApplication(), AllDetailActivity.class);
                intent2.putExtra(AllDetailActivity.EXTRA_KEY_CONTRACT_ID, items.get(itemPosition).contractId);
                startActivity(intent2);
            }
            else
            {
                Toast.makeText(SearchResultPastContract.this, "Please Check Internet Connection ..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}