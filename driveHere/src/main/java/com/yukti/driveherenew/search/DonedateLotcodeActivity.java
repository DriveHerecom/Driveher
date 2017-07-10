package com.yukti.driveherenew.search;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.MyApplication;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.jsonparser.Login;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DonedateLotcodeActivity extends AppCompatActivity implements View.OnClickListener {
    String donedate, lotcode, carid, is_done;
    LinearLayout ll_progressDonedateLotcode, ll_DonedateLotcode;
    EditText edt_DoneDate, edt_Lotcode;
    boolean lot, date;
    public static String EXTRAKEY_ID = "carId";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donedate_lotcode);

        initToolbar();
        lot = false;
        date = false;
        ll_progressDonedateLotcode = (LinearLayout) findViewById(R.id.ll_progress_donedate_lotcode);
        ll_DonedateLotcode = (LinearLayout) findViewById(R.id.ll_donedate_lotcode);
        edt_DoneDate = (EditText) findViewById(R.id.edt_donedate);
        edt_Lotcode = (EditText) findViewById(R.id.edt_donedate_lotcode);
        Button btn_Update = (Button) findViewById(R.id.btn_update_lotcode);

        carid = getIntent().getExtras().getString(EXTRAKEY_ID);
        is_done = getIntent().getExtras().getString("is_done");
        Log.e("is_done", "" + is_done);
        Log.e("carid", "" + carid);
        edt_DoneDate.setOnClickListener(this);
        edt_Lotcode.setOnClickListener(this);
        btn_Update.setOnClickListener(this);
    }

    public void OpenDialogDonedate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DonedateLotcodeActivity.this);
        builder.setMessage("car is not done in previous stage, Do you want to done priveous stage and change car stage to new stage?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openDatePicker();
            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog d = builder.create();
        d.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_donedate:
                if (is_done.equalsIgnoreCase("1")) {
                    OpenDialogDonedate();
                } else {
                    openDatePicker();
                }

                break;

            case R.id.edt_donedate_lotcode:
                chooseLotcode();
                break;

            case R.id.btn_update_lotcode:

                if (lotcode != null && lotcode.length() > 0)
                    lot = true;
                else
                    lot = false;

                if (donedate != null && donedate.length() > 0)
                    date = true;
                else
                    date = false;

                if (lot && date) {
                    editDoneDateUsingVolley(donedate, lotcode);
                } else {

                    if (!lot && !date)
                        Toast.makeText(getApplicationContext(), "Fill All Details", Toast.LENGTH_SHORT).show();
                    else {
                        if (!lot)
                            Toast.makeText(getApplicationContext(), "Please Select Lotcode", Toast.LENGTH_SHORT).show();
                        if (!date)
                            Toast.makeText(getApplicationContext(), "Please Select Donedate", Toast.LENGTH_SHORT).show();

                    }

                }

                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_donedate_lotcode_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(DonedateLotcodeActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {

                        monthOfYear = monthOfYear + 1;
                        String month,day;

                        if (monthOfYear < 10) {
                            month = "0" + monthOfYear ;
                        } else {
                            month = "" + monthOfYear ;
                        }

                        if (dayOfMonth<10)
                        {
                            day = "0" + dayOfMonth ;
                        }
                        else
                        {
                            day = "" + dayOfMonth ;
                        }
                        donedate = year+"-"+month+"-"+day;
                        edt_DoneDate.setText(year+"-"+month+"-"+day);
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        dpd.show();
    }


    void chooseLotcode() {
        Log.e("INSIDE", "CHOOSE LOTCODE");

        final String title = "Choose Lot Code";
        final CharSequence[] lotList = getResources().getStringArray(R.array.Lotcode);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_Lotcode.setText(lotList[position]);
                lotcode = lotList[position].toString();
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        String TAG_COLOR = "TAG_COLOR";
        LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
                listener);
        dialog1.show(getSupportFragmentManager(), TAG_COLOR);
    }

    void editDoneDateUsingVolley(final String donedate, final String doneDateLotcode) {

        ll_DonedateLotcode.setClickable(false);
        ll_progressDonedateLotcode.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_UPDATEDONEDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                ll_progressDonedateLotcode.setVisibility(View.GONE);
                ll_DonedateLotcode.setClickable(true);
                try {
                    Login respons = AppSingleTon.APP_JSON_PARSER.login(response);
                    if (respons.status_code.equals("1")) {
                        Toast.makeText(getApplicationContext(), respons.message, Toast.LENGTH_SHORT).show();
                        CarDetailsActivity.isNeeded=true;
                        finish();

                    } else if (respons.status_code.equals("2")) {
                        Toast.makeText(getApplicationContext(), respons.message, Toast.LENGTH_SHORT).show();

                    } else if (respons.status_code.equals("0")) {
                        Toast.makeText(getApplicationContext(), respons.message, Toast.LENGTH_SHORT).show();
                    }else if(respons.status_code.equalsIgnoreCase("4")){
                        Toast.makeText(getApplicationContext(), respons.message, Toast.LENGTH_SHORT).show();
                        AppSingleTon.logOut(getApplicationContext());
                    }else{
                        Toast.makeText(getApplicationContext(), respons.message, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(DonedateLotcodeActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                editDoneDateUsingVolley(donedate,doneDateLotcode);
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
                        ll_progressDonedateLotcode.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                params.put(ParamsKey.KEY_carId, carid);
                params.put(ParamsKey.KEY_doneDate, donedate);
                params.put(ParamsKey.KEY_doneDateLotCode, doneDateLotcode);
                Log.e("parameter", "" + params);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
