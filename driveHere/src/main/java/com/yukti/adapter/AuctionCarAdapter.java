package com.yukti.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.search.CarInventory;
import com.yukti.utils.Constant;
import com.yukti.utils.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by admin on 2/8/2016.
 */
public class AuctionCarAdapter extends RecyclerView.Adapter<AuctionCarAdapter.MyViewHolder>
{
    Context context;
    public ArrayList<CarInventory> CarList;
    LayoutInflater inflater;
    MyItemClickListener itemClickListener;

    public AuctionCarAdapter(ArrayList<CarInventory> CarList, Context context) {
        this.CarList = CarList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.available_auction_car,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position)
    {
        CarInventory car = CarList.get(position);
        if (car.imagePath != null && car.imagePath.length() > 0) {
            viewHolder.networkImageView.setVisibility(View.VISIBLE);
            viewHolder.networkImageView.setImageUrl(
                             car.imagePath, VolleySingleton
                            .getInstance(context)
                            .getImageLoader());

        } else {
            viewHolder.networkImageView.setVisibility(View.INVISIBLE);
            viewHolder.img_previewLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_default_car));
        }

        if (car.vin.length() == 17) {
            viewHolder.vin_last_eight.setText(".." + car.vin.substring(9, car.vin.length()));
        } else {
            viewHolder.vin_last_eight.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (car.rfid != null && car.rfid.length() > 3) {
            viewHolder.tv_rfid.setText(car.rfid);
        } else {
            viewHolder.tv_rfid.setText(Constant.KEY_NOT_AVAILABLE);
        }

        if (car.stage != null && car.stage.length() > 0) {
            viewHolder.tv_carStage.setText(car.stage);
        } else {
            viewHolder.tv_carStage.setText(Constant.KEY_NOT_AVAILABLE);
        }

        String carname = "";

        if (car.make!=null)
        {
            carname = carname + car.make + " " ;
        }

        if (car.model!=null)
        {
            carname = carname + car.model + " ";
        }

        if (car.modelNumber!=null)
        {
            carname = carname + " "+car.modelNumber;
        }

        viewHolder.title.setText(carname);

        if (car.price!= null && car.price.length() > 0) {
            viewHolder.subtitle.setText(car.price + " $");
        } else {
            viewHolder.subtitle.setText(Constant.KEY_NOT_AVAILABLE);
        }

        viewHolder.iv_editCar.setVisibility(View.GONE);

        if (car.stage!=null)
        viewHolder.oneOne.setText(car.stage);
        else
            viewHolder.oneOne.setText(Constant.KEY_NOT_AVAILABLE);

        if (car.note != null && car.note.length() > 0) {
            viewHolder.twoTwo.setText(car.note);
        } else {
            viewHolder.twoTwo.setText(Constant.KEY_NOT_AVAILABLE);
        }

        viewHolder.totalPhoto.setText(car.imageCount + "");

        /*if (car.title != null && car.title.length() > 0) {
            if (car.Title.equalsIgnoreCase("yes")) {
                viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#338033"));
            } else {
                viewHolder.ll_has_title.setBackgroundColor(Color.parseColor("#000000"));
            }
        }
        */

        if (car.lotCode == null) {
            viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
            viewHolder.tv_lotcode.setText(Constant.KEY_NOT_AVAILABLE);
        } else {
            viewHolder.ll_lotcode_container.setVisibility(View.VISIBLE);
            viewHolder.tv_lotcode.setText(car.lotCode);
        }
    }

    @Override
    public int getItemCount() {
        return CarList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        String hasTitle;
        NetworkImageView networkImageView;
        RelativeLayout parentLayout;
        LinearLayout ll_has_title;
        FrameLayout img_previewLayout;
        ImageView iv_editCar;
        TextView title, subtitle, oneOne, oneTwo, twoOne, twoTwo, totalPhoto, vin_last_eight, tv_rfid, tv_carStage;

        TextView tv_lotcode;
        LinearLayout ll_lotcode_container;

        public MyViewHolder(View v)
        {
            super(v);
            v.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            if(itemClickListener != null)
            {
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
    public interface MyItemClickListener
    {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final MyItemClickListener mItemClickListener)
    {
        this.itemClickListener = mItemClickListener;
    }
}
