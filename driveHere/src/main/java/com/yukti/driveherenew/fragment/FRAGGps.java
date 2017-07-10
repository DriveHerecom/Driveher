package com.yukti.driveherenew.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.BarCodeScannerActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.ParamsKey;

public class FRAGGps extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    //    EditText edt_gps_installed;
    String TAG_GPS_NSTALLED = "TAG_GPS_NSTALLED", checkBoxValue = "";
    LinearLayout ll_container;
    CheckBox checkboxYes, checkboxNo;
    EditText edt_gps;
    TextView tv_scan;
    OnClickListener checkBoxListner;
    public int IP_SCANNER = 101;
    public static final int FRAGGPS__REQUEST_CAMERA_GPS = 122;

    public static FRAGGps newInstance() {
        FRAGGps f = new FRAGGps();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_gps_installed, container,
                false);

        initGps();
        initGPSInstalled();
        InitNext();
        return mView;
    }

    void initGps() {
        ll_container = (LinearLayout) mView.findViewById(R.id.gps_container);
        if (AddNewCarActivity.arrayListGpsSerial.size() == 0) {
            addGpsView("");
        }
    }

    int i = 0;

    EditText edtSelected;

    void addGpsView(String value) {
        final View itemList = getActivity().getLayoutInflater().inflate(R.layout.row_for_gps, ll_container, false);

        Button btn_plus = (Button) itemList.findViewById(R.id.btn_plus);
        edt_gps = (EditText) itemList.findViewById(R.id.ed_gps);
        edt_gps.setText(value);
        btn_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addGpsView("");
            }
        });

        Button btn_minus = (Button) itemList.findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) itemList.getParent()).removeView(itemList);
                String gps_temp = ((EditText) itemList.findViewById(R.id.ed_gps)).getText().toString().trim();
                if (AddNewCarActivity.arrayListGpsSerial.contains(gps_temp)) {
                    AddNewCarActivity.arrayListGpsSerial.remove(gps_temp);
                }
            }
        });
        tv_scan = (TextView) itemList.findViewById(R.id.tv_gpsscan);
        tv_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AppSingleTon.VERSION_OS.checkVersion()) {
                    // Marshmallow+
                    if (getActivity().checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        View view = (View) v.getParent();
                        edtSelected = (EditText) view.findViewById(R.id.ed_gps);

                        Intent scanIPintent = new Intent(getActivity(), BarCodeScannerActivity.class);
                        startActivityForResult(scanIPintent, IP_SCANNER);
                    } else {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("");
                            builder.setMessage("Camera Permission Needed For Scanning,Allow It?");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final Intent i = new Intent();
                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(i);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, FRAGGPS__REQUEST_CAMERA_GPS);
                                }
                            });
                            builder.show();
                        }
                    }
                } else {
                    // Pre-Marshmallow
                    View view = (View) v.getParent();
                    edtSelected = (EditText) view.findViewById(R.id.ed_gps);
                    Intent scanIPintent = new Intent(getActivity(), BarCodeScannerActivity.class);
                    startActivityForResult(scanIPintent, IP_SCANNER);
                }
            }
        });
        if (ll_container.getChildCount() > 0) {
            btn_plus.setVisibility(View.GONE);
            btn_minus.setVisibility(View.VISIBLE);
        }
        ll_container.addView(itemList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK)
            if (requestCode == IP_SCANNER) {
                edtSelected.setText(data.getStringExtra("code"));
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FRAGGPS__REQUEST_CAMERA_GPS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent scanIPintent = new Intent(getActivity(),
                        BarCodeScannerActivity.class);
                startActivityForResult(scanIPintent, IP_SCANNER);
            } else {
                Toast.makeText(getActivity(), "Camera Permission was Denied", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackAdd = (CallbackAdd) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackAdd");
        }
    }

    void InitNext() {
        Button btn_next = (Button) mView
                .findViewById(R.id.btn_nextGpsInstalled);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Add Photos");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrGpsInstall().equalsIgnoreCase(checkBoxValue.toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_gpsInstalled))
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_gpsInstalled);
                }
                AddNewCarActivity.addCarModelObject.setStrGpsInstall(checkBoxValue.trim().toString());
                if (AddNewCarActivity.addCarModelObject.getStrGpsInstall().equalsIgnoreCase("yes")) {
                    AddNewCarActivity.arrayListGpsSerial = new ArrayList<String>();
                    int count = ll_container.getChildCount();
                    for (int i = 0; i < count; i++) {
                        View itemList = ll_container.getChildAt(i);
                        EditText edt_gps = (EditText) itemList.findViewById(R.id.ed_gps);
                        String gpsSerial = edt_gps.getText().toString();
                        if (gpsSerial != null && gpsSerial.length() > 0)
                            AddNewCarActivity.arrayListGpsSerial.add(gpsSerial);
                    }
                } else {
                    AddNewCarActivity.arrayListGpsSerial = new ArrayList<String>();
                }

                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrGpsInstall() != null
                && AddNewCarActivity.addCarModelObject.getStrGpsInstall().length() != 0) {
//            edt_gps_installed.setText(AddNewCarActivity.addCarModelObject.getStrGpsInstall());
            if (AddNewCarActivity.addCarModelObject.getStrGpsInstall().equalsIgnoreCase("yes")) {
                checkboxYes.setChecked(true);
            } else {
                checkboxNo.setChecked(true);
            }
        }
        setgpsvalue();
    }

    void setgpsvalue() {
        Log.e("GPS ARRAY SIZE", AddNewCarActivity.arrayListGpsSerial.size()
                + "");
        if (AddNewCarActivity.arrayListGpsSerial != null
                && AddNewCarActivity.arrayListGpsSerial.size() > 0) {
            for (int i = 0; i < AddNewCarActivity.arrayListGpsSerial.size(); i++) {
                addGpsView(AddNewCarActivity.arrayListGpsSerial.get(i));
            }
        }
    }

    void initGPSInstalled() {
        checkboxYes = (CheckBox) mView.findViewById(R.id.checkboxYes);
        checkboxNo = (CheckBox) mView.findViewById(R.id.checkboxNo);

        checkboxYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkboxYes.isChecked()) {
                    ll_container.setVisibility(View.VISIBLE);
                    checkboxNo.setChecked(false);
                    checkBoxValue = "yes";
                }
            }
        });
        checkboxNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkboxNo.isChecked()) {
                    ll_container.setVisibility(View.GONE);
                    checkboxYes.setChecked(false);
                    checkBoxValue = "no";
                }
            }
        });
       /* edt_gps_installed = (EditText) mView.findViewById(R.carId.edt_GpsInstalled);
        edt_gps_installed.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("yes")) {
                    ll_container.setVisibility(View.VISIBLE);
                } else {
                    ll_container.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_gps_installed.setText("No");*/

        final String title = "Choose One";
        final CharSequence[] driveTypeList = getResources().getStringArray(R.array.Title);

        final ListDialogListener listener = new ListDialogListener() {
            @Override
            public void onItemClick(int position) {
//                edt_gps_installed.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        /*edt_gps_installed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, driveTypeList, listener);
                dialog.show(getChildFragmentManager(), TAG_GPS_NSTALLED);
            }
        });*/
    }
}
