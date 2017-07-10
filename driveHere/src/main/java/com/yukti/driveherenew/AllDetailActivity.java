package com.yukti.driveherenew;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yukti.newchanges.fragment.CustomerDetailFragment;
import com.yukti.newchanges.fragment.ReturnCarFragment;
import com.yukti.newchanges.fragment.LoanerCarFragment;
import com.yukti.utils.AllDetail;
import com.yukti.utils.AllDetailModel;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllDetailActivity extends AppCompatActivity {

    public static AllDetailModel detail;
    public static String EXTRA_KEY_CONTRACT_ID = "contract_id";
    String str_contract_id = "";
    private LinearLayout ll_ProgressDialog;
    private ProgressWheel wheel;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_detail);

        wheel = new ProgressWheel(getApplicationContext());
        ll_ProgressDialog = (LinearLayout) findViewById(R.id.ll_activity_all_detail_progress);
        wheel.setVisibility(View.GONE);
        ll_ProgressDialog.setVisibility(View.GONE);

        toolbar = (Toolbar) findViewById(R.id.tb_allDeatailActivity);
        setSupportActionBar(toolbar);
        TextView tb_title = (TextView) toolbar.findViewById(R.id.tv_fragName);
        tb_title.setText("Contract Id  - " + getIntent().getStringExtra(EXTRA_KEY_CONTRACT_ID));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        init();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustomerDetailFragment(), "Customer");
        adapter.addFragment(new LoanerCarFragment(), "Loaner Car");
        adapter.addFragment(new ReturnCarFragment(), "Return Car");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        initIntentSetValues();
        getDataUsingVolley();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ll_ProgressDialog.getVisibility() == View.VISIBLE) {
            ll_ProgressDialog.setVisibility(View.GONE);
        }
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Log.e("On Back", "Cancle request");
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constant.REQUEST_TAG);
        }
    }

    void getDataUsingVolley() {
        ll_ProgressDialog.setVisibility(View.VISIBLE);
        String url = AppSingleTon.APP_URL.URL_CONTRACT_DETAIL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ll_ProgressDialog.setVisibility(View.GONE);

                try {
                    AllDetail detail1 = AppSingleTon.APP_JSON_PARSER.getAllDataResponse(response);

                    if (detail1.status_code.equals("1")) {
                        detail = detail1.cardetail;
                        setupViewPager(viewPager);
                    } else if (detail1.status_code.equals("0")) {
                        Toast.makeText(getApplicationContext(), detail1.message, Toast.LENGTH_SHORT).show();
                        AllDetailActivity.this.finish();
                    } else if (detail1.status_code.equals("4")) {
                        Toast.makeText(getApplicationContext(), detail1.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(AllDetailActivity.this);
                    } else if (detail1.status_code.equals("2")) {
                        Toast.makeText(getApplicationContext(), detail1.message, Toast.LENGTH_SHORT).show();
                    } else if (detail1.status_code.equals("5")) {
                        Toast.makeText(getApplicationContext(), detail1.message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(AllDetailActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                getDataUsingVolley();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ll_ProgressDialog.setVisibility(View.GONE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put(ParamsKey.KEY_contractId, str_contract_id);
                return param;
            }
        };
        stringRequest.setTag(Constant.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void initIntentSetValues() {
        if (getIntent().getExtras() != null) {
            str_contract_id = getIntent().getStringExtra(EXTRA_KEY_CONTRACT_ID);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
}