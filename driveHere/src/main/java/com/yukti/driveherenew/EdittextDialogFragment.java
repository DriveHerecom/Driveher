package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

public class EdittextDialogFragment extends DialogFragment {

	String title, message,hint="",value="",settextdata = "";
	private EdittextDialogListener mListener;
	
	public EdittextDialogFragment(String title, String message, String hINT2, EdittextDialogListener listener, String settextdata){
		
		this.title = title;
		this.message = message;
		this.mListener=listener;
		this.hint= hINT2;
		this.settextdata =settextdata;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set an EditText view to get user input 
		final EditText input = new EditText(getActivity());
		input.setHint(hint);
	
		if(!settextdata.equalsIgnoreCase("")){
			input.setText(settextdata);	
		}
		
		if(message.equalsIgnoreCase("auction")){
			input.setInputType(InputType.TYPE_CLASS_TEXT);
		}else{
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		  
		
		builder.setView(input);
 		
		
//		LayoutInflater inflater = getActivity().getLayoutInflater();
//		View dialogView = inflater.inflate(R.layout.alert_pass_confirmation, null);
//		builder.setView(dialogView);
		
	
		builder.setPositiveButton("OK", new OnClickListener() {
			
			

			@Override
			public void onClick(DialogInterface dialog, int which) {
				value = input.getText().toString();
				if(mListener != null) {
                    mListener.onOkClick(value);
                }
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.setIcon(getActivity().getResources().getDrawable(R.drawable.app_icon));
		dialog.setTitle(title);
		return dialog;
	}
	
	
	public interface EdittextDialogListener {
		
        public void onOkClick(String pass);
    }
}
