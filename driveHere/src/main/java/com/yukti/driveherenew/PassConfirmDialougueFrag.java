package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

public class PassConfirmDialougueFrag extends DialogFragment {

	String title, message,pass="";
	private PassDialogListener mListener;
	
	PassConfirmDialougueFrag(String title, String message, PassDialogListener listener){
		
		this.title = title;
		this.message = message;
		this.mListener=listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set an EditText view to get user input 
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);
		
		
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//		View dialogView = inflater.inflate(R.layout.alert_pass_confirmation, null);
//		builder.setView(dialogView);
		
	
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pass = input.getText().toString();
				if(mListener != null) {
                    mListener.onOkClick(pass);
                }
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setIcon(getActivity().getResources().getDrawable(R.drawable.app_icon));
		dialog.setTitle(title);
		return dialog;
	}
	
	
	public interface PassDialogListener {
		
        public void onOkClick(String pass);
    }

}
