package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialogFragment extends DialogFragment {

	public MessageDialogFragment() {

	}

	public interface MessageDialogListener {
		public void onDialogPositiveClick(MessageDialogFragment dialog);

		public void onDialogNegativeClick(MessageDialogFragment dialog);

		public void onDialogNeutralClick(MessageDialogFragment dialog);
	}

	String mTitle, mMessage, positiveTitle, negativeTitle, neutralTitle, data;
	boolean enablePositive, enableNegetive, enableNeutral;
	private MessageDialogListener mListener;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setRetainInstance(true);
	}

	public MessageDialogFragment(String title, String message,
			boolean enablePositive, String positiveTitle,
			boolean enableNegative, String negativeTitle,
			boolean enableNeutral, String neutralTitle,
			MessageDialogListener listener) {

		mTitle = title;
		mMessage = message;
		mListener = listener;

		this.enablePositive = enablePositive;
		this.positiveTitle = positiveTitle;

		this.enableNegetive = enableNegative;
		this.negativeTitle = negativeTitle;

		this.enableNeutral = enableNeutral;
		this.neutralTitle = neutralTitle;
	}

	public static MessageDialogFragment newInstance(String title, String message,boolean enablePositive, String positiveTitle,
													boolean enableNegative, String negativeTitle,boolean enableNeutral, String neutralTitle,
													MessageDialogListener listener)
	{
		MessageDialogFragment f = new MessageDialogFragment();
		f.mTitle = title;
		f.mMessage = message;
		f.enablePositive = enablePositive;
		f.positiveTitle = positiveTitle;
		f.enableNegetive = enableNegative;
		f.negativeTitle = negativeTitle;
		f.enableNeutral = enableNeutral;
		f.neutralTitle = neutralTitle;
		f.mListener = listener;

		return f;
	}



	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		if (mMessage != null) {
			builder.setMessage(mMessage);
		}
		if (mTitle != null) {
			builder.setTitle(mTitle);
		}

		if (enablePositive) {

			builder.setPositiveButton(positiveTitle,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if (mListener != null) {
								mListener
										.onDialogPositiveClick(MessageDialogFragment.this);
							}
						}
					});
		}
		if (enableNegetive) {
			builder.setNegativeButton(negativeTitle, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mListener != null) {
						mListener
								.onDialogNegativeClick(MessageDialogFragment.this);
					}
				}
			});
		}

		if (enableNeutral) {
			builder.setNeutralButton(neutralTitle, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (mListener != null) {
						mListener
								.onDialogNeutralClick(MessageDialogFragment.this);
					}
				}
			});
		}

		return builder.create();
	}
}
