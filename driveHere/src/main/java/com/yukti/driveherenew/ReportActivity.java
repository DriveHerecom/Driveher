package com.yukti.driveherenew;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yukti.dataone.model.ReportDetail;
import com.yukti.dataone.model.ReportResult;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends BaseActivity implements MessageDialogListener {
    private static final String TAG = "CustomAdapter";
    public static ArrayList<CarInventory> carList = null;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    ReportResult orm;
    LinearLayout ll_reportprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Log.e("OnCreate", "OnCreate");
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_report_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ll_reportprogress = (LinearLayout) findViewById(R.id.ll_progressReport);

        if (savedInstanceState != null) {
            orm = savedInstanceState.getParcelable("data");
            setAdapter(orm.result);
        } else
            getReportDataUsingVolley();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("OnSaveInstanceState", "OnSaveInstanceState");
        outState.putParcelable("data", orm);
    }

    void setAdapter(ArrayList<ReportDetail> reportdetail) {
        if (adapter == null) {
            adapter = new CustomAdapter(reportdetail);
            recyclerView.setAdapter(adapter);
        } else {
        }
    }

    void getReportDataUsingVolley() {
        ll_reportprogress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_REPORT_MISSING_CAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ll_reportprogress.setVisibility(View.GONE);

                        try {
                            orm = AppSingleTon.APP_JSON_PARSER.reportdata(response);
                            Log.e("Size", orm.status_code + " test");

                            if (orm.status_code.equals("1.")) {
                                for (int i = 0; i < orm.result.size(); i++) {
                                    Log.e("Size", orm.result.get(i).stages.size() + " test");
                                }
                                setAdapter(orm.result);
                            } else if (orm.status_code.equals("2")) {
                                Toast.makeText(ReportActivity.this, "" + orm.msg, Toast.LENGTH_SHORT).show();
                            }
                            else if (orm.status_code.equals("4")) {
                                Toast.makeText(ReportActivity.this, "" + orm.msg, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(ReportActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(ReportActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        getReportDataUsingVolley();
                                    else
                                        Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_reportprogress.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_type, "drivehere");
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_requestType, "1");
                Log.e("Param", params.toString() + " ");
                return params;
            }
        };
        ;
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancleRequest();
        ll_reportprogress.setVisibility(View.GONE);
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }

    void sendIntent(String s, String LotCode, String count) {
        Intent i = new Intent(ReportActivity.this, MissingCarFullDetailActivity.class);
        i.putExtra(MissingCarFullDetailActivity.EXTRA_KEY_STAGE, s);
        if (LotCode.equalsIgnoreCase(""))
            LotCode = "nolocation";
        i.putExtra(MissingCarFullDetailActivity.EXTRA_KEY_LOTCODE, LotCode);
        i.putExtra(MissingCarFullDetailActivity.EXTRA_COUNT, count);
        startActivity(i);
    }

    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            OnClickListener {

        public List<ReportDetail> items;
        LayoutInflater inflater;

        public CustomAdapter(ArrayList<ReportDetail> reportdetail) {
            this.items = reportdetail;
            inflater = LayoutInflater.from(ReportActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = inflater.inflate(R.layout.row_reportdetail, viewGroup, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            Log.e(TAG, "Element " + position + " set.");

            final ReportDetail lotcode = items.get(position);

            if (lotcode.lotcode.equalsIgnoreCase("")) {

                viewHolder.txtlotcode.setText("No Location");

            } else {
                viewHolder.txtlotcode.setText(lotcode.lotcode);
            }


            viewHolder.search_row_parent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Clicked", "Clicked");
                    if (viewHolder.iv_plus.getVisibility() == View.VISIBLE) {
                        viewHolder.rl_mainStage.setVisibility(View.VISIBLE);
                        viewHolder.iv_minus.setVisibility(View.VISIBLE);
                        viewHolder.iv_plus.setVisibility(View.GONE);
                        viewHolder.search_row_parent.setBackgroundColor(getResources().getColor(R.color.accentColor));
                        viewHolder.txtlotcode.setTextColor(getResources().getColor(R.color.white));
                        viewHolder.txtmissingcar.setTextColor(getResources().getColor(R.color.white));
                        viewHolder.txt_totalCar.setTextColor(getResources().getColor(R.color.lightgray));
                    } else if (viewHolder.iv_plus.getVisibility() == View.GONE) {
                        viewHolder.rl_mainStage.setVisibility(View.GONE);
                        viewHolder.iv_minus.setVisibility(View.GONE);
                        viewHolder.iv_plus.setVisibility(View.VISIBLE);
                        viewHolder.search_row_parent.setBackgroundColor(getResources().getColor(R.color.white));
                        viewHolder.txtlotcode.setTextColor(getResources().getColor(R.color.black));
                        viewHolder.txtmissingcar.setTextColor(getResources().getColor(R.color.accentColor));
                        viewHolder.txt_totalCar.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });


            viewHolder.iv_plus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.rl_mainStage.setVisibility(View.VISIBLE);
                    viewHolder.iv_minus.setVisibility(View.VISIBLE);
                    viewHolder.iv_plus.setVisibility(View.GONE);
                    viewHolder.search_row_parent.setBackgroundColor(getResources().getColor(R.color.accentColor));
                    viewHolder.txtlotcode.setTextColor(getResources().getColor(R.color.white));
                    viewHolder.txtmissingcar.setTextColor(getResources().getColor(R.color.white));
                    viewHolder.txt_totalCar.setTextColor(getResources().getColor(R.color.lightgray));
                }
            });

            viewHolder.iv_minus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.rl_mainStage.setVisibility(View.GONE);
                    viewHolder.iv_minus.setVisibility(View.GONE);
                    viewHolder.iv_plus.setVisibility(View.VISIBLE);
                    viewHolder.search_row_parent.setBackgroundColor(getResources().getColor(R.color.white));
                    viewHolder.txtlotcode.setTextColor(getResources().getColor(R.color.black));
                    viewHolder.txtmissingcar.setTextColor(getResources().getColor(R.color.accentColor));
                    viewHolder.txt_totalCar.setTextColor(getResources().getColor(R.color.gray));
                }
            });

            if (items != null && items.get(position).stages != null) {
                viewHolder.tv_all.setText("All" + " (" + items.get(position).total + ")");

                for (int i = 0; i < items.get(position).stages.size(); i++) {
                    switch (items.get(position).stages.get(i).getStageName()) {

                        case "MC":
                            viewHolder.tv_mc.setText("MC" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "unknown":
                            viewHolder.tv_unknown.setText("Unknown" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "DONE":
                            viewHolder.tv_done.setText("Done" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "Ready":
                            viewHolder.tv_ready.setText("Ready" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "AUCTION":
                            viewHolder.tv_auction.setText("Auction" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "SOLD":
                            viewHolder.tv_sold.setText("Sold" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "PARTS":
                            viewHolder.tv_parts.setText("Parts" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "LOANER_OUT":
                            viewHolder.tv_loanerOut.setText("Loaner out" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "P/H-Customer":
                            viewHolder.tv_phCustomer.setText("P/H Customer" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "LOANER_SERVICE":
                            viewHolder.tv_loanerService.setText("Loaner ser" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "W/P-Customer":
                            viewHolder.tv_wpCustomer.setText("W/P Customer" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "JUNK":
                            viewHolder.tv_junk.setText("Junk" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "W/P-118":
                            viewHolder.tv_wp118.setText("W/P 118" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "LOANER_MISSING":
                            viewHolder.tv_loanerMissing.setText("Loaner Miss" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "W/P-CB":
                            viewHolder.tv_wpCB.setText("W/P CB" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "LOANER_IN":
                            viewHolder.tv_loanerIn.setText("Loaner In" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "Detailing":
                            viewHolder.tv_detailing.setText("Detailing" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "118":
                            viewHolder.tv_118.setText("118" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "MISSING":
                            viewHolder.tv_missing.setText("Missing" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "AVAILABLE":
                            viewHolder.tv_available.setText("Available" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "Hold Repo":
                            viewHolder.tv_holdRepo.setText("Hold Repo" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "P/H-118":
                            viewHolder.tv_ph118.setText("P/H 118" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "CB":
                            viewHolder.tv_cb.setText("CB" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "QC":
                            viewHolder.tv_qc.setText("QC" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "P/H-CB":
                            viewHolder.tv_phCB.setText("P/H CB" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "Hold PPA":
                            viewHolder.tv_holdPPA.setText("Hold PPA" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;

                        case "40 Miles":
                            viewHolder.tv_40Miles.setText("40 Miles" + " (" + items.get(position).stages.get(i).getTotal() + ")");
                            break;
                    }
                }
            }

            viewHolder.tv_all.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent("All", items.get(position).lotcode, items.get(position).stages.size() + "");
                }
            });

            viewHolder.tv_mc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("MC")) {
                            sendIntent("MC", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_wp118.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("W/P-118")) {
                            sendIntent("W/P-118", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_wpCB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("W/P-CB",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("W/P-CB")) {
                            sendIntent("W/P-CB", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_wpCustomer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("W/P-Customer",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("W/P-Customer")) {
                            sendIntent("W/P-Customer", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_ph118.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("P/H-118",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("P/H-118")) {
                            sendIntent("P/H-118", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_phCB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("P/H-CB",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("P/H-CB")) {
                            sendIntent("P/H-CB", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_phCustomer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("P/H-Customer",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("P/H-Customer")) {
                            sendIntent("P/H-Customer", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_118.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("118",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("118")) {
                            sendIntent("118", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_qc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("QC",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("QC")) {
                            sendIntent("QC", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_cb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("CB",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("CB")) {
                            sendIntent("CB", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_40Miles.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("40 Miles",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("40 Miles")) {
                            sendIntent("40 Miles", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_detailing.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("Detailing",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("Detailing")) {
                            sendIntent("Detailing", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_ready.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("Ready",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("Ready")) {
                            sendIntent("Ready", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_holdPPA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("Hold PPA",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("Hold PPA")) {
                            sendIntent("Hold PPA", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_holdRepo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("Hold Repo",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("Hold Repo")) {
                            sendIntent("Hold Repo", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_unknown.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("UNKNOWN",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("unknown")) {
                            sendIntent("unknown", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_junk.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("JUNK",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("JUNK")) {
                            sendIntent("JUNK", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_auction.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("AUCTION",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("AUCTION")) {
                            sendIntent("AUCTION", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_done.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("DONE",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("DONE")) {
                            sendIntent("DONE", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });
            viewHolder.tv_sold.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("SOLD",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("SOLD")) {
                            sendIntent("SOLD", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_parts.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("PARTS",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("PARTS")) {
                            sendIntent("PARTS", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_loanerOut.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("LOANER_OUT",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("LOANER_OUT")) {
                            sendIntent("LOANER_OUT", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_loanerService.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("LOANER_SERVICE",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("LOANER_SERVICE")) {
                            sendIntent("LOANER_SERVICE", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_loanerMissing.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("LOANER_MISSING",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("LOANER_MISSING")) {
                            sendIntent("LOANER_MISSING", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_loanerIn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("LOANER_IN",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("LOANER_IN")) {
                            sendIntent("LOANER_IN", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_missing.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("MISSING",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("MISSING")) {
                            sendIntent("MISSING", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.tv_available.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendIntent("AVAILABLE",items.get(position).lotcode);
                    for (int i = 0; i < items.get(position).stages.size(); i++) {
                        if (items.get(position).stages.get(i).getStageName().equalsIgnoreCase("AVAILABLE")) {
                            sendIntent("AVAILABLE", items.get(position).lotcode, items.get(position).stages.get(i).getTotal() + "");
                        }
                    }
                }
            });

            viewHolder.txtmissingcar.setText(lotcode.total);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onClick(View v) {
         /* int itemPosition = recyclerView.getChildAdapterPosition(v);
            Intent intent = new Intent(ReportActivity.this, MissCarSearchActivity.class);
            intent.putExtra("lotcode", "" + items.get(itemPosition).lotcode);
            Log.e("selected_lotcode", "" + items.get(itemPosition).lotcode);
            startActivity(intent);*/
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtlotcode, txtmissingcar, txt_totalCar;

            LinearLayout search_row_parent, ll_view;

            RelativeLayout rl_mainStage;
            ImageView iv_plus, iv_minus;

            TextView tv_all, tv_mc, tv_wp118, tv_wpCB, tv_wpCustomer, tv_ph118, tv_phCB, tv_phCustomer,
                    tv_118, tv_qc, tv_cb, tv_40Miles, tv_detailing, tv_ready, tv_holdPPA, tv_holdRepo, tv_unknown, tv_junk, tv_auction;

            /////////////////    21 feb 2017
            TextView tv_done, tv_sold, tv_parts, tv_loanerOut, tv_loanerService, tv_loanerMissing, tv_loanerIn, tv_missing, tv_available;

            public ViewHolder(View v) {
                super(v);

                rl_mainStage = (RelativeLayout) v.findViewById(R.id.rl_mainStage);
                search_row_parent = (LinearLayout) v.findViewById(R.id.search_row_parent);
                ll_view = (LinearLayout) v.findViewById(R.id.ll_view);
                iv_plus = (ImageView) v.findViewById(R.id.iv_plus);
                iv_minus = (ImageView) v.findViewById(R.id.iv_minus);
                txtlotcode = (TextView) v.findViewById(R.id.lotecode);
                txt_totalCar = (TextView) v.findViewById(R.id.txt_totalCar);
                txtmissingcar = (TextView) v.findViewById(R.id.totalmissingcar);
                tv_all = (TextView) v.findViewById(R.id.tv_all);
                tv_mc = (TextView) v.findViewById(R.id.tv_mc);
                tv_wp118 = (TextView) v.findViewById(R.id.tv_wp118);
                tv_wpCB = (TextView) v.findViewById(R.id.tv_wpCB);
                tv_wpCustomer = (TextView) v.findViewById(R.id.tv_wpCustomer);
                tv_ph118 = (TextView) v.findViewById(R.id.tv_ph118);
                tv_phCB = (TextView) v.findViewById(R.id.tv_phCB);
                tv_phCustomer = (TextView) v.findViewById(R.id.tv_phCustomer);
                tv_118 = (TextView) v.findViewById(R.id.tv_118);
                tv_qc = (TextView) v.findViewById(R.id.tv_qc);
                tv_cb = (TextView) v.findViewById(R.id.tv_cb);
                tv_40Miles = (TextView) v.findViewById(R.id.tv_40Miles);
                tv_detailing = (TextView) v.findViewById(R.id.tv_detailing);
                tv_ready = (TextView) v.findViewById(R.id.tv_ready);
                tv_holdPPA = (TextView) v.findViewById(R.id.tv_holdPPA);
                tv_holdRepo = (TextView) v.findViewById(R.id.tv_holdRepo);
                tv_unknown = (TextView) v.findViewById(R.id.tv_unknown);
                tv_junk = (TextView) v.findViewById(R.id.tv_junk);
                tv_auction = (TextView) v.findViewById(R.id.tv_auction);

                /////////////////    21 feb 2017
                tv_done = (TextView) v.findViewById(R.id.tv_done);
                tv_sold = (TextView) v.findViewById(R.id.tv_sold);
                tv_parts = (TextView) v.findViewById(R.id.tv_parts);
                tv_loanerOut = (TextView) v.findViewById(R.id.tv_loanerOut);
                tv_loanerService = (TextView) v.findViewById(R.id.tv_loanerService);
                tv_loanerMissing = (TextView) v.findViewById(R.id.tv_loanerMissing);
                tv_loanerIn = (TextView) v.findViewById(R.id.tv_loanerIn);
                tv_missing = (TextView) v.findViewById(R.id.tv_missing);
                tv_available = (TextView) v.findViewById(R.id.tv_available);
            }
        }
    }
}