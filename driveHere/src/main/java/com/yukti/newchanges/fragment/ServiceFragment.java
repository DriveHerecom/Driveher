package com.yukti.newchanges.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;

import java.util.Calendar;

public class ServiceFragment extends Fragment {

    EditText edtSerStage , edtSerProblem , edtSerDoneDate , edtSeDoneDateLotCode , edtSerMechanic ;
    boolean clear ;

    public ServiceFragment(){}

    ServiceFragment(boolean clear) {
        this.clear = clear ;
    }

    public static ServiceFragment newInstance(boolean clear) {
        ServiceFragment fragment = new ServiceFragment(clear);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_search, container, false);
        initValues(view);
        initTextChangeListner();

        initServiceStage();
        initProblem();
        initDoneDate();
        initDonedateLotcode();

        if (clear)
        {
            clearData();
        }

        return view;
    }

    void initValues(View view)
    {
        edtSerMechanic = (EditText) view.findViewById(R.id.edt_MechanicSearch);
        edtSerStage = (EditText) view.findViewById(R.id.service_stage);
        edtSerProblem = (EditText) view.findViewById(R.id.problem);
        edtSerDoneDate = (EditText) view.findViewById(R.id.done_date);
        edtSeDoneDateLotCode = (EditText) view.findViewById(R.id.done_dateLotcode);

        if (MainActivity.searchModel!=null)
        {
            edtSerStage.setText(MainActivity.searchModel.getSerStage());
            edtSerProblem.setText(MainActivity.searchModel.getSerProblem());
            edtSerDoneDate.setText(MainActivity.searchModel.getSerDoneDate());
            edtSeDoneDateLotCode.setText(MainActivity.searchModel.getSerDoneDateLotcode());
            edtSerMechanic.setText(MainActivity.searchModel.getSerMechanic());
        }
    }

    void clearData()
    {
        edtSerStage.setText("");
        edtSerProblem.setText("");
        edtSerDoneDate.setText("");
        edtSeDoneDateLotCode.setText("");
        edtSerMechanic.setText("");
    }

    void initTextChangeListner()
    {
        edtSerStage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setSerStage(s.toString());
            }
        });

        edtSerProblem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setSerProblem(s.toString());
            }
        });

        edtSerDoneDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setSerDoneDate(s.toString());
            }
        });

        edtSeDoneDateLotCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setSerDoneDateLotcode(s.toString());
            }
        });

        edtSerMechanic.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setSerMechanic(s.toString());
            }
        });
    }

    void initServiceStage()
    {
        final String title = "Choose Service Stage";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.ServiceStage);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {
            @Override
            public void onItemClick(int position) {
                edtSerStage.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edtSerStage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), "Service Stage");
            }
        });
    }

    void initProblem() {
        final String title = "Choose Problem";
        final CharSequence[] statusList = getResources().getStringArray(R.array.Problem);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener()
        {
            @Override
            public void onItemClick(int position) {
                edtSerProblem.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edtSerProblem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), "Service Problem");
            }
        });
    }

    void initDoneDate() {
        edtSerDoneDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    void initDonedateLotcode() {
        final String title = "Done date Lotcode";
        final String TAG_HAS_RFID = "CHOOSE OPTION";
        final CharSequence[] doneDateLotcodeList = getResources().getStringArray(
                R.array.Lotcode);
        final SingleChoiceTextDialogFragment.ListDialogListener DonedateLotListener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edtSeDoneDateLotCode.setText(doneDateLotcodeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edtSeDoneDateLotCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, doneDateLotcodeList, DonedateLotListener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG_HAS_RFID);
            }
        });

    }

    void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {

                        monthOfYear = monthOfYear + 1;
                        String month,day;

                        if (monthOfYear < 10) {
                            month = "0" + monthOfYear ;
                        } else {
                            month = "" + monthOfYear ;
                        }

                        if (dayOfMonth<10)
                        {
                            day = "0" + dayOfMonth ;
                        }
                        else
                        {
                            day = "" + dayOfMonth ;
                        }
                        edtSerDoneDate.setText(year+"-"+month+"-"+day);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
}
