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

import com.yukti.driveherenew.ColorChoiceDialogFragment;
import com.yukti.driveherenew.MainActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import java.util.Calendar;

public class CarDetailFragment extends Fragment
{
    EditText edt_cdColor,edt_cdModelNumber,edt_cdMaxHP,edt_cdSalesprice,edt_cdMiles,edt_cdFuelType,edt_cdDriveType,edt_cdCylinder,edt_cdVehicleType,
            edt_cdTitle,edt_cdModel,edt_cdLocation,edt_cdCompany,
            edt_cdStockNumber,edt_cdPurchasedFrom,edt_cdGas,edt_cdNote,edt_cdNoteDate,edt_cdGpsIntalled;

    boolean clear;

    String TAG = "Choose Option";

    public CarDetailFragment(){}

    CarDetailFragment(boolean clear) {
        this.clear = clear ;
    }

    public static CarDetailFragment newInstance(boolean clear)
    {
        CarDetailFragment fragment = new CarDetailFragment(clear);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_car_detail, container, false);
        initValues(view);

        initColor();
        initSalesPrice();
        initFuelType();
        initDriveType();
        initTitle();
        initLocation();
        initGasTank();
        initNoteDate();
        initGpsInstalled();
        initTextChangeListner();

        if (clear)
        {
            clearData();
        }
        return view;
    }

    void initValues(View view)
    {
        edt_cdColor = (EditText) view.findViewById(R.id.color);
        edt_cdModelNumber = (EditText) view.findViewById(R.id.model_number);
        edt_cdMaxHP = (EditText) view.findViewById(R.id.maxHP);
        edt_cdSalesprice = (EditText) view.findViewById(R.id.sales_price);
        edt_cdMiles = (EditText) view.findViewById(R.id.edt_Miles);
        edt_cdFuelType = (EditText) view.findViewById(R.id.fuel_type);
        edt_cdDriveType = (EditText) view.findViewById(R.id.driveType);
        edt_cdCylinder = (EditText) view.findViewById(R.id.edcylinder);
        edt_cdVehicleType = (EditText) view.findViewById(R.id.vehicleType);
        edt_cdTitle = (EditText) view.findViewById(R.id.title);
        edt_cdModel = (EditText) view.findViewById(R.id.model);
        edt_cdLocation = (EditText) view.findViewById(R.id.edt_location);
        edt_cdCompany = (EditText) view.findViewById(R.id.company);
        edt_cdStockNumber = (EditText) view.findViewById(R.id.stockNumber);
        edt_cdPurchasedFrom = (EditText) view.findViewById(R.id.purchasedFrom);
        edt_cdGas = (EditText) view.findViewById(R.id.edt_gas_tank);
        edt_cdNote = (EditText) view.findViewById(R.id.note);
        edt_cdNoteDate = (EditText) view.findViewById(R.id.note_date);
        edt_cdGpsIntalled = (EditText) view.findViewById(R.id.gps_installed);

        if (MainActivity.searchModel!=null)
        {
            edt_cdColor.setText(MainActivity.searchModel.getCdColor());
            edt_cdModelNumber.setText(MainActivity.searchModel.getCdModelNumber());

            if (MainActivity.searchModel.getCdSalesPriceMin()!=null && MainActivity.searchModel.getCdSalesPriceMin().length()>0)
            {
                edt_cdSalesprice.setText(MainActivity.searchModel.getCdSalesPriceMin().replace("-"," - "));
            }

            edt_cdMiles.setText(MainActivity.searchModel.getCdMiles());
            edt_cdFuelType.setText(MainActivity.searchModel.getCdFuelType());
            edt_cdDriveType.setText(MainActivity.searchModel.getCdDriveType());
            edt_cdCylinder.setText(MainActivity.searchModel.getCdCylinder());
            edt_cdVehicleType.setText(MainActivity.searchModel.getCdVehicleType());
            edt_cdTitle.setText(MainActivity.searchModel.getCdTitle());
            edt_cdModel.setText(MainActivity.searchModel.getCdModel());
            edt_cdLocation.setText(MainActivity.searchModel.getCdLocation());
            edt_cdCompany.setText(MainActivity.searchModel.getCdCompany());
            edt_cdStockNumber.setText(MainActivity.searchModel.getCdStocknumber());
            edt_cdPurchasedFrom.setText(MainActivity.searchModel.getCdPurchasedFrom());
            edt_cdGas.setText(MainActivity.searchModel.getCdGasTank());
            edt_cdNote.setText(MainActivity.searchModel.getCdNote());
            edt_cdNoteDate.setText(MainActivity.searchModel.getCdNoteDate());
            edt_cdGpsIntalled.setText(MainActivity.searchModel.getCdGpsInstalled());
            edt_cdMaxHP.setText(MainActivity.searchModel.getCdMaxHP());
        }
    }

    void clearData()
    {
        edt_cdColor.setText("");
        edt_cdModelNumber.setText("");
        edt_cdSalesprice.setText("");
        edt_cdMiles.setText("");
        edt_cdFuelType.setText("");
        edt_cdDriveType.setText("");
        edt_cdCylinder.setText("");
        edt_cdVehicleType.setText("");
        edt_cdTitle.setText("");
        edt_cdModel.setText("");
        edt_cdLocation.setText("");
        edt_cdCompany.setText("");
        edt_cdStockNumber.setText("");
        edt_cdPurchasedFrom.setText("");
        edt_cdGas.setText("");
        edt_cdNote.setText("");
        edt_cdNoteDate.setText("");
        edt_cdGpsIntalled.setText("");
        edt_cdMaxHP.setText("");
    }

    void initTextChangeListner()
    {
        edt_cdModelNumber.addTextChangedListener(new TextWatcher() {

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
                MainActivity.searchModel.setCdModelNumber(s.toString());
            }
        });

        edt_cdMaxHP.addTextChangedListener(new TextWatcher() {

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
                MainActivity.searchModel.setCdMaxHP(s.toString());
            }
        });

        edt_cdColor.addTextChangedListener(new TextWatcher() {

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
                MainActivity.searchModel.setCdColor(s.toString());
            }
        });

        edt_cdSalesprice.addTextChangedListener(new TextWatcher() {

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
                if (s.length()>0)
                {
                    MainActivity.searchModel.setCdSalesPriceMin(s.toString().replaceAll(" ","").replaceAll(",",""));
                }
            }
        });

        edt_cdMiles.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdMiles(s.toString());
            }
        });

        edt_cdFuelType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdFuelType(s.toString());
            }
        });

        edt_cdDriveType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdDriveType(s.toString());
            }
        });

        edt_cdCylinder.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdCylinder(s.toString());
            }
        });

        edt_cdVehicleType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdVehicleType(s.toString());
            }
        });

        edt_cdTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdTitle(s.toString());
            }
        });

        edt_cdModel.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdModel(s.toString());
            }
        });

        edt_cdLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdLocation(s.toString());
            }
        });

        edt_cdCompany.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdCompany(s.toString());
            }
        });

        edt_cdStockNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdStocknumber(s.toString());
            }
        });

        edt_cdPurchasedFrom.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdPurchasedFrom(s.toString());
            }
        });

        edt_cdGas.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdGasTank(s.toString());
            }
        });

        edt_cdNote.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdNote(s.toString());
            }
        });

        edt_cdNoteDate.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdNoteDate(s.toString());
            }
        });

        edt_cdGpsIntalled.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                MainActivity.searchModel.setCdGpsInstalled(s.toString());
            }
        });
}

    void initColor()
    {
        final CharSequence[] colorNameList = getResources().getStringArray(R.array.ColorName);
        final CharSequence[] colorValueList = getResources().getStringArray(R.array.ColorValue);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener()
        {
            @Override
            public void onItemClick(int position) {
                edt_cdColor.setText(colorNameList[position]);
                edt_cdColor.setTag(colorValueList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_cdColor.setTag("");
        edt_cdColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChoiceDialogFragment dialog = new ColorChoiceDialogFragment(listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initSalesPrice()
    {
        final String title = "Choose Price(USD)";
        final CharSequence[] priceList = getResources().getStringArray(R.array.PriceList);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                String value = priceList[position].toString();
                edt_cdSalesprice.setText(value);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdSalesprice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, priceList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initFuelType()
    {
        final String title = "Choose Fuel Type";
        final CharSequence[] statusList = getResources().getStringArray(R.array.FuelType);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdFuelType.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdFuelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initDriveType() {

        final String title = "Choose Drive Type";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.DriveType);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdDriveType.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdDriveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initTitle() {

        final String title = "Choose Title";
        final CharSequence[] statusList = getResources().getStringArray(R.array.Title_Serch);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdTitle.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initLocation()
    {
        final String title = "Choose Location Title";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.location_title);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdLocation.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    private void initGasTank() {
        final String title = "Select GasTank";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.GasTankList);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdGas.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    void initNoteDate()
    {
        edt_cdNoteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    void initGpsInstalled()
    {
        final String title = "Gps Installed?";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.Gps);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_cdGpsIntalled.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_cdGpsIntalled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getActivity().getSupportFragmentManager(), TAG);
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
                        String donedate;
                        monthOfYear = monthOfYear + 1;

                        String date;
                        if (dayOfMonth < 10) {

                            date = "0" + Integer.toString(dayOfMonth);

                        } else
                            date = Integer.toString(dayOfMonth);

                        if (monthOfYear < 10) {
                            donedate = year + "-" + "0" + monthOfYear + "-"
                                    + date;
                        } else {
                            donedate = year + "-" + monthOfYear + "-"
                                    + date;
                        }
                        edt_cdNoteDate.setText(donedate);
                    }

                }, mYear, mMonth, mDay);
        dpd.show();
    }
}
