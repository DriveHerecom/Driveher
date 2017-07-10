package com.yukti.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yukti.driveherenew.R;
import com.yukti.utils.DrawerItem;

import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {
	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;
	Activity activity;

	public CustomDrawerAdapter(Context context, Activity activity, int layoutResourceID, List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
		this.activity = activity;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub

		DrawerItemHolder drawerHolder;
		View view = convertView;

		LayoutInflater inflater = activity.getLayoutInflater();
		drawerHolder = new DrawerItemHolder();

		view = inflater.inflate(layoutResID, parent, false);

		drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_itemName);
		drawerHolder.ItemDescription = (TextView) view.findViewById(R.id.drawer_description);
		drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

		view.setTag(drawerHolder);

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);
		drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
		drawerHolder.ItemName.setText(dItem.getCatName());
		drawerHolder.ItemDescription.setText(dItem.getDescription());

		if (position==0)
		{
			view.setActivated(true);
		}
		else
		{
			view.setActivated(false);
		}


		return view;
	}

	private static class DrawerItemHolder
	{
		TextView ItemName,ItemDescription;
		ImageView icon;
	}
}