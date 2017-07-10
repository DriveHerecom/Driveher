package com.yukti.newchanges.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;

public class StatusesFragment extends Fragment {

    EditText edt_vehStatus , edt_vehHasRfid ,edt_vehVacancy , edt_vehStage;
    boolean clear ;
    final String TAG = "CHOOSE OPTION";

    public StatusesFragment(){}

    StatusesFragment(boolean clear) {
        this.clear = clear ;
    }

    public static StatusesFragment newInstance(boolean clear) {
        StatusesFragment fragment = new StatusesFragment(clear);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_statuses, container, false);

        initValues(view);
        initTextChangeListner();

        initVehicleStatus();
        initHasRfid();
        initVacancy();
        initVehicleStage();

        if (clear)
        {
            clearData();
        }

        return view;
    }

    void initValues(View view)
    {
        edt_vehStatus = (EditText) view.findViewById(R.id.vehicle_status);
        edt_vehHasRfid = (EditText) view.findViewById(R.id.edt_hasRfid);
        edt_vehVacancy = (EditText) view.findViewById(R.id.edt_vacancySearch);
        edt_vehStage = (EditText) view.findViewById(R.id.vehicle_stage);

        if(MainActivity.searchModel!=null)
        {
            edt_vehVacancy.setText(MainActivity.searchModel.getVehVacancy());
            edt_vehHasRfid.setText(MainActivity.searchModel.getVehHasRfid());
            edt_vehStatus.setText(MainActivity.searchModel.getVehStatus());
            edt_vehStage.setText(MainActivity.searchModel.getVehStage());
        }
    }

    void clearData()
    {
        edt_vehVacancy.setText("");
        edt_vehHasRfid.setText("");
        edt_vehStatus.setText("");
        edt_vehStage.setText("");
    }

    void initTextChangeListner()
    {
        edt_vehVacancy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setVehVacancy(s.toString());
            }
        });

        edt_vehHasRfid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setVehHasRfid(s.toString());
            }
        });

        edt_vehStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setVehStatus(s.toString());
            }
        });

        edt_vehStage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.searchModel.setVehStage(s.toString());
            }
        });
    }

    void initVehicleStatus() {
        final String title = "Choose Vehicle Status";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.VehicleStatus);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                edt_vehStatus.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_vehStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initHasRfid() {
        final String title = "Car Has Rfid";
        final CharSequence[] rfIdOptionArray = getResources().getStringArray(
                R.array.car_ready);
        final SingleChoiceTextDialogFragment.ListDialogListener listener_has_rfid = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_vehHasRfid.setText(rfIdOptionArray[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };
        edt_vehHasRfid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, rfIdOptionArray, listener_has_rfid);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initVacancy() {
        final String title = "Vacancy";
        final CharSequence[] vacancyList = getResources().getStringArray(
                R.array.vacancylist);
        final SingleChoiceTextDialogFragment.ListDialogListener VacancyListener = new SingleChoiceTextDialogFragment.ListDialogListener()
        {
            @Override
            public void onItemClick(int position) {
                edt_vehVacancy.setText(vacancyList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_vehVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, vacancyList, VacancyListener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initVehicleStage() {
        final String title = "Choose Vehicle Stage";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.StageType);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_vehStage.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_vehStage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }
}
