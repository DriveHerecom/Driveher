package com.yukti.driveherenew;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FEATURE_LIST;
import com.yukti.database.SequrityClockDatabase;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.driveherenew.PassConfirmDialougueFrag.PassDialogListener;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.driveherenew.fragment.NavigationDrawerFragment;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.driveherenew.search.OnLoadMoreListener;
import com.yukti.driveherenew.search.Search;
import com.yukti.facerecognization.localdatabase.SettingStore;
import com.yukti.jsonparser.AppJsonParser;
import com.yukti.jsonparser.Login;
import com.yukti.location.PlayConstants;
import com.yukti.location.PlayManager;
import com.yukti.location.PlayManager.PlayCallback;
import com.yukti.newchanges.Activity.SearchActivity;
import com.yukti.newchanges.util.SearchModel;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.AppUrl;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.DrawerItem;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;
import com.yukti.utils.VolleySingleton;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements MessageDialogListener, NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static final int MAIN_ACTIVITY_REQUEST_ACCESS_FINE_LOCATION = 111;
    public static final int MAIN_ACTIVITY_REQUEST_CAMERA = 112;
    public static final int REQUEST_CAMERA = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int REQ_ADD_UPDATE_FACE = 1020;
    public static final int REQUEST_ACTION_FILTER = 1000;
    public static final int REQUEST_ACTION_EDIT = 1015;
    public static final int REQUEST_ACTION_SEARCH = 2012;
    public static SearchModel searchModel;
    public static int threshhold = 3;
    public static boolean isneeded = false;
    public static boolean Addnewcar = false;
    static LinearLayoutManager linearLayoutManager;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    protected Handler handler;
    LinearLayout search;
    String TAG_ENABLE_GPS = "ENABLE_GPS";
    String TAG_ENABLE_SETTING = "ENABLE_SETTING";
    String TAG_CONFIRM_PASS = "TAG_CONFIRM_PASS";
    Context mContext;
    SettingStore settingStore;
    SequrityClockDatabase scdb;
    int version;
    LinearLayout loadMoreLayout;
    CustomAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    LinearLayout ll_searchProgress;
    int totalSearchItemFound = 0;
    int page = 0;
    boolean isSearching = false;
    ArrayList<CarInventory> carList = null;
    int totalPages, currentPage;
    private Uri fileUri;
    private NavigationDrawerFragment navigationDrawerFragment;
    private List<DrawerItem> drawerItemList;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.activity_main_app_bar);
        setSupportActionBar(toolbar);
        mContext = this;

//        Crittercism.initialize(getApplicationContext(), "56a8c16eb23c2c0f005886d3");
        registerGCM();
        scdb = new SequrityClockDatabase(MainActivity.this);
        settingStore = new SettingStore(getApplicationContext());

        ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
        loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
        initRecyclerView();

        if (AppSingleTon.isNetConnection()) {
            seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You need to switch on Internet to access application.");
            alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    try {
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });

            alertDialogBuilder.setTitle("Device Internet is Off");
            alertDialogBuilder.setCancelable(false);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        recyclerView.setHasFixedSize(true);
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerItemList = new ArrayList<DrawerItem>();

        searchModel = new SearchModel();

        initFab();

        if (Common.isNetworkConnected(getApplicationContext())) {
            if (!Constants.isGoldstarLogin) {
                getCredentials();
            }
        }

        AppSingleTon.PLAY_MANAGER = new PlayManager(MainActivity.this);
        connectPlayManager();

        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        version = info.versionCode;
        if (settingStore.getPRE_ServerApp_Version() > version) {
            updateVersionDialog();
        } else if (Common.isNetworkConnected(getApplicationContext())) {
            checkVersionUpdate();
        }

        new setDrawer().execute("");
    }

    void initFab() {
        FloatingActionButton fab_filter = (FloatingActionButton) findViewById(R.id.fab_filter);
        fab_filter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isNetworkConnected(getApplicationContext())) {
                    startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), REQUEST_ACTION_FILTER);
                } else {
                    Toast.makeText(getApplicationContext(), "Please turn on internet connection",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void checkVersionUpdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_CHECK_APPVERSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    AppJsonParser parser = new AppJsonParser();
                    ResVersionModel resVersionModel = parser.getVerResponse(response);
                    if (resVersionModel.status_code != 0) {
                        settingStore.setPRE_ServerApp_Version(resVersionModel.current_version_code);
                        if (settingStore.getPRE_ServerApp_Version() > version) {
                            updateVersionDialog();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_VERSIONTYPE, Constants.APPLICATION_NAME);
                params.put(ParamsKey.KEY_VERSIONCODE, version + "");
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


    void scannerClicked() {

        CharSequence[] items = new String[2];
        items[0] = "INPUT NUMBER";
        items[1] = "SCAN";

        SingleChoiceTextDialogFragment singleList = new SingleChoiceTextDialogFragment("CHOOSE METHOD", items, new ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this,
                            InputScanActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this,
                            VinScannerActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        });
        singleList.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PlayConstants.PLAY_CONNECTION_FAILURE_RESOLUTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                connectPlayManager();
            } else if (resultCode == RESULT_CANCELED) {
                showToast("PLAY_CONNECTION_FAILURE_RESOLUTION_REQUEST failed");
            }
        } else if (requestCode == PlayConstants.PLAY_RESOLUTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                connectPlayManager();
            } else if (resultCode == RESULT_CANCELED) {
                showToast("PLAY_RESOLUTION_REQUEST failed");
            }
        } else if ((requestCode == REQUEST_ACTION_SEARCH)) {

            if (resultCode == RESULT_OK) {
                boolean isUpdate = data.getBooleanExtra("isUpdate", false);
                if (isUpdate) {
                    CarInventory carx = (CarInventory) data.getExtras().getSerializable("each_car");
                    int itemPosition = data.getIntExtra("itemPosition", 0);
                    carList.remove(itemPosition);
                    carList.add(itemPosition, carx);
                    adapter.notifyItemChanged(itemPosition);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        } else if ((requestCode == REQUEST_ACTION_FILTER)) {
            if (resultCode == RESULT_OK) {
                clearRecyclerView();
                ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
                loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
//                errorText = (TextView) findViewById(R.carId.errorText);
                initRecyclerView();
                seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
                recyclerView.setHasFixedSize(true);
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        } else if ((requestCode == REQUEST_ACTION_EDIT)) {
            if (resultCode == Activity.RESULT_OK) {
                clearRecyclerView();
                ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
                loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
//                errorText = (TextView) findViewById(R.carId.errorText);
                initRecyclerView();
                seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
                recyclerView.setHasFixedSize(true);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    void getLocation() {
        if (AppSingleTon.PLAY_MANAGER.isPlayAvailable()) {
            AppSingleTon.PLAY_MANAGER.tryToConnect(new PlayCallback() {
                @Override
                public void onGoogleApiClientReady() {
                    AppSingleTon.PLAY_MANAGER.startLocationListening(MainActivity.this);
                }

                @Override
                public void onGoogleApiClientConnectionFail(ConnectionResult result) {
                    AppSingleTon.PLAY_MANAGER.startConnectionFailResolution(result);
                }
            });
        } else {
            AppSingleTon.PLAY_MANAGER.startNoPlayResolution();
        }
    }

    void connectPlayManager() {
        if (AppSingleTon.VERSION_OS.checkVersion()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("");
                    builder.setMessage("App Needed To Access Device Location, Allow It?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MAIN_ACTIVITY_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    });
                    builder.show();
                }
            }
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MAIN_ACTIVITY_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Location Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == MAIN_ACTIVITY_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scannerClicked();
            } else {
                Toast.makeText(getApplicationContext(), "Camera Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    ///////////////////// Drawer Implementtaion
    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {

        String tag = dialog.getTag();

        if (tag.equals(TAG_ENABLE_GPS)) {
            AppSingleTon.METHOD_BOX.startLocationSetting(MainActivity.this);
        } else if (tag.equals(TAG_ENABLE_SETTING)) {
            AppSingleTon.METHOD_BOX.startSetting(MainActivity.this);
        }
    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSingleTon.PLAY_MANAGER.stopListeningAndDisconnect();
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    ///////////////////////////  For Search Filter
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_update_face) {
            boolean isSupported = FacialProcessing.isFeatureSupported(FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
            boolean hasFroncamera = Common.isFrontCameraAvailable();
            if (isSupported && hasFroncamera) {
                PassConfirmDialogue();
            } else {
                doesnotSupportFaceRecg();
            }
            return true;
        }

//        if (carId == R.carId.action_profile) {
//            Intent intent = new Intent(MainActivity.this,
//                    ProfileActivity.class);
//            startActivity(intent);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    void PassConfirmDialogue() {
        final PassDialogListener listener = new PassDialogListener() {

            @Override
            public void onOkClick(String pass) {
                if (!pass.equalsIgnoreCase("")) {
                    if (pass.equalsIgnoreCase(AppSingleTon.SHARED_PREFERENCE.getPassword())) {
                        addORupdateFace();
                    } else {
                        Toast.makeText(getApplication(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        PassConfirmDialougueFrag dialog = new PassConfirmDialougueFrag("Enter Your Password", "msg", listener);
        dialog.show(getSupportFragmentManager(), TAG_CONFIRM_PASS);
    }

    protected void addORupdateFace() {
        Intent intent = new Intent(MainActivity.this, com.yukti.facerecognization.FacialRecognitionActivity.class);
        intent.putExtra("isaddPhoto", true);
        intent.putExtra("isFromEditCar", true);
        startActivityForResult(intent, REQ_ADD_UPDATE_FACE);
    }

    void doesnotSupportFaceRecg() {
        Log.e("MainActivty", "Feature Facial Recognition is NOT supported");
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Your device does NOT support Qualcomm's Facial Recognition feature. ")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    private void registerGCM() {

        try {
            GCMRegistrar.checkDevice(this);
            GCMRegistrar.checkManifest(this);

            final String regId = GCMRegistrar.getRegistrationId(this);

            if (regId == null || regId.equals("")) {
                GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
                String regId1 = GCMRegistrar.getRegistrationId(this);
                registerInBackground(regId1);
            } else {
                if (!GCMRegistrar.isRegisteredOnServer(this)) {
                    registerInBackground(regId);
                } else {
                }
            }
        } catch (RuntimeException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void registerInBackground(String regId) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Login orm = AppSingleTon.APP_JSON_PARSER.login(response);
                    if (orm.status_code.equals("1")) {
                        GCMRegistrar.setRegisteredOnServer(getApplication(), true);
                    } else if (orm.status_code.equals("2")) {
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };

        String url = AppSingleTon.APP_URL.URL_REG_ID;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_USERID, AppSingleTon.SHARED_PREFERENCE.getUserId());
        params.put(ParamsKey.KEY_GCMID, regId);
        RestClient.post(this, url, params, handler);
    }

    void getCredentials() {
        final ProgressDialog pdial = new ProgressDialog(MainActivity.this);
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("get Report", " error response" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e("Response", response.toString());
                    JSONObject data = response.getJSONObject("data");
                    String token = data.getString("token");
                    logedin(token, "developer1");
                    Constants.isGoldstarLogin = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };
        String loginUrl = AppUrl.LOGIN_URL + "username=" + "developer1" + "&password=" + "1drivehere" + "&format=json";
        RequestParams params = new RequestParams();
        RestClient.post(this, loginUrl, params, handler);
    }

    private void logedin(final String token, final String username) {

        final ProgressDialog pdial = new ProgressDialog(MainActivity.this);
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        };

        String url = AppUrl.SESSION_URL;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_AUTHONTICATIONTOKEN, token);
        params.put(ParamsKey.KEY_AUTHONTICATIONUSERNAME, username);
        RestClient.post(this, url, params, handler);
    }

    public void updateVersionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Update Available In Playstore");
        builder.setMessage("Need To Update the Application.");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?carId=com.yukti.drivehere"));
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else {
                super.onBackPressed();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, boolean isDrawerAction, boolean isopen) {

        if (!isDrawerAction) {
            selectedItem(position);
        } else if (isopen) {
        } else {
        }
    }

    public void selectedItem(int position) {
        // TODO Auto-generated method stub

        if (position == 0) {
            searchModel = new SearchModel();
            clearRecyclerView();
            ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
            loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
            initRecyclerView();
            seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
            recyclerView.setHasFixedSize(true);
//           startActivity(new Intent(MainActivity.this, SimpleTabsActivity.class));
        } else if (position == 1) {
            if (Common.isNetworkConnected(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this,
                        AddNewCarDetailsActivity.class);
                Addnewcar = true;
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please Connect To Internet", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (position == 2) {
            if (Common.isNetworkConnected(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this,
                        AddNewCarDetailsActivity.class);
                Addnewcar = false;
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please Connect To Internet", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (position == 3) {
            if (Common.isNetworkConnected(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this,
                        ReportActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please Connect To Internet", Toast.LENGTH_LONG)
                        .show();
            }
        } else if (position == 4) {
            if (Common.isNetworkConnected(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this,
                        StageActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(),
                        "Please Connect To Internet", Toast.LENGTH_LONG)
                        .show();
        } else if (position == 5) {
            Uri uri = Uri.parse("http://drivehere.com/admin/graph/demos/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (position == 6) {
            if (Common.isNetworkConnected(getApplicationContext())) {
                Intent intent = new Intent(MainActivity.this, AuctionActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(),
                        "Please Connect To Internet", Toast.LENGTH_LONG)
                        .show();
        } else if (position == 7) {
            Intent intent = new Intent(MainActivity.this, PastContractActivity.class);
            startActivity(intent);
        } else if (position == 8) {
            AppSingleTon.SHARED_PREFERENCE.logout();
            Intent intent = new Intent(MainActivity.this, com.yukti.driveherenew.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {

        }
    }

    void clearRecyclerView() {
        totalPages = 0;
        currentPage = 0;
        handler = null;
        lastVisibleItem = 0;
        totalItemCount = 0;
        if (carList != null)
            carList.clear();
        threshhold = 3;
        isSearching = false;
        page = 0;
        totalSearchItemFound = 0;
        adapter = null;
        loading = false;
    }

    void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    void showHideRecyclerView(boolean flag) {
        if (flag)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    public void addNewData(ArrayList<CarInventory> carInventories) {
        carList.remove(carList.size() - 1);
        adapter.notifyItemRemoved(carList.size());
        for (int i = 0; i < carInventories.size(); i++) {
            carList.add(carInventories.get(i));
            adapter.notifyItemInserted(carList.size());
        }
        adapter.setLoaded();
    }

    void setAdapter() {
        if (adapter == null) {
            adapter = new MainActivity.CustomAdapter(carList);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (currentPage < totalPages) {
                        carList.add(null);
                        adapter.notifyItemInserted(carList.size() - 1);
                        currentPage++;
                        seachUsingVolley(com.yukti.utils.Constants.limit, currentPage, false);
                    }
                }
            });
        } else {
        }
    }

    void seachUsingVolley(final int limit, final int pageNumber, final boolean isFirst) {
        isSearching = true;
        if (isFirst) {
            ll_searchProgress.setVisibility(View.VISIBLE);
        } else {
            ll_searchProgress.setVisibility(View.GONE);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_ALL_CAR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        TextView errorText = (TextView) findViewById(R.id.errorText);
                        isSearching = false;
                        ll_searchProgress.setVisibility(View.GONE);

                        try {
                            Search search = AppSingleTon.APP_JSON_PARSER.search(response);
                            if (search.status_code.equalsIgnoreCase("1")) {
                                if (search.count.equals("0")) {
                                    recyclerView.setVisibility(View.GONE);
                                    errorText.setVisibility(View.VISIBLE);
                                    getSupportActionBar().setTitle("Cars");
                                    errorText.setText("Found Nothing!");
                                    return;
                                }

                                if (search.cars != null && search.cars.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    errorText.setVisibility(View.GONE);
                                    if (carList != null && carList.size() > 0) {
                                        addNewData(search.cars);
                                    } else {
                                        currentPage = 1;
                                        carList = search.cars;
                                        setAdapter();
                                        getSupportActionBar().setTitle("Cars (" + search.count + ")");

                                        showHideRecyclerView(true);
                                        totalPages = (Integer.parseInt(search.count) / com.yukti.utils.Constants.limit);
                                        if (totalPages > 0 && (Integer.parseInt(search.count) % com.yukti.utils.Constants.limit) > 0)
                                            totalPages++;
                                    }
                                } else {
                                }
                            } else if (search.status_code.equalsIgnoreCase("0")) {
                                Toast.makeText(MainActivity.this, search.message + " ", Toast.LENGTH_SHORT).show();
                            }else if (search.status_code.equalsIgnoreCase("2")) {
                                recyclerView.setVisibility(View.GONE);
                                errorText.setVisibility(View.VISIBLE);
                                getSupportActionBar().setTitle("Cars");
                                errorText.setText("Found Nothing!");
                            }
                            else if (search.status_code.equalsIgnoreCase("4")) {
                                Toast.makeText(MainActivity.this, "" + search.message, Toast.LENGTH_SHORT).show();
                                AppSingleTon.logOut(MainActivity.this);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            CommonUtils.showAlertDialog(MainActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    if (Common.isNetworkConnected(getApplicationContext()))
                                        seachUsingVolley(limit, pageNumber, true);
                                    else
                                        Toast.makeText(getApplicationContext(),Constant.ERR_INTERNET , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isSearching = false;
                ll_searchProgress.setVisibility(View.GONE);
                showTryAgainAlert("Info", "Network error, Please try again!", limit, pageNumber);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_vin, searchModel.getVin());
                params.put(ParamsKey.KEY_rfid, searchModel.getRfid());
                params.put(ParamsKey.KEY_make, searchModel.getMake());
                params.put(ParamsKey.KEY_modelNumber, searchModel.getCdModelNumber());
                params.put(ParamsKey.KEY_miles, searchModel.getCdMiles());
                params.put(ParamsKey.KEY_modelYear, searchModel.getModelYear());
                params.put(ParamsKey.KEY_vehicleStatus, searchModel.getVehStatus());
                params.put(ParamsKey.KEY_vehicleStage, searchModel.getVehStage());
                params.put(ParamsKey.KEY_serviceStage, searchModel.getSerStage());
                params.put(ParamsKey.KEY_FUEL, searchModel.getCdFuelType());
                params.put(ParamsKey.KEY_problem, searchModel.getSerProblem());
                params.put(ParamsKey.KEY_doneDateLotCode, searchModel.getSerDoneDateLotcode());
                params.put(ParamsKey.KEY_hasTitle, searchModel.getCdTitle());
                params.put(ParamsKey.KEY_locationTitle, searchModel.getCdLocation());
                params.put(ParamsKey.KEY_modelYear, searchModel.getModelYear());
                params.put(ParamsKey.KEY_driveType, searchModel.getCdDriveType());
                params.put(ParamsKey.KEY_company, searchModel.getCdCompany());
                params.put(ParamsKey.KEY_lotCode, searchModel.getLotCode());
                params.put(ParamsKey.KEY_stockNumber, searchModel.getCdStocknumber());
                params.put(ParamsKey.KEY_purchaseFrom, searchModel.getCdPurchasedFrom());
                params.put(ParamsKey.KEY_cylinder, searchModel.getCdCylinder());
                params.put(ParamsKey.KEY_vehicleType, searchModel.getCdVehicleType());
                params.put(ParamsKey.KEY_note, searchModel.getCdNote());
                params.put(ParamsKey.KEY_noteDate, searchModel.getCdNoteDate());
                params.put(ParamsKey.KEY_auctionName, searchModel.getAuctionName());
                params.put(ParamsKey.KEY_doneDate, searchModel.getSerDoneDate());
                params.put(ParamsKey.KEY_gpsInstalled, searchModel.getCdGpsInstalled());
                params.put(ParamsKey.KEY_auctionDate, searchModel.getAuctionDate());
                params.put(ParamsKey.KEY_carReadyForAuction, searchModel.getCarReadyForAuction());
                params.put(ParamsKey.KEY_carAtAuction, searchModel.getCarAtAuction());
                params.put(ParamsKey.KEY_mechanic, searchModel.getSerMechanic());
                params.put(ParamsKey.KEY_vacancy, searchModel.getVehVacancy());
                params.put(ParamsKey.KEY_hasRfid, searchModel.getVehHasRfid());
                params.put(ParamsKey.KEY_inOnePage, limit + "");
                params.put(ParamsKey.KEY_pageNo, pageNumber + "");
                params.put(ParamsKey.KEY_type, Constant.APP_TYPE);
                params.put(ParamsKey.KEY_userId, AppSingleTon.SHARED_PREFERENCE.getUserId());
                params.put(ParamsKey.KEY_gasTank, searchModel.getCdGasTank());
                params.put(ParamsKey.KEY_maxHP, searchModel.getCdMaxHP());
                params.put(ParamsKey.KEY_color, searchModel.getCdColor());
                params.put(ParamsKey.KEY_price, searchModel.getCdSalesPriceMin());
                params.put(ParamsKey.KEY_purchasedFrom, searchModel.getCdPurchasedFrom());
                Log.e("Param", params.toString() + " ");
                return params;
            }
        };

        stringRequest.setTag(com.yukti.utils.Constants.REQUEST_TAG);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                com.yukti.utils.Constants.VOLLEY_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void showTryAgainAlert(String title, String message, final int limit, final int pageNumber) {
        CommonUtils.showAlertWithNegativeButton(MainActivity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (Common.isNetworkConnected(getApplicationContext()))
                    seachUsingVolley(limit, pageNumber, true);
                else
                    Toast.makeText(getApplicationContext(), "Please Connect To Internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_searchProgress.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().
                    cancelAll(com.yukti.utils.Constants.REQUEST_TAG);
        }
    }

    public class setDrawer extends AsyncTask<String, Integer, String> {
        ProgressDialog PDialog;

        public setDrawer() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
            PDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            DrawerItem drawerItem = new DrawerItem(Constant.ALL_CARS, R.drawable.ic_allcarlist, Constant.DESC_ALL_CARS);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.ADD_CAR, R.drawable.ic_addcar, Constant.DESC_ADD_CAR);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.SCANCHECK, R.drawable.ic_scancheck, Constant.DESC_SCANCHECK);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.REPORT, R.drawable.ic_report, Constant.DESC_REPORT);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.STAGEGRAPH, R.drawable.ic_stagegraph, Constant.DESC_STAGEGRAPH);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.GAUGE_CLOCK, R.drawable.ic_gaugeclock, Constant.DESC_GAUGE_CLOCK);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.AUCTION, R.drawable.ic_auction, Constant.DESC_AUCTION);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.PAST_CONTRACT, R.drawable.ic_searchcontract, Constant.DESC_PAST_CONTRACT);
            drawerItemList.add(drawerItem);

            drawerItem = new DrawerItem(Constant.LOG_OUT, R.drawable.active_logout, Constant.DESC_LOGOUT);
            drawerItemList.add(drawerItem);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PDialog.dismiss();
            navigationDrawerFragment.setUp(R.id.fragment_navigation_drawer, drawer, toolbar, drawerItemList);

        }
    }

    @SuppressLint("ResourceAsColor")
    public class CustomAdapter extends RecyclerView.Adapter implements OnClickListener {
        private static final String TAG = "CustomAdapter";
        public List<CarInventory> items;
        LayoutInflater inflater;
        private OnLoadMoreListener onLoadMoreListener;

        public CustomAdapter(List<CarInventory> items) {
            this.items = items;
            inflater = LayoutInflater.from(MainActivity.this);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + threshhold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view.
            RecyclerView.ViewHolder vh;
            if (viewType == VIEW_ITEM) {
                View v = inflater.inflate(R.layout.row_search_result, viewGroup, false);
                v.setOnClickListener(this);
                vh = new ItemViewHolder(v);
            } else {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progress_layout, viewGroup, false);
                vh = new ProgressViewHolder(v);
            }
            return vh;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder_main, final int position) {
            if (viewHolder_main instanceof MainActivity.CustomAdapter.ItemViewHolder) {
                ItemViewHolder viewHolder = (ItemViewHolder) viewHolder_main;
                final CarInventory car = items.get(position);
                if (car.imagePath != null && car.imagePath.length() > 0) {
                    viewHolder.networkImageView.setVisibility(View.VISIBLE);
                    viewHolder.networkImageView.setImageUrl(
                            car.imagePath, VolleySingleton
                                    .getInstance(MainActivity.this)
                                    .getImageLoader());

                } else {
                    viewHolder.networkImageView.setVisibility(View.INVISIBLE);
                    viewHolder.img_previewLayout.setBackground(getResources().getDrawable(R.drawable.ic_default_car));
                }

                if (car.vin.length() == 17) {
                    viewHolder.vin_last_eight.setText(".." + car.vin.substring(9, car.vin.length()));
                } else {
                    viewHolder.vin_last_eight.setText("N/A");
                }

                if (car.rfid != null && car.rfid.length() > 3) {
                    viewHolder.tv_rfid.setText(car.rfid);
                } else {
                    viewHolder.tv_rfid.setText("N/A");
                }

                if (car.stage != null && car.stage.length() > 0) {
                    viewHolder.tv_carStage.setText(car.stage);
                } else {
                    viewHolder.tv_carStage.setText("N/A");
                }

                viewHolder.title.setText((car.modelYear + " " + car.make + " " + car.model + " " + car.modelNumber).replaceAll("null", ""));

                if (car.price != null && car.price.length() > 0) {
                    viewHolder.subtitle.setText(car.price + " $");
                } else {
                    viewHolder.subtitle.setText("N/A");
                }

                viewHolder.oneOne.setText(car.stage);

                if (car.note != null && car.note.length() > 0) {
                    viewHolder.twoTwo.setText(car.note);
                } else {
                    viewHolder.twoTwo.setText("N/A");
                }

                viewHolder.iv_editCar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddNewCarActivity.class);
                        intent.putExtra("redirect", true);
                        intent.putExtra("carVin", car.vin);
                        Log.e("vin", "" + car.vin);
                        startActivityForResult(intent, REQUEST_ACTION_EDIT);
                    }
                });

                if (car.imageCount != null)
                    viewHolder.totalPhoto.setText(car.imageCount + "");
                else
                    viewHolder.totalPhoto.setText("0");

                if (car.Title != null && car.Title.length() > 0) {
                    if (car.Title.equalsIgnoreCase("yes")) {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#338033"));
                    } else {
                        viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#000000"));
                    }
                }

                if (car.lotCode == null || car.lotCode.equalsIgnoreCase("")) {
                    viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
                    viewHolder.tv_lotcode.setText(Constant.KEY_NOT_AVAILABLE);
                } else {
                    viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
                    viewHolder.tv_lotcode.setText(car.lotCode);
                }
            } else {
                ((ProgressViewHolder) viewHolder_main).progressBar.setIndeterminate(true);
            }
        }

        public void setLoaded() {
            loading = false;
        }

        @Override
        public int getItemViewType(int position) {
            return carList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void add(CarInventory item, int position) {
            items.add(position, item);
            notifyItemInserted(position);
        }

        public void add(ArrayList<CarInventory> elements) {
            for (int i = 0; i < elements.size(); i++) {
                items.add(elements.get(i));
                int position = items.indexOf(elements.get(i));
                notifyItemInserted(position);
            }
        }

        public void remove(CarInventory item) {
            int position = items.indexOf(item);
            items.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            Intent intent = new Intent(MainActivity.this, CarDetailsActivity.class);
            intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, items.get(itemPosition).vin);
            startActivityForResult(intent, REQUEST_ACTION_SEARCH);
            ;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            String hasTitle;
            NetworkImageView networkImageView;
            RelativeLayout parentLayout;
            LinearLayout ll_has_title;
            FrameLayout img_previewLayout;
            ImageView iv_editCar;
            TextView title, subtitle, oneOne, oneTwo, twoOne, twoTwo, totalPhoto, vin_last_eight, tv_rfid, tv_carStage;

            TextView tv_lotcode;
            LinearLayout ll_lotcode_container;

            public ItemViewHolder(View v) {
                super(v);
                parentLayout = (RelativeLayout) v.findViewById(R.id.search_row_parent);
                networkImageView = (NetworkImageView) v.findViewById(R.id.img);
                networkImageView.setDefaultImageResId(R.drawable.ic_default_car);
                img_previewLayout = (FrameLayout) v.findViewById(R.id.img_preview);
                title = (TextView) v.findViewById(R.id.title);
                subtitle = (TextView) v.findViewById(R.id.subtitle);
                oneOne = (TextView) v.findViewById(R.id.oneOneTxt);
                twoOne = (TextView) v.findViewById(R.id.twoOneTxt);
                oneTwo = (TextView) v.findViewById(R.id.onetwoTxt);
                twoTwo = (TextView) v.findViewById(R.id.twoTwoTxt);
                totalPhoto = (TextView) v.findViewById(R.id.totalPhoto);
                tv_rfid = (TextView) v.findViewById(R.id.tv_rfid);
                tv_carStage = (TextView) v.findViewById(R.id.tv_carStage);
                vin_last_eight = (TextView) v.findViewById(R.id.vin_last_eight);
                tv_lotcode = (TextView) v.findViewById(R.id.tv_lotcode);

                iv_editCar = (ImageView) v.findViewById(R.id.iv_editCar);

                ll_has_title = (LinearLayout) v.findViewById(R.id.ll_has_title);
                ll_lotcode_container = (LinearLayout) v.findViewById(R.id.ll_lotcode_container);
            }

            public NetworkImageView getNetworkImageView() {
                return networkImageView;
            }
        }

        public class ProgressViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public ProgressViewHolder(View v) {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isneeded) {
            isneeded = false;
            CarDetailsActivity.isNeeded = false;
            searchModel = new SearchModel();
            clearRecyclerView();
            ll_searchProgress = (LinearLayout) findViewById(R.id.ll_progressSearch);
            loadMoreLayout = (LinearLayout) findViewById(R.id.loadMore);
            initRecyclerView();
            seachUsingVolley(com.yukti.utils.Constants.limit, com.yukti.utils.Constants.pageNumber, true);
        }
    }
}