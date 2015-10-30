package com.lucien.team.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.lucien.team.app.MyApplication;

/**
 * Created by lucien.li on 2015/10/30.
 */
public class BaseFragment extends Fragment {
    public MyApplication context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (MyApplication) getActivity().getApplicationContext();
    }
}