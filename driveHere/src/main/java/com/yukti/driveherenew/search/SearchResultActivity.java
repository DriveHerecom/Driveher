package com.yukti.driveherenew.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.yukti.driveherenew.BaseActivity;

import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.VolleySingleton;

public class SearchResultActivity extends BaseActivity {

    TextView errorText;
    LinearLayout loadMoreLayout;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
//    ProgressBar progressBar;

    LinearLayout ll_searchProgress;
    int totalSearchItemFound = 0;
    int page = 0;
    boolean isSearching = false;

    public static int threshhold = 3;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    ArrayList<CarInventory> carList = null;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    protected Handler handler;
    int totalPages, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_search_result_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        progressBar = (ProgressBar) findViewById(R.carId.progressBar);
        ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
        loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
        errorText = (TextView) findViewById(R.id.errorText);
        initRecyclerView();
//        search(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber);

        seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber);
        recyclerView.setHasFixedSize(true);
    }

    void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if ((totalItemCount + 10 <= totalSearchItemFound)
                        && (lastVisibleItem >= totalItemCount - 4)
                        && (isSearching == false)) {
                    loadMoreLayout.setVisibility(View.VISIBLE);
                    currentPage = 1;
                    seachUsingVolley(com.yukti.utils.Constants.limit, currentPage);
                }
            }
        });
    }

    void showHideRecyclerView(boolean flag) {
        if (flag)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    public void addNewData(ArrayList<CarInventory> carInventories) {

        Log.e("Handler call", "Handler calles");
        carList.remove(carList.size() - 1);
        adapter.notifyItemRemoved(carList.size());
        for (int i = 0; i < carInventories.size(); i++) {
            carList.add(carInventories.get(i));
            adapter.notifyItemInserted(carList.size());
        }
        adapter.setLoaded();
    }

    void setAdapter() {
        if (adapter == null) {
            Log.e("setadapter", "in if" + carList.size());
            adapter = new CustomAdapter(carList);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    if (currentPage < totalPages) {
                        carList.add(null);
                        adapter.notifyItemInserted(carList.size() - 1);
                        currentPage++;
//                        search(com.yukti.utils.Constants.limit, currentPage);
                        seachUsingVolley(com.yukti.utils.Constants.limit, currentPage);
                    }
                }
            });

        } else {
            Log.e("setadapter", "in else");
        }
    }

    void seachUsingVolley(final int limit, final int pageNumber)
    {
        isSearching = true;
        ll_searchProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_SEARCH_VEHICLE_NEW,
                new Response.Listener<String>()
                {
            @Override
            public void onResponse(String response)
            {
                isSearching = false;
                ll_searchProgress.setVisibility(View.GONE);

                Search search = AppSingleTon.APP_JSON_PARSER.search(response);
                if (search.count.equals("0"))
                {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Found Nothing!");
                    return;
                }
                if (search.result != null && search.result.size() > 0)
                {
                    Log.e("in Success", "search.totalrecord :: " + search.totalrecord);
                    if (carList != null && carList.size() > 0)
                    {
                        Log.e("in Success", "on load more with result");
                        addNewData(search.result);
                    } else {
                        Log.e("in Success", "on load first with result");
                        currentPage = 1;
                        carList = search.result;
                        setAdapter();
                        getSupportActionBar().setTitle(
                                "Found " + search.totalrecord + " items");

                        showHideRecyclerView(true);
                        totalPages = (search.totalrecord / com.yukti.utils.Constants.limit);
                        if (totalPages > 0 && (search.totalrecord % com.yukti.utils.Constants.limit) > 0)
                            totalPages++;
                    }
                } else {
                    Log.e("Error", "in else with no data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isSearching = false;
                ll_searchProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                CarInventory search_query = (CarInventory) getIntent().getExtras().getSerializable("search_query");
                params.put("Vin", search_query.Vin);
                params.put("Rfid", search_query.Rfid);
                params.put("Make", search_query.Make);
                params.put("Model", search_query.Model);
                params.put("Miles", search_query.Miles);
                params.put("ModelYear", search_query.ModelYear);
                params.put("VehicleStatus", search_query.VehicleStatus);
                params.put("Stage", search_query.Stage);
                params.put("ServiceStage", search_query.ServiceStage);
                params.put("FuelType", search_query.FuelType);
                params.put("Color", search_query.Color);
                params.put("MinSalesPrice", search_query.MinSalesPrice.replace(",", ""));
                params.put("MaxSalesPrice", search_query.MaxSalesPrice.replace(",", ""));
                // Toast.makeText(getApplicationContext(),search_query.MinSalesPrice+"--"+search_query.MaxSalesPrice
                // , Toast.LENGTH_LONG).show();
                params.put("Problem", search_query.Problem);
                params.put("done_date_lotcode",search_query.DonedateLot);
                params.put("Title", search_query.Title);
                params.put("has_location", search_query.has_location);

                // new added field......
                params.put("ModelNumber", search_query.ModelNumber);
                params.put("maxHP", search_query.MaxHP);
                params.put("maxTorque", search_query.MaxTorque);
                params.put("oilCapacity", search_query.OilCapacity);
                params.put("driveType", search_query.DriveType);
                params.put("Company", search_query.Company);
                params.put("LotCode", search_query.LotCode);
                params.put("StockNumber", search_query.StockNumber);
                params.put("purchasedfrom", search_query.PurchasedFrom);

                params.put("cylinders", search_query.Cylinders);
                params.put("gas_tank", search_query.Gastank);

                params.put("vehicleType", search_query.VehicleType);
                params.put("note", search_query.Note);
                params.put("note_date", search_query.NoteDate);
                params.put("AuctionName", search_query.auctionname);
                params.put("done_date", search_query.DoneDate);
                params.put("gps_installed", search_query.Gps_Installed);
                params.put("Page", ++page + "");
                params.put("auction_date", search_query.auctiondate);
                params.put("carready", search_query.carready);
                params.put("caratauction", search_query.caratauction);
                params.put("mechanic", search_query.mechanic);

                params.put("vacancy", search_query.vacancy);
                params.put("hasrfid", search_query.HasRfid);

                params.put("limit", limit + "");
                params.put("page", pageNumber + "");

                Log.e("limit", limit + "");
                Log.e("page", pageNumber + "");

                Log.e("car ready111", ""+search_query.carready);
                Log.e("Service Stage111", ""+search_query.ServiceStage);
                Log.e("car At Auction111", ""+search_query.caratauction);
                return params;
            }
        };
//        requestQueue.add(stringRequest);
        stringRequest.setTag(com.yukti.utils.Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                com.yukti.utils.Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_result, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_searchProgress.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Log.e("On Stop", "Cancle request");
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(com.yukti.utils.Constants.REQUEST_TAG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_ACTION_SEARCH)) {

            if (resultCode == RESULT_OK) {
                // car = (CarInventory)
                // data.getExtras().getSerializable("each_car");
                /*
                 * Toast.makeText(getApplicationContext(), "isupdate",
				 * Toast.LENGTH_SHORT).show();
				 */

                boolean isUpdate = data.getBooleanExtra("isUpdate", false);

                if (isUpdate) {
                    CarInventory carx = (CarInventory) data.getExtras()
                            .getSerializable("each_car");
                    int itemPosition = data.getIntExtra("itemPosition", 0);

                    carList.remove(itemPosition);
                    carList.add(itemPosition, carx);
                    /*
                     * adapter.items.get(itemPosition).Make = carx.Make;
					 * adapter.items.get(itemPosition).Model = carx.Model;
					 * adapter.items.get(itemPosition).ModelNumber =
					 * carx.ModelNumber;
					 * adapter.items.get(itemPosition).SalesPrice =
					 * carx.SalesPrice; adapter.items.get(itemPosition).Stage =
					 * carx.Stage; adapter.items.get(itemPosition).Miles =
					 * carx.Miles; adapter.items.get(itemPosition).Problem =
					 * carx.Problem; adapter.items.get(itemPosition).Note =
					 * carx.Note; adapter.items.get(itemPosition).Title =
					 * carx.Title;
					 */

                    adapter.notifyItemChanged(itemPosition);

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*
                 * Toast.makeText(getApplicationContext(), "isupdate canceled",
				 * Toast.LENGTH_SHORT).show();
				 */
            }

        }

    }

    public static final int REQUEST_ACTION_SEARCH = 2012;

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends RecyclerView.Adapter implements OnClickListener {
        private static final String TAG = "CustomAdapter";

        private OnLoadMoreListener onLoadMoreListener;
        public List<CarInventory> items;
        LayoutInflater inflater;

        public CustomAdapter(List<CarInventory> items) {
            this.items = items;
            inflater = LayoutInflater.from(SearchResultActivity.this);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();

                recyclerView
                        .addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView,
                                                   int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                totalItemCount = linearLayoutManager.getItemCount();
                                lastVisibleItem = linearLayoutManager
                                        .findLastVisibleItemPosition();
                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + threshhold)) {
                                    // End has been reached
                                    // Do something


                                    // InitPullToRefresh();
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }
                        });
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            RecyclerView.ViewHolder vh;
            if (viewType == VIEW_ITEM) {
                View v = inflater.inflate(R.layout.row_search_result, viewGroup,
                        false);
                v.setOnClickListener(this);
                vh = new ItemViewHolder(v);
               // Log.e("ON Bind ViewHolder", "ON Bind ViewHolder");
            } else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.progress_layout, viewGroup, false);
                vh = new ProgressViewHolder(v);
                Log.e("ON Bind Progressbar", "ON Bind Progrssbar");
            }
            return vh;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder_main, final int position) {
            if (viewHolder_main instanceof CustomAdapter.ItemViewHolder) {
                SearchResultActivity.CustomAdapter.ItemViewHolder viewHolder = (SearchResultActivity.CustomAdapter.ItemViewHolder) viewHolder_main;
                final CarInventory car = items.get(position);
                if (car.photos != null && car.photos.size() > 0) {

                    viewHolder.networkImageView.setVisibility(View.VISIBLE);
                    viewHolder.networkImageView.setImageUrl(
                            AppSingleTon.APP_URL.URL_PHOTO_BASE
                                    + car.photos.get(0).ImagePath, VolleySingleton
                                    .getInstance(SearchResultActivity.this)
                                    .getImageLoader());

                } else {
                    viewHolder.networkImageView.setVisibility(View.INVISIBLE);
                    viewHolder.img_previewLayout.setBackground(getResources().getDrawable(R.drawable.ic_default_car));
                }

                if (car.Vin.length() == 17) {
                    viewHolder.vin_last_eight.setText(".." + car.Vin.substring(9, car.Vin.length()));
                } else {
                    viewHolder.vin_last_eight.setText("N/A");
                }

                if (car.Rfid != null && car.Rfid.length() > 3) {
                    viewHolder.tv_rfid.setText(car.Rfid);
                } else {
                    viewHolder.tv_rfid.setText("N/A");
                }

                if (car.Stage != null && car.Stage.length() > 0) {
                    viewHolder.tv_carStage.setText(car.Stage);
                } else {
                    viewHolder.tv_carStage.setText("N/A");
                }

                viewHolder.title.setText(car.ModelYear + " " + car.Make + " " + car.Model + " " + car.ModelNumber);

                if (car.SalesPrice != null && car.SalesPrice.length() > 0) {
                    viewHolder.subtitle.setText(car.SalesPrice + " $");
                } else {
                    viewHolder.subtitle.setText("N/A");
                }

                viewHolder.oneOne.setText(car.Stage);

                if (car.Miles != null && car.Miles.length() >=1) {
                    String milesSubstr = car.Miles.substring(car.Miles.length() - 3);
                    if (milesSubstr.equalsIgnoreCase(".00")) {
                        viewHolder.oneTwo.setText(car.Miles.substring(0,
                                car.Miles.length() - 3));
                    } else {
                        viewHolder.oneTwo.setText(car.Miles);
                    }
                } else {
                    viewHolder.oneTwo.setText(car.Miles);
                }

                viewHolder.twoOne.setText(car.Problem);

                if (car.Note != null && car.Note.length() > 0) {
                    viewHolder.twoTwo.setText(car.Note);
                } else {
                    viewHolder.twoTwo.setText("N/A");
                }

                viewHolder.iv_editCar.setVisibility(View.GONE);/*.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchResultActivity.this, com.yukti.driveherenew.EditCarActivity.class);
                        intent.putExtra("each_car", car);
                        intent.putExtra("itemPosition", position);
                        startActivityForResult(intent, REQUEST_ACTION_EDIT);
                    }
                });*/


                viewHolder.totalPhoto.setText(car.photos.size() + "");

                if (car.Title != null && car.Title.length() > 0) {
                    if (car.Title.equalsIgnoreCase("yes")) {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#338033"));
                    } else {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#000000"));
                    }
                }

                if (car.LotCode.equalsIgnoreCase("")) {
                    viewHolder.ll_lotcode_container.setVisibility(View.GONE);
                } else {
                    viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
                    viewHolder.tv_lotcode.setText(car.LotCode);
                }
            } else {
                ((CustomAdapter.ProgressViewHolder) viewHolder_main).progressBar.setIndeterminate(true);
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
                // Log.e("rrrrrrrrrrrrrrrrrrrrrr",
                // elements.get(i).mechanics+"");
                int position = items.indexOf(elements.get(i));
                notifyItemInserted(position);
            }
        }

        public void remove(CarInventory item)
        {
            int position = items.indexOf(item);
            items.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onClick(View v)
        {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            // showToast("clicked "+itemPosition);
            Log.e("mechanic at result", adapter.items.get(itemPosition).mechanic + "");
            Log.e("vin at result", adapter.items.get(itemPosition).Vin + "");
            Log.e("ServiceStage at result", ""+adapter.items.get(itemPosition).ServiceStage + "");
            Log.e("carAtAuction at result", ""+adapter.items.get(itemPosition).caratauction + "");
            Intent intent = new Intent(SearchResultActivity.this,
                    CarDetailsActivity.class);
            intent.putExtra("each_car", adapter.items.get(itemPosition));
            intent.putExtra("itemPosition", itemPosition);
            startActivityForResult(intent, REQUEST_ACTION_SEARCH);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            String hasTitle;
            NetworkImageView networkImageView;
            RelativeLayout parentLayout;
            LinearLayout ll_has_title;
            FrameLayout img_previewLayout;
            ImageView iv_editCar;
            TextView title, subtitle, oneOne, oneTwo, twoOne, twoTwo,
                    totalPhoto, vin_last_eight, tv_rfid, tv_carStage;

            TextView tv_lotcode;
            LinearLayout ll_lotcode_container;

            public ItemViewHolder(View v) {
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
                tv_rfid = (TextView) v.findViewById(R.id.tv_rfid);
                tv_carStage = (TextView) v.findViewById(R.id.tv_carStage);
                ll_has_title = (LinearLayout) v.findViewById(R.id.ll_has_title);

                vin_last_eight = (TextView) v.findViewById(R.id.vin_last_eight);

                tv_lotcode = (TextView) v.findViewById(R.id.tv_lotcode);

                iv_editCar = (ImageView) v.findViewById(R.id.iv_editCar);

                ll_lotcode_container = (LinearLayout) v.findViewById(R.id.ll_lotcode_container);
            }

            public NetworkImageView getNetworkImageView() {
                return networkImageView;
            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder
        {
            public ProgressBar progressBar;

            public ProgressViewHolder(View v)
            {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            }
        }
    }
}
