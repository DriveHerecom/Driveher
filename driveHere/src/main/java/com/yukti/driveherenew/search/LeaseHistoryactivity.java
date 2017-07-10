package com.yukti.driveherenew.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.yukti.driveherenew.BaseActivity;
import com.yukti.driveherenew.R;

public class LeaseHistoryactivity extends BaseActivity {

    String Vinnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leasehistory);

        Intent intent = getIntent();
        Vinnumber = intent.getStringExtra("vin");
        TextView tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        tv_no_record.setVisibility(View.VISIBLE);
//        Leasehistorydata();

    }

    private void Leasehistorydata() {
        // TODO Auto-generated method stub

        RequestParams params = new RequestParams();
        params.put("vin", Vinnumber);

        AsyncHttpClient client = new AsyncHttpClient();
//		client.post("http://www.drivehere.com/api/autostart.php", params,new Leasedatahandler());
    }
}
