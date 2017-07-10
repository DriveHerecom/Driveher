package com.yukti.driveherenew;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yukti.utils.AppSingleTon;
import com.yukti.utils.UniversalImageLoader;

import java.util.ArrayList;

/**
 * Created by ravi on 20-02-2017.
 */

public class AddNewCarDetailsActivity extends BaseActivity implements MessageDialogFragment.MessageDialogListener {

    static Activity activity;
    public static final int NEWCAR_REQUEST_CAMERA = 112;
    String TAG_ENABLE_GPS = "ENABLE_GPS";
    String TAG_ENABLE_SETTING = "ENABLE_SETTING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        methodForAdd();
        UniversalImageLoader.initImageLoader(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_add_car_app_bar);
        setSupportActionBar(toolbar);
        setActionBar("Add New Car");
        methodForAdd();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }


    private void setActionBar(String title) {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(title);
    }

    public void methodForAdd() {
        if (!AppSingleTon.METHOD_BOX.isGpsEnabled(AddNewCarDetailsActivity.this)) {
            DialogFragment fragment = new MessageDialogFragment(
                    "Device GPS is Off.",
                    "You need to switch on GPS to use this functionality.",
                    true, "Enable GPS", true, "Cancel", false, "",
                    AddNewCarDetailsActivity.this);
            fragment.show(getSupportFragmentManager(), TAG_ENABLE_GPS);
        } else if (!AppSingleTon.METHOD_BOX.isInternetConnected()) {
            DialogFragment fragment = new MessageDialogFragment(
                    "No internet Connection.",
                    "Please pick internet connection.", true,
                    "Enable Internet", true, "Cancel", false, "",
                    AddNewCarDetailsActivity.this);
            fragment.show(getSupportFragmentManager(), TAG_ENABLE_SETTING);
        } else {
            if (AppSingleTon.VERSION_OS.checkVersion()) {
                // Marshmallow+
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    scannerClicked();
                } else {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCarDetailsActivity.this);

                        builder.setTitle("");
                        builder.setMessage("Camera Permission Needed To Scan, Allow It?");
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
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, NEWCAR_REQUEST_CAMERA);
                            }
                        });
                        builder.show();
                    }
                }
            } else {
                scannerClicked();
            }
        }
    }

    void scannerClicked() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCarDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custome, null);
        builder.setCancelable(false);
        TextView cancel_dialog = (TextView) view.findViewById(R.id.cancel_dialog);
        TextView inputMethod = (TextView) view.findViewById(R.id.inputnumber);
        TextView scanMethod = (TextView) view.findViewById(R.id.scan);

        inputMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCarDetailsActivity.this,
                        InputScanActivity.class);
                startActivity(intent);
                finish();

            }
        });
        scanMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCarDetailsActivity.this,
                        VinScannerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setView(view);
        builder.show();
    }


    @Override
    public void onDialogNegativeClick(MessageDialogFragment dialog) {
        String tag = dialog.getTag();

        if (tag.equals(TAG_ENABLE_GPS)) {
            finish();
        } else if (tag.equals(TAG_ENABLE_SETTING)) {
            finish();
        }
    }

    @Override
    public void onDialogNeutralClick(MessageDialogFragment dialog) {


    }
    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog) {

        String tag = dialog.getTag();

        if (tag.equals(TAG_ENABLE_GPS)) {

            AppSingleTon.METHOD_BOX.startLocationSetting(AddNewCarDetailsActivity.this);
                finish();
        } else if (tag.equals(TAG_ENABLE_SETTING)) {

            AppSingleTon.METHOD_BOX.startSetting(AddNewCarDetailsActivity.this);
            finish();
        }
    }
}
