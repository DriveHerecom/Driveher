package com.yukti.driveherenew;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.driveherenew.search.CircleView;

public class ConditionColorDialogFragment extends DialogFragment implements
		View.OnClickListener {

	ListDialogListener mListener;
	RecyclerView recyclerView;
	String conditions = "-1";
	int intCondition = -1;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setRetainInstance(true);
	}

	public ConditionColorDialogFragment(ListDialogListener listener,
			String conditions) {

		mListener = listener;
		this.conditions = conditions;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		intCondition = Integer.parseInt(conditions);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose condition");
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_condition, null);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

		recyclerView.setItemAnimator(new DefaultItemAnimator());

		LinearLayoutManager layoutManager = new LinearLayoutManager(
				getActivity());
		recyclerView.setLayoutManager(layoutManager);

		CustomAdapter adapter = new CustomAdapter(recyclerView);
		recyclerView.setAdapter(adapter);
		builder.setView(view);

		builder.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (mListener != null) {
							mListener
									.onDialogNegativeClick(ConditionColorDialogFragment.this);
						}
					}
				});

		return builder.create();
	}

	public class CustomAdapter extends
			RecyclerView.Adapter<CustomAdapter.ViewHolder> {

		String[] colorValues;
		LayoutInflater inflater;
		RecyclerView recyclerView;

		public CustomAdapter(RecyclerView recyclerView) {
			this.recyclerView = recyclerView;
			// colorNames = getResources().getStringArray(R.array.ColorName);
			colorValues = getResources().getStringArray(
					R.array.ConditionColorValue);
			inflater = LayoutInflater.from(getActivity());
		}

		public class ViewHolder extends RecyclerView.ViewHolder {

			CircleView circleView1, circleView2, circleView3, circleView4,
					circleView5;

			ImageView iv_check;

			// TextView colorName;

			public ViewHolder(View v) {
				super(v);
				circleView1 = (CircleView) v.findViewById(R.id.circleView1);
				circleView2 = (CircleView) v.findViewById(R.id.circleView2);
				circleView3 = (CircleView) v.findViewById(R.id.circleView3);
				circleView4 = (CircleView) v.findViewById(R.id.circleView4);
				circleView5 = (CircleView) v.findViewById(R.id.circleView5);

				iv_check = (ImageView) v.findViewById(R.id.iv_check);
				// colorName = (TextView) v.findViewById(R.carId.colorName);
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			// Create a new view.
			View v = inflater.inflate(R.layout.row_condition, viewGroup, false);
			v.setOnClickListener(ConditionColorDialogFragment.this);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, final int position) {

			if (position == 4) {

				viewHolder.circleView1.drawAgain(colorValues[position]);
				viewHolder.circleView2.drawAgain(colorValues[position]);
				viewHolder.circleView3.drawAgain(colorValues[position]);
				viewHolder.circleView4.drawAgain(colorValues[position]);
				viewHolder.circleView5.drawAgain(colorValues[position]);

			} else if (position == 3) {
				viewHolder.circleView1.drawAgain(colorValues[position]);
				viewHolder.circleView2.drawAgain(colorValues[position]);
				viewHolder.circleView3.drawAgain(colorValues[position]);
				viewHolder.circleView4.drawAgain(colorValues[position]);

				viewHolder.circleView5.setVisibility(View.INVISIBLE);

			} else if (position == 2) {

				viewHolder.circleView1.drawAgain(colorValues[position]);
				viewHolder.circleView2.drawAgain(colorValues[position]);
				viewHolder.circleView3.drawAgain(colorValues[position]);

				viewHolder.circleView4.setVisibility(View.INVISIBLE);
				viewHolder.circleView5.setVisibility(View.INVISIBLE);

			} else if (position == 1) {
				viewHolder.circleView1.drawAgain(colorValues[position]);
				viewHolder.circleView2.drawAgain(colorValues[position]);

				viewHolder.circleView3.setVisibility(View.INVISIBLE);
				viewHolder.circleView4.setVisibility(View.INVISIBLE);
				viewHolder.circleView5.setVisibility(View.INVISIBLE);

			} else if (position == 0) {

				viewHolder.circleView1.drawAgain(colorValues[position]);

				viewHolder.circleView2.setVisibility(View.INVISIBLE);
				viewHolder.circleView3.setVisibility(View.INVISIBLE);
				viewHolder.circleView4.setVisibility(View.INVISIBLE);
				viewHolder.circleView5.setVisibility(View.INVISIBLE);
			}

			if (intCondition == position + 1) {
				viewHolder.iv_check.setVisibility(View.VISIBLE);
			}

			// viewHolder.colorName.setText(colorNames[position]);
		}

		@Override
		public int getItemCount() {
			return 5;
		}
	}

	@Override
	public void onClick(View v) {
		int position = recyclerView.getChildAdapterPosition(v);
		mListener.onItemClick(position);
		dismiss();
	}
}
