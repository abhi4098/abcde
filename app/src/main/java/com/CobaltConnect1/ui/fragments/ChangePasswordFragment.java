package com.CobaltConnect1.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.CobaltConnect1.R;
import com.CobaltConnect1.model.InventoryItems;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

/**
 * Created by Abhinandan on 18/8/17.
 */
public class ChangePasswordFragment extends Fragment {


    ArrayList<InventoryItems> apiDataList;
    BarChart revenueBarChart;



    public ChangePasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);



        return rootView;
    }


}
