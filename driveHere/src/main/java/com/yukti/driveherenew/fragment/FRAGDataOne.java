package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.ColorChoiceDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.ParamsKey;

public class FRAGDataOne extends Fragment {

    View view_main;

    CallbackAdd callbackAdd;

    EditText edt_color, edt_make, edt_model, edt_modelyear, edt_modelnumber,
            edt_maxhp, edt_maxtorque, edt_fueltype, edt_oilcapacity,
            edt_drivetype, edt_cylinder, edt_vehicletype;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackAdd = (CallbackAdd) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackAdd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_main = (inflater.inflate(R.layout.fragment_dataoneinfo, container,
                false));
        Log.e("ON NEXT BUTTON YES", " " + AddNewCarActivity.addCarModelObject.getStrHasTitle());
        init();
        initColor();
        initMake();
        initFuelType();
        initDriveType();
        initnext();
        return view_main;
    }

    void init() {
        edt_color = (EditText) view_main.findViewById(R.id.edt_vehiclecolor);
        edt_make = (EditText) view_main.findViewById(R.id.edt_vehiclemake);
        edt_model = (EditText) view_main.findViewById(R.id.edt_vehiclemodel);
        edt_modelyear = (EditText) view_main
                .findViewById(R.id.edt_vehiclemodelyear);
        edt_modelnumber = (EditText) view_main
                .findViewById(R.id.edt_vehiclemodelnumber);
        edt_maxhp = (EditText) view_main.findViewById(R.id.edt_vehiclemaxhp);
        edt_maxtorque = (EditText) view_main
                .findViewById(R.id.edt_vehiclemaxtorque);
        edt_fueltype = (EditText) view_main
                .findViewById(R.id.edt_vehiclefueltype);
        edt_oilcapacity = (EditText) view_main
                .findViewById(R.id.edt_vehicleoilcapacity);
        edt_drivetype = (EditText) view_main
                .findViewById(R.id.edt_vehicledrivetype);

        edt_cylinder = (EditText) view_main
                .findViewById(R.id.edt_vehiclecylinder);
        edt_vehicletype = (EditText) view_main
                .findViewById(R.id.edt_vehicletype);
    }

    void initnext() {
        Button btn_next = (Button) view_main
                .findViewById(R.id.btn_nextdataoneinfo);

        // if (!AddNewCarActivity.isFrmInfo) {
        // btn_next.setText("Next >> Dat");
        // } else {
        // btn_next.setText("Back << All Info");
        // }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setstaticvalue();
                // callbackAdd.onNextSecected(Fragments.dataonefrag, false);
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void setstaticvalue() {
        if (AddNewCarActivity.isedit) {
            if (!AddNewCarActivity.addCarModelObject.getStrColor().equalsIgnoreCase(edt_color.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_color)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_color);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrMake().equalsIgnoreCase(edt_make.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_make)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_make);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrModelNumber().equalsIgnoreCase(edt_modelnumber.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_modelNumber)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_modelNumber);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrModelYear().equalsIgnoreCase(edt_modelyear.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_modelYear)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_modelYear);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrMaxHp().equalsIgnoreCase(edt_maxhp.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_maxHP)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_maxHP);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrMaxTorque().equalsIgnoreCase(edt_maxtorque.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_maxTorque)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_maxTorque);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrFuelType().equalsIgnoreCase(edt_fueltype.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_fuelType)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_fuelType);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrOilCapacity().equalsIgnoreCase(edt_oilcapacity.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_oilCapacity)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_oilCapacity);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrDriveType().equalsIgnoreCase(edt_drivetype.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_driveType)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_driveType);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrCylinder().equalsIgnoreCase(edt_cylinder.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_cylinder)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_cylinder);
            }
            if (!AddNewCarActivity.addCarModelObject.getStrVehicleType().equalsIgnoreCase(edt_vehicletype.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_vehicleType)) {
                AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_vehicleType);
            }
        }
        AddNewCarActivity.addCarModelObject.setStrColor(edt_color.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrcolorcode(edt_color.getTag().toString());
        AddNewCarActivity.addCarModelObject.setStrMake(edt_make.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrModel(edt_model.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrModelNumber(edt_modelnumber.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrModelYear(edt_modelyear.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrMaxHp(edt_maxhp.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrMaxTorque(edt_maxtorque.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrFuelType(edt_fueltype.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrOilCapacity(edt_oilcapacity.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrDriveType(edt_drivetype.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrCylinder(edt_cylinder.getText().toString());
        AddNewCarActivity.addCarModelObject.setStrVehicleType(edt_vehicletype.getText().toString());
    }

    public static FRAGDataOne newInstance() {
        FRAGDataOne f = new FRAGDataOne();
        return f;
    }

    void initColor() {

        final String TAG_COLOR = "COLOR";
        final String title = "Choose Color";
        final CharSequence[] colorNameList = getResources().getStringArray(
                R.array.ColorName);
        final CharSequence[] colorValueList = getResources().getStringArray(
                R.array.ColorValue);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_color.setText(colorNameList[position]);
                edt_color.setTag(colorValueList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            }
        };

        edt_color.setTag("");
        edt_color.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ColorChoiceDialogFragment dialog = new ColorChoiceDialogFragment(
                        listener);
                dialog.show(getChildFragmentManager(), TAG_COLOR);

                // ColorChoiceDialogFragment dialog = new
                // ColorChoiceDialogFragment(listener);
                // dialog.show(getSupportFragmentManager(), TAG_COLOR);
            }
        });
    }

    void initMake() {
        final String TAG_MAKE = "MAKE";
        final String title = "Choose Make";
        final CharSequence[] makeList = getResources().getStringArray(
                R.array.Make);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_make.setText(makeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_make.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, makeList, listener);
                dialog.show(getChildFragmentManager(), TAG_MAKE);
            }
        });
    }

    void initFuelType() {

        final String TAG_FUEL_TYPE = "FUEL TYPE";
        final String title = "Choose Fuel Type";
        final CharSequence[] statusList = getResources().getStringArray(
                R.array.FuelType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_fueltype.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }

        };

        edt_fueltype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, statusList, listener);
                dialog.show(getChildFragmentManager(), TAG_FUEL_TYPE);
            }
        });
    }

    void initDriveType() {
        final String TAG_DRIVE_TYPE = "DRIVE TYPE";
        final String title = "Choose Drive Type";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.DriveType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_drivetype.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };
        edt_drivetype.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getChildFragmentManager(), TAG_DRIVE_TYPE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setvalues();
    }

    void setvalues() {

        if (AddNewCarActivity.addCarModelObject.getStrColor() != null
                && AddNewCarActivity.addCarModelObject.getStrColor().length() != 0) {
            edt_color.setText(AddNewCarActivity.addCarModelObject.getStrColor());
        }
        if (AddNewCarActivity.addCarModelObject.getStrMake() != null
                && AddNewCarActivity.addCarModelObject.getStrMake().length() != 0) {
            edt_make.setText(AddNewCarActivity.addCarModelObject.getStrMake());
        }
        if (AddNewCarActivity.addCarModelObject.getStrModel() != null
                && AddNewCarActivity.addCarModelObject.getStrModel().length() != 0) {
            edt_model.setText(AddNewCarActivity.addCarModelObject.getStrModel());
        }
        if (AddNewCarActivity.addCarModelObject.getStrModelNumber() != null
                && AddNewCarActivity.addCarModelObject.getStrModelNumber().length() != 0) {
            edt_modelnumber.setText(AddNewCarActivity.addCarModelObject.getStrModelNumber());
        }
        if (AddNewCarActivity.addCarModelObject.getStrModelYear() != null
                && AddNewCarActivity.addCarModelObject.getStrModelYear().length() != 0) {
            edt_modelyear.setText(AddNewCarActivity.addCarModelObject.getStrModelYear());
        }
        if (AddNewCarActivity.addCarModelObject.getStrMaxHp() != null
                && AddNewCarActivity.addCarModelObject.getStrMaxHp().length() != 0) {
            edt_maxhp.setText(AddNewCarActivity.addCarModelObject.getStrMaxHp());
        }
        if (AddNewCarActivity.addCarModelObject.getStrMaxTorque() != null
                && AddNewCarActivity.addCarModelObject.getStrMaxTorque().length() != 0) {
            edt_maxtorque.setText(AddNewCarActivity.addCarModelObject.getStrMaxTorque());
        }
        if (AddNewCarActivity.addCarModelObject.getStrFuelType() != null
                && AddNewCarActivity.addCarModelObject.getStrFuelType().length() != 0) {
            edt_fueltype.setText(AddNewCarActivity.addCarModelObject.getStrFuelType());
        }
        if (AddNewCarActivity.addCarModelObject.getStrOilCapacity() != null
                && AddNewCarActivity.addCarModelObject.getStrOilCapacity().length() != 0) {
            edt_oilcapacity.setText(AddNewCarActivity.addCarModelObject.getStrOilCapacity());
        }
        if (AddNewCarActivity.addCarModelObject.getStrDriveType() != null
                && AddNewCarActivity.addCarModelObject.getStrDriveType().length() != 0) {
            edt_drivetype.setText(AddNewCarActivity.addCarModelObject.getStrDriveType());
        }

        if (AddNewCarActivity.addCarModelObject.getStrCylinder() != null
                && AddNewCarActivity.addCarModelObject.getStrCylinder().length() != 0) {
            edt_cylinder.setText(AddNewCarActivity.addCarModelObject.getStrCylinder());
        }
        if (AddNewCarActivity.addCarModelObject.getStrVehicleType() != null
                && AddNewCarActivity.addCarModelObject.getStrVehicleType().length() != 0) {
            edt_vehicletype.setText(AddNewCarActivity.addCarModelObject.getStrVehicleType());
        }
    }
}
