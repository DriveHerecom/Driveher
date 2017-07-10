package com.yukti.driveherenew;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stagewithcaractivity extends BaseActivity {

    public static final int REQUEST_ACTION_SEARCH = 2012;
    TextView txt_noData;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    TextView tv_totalCar;
    TextView tb_tile;
    OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String vin = v.getTag().toString();
            Intent intent = new Intent(Stagewithcaractivity.this, CarDetailsActivity.class);
            int i = 0;
            StringBuilder p = new StringBuilder();
            while (vin.charAt(i) != ',') {
                p = p.append(vin.charAt(i));
                i++;
            }
            intent.putExtra(Constant.EXTRA_KEY_EACH_CAR, v.getTag().toString().substring(i + 1));
            intent.putExtra(Constant.EXTRA_KEY_ITEM_POSITION, p.toString());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stage_with_car);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_stage_with_car_app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.app_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(null);

        tv_totalCar = (TextView) toolbar.findViewById(R.id.tv_totalCar);
        tb_tile = (TextView) toolbar.findViewById(R.id.toolbartext);
        tb_tile.setText("Stage Car List");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCarDetail);
        txt_noData = (TextView) findViewById(R.id.tv_stage_data);

        LinearLayoutManager layoutManager = new LinearLayoutManager(Stagewithcaractivity.this);
        recyclerView.setLayoutManager(layoutManager);
        getStagewithlevelDetailUsingVolley(getIntent().getExtras().getString(Constant.EXTRA_KEY_STAGE),
                getIntent().getExtras().getString(Constant.EXTRA_KEY_LEVEL));
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

    void getStagewithlevelDetailUsingVolley(final String stage, final String level) {
        showUpdateProgressDialog("Get Stage With Car  Data......");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_STAGE_REPORT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Stage car detail ", response);
                dismissProgressDialog();
                try {
                    Stagewithlevel stagelevel = AppSingleTon.APP_JSON_PARSER.stageleveldata(response);
                    if (stagelevel.status_code.equals("1")) {
                        setAdapter(stagelevel.cars);
                    } else if (stagelevel.status_code.equals("4")) {
                        Toast.makeText(Stagewithcaractivity.this, "" + stagelevel.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(Stagewithcaractivity.this);
                    } else if (stagelevel.status_code.equals("2")) {
                        recyclerView.setVisibility(View.GONE);
                        txt_noData.setVisibility(View.VISIBLE);
                        tv_totalCar.setText("0");
                        tb_tile.setText("No Cars ");
                    } else if (stagelevel.status_code.equals("0")) {
                        Toast.makeText(Stagewithcaractivity.this, "Parameter Missing", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(Stagewithcaractivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getStagewithlevelDetailUsingVolley(stage, level);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                        Log.e("Error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                if (stage.equalsIgnoreCase("")) {
                    params.put(Constant.EXTRA_KEY_STAGE, "unknown");
                } else {
                    params.put(Constant.EXTRA_KEY_STAGE, stage);
                }

                params.put(Constant.EXTRA_KEY_LEVEL, level);
                Log.e("Param", params.toString());
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(Constant.KEY_TYPE, Constant.APP_TYPE);
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

    void setAdapter(ArrayList<Stagelevelresultdata> result) {
        if (result != null && result.size() > 0) {
            if (adapter == null) {
                adapter = new CustomAdapter(result, clickListener);
                recyclerView.setAdapter(adapter);
                tv_totalCar.setText(result.size() + "");
            } else {
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            txt_noData.setVisibility(View.VISIBLE);
            tv_totalCar.setText("0");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ACTION_SEARCH)) {

            if (resultCode == RESULT_OK) {
                boolean isUpdate = data.getBooleanExtra(Constant.EXTRA_KEY_IS_UPDATE, false);
                if (isUpdate) {
                    CarInventory carx = (CarInventory) data.getExtras().getSerializable(Constant.EXTRA_KEY_EACH_CAR);
                    int itemPosition = data.getIntExtra(Constant.EXTRA_KEY_ITEM_POSITION, 0);
                    adapter.notifyItemChanged(itemPosition);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    public class CustomAdapter extends
            RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
            OnClickListener {

        public List<Stagelevelresultdata> items;

        LayoutInflater inflater;
        OnClickListener onClickListener;

        public CustomAdapter(ArrayList<Stagelevelresultdata> result, OnClickListener onClickListener) {
            this.items = result;
            this.onClickListener = onClickListener;
            inflater = LayoutInflater.from(Stagewithcaractivity.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.activity_row_stagelevel_car, viewGroup, false);
            v.setOnClickListener(this);
            return new ViewHolder(v);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder viewholder, int position) {

            if (items.get(position).vin != null && items.get(position).vin.equalsIgnoreCase("")) {
                viewholder.txtvin.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.txtvin.setText(items.get(position).vin);
            }

            if (items != null && items.get(position) != null && items.get(position).color != null && items.get(position).color.length() > 2 && items.get(position).color.contains("#")) {
                viewholder.txtcolor.setText("             ");
                viewholder.txtcolor.setBackgroundColor(Color.parseColor(items.get(position).color));
            } else {
                viewholder.txtcolor.setText(Constant.KEY_NOT_AVAILABLE);
                viewholder.txtcolor.setBackgroundColor(getResources().getColor(R.color.white));
            }

            if (items.get(position).model != null && items.get(position).model.equalsIgnoreCase("")) {
                viewholder.txtmodel.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.txtmodel.setText(items.get(position).model);
            }

            if (items.get(position).rfid != null && items.get(position).rfid.equalsIgnoreCase("")) {
                viewholder.txtrfid.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.txtrfid.setText(items.get(position).rfid);
            }

            if (items.get(position).year == null) {
                viewholder.txtyear.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.txtyear.setText(items.get(position).year);
            }

            if (items.get(position).make != null && items.get(position).make.equalsIgnoreCase("")) {
                viewholder.txtmake.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.txtmake.setText(items.get(position).make);
            }

            if (items.get(position).lotCode == null) {
                viewholder.tv_lotcode.setText(Constant.KEY_NOT_AVAILABLE);
            } else {
                viewholder.tv_lotcode.setText(items.get(position).lotCode);
            }

            if (items.get(position).imageCount == null || items.get(position).imageCount.equalsIgnoreCase("")) {
                viewholder.tv_photCount.setText("0");
            } else {
                viewholder.tv_photCount.setText(items.get(position).imageCount);
            }

            if (items.get(position).imagePath != null && items.get(position).imagePath.length() > 0) {
                viewholder.iv_imageView.setVisibility(View.VISIBLE);
                viewholder.iv_imageView.setImageUrl(
                        items.get(position).imagePath, VolleySingleton
                                .getInstance(Stagewithcaractivity.this)
                                .getImageLoader());
            } else {
                viewholder.iv_imageView.setVisibility(View.INVISIBLE);
                viewholder.img_previewLayout.setBackground(getResources().getDrawable(R.drawable.ic_default_car));
            }
        }

        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            Intent intent = new Intent(Stagewithcaractivity.this, CarDetailsActivity.class);
            if (adapter.items.get(itemPosition).vin != null)
                intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, adapter.items.get(itemPosition).vin);
            else
                intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, adapter.items.get(itemPosition).rfid);
            startActivity(intent);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtvin, txtyear, txtcolor, txtmodel, txtrfid, txtmake, tv_lotcode, tv_photCount;
            RelativeLayout relativelayout;
            LinearLayout topcell;
            NetworkImageView iv_imageView;
            FrameLayout img_previewLayout;

            public ViewHolder(View v) {
                super(v);

                iv_imageView = (NetworkImageView) v.findViewById(R.id.img);
                iv_imageView.setDefaultImageResId(R.drawable.ic_default_car);
                img_previewLayout = (FrameLayout) v.findViewById(R.id.img_preview);
                tv_lotcode = (TextView) v.findViewById(R.id.tv_lotcode);
                tv_photCount = (TextView) v.findViewById(R.id.totalPhoto);
                topcell = (LinearLayout) v.findViewById(R.id.topCell);
                relativelayout = (RelativeLayout) v.findViewById(R.id.search_row_parent);
                txtvin = (TextView) v.findViewById(R.id.tv_vin);
                txtyear = (TextView) v.findViewById(R.id.tv_year);
                txtcolor = (TextView) v.findViewById(R.id.tv_color);
                txtmodel = (TextView) v.findViewById(R.id.tv_modelyear);
                txtrfid = (TextView) v.findViewById(R.id.tv_rfid);
                txtmake = (TextView) v.findViewById(R.id.tv_make);
            }
        }
    }
}
