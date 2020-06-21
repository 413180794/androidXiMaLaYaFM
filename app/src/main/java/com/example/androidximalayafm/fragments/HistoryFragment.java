package com.example.androidximalayafm.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.androidximalayafm.R;
import com.example.androidximalayafm.base.BaseFragment;

public class HistoryFragment extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View rootView = layoutInflater.inflate(R.layout.fragment_history,container, false);
        return rootView;
    }
}
