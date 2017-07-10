package com.yukti.driveherenew;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.ParamsKey;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class InquiryActivity extends AppCompatActivity {
    EditText edt_model, edt_vin, edt_date, edt_query;
    AutoCompleteTextView edt_mail;
    ArrayList<String> emaillist;
    CarInventory car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        setToolbar();
        init();
        initsubmitbutton();
        getloginemail();
    }

    void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_inquiry_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void getloginemail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();

        emaillist = new ArrayList<String>();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                emaillist.add(possibleEmail);
            }
        }

        edt_mail = (AutoCompleteTextView) findViewById(R.id.autocompleteEditTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, emaillist);
        edt_mail.setThreshold(1);
        edt_mail.setAdapter(arrayAdapter);
    }

    void init() {
        edt_mail = (AutoCompleteTextView) findViewById(R.id.autocompleteEditTextView);
        edt_model = (EditText) findViewById(R.id.edt_model);
        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_query = (EditText) findViewById(R.id.edt_query);
        edt_vin = (EditText) findViewById(R.id.edt_vin);

        car = (CarInventory) getIntent().getExtras().getSerializable(Constant.EXTRA_KEY_EACH_CAR);

        if (car.vin.length() != 0 && car.vin != null) {
            edt_vin.setText(car.vin);
        }
        if (car.model.length() != 0 && car.model != null) {
            edt_model.setText(car.model);
        }


        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        edt_date.setText(date);
    }

    boolean validation() {
        boolean isValidate = false;
        if (edt_mail.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_mail.getText().toString().trim()).matches()) {
            Toast.makeText(getApplicationContext(),"Fill Proper Email Address",Toast.LENGTH_SHORT).show();
        } else
            isValidate = true;
        return isValidate;
    }

    void initsubmitbutton() {
        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    if (Common.isNetworkConnected(getApplicationContext())) {
                        storeinquirydata();
                    } else {
                        Toast.makeText(getApplicationContext(),"Please Connect To Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    void storeinquirydata() {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Common.dismissProgressDialog();
                Log.e("Succcess", response + "");
                try {
                    if (response.getString("status_code").equalsIgnoreCase("1")) {
                        Toast.makeText(getApplicationContext(), "Store Successfully..", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    if (response.getString("status_code").equalsIgnoreCase("4"))
                    {
                        Toast.makeText(InquiryActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(InquiryActivity.this);
                    }
                    else
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(InquiryActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                storeinquirydata();
                            else
                                Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("Failure", responseString);
                Common.dismissProgressDialog();
            }

            @Override
            public void onStart() {
                super.onStart();
                Common.showUpdateProgressDialog("Please Wait...", InquiryActivity.this);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Common.dismissProgressDialog();
            }
        };

        RequestParams params = new RequestParams();

        params.put(ParamsKey.KEY_vin, edt_vin.getText().toString().trim());
        params.put(ParamsKey.KEY_EMAILID, edt_mail.getText().toString().trim());
        params.put(ParamsKey.KEY_DATE, edt_date.getText().toString().trim());
        params.put(ParamsKey.KEY_QUERY, edt_query.getText().toString().trim());
        params.put(ParamsKey.KEY_model, edt_model.getText().toString().trim());
        params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
        params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getApplicationContext(), AppSingleTon.APP_URL.URL_AUCTION_INQUIRY, params, handler);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
