package com.yukti.driveherenew.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.yukti.adapter.CustomDrawerAdapter;
import com.yukti.driveherenew.R;
import com.yukti.facerecognization.localdatabase.SettingStore;
import com.yukti.utils.AppSingleTon;
import com.yukti.utils.CommonUtils;
import com.yukti.utils.DrawerItem;

import org.w3c.dom.Text;

import java.util.List;

public class NavigationDrawerFragment extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private CustomDrawerAdapter drawerAdapter;
    List<DrawerItem> drawerItems;

    NavigationDrawerCallbacks mCallbacks;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = readFromPreferance(getActivity().getBaseContext());

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        LinearLayout ll_profile = (LinearLayout) view.findViewById(R.id.ll_profile);
        mDrawerListView = (ListView) view.findViewById(R.id.list_drawer);
        TextView txt_Email = (TextView) view.findViewById(R.id.tv_email);
        TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);

        txt_Email.setText(CommonUtils.getCapitalize(AppSingleTon.SHARED_PREFERENCE.getUserEmail()));
        tvUserName.setText(CommonUtils.getCapitalize(AppSingleTon.SHARED_PREFERENCE.getUserName()));
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar, List<DrawerItem> drawerItems) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        this.drawerItems = drawerItems;
        drawerAdapter = new CustomDrawerAdapter(getActivity().getBaseContext(), getActivity(), R.layout.custom_drawer_item, drawerItems);
        mDrawerListView.setAdapter(drawerAdapter);

        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mCallbacks.onNavigationDrawerItemSelected(0, true, false);
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferance(getActivity().getBaseContext(), mUserLearnedDrawer);
                }

                mDrawerLayout.setDrawerListener(mDrawerToggle);
                getActivity().supportInvalidateOptionsMenu();
                mCallbacks.onNavigationDrawerItemSelected(0, true, true);
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mCallbacks.onNavigationDrawerItemSelected(-1, false, false);
        mDrawerListView.setItemChecked(0, true);
    }

    public static void saveToPreferance(Context context, boolean preferanceValue) {
        SettingStore ss = new SettingStore(context);
        ss.setPreDrawerLearned(preferanceValue);
    }

    public static boolean readFromPreferance(Context context) {
        SettingStore ss = new SettingStore(context);
        return ss.getPreDrawerLearned();
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerListView.getChildAt(0).findViewById(R.id.vw_view).setVisibility(View.GONE);
            if (drawerItems.get(position).getTitle() == null) {
                SelectItem(position);
            }
        }
    }

    public void SelectItem(int position) {
        mDrawerListView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mFragmentContainerView);
        mCallbacks.onNavigationDrawerItemSelected(position, false, false);
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position, boolean isDrawerAction, boolean isopen);
    }

}
