package com.yukti.dataone.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class AuctionNameResult implements Serializable, Parcelable {
    public String message, status_code;
    public ArrayList<AuctionNameDetail> carList;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
