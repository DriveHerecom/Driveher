package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.ParamsKey;

public class FRAGStatus extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    EditText edt_Status;
    String TAG_VEHICLE_STATUS = "TAG_VEHICLE_STATUS";

    public static FRAGStatus newInstance() {
        FRAGStatus f = new FRAGStatus();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackAdd = (CallbackAdd) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallbackAdd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_status, container, false);
        edt_Status = (EditText) mView.findViewById(R.id.edt_Status);
        initStatusOfVehicle();
        initnext();
        return mView;
    }

    void initnext() {
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextStatus);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Stage");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrStatus().contains(edt_Status.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_vehicleStatus)) {
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_vehicleStatus);
                    }
                }
                AddNewCarActivity.addCarModelObject.setStrStatus(edt_Status.getText().toString().trim());
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void initStatusOfVehicle() {
        final String title = "Choose Vehicle Status";
        final CharSequence[] statusList = getResources().getStringArray(R.array.VehicleStatus);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_Status.setText(statusList[position]);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_Status.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(title, statusList, listener);
                dialog.show(getChildFragmentManager(), TAG_VEHICLE_STATUS);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrStatus() != null && AddNewCarActivity.addCarModelObject.getStrStatus().length() != 0) {
            edt_Status.setText(AddNewCarActivity.addCarModelObject.getStrStatus());
        }
    }
}
