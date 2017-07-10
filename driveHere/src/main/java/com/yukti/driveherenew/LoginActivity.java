package com.yukti.driveherenew;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FEATURE_LIST;
import com.yukti.driveherenew.MessageDialogFragment.MessageDialogListener;
import com.yukti.facerecognization.localdatabase.DBController;
import com.yukti.jsonparser.Login;
import com.yukti.jsonparser.User;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements
        MessageDialogListener {


    EditText email, password;
    int SIGNUP_ACTIVITY_REQUEST_CODE = 100;
    public static final int REQ_LIVE_REQ = 1278;
    public static final int REQUEST_CAMERA = 111;
    LinearLayout ll_progress;
    User user;
    public static int REQ_ADD_PHOTO = 1089;
    DBController dbcontroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ll_progress = (LinearLayout) findViewById(R.id.ll_progresslogin);
        if (AppSingleTon.SHARED_PREFERENCE.isLoggedin()) {
            Intent intent = new Intent(LoginActivity.this,
                    com.yukti.driveherenew.MainActivity.class);
            startActivity(intent);
            finish();
        }
        init();
        initLogin();
        initWithFace();
        initSignUp();

    }

    void initWithFace() {
        // getData();
        TextView with_face = (TextView) findViewById(R.id.with_face);
        with_face.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isSupported = checkFaceRecgSupported();
                boolean hasFroncamera = Common.isFrontCameraAvailable();
                if (isSupported && hasFroncamera) {
                    if (AppSingleTon.VERSION_OS.checkVersion()) {
                        // Marshmallow+
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            faceRecogition();

                        } else {
                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                                builder.setTitle("");
                                builder.setMessage("Allow App To Access Camera And Storage To Save Images?");
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
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                builder.show();

                            }


                        }

                    } else {
                        // Pre-Marshmallow
                        faceRecogition();

                    }

                } else {
                    showFaceRecgNotSupportMsg();
                }
            }
        });
    }


    void faceRecogition() {
        Intent intent = new Intent(LoginActivity.this,
                com.yukti.facerecognization.localdatabase.FacialRecognitionActivityLocal.class);
        intent.putExtra("isaddPhoto", false);
        startActivityForResult(intent, REQ_LIVE_REQ);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                faceRecogition();
            } else {
                Toast.makeText(getApplicationContext(), "Both Camera & Storage Permission Needed", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void getData() {
        int i = dbcontroller.tableRows();
        Log.e("table row", "" + i);
    }

    void showFaceRecgNotSupportMsg() {
        Log.e("Login Activity", "Feature Facial Recognition is NOT supported");
        new AlertDialog.Builder(this)
                .setMessage(
                        "Your device does NOT support Facial Recognition feature.")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
    }

    boolean checkFaceRecgSupported() {
        boolean isSupported = FacialProcessing
                .isFeatureSupported(FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
        return isSupported;
    }

    void init() {
        dbcontroller = new DBController(getApplicationContext());

        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        TextView forgotPassword = (TextView) findViewById(R.id.forgot_password);

        forgotPassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        ForgotPasswordActivityTwo.class);
                startActivity(intent);
            }
        });
    }

    void initLogin() {

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                validateSignIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		/* user has signed up successfully and come back to login activity. */
        if (requestCode == SIGNUP_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            // TODO Show dialog for pending approval
            MessageDialogFragment fragment = new MessageDialogFragment(
                    "Approval", "Panding Admin Approval", true, "Ok", false,
                    "", false, "", LoginActivity.this);
            fragment.show(getSupportFragmentManager(), "TAG_PUSH_RESULT");

            // AppSingleTon.SHARED_PREFERENCE.login(true);
            // Intent intent = new Intent(LoginActivity.this,
            // com.yukti.drivehere.MainActivity.class);
            // startActivity(intent);
            // finish();

            // } else if (requestCode == SIGNUP_ACTIVITY_REQUEST_CODE) {
            //
            // openMainActivity();

        } else if (requestCode == REQ_ADD_PHOTO) {

            openMainActivity();

        } else if (requestCode == REQ_LIVE_REQ) {

            if (resultCode == RESULT_OK) {

                postLogin(data.getStringExtra("Email"),
                        data.getStringExtra("pass"), true);
//                login(data.getStringExtra("Email"),
//                        data.getStringExtra("pass"), true);
            } else {
            }
        }

    }

    void validateSignIn() {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        boolean flag = true;
        if (emailStr.equals("")) {
            flag = false;
            showEmailErrorText("Email cant be empty");
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            flag = false;
            showEmailErrorText("Invalid Email Address");
        }
        if (passwordStr.equals("")) {

            flag = false;
            showPasswordErrorText("Password cant be empty");

        } else if (passwordStr.length() < 6) {
            flag = false;
            showPasswordErrorText("Password length should be grater than 6");
        }

        if (flag == true) {
            postLogin(emailStr, passwordStr, false);
//            login(emailStr, passwordStr, false);
        }
    }

    void postLogin(final String emailStr, final String passwordStr,
                   final boolean isFromLiveRecg) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("login error response", responseString);

                showToast("Unknown login Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("login success response", response.toString());

                try {
                    Login orm = AppSingleTon.APP_JSON_PARSER.login(response);
                    user = orm.user;
                    if (orm.status_code.equals("1")) {

                        AppSingleTon.SHARED_PREFERENCE.setUserId(user.id);
                        AppSingleTon.SHARED_PREFERENCE.setUserName(user.name);
                        AppSingleTon.SHARED_PREFERENCE.seUserEmail(user.email);
                        AppSingleTon.SHARED_PREFERENCE
                                .setPhoneNumber(user.phone_number);
                        AppSingleTon.SHARED_PREFERENCE.login(true);

                        if (user.is_verified.equals("1")) {
                            AppSingleTon.SHARED_PREFERENCE.setVerify(true);
                        }
                        AppSingleTon.SHARED_PREFERENCE.setPassword(password
                                .getText().toString());

                        openMainActivity();
                    } else if (orm.status_code.equals("2")) {
                        // showToast(orm.msg);
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Approval", orm.msg, true, "Ok", false, "", false,
                                "", LoginActivity.this);
                        fragment.show(getSupportFragmentManager(),
                                "TAG_PUSH_RESULT");
                        showToast(orm.msg);
                    } else if (orm.status_code.equals("3")) {
                        // showEmailErrorText(orm.msg);
                        showToast(orm.msg);
                    } else if (orm.status_code.equals("0")) {
                        // showPasswordErrorText(orm.msg);
                        showToast(orm.msg);
                    } else if (orm.status_code.equals("4")) {
                        showPasswordErrorText(orm.msg);
                    } else if (orm.status_code.equals("5")) {
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Approval", orm.msg, true, "Ok", false, "", false,
                                "", LoginActivity.this);
                        fragment.show(getSupportFragmentManager(),
                                "TAG_PUSH_RESULT");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtils.showAlertDialog(LoginActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (Common.isNetworkConnected(getApplicationContext()))
                                postLogin(emailStr, passwordStr, isFromLiveRecg);
                            else
                                Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("Logging in...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };
        String url = AppSingleTon.APP_URL.URL_LOGIN;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_EMAILID, emailStr);
        params.put(ParamsKey.KEY_PASSWORD, passwordStr);
        if (Common.isNetworkConnected(getApplicationContext())) {
            RestClient.post(this, url, params, handler);
        } else
            Toast.makeText(getApplicationContext(),
                    "Please Connect To Internet", Toast.LENGTH_SHORT).show();
    }

    /*void login(final String email, final String pass, final boolean isFromLiveRecg) {
//        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        if (Common.isNetworkConnected(getApplicationContext())) {
            ll_progress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response..", response.toString());
                    ll_progress.setVisibility(View.GONE);

                   JSONObject jsonObject = new JSONObject(response);
                    Log.e("status_code", jsonObject.getString("status_code"));

                    if (jsonObject.getString("status_code").equals("1")) {
                        JSONObject jobj = jsonObject.getJSONObject("user");
                        String id = jobj.getString("carId");
                        Log.e("carId..", id);
                        Log.e("name", jobj.getString("name"));
                        Log.e("phone", jobj.getString("telephone"));
                        Log.e("Varified", jobj.getString("is_verified"));
                        AppSingleTon.SHARED_PREFERENCE.setUserId(jobj.getString("carId"));
                        AppSingleTon.SHARED_PREFERENCE.setUserName(jobj.getString("name"));
                        AppSingleTon.SHARED_PREFERENCE.seUserEmail(jobj.getString("email"));
                        AppSingleTon.SHARED_PREFERENCE.setPhoneNumber(jobj.getString("telephone"));
                        AppSingleTon.SHARED_PREFERENCE.login(true);
//                        AppPreference appPreference=new AppPreference();
//                        appPreference.login(true);
                        if (jobj.getString("is_verified").equals("1")) {
                            AppSingleTon.SHARED_PREFERENCE.setVerify(true);
                        }
                        AppSingleTon.SHARED_PREFERENCE.setPassword(password
                                .getText().toString());
                        openMainActivity();
                        // getFace(isFromLiveRecg);
                        // Intent intent = new Intent(LoginActivity.this,
                        // com.yukti.drivehere.MainActivity.class);
                        // startActivity(intent);
                        // finish();

                    } else if ((jsonObject.getString("status_code").equals("2"))) {
                        // showToast(orm.msg);
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Approval", jsonObject.getString("msg"), true, "Ok", false, "", false,
                                "", LoginActivity.this);
                        fragment.show(getSupportFragmentManager(),
                                "TAG_PUSH_RESULT");
                        showToast((jsonObject.getString("msg")));
                    } else if ((jsonObject.getString("status_code").equals("3"))) {
                        // showEmailErrorText(orm.msg);
                        showToast((jsonObject.getString("msg")));
                    } else if ((jsonObject.getString("status_code").equals("0"))) {
                        // showPasswordErrorText(orm.msg);
                        showToast((jsonObject.getString("msg")));
                    } else if (jsonObject.getString("status_code").equals("4")) {
                        showPasswordErrorText(jsonObject.getString("status_code"));
                    } else if (jsonObject.getString("status_code").equals("5")) {
                        MessageDialogFragment fragment = new MessageDialogFragment(
                                "Approval", jsonObject.getString("status_code"), true, "Ok", false, "", false,
                                "", LoginActivity.this);
                        fragment.show(getSupportFragmentManager(),
                                "TAG_PUSH_RESULT");
                    } else {
                        showPasswordErrorText(jsonObject.getString("status_code"));
                    }
//                }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        CommonUtils.showAlertDialog(LoginActivity.this, Constant.ERR_TITLE, Constant.ERR_MESSAGE, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                if (Common.isNetworkConnected(getApplicationContext()))
//                                    login(email, pass, isFromLiveRecg);
//                                else
//                                    Toast.makeText(getApplicationContext(), Constant.ERR_INTERNET, Toast.LENGTH_LONG).show();
//                            }
//                        });

//                }
                }
            }
                ,new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                    ll_progress.setVisibility(View.GONE);
                    Log.e("error", "" + error);
                }
                }

                )

                {
                    @Override
                    protected Map<String, String> getParams () {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(ParamsKey.KEY_EMAILID, email);
                    params.put(ParamsKey.KEY_PASSWORD, pass);
                    Log.e("Credentials..", email + "\n" + pass);
                    return params;
                }
                }

                ;
//        queue.add(stringRequest);
                stringRequest.setTag(Constants.REQUEST_TAG);
                stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                        Constants.VOLLEY_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

                );
                MyApplication.getInstance(

                getApplicationContext()

                ).

                addToRequestQueue(stringRequest);
            }else{
                Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }*/


    protected void getFace(final boolean isFromLiveRecg) {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("face error response", responseString);
                showToast("get face Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("face success response", response.toString());

                Login orm = AppSingleTon.APP_JSON_PARSER.login(response);

                if (orm.status_code.equals("1")) {

                    AppSingleTon.SHARED_PREFERENCE.setUserFace(orm.face);
                    // openMainActivity();
                    // face2 code
                    if (isFromLiveRecg) {
                        openMainActivity();
                    } else {
                        boolean isSupport = checkFaceRecgSupported();
                        boolean hasFroncamera = Common.isFrontCameraAvailable();
                        if (isSupport && hasFroncamera) {

                            boolean hasFaceId = getFaceIDfromLD();

                            if (!hasFaceId) {
                                showFaceRecognizationAlert();
                            } else {
                                openMainActivity();
                            }
                        } else {
                            openMainActivity();
                        }
                    }
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("getting face...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        };
        String url = AppSingleTon.APP_URL.URL_GET_FACE;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_UID, user.id);
        RestClient.post(this, url, params, handler);
    }

    void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this,
                com.yukti.driveherenew.MainActivity.class);
        startActivity(intent);
        finish();
    }

    void showEmailErrorText(String text) {
        email.setError(text);
    }

    void showPasswordErrorText(String text) {
        password.setError(text);
    }

    void initSignUp() {

        TextView signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        com.yukti.driveherenew.SignUpActivity.class);
                startActivityForResult(intent, SIGNUP_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        RestClient.cancel(this, true);
        ll_progress.setVisibility(View.GONE);
        cancleRequest();
    }

    void cancleRequest() {
        if (MyApplication.getInstance(this.getApplicationContext()).getRequestQueue() != null) {
            Log.e("On Stop", "Cancle request");
            MyApplication.getInstance(this.getApplicationContext()).getRequestQueue().cancelAll(Constants.REQUEST_TAG);
        }
    }
    // void storeFaceIDtoLD(String face) {
    //
    // dbcontroller = new DBController(getApplicationContext());
    // dbcontroller.add_user(email.getText().toString().trim(), password
    // .getText().toString().trim(), face);
    //
    // }

    boolean getFaceIDfromLD() {

        boolean hasFaceid = dbcontroller.getFaceID(email.getText().toString()
                .trim(), password.getText().toString().trim());
        return hasFaceid;
    }

    protected void showFaceRecognizationAlert() {

        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Want to use Facial Recognition feature. ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(
                                LoginActivity.this,
                                com.yukti.facerecognization.localdatabase.FacialRecognitionActivityLocal.class);
                        intent.putExtra("isaddPhoto", true);
                        intent.putExtra("Email", email.getText().toString()
                                .trim());
                        intent.putExtra("password", password.getText()
                                .toString().trim());
                        // intent.putExtra("Userid", user.carId);
                        startActivityForResult(intent, REQ_ADD_PHOTO);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                openMainActivity();
                            }
                        })
                .show();
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {
        // TODO Auto-generated method stub

    }

}
