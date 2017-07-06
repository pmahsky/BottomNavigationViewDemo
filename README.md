# BottomNavigationViewDemo
BottomNavigationView is a component added in Google Support Library. It provides a quick navigation between top level views within an app. It should be used when application has three to five top-level destinations.

### Implementation
Create the BottomNavigationView in xml of your layout and provide a menu resource to it:
```
<android.support.design.widget.BottomNavigationView
        android:id="@+id/navigation"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginEnd="0dp"
        android:layout_marginStart="0dp"
        android:background="?android:attr/windowBackground"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:menu="@menu/navigation" />
 ```    
 Create a file here navigation.xml in menu resource folder. This file is used for providing the MenuItems in BottomNavigationView
 ```
 <?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navigation_home"
        android:icon="@drawable/ic_home_black_24dp"
        android:title="@string/title_home" />

    <item
        android:id="@+id/navigation_dashboard"
        android:icon="@drawable/ic_dashboard_black_24dp"
        android:title="@string/title_dashboard" />

    <item
        android:id="@+id/navigation_notifications"
        android:icon="@drawable/ic_notifications_black_24dp"
        android:title="@string/title_notifications" />

    <item
        android:id="@+id/navigation_settings"
        android:icon="@drawable/ic_settings_black_24dp"
        android:title="@string/title_settings" />

</menu>
 
 ```
 
With everything in line this much code shows up the BottomBar on running the app.
Now lets set the listener for the Click Events OnNavigationItemSelectedListener and OnNavigationItemReselectedListener on Menu Items -

```
private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;

                case R.id.navigation_dashboard:
                    return true;

                case R.id.navigation_notifications:
                    return true;

                case R.id.navigation_settings:
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

bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

bottomNavigationView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

```
If the no of Menu Items are more than 3 then the selected Item will take more space in the BottomNavView and it looks a little weird as of now, may be intentionally Google has kept it like this: 

[![BottomNavigationViewDemo](https://github.com/pmahsky/BottomNavigationViewDemo/blob/master/Screenshots/Screenshot_3.jpg)]

This behavior is defined by ShiftingMode property of BottomNavigationView, which can't be disabled in a straightforward way as of now, as its api is not public. But there is a way through Reflection to do it :

```
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

        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (SecurityException e) {
            e.printStackTrace();

        }
```
After setting this here is the output :

[![BottomNavigationViewDemo](https://github.com/pmahsky/BottomNavigationViewDemo/blob/master/Screenshots/Screenshot_1.jpg)]

I've also added method to add the MenuItems through code, other than xml.

### References

https://material.io/guidelines/components/bottom-navigation.html#bottom-navigation-usage          
https://developer.android.com/reference/android/support/design/widget/BottomNavigationView.html
https://medium.com/@hitherejoe/exploring-the-android-design-support-library-bottom-navigation-drawer-548de699e8e0
https://stackoverflow.com/a/40973308/2412582
https://github.com/roughike/BottomBar




