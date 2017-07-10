package com.yukti.newchanges.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.search.SearchActivity;

import java.util.Calendar;

public class AuctionFragment extends Fragment {

    EditText edtAuctionName, edtAuctionDate, edtCarReady, edtCarAtAuction;
    boolean clear ;

    public AuctionFragment(){}

    AuctionFragment(boolean clear) {
        this.clear = clear ;
    }

    public static AuctionFragment newInstance(boolean clear)
    {
        AuctionFragment fragment = new AuctionFragment(clear);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auction, container, false);
        initValues(view);
        initTextChangeListner();
        initAuctionName();
        initAuctionDate();
        initcarReady();
        initcarAtAuction();

        if (clear)
        {
            clearData();
        }

        return view;
    }

    void initValues(View view) {

        edtAuctionName = (EditText) view.findViewById(R.id.auctionName);
        edtAuctionDate = (EditText) view.findViewById(R.id.edt_auctiondate);
        edtCarReady = (EditText) view.findViewById(R.id.edt_car_ready);
        edtCarAtAuction = (EditText) view.findViewById(R.id.edt_car_at_auction);

        if (MainActivity.searchModel!=null)
        {
            edtAuctionName.setText(MainActivity.searchModel.getAuctionName());
            edtAuctionDate.setText(MainActivity.searchModel.getAuctionDate());
            edtCarReady.setText(MainActivity.searchModel.getCarReadyForAuction());
            edtCarAtAuction.setText(MainActivity.searchModel.getCarAtAuction());
        }
    }

    void clearData()
    {
        edtAuctionName.setText("");
        edtAuctionDate.setText("");
        edtCarReady.setText("");
        edtCarAtAuction.setText("");
    }

    void initAuctionDate()
    {
        edtAuctionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    void initAuctionName() {
        final String title = "Auction Name";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.Auction_Name);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edtAuctionName.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edtAuctionName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), "Choose Option");
            }
        });
    }

    void initcarReady() {

        final String title = "Car Ready For Auction?";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.car_ready);
        final SingleChoiceTextDialogFragment.ListDialogListener listener_car_ready = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edtCarReady.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edtCarReady.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener_car_ready);
                dialog.show(getActivity().getSupportFragmentManager(), "Choose Option");
            }
        });
    }

    void initcarAtAuction()
    {
        final String title = "Car At The Auction?";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.car_atauction);
        final SingleChoiceTextDialogFragment.ListDialogListener listener_car_ready = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edtCarAtAuction.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edtCarAtAuction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener_car_ready);
                dialog.show(getActivity().getSupportFragmentManager(), "Choose Option");
            }
        });
    }

    void initTextChangeListner() {
        edtAuctionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setAuctionName(s.toString());
            }
        });

        edtAuctionDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setAuctionDate(s.toString());
            }
        });

        edtCarReady.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setCarReadyForAuction(s.toString());
            }
        });

        edtCarAtAuction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setCarAtAuction(s.toString());
            }
        });
    }

    void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        String donedate;
                        monthOfYear = monthOfYear + 1;

                        String date;
                        if (dayOfMonth < 10) {
                            date = "0" + Integer.toString(dayOfMonth);
                        } else
                            date = Integer.toString(dayOfMonth);

                        if (monthOfYear < 10) {
                            donedate = year + "-" + "0" + monthOfYear + "-" + date;
                        } else {
                            donedate = year + "-" + monthOfYear + "-" + date;
                        }
                        edtAuctionDate.setText(donedate);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
}