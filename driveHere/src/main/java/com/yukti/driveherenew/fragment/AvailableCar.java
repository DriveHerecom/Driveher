package com.yukti.driveherenew.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yukti.adapter.AuctionCarAdapter;
import com.yukti.driveherenew.AuctionCarDetailsActivity;
import com.yukti.driveherenew.AuctionLotAvailableDetailsActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.search.CarDetailsActivity;
import com.yukti.driveherenew.search.CarInventory;
import java.util.ArrayList;

public class AvailableCar extends Fragment {

    ArrayList<CarInventory> availableCarList;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView tvNoData;
    AuctionCarAdapter adapter;

    public static AvailableCar newInstance(ArrayList<CarInventory> available_carList) {
        AvailableCar f = new AvailableCar();
        f.availableCarList = available_carList;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_available, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_availableCar);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        tvNoData = (TextView) view.findViewById(R.id.tv_no_availablecar);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        availableCarList = AuctionLotAvailableDetailsActivity.availableCarList;
        initRecyclerView();
    }

    void initRecyclerView()
    {
        if (availableCarList != null && availableCarList.size() > 0) {

            adapter = new AuctionCarAdapter(availableCarList, getActivity());
            adapter.SetOnItemClickListener(new AuctionCarAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(),AuctionCarDetailsActivity.class);
                    intent.putExtra(CarDetailsActivity.EXTRAKEY_VIN, adapter.CarList.get(position).vin);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        else {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

}