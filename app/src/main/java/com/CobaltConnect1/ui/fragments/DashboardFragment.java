package com.CobaltConnect1.ui.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.DashboardData;
import com.CobaltConnect1.generated.model.DashboardDataResponse;
import com.CobaltConnect1.generated.model.MyCloverProduct;
import com.CobaltConnect1.generated.model.MyCloverProductResponse;
import com.CobaltConnect1.model.UnderStockItemPieChart;
import com.CobaltConnect1.ui.activities.LoadingDialog;
import com.CobaltConnect1.ui.activities.NavigationalDrawerActivity;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class DashboardFragment extends Fragment implements View.OnClickListener {
    ArrayList<UnderStockItemPieChart> apiDataList;
    private RetrofitInterface.DashboardDataClient MyDashboardAdapter;
    TextView tvMyProducts,tvProductUpdates,tvAffectedProducts,tvMissingMargins;
    PieChart pieChart;
    LinearLayout llMyProducts,llProductUpdates,llAffectedProducts,llMissingProducts;
    TextView tvAppTitle;
    String[] items = new String[]{"Time Interval", "Time Interval1", "Time Interval2","Time Interval3"};

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Spinner dropdown = (Spinner)rootView.findViewById(R.id.spinner);
        tvAppTitle = (TextView) rootView.findViewById(R.id.tv_app_title);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        llMyProducts = (LinearLayout) rootView.findViewById(R.id.ll_my_products);
        llProductUpdates = (LinearLayout) rootView.findViewById(R.id.ll_product_updates);
        llAffectedProducts = (LinearLayout) rootView.findViewById(R.id.ll_affected_products);
        llMissingProducts = (LinearLayout) rootView.findViewById(R.id.ll_missing_margins);
        llMyProducts = (LinearLayout) rootView.findViewById(R.id.ll_my_products);
        llProductUpdates = (LinearLayout) rootView.findViewById(R.id.ll_product_updates);
        tvMyProducts = (TextView) rootView.findViewById(R.id.my_products);
        tvProductUpdates = (TextView) rootView.findViewById(R.id.product_updates);
        tvAffectedProducts = (TextView) rootView.findViewById(R.id.effected_products);
        tvMissingMargins = (TextView) rootView.findViewById(R.id.product_missing_margins);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, items);
        dropdown.setAdapter(adapter);
        //pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        //pieChart.setUsePercentValues(true);
        llMyProducts.setOnClickListener(this);
        llProductUpdates.setOnClickListener(this);
        llAffectedProducts.setOnClickListener(this);
        llMissingProducts.setOnClickListener(this);
        setUpRestAdapter();
        MyDashboardDetails();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
      /*  ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
        yvalues.add(new Entry(25f, 3));
        yvalues.add(new Entry(23f, 4));
        yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues, "");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        pieChart.setDrawSliceText(false);
        pieChart.setDescription(null);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.DKGRAY);*/


        return rootView;
    }

    private void setUpRestAdapter() {
        MyDashboardAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.DashboardDataClient.class, BASE_URL, getActivity());


    }

    private void MyDashboardDetails() {
        LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
        Call<DashboardDataResponse> call = MyDashboardAdapter.merchantDashboardData(new DashboardData("dasboardData",PrefUtils.getAuthToken(getContext())));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<DashboardDataResponse>() {

                @Override
                public void onResponse(Call<DashboardDataResponse> call, Response<DashboardDataResponse> response) {

                    if (response.isSuccessful()) {
                        
                        if (response.body().getType() ==1 )
                        {
                            tvMyProducts.setText(response.body().getMyProducts());
                            tvProductUpdates.setText(response.body().getProductsUpdate());
                            tvAffectedProducts.setText(response.body().getAffectedProducts());
                            tvMissingMargins.setText(response.body().getMissingMargins());
                            LoadingDialog.cancelLoading();
                        }
                        else
                        {
                            LoadingDialog.cancelLoading();
                            
                        }



                    }
                }

                @Override
                public void onFailure(Call<DashboardDataResponse> call, Throwable t) {
                    LoadingDialog.cancelLoading();

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_my_products:
                 Fragment fragment = new MyProductsFragment();
                ((NavigationalDrawerActivity) getActivity()).tvAppTitle.setText("My Products");
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment).commit();

                break;

            case R.id.ll_product_updates:
                Fragment fragment1 = new NewlyUpdatedFragment();
                ((NavigationalDrawerActivity) getActivity()).tvAppTitle.setText("Products Updates");
                FragmentManager fragmentManager1 = getFragmentManager();
                fragmentManager1.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment1).commit();

                break;

            case R.id.ll_affected_products:
                Fragment fragment2 = new MyProductsFragment();
                ((NavigationalDrawerActivity) getActivity()).tvAppTitle.setText("My Products");
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment2).commit();

                break;

            case R.id.ll_missing_margins:
                Fragment fragment3 = new MyProductsFragment();
                ((NavigationalDrawerActivity) getActivity()).tvAppTitle.setText("My Products");
                FragmentManager fragmentManager3 = getFragmentManager();
                fragmentManager3.beginTransaction().addToBackStack(null).replace(R.id.fragment_container, fragment3).commit();

                break;

            default:
                break;
        }

    }


}
