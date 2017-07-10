package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.utils.ParamsKey;

/**
 * Created by Creadigol on 3/4/2016.
 */
public class FRAGService extends Fragment {
    private View mView;

    EditText edt_ServiceStage;
    String TAG_SERVICE_TYPE = "TAG_SERVICE_TYPE";
    CallbackAdd callbackAdd;

    public static FRAGService newInstance() {
        FRAGService f = new FRAGService();
        return f;
    }

    public interface OnTagSelectedListener {
        public void onTagSelected(int tagPosition, String strTag);

        public void onTagLongPress(int tagPosition, String strTag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_service, container, false);
        edt_ServiceStage = (EditText) mView.findViewById(R.id.edt_ServiceStage);
        initServiceStage();
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
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextServiceStage);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Lotcode");
        } else {
            btn_next.setText("Back << All Info");
        }

        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrServiceStage().equalsIgnoreCase(edt_ServiceStage.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_serviceStage))
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_serviceStage);
                }
                AddNewCarActivity.addCarModelObject.setStrServiceStage(edt_ServiceStage.getText().toString()
                        .trim());
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void initServiceStage() {

        final String title = "Choose Service Stage";
        final CharSequence[] driveTypeList = getResources().getStringArray(
                R.array.ServiceStage);
        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_ServiceStage.setText(driveTypeList[position]);
            }

            @Override
            public void onDialogNegativeClick(
                    android.support.v4.app.DialogFragment dialog) {
                // TODO Auto-generated method stub

            }
        };

        edt_ServiceStage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, driveTypeList, listener);
                dialog.show(getChildFragmentManager(), TAG_SERVICE_TYPE);
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrServiceStage() != null
                && AddNewCarActivity.addCarModelObject.getStrServiceStage().length() != 0) {
            edt_ServiceStage.setText(AddNewCarActivity.addCarModelObject.getStrServiceStage());
        }
    }

}
