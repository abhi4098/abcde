package com.CobaltConnect1.ui.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.CategoryList;
import com.CobaltConnect1.generated.model.CategoryListResponse;
import com.CobaltConnect1.generated.model.Inventory;
import com.CobaltConnect1.generated.model.MarginLocalData;
import com.CobaltConnect1.generated.model.MyCloverProduct;
import com.CobaltConnect1.model.InventoryItems;
import com.CobaltConnect1.ui.activities.LoadingDialog;
import com.CobaltConnect1.ui.adapters.ManageCategoriesAdapter;
import com.CobaltConnect1.ui.adapters.MyProductAdapter;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class ManageCategoriesfragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{



    String orderField,orderType = " ";
    EditText etSearch;
    static ArrayList<MarginLocalData> productTestId = new ArrayList<>();
    private com.CobaltConnect1.ui.adapters.ManageCategoriesAdapter manageCategoriesAdapter;
    ListView listview;
    ArrayList<CategoryList> manageCategoryList = null;
    private RetrofitInterface.MerchantMyProductCategoryClient MyProductCategoryAdapter;
    int totalItems = 0;
    int  totalNoPages = 0;
    ArrayList<CategoryList> showManageCategoriesList = null;
    ArrayList<CategoryList> searchManageCategoriesList = null;
    int pageNum =1;
    Spinner spDropdown,spCategoryDropdown;
    int spSelectedItem = 10;
    String[] items = new String[]{"10", "25", "50", "100"};
    Button updateToClover, btPrev, btNext;
    TextView emptyMessage,tvLastFetch,tvPageNum, tvShowStats,tvProcessing,tvLast;
    LinearLayout llAscId,llDscId,llAscCategory,llDscCategory,llAscMinStock,llDscMinStock,llDscDefaultMargin,llAscDefaultMargin;



    public ManageCategoriesfragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manage_categories, container, false);
        listview = (ListView) rootView.findViewById(R.id.listview);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        emptyMessage = (TextView) rootView.findViewById(R.id.empty);
        spDropdown = (Spinner) rootView.findViewById(R.id.spinner);
        tvPageNum = (TextView) rootView.findViewById(R.id.page_num);
        tvShowStats = (TextView) rootView.findViewById(R.id.show_stats);
        tvProcessing = (TextView) rootView.findViewById(R.id.processing);
        tvLast = (TextView) rootView.findViewById(R.id.last_fetch);
        btNext = (Button) rootView.findViewById(R.id.next);
        btPrev = (Button) rootView.findViewById(R.id.prev);

        llAscId = (LinearLayout) rootView.findViewById(R.id.ll_id_asc);
        llDscId = (LinearLayout) rootView.findViewById(R.id.ll_id_dsc);
        llAscCategory = (LinearLayout) rootView.findViewById(R.id.ll_category_asc);
        llDscCategory = (LinearLayout) rootView.findViewById(R.id.ll_category_dsc);
        llAscDefaultMargin = (LinearLayout) rootView.findViewById(R.id.ll_default_margin_asc);
        llDscDefaultMargin = (LinearLayout) rootView.findViewById(R.id.ll_default_margin_dsc);
        llAscMinStock = (LinearLayout) rootView.findViewById(R.id.ll_min_stock_asc);
        llDscMinStock = (LinearLayout) rootView.findViewById(R.id.ll_min_stock_dsc);


        llAscId.setOnClickListener(this);
        llDscId.setOnClickListener(this);
        llAscCategory.setOnClickListener(this);
        llDscCategory.setOnClickListener(this);
        llAscMinStock.setOnClickListener(this);
        llDscMinStock .setOnClickListener(this);
        llAscDefaultMargin.setOnClickListener(this);
        llDscDefaultMargin.setOnClickListener(this);

        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
        if (pageNum ==1)
        {
            btPrev.setEnabled(false);
            btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, items);
        spDropdown.setAdapter(adapter);
        spDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                etSearch.getText().clear();
                spSelectedItem = Integer.parseInt(spDropdown.getSelectedItem().toString());
                pageNum = 1;
                tvPageNum.setText(String.valueOf(pageNum));

                btPrev.setEnabled(false);
                btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
                btNext.setEnabled(true);
                btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));

                setPageInformation();


                if (manageCategoryList != null)

                {
                    showManageCategoriesList = new ArrayList<>();
                    for (int i = 0; i < spSelectedItem && i<totalItems; i++)

                    {
                        CategoryList categoryList = new CategoryList();
                        categoryList.setCategoryId(manageCategoryList.get(i).getCategoryId());
                        categoryList.setTitle(manageCategoryList.get(i).getTitle());
                        categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
                        categoryList.setMinStock(manageCategoryList.get(i).getMinStock());
                        showManageCategoriesList.add(categoryList);

                    }
                    manageCategoriesAdapter = new ManageCategoriesAdapter(getActivity(), R.layout.manage_product_list_layout, R.id.item_name, showManageCategoriesList,productTestId, manageCategoryList);
                    listview.setAdapter(manageCategoriesAdapter);
                    LoadingDialog.cancelLoading();
                    listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
                    listview.setDividerHeight(1);
                    listview.setTextFilterEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pageNum = 1;
                tvPageNum.setText(String.valueOf(pageNum));
                btNext.setEnabled(true);
                btPrev.setEnabled(false);
                btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));
                btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));


                if (manageCategoryList != null) {

                    filterSearch(s.toString());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                if (manageCategoryList != null) {
                    manageCategoriesAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setUpRestAdapter();
        getCategoryList();


        return rootView;
    }

    private void setPageInformation() {
        if (totalItems != 0)

        {


            if ((totalItems % spSelectedItem) == 0) {
                totalNoPages = totalItems / spSelectedItem;
                tvShowStats.setText("Showing " +pageNum + " to " +spSelectedItem + " of "  +totalItems );
            }

            else if(totalItems<spSelectedItem)
            {
                totalNoPages = 1;
                tvShowStats.setText("Showing " +pageNum + " to " +totalItems + " of "  +totalItems );
            }

            else {

                totalNoPages = ((totalItems / spSelectedItem)+1);
                tvShowStats.setText("Showing " +pageNum + " to " +spSelectedItem + " of "  +totalItems );
            }


        }

    }

    private void filterSearch(String constraint) {

        constraint = constraint.toString().toLowerCase();
        searchManageCategoriesList =new ArrayList<>();
        for (int i = 0; i < manageCategoryList.size(); i++) {
            String data = manageCategoryList.get(i).getTitle();
            if (data.toLowerCase().startsWith(constraint.toString())) {
                CategoryList categoryList = new CategoryList();
                categoryList.setTitle(manageCategoryList.get(i).getTitle());
                categoryList.setCategoryId(manageCategoryList.get(i).getCategoryId());

                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (manageCategoryList.get(i).getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {

                            categoryList.setDefaultMargin(productTestId.get(j).getDefaultMargin());
                            categoryList.setMinStock(productTestId.get(j).getMinStock());
                            break;
                        }
                        categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
                        categoryList.setMinStock(manageCategoryList.get(i).getMinStock());

                    }


                }
                else
                {
                    categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
                    categoryList.setMinStock(manageCategoryList.get(i).getMinStock());
                }


                searchManageCategoriesList.add(categoryList);

            }
        }
        // set the Filtered result to return

        totalItems = searchManageCategoriesList.size();
        setPageInformation();
        showManageCategoriesList = new ArrayList<>();
        for (int i = 0;  i < spSelectedItem&& i<totalItems; i++) {
            CategoryList categoryList = new CategoryList();
            categoryList.setTitle(searchManageCategoriesList.get(i).getTitle());
            categoryList.setCategoryId(searchManageCategoriesList.get(i).getCategoryId());
            categoryList.setMinStock(searchManageCategoriesList.get(i).getMinStock());
            categoryList.setDefaultMargin(searchManageCategoriesList.get(i).getDefaultMargin());
            showManageCategoriesList.add(categoryList);
        }

        manageCategoriesAdapter = new ManageCategoriesAdapter(getActivity(), R.layout.manage_product_list_layout, R.id.item_name, showManageCategoriesList,productTestId, searchManageCategoriesList);
        listview.setAdapter(manageCategoriesAdapter);
        LoadingDialog.cancelLoading();
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
        listview.setDividerHeight(1);
        listview.setTextFilterEnabled(true);

    }


    private void setUpRestAdapter() {

        MyProductCategoryAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMyProductCategoryClient.class, BASE_URL, getActivity());
    }

    private void getCategoryList() {
        Call<CategoryListResponse> call = MyProductCategoryAdapter.merchantMyProductCategory(new MyCloverProduct(PrefUtils.getAuthToken(getContext()), "listCategories",orderField,orderType));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<CategoryListResponse>() {

                @Override
                public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getType()!= 0 ) {
                            setCategoryList(response);
                        }




                    }
                }

                @Override
                public void onFailure(Call<CategoryListResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }



    }


    private void setCategoryList(Response<CategoryListResponse> response) {

        pageNum = 1;
        tvPageNum.setText(String.valueOf(pageNum));
        btPrev.setEnabled(false);
        btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
        btNext.setEnabled(true);
        showManageCategoriesList = new ArrayList<>();
        manageCategoryList = new ArrayList<>();
        for (int i = 0; i < response.body().getList().size(); i++) {

            CategoryList categoryList = new CategoryList();
            categoryList.setCategoryId(response.body().getList().get(i).getCategoryId());
            categoryList.setTitle(response.body().getList().get(i).getTitle());
            categoryList.setDefaultMargin(response.body().getList().get(i).getDefaultMargin());
            categoryList.setMinStock(response.body().getList().get(i).getMinStock());
            manageCategoryList.add(categoryList);

        }

        totalItems = manageCategoryList.size();
        setPageInformation();



        for (int i = 0; i < spSelectedItem&& i<totalItems; i++) {
            CategoryList categoryList = new CategoryList();
            categoryList.setCategoryId(manageCategoryList.get(i).getCategoryId());
            categoryList.setTitle(manageCategoryList.get(i).getTitle());
            categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
            categoryList.setMinStock(manageCategoryList.get(i).getMinStock());
            showManageCategoriesList.add(categoryList);
        }


        manageCategoriesAdapter = new ManageCategoriesAdapter(getActivity(), R.layout.manage_product_list_layout, R.id.item_name, showManageCategoriesList,productTestId, manageCategoryList);
        listview.setAdapter(manageCategoriesAdapter);
        LoadingDialog.cancelLoading();
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
        listview.setDividerHeight(1);
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(this);


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.next:
                btPrev.setEnabled(true);
                btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));

                if (pageNum<totalNoPages) {
                    pageNum++;
                    filterListPages();
                }
                if (pageNum == totalNoPages)
                {

                    btNext.setEnabled(false);
                    btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
                }
                break;

            case R.id.prev:
                btNext.setEnabled(true);
                btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));

                if (pageNum >1) {
                    pageNum--;
                    filterListPages();
                }
                if (pageNum == 1)
                {
                    btPrev.setEnabled(false);
                    btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
                }
                break;

            case R.id.ll_id_asc:
                orderType = "asc";
                orderField = "categoryId";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.INVISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_id_dsc:
                orderType = "desc";
                orderField = "categoryId";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.INVISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_category_asc:
                orderType = "asc";
                orderField = "title";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.INVISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_category_dsc:
                orderType = "desc";
                orderField = "title";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.INVISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_min_stock_asc:
                orderType = "asc";
                orderField = "minStock";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.INVISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_min_stock_dsc:
                orderType = "desc";
                orderField = "minStock";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.INVISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_default_margin_asc:
                orderType = "asc";
                orderField = "defaultMargin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.INVISIBLE);
                llDscDefaultMargin.setVisibility(View.VISIBLE);


                break;

            case R.id.ll_default_margin_dsc:
                orderType = "desc";
                orderField = "defaultMargin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                getCategoryList();
                llAscId.setVisibility(View.VISIBLE);
                llDscId.setVisibility(View.VISIBLE);
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                llAscMinStock.setVisibility(View.VISIBLE);
                llDscMinStock.setVisibility(View.VISIBLE);
                llAscDefaultMargin.setVisibility(View.VISIBLE);
                llDscDefaultMargin.setVisibility(View.INVISIBLE);

                break;


            default:
                break;

        }
    }


    private void filterListPages() {

        if (pageNum == 1) {
            tvShowStats.setText("Showing " + pageNum + " to " + pageNum * spSelectedItem + " of " + totalItems);
        } else if (pageNum * spSelectedItem >= totalItems)

        {
            tvShowStats.setText("Showing " + (pageNum - 1) * spSelectedItem + " to " + totalItems + " of " + totalItems);
        } else {
            tvShowStats.setText("Showing " + (pageNum - 1) * spSelectedItem + " to " + pageNum * spSelectedItem + " of " + totalItems);
        }
        tvPageNum.setText(String.valueOf(pageNum));
        showManageCategoriesList = new ArrayList<>();

        if (etSearch.getText().toString().equals(""))

        {

            for (int i = (pageNum - 1) * spSelectedItem; i < pageNum * spSelectedItem && i < totalItems; i++) {

                CategoryList categoryList = new CategoryList();
                categoryList.setCategoryId(manageCategoryList.get(i).getCategoryId());
                categoryList.setTitle(manageCategoryList.get(i).getTitle());

                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (manageCategoryList.get(i).getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {
                            categoryList.setDefaultMargin(productTestId.get(j).getDefaultMargin());
                            categoryList.setMinStock(productTestId.get(j).getMinStock());

                            break;
                        }
                        categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
                        categoryList.setMinStock(manageCategoryList.get(i).getMinStock());

                    }


                }
                else
                {
                    categoryList.setDefaultMargin(manageCategoryList.get(i).getDefaultMargin());
                    categoryList.setMinStock(manageCategoryList.get(i).getMinStock());
                }

                showManageCategoriesList.add(categoryList);

            }

            manageCategoriesAdapter = new ManageCategoriesAdapter(getActivity(), R.layout.manage_product_list_layout, R.id.item_name, showManageCategoriesList, productTestId, manageCategoryList);
            listview.setAdapter(manageCategoriesAdapter);
            LoadingDialog.cancelLoading();
            listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
            listview.setDividerHeight(1);
            listview.setTextFilterEnabled(true);
            listview.setOnItemClickListener(this);
        }

        else
        {


            for (int i = (pageNum - 1) * spSelectedItem; i < pageNum * spSelectedItem && i < totalItems; i++) {

                CategoryList categoryList = new CategoryList();
                categoryList.setCategoryId(searchManageCategoriesList.get(i).getCategoryId());
                categoryList.setTitle(searchManageCategoriesList.get(i).getTitle());
                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (searchManageCategoriesList.get(i).getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {

                            categoryList.setDefaultMargin(productTestId.get(j).getDefaultMargin());
                            categoryList.setMinStock(productTestId.get(j).getMinStock());

                            break;
                        }
                        categoryList.setDefaultMargin(searchManageCategoriesList.get(i).getDefaultMargin());
                        categoryList.setMinStock(searchManageCategoriesList.get(i).getMinStock());

                    }


                }
                else
                {
                    categoryList.setDefaultMargin(searchManageCategoriesList.get(i).getDefaultMargin());
                    categoryList.setMinStock(searchManageCategoriesList.get(i).getMinStock());
                }
                showManageCategoriesList.add(categoryList);

            }

            manageCategoriesAdapter = new ManageCategoriesAdapter(getActivity(), R.layout.manage_product_list_layout, R.id.item_name, showManageCategoriesList, productTestId, searchManageCategoriesList);
            listview.setAdapter(manageCategoriesAdapter);
            LoadingDialog.cancelLoading();
            listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
            listview.setDividerHeight(1);
            listview.setTextFilterEnabled(true);
            listview.setOnItemClickListener(this);
        }
    }


}
