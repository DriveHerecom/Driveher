package com.yukti.driveherenew;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.yukti.dataone.model.AuctionSearch;
import com.yukti.driveherenew.fragment.AvailableCar;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuctionLotAvailableDetailsActivity extends AppCompatActivity {
    public static String EXTRA_KEY_LOTCODE = "lotcode";
    public static String EXTRA_KEY_AUCTIONNAME = "auctionname";
    public static Toolbar toolbar;
    static public ArrayList<CarInventory> availableCarList;
    static public String lot, auctname;
    LinearLayout ll_progress;
    AvailableCar availableCar;
    private SmartTabLayout tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_lot_available_details);

        toolbar = (Toolbar) findViewById(R.id.activity_auction_lot_available_details_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Auction Lot CarList");

        availableCarList = new ArrayList<CarInventory>();

        ll_progress = (LinearLayout) findViewById(R.id.ll_auction_lot_car_progress);
        tabs = (SmartTabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        lot = getIntent().getStringExtra(EXTRA_KEY_LOTCODE);
        auctname = getIntent().getStringExtra(EXTRA_KEY_AUCTIONNAME);

        getCarData(lot, auctname);
    }
/*

    @Override
    protected void onResume() {
        super.onResume();

    }
*/

    void getCarData(final String lotcode, final String auctionname) {
        ll_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_AUCTION_CAR_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ll_progress.setVisibility(View.GONE);
                        try {
                            AuctionSearch search = AppSingleTon.APP_JSON_PARSER.actoinsearch(response);
                            if (search.status_code.equalsIgnoreCase("1"))
                            {
                                if (search.carDetailCount > 0) {
                                    availableCarList = search.carDetail;
                                }
                                setViewPager();
                            } else if (search.status_code.equals("4")) {
                                Toast.makeText(getApplicationContext(), search.message, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(AuctionLotAvailableDetailsActivity.this);
                            }
                            else
                            {
                                Toast.makeText(AuctionLotAvailableDetailsActivity.this, "" + search.message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(AuctionLotAvailableDetailsActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        getCarData(lotcode,auctionname);
                                    else
                                        Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ll_progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), " " + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_auctionName, auctionname);
                params.put(ParamsKey.KEY_lotCode, lotcode);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
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

    void setViewPager() {
        adapter = new MyPagerAdapter(getSupportFragmentManager(), availableCarList);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"AVAILABLE"};
        ArrayList<CarInventory> availableCarList;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyPagerAdapter(FragmentManager fm, ArrayList<CarInventory> available_car_list) {
            super(fm);
            this.availableCarList = available_car_list;
            this.TITLES[0] = this.TITLES[0] + " (" + available_car_list.size() + ")";
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    availableCar = AvailableCar.newInstance(availableCarList);
                    return availableCar;
                case 1:
                    break;
                default:
                    break;
            }
            return null;
        }
    }
}
