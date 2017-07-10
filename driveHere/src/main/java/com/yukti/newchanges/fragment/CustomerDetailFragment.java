package com.yukti.newchanges.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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

public class CustomerDetailFragment extends Fragment implements View.OnClickListener
{

    TextView first_name, last_name, cell_number, home_number, work_number;
    private ImageView drivingImage, contractImage, insuranceImage;
    private String licenseurl, contracturl, insuranceurl;

    public CustomerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view =inflater.inflate(R.layout.fragment_one, container, false);

        first_name = (TextView) view.findViewById(R.id.first_name);
        last_name = (TextView) view.findViewById(R.id.last_name);
        cell_number = (TextView) view.findViewById(R.id.cell_number);
        home_number = (TextView) view.findViewById(R.id.home_number);
        work_number = (TextView) view.findViewById(R.id.work_number);

        drivingImage = (ImageView) view.findViewById(R.id.license_image);
        contractImage = (ImageView) view.findViewById(R.id.contract_image);
        insuranceImage = (ImageView) view.findViewById(R.id.insurance_image);

        drivingImage.setOnClickListener(this);
        contractImage.setOnClickListener(this);
        insuranceImage.setOnClickListener(this);

        setDetail();

        setDrivingImage();
        setContractImage();
        setInsuranceImage();

        return view;
    }

    void setDetail()
    {
        if (AllDetailActivity.detail.firstName!=null)
        {
            first_name.setText(AllDetailActivity.detail.firstName);
        }
        else
        {
            first_name.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.lastName!=null)
        {
            last_name.setText(AllDetailActivity.detail.lastName);
        }
        else
        {
            last_name.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.cphone!=null)
        {
            cell_number.setText(AllDetailActivity.detail.cphone);
        }
        else
        {
            cell_number.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.hphone!=null)
        {
            home_number.setText(AllDetailActivity.detail.hphone);
        }
        else
        {
            home_number.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.wphone!=null)
        {
            work_number.setText(AllDetailActivity.detail.wphone);
        }
        else
        {
            work_number.setText(Constant.KEY_NOT_AVAILABLE);
        }
    }

    @Override
    public void onClick(View v) {
        String s = "", path = "", url = "";
        Bitmap imgBitmap = null;
        switch (v.getId())
        {
            case R.id.license_image:
                imgBitmap = ((BitmapDrawable) drivingImage.getDrawable()).getBitmap();
                url = licenseurl;
                path = "";
                s = "License";
                break;
            case R.id.contract_image:
                imgBitmap = ((BitmapDrawable) contractImage.getDrawable()).getBitmap();
                url = contracturl;
                path = "";
                s = "Contract";
                break;
            case R.id.insurance_image:
                imgBitmap = ((BitmapDrawable) insuranceImage.getDrawable()).getBitmap();
                url = insuranceurl;
                path = "";
                s = "Insurance";
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

    private void setDrivingImage() {
        licenseurl = AllDetailActivity.detail.licImage;
        licenseurl = licenseurl.replace(" ", "%20");
        AppSingleTon.setPicUniversalImageLoader(drivingImage,licenseurl,getActivity());
    }

    private void setContractImage() {
        contracturl = AllDetailActivity.detail.contract;
        contracturl = contracturl.replace(" ", "%20");
        AppSingleTon.setPicUniversalImageLoader(contractImage,contracturl,getActivity());
    }

    private void setInsuranceImage() {
        insuranceurl = AllDetailActivity.detail.insurance;
        insuranceurl = insuranceurl.replace(" ", "%20");
        AppSingleTon.setPicUniversalImageLoader(insuranceImage,insuranceurl,getActivity());
    }
}