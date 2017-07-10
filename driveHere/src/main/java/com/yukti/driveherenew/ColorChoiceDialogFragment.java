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
import android.widget.TextView;
import com.yukti.driveherenew.SingleChoiceTextDialogFragment.ListDialogListener;
import com.yukti.driveherenew.search.CircleView;

public class ColorChoiceDialogFragment extends DialogFragment  implements View.OnClickListener{

    ListDialogListener mListener;
    RecyclerView recyclerView;
    
    @Override
	public void onCreate(Bundle state) {
        super.onCreate(state);
        //setRetainInstance(true);
    }

    public ColorChoiceDialogFragment(ListDialogListener listener)
	{
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Color");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_color_choice, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);
        
        CustomAdapter adapter = new CustomAdapter(recyclerView);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
       
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogNegativeClick(ColorChoiceDialogFragment.this);
                }
            }
        });

        return builder.create();
    }
    
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
	    
	    String[] colorNames, colorValues;
	    LayoutInflater inflater;
	    RecyclerView recyclerView;
	    
	    public CustomAdapter(RecyclerView recyclerView){
	    	this.recyclerView = recyclerView;
	    	colorNames = getResources().getStringArray(R.array.ColorName);
	    	colorValues = getResources().getStringArray(R.array.ColorValue);
	    	inflater = LayoutInflater.from(getActivity()); 
	    }
	    
	    public class ViewHolder extends RecyclerView.ViewHolder {
	    	
	    
	        CircleView circleView;
	        TextView colorName;
	 
	        public ViewHolder(View v) {
	            super(v);  
	            circleView = (CircleView) v.findViewById(R.id.circleView);
	            colorName = (TextView) v.findViewById(R.id.colorName);
	        }
	    }
	 
	    @Override
	    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
	        // Create a new view.
	        View v = inflater.inflate(R.layout.row_color, viewGroup, false);
	        v.setOnClickListener(ColorChoiceDialogFragment.this);
	        return new ViewHolder(v);
	    }
	 
	    @Override
	    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
	    	
	        viewHolder.circleView.drawAgain(colorValues[position]);
	        viewHolder.colorName.setText(colorNames[position]);
	    }
	 
	    @Override
	    public int getItemCount() {
	        return colorNames.length;
	    }
	}

	@Override
	public void onClick(View v) {
		int position = recyclerView.getChildAdapterPosition(v);
		mListener.onItemClick(position);
		dismiss();
	}
}
