package com.yukti.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yukti.dataone.model.TitleHistoryModelCustom;
import com.yukti.driveherenew.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by prashant on 23/1/16.
 */
public class TitleHistoryAdapter extends RecyclerView.Adapter<TitleHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<TitleHistoryModelCustom> title = new ArrayList<TitleHistoryModelCustom>();

    public TitleHistoryAdapter(Context context,ArrayList<TitleHistoryModelCustom> result) {
        this.context = context;
        this.title = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_titlehistory, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position)
    {
        if(title.get(position).getLotcode()!=null && title.get(position).getLotcode().length()>0) {
            viewHolder.mTextViewLotcode.setText(title.get(position).getLotcode());
        }
        if(title.get(position).getCreation_date()!=null && title.get(position).getCreation_date().length()>0) {
            viewHolder.mTextViewDate.setText(title.get(position).getCreation_date());
        }

        String path = title.get(position).getImagepath();
        File imgFile = new  File(path);

        Picasso.with(context).load(path)
                .resize(500,400)
                .centerInside()
                .into(viewHolder.mImageView);

        /*for(int i = 1; i < title.get(position).getImagepath().size(); i++)
         {

*//*
            View itemList = null;

            itemList = LayoutInflater.from(viewHolder.mLinearLayoutPictures.getContext()).inflate(R.layout.addtitle_image,
                                                                                       viewHolder.mLinearLayoutPictures, false);
*//*


            path = title.get(position).getImagepath().get(i);
            imgFile = new  File(path);

            Picasso.with(context).load(path)
                                 .resize(500,400)
                                 .centerInside()
                                 .into(viewHolder.mImageView);

            viewHolder.mLinearLayoutPictures.addView(viewHolder.mImageView);

        }*/


    }

    @Override
    public int getItemCount() {

        return title.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLinearLayoutPictures;
        private TextView mTextViewLotcode;
        private TextView mTextViewDate;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mLinearLayoutPictures = (LinearLayout)itemView.findViewById(R.id.container_titlePhotos);
            mTextViewLotcode = (TextView)itemView.findViewById(R.id.tv_lotcode);
            mTextViewDate = (TextView)itemView.findViewById(R.id.tv_date);
            mImageView = (ImageView)itemView.findViewById(R.id.iv_title);
        }

    }
}

