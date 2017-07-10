package com.yukti.driveherenew.fragment;//package com.yukti.drivehere.fragment;
//
//import java.util.ArrayList;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.R.integer;
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.qualcomm.snapdragon.sdk.face.FaceData.Chin;
//import com.yukti.drivehere.AddNewCarActivity;
//import com.yukti.drivehere.AddNewCarActivity.Fragments;
//import com.yukti.drivehere.R;
//import com.yukti.drivehere.fragment.CallbackAdd;
//
//public class FRAGAddGps extends Fragment {
//
//	private View mView;
//	CallbackAdd callbackAdd;
//	Button btn_next;
//	LinearLayout ll_container;
//	boolean isFirst;
//	EditText edt_gps;
//	SharedPreferences sharedpreferences;
//	Editor editor;
//
//	public static FRAGAddGps newInstance() {
//		FRAGAddGps f = new FRAGAddGps();
//		return f;
//	}
//
//	public interface OnTagSelectedListener {
//		public void onTagSelected(int tagPosition, String strTag);
//
//		public void onTagLongPress(int tagPosition, String strTag);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		mView = inflater.inflate(R.layout.fragment_add_gps, container, false);
//
//		btn_next = (Button) mView.findViewById(R.carId.btn_nextAddGpdValue);
//		InitNext();
//		initGps();
//
//		return mView;
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			callbackAdd = (CallbackAdd) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement CallbackAdd");
//		}
//	}
//
//	void InitNext() {
//
//		btn_next.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				AddNewCarActivity.Childcount = ll_container.getChildCount();
//				Log.e("ChildCount is", "" + AddNewCarActivity.Childcount);
//
//				AddNewCarActivity.arrayList_gps = new ArrayList<String>();
//				for (int i = 0; i < AddNewCarActivity.Childcount; i++) {
//
//					View itemList = ll_container.getChildAt(i);
//					edt_gps = (EditText) itemList.findViewById(R.carId.ed_gps);
//					AddNewCarActivity.strGps = edt_gps.getText().toString();
//					Log.e("Gps string", AddNewCarActivity.strGps);
//					AddNewCarActivity.arrayList_gps
//							.add(AddNewCarActivity.strGps);
//				}
//				Log.e("Gps Arraylist",
//						"" + AddNewCarActivity.arrayList_gps.size());
//				// getGps();
//			}
//		});
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//		if (AddNewCarActivity.arrayList_gps != null
//				&& AddNewCarActivity.arrayList_gps.size() > 0) {
//
//			edt_gps.setText(AddNewCarActivity.arrayList_gps.get(0));
//			Log.e("Gps Childcount onResume", "" + AddNewCarActivity.Childcount);
//
//			for (int i = 0; i < AddNewCarActivity.Childcount - 1; i++) {
//
//				addGpsView();
//				edt_gps.setText(AddNewCarActivity.arrayList_gps.get(i + 1));
//			}
//		}
//		if (AddNewCarActivity.AtLast) {
//			btn_next.setText(R.string.view_all_detail);
//		}
//	}
//
//	void initGps() {
//		isFirst = true;
//		ll_container = (LinearLayout) mView.findViewById(R.carId.gps_container);
//		addGpsView();
//		// getGps();
//	}
//	
//	void addGpsView() {
//		final View itemList = getActivity().getLayoutInflater().inflate(
//				R.layout.row_for_gps, ll_container, false);
//		
//		// itemList = getActivity().getLayoutInflater().inflate(
//		// R.layout.row_for_gps, ll_container, false);
//
//		edt_gps = (EditText) itemList.findViewById(R.carId.ed_gps);
//
//		Button btn_plus = (Button) itemList.findViewById(R.carId.btn_plus);
//		btn_plus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// Toast.makeText(getApplication(), "Clicked on plus",
//				// Toast.LENGTH_SHORT).show();
//
//				addGpsView();
//				// Log.e("EditValue", edt_gps.getText().toString());
//				// AddNewCarActivity.arrayList_gps.add(edt_gps.getText()
//				// .toString());
//
//			}
//		});
//
//		Button btn_minus = (Button) itemList.findViewById(R.carId.btn_minus);
//		btn_minus.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// Toast.makeText(getApplication(), "Clicked on minus",
//				// Toast.LENGTH_SHORT).show();
//
//				AddNewCarActivity.Childcount--;
//				((LinearLayout) itemList.getParent()).removeView(itemList);
//				// containerlayout.removeView(itemList.getTag());
//				edt_gps.setText("");
//			}
//		});
//
//		if (isFirst) {
//
//			isFirst = false;
//
//		} else {
//
//			btn_plus.setVisibility(View.GONE);
//			btn_minus.setVisibility(View.VISIBLE);
//		}
//		ll_container.addView(itemList);
//
//	}
//
//	String gpsJsonStr = "";
//
//	// void setView() {
//	// if (AddNewCarActivity.Childcount != 0) {
//	// for (int i = 0; i < AddNewCarActivity.Childcount; i++) {
//	// addGpsView();
//	// }
//	// AddNewCarActivity.Childcount = ll_container.getChildCount();
//	// for (int i = 0; i < AddNewCarActivity.Childcount; i++) {
//	//
//	// View itemList = ll_container.getChildAt(i);
//	// edt_gps = (EditText) itemList.findViewById(R.carId.ed_gps);
//	//
//	// String gpsStr = edt_gps.getText().toString().trim();
//	// AddNewCarActivity.arrayList_gps.add(gpsStr);
//	// edt_gps.setText(AddNewCarActivity.arrayList_gps.get(i));
//	// }
//	// }
//	// }
//
//	void getGps() {
//
//		AddNewCarActivity.Childcount = ll_container.getChildCount();
//
//		for (int i = 0; i < AddNewCarActivity.Childcount; i++) {
//
//			View itemList = ll_container.getChildAt(i);
//			edt_gps = (EditText) itemList.findViewById(R.carId.ed_gps);
//
//			String gpsStr = edt_gps.getText().toString().trim();
//
//			AddNewCarActivity.arrayList_gps.add(gpsStr);
//		}
//		if (AddNewCarActivity.arrayList_gps != null
//				&& AddNewCarActivity.arrayList_gps.size() > 0) {
//			gpsJsonStr = getGpsJsonString();
//			Log.e("gpsJsonStr", gpsJsonStr);
//		}
//
//	}
//
//	public String getGpsJsonString() {
//
//		JSONObject object = new JSONObject();
//
//		JSONArray jArray = new JSONArray();
//
//		try {
//
//			for (int i = 0; i < AddNewCarActivity.arrayList_gps.size(); i++) {
//				JSONObject Obj = new JSONObject();
//				Obj.put("carId", "-1");
//				Obj.put("gpsid", AddNewCarActivity.arrayList_gps.get(i));
//				jArray.put(Obj);
//			}
//
//			object.put("Gps", jArray);
//
//		} catch (Exception e) {
//			// Log.e("Error in getGpsJsonString() :" + e.toString());
//		}
//		// Log.e("Item To ADD:  " + object.toString());
//
//		return object.toString();
//	}
//}
