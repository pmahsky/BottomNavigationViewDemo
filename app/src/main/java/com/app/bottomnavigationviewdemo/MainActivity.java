package com.app.bottomnavigationviewdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int FRAGMENT_HOME_POSITION = 0;
    private static final int FRAGMENT_DASHBOARD_POSITION = 1;
    private static final int FRAGMENT_NOTIFICATIONS_POSITION = 2;
    private static final int FRAGMENT_SETTINGS_POSITION = 3;

    @BindView(R.id.content)
    FrameLayout content;

    BottomNavigationView bottomNavigationView;
    private ArrayList<FragmentBottomNavView> fragmentsList;
    private String[] fragmentArgList = new String[]{"HOME FRAGMENT", "DASHBOARD FRAGMENT", "NOTIFICATIONS FRAGMENT", "SETTINGS FRAGMENT"};
    private boolean toCreateDynamicBottomNavView;
    private int dynamicHomeItemId;
    private int dynamicDashboardItemId;
    private int dynamicNotificationsItemId;
    private int dynamicSettingsItemId;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toCreateDynamicBottomNavView = true;

        if (toCreateDynamicBottomNavView) {

            bottomNavigationView = findViewById(R.id.navigationDynamic);
            addViewsDynamicallyInBottomNavigationView(bottomNavigationView);
            findViewById(R.id.navigation).setVisibility(View.GONE);

            Snackbar.make(findViewById(R.id.container), "Bottom Navigation View Created Dynamically", Snackbar.LENGTH_SHORT).show();

        } else {

            findViewById(R.id.navigationDynamic).setVisibility(View.GONE);
            bottomNavigationView = findViewById(R.id.navigation);
            Snackbar.make(findViewById(R.id.container), "Bottom Navigation View Created from XML", Snackbar.LENGTH_SHORT).show();

        }

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomNavigationView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

        fragmentsList = new ArrayList<>();

        for (int i = 0; i < fragmentArgList.length; i++) {

            createFragmentList(fragmentArgList[i]);
        }

        selectFragment(0);

        /**
         * If there are more than 3 Views then the selected View takes more space than others by default. This method disables the Shifting Mode through Reflection. There is no public API as of now to do this.
         * To keep the default behavior, just comment this method.
         * @params: Reference of BottomNavigationView
         */
        disableShiftingModeOfBottomNavigationView(bottomNavigationView);

    }

    private void addViewsDynamicallyInBottomNavigationView(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();

        dynamicHomeItemId = menu.add("Home").setIcon(R.drawable.ic_home_black_24dp).getItemId();
        dynamicDashboardItemId = menu.add("Dashboard").setIcon(R.drawable.ic_dashboard_black_24dp).getItemId();
        dynamicNotificationsItemId = menu.add("Notifications").setIcon(R.drawable.ic_notifications_black_24dp).getItemId();
        dynamicSettingsItemId = menu.add("Settings").setIcon(R.drawable.ic_settings_black_24dp).getItemId();

    }

    @SuppressLint("RestrictedApi")
    private void disableShiftingModeOfBottomNavigationView(BottomNavigationView btmNavigationView) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) btmNavigationView.getChildAt(0);

        try {

            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {

                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                //To update view, set the checked value again
                item.setChecked(item.getItemData().isChecked());
            }


        } catch (NoSuchFieldException e) {
            e.printStackTrace();

            Log.e(TAG, "Unable to get shift mode field");


        } catch (IllegalAccessException e) {
            e.printStackTrace();

            Log.e(TAG, "Unable to change value of shift mode");

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void createFragmentList(String fragmentArgument) {

        FragmentBottomNavView fragment = new FragmentBottomNavView();

        Bundle bundle = new Bundle();
        bundle.putString(FragmentBottomNavView.FRAGMENT_ARGUMENT, fragmentArgument);

        fragment.setArguments(bundle);

        fragmentsList.add(fragment);

    }

    private void selectFragment(int i) {

        FragmentBottomNavView fragmentBottomNavView = fragmentsList.get(i);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragmentBottomNavView).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if ((item.getItemId() == R.id.navigation_home) || (item.getItemId() == dynamicHomeItemId)) {
                selectFragment(FRAGMENT_HOME_POSITION);

            } else if ((item.getItemId() == R.id.navigation_dashboard) || (item.getItemId() == dynamicDashboardItemId)) {

                selectFragment(FRAGMENT_DASHBOARD_POSITION);

            } else if ((item.getItemId() == R.id.navigation_notifications) || (item.getItemId() == dynamicNotificationsItemId)) {

                selectFragment(FRAGMENT_NOTIFICATIONS_POSITION);

            } else if ((item.getItemId() == R.id.navigation_settings) || (item.getItemId() == dynamicSettingsItemId)) {

                selectFragment(FRAGMENT_SETTINGS_POSITION);

            }

         /*   switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectFragment(FRAGMENT_HOME_POSITION);
                    return true;

                case R.id.navigation_dashboard:
                    selectFragment(FRAGMENT_DASHBOARD_POSITION);

                    return true;

                case R.id.navigation_notifications:
                    selectFragment(FRAGMENT_NOTIFICATIONS_POSITION);
                    return true;

                case R.id.navigation_settings:
                    selectFragment(FRAGMENT_SETTINGS_POSITION);
                    return true;

            }*/
            return true;
        }

    };

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    Log.d(TAG, "Navigation Reselected ===");
                    break;

                case R.id.navigation_dashboard:
                    Log.d(TAG, "Dashboard Reselected ===");
                    break;

                case R.id.navigation_notifications:
                    Log.d(TAG, "Notification Reselected ===");
                    break;

                case R.id.navigation_settings:
                    Log.d(TAG, "Settings Reselected ===");
                    break;
            }

        }
    };


}
