package com.yukti.driveherenew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.yukti.driveherenew.search.OnLoadMoreListener;
import com.yukti.driveherenew.search.Search;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissingCarFullDetailActivity extends BaseActivity {
    public static final int REQUEST_ACTION_SEARCH = 2012;
    public static String EXTRA_KEY_STAGE = "stage";
    public static String EXTRA_KEY_LOTCODE = "lotCode";
    public static String EXTRA_COUNT = "count";
    public static int threshhold = 3;
    public boolean flag = false;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<CarInventory> carList = null;
    TextView txt_noData;
    LinearLayoutManager linearLayoutManager;
    int totalPages, currentPage;
    ProgressDialog progressDialog;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_car_full_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_missing_car_full_detail_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();

        carList = ReportActivity.carList;
        txt_noData = (TextView) findViewById(R.id.tv_no_data);

        seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
    }

    void seachUsingVolley(final int limit, final int pageNumber, final boolean pDialog) {

        if (pDialog) {
            progressDialog = new ProgressDialog(MissingCarFullDetailActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_REPORT_MISSING_CAR_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", " " + response);
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        try {

                            Search search = AppSingleTon.APP_JSON_PARSER.search(response);
                            if (search.status_code.equalsIgnoreCase("1")) {
                                if (search.count.equals("0")) {
                                    recyclerView.setVisibility(View.GONE);
                                    txt_noData.setVisibility(View.VISIBLE);
                                    getSupportActionBar().setTitle("No Cars Found");
                                    return;
                                }

                                if (search.cars != null && search.cars.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if (carList != null && carList.size() > 0) {
                                        addNewData(search.cars);
                                    } else {
                                        currentPage = 1;
                                        carList = search.cars;
                                        setAdapter();

                                        showHideRecyclerView(true);
                                  /*  totalPages = (Integer.parseInt(getIntent().getStringExtra(EXTRA_COUNT))/ com.yukti.utils.Constants.limit);
                                    if (totalPages > 0 && (Integer.parseInt(getIntent().getStringExtra(EXTRA_COUNT)) % com.yukti.utils.Constants.limit) > 0)
                                        totalPages++;*/
                                        getSupportActionBar().setTitle("Cars (" + search.count + ")");
                                        totalPages = (Integer.parseInt(search.count) / com.yukti.utils.Constants.limit);
                                        if (totalPages > 0 && (Integer.parseInt(search.count) % com.yukti.utils.Constants.limit) > 0)
                                            totalPages++;
                                    }
                                } else {
                                }
                            } else if (search.status_code.equalsIgnoreCase("4")) {
                                recyclerView.setVisibility(View.GONE);
                                txt_noData.setVisibility(View.VISIBLE);
                                getSupportActionBar().setTitle("No Cars Found");

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MissingCarFullDetailActivity.this);
                                alertDialogBuilder.setMessage(search.message);
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();
                                            }
                                        });

                                alertDialogBuilder.setTitle("Not authorized");
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } else if (search.status_code.equalsIgnoreCase("2")) {
                                recyclerView.setVisibility(View.GONE);
                                txt_noData.setVisibility(View.VISIBLE);
                                getSupportActionBar().setTitle("No Cars Found");
                            } else if (search.status_code.equalsIgnoreCase("4")) {
                                Toast.makeText(MissingCarFullDetailActivity.this, "" + search.message, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(MissingCarFullDetailActivity.this);
                            }
                            else if (search.status_code.equalsIgnoreCase("0")) {
                                Toast.makeText(MissingCarFullDetailActivity.this, search.message + " ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(MissingCarFullDetailActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        seachUsingVolley(limit,pageNumber,pDialog);
                                    else
                                        Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_lotCode, getIntent().getStringExtra(EXTRA_KEY_LOTCODE));
                params.put(ParamsKey.KEY_stage, getIntent().getStringExtra(EXTRA_KEY_STAGE));
                params.put(ParamsKey.KEY_type, "drivehere");
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_inOnePage, limit + "");
                params.put(ParamsKey.KEY_pageNo, pageNumber + "");
                Log.e("Param", params.toString() + " ");
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

    void showHideRecyclerView(boolean flag) {
        if (flag)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    void setAdapter() {
        if (adapter == null) {
            adapter = new CustomAdapter(carList);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (currentPage < totalPages) {
                        carList.add(null);
                        adapter.notifyItemInserted(carList.size() - 1);
                        currentPage++;
                        seachUsingVolley(com.yukti.utils.Constants.limit, currentPage, false);
                    }
                }
            });
        } else {
        }
    }

    public void addNewData(ArrayList<CarInventory> carInventories) {
        carList.remove(carList.size() - 1);
        adapter.notifyItemRemoved(carList.size());
        for (int i = 0; i < carInventories.size(); i++) {
            carList.add(carInventories.get(i));
            adapter.notifyItemInserted(carList.size());
        }
        adapter.setLoaded();
    }

    void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        linearLayoutManager = new LinearLayoutManager(MissingCarFullDetailActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ACTION_SEARCH)) {
            if (resultCode == RESULT_OK) {
                boolean isUpdate = data.getBooleanExtra("isUpdate", false);
                if (isUpdate) {
                    CarInventory carx = (CarInventory) data.getExtras().getSerializable("each_car");
                    int itemPosition = data.getIntExtra("itemPosition", 0);
                    carList.remove(itemPosition);
                    carList.add(itemPosition, carx);
                    adapter.notifyItemChanged(itemPosition);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
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

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends
            RecyclerView.Adapter implements
            OnClickListener {
        private static final String TAG = "CustomAdapter";
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;
        public List<CarInventory> items;
        LayoutInflater inflater;
        private OnLoadMoreListener onLoadMoreListener;

        public CustomAdapter(List<CarInventory> items) {
            this.items = items;
            inflater = LayoutInflater.from(MissingCarFullDetailActivity.this);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + threshhold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder vh;
            if (viewType == VIEW_ITEM) {
                View v = inflater.inflate(R.layout.row_search_result, viewGroup, false);
                v.setOnClickListener(this);
                vh = new ViewHolder(v);
            } else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_layout, viewGroup, false);
                vh = new ProgressViewHolder(v);
            }
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder1, int position) {
            if (items != null && items.size() > 0 && items.get(position) != null) {
                ViewHolder viewHolder = (ViewHolder) viewHolder1;
                final CarInventory car = items.get(position);
                if (car.imagePath != null && car.imagePath.length() > 0) {
                    viewHolder.networkImageView.setVisibility(View.VISIBLE);
                    viewHolder.networkImageView.setImageUrl(
                            car.imagePath, VolleySingleton
                                    .getInstance(MissingCarFullDetailActivity.this)
                                    .getImageLoader());

                } else {
                    viewHolder.networkImageView.setVisibility(View.INVISIBLE);
                    viewHolder.img_previewLayout.setBackground(getResources().getDrawable(R.drawable.ic_default_car));
                }

                if (car.vin.length() == 17) {
                    viewHolder.vin_last_eight.setText(".." + car.vin.substring(9, car.vin.length()));
                } else {
                    viewHolder.vin_last_eight.setText("N/A");
                }

                if (car.rfid != null && car.rfid.length() > 3) {
                    viewHolder.tv_rfid.setText(car.rfid);
                } else {
                    viewHolder.tv_rfid.setText("N/A");
                }

                if (car.stage != null && car.stage.length() > 0) {
                    viewHolder.tv_carStage.setText(car.stage);
                } else {
                    viewHolder.tv_carStage.setText("N/A");
                }

                viewHolder.title.setText((car.modelYear + " " + car.make + " " + car.model + " " + car.modelNumber).replaceAll("null", ""));

                if (car.price != null && car.price.length() > 0) {
                    viewHolder.subtitle.setText(car.price + " $");
                } else {
                    viewHolder.subtitle.setText("N/A");
                }

                viewHolder.oneOne.setText(car.stage);

                if (car.note != null && car.note.length() > 0) {
                    viewHolder.twoTwo.setText(car.note);
                } else {
                    viewHolder.twoTwo.setText("N/A");
                }

                viewHolder.iv_editCar.setVisibility(View.GONE);

              /*  viewHolder.iv_editCar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, com.yukti.driveherenew.EditCarActivity.class);
                        intent.putExtra(Constant.EXTRA_KEY_EACH_CAR, car);
                        intent.putExtra(Constant.EXTRA_KEY_ITEM_POSITION, position);
                        startActivityForResult(intent, REQUEST_ACTION_EDIT);
                    }
                });*/

                if (car.imageCount != null)
                    viewHolder.totalPhoto.setText(car.imageCount + "");
                else
                    viewHolder.totalPhoto.setText("0");

                if (car.Title != null && car.Title.length() > 0) {
                    if (car.Title.equalsIgnoreCase("yes")) {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#338033"));
                    } else {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#000000"));
                    }
                }

                if (car.lotCode == null || car.lotCode.equalsIgnoreCase("")) {
                    viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
                    viewHolder.tv_lotcode.setText(Constant.KEY_NOT_AVAILABLE);
                } else {
                    viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
                    viewHolder.tv_lotcode.setText(car.lotCode);
                }
            } else {
                ((ProgressViewHolder) viewHolder1).progressBar.setIndeterminate(true);
            }
        }

        public void setLoaded() {
            loading = false;
        }

        @Override
        public int getItemViewType(int position) {
            return carList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void add(CarInventory item, int position) {
            items.add(position, item);
            notifyItemInserted(position);
        }

        public void add(ArrayList<CarInventory> elements) {
            for (int i = 0; i < elements.size(); i++) {
                items.add(elements.get(i));
                int position = items.indexOf(elements.get(i));
                notifyItemInserted(position);
            }
        }

        public void remove(CarInventory item) {
            int position = items.indexOf(item);
            items.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            Intent intent = new Intent(MissingCarFullDetailActivity.this, CarDetailsActivity.class);
            intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, items.get(itemPosition).vin);
            startActivityForResult(intent, REQUEST_ACTION_SEARCH);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            NetworkImageView networkImageView;
            RelativeLayout parentLayout;
            LinearLayout ll_has_title;
            FrameLayout img_previewLayout;
            ImageView iv_editCar;
            TextView title, subtitle, oneOne, oneTwo, twoOne, twoTwo,
                    totalPhoto, vin_last_eight, tv_rfid, tv_carStage;

            TextView tv_lotcode;
            LinearLayout ll_lotcode_container;

            public ViewHolder(View v) {
                super(v);

                parentLayout = (RelativeLayout) v.findViewById(R.id.search_row_parent);
                networkImageView = (NetworkImageView) v.findViewById(R.id.img);
                networkImageView.setDefaultImageResId(R.drawable.ic_default_car);

                img_previewLayout = (FrameLayout) v.findViewById(R.id.img_preview);
                title = (TextView) v.findViewById(R.id.title);
                subtitle = (TextView) v.findViewById(R.id.subtitle);
                oneOne = (TextView) v.findViewById(R.id.oneOneTxt);
                twoOne = (TextView) v.findViewById(R.id.twoOneTxt);
                oneTwo = (TextView) v.findViewById(R.id.onetwoTxt);
                twoTwo = (TextView) v.findViewById(R.id.twoTwoTxt);
                totalPhoto = (TextView) v.findViewById(R.id.totalPhoto);
                ll_has_title = (LinearLayout) v.findViewById(R.id.ll_has_title);
                vin_last_eight = (TextView) v.findViewById(R.id.vin_last_eight);

                tv_carStage = (TextView) v.findViewById(R.id.tv_carStage);
                tv_rfid = (TextView) v.findViewById(R.id.tv_rfid);
                ll_has_title = (LinearLayout) v.findViewById(R.id.ll_has_title);
                iv_editCar = (ImageView) v.findViewById(R.id.iv_editCar);
                tv_lotcode = (TextView) v.findViewById(R.id.tv_lotcode);
                ll_lotcode_container = (LinearLayout) v.findViewById(R.id.ll_lotcode_container);
            }

            public NetworkImageView getNetworkImageView() {
                return networkImageView;
            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public ProgressViewHolder(View v) {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            }
        }
    }
}