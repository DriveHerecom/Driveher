package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.R;
import com.yukti.utils.ParamsKey;

public class FRAGMiles extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    EditText edt_miles;

    public static FRAGMiles newInstance() {
        FRAGMiles f = new FRAGMiles();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_miles, container, false);

        edt_miles = (EditText) mView.findViewById(R.id.edt_Miles);

        InitNext();
        return mView;
    }

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

    void InitNext() {
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextMiles);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Gps");
        } else {
            btn_next.setText("Back << All Info");
        }
        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrMiles().equalsIgnoreCase(edt_miles.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_miles))
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_miles);
                }
                AddNewCarActivity.addCarModelObject.setStrMiles(edt_miles.getText().toString()
                        .trim());
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrMiles() != null
                && AddNewCarActivity.addCarModelObject.getStrMiles().length() != 0) {
            edt_miles.setText(AddNewCarActivity.addCarModelObject.getStrMiles());
        }
    }
}
