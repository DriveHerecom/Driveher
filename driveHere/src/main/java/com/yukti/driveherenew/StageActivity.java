package com.yukti.driveherenew;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.yukti.jsonparser.GeneralOrm;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StageActivity extends BaseActivity {
    public List<Stageresult> items;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    StageDetail stage;
    LinearLayout ll_stageProgress;
    TextView tb_title, tb_totalCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_stage_app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.app_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tb_title = (TextView) toolbar.findViewById(R.id.tv_title);
        tb_title.setText("Stage Graph");
        tb_totalCar = (TextView) toolbar.findViewById(R.id.tv_totalCar);

        ll_stageProgress = (LinearLayout) findViewById(R.id.ll_progressStage);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(StageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        GetReportDataUsingVolley();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mailjson, menu);
        return true;
    }

    void setAdapter(ArrayList<Stageresult> result) {
        if (adapter == null) {
            adapter = new CustomAdapter(result);
            recyclerView.setAdapter(adapter);

        } else {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.sendMail:
                sendMailUsingVolley();
                return true;
        }
        return true;
    }

    void sendIntent(String stage, String level) {
        Intent i = new Intent(StageActivity.this, Stagewithcaractivity.class);
        i.putExtra(Constant.EXTRA_KEY_STAGE, stage);
        i.putExtra(Constant.EXTRA_KEY_LEVEL, level);
        startActivity(i);
    }

    void GetReportDataUsingVolley() {
        ll_stageProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_STAGE_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stage = AppSingleTon.APP_JSON_PARSER.stagedata(response);
                ll_stageProgress.setVisibility(View.GONE);
                Log.e("Response", "" + response);
                if (stage.status_code.equals("1")) {
                    if (stage.result != null && stage.result.size() > 0) {
                        for (int i = 0; i < stage.result.size(); i++) {
                            Stageresult stagedetail = stage.result.get(i);
                            if (stagedetail.totalcarinstage.equalsIgnoreCase("")) {
                                stagedetail.totalcarInstagePercentage = 0;
                            } else {
                                String valuetotalcarinstage = stagedetail.totalcarinstage;
                                int valuetotalcarinstageone = Integer.parseInt(valuetotalcarinstage) * 100;
                                int valuetotalcarinstagetwo = Integer.parseInt(stagedetail.totalcar);

                                int percentag = (valuetotalcarinstageone / valuetotalcarinstagetwo);
                                stagedetail.totalcarInstagePercentage = percentag;
                            }

                            if (stagedetail.days0to7.equalsIgnoreCase("")) {
                                stagedetail.days0to7Percentage = 0;
                            } else {
                                String value0to7 = stagedetail.days0to7;
                                int value0toone = Integer.parseInt(value0to7) * 100;
                                int value0totwo = Integer.parseInt(stagedetail.totalcarinstage);
                                int percentag = (value0toone / value0totwo);
                                stagedetail.days0to7Percentage = percentag;
                            }

                            if (stagedetail.days8to15.equalsIgnoreCase("")) {
                                stagedetail.days8to15Percentage = 0;
                            } else {
                                String value8to15 = stagedetail.days8to15;
                                int value8to15one = Integer.parseInt(value8to15) * 100;
                                int value8to15two = Integer.parseInt(stagedetail.totalcarinstage);
                                int percentag = (value8to15one / value8to15two);
                                stagedetail.days8to15Percentage = percentag;
                            }

                            if (stagedetail.days16to30.equalsIgnoreCase("")) {
                                stagedetail.days16to30Percentage = 0;
                            } else {
                                String value16to30 = stagedetail.days16to30;
                                int value16to30one = Integer.parseInt(value16to30) * 100;
                                int value16to30two = Integer.parseInt(stagedetail.totalcarinstage);
                                int percentag = (value16to30one / value16to30two);
                                stagedetail.days16to30Percentage = percentag;
                            }

                            if (stagedetail.days30more.equalsIgnoreCase("")) {
                                stagedetail.days30morePercentage = 0;
                            } else {
                                String value30tomore = stagedetail.days30more;
                                int value30moreone = Integer.parseInt(value30tomore) * 100;
                                int value30tomoretwo = Integer.parseInt(stagedetail.totalcarinstage);
                                int percentag = (value30moreone / value30tomoretwo);
                                stagedetail.days30morePercentage = percentag;
                            }
                        }
                    }
                    setAdapter(stage.result);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ll_stageProgress.setVisibility(View.GONE);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                return params;
            }
        };
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_stageProgress.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }

    void sendMailUsingVolley() {
        ll_stageProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_SEND_MAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ll_stageProgress.setVisibility(View.GONE);
                GeneralOrm generalOrm = AppSingleTon.APP_JSON_PARSER.general(response);
                Log.e("Reposne",response + "Test");
                if (generalOrm.status_code.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Send Mail Successfully..", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        showToast("Sending Fail");
                        ll_stageProgress.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject jobj = null;
                    JSONArray jarray = new JSONArray();
                    String jsonString;
                    for (int i = 0; i < stage.result.size(); i++) {
                        Stageresult stagedetail = items.get(i);
                        jobj = new JSONObject();
                        jobj.put("carId", i);
                        if (stage.result.get(i).stage.equals("")) {
                            jobj.put(Constant.EXTRA_KEY_STAGE, Constant.TAG_NO_LOCATION);
                        } else {
                            jobj.put(Constant.EXTRA_KEY_STAGE, stage.result.get(i).stage);
                        }

                        jobj.put("totalcar", stage.result.get(i).totalcar);
                        jobj.put("totalcarinstage", stage.result.get(i).totalcarinstage + " | " + stagedetail.totalcarInstagePercentage + "%");
                        jobj.put("days0to7", stage.result.get(i).days0to7 + " | " + stagedetail.days0to7Percentage + "%");
                        jobj.put("days16to30", stage.result.get(i).days16to30 + " | " + stagedetail.days16to30Percentage + "%");
                        jobj.put("days30more", stage.result.get(i).days30more + " | " + stagedetail.days30morePercentage + "%");
                        jobj.put("days8to15", stage.result.get(i).days8to15 + " | " + stagedetail.days8to15Percentage + "%");

                        jarray.put(jobj);
                    }

                    jsonString = jarray.toString();
                    params.put("json", jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>
    {
        LayoutInflater inflater;
        View.OnClickListener onclick0to7 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendIntent(items.get(recyclerView.getChildAdapterPosition((View) v.getTag())).stage, "0");
            }
        };

        View.OnClickListener onclick8to15 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendIntent(items.get(recyclerView.getChildAdapterPosition((View) v.getTag())).stage, "1");
            }
        };

        View.OnClickListener onclick16to30 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendIntent(items.get(recyclerView.getChildAdapterPosition((View) v.getTag())).stage, "2");
            }
        };

        View.OnClickListener onclick30more = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sendIntent(items.get(recyclerView.getChildAdapterPosition((View) v.getTag())).stage, "3");
            }
        };

        public CustomAdapter(ArrayList<Stageresult> result) {
            items = result;
            inflater = LayoutInflater.from(StageActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.activity_row_stage_detail, viewGroup, false);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            Stageresult stagedetail = items.get(position);

            if (stagedetail.stage.equalsIgnoreCase("")) {
                viewHolder.txtstage.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.txtstage.setText(stagedetail.stage);
            }

            if (stagedetail.totalcar.equalsIgnoreCase("")) {
                tb_totalCar.setText(Constant.TAG_NO_LOCATION);
            } else {
                tb_totalCar.setText(stagedetail.totalcar);
            }

            if (stagedetail.totalcarinstage.equalsIgnoreCase("")) {
                viewHolder.txttotalcarinstage.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.txttotalcarinstagepers.setText(stagedetail.totalcarInstagePercentage + "%");
                viewHolder.txttotalcarinstage.setText(stagedetail.totalcarinstage);
            }

            if (stagedetail.days0to7.equalsIgnoreCase("")) {
                viewHolder.days0to7.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.days0to7.setText(stagedetail.days0to7);
                viewHolder.days0to7pers.setText(stagedetail.days0to7Percentage + "%");
            }

            if (stagedetail.days8to15.equalsIgnoreCase("")) {
                viewHolder.days8to15.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.days8to15pers.setText(stagedetail.days8to15Percentage + "%");
                viewHolder.days8to15.setText(stagedetail.days8to15);
            }

            if (stagedetail.days16to30.equalsIgnoreCase("")) {
                viewHolder.days16to30.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.days16to30pers.setText(stagedetail.days16to30Percentage + "%");
                viewHolder.days16to30.setText(stagedetail.days16to30);
            }

            if (stagedetail.days30more.equalsIgnoreCase("")) {
                viewHolder.days30more.setText(Constant.TAG_NO_LOCATION);
            } else {
                viewHolder.days30morepers.setText(stagedetail.days30morePercentage + "%");
                viewHolder.days30more.setText(stagedetail.days30more);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtstage, txttotalcarinstage, days0to7, days8to15, days16to30, days30more;
            TextView txttotalcarinstagepers, days0to7pers, days8to15pers, days16to30pers, days30morepers;
            ImageView iv_0to7, iv_8to15, iv_16to30, iv_30plus;

            public ViewHolder(View v) {
                super(v);
                txtstage = (TextView) v.findViewById(R.id.textstage);
                txttotalcarinstage = (TextView) v.findViewById(R.id.totalcarinstage);
                days0to7 = (TextView) v.findViewById(R.id.days0to7);
                days8to15 = (TextView) v.findViewById(R.id.days8to15);
                days16to30 = (TextView) v.findViewById(R.id.days16to30);
                days30more = (TextView) v.findViewById(R.id.days30more);

                iv_0to7 = (ImageView) v.findViewById(R.id.iv_0to7);
                iv_8to15 = (ImageView) v.findViewById(R.id.iv_8to15);
                iv_16to30 = (ImageView) v.findViewById(R.id.iv_16to30);
                iv_30plus = (ImageView) v.findViewById(R.id.iv_30plus);

                txttotalcarinstagepers = (TextView) v.findViewById(R.id.totalcarinstagepers);
                days0to7pers = (TextView) v.findViewById(R.id.days0to7pers);
                days8to15pers = (TextView) v.findViewById(R.id.days8to15pers);
                days16to30pers = (TextView) v.findViewById(R.id.days16to30pers);
                days30morepers = (TextView) v.findViewById(R.id.days30morepers);

                iv_0to7.setTag(v);
                iv_8to15.setTag(v);
                iv_16to30.setTag(v);
                iv_30plus.setTag(v);

                iv_0to7.setOnClickListener(onclick0to7);
                iv_8to15.setOnClickListener(onclick8to15);
                iv_16to30.setOnClickListener(onclick16to30);
                iv_30plus.setOnClickListener(onclick30more);
            }
        }
    }
}
