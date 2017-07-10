package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yukti.driveherenew.AddNewCarActivity;
import com.yukti.driveherenew.LotcodeChoiceDialogFragment;
import com.yukti.driveherenew.R;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment;
import com.yukti.utils.ParamsKey;

public class FRAGVacancy extends Fragment {

    private View mView;
    CallbackAdd callbackAdd;
    EditText edt_Vacancy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_vacancy, container, false);
        edt_Vacancy = (EditText) mView.findViewById(R.id.edt_vacancy);
        initVacancy();
        initnext();
        return mView;
    }

    public static FRAGVacancy newInstance() {
        FRAGVacancy f = new FRAGVacancy();
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

    void initnext() {
        Button btn_next = (Button) mView.findViewById(R.id.btn_nextvacancy);
        if (!AddNewCarActivity.addCarModelObject.isFrmInfo()) {
            btn_next.setText("Next >> Status");
        } else {
            btn_next.setText("Back << All Info");
        }
        btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AddNewCarActivity.isedit) {
                    if (!AddNewCarActivity.addCarModelObject.getStrVacancy().equalsIgnoreCase(edt_Vacancy.getText().toString()) && !AddNewCarActivity.addCarModelObject.editFilied.contains(ParamsKey.KEY_vacancy))
                        AddNewCarActivity.addCarModelObject.editFilied.add(ParamsKey.KEY_vacancy);
                }
                AddNewCarActivity.addCarModelObject.setStrVacancy(edt_Vacancy.getText()
                        .toString().trim());
                callbackAdd.onNextSecected(false, null);
            }
        });
    }

    void initVacancy() {
        // lotCode = (EditText) findViewById(R.carId.lotCode);
        final String title = "Choose Vacancy";
        final CharSequence[] VacacnyArray = getResources().getStringArray(
                R.array.vacancylist);
        // final CharSequence[] colorValueList = getResources().getStringArray(
        // R.array.ColorValue);

        final SingleChoiceTextDialogFragment.ListDialogListener listener = new SingleChoiceTextDialogFragment.ListDialogListener() {

            @Override
            public void onItemClick(int position) {
                edt_Vacancy.setText(VacacnyArray[position]);
            }

            @Override
            public void onDialogNegativeClick(
                    DialogFragment dialog) {
            }
        };
        edt_Vacancy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String TAG_VACANCY = "VACANCY";
                SingleChoiceTextDialogFragment dialog = new SingleChoiceTextDialogFragment(
                        title, VacacnyArray, listener);
                dialog.show(getChildFragmentManager(), TAG_VACANCY);
            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        if (AddNewCarActivity.addCarModelObject.getStrVacancy() != null
                && AddNewCarActivity.addCarModelObject.getStrVacancy().length() != 0) {
            edt_Vacancy.setText(AddNewCarActivity.addCarModelObject.getStrVacancy());
        }
    }
}
