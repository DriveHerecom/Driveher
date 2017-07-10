package com.yukti.driveherenew;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.yukti.dataone.model.AuctionNameDetail;
import com.yukti.dataone.model.AuctionNameResult;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionActivity extends AppCompatActivity implements MessageDialogFragment.MessageDialogListener {
    private static final String TAG = "CustomAdapter";
    CustomAdapter adapter;
    RecyclerView recyclerView;
    AuctionNameResult orm;
    LinearLayout ll_auctionprogress;
    Toolbar toolbar;
    TextView txt_noData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        toolbar = (Toolbar) findViewById(R.id.activity_auction_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Auction Names");

        txt_noData = (TextView) findViewById(R.id.tv_no_data);
        recyclerView = (RecyclerView) findViewById(R.id.rv_auctionnames);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(AuctionActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ll_auctionprogress = (LinearLayout) findViewById(R.id.ll_progressAuctionNames);

        if (savedInstanceState != null) {
            orm = savedInstanceState.getParcelable("data");
            setAdapter(orm.carList);
        } else
            getReportDataUsingVolley();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("OnSaveInstanceState", "OnSaveInstanceState");
        outState.putParcelable("data", orm);
    }

    void setAdapter(ArrayList<AuctionNameDetail> auctoinNamedetail) {
        if (adapter == null) {
            adapter = new CustomAdapter(auctoinNamedetail);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d("Adapter", " All Ready set");
        }
    }

    void getReportDataUsingVolley() {
        ll_auctionprogress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_AUCTION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Resposne " , " Response " + response );
                        ll_auctionprogress.setVisibility(View.GONE);

                        try {
                            orm = AppSingleTon.APP_JSON_PARSER.auctiondata(response);

                            if (orm.status_code.equals("1")) {
                                setAdapter(orm.carList);
                            }
                            else if (orm.status_code.equals("4")) {
                                Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(AuctionActivity.this);
                            }
                            else{
                                txt_noData.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                txt_noData.setText(orm.message);
                                Toast.makeText(AuctionActivity.this, "" + orm.message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(AuctionActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        getReportDataUsingVolley();
                                    else
                                        Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_auctionprogress.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                return params;
            }
        };;
        stringRequest.setTag(Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancleRequest();
        ll_auctionprogress.setVisibility(View.GONE);
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Log.e("On Stop", "Cancle request");
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    void sendIntent(String lotcode, String auctionName) {
        Intent i = new Intent(AuctionActivity.this, AuctionLotAvailableDetailsActivity.class);
        i.putExtra(AuctionLotAvailableDetailsActivity.EXTRA_KEY_LOTCODE, lotcode);
        i.putExtra(AuctionLotAvailableDetailsActivity.EXTRA_KEY_AUCTIONNAME, auctionName);
        startActivity(i);
    }

    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            View.OnClickListener {

        public List<AuctionNameDetail> items;

        LayoutInflater inflater;

        public CustomAdapter(ArrayList<AuctionNameDetail> auctoinNamedetail) {
            this.items = sorttingWithCount(auctoinNamedetail);
            inflater = LayoutInflater.from(AuctionActivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            View v = inflater.inflate(R.layout.row_auctionnames, viewGroup, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            Log.e(TAG, "Element " + position + " set.");

            AuctionNameDetail auctionname = items.get(position);
            byte[] data = Base64.decode(auctionname.auctionName, Base64.DEFAULT);
            try {
                String text = new String(data, "UTF-8");
                viewHolder.txtAuctionName.setText(text.toUpperCase());
            } catch (UnsupportedEncodingException e) {
                viewHolder.txtAuctionName.setText(auctionname.auctionName);
                e.printStackTrace();
            }

            viewHolder.btn_dhc_04.setText(Constant.STR_DHC_04 + " (0)");
            viewHolder.btn_dhp_05.setText(Constant.STR_DHP_05 + " (0)");
            viewHolder.btn_cv_51.setText(Constant.STR_CV_51 + " (0)");
            viewHolder.btn_bs_52.setText(Constant.STR_BS_52 + " (0)");
            viewHolder.btn_l405_53.setText(Constant.STR_405_53 + " (0)");
            viewHolder.btn_unknown.setText(Constant.STR_UNKNOWN + " (0)");
            viewHolder.btn_Int.setText(Constant.STR_INT + " (0)");
            viewHolder.btn_NoLotCode.setText(Constant.STR_NO_LOTCODE + " (0)");

            viewHolder.btn_dhc_04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_DHC_04, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_dhp_05.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_DHP_05, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_cv_51.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_CV_51, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_bs_52.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_BS_52, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_l405_53.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_405_53, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_unknown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_UNKNOWN, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_Int.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_INT, viewHolder.txtAuctionName.getText().toString());
                }
            });

            viewHolder.btn_NoLotCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendIntent(Constant.STR_NO_LOTCODE, viewHolder.txtAuctionName.getText().toString());
                }
            });

            if (items.get(position).result != null) {
                for (int i = 0; i < items.get(position).result.size(); i++) {
                    switch (items.get(position).result.get(i).lotcode) {
                        case "DHC-04":
                            viewHolder.btn_dhc_04.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "DHP-05":
                            viewHolder.btn_dhp_05.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "CV-51":
                            viewHolder.btn_cv_51.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "BS-52":
                            viewHolder.btn_bs_52.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "405-53":
                            viewHolder.btn_l405_53.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "Unknown":
                            viewHolder.btn_unknown.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "Int":
                            viewHolder.btn_Int.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        case "No LotCode":
                            viewHolder.btn_NoLotCode.setText(items.get(position).result.get(i).lotcode + " (" + items.get(position).result.get(i).total + ")");
                            break;
                        default:
                            break;
                    }
                }
            }

            if (!items.get(position).visibilty) {
                viewHolder.iv_plus.setVisibility(View.VISIBLE);
                viewHolder.iv_minus.setVisibility(View.GONE);
                viewHolder.ll_topMain.setBackgroundColor(getResources().getColor(R.color.white));
                viewHolder.rl_mainLotCode.setVisibility(View.GONE);
                viewHolder.txtAuctionName.setTextColor(getResources().getColor(R.color.black));
                viewHolder.txtTotalcar.setTextColor(getResources().getColor(R.color.accentColor));
                viewHolder.txt_hintTotalCar.setTextColor(getResources().getColor(R.color.brightgray));
                items.get(position).visibilty = false;
            } else if (items.get(position).visibilty) {
                viewHolder.iv_plus.setVisibility(View.GONE);
                viewHolder.iv_minus.setVisibility(View.VISIBLE);
                viewHolder.ll_topMain.setBackgroundColor(getResources().getColor(R.color.accentColor));
                viewHolder.rl_mainLotCode.setVisibility(View.VISIBLE);
                viewHolder.txtAuctionName.setTextColor(getResources().getColor(R.color.white));
                viewHolder.txtTotalcar.setTextColor(getResources().getColor(R.color.white));
                viewHolder.txt_hintTotalCar.setTextColor(getResources().getColor(R.color.white));
                items.get(position).visibilty = true;
            }

            viewHolder.ll_topMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!items.get(position).visibilty) {
                        viewHolder.iv_plus.setVisibility(View.GONE);
                        viewHolder.iv_minus.setVisibility(View.VISIBLE);
                        viewHolder.ll_topMain.setBackgroundColor(getResources().getColor(R.color.accentColor));
                        viewHolder.rl_mainLotCode.setVisibility(View.VISIBLE);
                        viewHolder.txtAuctionName.setTextColor(getResources().getColor(R.color.white));
                        viewHolder.txtTotalcar.setTextColor(getResources().getColor(R.color.white));
                        viewHolder.txt_hintTotalCar.setTextColor(getResources().getColor(R.color.white));
                        items.get(position).visibilty = true;
                    } else if (items.get(position).visibilty) {
                        viewHolder.iv_plus.setVisibility(View.VISIBLE);
                        viewHolder.iv_minus.setVisibility(View.GONE);
                        viewHolder.ll_topMain.setBackgroundColor(getResources().getColor(R.color.white));
                        viewHolder.rl_mainLotCode.setVisibility(View.GONE);
                        viewHolder.txtAuctionName.setTextColor(getResources().getColor(R.color.black));
                        viewHolder.txtTotalcar.setTextColor(getResources().getColor(R.color.accentColor));
                        viewHolder.txt_hintTotalCar.setTextColor(getResources().getColor(R.color.brightgray));
                        items.get(position).visibilty = false;
                    }
                }
            });

            viewHolder.txtTotalcar.setText(items.get(position).count + "");
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onClick(View v) {
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtAuctionName, txtTotalcar, txt_hintTotalCar;
            LinearLayout ll_topMain;
            RelativeLayout rl_mainLotCode;
            Button btn_dhc_04, btn_dhp_05, btn_cv_51, btn_bs_52, btn_l405_53, btn_unknown, btn_Int, btn_NoLotCode;

            ImageView iv_plus, iv_minus;

            public ViewHolder(View v) {
                super(v);
                txtAuctionName = (TextView) v.findViewById(R.id.tv_auctionname);
                txtTotalcar = (TextView) v.findViewById(R.id.tv_totalcars);
                txt_hintTotalCar = (TextView) v.findViewById(R.id.txt_hintTotalCar);

                ll_topMain = (LinearLayout) v.findViewById(R.id.ll_top);
                rl_mainLotCode = (RelativeLayout) v.findViewById(R.id.rl_mainLotCode);

                iv_plus = (ImageView) v.findViewById(R.id.iv_plus);
                iv_minus = (ImageView) v.findViewById(R.id.iv_minus);

                btn_dhc_04 = (Button) v.findViewById(R.id.btn_dhc_04);
                btn_dhp_05 = (Button) v.findViewById(R.id.btn_dhp_05);
                btn_cv_51 = (Button) v.findViewById(R.id.btn_cv_51);
                btn_bs_52 = (Button) v.findViewById(R.id.btn_bs_52);
                btn_l405_53 = (Button) v.findViewById(R.id.btn_l405_53);
                btn_unknown = (Button) v.findViewById(R.id.btn_unknown);
                btn_Int = (Button) v.findViewById(R.id.btn_Int);
                btn_NoLotCode = (Button) v.findViewById(R.id.btn_NoLotCode);
            }
        }
    }

    public ArrayList<AuctionNameDetail> sorttingWithCount(ArrayList<AuctionNameDetail> list)
    {
        Collections.sort(list, new Comparator<AuctionNameDetail>(){
            @Override
            public int compare(AuctionNameDetail lhs, AuctionNameDetail rhs) {
                return rhs.count-lhs.count;
            }
        });
        return list;
    }
}