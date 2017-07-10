package com.yukti.driveherenew;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.MissingCar;
import com.yukti.dataone.model.ReportResult;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.RestClient;

public class MissingCarDetailActivity extends BaseActivity {


	String lotcode = "";
	RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_missing_car_detail);
		initToolbar();
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		LinearLayoutManager	layoutManager = new LinearLayoutManager(MissingCarDetailActivity.this);
		recyclerView.setLayoutManager(layoutManager);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			lotcode = getIntent().getStringExtra("lotcode");
			Log.e("LotcodeFromBundle", lotcode);
			getMissingCarDetail();
			
		}
	}

	void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_missing_car_detail_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

	CustomAdapter adapter;

	void setAdapter(ArrayList<MissingCar> missingcarresult) {
		if (adapter == null) {
			adapter = new CustomAdapter(missingcarresult);
			recyclerView.setAdapter(adapter);
		} else {
			// adapter.add(carList);
		}
	}

	public class CustomAdapter extends
			RecyclerView.Adapter<CustomAdapter.ViewHolder> implements
			OnClickListener {

		public List<MissingCar> items;

		LayoutInflater inflater;

		public CustomAdapter(ArrayList<MissingCar> missingcarresult) {
			this.items = missingcarresult;
			inflater = LayoutInflater.from(MissingCarDetailActivity.this);
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			TextView txtVin, txtRfid;
			public ViewHolder(View v) {
				super(v);
				txtVin = (TextView) v.findViewById(R.id.tv_vin);
				txtRfid = (TextView) v.findViewById(R.id.tv_rfid);
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			// Create a new view.
			View v = inflater.inflate(R.layout.row_miss_car_detail, viewGroup,false);
			v.setOnClickListener(this);
			return new ViewHolder(v);
		}

		@SuppressLint("NewApi")
		@Override
		public void onBindViewHolder(ViewHolder viewHolder, final int position) {

			MissingCar missingcar = items.get(position);

			if (!missingcar.vin.equalsIgnoreCase(""))
				viewHolder.txtVin.setText(missingcar.vin);

			if (!missingcar.rfid.equalsIgnoreCase(""))
				viewHolder.txtRfid.setText(missingcar.rfid);
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		@Override
		public void onClick(View v) {
			 int itemPosition = recyclerView.getChildAdapterPosition(v);
			 // showToast("clicked "+itemPosition);
			 Intent intent = new Intent(MissingCarDetailActivity.this,
			 com.yukti.driveherenew.search.CarDetailsActivity.class);
			 Log.e("selected vin", adapter.items.get(itemPosition).vin);
			 intent.putExtra("vin",
			 adapter.items.get(itemPosition).vin);
			 intent.putExtra("itemPosition",itemPosition);
			 intent.putExtra("isFromMissingCar", true);
			 startActivity(intent);
		}
	}

	void getMissingCarDetail() {
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.d("Missing Car"," error response"+ responseString);
				showToast("Missing Car error");
				dismissProgressDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.d("Missing_Car_response", response.toString());

				ReportResult orm = AppSingleTon.APP_JSON_PARSER.missingCardata(response);

				if (orm.status_code.equals("1")) {
					 setAdapter(orm.missingcarresult);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				showUpdateProgressDialog("Get Missing Car Data......");
			}

			@Override
			public void onFinish() {
				super.onFinish();
				dismissProgressDialog();
			}
		};

		String url = AppSingleTon.APP_URL.URL_GET_MISSING_CAR_DETAIL;
		RequestParams params = new RequestParams();
		params.put("lotcode", lotcode);
		RestClient.post(this, url, params, handler);
	}
}
