package com.yukti.driveherenew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yukti.dataone.model.*;
import com.yukti.dataone.model.StageDetail;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.driveherenew.search.Search;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Constants;

public class MissCarSearchActivity extends BaseActivity
{

    CustomAdapter adapter;
    RecyclerView recyclerView;
    String lotcode;
    String NO_DATA = "No data Available";

    ProgressDialog progress;
    public static ArrayList<CarInventory> carList = null;
    public NumberOfMissCar numberOfMissCar;

    LinearLayout ll_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miss_car_search);
        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.rv_stages);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(MissCarSearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ll_progress = (LinearLayout) findViewById(R.id.ll_progress);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            lotcode = getIntent().getStringExtra("lotcode");
            Log.e("LotcodeFromBundle", "" + lotcode);
        }

        searchUsingVolley();
    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_miss_car_search_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_progress.setVisibility(View.GONE);
        cancleRequest();
    }
    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Log.e("On Stop", "Cancle request");
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }
    void searchUsingVolley() {

        ll_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_GET_MISSING_CAR_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ll_progress.setVisibility(View.GONE);

                        Search search = AppSingleTon.APP_JSON_PARSER.search(response);
                        if (search.count.equals("0")) {
                            return;
                        }

                        carList = search.result;
                        Log.e(" car list", "carlist " + carList);

                        if (carList != null && carList.size() > 0) {

                            numberOfMissCar = new NumberOfMissCar();
                            ArrayList<StageDetail> sdList = new ArrayList<>();

                            HashMap<String, Integer> hashStageList = numberOfMissCar.hashStageList;
                            Log.e("Car size", "" + carList.size());
                            for (int i = 0; i < carList.size(); i++)
                            {
                                // All,Auction,Junk,MC,WFP,PA,OneOneEight,QC,FortyMiles,Detailing,Cb,Ready;
                                CarInventory carInventory = carList.get(i);
                                Log.e("Car vaccancy", "" + carInventory.vacancy);

                                if (carInventory.vacancy!=null && carInventory.vacancy.equalsIgnoreCase("available"))
                                {
                                    if (hashStageList.containsKey("available"))
                                    {
                                        int n1 = hashStageList.get("available");
                                        hashStageList.put("available", n1 + 1);

                                        if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("MC"))
                                        {
                                            if (hashStageList.containsKey("MC"))
                                            {
                                                int n = hashStageList.get("MC");
                                                hashStageList.put("MC", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("MC", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("W/P-118"))
                                        {
                                            if (hashStageList.containsKey("W/P-118"))
                                            {
                                                int n = hashStageList.get("W/P-118");
                                                hashStageList.put("W/P-118", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("W/P-118", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("W/P-CB"))
                                        {
                                            if (hashStageList.containsKey("W/P-CB"))
                                            {
                                                int n = hashStageList.get("W/P-CB");
                                                hashStageList.put("W/P-CB", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("W/P-CB", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("W/P-Customer"))
                                        {
                                            if (hashStageList.containsKey("W/P-Customer"))
                                            {
                                                int n = hashStageList.get("W/P-Customer");
                                                hashStageList.put("W/P-Customer", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("W/P-Customer", 1);
                                            }
                                        }
                                       /* else if (carInventory.Stage.equalsIgnoreCase("P/H"))
                                        {
                                            if (hashStageList.containsKey("P/H"))
                                            {
                                                int n = hashStageList.get("P/H");
                                                hashStageList.put("P/H", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("P/H", 1);
                                            }
                                        }*/
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("P/H-118"))
                                        {
                                            if (hashStageList.containsKey("P/H-118"))
                                            {
                                                int n = hashStageList.get("P/H-118");
                                                hashStageList.put("P/H-118", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("P/H-118", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("P/H-CB"))
                                        {
                                            if (hashStageList.containsKey("P/H-CB"))
                                            {
                                                int n = hashStageList.get("P/H-CB");
                                                hashStageList.put("P/H-CB", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("P/H-CB", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("P/H-Customer"))
                                        {
                                            if (hashStageList.containsKey("P/H-Customer"))
                                            {
                                                int n = hashStageList.get("P/H-Customer");
                                                hashStageList.put("P/H-Customer", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("P/H-Customer", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("118"))
                                        {
                                            if (hashStageList.containsKey("118"))
                                            {
                                                int n = hashStageList.get("118");
                                                hashStageList.put("118", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("118", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("QC"))
                                        {
                                            if (hashStageList.containsKey("QC"))
                                            {
                                                int n = hashStageList.get("QC");
                                                hashStageList.put("QC", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("QC", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("CB"))
                                        {
                                            if (hashStageList.containsKey("CB"))
                                            {
                                                int n = hashStageList.get("CB");
                                                hashStageList.put("CB", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("CB", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("40 Miles"))
                                        {
                                            if (hashStageList.containsKey("40 Miles"))
                                            {
                                                int n = hashStageList.get("40 Miles");
                                                hashStageList.put("40 Miles", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("40 Miles", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Detailing"))
                                        {
                                            if (hashStageList.containsKey("Detailing"))
                                            {
                                                int n = hashStageList.get("Detailing");
                                                hashStageList.put("Detailing", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Detailing", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Ready"))
                                        {
                                            if (hashStageList.containsKey("Ready"))
                                            {
                                                int n = hashStageList.get("Ready");
                                                hashStageList.put("Ready", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Ready", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Hold PPA"))
                                        {
                                            if (hashStageList.containsKey("Hold PPA"))
                                            {
                                                int n = hashStageList.get("Hold PPA");
                                                hashStageList.put("Hold PPA", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Hold PPA", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Hold Repo"))
                                        {
                                            if (hashStageList.containsKey("Hold Repo"))
                                            {
                                                int n = hashStageList.get("Hold Repo");
                                                hashStageList.put("Hold Repo", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Hold Repo", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Select"))
                                        {
                                            if (hashStageList.containsKey("Unknown"))
                                            {
                                                int n = hashStageList.get("Unknown");
                                                hashStageList.put("Unknown", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Unknown", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase(""))
                                        {
                                            if (hashStageList.containsKey("Unknown"))
                                            {
                                                int n = hashStageList.get("Unknown");
                                                hashStageList.put("Unknown", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Unknown", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Junk"))
                                        {
                                            if (hashStageList.containsKey("Junk"))
                                            {
                                                int n = hashStageList.get("Junk");
                                                hashStageList.put("Junk", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Junk", 1);
                                            }
                                        }
                                        else if (carInventory.Stage!=null && carInventory.Stage.equalsIgnoreCase("Auction"))
                                        {
                                            if (hashStageList.containsKey("Auction"))
                                            {
                                                int n = hashStageList.get("Auction");
                                                hashStageList.put("Auction", n + 1);
                                            }
                                            else
                                            {
                                                hashStageList.put("Auction", 1);
                                            }
                                        }
                                    }
                                    else {
                                        hashStageList.put("available", 1);
                                    }
                                }

                            }

                            if (hashStageList.containsKey("available"))
                            {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("available");
                                stageDetail.setTotal(hashStageList.get("available"));
                                Log.e("ava",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("MC")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("MC");
                                stageDetail.setTotal(hashStageList.get("MC"));
                                Log.e("mc",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("W/P-118")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("W/P-118");
                                stageDetail.setTotal(hashStageList.get("W/P-118"));
                                Log.e("w/p-118",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("W/P-CB")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("W/P-CB");
                                stageDetail.setTotal(hashStageList.get("W/P-CB"));
                                Log.e("W/P-CB",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("W/P-Customer")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("W/P-Customer");
                                stageDetail.setTotal(hashStageList.get("W/P-Customer"));
                                Log.e("W/P-Customer",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            /*if (hashStageList.containsKey("P/H")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("P/H");
                                stageDetail.setTotal(hashStageList.get("P/H"));
                                Log.e("P/H",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }*/
                            if (hashStageList.containsKey("P/H-118")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("P/H-118");
                                stageDetail.setTotal(hashStageList.get("P/H-118"));
                                Log.e("P/H-118",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("P/H-CB")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("P/H-CB");
                                stageDetail.setTotal(hashStageList.get("P/H-CB"));
                                Log.e("P/H-CB",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("P/H-Customer")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("P/H-Customer");
                                stageDetail.setTotal(hashStageList.get("P/H-Customer"));
                                Log.e("P/H-Custome",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("118")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("118");
                                stageDetail.setTotal(hashStageList.get("118"));
                                Log.e("118",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("QC")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("QC");
                                stageDetail.setTotal(hashStageList.get("QC"));
                                Log.e("QC",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("CB")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("CB");
                                stageDetail.setTotal(hashStageList.get("CB"));
                                Log.e("CB",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("40 Miles")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("40 Miles");
                                stageDetail.setTotal(hashStageList.get("40 Miles"));
                                Log.e("40 Miles",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Detailing")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Detailing");
                                stageDetail.setTotal(hashStageList.get("Detailing"));
                                Log.e("Detailing",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Ready")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Ready");
                                stageDetail.setTotal(hashStageList.get("Ready"));
                                Log.e("Ready",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Hold PPA")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Hold PPA");
                                stageDetail.setTotal(hashStageList.get("Hold PPA"));
                                Log.e("Hold PPA",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Hold Repo")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Hold Repo");
                                stageDetail.setTotal(hashStageList.get("Hold Repo"));
                                Log.e("Hold Repo",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Unknown")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Unknown");
                                stageDetail.setTotal(hashStageList.get("Unknown"));
                                Log.e("Unknown",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Junk")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Junk");
                                stageDetail.setTotal(hashStageList.get("Junk"));
                                Log.e("Junk",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            if (hashStageList.containsKey("Auction")) {
                                StageDetail stageDetail = new StageDetail();
                                stageDetail.setStageName("Auction");
                                stageDetail.setTotal(hashStageList.get("Auction"));
                                Log.e("Auction",""+stageDetail.getTotal());
                                sdList.add(stageDetail);
                            }
                            Log.e("Size  of  list",""+sdList.size());
                            setAdapter(sdList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        ll_progress.setVisibility(View.GONE);
                        Log.e("RESPONSE ", "for MissCar Search" + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("lotcode", lotcode);
                return params;
            }
        };
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    void setAdapter(ArrayList<StageDetail> sdList) {
        if (adapter == null) {
            adapter = new CustomAdapter(sdList);
            recyclerView.setAdapter(adapter);
        } else {
            // adapter.add(carList);
        }
    }


    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            View.OnClickListener {

        public List<StageDetail> items;

        LayoutInflater inflater;

        public CustomAdapter(ArrayList<StageDetail> sdList) {
            this.items = sdList;
            inflater = LayoutInflater.from(MissCarSearchActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = inflater.inflate(R.layout.row_stagenames, viewGroup, false);
            v.setOnClickListener(this);

            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Log.e("MissCarSearchActivity", "Element " + position + " set.");

            StageDetail stage = items.get(position);
            if(stage.getStageName()!=null && stage.getStageName().toString().equalsIgnoreCase("available"))
            {
                viewHolder.txtStageName.setText("All");
                viewHolder.txtTotal.setText(""+stage.getTotal());
            }
            else
            {
                viewHolder.txtStageName.setText(stage.getStageName().toString());
                viewHolder.txtTotal.setText(""+stage.getTotal());
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtStageName, txtTotal;

            public ViewHolder(View v) {
                super(v);

                txtStageName = (TextView) v.findViewById(R.id.tv_stagename);
                txtTotal = (TextView) v.findViewById(R.id.tv_total);
            }
        }

        @Override
        public void onClick(View v) {

            int itemPosition = recyclerView.getChildAdapterPosition(v);
            // showToast("clicked "+itemPosition);
            //sendStage(items.get(itemPosition));

            /*Intent i = new Intent(MissCarSearchActivity.this,AuctionLotCodeDetailsActivity.class);
            i.putExtra("auctionname", items.get(itemPosition).auctionname);
            Log.e("selected_auctionname", "" + items.get(itemPosition).auctionname);
            startActivity(i);*/
            String s = null;
            Intent i = new Intent(MissCarSearchActivity.this, MissingCarFullDetailActivity.class);
            // i.putExtra("carlist",list);
            Log.e("Put Extra", items.get(itemPosition).getStageName());
            if(items.get(itemPosition)!=null && items.get(itemPosition).getStageName()!=null &&
                    items.get(itemPosition).getStageName().equalsIgnoreCase("Unknown"))
            {
                s = "";
                i.putExtra("SelectedItem", s);
            }
            else if(items.get(itemPosition)!=null && items.get(itemPosition).getStageName()!=null &&
                    items.get(itemPosition).getStageName().equalsIgnoreCase("available"))
            {
                s = "All";
                i.putExtra("SelectedItem", s);
            }
            else
            {
                i.putExtra("SelectedItem", items.get(itemPosition).getStageName());
            }
            startActivity(i);
        }
    }


    @Override
    protected void onStop() {

        super.onStop();
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
}
