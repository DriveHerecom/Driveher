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

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.ParamsKey;

public class FRAGLotcode extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    EditText edt_lotcode;

    public static FRAGLotcode newInstance() {
        FRAGLotcode f = new FRAGLotcode();
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
            throw new ClassCastException(activity.toString()
                    + " must implement CallbackAdd");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_lotcode, container, false);
        edt_lotcode = (EditText) mView.findViewById(R.id.edt_lotcode);
        initLotCode();
        initnext();
        return mView;
    }

    void initnext() {
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextlotcode);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Miles");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrLotCode().equalsIgnoreCase(edt_lotcode.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_lotCode))
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_lotCode);
                }
                AddNewCarActivity.addCarModelObject.setStrLotCode(edt_lotcode.getText()
                        .toString().trim());
                callbackAdd.onNextSecected(false, null);

            }
        });
    }

    void initLotCode() {
        // lotCode = (EditText) findViewById(R.carId.lotCode);
        final String title = "Choose Lot Code";
        final CharSequence[] lotList = getResources().getStringArray(
                R.array.Lotcode);
        // final CharSequence[] colorValueList = getResources().getStringArray(
        // R.array.ColorValue);

        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_lotcode.setText(lotList[position]);
                // lotCode.setTag(colorValueList);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        };

        edt_lotcode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

				/*
                 * SingleChoiceTextDialogFragment dialog = new
				 * SingleChoiceTextDialogFragment( title,lotList,listener);
				 * dialog.show(getSupportFragmentManager(),TAG_VEHICLE_STATUS);
				 */
                /*
				 * ColorChoiceDialogFragment dialog1 = new
				 * ColorChoiceDialogFragment( listener);
				 * dialog1.show(getSupportFragmentManager(), TAG_COLOR);
				 */
                String TAG_COLOR = "TAG_COLOR";
                LotcodeChoiceDialogFragment dialog1 = new LotcodeChoiceDialogFragment(
                        listener);
                dialog1.show(getChildFragmentManager(), TAG_COLOR);

            }
        });

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrLotCode() != null
                && AddNewCarActivity.addCarModelObject.getStrLotCode().length() != 0) {
            edt_lotcode.setText(AddNewCarActivity.addCarModelObject.getStrLotCode());
        }
    }
}
