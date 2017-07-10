package com.yukti.newchanges.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.R;
import com.yukti.newchanges.fragment.AuctionFragment;
import com.yukti.newchanges.fragment.BasicFragment;
import com.yukti.newchanges.fragment.CarDetailFragment;
import com.yukti.newchanges.fragment.ServiceFragment;
import com.yukti.newchanges.fragment.StatusesFragment;
import com.yukti.newchanges.util.SearchModel;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    View vw_basic, vw_carDetails, vw_statuses, vw_auction, vw_service;
    LinearLayout ll_basic, ll_carDetails, ll_statuses, ll_auction, ll_service;
    ImageView iv_basic, iv_cardtail, iv_status, iv_auction, iv_service;
    TextView tv_basic, tv_cardtail, tv_status, tv_auction, tv_service;

    Button btn_applyFilter, btnReset;

    int container;
    FragmentTransaction ft;

    String currentFragment = null;
    public static boolean needReset = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        initToolbar();
        initView();
        currentFragment = "basic";
        setFragment(false);
        needReset = false;
    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_search);
        toolbar.setTitle("Filter Car List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void initView() {
        ll_basic = (LinearLayout) findViewById(R.id.ll_basic);
        ll_carDetails = (LinearLayout) findViewById(R.id.ll_carDetails);
        ll_statuses = (LinearLayout) findViewById(R.id.ll_statuses);
        ll_service = (LinearLayout) findViewById(R.id.ll_service);
        ll_auction = (LinearLayout) findViewById(R.id.ll_auction);

        ll_basic.setOnClickListener(this);
        ll_carDetails.setOnClickListener(this);
        ll_statuses.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_auction.setOnClickListener(this);

        vw_basic = findViewById(R.id.vw_basic);
        vw_carDetails = findViewById(R.id.vw_carDetails);
        vw_statuses = findViewById(R.id.vw_statuses);
        vw_service = findViewById(R.id.vw_service);
        vw_auction = findViewById(R.id.vw_auction);

        iv_basic = (ImageView) findViewById(R.id.iv_basic);
        iv_cardtail = (ImageView) findViewById(R.id.iv_carDetails);
        iv_status = (ImageView) findViewById(R.id.iv_statuses);
        iv_service = (ImageView) findViewById(R.id.iv_service);
        iv_auction = (ImageView) findViewById(R.id.iv_auction);

        tv_basic = (TextView) findViewById(R.id.tv_basic);
        tv_cardtail = (TextView) findViewById(R.id.tv_carDetails);
        tv_status = (TextView) findViewById(R.id.tv_statuses);
        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_auction = (TextView) findViewById(R.id.tv_auction);

        btn_applyFilter = (Button) findViewById(R.id.btn_applyFilter);
        btn_applyFilter.setOnClickListener(this);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        container = R.id.fm_container;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_basic:
                currentFragment = "basic";
                setFragment(false);
                break;

            case R.id.ll_carDetails:
                currentFragment = "carDetail";
                setFragment(false);
                break;

            case R.id.ll_statuses:
                currentFragment = "statuses";
                setFragment(false);
                break;

            case R.id.ll_service:
                currentFragment = "service";
                setFragment(false);
                break;

            case R.id.ll_auction:
                currentFragment = "auction";
                setFragment(false);
                break;

            case R.id.btn_applyFilter:
                setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.btnReset:
                needReset = true ;
                setFragment(true);
                break;
        }
    }

    void clearAllView() {
        ll_basic.setBackgroundColor(getResources().getColor(R.color.black));
        ll_carDetails.setBackgroundColor(getResources().getColor(R.color.black));
        ll_statuses.setBackgroundColor(getResources().getColor(R.color.black));
        ll_service.setBackgroundColor(getResources().getColor(R.color.black));
        ll_auction.setBackgroundColor(getResources().getColor(R.color.black));

        iv_basic.setImageDrawable(getResources().getDrawable(R.drawable.wh_basic_search));
        iv_cardtail.setImageDrawable(getResources().getDrawable(R.drawable.wh_cardtail_search));
        iv_service.setImageDrawable(getResources().getDrawable(R.drawable.wh_service_search));
        iv_status.setImageDrawable(getResources().getDrawable(R.drawable.wh_status_search));
        iv_auction.setImageDrawable(getResources().getDrawable(R.drawable.wh_auction_search));

        tv_basic.setTextColor(getResources().getColor(R.color.white));
        tv_cardtail.setTextColor(getResources().getColor(R.color.white));
        tv_status.setTextColor(getResources().getColor(R.color.white));
        tv_service.setTextColor(getResources().getColor(R.color.white));
        tv_auction.setTextColor(getResources().getColor(R.color.white));

        vw_basic.setVisibility(View.GONE);
        vw_carDetails.setVisibility(View.GONE);
        vw_statuses.setVisibility(View.GONE);
        vw_service.setVisibility(View.GONE);
        vw_auction.setVisibility(View.GONE);
    }

    void viewVisibility(String view) {
        findViewById(getResources().getIdentifier("ll_" + view, "carId", this.getPackageName())).setBackgroundColor(getResources().getColor(R.color.white));
        findViewById(getResources().getIdentifier("vw_" + view, "carId", this.getPackageName())).setVisibility(View.VISIBLE);
        TextView txt = (TextView) findViewById(getResources().getIdentifier("tv_" + view, "carId", this.getPackageName()));
        txt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        ImageView iv = (ImageView) findViewById(getResources().getIdentifier("iv_" + view, "carId", this.getPackageName()));
        int id = this.getResources().getIdentifier("bl_" + view.toLowerCase() + "_search", "drawable", this.getPackageName());
        iv.setImageResource(id);
    }

    void setFragment(boolean clearData) {
        clearAllView();

        if (clearData) {
            MainActivity.searchModel = new SearchModel();
        }

        if (currentFragment.equalsIgnoreCase("basic")) {
            viewVisibility("basic");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(container, BasicFragment.newInstance(clearData));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            currentFragment = "basic";
        } else if (currentFragment.equalsIgnoreCase("carDetail")) {
            viewVisibility("carDetails");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(container, CarDetailFragment.newInstance(clearData));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            currentFragment = "carDetail";
        } else if (currentFragment.equalsIgnoreCase("statuses")) {
            viewVisibility("statuses");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(container, StatusesFragment.newInstance(clearData));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            currentFragment = "statuses";
        } else if (currentFragment.equalsIgnoreCase("service")) {
            viewVisibility("service");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(container, ServiceFragment.newInstance(clearData));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            currentFragment = "service";
        } else if (currentFragment.equalsIgnoreCase("auction")) {
            viewVisibility("auction");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(container, AuctionFragment.newInstance(clearData));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            currentFragment = "auction";
        }
    }
}
