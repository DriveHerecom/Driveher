package com.yukti.driveherenew;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing.FEATURE_LIST;
import com.yukti.jsonparser.Signup;
import com.yukti.jsonparser.User;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.Common;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constants;
import com.yukti.utils.ParamsKey;
import com.yukti.utils.RestClient;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity {


    EditText name, email, phone, password,confirmPassword;
    TextView tAndCText;

    User user;
    public static final int REQ_ADD_PHOTO = 815;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        init();
        initTermsAndConditions();
        initSignUp();
        Login();
    }

    void Login() {
        TextView tv_login = (TextView) findViewById(R.id.tv_signIn);
        tv_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    void init() {
        name = (EditText) findViewById(R.id.UserName);
        email = (EditText) findViewById(R.id.EmailId);
        phone = (EditText) findViewById(R.id.PhoneNumber);
        password = (EditText) findViewById(R.id.Password);
        confirmPassword = (EditText) findViewById(R.id.editTextConfrimPass);
        tAndCText = (TextView) findViewById(R.id.tAndCText);
        tAndCText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    void initSignUp() {
        Button signUp = (Button) findViewById(R.id.signup);
        signUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                validateSignUp();
            }
        });
    }

    void validateSignUp() {

        String nameStr = name.getText().toString();
        String emailStr = email.getText().toString();
        String phoneStr = phone.getText().toString();
        String passwordStr = password.getText().toString();
        String confirmPass= confirmPassword.getText().toString();

        boolean flag = true;

        if (nameStr.equals("")) {

            flag = false;
            showNameErrorText("Name cant be empty");
        }
        if (emailStr.equals("")) {

            flag = false;
            showEmailErrorText("Email cant be empty");
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            flag = false;
            showEmailErrorText("Email not valid");
        }
        if (phoneStr.equals("")) {

            flag = false;
            showPhoneErrorText("phone no cant be empty");
        }
        if (passwordStr.equals("")) {

            flag = false;
            showPasswordErrorText("Password cant be empty");

        } else if (passwordStr.length() < 6) {

            flag = false;
            showPasswordErrorText("Password length should be grater than 6");
        }else if(!confirmPass.equalsIgnoreCase(passwordStr)){
            flag = false;
            showConfirmPasswordErrorText("Confirm Password & password are not match");
        }
        if (flag == true) {
       //     postSignUp();
            signUpUsingVolley();
        }
    }

    void postSignUp() {

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("SIGNUP error response", responseString);
                showToast("Signup Error");
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("SIGNUP_response", response.toString());

                Signup orm = AppSingleTon.APP_JSON_PARSER.singup(response);
                user = orm.user;

                Log.e("Status Code....", orm.status_code);
                if (orm.status_code.equals("1")) {
                    Log.e("Status Code....", orm.status_code);


                    setResult(Activity.RESULT_OK);
                    finish();
                    // showFaceRecognizationAlert();

                } else if (orm.status_code.equals("2")) {
                    showEmailErrorText(orm.message);
                    Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                } else if (orm.status_code.equals("4")) {
                    Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                    showEmailErrorText(orm.message);
                } else {
                    showEmailErrorText(orm.message);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                showUpdateProgressDialog("Signing up...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }

        };
        String url = AppSingleTon.APP_URL.URL_SIGN_UP;
        RequestParams params = new RequestParams();
        params.put(ParamsKey.KEY_USERNAME, name.getText().toString());
        params.put(ParamsKey.KEY_CONTACTNO, phone.getText().toString());
        params.put(ParamsKey.KEY_EMAILID, email.getText().toString());
        params.put(ParamsKey.KEY_PASSWORD, password.getText().toString());
        Log.e("Params",params + "test");
        RestClient.post(this, url, params, handler);
    }

    void signUpUsingVolley() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppSingleTon.APP_URL.URL_SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Signup orm = AppSingleTon.APP_JSON_PARSER.singup(response);
                user = orm.user;

                Log.e("Status Code....", orm.status_code);
                if (orm.status_code.equals("1")) {
                    Log.e("Status Code....", orm.status_code);


                    setResult(Activity.RESULT_OK);
                    finish();
                    // showFaceRecognizationAlert();

                } else if (orm.status_code.equals("2")) {
                    showEmailErrorText(orm.message);
                    Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                } else if (orm.status_code.equals("4")) {
                    Toast.makeText(getApplicationContext(), orm.message, Toast.LENGTH_SHORT).show();
                    showEmailErrorText(orm.message);
                } else {
                    showEmailErrorText(orm.message);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Error", error.toString());
                        showTryAgainAlert("Info", "Network error, Please try again!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(ParamsKey.KEY_USERNAME, name.getText().toString());
                params.put(ParamsKey.KEY_CONTACTNO, phone.getText().toString());
                params.put(ParamsKey.KEY_EMAILID, email.getText().toString());
                params.put(ParamsKey.KEY_PASSWORD, password.getText().toString());
                Log.e("Params",params + "test");
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

    public void showTryAgainAlert(String title, String message) {
        CommonUtils.showAlertWithNegativeButton(SignUpActivity.this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (Common.isNetworkConnected(getApplicationContext()))
                    signUpUsingVolley();
                else
                    Toast.makeText(getApplicationContext(),
                            "Please Connect To Internet", Toast.LENGTH_LONG)
                            .show();
            }
        });
    }
    protected void showFaceRecognizationAlert() {

        new AlertDialog.Builder(SignUpActivity.this)
                .setMessage("Want to use Facial Recognition feature. ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        boolean isSupported = FacialProcessing
                                .isFeatureSupported(FEATURE_LIST.FEATURE_FACIAL_RECOGNITION);
                        if (isSupported) {

                            Intent intent = new Intent(
                                    SignUpActivity.this,
                                    com.yukti.facerecognization.FacialRecognitionActivity.class);
                            intent.putExtra("isaddPhoto", true);
                            // intent.putExtra("Userid", user.carId);
                            startActivityForResult(intent, REQ_ADD_PHOTO);

                        } else {

                            Log.e("SignUpActivity",
                                    "Feature Facial Recognition is NOT supported");

                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setMessage(
                                            "Your device does NOT support Qualcomm's Facial Recognition feature. ")
                                    .setCancelable(false)
                                    .setNegativeButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {

                                                    setResult(Activity.RESULT_OK);
                                                    finish();
                                                }
                                            }).show();

                        }

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                setResult(Activity.RESULT_OK);
                                finish();

                            }
                        })

                .show();
    }

    void showNameErrorText(String text) {
        name.setError(text);
    }

    void showEmailErrorText(String text) {
        email.setError(text);
    }

    void showPhoneErrorText(String text) {
        phone.setError(text);
    }

    void showPasswordErrorText(String text) {
        password.setError(text);
    }
    void showConfirmPasswordErrorText(String text) {
        confirmPassword.setError(text);
    }
    void startHome() {
        Intent intent = new Intent(SignUpActivity.this,
                com.yukti.driveherenew.MainActivity.class);
        startActivity(intent);
    }

    void initTermsAndConditions() {

        tAndCText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,
                        com.yukti.driveherenew.TermsAndConditions.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        RestClient.cancel(this, true);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // user has signed up successfully and come back to login activity.*/
        if (requestCode == REQ_ADD_PHOTO && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
