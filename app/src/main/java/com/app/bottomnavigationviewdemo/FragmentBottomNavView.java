package com.app.bottomnavigationviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Prashant on 05/07/17.
 */

public class FragmentBottomNavView extends Fragment {

    static final String FRAGMENT_ARGUMENT = "fragment_argument";
    static final String DEFAULT_VALUE = "Default Value";
    @BindView(R.id.fragmentTextView)
    TextView fragmentTextView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_nav_view, container, false);

        unbinder = ButterKnife.bind(this, view);

        String argValue = getArguments().getString(FRAGMENT_ARGUMENT);

        fragmentTextView.setText(argValue == null ? DEFAULT_VALUE : argValue);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
