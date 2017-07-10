package com.yukti.driveherenew;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yukti.dataone.model.BasicData;
import com.yukti.dataone.model.Colors;
import com.yukti.dataone.model.Engine;
import com.yukti.dataone.model.ParentNode;
import com.yukti.dataone.model.Query_Error;
import com.yukti.dataone.model.Warranties;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.jsonparser.AddCarResponse;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CamOperation;
import com.yukti.utils.CamOperation.CameraResponse;
import com.yukti.utils.Common;
import com.yukti.utils.RestClient;
import com.yukti.utils.SDCardManager;

public class AddNewCarActivity_old extends BaseActivity implements
		MessageDialogListener, OnClickListener {

	public static final int REQUEST_SCAN_VIN = 1001;
	public static final int REQUEST_SCAN_RFID = 1002;

	RelativeLayout rl_carone, rl_cartwo, rl_carthree;
	ImageView img_carone, img_cartwo, img_carthird;

	private int year;
	private int month;
	private int day;
	private static int click;
	boolean isNeedToScan = true;

	static final int DATE_PICKER_ID = 1111;

	private static EditText edt_inspection_date;
	private static EditText edt_registraion_date;
	private static EditText ed_insurancedate;

	Toolbar toolbar;
	LinearLayout camera, send, show_photo;
	EditText make, model, note, modelNumber, modelYear, vin, rfid, color,
			stage, stockNumber, lotCode, salesPrice, statusOfVehicle,
			purchasedfrom, cylinders, edt_gas_tank, company, miles,
			oilCapacity, driveType, maxHP, maxTorque, fuelType, vehicleType,
			edt_problem, edt_title, edt_location, edt_gps_installed;

	// modelNumber,maxHP,maxTorque,oilCapacity,driveType,company,stockNumber,purchasedfrom,cylinders,vehicleType,note
	TextView cameraTxt, tvScanVin, tvScanRfid;
	CamOperation camOperation = null;
	CameraResponse cameraResponse = null;
	File currentImageFile = null;

	SDCardManager sdcardManager;
	public static ArrayList<File> photoList;
	String scanCode;
	OnEditorActionListener doneListener;
	int REQUEST_SCAN_CODE = 25743;
	int scanCodeLength = 0;
	String TAG_PUSH_RESULT = "TAG_PUSH_RESULT";
	String dataoneInformation = "";

	File companyInsuranceFile = null;

	String TAG_DRIVE_TYPE = "TAG_DRIVE_TYPE";
	String TAG_MAKE = "TAG_MAKE";
	String TAG_MODEL_YEAR = "TAG_MODEL_YEAR";
	String TAG_COLOR = "TAG_COLOR";
	String TAG_PRICE = "TAG_PRICE";
	String TAG_VEHICLE_STATUS = "TAG_VEHICLE_STATUS";
	String TAG_FUEL_TYPE = "TAG_FUEL_TYPE";
	String TAG_STAGE_TYPE = "TAG_STAGE_TYPE";
	String TAG_PROBLEM = "TAG_PROBLEM";
	String TAG_TITLE = "TAG_TITLE";
	String TAG_GPS_NSTALLED = "TAG_GPS_NSTALLED";
	String TAG_GAS_TANK = "TAG_GAS_TANK";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_car);

		toolbar = (Toolbar) findViewById(R.id.activity_add_new_car_app_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		tvScanRfid = (TextView) findViewById(R.id.tv_scan_rfid);
		tvScanVin = (TextView) findViewById(R.id.tv_scan_vin);

		tvScanRfid.setOnClickListener(scanListener);
		tvScanVin.setOnClickListener(scanListener);

		initVin();
		initRFID();
		initCarPhoto();
		initMiles();
		initCompany();
		initMake();
		initModel();
		initModelNumber();
		initModelYear();
		initColor();
		initNote();
		initLotCode();
		initPrice();
		initStockNumber();
		initStatusOfVehicle();
		initPurchasedFrom();
		initOilCapacity();
		initFuelType();
		initDriveType();
		initVehicleType();
		initMaxHP();
		initMaxTorque();
		initCylinders();

		initGasTank();

		initCamera();
		initShowPhoto();

		initSend();
		initDoneListener();

		initStage();
		initProblem();
		initTitle();
		initLocation();

		// initimagetitle();
		// initimagetitle();

		initCompanyInsurance();

		initGPSInstalled();

		initGps();
		initclickdatelistener();

		vin.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 17) {
					Toast.makeText(getBaseContext(), "on text change",
							Toast.LENGTH_LONG).show();
					Log.e("", "on text change");
					pullDataoneInformations();
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			scanCode = bundle.getString("code");
			scanCodeLength = scanCode.length();
			if (scanCodeLength == 18
					&& (scanCode.startsWith("i") || scanCode.startsWith("I"))) {
				scanCode = scanCode.substring(1, scanCode.length());
			}

			Log.e("scanCode", scanCode);
			scanCodeLength = scanCode.length();

			if (scanCodeLength == 17) {
				vin.setText(scanCode);
				// tvScanVin.setVisibility(View.GONE);
			} else if (scanCodeLength == 7) {
				rfid.setText(scanCode);
				// tvScanRfid.setVisibility(View.GONE);
			}
		}

		AppSingleTon.METHOD_BOX.hidekeyBoard(this);
		if (scanCode.length() == 17) {

		}

	}

	Button btn_company_insurance;
	ImageView iv_company_insurance;

	private void initCompanyInsurance() {

		btn_company_insurance = (Button) findViewById(R.id.btn_company_insurance);
		iv_company_insurance = (ImageView) findViewById(R.id.iv_company_insurance);

		btn_company_insurance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// isFromCompanyInsurance =true;
				startCameraIntent(true);
			}
		});

		// companyInsuranceFile
		iv_company_insurance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				RotateImage();

			}
		});

	}

	void initGasTank() {
		edt_gas_tank = (EditText) findViewById(R.id.edt_gas_tank);
		// edt_location = (EditText) findViewById(R.carId.edt_location);
		// edt_location.setText("No");

		final String title = "Select GasTank";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.GasTankList);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				edt_gas_tank.setText(driveTypeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		edt_gas_tank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_GAS_TANK);
			}
		});

	}

	OnClickListener scanListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_scan_rfid:
				// TODO start rfid scan intant for result
				Intent scannerRfid = new Intent(AddNewCarActivity_old.this,
						AddCarScannerActivity.class);
				scannerRfid.putExtra("IS_VIN", false);
				startActivityForResult(scannerRfid, REQUEST_SCAN_RFID);
				break;
			case R.id.tv_scan_vin:
				// TODO start VIN scan intant for result
				Intent scannerVin = new Intent(AddNewCarActivity_old.this,
						AddCarScannerActivity.class);
				scannerVin.putExtra("IS_VIN", true);
				startActivityForResult(scannerVin, REQUEST_SCAN_VIN);

				break;
			default:
				break;
			}

		}
	};

	void initStage() {
		stage = (EditText) findViewById(R.id.stage);

		final String title = "Choose Stage";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.StageType);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				stage.setText(driveTypeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		stage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_STAGE_TYPE);
			}
		});
	}

	void initProblem() {
		edt_problem = (EditText) findViewById(R.id.edt_problem);

		final String title = "Choose Problem";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.Problem);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				edt_problem.setText(driveTypeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		edt_problem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_PROBLEM);
			}
		});
	}

	void initclickdatelistener() {

		edt_inspection_date = (EditText) findViewById(R.id.ed_inspectiondate);
		edt_registraion_date = (EditText) findViewById(R.id.ed_registrationdate);
		ed_insurancedate = (EditText) findViewById(R.id.ed_insurancedate);

		// edt_inspection_date.setInputType(InputType.TYPE_NULL);
		// edt_registraion_date.setInputType(InputType.TYPE_NULL);
		// ed_insurancedate.setInputType(InputType.TYPE_NULL);

		edt_inspection_date.setOnClickListener(this);
		edt_registraion_date.setOnClickListener(this);
		ed_insurancedate.setOnClickListener(this);

		// edt_registraion_date.setEnabled(false);
		// edt_insurancedate.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ed_inspectiondate:
			click = 1;
			showDatePickerDialog(v);
			break;
		case R.id.ed_registrationdate:
			click = 2;
			showDatePickerDialog(v);
			break;
		case R.id.ed_insurancedate:
			click = 3;
			showDatePickerDialog(v);
			break;
		}

	}

	void initTitle() {
		edt_title = (EditText) findViewById(R.id.edt_title);
		edt_title.setText("No");
		final String title = "Choose Title";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.Title);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {

				edt_title.setText(driveTypeList[position]);

			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		edt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_TITLE);
			}
		});
	}

	void initGPSInstalled() {

		edt_gps_installed = (EditText) findViewById(R.id.edt_gps_installed);
		edt_gps_installed.setText("No");

		final String title = "Choose One";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.Title);

		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				edt_gps_installed.setText(driveTypeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		edt_gps_installed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_GPS_NSTALLED);
			}
		});
	}

	void initLocation() {

		edt_location = (EditText) findViewById(R.id.edt_location);

		// edt_location.setText("No");
		final String title = "Choose Location Title";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.location_title);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				edt_location.setText(driveTypeList[position]);

			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		edt_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_TITLE);
			}
		});
	}

	void initCylinders() {
		cylinders = (EditText) findViewById(R.id.cylinders);
	}

	void initMaxTorque() {
		maxTorque = (EditText) findViewById(R.id.maxTorque);
	}

	void initMaxHP() {
		maxHP = (EditText) findViewById(R.id.maxHP);
	}

	void initVehicleType() {
		vehicleType = (EditText) findViewById(R.id.vehicleType);
	}

	void initDriveType() {
		driveType = (EditText) findViewById(R.id.driveType);

		final String title = "Choose Drive Type";
		final CharSequence[] driveTypeList = getResources().getStringArray(
				R.array.DriveType);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				driveType.setText(driveTypeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		driveType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, driveTypeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_DRIVE_TYPE);
			}
		});
	}

	void initFuelType() {

		final String title = "Choose Fuel Type";
		final CharSequence[] statusList = getResources().getStringArray(
				R.array.FuelType);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				fuelType.setText(statusList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}

		};

		fuelType = (EditText) findViewById(R.id.fuelType);
		fuelType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, statusList, listener);
				dialog.show(getSupportFragmentManager(), TAG_FUEL_TYPE);
			}
		});
	}

	void initOilCapacity() {
		oilCapacity = (EditText) findViewById(R.id.oilCapacity);
	}

	void initDoneListener() {

		doneListener = new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					AppSingleTon.METHOD_BOX
							.hidekeyBoard(AddNewCarActivity_old.this);
				}
				return false;
			}
		};
	}

	void initMake() {
		final String title = "Choose Make";
		final CharSequence[] makeList = getResources().getStringArray(
				R.array.Make);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				make.setText(makeList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		make = (EditText) findViewById(R.id.make);
		make.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, makeList, listener);
				dialog.show(getSupportFragmentManager(), TAG_MAKE);
			}
		});
	}

	void initModel() {
		model = (EditText) findViewById(R.id.model);
	}

	void initCompany() {
		company = (EditText) findViewById(R.id.company);
	}

	void initMiles() {
		miles = (EditText) findViewById(R.id.miles);
	}

	void initModelYear() {
		final String title = "Choose Model Year";
		final CharSequence[] yearList = getYearList();

		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				modelYear.setText(yearList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		modelYear = (EditText) findViewById(R.id.modelYear);
		modelYear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, yearList, listener);
				dialog.show(getSupportFragmentManager(), TAG_MODEL_YEAR);
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

	void initModelNumber() {
		modelNumber = (EditText) findViewById(R.id.modelNumber);
	}

	void initNote() {
		note = (EditText) findViewById(R.id.note);
	}

	void initLotCode() {
		// lotCode = (EditText) findViewById(R.carId.lotCode);
		final String title = "Choose Lot Code";
		final CharSequence[] lotList = getResources().getStringArray(
				R.array.Lotcode);
		// final CharSequence[] colorValueList = getResources().getStringArray(
		// R.array.ColorValue);

		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				lotCode.setText(lotList[position]);
				// lotCode.setTag(colorValueList);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		lotCode = (EditText) findViewById(R.id.lotCode);
		// color.setTag("");

		// lotCode.setText("Unknown");
		lotCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * SingleChoiceTextDialogFragment dialog = new
				 * SingleChoiceTextDialogFragment( title,lotList,listener);
				 * dialog.show(getSupportFragmentManager(),TAG_VEHICLE_STATUS);
				 */
				/*
				 * ColorChoiceDialogFragment dialog1 = new
				 * ColorChoiceDialogFragment( listener);
				 * dialog1.show(getSupportFragmentManager(), TAG_COLOR);
				 */
				LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
						listener);
				dialog1.show(getSupportFragmentManager(), TAG_COLOR);

			}
		});

	}

	void initPrice() {
		salesPrice = (EditText) findViewById(R.id.salesPrice);
	}

	void initStockNumber() {
		stockNumber = (EditText) findViewById(R.id.stockNumber);
	}

	void initStatusOfVehicle() {
		final String title = "Choose Vehicle Status";
		final CharSequence[] statusList = getResources().getStringArray(
				R.array.VehicleStatus);
		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				statusOfVehicle.setText(statusList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		statusOfVehicle = (EditText) findViewById(R.id.vehicleStatus);
		statusOfVehicle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
						title, statusList, listener);
				dialog.show(getSupportFragmentManager(), TAG_VEHICLE_STATUS);
			}
		});
	}

	void initPurchasedFrom() {
		purchasedfrom = (EditText) findViewById(R.id.purchasedFrom);
		purchasedfrom.setOnEditorActionListener(doneListener);
	}

	public void initSend() {
		send = (LinearLayout) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// addNewSmart();
				getGps();
				try {
					addNew();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
	}

	void initVin() {
		vin = (EditText) findViewById(R.id.vin);
		/*
		 * vin.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent intent = new
		 * Intent(AddNewCarActivity.this,BarCodeScannerActivity.class);
		 * startActivityForResult(intent, REQUEST_SCAN_CODE); } });
		 */
	}

	void initRFID() {
		rfid = (EditText) findViewById(R.id.rfid);
		/*
		 * rfid.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * Intent intent = new
		 * Intent(AddNewCarActivity.this,BarCodeScannerActivity.class);
		 * startActivityForResult(intent, REQUEST_SCAN_CODE); } });
		 */
	}

	private void initCarPhoto() {

		//
		//
		// rl_carone = (RelativeLayout) findViewById(R.carId.rl_carone);
		// rl_cartwo = (RelativeLayout) findViewById(R.carId.rl_cartwo);
		// rl_carthree = (RelativeLayout) findViewById(R.carId.rl_carthird);
		//
		// img_carone = (ImageView) findViewById(R.carId.img_carone);
		// img_cartwo = (ImageView) findViewById(R.carId.img_cartwo);
		// img_carthird = (ImageView) findViewById(R.carId.img_carthird);
		//
		// rl_carone.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(AddNewCarActivity.this, "carone clicked..",
		// Toast.LENGTH_LONG).show();
		// startCameraIntent(true,false,false);
		//
		// }
		// });
		//
		// rl_cartwo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(AddNewCarActivity.this, "cartwo clicked..",
		// Toast.LENGTH_LONG).show();
		// startCameraIntent(false,true,false);
		// }
		// });
		//
		// rl_carthree.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Toast.makeText(AddNewCarActivity.this, "carthree clicked..",
		// Toast.LENGTH_LONG).show();
		// startCameraIntent(false,false,true);
		// }
		// });
		//
		//
	}

	void initColor() {

		final String title = "Choose Color";
		final CharSequence[] colorNameList = getResources().getStringArray(
				R.array.ColorName);
		final CharSequence[] colorValueList = getResources().getStringArray(
				R.array.ColorValue);

		final ListDialogListener listener = new ListDialogListener() {

			@Override
			public void onItemClick(int position) {
				color.setText(colorNameList[position]);
				color.setTag(colorValueList[position]);
			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};

		color = (EditText) findViewById(R.id.color);
		color.setTag("");
		color.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ColorChoiceDialogFragment dialog = new ColorChoiceDialogFragment(
						listener);
				dialog.show(getSupportFragmentManager(), TAG_COLOR);

				// ColorChoiceDialogFragment dialog = new
				// ColorChoiceDialogFragment(listener);
				// dialog.show(getSupportFragmentManager(), TAG_COLOR);
			}
		});
	}

	void addNew() throws FileNotFoundException {

		String Make = "", Model = "", Note = "", ModelNumber = "", ModelYear = "", Company = "", Miles = "", StockNumber = "", LotCode = "", SalesPrice = "", VehicleStatus = "", PurchasedFrom = "", Vin = "", Rfid = "", Color = "", Cylinders = "", GasTank = "", MaxHP = "", MaxTorque = "", VehicleType = "", DriveType = "", FuelType = "", OilCapacity = "", Stage = "", Title = "", Location_Title = "", Problem = "", Gps_Installed = "";

		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.e("new car add error response", responseString
						+ " statusCode:" + statusCode);
				showToast("new car add  Error");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);

				Log.e("add new ", response.toString());

				AddCarResponse addCarResponse = AppSingleTon.APP_JSON_PARSER.addCarResponse(response.toString());
				if (addCarResponse.status_code.equals("1")) {
					MessageDialogFragment fragment = new MessageDialogFragment(
							"Success", "Car added successfuly in back end.",
							true, "Ok", false, "", false, "",
							AddNewCarActivity_old.this);
					fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
				} else if (addCarResponse.status_code.equals("2")) {

					MessageDialogFragment fragment = new MessageDialogFragment(
							"Info", addCarResponse.message, true, "Ok", false,
							"", false, "", AddNewCarActivity_old.this);
					fragment.show(getSupportFragmentManager(), "");

				} else {
					MessageDialogFragment fragment = new MessageDialogFragment(
							"Failed", "Failed to add in back end.", true, "Ok",
							false, "", false, "", AddNewCarActivity_old.this);
					fragment.show(getSupportFragmentManager(), TAG_PUSH_RESULT);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				showUpdateProgressDialog("Pushing to pending list...");
			}

			@Override
			public void onFinish() {
				super.onFinish();
				dismissProgressDialog();
			}
		};

		String url = AppSingleTon.APP_URL.URL_ADD_VEHICLE_NEW;
		RequestParams params = new RequestParams();

		Vin = vin.getText().toString().trim();
		Rfid = rfid.getText().toString().trim();
		Make = make.getText().toString().trim();
		Model = model.getText().toString().trim();
		Miles = miles.getText().toString().trim();
		Company = company.getText().toString().trim();
		ModelYear = modelYear.getText().toString().trim();
		ModelNumber = modelNumber.getText().toString().trim();
		Note = note.getText().toString().trim();
		SalesPrice = salesPrice.getText().toString().trim();
		PurchasedFrom = purchasedfrom.getText().toString().trim();
		VehicleStatus = statusOfVehicle.getText().toString().trim();
		StockNumber = stockNumber.getText().toString().trim();
		LotCode = lotCode.getText().toString().trim();
		Color = color.getTag().toString().trim();
		VehicleType = vehicleType.getText().toString().trim();
		DriveType = driveType.getText().toString().trim();
		FuelType = fuelType.getText().toString().trim();
		MaxHP = maxHP.getText().toString().trim();
		MaxTorque = maxTorque.getText().toString().trim();
		Cylinders = cylinders.getText().toString().trim();
		GasTank = edt_gas_tank.getText().toString().trim();
		OilCapacity = oilCapacity.getText().toString().trim();
		Stage = stage.getText().toString().trim();
		Problem = edt_problem.getText().toString().trim();

		if (edt_title.getText().toString().trim().equalsIgnoreCase("Blank")) {
			Title = "";
		} else {
			Title = edt_title.getText().toString().trim();
		}

		Gps_Installed = edt_gps_installed.getText().toString().trim();

		Location_Title = edt_location.getText().toString().trim();

		// if (!Vin.equals(""))
		params.put("Vin", Vin);

		// if (!Rfid.equals(""))
		params.put("Rfid", Rfid);

		// if (!Make.equals(""))
		params.put("Make", Make);

		// if (!Model.equals(""))
		params.put("Model", Model);

		// if (!Miles.equals(""))
		params.put("Miles", Miles);

		// if (!Company.equals(""))
		params.put("Company", Company);

		// if (!ModelYear.equals(""))
		params.put("ModelYear", ModelYear);

		// if (!ModelNumber.equals(""))
		params.put("ModelNumber", ModelNumber);

		// if (!Note.equals(""))
		params.put("Note", Note);

		// if (!SalesPrice.equals(""))
		params.put("SalesPrice", SalesPrice);

		// if (!PurchasedFrom.equals(""))
		params.put("PurchasedFrom", PurchasedFrom);

		// if (!VehicleStatus.equals(""))
		params.put("VehicleStatus", VehicleStatus);

		// if (!StockNumber.equals(""))
		params.put("StockNumber", StockNumber);

		// if (!dataoneInformation.equals(""))

		params.put("DataoneInformation",
				Common.Encode_String(dataoneInformation));

		// if (!LotCode.equals(""))
		params.put("LotCode", LotCode);

		// if (!Color.equals(""))
		params.put("Color", Color);

		// if (!VehicleType.equals(""))
		params.put("VehicleType", VehicleType);

		// if (!DriveType.equals(""))
		params.put("DriveType", DriveType);

		// if (!FuelType.equals(""))
		params.put("FuelType", FuelType);

		// if (!MaxHP.equals(""))
		params.put("MaxHP", MaxHP);

		// if (!MaxTorque.equals(""))
		params.put("MaxTorque", MaxTorque);

		// if (!Cylinders.equals(""))
		params.put("Cylinders", Cylinders);

		params.put("gas_tank", GasTank);

		// if (!OilCapacity.equals(""))
		params.put("OilCapacity", OilCapacity);

		// if (!Title.equals(""))
		params.put("Title", Title);

		// if (!Location_Title.equals(""))
		params.put("has_location", Location_Title);

		// if (!Problem.equals(""))
		params.put("Problem", Problem);

		if (companyInsuranceFile != null) {
			Log.e("companyInsuranceFile", companyInsuranceFile.getPath()
					.toString());
			params.put("company_insurance", companyInsuranceFile);
		}

		// if (!stage.equals(""))
		params.put("Stage", Stage);
		params.put("insurancedate", ed_insurancedate.getText().toString());
		params.put("registrationdate", edt_registraion_date.getText()
				.toString());
		params.put("inspectiondate", edt_inspection_date.getText().toString());

		params.put("Gps_Installed", Gps_Installed);

		// TODO
		Log.e("paramput Str", gpsJsonStr);
		params.put("Gps", gpsJsonStr);

		// if (!AppSingleTon.SHARED_PREFERENCE.getUserId().equals(""))
		params.put("UserWhoUploaded",
				AppSingleTon.SHARED_PREFERENCE.getUserId());

		Location location = AppSingleTon.PLAY_MANAGER.getLastLocation();
		if (location != null) {
			params.put("Latitude", location.getLatitude() + "");
			params.put("Longitude", location.getLongitude() + "");
		} else {
			params.put("Latitude", "");
			params.put("Longitude", "");
		}

		if (photoList != null && photoList.size() > 0) {

			params.put("count", photoList.size());

			for (int k = 0; k < photoList.size(); k++) {
				try {
					File file = photoList.get(k);
					params.put("image_" + (k + 1), file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		}
		RestClient.post(this, url, params, handler);

	}

	private boolean isLastPhoto = false;


	public void initCamera() {
		photoList = null;
		photoList = new ArrayList<File>();
		sdcardManager = new SDCardManager();
		camOperation = new CamOperation(this);

		camera = (LinearLayout) findViewById(R.id.camera);
		cameraTxt = (TextView) findViewById(R.id.camera_txt);

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCameraIntent(false);
			}
		});
	}

	void initShowPhoto() {

		show_photo = (LinearLayout) findViewById(R.id.show_photo);

		show_photo.setVisibility(View.INVISIBLE);

		show_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoIntent();
			}
		});
	}

	void showPhotoIntent() {

		if (photoList != null && photoList.size() > 0) {
			Intent showPhotoIntent = new Intent(getApplication(),
					ShowPhotoActivity.class);
			// showPhotoIntent.putExtra("photolist", photoList);
			startActivity(showPhotoIntent);
		}
	}

	public void initimagetitle() {

		// imgtitle = (EditText) findViewById(R.carId.imgtitle);

		// imgtitle.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// startCameraIntent();
		// }
		// });
	}

	public void startCameraIntent(final boolean isFromCompanyInsurance) {

		if (!sdcardManager.isSDCardExists()) {
			showToast("Device don't have any sdcard.");
			return;
		}

		cameraResponse = new CameraResponse() {

			@Override
			public void onNoSdcardFound() {
				showToast("No sdcard found!");
			}

			@Override
			public void onFileCreatingError() {
				showToast("Image file creating error on sdcard!");
			}

			@Override
			public void onCameraReady(File file) {
				currentImageFile = file;
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(file));
				Uri uri = Uri.fromFile(file);
				Log.e("camara uri", "camera uri" + uri);
				startActivityForResult(takePictureIntent,
						CamOperation.ACTION_TAKE_PHOTO);
			}

			@Override
			public void onSuccess() {

				try {

					byte[] photoByte = camOperation
							.fileToByteOne(currentImageFile);

					// Bitmap bitmap =
					// camOperation.fileToBitmap(currentImageFile);

					// openCarImagedialog(bitmap, photoByte);

					FileOutputStream fos = new FileOutputStream(
							currentImageFile);

					fos.write(photoByte);
					fos.flush();
					fos.close();

					if (isFromCompanyInsurance) {

						companyInsuranceFile = currentImageFile;
						iv_company_insurance.setImageBitmap(Common
								.filepathTobitmap(companyInsuranceFile
										.getPath()));
						iv_company_insurance.setVisibility(View.VISIBLE);

					} else {
						photoList.add(currentImageFile);

						cameraTxt.setText("ADD PHOTO" + "(" + photoList.size()
								+ ")");

						show_photo.setVisibility(View.VISIBLE);
					}

					galleryAddPic(currentImageFile);

				} catch (Exception e) {
					Log.e("photo size decrease error",
							"Exception in photo size decrease", e);
				}

			}

			@Override
			public void onFailure() {

				currentImageFile.delete();
				currentImageFile = null;

			}
		};

		if (camOperation.isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE)) {
			camOperation.createESPhotoFile(cameraResponse);
		} else {
			showToast("No Camera intent found on this device");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == CamOperation.ACTION_TAKE_PHOTO)) {

			if (resultCode == Activity.RESULT_OK) {
				cameraResponse.onSuccess();
			} else if (resultCode == Activity.RESULT_CANCELED) {

			}

		} else if ((requestCode == REQUEST_SCAN_CODE)) {

			if (resultCode == Activity.RESULT_OK) {
				String code = data.getStringExtra("code");
				if (code.length() == 17) {
					vin.setText(code);
					pullDataoneInformations();
				} else if (code.length() == 7) {
					rfid.setText(code);
				}
			}
		} else if (requestCode == REQUEST_SCAN_RFID) {
			// TODO set Scan RFID
			if (resultCode == Activity.RESULT_OK) {
				scanCode = data.getStringExtra("code");
				scanCodeLength = scanCode.length();
				if (scanCodeLength == 18
						&& (scanCode.startsWith("i") || scanCode
								.startsWith("I"))) {
					scanCode = scanCode.substring(1, scanCode.length());
				}

				Log.e("scanCode", scanCode);
				scanCodeLength = scanCode.length();

				/*
				 * if (scanCodeLength == 17) { vin.setText(scanCode);
				 * tvScanVin.setVisibility(View.GONE); } else
				 */if (scanCodeLength == 7) {
					rfid.setText(scanCode);
					// tvScanRfid.setVisibility(View.GONE);
				}

				// }

				// AppSingleTon.METHOD_BOX.hidekeyBoard(this);
				// if (scanCode.length() == 17) {
				// pullDataoneInformations();
				// }
				//
				//
				// if (code.length() == 17) {
				// vin.setText(code);
				// pullDataoneInformations();
				// } else if (code.length() == 7) {
				// rfid.setText(code);
				// }
			}
		} else if (requestCode == REQUEST_SCAN_VIN) {
			// TODO set Scan VIN
			if (resultCode == Activity.RESULT_OK) {
				scanCode = data.getStringExtra("code");
				scanCodeLength = scanCode.length();
				if (scanCodeLength == 18
						&& (scanCode.startsWith("i") || scanCode
								.startsWith("I"))) {
					scanCode = scanCode.substring(1, scanCode.length());
				}

				Log.e("scanCode", scanCode);
				scanCodeLength = scanCode.length();

				if (scanCodeLength == 17) {
					vin.setText(scanCode);
					// tvScanVin.setVisibility(View.GONE);
				} /*
				 * else if (scanCodeLength == 7) { rfid.setText(scanCode);
				 * tvScanRfid.setVisibility(View.GONE); }
				 */

				AppSingleTon.METHOD_BOX.hidekeyBoard(this);
				if (scanCode.length() == 17) {
					// pullDataoneInformations();
				}
			}
		}
	}

	private void galleryAddPic(File file) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(file);
		Log.e("content  URI ", "URI" + contentUri);
		mediaScanIntent.setData(contentUri);

		String imagename = (contentUri + "").substring((contentUri + "")
				.lastIndexOf("/") + 1);

		// Log.e("new Uri", "new URI" + imagename);

		// imgtitle.setText("Titleimage_"+ imagename + "");
		this.sendBroadcast(mediaScanIntent);
	}

	@Override
	public void onDialogPositiveClick(MessageDialogFragment dialog) {

		if (dialog.getTag().equals(TAG_PUSH_RESULT)) {
			finish();
		}
	}

	@Override
	public void onDialogNegativeClick(MessageDialogFragment dialog) {

	}

	@Override
	public void onDialogNeutralClick(MessageDialogFragment dialog) {

	}

	private void pullDataoneInformations() {

		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				showToast("Dataone Request Failed.");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.e("response", "dataone response : " + response.toString());
				ParentNode orm = AppSingleTon.APP_JSON_PARSER
						.parseDataoneResponse(response);
				// writeToFile(response.toString());

				Log.e("responsedata" ,response.toString() );
				Query_Error queryError = orm.query_responses.RequestSample.query_error;

				if (!queryError.error_code.equals("")) {
					showToast(queryError.error_message);
					Log.e("queryError.error_message", queryError.error_message);
					return;
				}

				dataoneInformation = response.toString();

				BasicData basicData = orm.query_responses.RequestSample.us_market_data.common_us_data.basic_data;

				if (basicData != null) {

					make.setText(basicData.make);
					model.setText(basicData.model);
					modelNumber.setText(basicData.model_number);
					modelYear.setText(basicData.year);
					vehicleType.setText(basicData.vehicle_type.trim());
					driveType.setText(basicData.drive_type.trim());

					/*
					 * if (!basicData.drive_type.equals("")) {
					 * 
					 * String[] dType = getResources().getStringArray(
					 * R.array.DriveType); for (int i = 0; i <dType.length; i++)
					 * { if (dType[i].equals(basicData.drive_type.trim())) {
					 * driveType.setText(dType[i]); break; } } }
					 */
				}

				ArrayList<Engine> engines = orm.query_responses.RequestSample.us_market_data.common_us_data.engines;

				if (engines != null && engines.size() > 0) {

					Engine engine = engines.get(0);

					maxHP.setText(engine.max_hp);
					maxTorque.setText(engine.max_torque);
					cylinders.setText(engine.cylinders);
					oilCapacity.setText(engine.oil_capacity);

					if (!engine.fuel_type.equals("")) {
						String[] sk = getResources().getStringArray(
								R.array.FuelTypeShortKey);
						String[] ft = getResources().getStringArray(
								R.array.FuelType);
						for (int i = 0; i < sk.length; i++) {
							if (sk[i].equals(engine.fuel_type.trim())) {
								fuelType.setText(ft[i]);
								break;
							}
						}
					}
				}
				Colors colors = orm.query_responses.RequestSample.us_market_data.common_us_data.colors;

				if (colors != null && colors.exterior_colors.size() > 0) {
					String clr = colors.exterior_colors.get(0).generic_color_name;

					final CharSequence[] colorNameList = getResources()
							.getStringArray(R.array.ColorName);
					final CharSequence[] colorValueList = getResources()
							.getStringArray(R.array.ColorValue);

					for (int i = 0; i < colorNameList.length; i++) {
						if (clr.equalsIgnoreCase(colorNameList[i].toString())) {

							color.setText(clr);
							color.setTag(colorValueList[i]);
						}
					}

					//
					// final CharSequence[] colorNameList =
					// getResources().getStringArray(
					// R.array.ColorName);
					// final CharSequence[] colorValueList =
					// getResources().getStringArray(
					// R.array.ColorValue);
					//
					// for (int i = 0; i < colorNameList.length; i++) {
					// if (clr.equals(colorValueList[i])) {
					// color.setText(colorNameList[i]);
					// color.setTag(colorValueList[i]);
					// break;
					// }
					// }
				}

				ArrayList<Warranties> warranties = orm.query_responses.RequestSample.us_market_data.common_us_data.warranties;

				if (warranties != null && warranties.size() > 0) {
					miles.setText(warranties.get(0).miles.trim());
				}

				AppSingleTon.METHOD_BOX
						.hidekeyBoard(AddNewCarActivity_old.this);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				dismissProgressDialog();
			}

			@Override
			public void onStart() {
				super.onStart();
				showUpdateProgressDialog("Getting Dataone suggesstion...");
			}

		};

		RequestParams params = new RequestParams();
		String decoder_query = getQueryParameters();
		params.put("client_id", getString(R.string.dataone_client_id));
		params.put("authorization_code",
				getString(R.string.dataone_authorization_code));
		params.put("decoder_query", decoder_query);

		Log.e("DataoneInfo",params + "");

		RestClient.post(this, AppSingleTon.APP_URL.URL_DATA_ONE, params,
				handler);

	}

	String getQueryParameters() {
		String decoder_query = getString(R.string.decoder_query_json);
		decoder_query = decoder_query.replace("xxxxx", scanCode);
		// Log.d("decoder_query", decoder_query);
		return decoder_query;
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

	private void writeToFile(String data) {
		try {

			File myFile = new File("/sdcard/mysdfile.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			myOutWriter.close();
			fOut.close();
			Toast.makeText(getBaseContext(), "Done writing SD 'mysdfile.txt'",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

	}

	LinearLayout ll_container;
	boolean isFirst;

	void initGps() {
		isFirst = true;
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		addGpsView();
	}

	void addGpsView() {

		final View itemList = AddNewCarActivity_old.this.getLayoutInflater()
				.inflate(R.layout.row_for_gps, ll_container, false);

		// itemList = AddNewCarActivity.this
		// .getLayoutInflater()
		// .inflate(R.layout.row_for_gps,
		// ll_container, false);

		Button btn_plus = (Button) itemList.findViewById(R.id.btn_plus);
		btn_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplication(), "Clicked on plus",
				// Toast.LENGTH_SHORT).show();
				addGpsView();
			}
		});

		Button btn_minus = (Button) itemList.findViewById(R.id.btn_minus);
		btn_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplication(), "Clicked on minus",
				// Toast.LENGTH_SHORT).show();

				((LinearLayout) itemList.getParent()).removeView(itemList);
				// containerlayout.removeView(itemList.getTag());

			}
		});

		if (isFirst) {

			isFirst = false;

		} else {

			btn_plus.setVisibility(View.GONE);
			btn_minus.setVisibility(View.VISIBLE);

		}

		ll_container.addView(itemList);

	}

	EditText edt_gps;
	ArrayList<String> arrayList_gps;
	String gpsJsonStr = "";

	void getGps() {

		arrayList_gps = new ArrayList<String>();
		int Childcount = ll_container.getChildCount();

		for (int i = 0; i < Childcount; i++) {

			View itemList = ll_container.getChildAt(i);
			edt_gps = (EditText) itemList.findViewById(R.id.ed_gps);
			String gpsStr = edt_gps.getText().toString().trim();
			arrayList_gps.add(gpsStr);

		}

		if (arrayList_gps != null && arrayList_gps.size() > 0) {
			gpsJsonStr = getGpsJsonString();
			Log.e("gpsJsonStr", gpsJsonStr);
		}
	}

	public String getGpsJsonString() {

		JSONObject object = new JSONObject();

		JSONArray jArray = new JSONArray();

		try {

			for (int i = 0; i < arrayList_gps.size(); i++) {
				JSONObject Obj = new JSONObject();
				Obj.put("carId", "-1");
				Obj.put("gpsid", arrayList_gps.get(i));
				jArray.put(Obj);
			}
			object.put("Gps", jArray);

		} catch (Exception e) {
			// Log.e("Error in getGpsJsonString() :" + e.toString());
		}
		// Log.e("Item To ADD:  " + object.toString());

		return object.toString();

	}

	int angle = 0;

	public void openCarImagedialog(final Bitmap photo, final byte[] photoByte) {

		final Dialog settingsDialog;

		settingsDialog = new Dialog(AddNewCarActivity_old.this);
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		View v = getLayoutInflater().inflate(R.layout.customimagedialog, null);
		final ImageView selectedimage = (ImageView) v
				.findViewById(R.id.selectedimg);
		Button btnrotet = (Button) v.findViewById(R.id.btn_rotet);
		Button btnok = (Button) v.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
		selectedimage.setImageBitmap(photo);
		settingsDialog.setContentView(v);

		btnok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				FileOutputStream fos;
				try {
					fos = new FileOutputStream(currentImageFile);
					fos.write(photoByte);
					fos.flush();
					fos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				photoList.add(currentImageFile);
				// cameraTxt.setText("ADD PHOTO" + "(" + photoList.size()
				// + ")");
				galleryAddPic(currentImageFile);
				img_carone.setVisibility(View.VISIBLE);
				img_carone.setImageBitmap(camOperation
						.fileToBitmap(currentImageFile));

				settingsDialog.cancel();
			}
		});

		btnrotet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Matrix matrix = new Matrix();

				// selectedimage.setScaleType(ScaleType.FIT_XY);
				angle = angle + 90;
				matrix.postRotate(angle, selectedimage.getDrawable()
						.getBounds().width() / 2, selectedimage.getDrawable()
						.getBounds().height() / 2);
				selectedimage.setRotation(angle);

				Bitmap bMapRotate = Bitmap.createBitmap(photo, 0, 0,
						selectedimage.getDrawable().getBounds().width(),
						selectedimage.getDrawable().getBounds().height(),
						matrix, true);

			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		settingsDialog.show();

	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			DecimalFormat formatter = new DecimalFormat("00");
			String date = String.valueOf(year) + "-"
					+ String.valueOf(formatter.format(month + 1)) + "-"
					+ String.valueOf(formatter.format(day));
			if (click == 1) {
				edt_inspection_date.setText(date);
			}

			else if (click == 2) {
				edt_registraion_date.setText(date);
			} else {
				// String.valueOf(year)
				//
				// String.valueOf(formatter.format(month + 1))
				//
				// String.valueOf(formatter.format(day))
				ed_insurancedate.setText(date);

			}
		}

	}

	Dialog dialog;
	ImageView dialogImage;
	Button RotateButton;
	Button DoneButton;
	int bitmapWidth;
	int bitmapHeight;

	public void RotateImage() {

		dialog = new Dialog(AddNewCarActivity_old.this);
		dialog.setTitle("Choose Action");
		@SuppressWarnings("static-access")
		LayoutInflater inflater = getLayoutInflater().from(
				getApplicationContext());

		View view = inflater.inflate(R.layout.activity_custom_dialog, null);

		dialog.setContentView(view);
		// dialog.setCancelable(false);
		dialogImage = (ImageView) view.findViewById(R.id.selectedImage);
		RotateButton = (Button) view.findViewById(R.id.RotateButton);
		DoneButton = (Button) view.findViewById(R.id.DoneButton);
		dialogImage.setImageBitmap(Common.filepathTobitmap(companyInsuranceFile
				.getPath()));

		DoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (resizedBitmap != null) {

					// ImageView img = (ImageView) ll_container
					// .findViewWithTag(pos);
					iv_company_insurance.setImageBitmap(resizedBitmap);

					File file;
					file = Common.bitmapToFilePath(resizedBitmap);

					companyInsuranceFile = file;
					// if (file != null) {
					// AddNewCarActivity.photoList.set(pos, file);
					// Log.e("rotate image"+ pos, file.getPath());
					// }
				}

				dialog.cancel();

			}
		});
		RotateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bitmap dialog_imageBitmap = ((BitmapDrawable) dialogImage
						.getDrawable()).getBitmap();
				bitmapWidth = dialog_imageBitmap.getWidth();
				bitmapHeight = dialog_imageBitmap.getHeight();
				drawMatrix(dialog_imageBitmap);

			}
		});
		dialog.show();
	}

	Bitmap resizedBitmap = null;

	private void drawMatrix(Bitmap dialog_imageBitmap) {
		Matrix matrix = new Matrix();
		matrix.preRotate(90);
		resizedBitmap = Bitmap.createBitmap(dialog_imageBitmap, 0, 0,
				bitmapWidth, bitmapHeight, matrix, true);
		BitmapDrawable b = new BitmapDrawable(resizedBitmap);
		dialogImage.setImageBitmap(resizedBitmap);
		// dialogImage.setRotation(angle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// showToast("destory");
		// photoList = null;
	}

}
