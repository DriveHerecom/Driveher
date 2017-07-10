package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yukti.driveherenew.AddNewCarActivity.Fragments;
import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.utils.ParamsKey;

public class FRAGStage extends Fragment {

    private View mView;

    EditText edt_Stage;
    String TAG_STAGE_TYPE = "TAG_STAGE_TYPE";
    CallbackAdd callbackAdd;

    public static FRAGStage newInstance() {
        FRAGStage f = new FRAGStage();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_stage, container, false);
        edt_Stage = (EditText) mView.findViewById(R.id.edt_Stage);
        initStage();
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
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextStage);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> ServiceStage");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrStage().contains(edt_Stage.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_stage)) {
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_stage);
                    }
                }
                AddNewCarActivity.addCarModelObject.setStrStage(edt_Stage.getText().toString()
                        .trim());
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void initStage() {

        final String title = "Choose Stage";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.StageType);
        final ListDialogListener listener = new ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_Stage.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(
                    android.support.v4.app.DialogFragment dialog) {
                // TODO Auto-generated method stub

            }
        };

        edt_Stage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getChildFragmentManager(), TAG_STAGE_TYPE);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrStage() != null
                && AddNewCarActivity.addCarModelObject.getStrStage().length() != 0) {
            edt_Stage.setText(AddNewCarActivity.addCarModelObject.getStrStage());
        }
    }
}
