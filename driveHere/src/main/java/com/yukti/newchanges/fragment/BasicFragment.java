package com.yukti.newchanges.fragment;

import android.Manifest;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yukti.driveherenew.AddCarScannerActivity;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.MessageDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.AppSingleTon;

import java.util.Calendar;

public class BasicFragment extends Fragment implements MessageDialogFragment.MessageDialogListener
{
    public static final int REQUEST_SCAN_VIN = 1001;
    public static final int REQUEST_SCAN_RFID = 1002;
    public static final int SEARCH_ACTIVITY_REQUEST_CAMERA = 1003;

    EditText edt_vin, edt_rfid, edt_make, edt_modelYear, edt_lotCode;
    String TAG = "Choose Option";
    int scanId;
    boolean clear;

    View.OnClickListener scanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int scanId = v.getId();
            if (AppSingleTon.VERSION_OS.checkVersion()) {
                if (!AppSingleTon.METHOD_BOX.isGpsEnabled(getActivity())) {
                    DialogFragment fragment = new MessageDialogFragment(
                            "Device GPS is Off.",
                            "You need to switch on GPS to use this functionality.",
                            true, "Enable GPS", true, "Cancel", false, "",
                            BasicFragment.this);
                    fragment.show(getActivity().getSupportFragmentManager(), "ENABLE_GPS");
                } else if (!AppSingleTon.METHOD_BOX.isInternetConnected()) {
                    DialogFragment fragment = new MessageDialogFragment(
                            "No internet Connection.",
                            "Please pick internet connection.", true,
                            "Enable Internet", true, "Cancel", false, "",
                            BasicFragment.this);
                    fragment.show(getActivity().getSupportFragmentManager(), "ENABLE_SETTING");
                }else
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    scanTest(scanId);
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setTitle("");
                        builder.setMessage("Camera Permission Needed For Scanning,Allow It?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getActivity().getApplicationContext().getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, SEARCH_ACTIVITY_REQUEST_CAMERA);
                            }
                        });
                        builder.show();
                    }
                }
            } else {
                scanTest(scanId);
            }
        }
    };

    public BasicFragment(){}

    BasicFragment(boolean clear) {
        this.clear = clear ;
    }

    public static BasicFragment newInstance(boolean clear) {
        BasicFragment fragment = new BasicFragment(clear);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragbasic, container, false);
        initValues(v);
        initTextChangeListner();

        initLotCode();
        initModelYear();
        initMake();

        if (clear) {
            clearData();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    void initValues(View view) {
        edt_lotCode = (EditText) view.findViewById(R.id.lotCode);
        edt_modelYear = (EditText) view.findViewById(R.id.model_year);
        edt_make = (EditText) view.findViewById(R.id.make);
        edt_vin = (EditText) view.findViewById(R.id.vin);
        edt_rfid = (EditText) view.findViewById(R.id.rfid);

        TextView tvScanRfid = (TextView) view.findViewById(R.id.tv_scan_rfid);
        TextView tvScanVin = (TextView) view.findViewById(R.id.tv_scan_vin);

        tvScanRfid.setOnClickListener(scanListener);
        tvScanVin.setOnClickListener(scanListener);
    }

    void setData()
    {
        if (MainActivity.searchModel != null) {
            edt_lotCode.setText(MainActivity.searchModel.getLotCode());
            edt_modelYear.setText(MainActivity.searchModel.getModelYear());
            edt_make.setText(MainActivity.searchModel.getMake());
            edt_vin.setText(MainActivity.searchModel.getVin());
            edt_rfid.setText(MainActivity.searchModel.getRfid());
        }
    }

    void clearData() {
        edt_lotCode.setText("");
        edt_modelYear.setText("");
        edt_make.setText("");
        edt_vin.setText("");
        edt_rfid.setText("");
    }

    void initTextChangeListner() {
        edt_lotCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setLotCode(s.toString());
            }
        });

        edt_modelYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setModelYear(s.toString());
            }
        });

        edt_make.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setMake(s.toString());
            }
        });

        edt_rfid.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setRfid(s.toString());
            }
        });

        edt_vin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setVin(s.toString());
            }
        });

    }

    void initLotCode() {
        final CharSequence[] lotList = getResources().getStringArray(R.array.Lotcode);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                edt_lotCode.setText(lotList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_lotCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(listener);
                dialog1.show(getActivity().getSupportFragmentManager(), TAG);

            }
        });
    }

    void initModelYear() {
        final String title = "Choose Model Year";
        final CharSequence[] yearList = getYearList();

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                edt_modelYear.setText(yearList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_modelYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, yearList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    CharSequence[] getYearList() {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int sz = (currentYear - 1980) + 1;
        CharSequence[] yearList = new CharSequence[sz];

        for (int i = 0; i < sz; i++) {
            yearList[i] = String.valueOf(1980 + i) + "";
        }
        return yearList;
    }

    void initMake() {
        final String title = "Choose Make";
        final CharSequence[] makeList = getResources().getStringArray(R.array.Make);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_make.setText(makeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, makeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanTest(scanId);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void scanTest(int id) {
        switch (id) {
            case R.id.tv_scan_rfid:
                Intent scannerRfid = new Intent(getActivity(), AddCarScannerActivity.class);
                scannerRfid.putExtra("IS_VIN", false);
                startActivityForResult(scannerRfid, REQUEST_SCAN_RFID);
                break;

            case R.id.tv_scan_vin:
                Intent scannerVin = new Intent(getActivity(), AddCarScannerActivity.class);
                scannerVin.putExtra("IS_VIN", true);
                startActivityForResult(scannerVin, REQUEST_SCAN_VIN);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_RFID) {
            // TODO set Scan RFID
            if (resultCode == Activity.RESULT_OK) {
                String scanCode = data.getStringExtra("code");
                int scanCodeLength = scanCode.length();
                Log.e("scanCode", scanCode);
                scanCodeLength = scanCode.length();

                if (scanCodeLength == 7) {
                    MainActivity.searchModel.setRfid(scanCode);
                    edt_rfid.setText(scanCode);
                }

               /* if (data.getBooleanExtra("FOUND", false)) {
                    CarInventory car = (CarInventory) data.getSerializableExtra("each_car");
                    Intent intent = new Intent(getActivity(),CarDetailsActivity.class);
                    intent.putExtra("each_car", car);
                    startActivity(intent);
                }*/
            }
        } else if (requestCode == REQUEST_SCAN_VIN) {
            // TODO set Scan VIN
            if (resultCode == Activity.RESULT_OK) {
                String scanCode = data.getStringExtra("code");
                int scanCodeLength = scanCode.length();
                if (scanCodeLength == 18
                        && (scanCode.startsWith("i") || scanCode
                        .startsWith("I"))) {
                    scanCode = scanCode.substring(1, scanCode.length());
                }

                Log.e("scanCode", scanCode);
                scanCodeLength = scanCode.length();

                if (scanCodeLength == 17) {
                    MainActivity.searchModel.setVin(scanCode);
                    edt_vin.setText(scanCode);
                }

               /* if (data.getBooleanExtra("FOUND", false)) {
                    CarInventory car = (CarInventory) data.getSerializableExtra("each_car");
                    Intent intent = new Intent(getActivity(), CarDetailsActivity.class);
                    intent.putExtra("each_car", car);
                    startActivity(intent);
                }*/
            }
        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        String tag = dialog.getTag();

        if (tag.equals("ENABLE_GPS")) {
            AppSingleTon.METHOD_BOX.startLocationSetting(getActivity());
        } else if (tag.equals("ENABLE_SETTING")) {
            AppSingleTon.METHOD_BOX.startSetting(getActivity());
        }
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {
    }
}