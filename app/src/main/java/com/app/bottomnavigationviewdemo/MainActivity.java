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

    private ArrayList<FragmentBottomNavView> fragmentsList;
    private String[] fragmentArgList = new String[]{"HOME FRAGMENT", "DASHBOARD FRAGMENT", "NOTIFICATIONS FRAGMENT", "SETTINGS FRAGMENT"};
    private boolean toCreateDynamicBottomNavView;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        TAG = getClass().getSimpleName();

        toCreateDynamicBottomNavView = true;

        BottomNavigationView bottomNavigationView;

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

    private void addViewsDynamicallyInBottomNavigationView(BottomNavigationView bottomNavigationViewDynamic) {

        Menu menu = bottomNavigationViewDynamic.getMenu();

        menu.add(Menu.NONE, FRAGMENT_HOME_POSITION, Menu.NONE, "Home").setIcon(R.drawable.ic_home_black_24dp).getItemId();
        menu.add(Menu.NONE, FRAGMENT_DASHBOARD_POSITION, Menu.NONE, "Dashboard").setIcon(R.drawable.ic_dashboard_black_24dp).getItemId();
        menu.add(Menu.NONE, FRAGMENT_NOTIFICATIONS_POSITION, Menu.NONE, "Notifications").setIcon(R.drawable.ic_notifications_black_24dp).getItemId();
        menu.add(Menu.NONE, FRAGMENT_SETTINGS_POSITION, Menu.NONE, "Settings").setIcon(R.drawable.ic_settings_black_24dp).getItemId();

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

            switch (item.getItemId()) {
                case R.id.navigation_home:
                case FRAGMENT_HOME_POSITION:
                    selectFragment(FRAGMENT_HOME_POSITION);
                    return true;

                case R.id.navigation_dashboard:
                case FRAGMENT_DASHBOARD_POSITION:
                    selectFragment(FRAGMENT_DASHBOARD_POSITION);

                    return true;

                case R.id.navigation_notifications:
                case FRAGMENT_NOTIFICATIONS_POSITION:
                    selectFragment(FRAGMENT_NOTIFICATIONS_POSITION);
                    return true;

                case R.id.navigation_settings:
                case FRAGMENT_SETTINGS_POSITION:
                    selectFragment(FRAGMENT_SETTINGS_POSITION);
                    return true;

            }
            return true;
        }

    };

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_home:
                case FRAGMENT_HOME_POSITION:
                    Log.d(TAG, "Navigation Reselected ===");
                    break;

                case R.id.navigation_dashboard:
                case FRAGMENT_DASHBOARD_POSITION:
                    Log.d(TAG, "Dashboard Reselected ===");
                    break;

                case R.id.navigation_notifications:
                case FRAGMENT_NOTIFICATIONS_POSITION:
                    Log.d(TAG, "Notification Reselected ===");
                    break;

                case R.id.navigation_settings:
                case FRAGMENT_SETTINGS_POSITION:
                    Log.d(TAG, "Settings Reselected ===");
                    break;
            }

        }
    };


}
