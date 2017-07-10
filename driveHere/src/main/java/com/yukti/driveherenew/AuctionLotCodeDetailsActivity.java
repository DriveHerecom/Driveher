package com.yukti.driveherenew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yukti.dataone.model.LotcodeCount;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuctionLotCodeDetailsActivity extends AppCompatActivity implements View.OnClickListener
{
    Button btn_dhc_04,btn_dhp_05,btn_unknown,btn_l405_53,btn_bs_52,btn_cv_51,btn_int,btn_nolotcode;
    LinearLayout ll_progress;
    Toolbar toolbar;
    String auctionName;
    public LotcodeCount lotcodeCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_lot_code_details);

        toolbar = (Toolbar) findViewById(R.id.activity_auction_lot_code_details_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("Auction Lotcodes");
        ll_progress = (LinearLayout) findViewById(R.id.ll_progress_auction_lotcodes);
        auctionName = getIntent().getStringExtra("auctionname");

        initButton();
        initOnclickListener();
//      search(auctionName);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.e("OnResume  lotes :","auction name"+auctionName);
        search(auctionName);
    }

    void initButton()
    {
        btn_dhc_04 = (Button) findViewById(R.id.btn_dhc_04);
        btn_dhp_05 = (Button) findViewById(R.id.btn_dhp_05);
        btn_unknown = (Button) findViewById(R.id.btn_unknown);
        btn_cv_51 = (Button) findViewById(R.id.btn_cv_51);
        btn_bs_52 = (Button) findViewById(R.id.btn_bs_52);
        btn_l405_53 = (Button) findViewById(R.id.btn_l405_53);
        btn_int = (Button) findViewById(R.id.btn_Int);
        btn_nolotcode=(Button)findViewById(R.id.btn_NoLotCode);
    }

    void initOnclickListener()
    {
        btn_dhp_05.setOnClickListener(this);
        btn_dhc_04.setOnClickListener(this);
        btn_unknown.setOnClickListener(this);
        btn_cv_51.setOnClickListener(this);
        btn_bs_52.setOnClickListener(this);
        btn_l405_53.setOnClickListener(this);
        btn_int.setOnClickListener(this);
        btn_nolotcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String lotcode = "";

        switch (v.getId())
        {
            case R.id.btn_dhc_04:
                lotcode = "DHC-04";
                break;

            case R.id.btn_dhp_05:
                lotcode = "DHP-05";
                break;

            case R.id.btn_cv_51:
                lotcode = "CV-51";
                break;

            case R.id.btn_bs_52:
                lotcode = "BS-52";
                break;

            case R.id.btn_l405_53:
                lotcode = "405-53";
                break;

            case R.id.btn_unknown:
                lotcode = "Unknown";
                break;

            case R.id.btn_Int:
                lotcode = "Int";
                break;

            case R.id.btn_NoLotCode:
                lotcode = "No LotCode";
                break;

            default:
                break;
        }
        Intent i = new Intent(AuctionLotCodeDetailsActivity.this,AuctionLotAvailableDetailsActivity.class);
        i.putExtra("lotcode",lotcode);
        i.putExtra("auctionname",auctionName);
        startActivity(i);
    }

    void search(final String auctionname)
    {
        ll_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppSingleTon.APP_URL.URL_AUCTION_NAME_LOTCODE_COUNT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("RESPONSE", "Auction Lotocde" + response);

                        ll_progress.setVisibility(View.GONE);

                        if (response != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status_code");
                                String msg = jsonObject.getString("message");
                                Log.e("status : ", "" + status + " : msg : " + msg);

                                if (status.equals("1"))
                                {
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                                    if (jsonArray.length() > 0)
                                    {
                                        lotcodeCount = new LotcodeCount();
                                        for (int i = 0; i < jsonArray.length(); i++)
                                        {
                                            JSONObject lotcode = jsonArray.getJSONObject(i);

                                            String lot = lotcode.getString("lotcode");
                                            int count = lotcode.getInt("total");

                                            //String auctionDate = lotcode.getString("auctiondate");

                                            switch (lot)
                                            {
                                                case "DHC-04":
                                                    lotcodeCount.dhc_04 = count;
                                                    break;
                                                case "DHP-05":
                                                    lotcodeCount.dhp_05 = count;
                                                    break;
                                                case "CV-51":
                                                    lotcodeCount.cv_51 = count;
                                                    break;
                                                case "BS-52":
                                                    lotcodeCount.bs_52 = count;
                                                    break;
                                                case "405-53":
                                                    lotcodeCount.l405_53 = count;
                                                    break;
                                               /* case "Kia-54":
                                                    lotcodeCount.kia_54 = count;
                                                    break;
                                                case "OT":
                                                    lotcodeCount.ot = count;
                                                    break; */
                                                case "Unknown":
                                                    lotcodeCount.unknown = count;
                                                    break;
                                                case "Int":
                                                    lotcodeCount.Int = count;
                                                    break;
                                                case "No LotCode":
                                                    lotcodeCount.No_LotCode = count;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        setButtonText();
                                    } else {

                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), " " + msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Log.e("EXCEPTION PARSING: ", "" + e.toString());
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        ll_progress.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext()," "+error,Toast.LENGTH_SHORT).show();
                        Log.e("RESPONSE ", "AuctionLotcode" + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("auctionname",auctionname);
                Log.e("SEARCH PARAMS ","auction name "+ auctionname);
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

    void setButtonText()
    {
        if(lotcodeCount == null)
        {
            btn_dhc_04.setText("DHC-04" + "  " + "(" + 0 + ")");
            btn_dhp_05.setText("DHP-05" + "  " + "(" + 0 + ")");
            btn_cv_51.setText("CV-51" + "  " + "(" + 0 + ")");
            btn_bs_52.setText("BS-52" + "  " + "(" + 0 + ")");
            btn_l405_53.setText("405-53" + "  " + "(" + 0 + ")");
            //btn_kia_54.setText("KIA-54" + "  " + "(" + 0 + ")");
            //btn_ot.setText("OT" + "  " + "(" + 0 + ")");
            btn_unknown.setText("UNKNOWN" + "  " + "(" + 0 + ")");
            btn_int.setText("INT" + "  " + "(" + 0 + ")");
            btn_nolotcode.setText("NO LOTCODE" + "  " + "(" + 0 + ")");
        }
        else
        {
            btn_dhc_04.setText("DHC-04" + "  " + "(" + lotcodeCount.dhc_04 + ")");
            btn_dhp_05.setText("DHP-05" + "  " + "(" + lotcodeCount.dhp_05 + ")");
            btn_cv_51.setText("CV-51" + "  " + "(" + lotcodeCount.cv_51 + ")");
            btn_bs_52.setText("BS-52" + "  " + "(" + lotcodeCount.bs_52 + ")");
            btn_l405_53.setText("405-53" + "  " + "(" + lotcodeCount.l405_53 + ")");
            //btn_kia_54.setText("KIA-54" + "  " + "(" + lotcodeCount.kia_54 + ")");
            //btn_ot.setText("OT" + "  " + "(" + lotcodeCount.ot + ")");
            btn_unknown.setText("UNKNOWN" + "  " + "(" + lotcodeCount.unknown + ")");
            btn_int.setText("INT" + "  " + "(" + lotcodeCount.Int + ")");
            btn_nolotcode.setText("NO LOTCODE" + "  " + "(" + lotcodeCount.No_LotCode + ")");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
