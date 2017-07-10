package com.yukti.newchanges.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.yukti.driveherenew.AllDetailActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.search.ZoomImageActivity;
import com.yukti.utils.AllDetail;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Contract;

import java.io.File;
import java.util.ArrayList;


public class LoanerCarFragment extends Fragment implements View.OnClickListener {
    public static TextView tv_expected_date;
    TextView reason;
    CheckBox checkBox_pre, checkBox_customer;
    TextView tv_vin, tv_rfid, milage, gas_tank, tv_booking_date, tv_pre_lot_recived, tv_pre_notes;
    private ImageView pictureimage1, pictureimage2;
    private String loanpic1url, loanpic2url;

    private int imageViewWidth = 0;
    private int imageViewHeight = 0;

    public LoanerCarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two, container, false) ;

        tv_vin = (TextView) view.findViewById(R.id.tv_vin);
        tv_rfid = (TextView) view.findViewById(R.id.tv_rfid);
        milage = (TextView) view.findViewById(R.id.milage);
        gas_tank = (TextView) view.findViewById(R.id.gas_tank);
        tv_expected_date = (TextView) view.findViewById(R.id.tv_expected_date);
        tv_booking_date = (TextView) view.findViewById(R.id.tv_booking_date);
        tv_pre_lot_recived = (TextView) view.findViewById(R.id.tv_pre_lot_recived);
        reason = (TextView) view.findViewById(R.id.reason);
        tv_pre_notes = (TextView) view.findViewById(R.id.tv_pre_notes);

        checkBox_pre = (CheckBox) view.findViewById(R.id.checkBox_pre);
        checkBox_customer = (CheckBox) view.findViewById(R.id.checkBox_customer);

        pictureimage1 = (ImageView) view.findViewById(R.id.btn_pictures1);
        pictureimage2 = (ImageView) view.findViewById(R.id.btn_pictures2);

        pictureimage1.setOnClickListener(this);
        pictureimage2.setOnClickListener(this);

        setDetail();
        return view;
    }

    void  setDetail()
    {
        if (AllDetailActivity.detail.preNotes!=null)
        {
            tv_pre_notes.setText(AllDetailActivity.detail.preNotes);
        }
        else
        {
            tv_pre_notes.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.preLotReceived!=null)
        {
            tv_pre_lot_recived.setText(AllDetailActivity.detail.preLotReceived);
        }
        else
        {
            tv_pre_lot_recived.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.bookingDate!=null)
        {
            tv_booking_date.setText(AllDetailActivity.detail.bookingDate);
        }
        else
        {
            tv_booking_date.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.expectedDate!=null)
        {
            tv_expected_date.setText(AllDetailActivity.detail.expectedDate);
        }
        else
        {
            tv_expected_date.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.preGasTank!=null)
        {
            gas_tank.setText(AllDetailActivity.detail.preGasTank);
        }
        else
        {
            gas_tank.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.preMilage!=null)
        {
            milage.setText(AllDetailActivity.detail.preMilage);
        }
        else
        {
            milage.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.rfid!=null)
        {
            tv_rfid.setText(AllDetailActivity.detail.rfid);
        }
        else
        {
            tv_rfid.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.vin!=null)
        {
            tv_vin.setText(AllDetailActivity.detail.vin);
        }
        else
        {
            tv_vin.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.reasone!=null)
        {
            reason.setText(AllDetailActivity.detail.reasone);
        }
        else
        {
            reason.setText(Constant.KEY_NOT_AVAILABLE);
        }


        initPreFillCheckInspection();
        initCheckcustomercheckbox();

        setpicture1image();
    }
    private void initPreFillCheckInspection() {
        switch (AllDetailActivity.detail.preInspection) {
            case "yes":
                checkBox_pre.setChecked(true);
                break;
            case "no":
                checkBox_pre.setChecked(false);
                break;
            case "1":
                checkBox_pre.setChecked(true);
                break;
            case "0":
                checkBox_pre.setChecked(false);
                break;
        }
    }

    private void initCheckcustomercheckbox() {

        switch (AllDetailActivity.detail.checkcards) {
            case "yes":
                checkBox_customer.setChecked(true);
                break;
            case "no":
                checkBox_customer.setChecked(false);
                break;
            case "1":
                checkBox_customer.setChecked(true);
                break;
            case "0":
                checkBox_customer.setChecked(false);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        String s = "", path = "", url = "";
        Bitmap imgBitmap = null;

        switch (v.getId()) {
            case R.id.btn_pictures1:
                imgBitmap = ((BitmapDrawable) pictureimage1.getDrawable()).getBitmap();
                url = loanpic1url;
                path = "";
                s = "LoanPic1";
                break;
            case R.id.btn_pictures2:
                imgBitmap = ((BitmapDrawable) pictureimage2.getDrawable()).getBitmap();
                url = loanpic2url;
                path = "";
                s = "LoanPic2";
                break;
            default:
                break;
        }
        if (imgBitmap != null) {
            Intent i = new Intent(getActivity(), ZoomImageActivity.class);
            i.putExtra("path", url);
            i.putExtra("path_f", path);
            i.putExtra(Constant.EXTRA_KEY_TITLE, s);
            CommonUtils.img = imgBitmap;
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), "Image not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void setpicture1image() {
        if (AllDetailActivity.detail.images.size() >= 1) {
            loanpic1url = AllDetailActivity.detail.images.get(0).image;
            loanpic1url = loanpic1url.replace(" ", "%20");
            if (imageViewWidth == 0) {
                AppSingleTon.setPicUniversalImageLoader(pictureimage1,loanpic1url,getActivity());
            } else {
                AppSingleTon.setPicUniversalImageLoader(pictureimage1,loanpic1url,getActivity());
            }
            setpicture2image();
        }
    }

    private void setpicture2image() {

        if (AllDetailActivity.detail.images.size() >= 2) {
            loanpic2url = AllDetailActivity.detail.images.get(1).image;
            loanpic2url = loanpic2url.replace(" ", "%20");
            if (imageViewWidth == 0) {
                AppSingleTon.setPicUniversalImageLoader(pictureimage2,loanpic2url,getActivity());
            } else {
                AppSingleTon.setPicUniversalImageLoader(pictureimage2,loanpic2url,getActivity());
            }
        }
    }
}
