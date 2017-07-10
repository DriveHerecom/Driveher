package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SingleChoiceTextDialogFragment extends DialogFragment {

    private String mTitle;
    private ListDialogListener mListener;
    private CharSequence[] mItems;

    @Override
	public void onCreate(Bundle state) {
        super.onCreate(state);
      //  setRetainInstance(true);
    }

    public SingleChoiceTextDialogFragment(String title, CharSequence[] items, ListDialogListener listener) {
        mTitle = title;
        mListener = listener;
        mItems = items;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle);
        
        builder.setItems(mItems, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mListener != null) {
                    mListener.onItemClick(which);
                }
			}
		});
       
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogNegativeClick(SingleChoiceTextDialogFragment.this);
                }
            }
        });

        return builder.create();
    }
    
    public interface ListDialogListener {
        public void onItemClick(int position);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
