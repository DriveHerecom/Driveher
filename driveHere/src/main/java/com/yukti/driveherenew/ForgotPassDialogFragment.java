package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ForgotPassDialogFragment extends DialogFragment {

	String title, message;
	
	ForgotPassDialogFragment(String title, String message){
		this.title = title;
		this.message = message;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setIcon(getActivity().getResources().getDrawable(R.drawable.app_icon));
		dialog.setTitle(title);
		dialog.setMessage(message);
		return dialog;
	}

}
