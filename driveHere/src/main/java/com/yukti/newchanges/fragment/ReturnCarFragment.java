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
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.Constant;
import com.yukti.utils.Contract;

import java.io.File;
import java.util.ArrayList;


public class ReturnCarFragment extends Fragment  implements View.OnClickListener
{
    CheckBox checkBox_post;
    TextView tv_post_milage, tv_post_gas_tank, tv_post_recrieved_date, tv_post_lot_recived, tv_post_notes;

    private ImageView returnpicture1, returnpicture2;
    private String returnpic1url, returnpic2url;

    public ReturnCarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_three, container, false) ;
        tv_post_milage = (TextView) view.findViewById(R.id.tv_post_milage);
        tv_post_gas_tank = (TextView) view.findViewById(R.id.tv_post_gas_tank);
        tv_post_recrieved_date = (TextView) view.findViewById(R.id.tv_post_recrieved_date);
        tv_post_lot_recived = (TextView) view.findViewById(R.id.tv_post_lot_recived);
        tv_post_notes = (TextView) view.findViewById(R.id.tv_post_notes);

        checkBox_post = (CheckBox) view.findViewById(R.id.checkBox_post);

        returnpicture1 = (ImageView) view.findViewById(R.id.btn_pictures1_return);
        returnpicture2 = (ImageView) view.findViewById(R.id.btn_pictures2_return);

        returnpicture1.setOnClickListener(this);
        returnpicture2.setOnClickListener(this);

        setDetail();
        seReturnPicture1image();
        return view;
    }

    @Override
    public void onClick(View v) {
        String s = "", path = "", url = "";
        Bitmap imgBitmap = null;

        switch (v.getId()) {
            case R.id.btn_pictures1_return:
                imgBitmap = ((BitmapDrawable) returnpicture1.getDrawable()).getBitmap();
                url = returnpic1url;
                path = "";
                s = "ReturnPic1";
                break;
            case R.id.btn_pictures2_return:
                imgBitmap = ((BitmapDrawable) returnpicture2.getDrawable()).getBitmap();
                url = returnpic2url;
                path = "";
                s = "ReturnPic2";
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

    void setDetail()
    {
        initPostFillCheckInspection();

        if (AllDetailActivity.detail.postMilage!=null)
        {
            tv_post_milage.setText(AllDetailActivity.detail.postMilage);
        }
        else
        {
            tv_post_milage.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.postGasTank!=null)
        {
            tv_post_gas_tank.setText(AllDetailActivity.detail.postGasTank);
        }
        else
        {
            tv_post_gas_tank.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.postNotes!=null)
        {
            tv_post_notes.setText(AllDetailActivity.detail.postNotes);
        }
        else
        {
            tv_post_notes.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.receivedDate!=null)
        {
            tv_post_recrieved_date.setText(AllDetailActivity.detail.receivedDate);
        }
        else
        {
            tv_post_recrieved_date.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (AllDetailActivity.detail.postLotReceived!=null)
        {
            tv_post_lot_recived.setText(AllDetailActivity.detail.postLotReceived);
        }
        else
        {
            tv_post_lot_recived.setText(Constant.KEY_NOT_AVAILABLE);
        }
    }

    private void initPostFillCheckInspection() {
        switch (AllDetailActivity.detail.postInspection) {
            case "yes":
                checkBox_post.setChecked(true);
                break;
            case "no":
                checkBox_post.setChecked(false);
                break;
            case "1":
                checkBox_post.setChecked(true);
                break;
            case "0":
                checkBox_post.setChecked(false);
                break;
        }
    }

    private void seReturnPicture1image() {
        if (AllDetailActivity.detail.images.size() >= 3) {
            returnpic1url = AllDetailActivity.detail.images.get(2).image;
            returnpic1url = returnpic1url.replace(" ", "%20");
            AppSingleTon.setPicUniversalImageLoader(returnpicture1,returnpic1url,getActivity());
            seReturnPicture2image();
        }
    }

    private void seReturnPicture2image() {
        if (AllDetailActivity.detail.images.size() >= 4) {
            returnpic2url = AllDetailActivity.detail.images.get(3).image;
            returnpic2url = returnpic2url.replace(" ", "%20");
            AppSingleTon.setPicUniversalImageLoader(returnpicture2,returnpic2url,getActivity());
        }
    }
}
