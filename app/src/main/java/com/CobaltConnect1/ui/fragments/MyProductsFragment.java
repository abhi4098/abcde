package com.CobaltConnect1.ui.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.CobaltConnect1.generated.model.CategoryListResponse;
import com.CobaltConnect1.generated.model.Inventory;
import com.CobaltConnect1.generated.model.MarginLocalData;
import com.CobaltConnect1.generated.model.MarginUpdateAll;
import com.CobaltConnect1.generated.model.MarginUpdateAllResponse;
import com.CobaltConnect1.generated.model.MyCloverProduct;
import com.CobaltConnect1.generated.model.MyCloverProductResponse;
import com.CobaltConnect1.model.InventoryItems;
import com.CobaltConnect1.ui.activities.LoadingDialog;
import com.CobaltConnect1.ui.adapters.MyProductAdapter;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class MyProductsFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private RetrofitInterface.MerchantMyProductClient MyProductAdapter;
    private RetrofitInterface.MerchantMyProductCategoryClient MyProductCategoryAdapter;
    private RetrofitInterface.MerchantFetchFromCloverClient MyCloverFetchAdapter;

    static ArrayList<Inventory> myProductList = null;
    ArrayList<String> categoryList;
    ArrayList<InventoryItems> dataList = new ArrayList<>();
    private RecyclerView newlyUpdatedRecyclerView;
    private com.CobaltConnect1.ui.adapters.MyProductAdapter myProductAdapter;
    EditText etSearch;
    ListView listview;
    TextView emptyMessage,tvLastFetch,tvPageNum, tvShowStats,tvProcessing,tvLast;
    String[] items = new String[]{"10", "25", "50", "100"};
    Button updateToClover, btPrev, btNext;
    String orderField,orderType = " ";
    int pageNum =1;
    Spinner spDropdown,spCategoryDropdown;
    int spSelectedItem = 10;
    int totalItems = 0;
    int  totalNoPages = 0;
    LinearLayout llAscName,llDscName,llAscWholesaler,llDscWholesaler,llAscNewCost,llDscNewCost,llDscPrevCost,llAscPrevCost,llAscPrevPrice,llDscPrevPrice,
            llAscNewPrice,llDscNewPrice,llAscMargin,llDscMargin,llAscStatus,llDscStatus,llAscProductId,llDscProductId,llAscStock,llDscStock,llAscCategory,llDscCategory;
    ArrayList<Inventory> showUpdateProductList = null;

    static ArrayList<MarginLocalData> productTestId = null;


    //String[] items = new String[]{"Category", "category1", "category2","category3"};
    public MyProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_products, container, false);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        listview = (ListView) rootView.findViewById(R.id.listview);
        emptyMessage = (TextView) rootView.findViewById(R.id.empty);
        spCategoryDropdown =(Spinner) rootView.findViewById(R.id.category_spinner);
        spDropdown = (Spinner) rootView.findViewById(R.id.spinner);
        tvLastFetch = (TextView) rootView.findViewById(R.id.last_fetch_time_date);
        tvPageNum = (TextView) rootView.findViewById(R.id.page_num);
        tvShowStats = (TextView) rootView.findViewById(R.id.show_stats);
        tvProcessing = (TextView) rootView.findViewById(R.id.processing);
        tvLast = (TextView) rootView.findViewById(R.id.last_fetch);
        btNext = (Button) rootView.findViewById(R.id.next);
        btPrev = (Button) rootView.findViewById(R.id.prev);

        llAscName = (LinearLayout) rootView.findViewById(R.id.ll_name_asc);
        llDscName = (LinearLayout) rootView.findViewById(R.id.ll_name_dsc);
        llAscWholesaler = (LinearLayout) rootView.findViewById(R.id.ll_wholesaler_asc);
        llDscWholesaler = (LinearLayout) rootView.findViewById(R.id.ll_wholesaler_dsc);
        llAscNewCost = (LinearLayout) rootView.findViewById(R.id.ll_new_cost_asc);
        llDscNewCost = (LinearLayout) rootView.findViewById(R.id.ll_new_cost_dsc);
        llAscPrevCost = (LinearLayout) rootView.findViewById(R.id.ll_prev_cost_asc);
        llDscPrevCost = (LinearLayout) rootView.findViewById(R.id.ll_prev_cost_dsc);
        llAscNewPrice = (LinearLayout) rootView.findViewById(R.id.ll_new_price_asc);
        llDscNewPrice = (LinearLayout) rootView.findViewById(R.id.ll_new_price_dsc);
        llAscMargin = (LinearLayout) rootView.findViewById(R.id.ll_margin_asc);
        llDscMargin = (LinearLayout) rootView.findViewById(R.id.ll_margin_dsc);
        llAscPrevPrice = (LinearLayout) rootView.findViewById(R.id.ll_prev_price_asc);
        llDscPrevPrice = (LinearLayout) rootView.findViewById(R.id.ll_prev_price_dsc);
        llAscStatus = (LinearLayout) rootView.findViewById(R.id.ll_status_asc);
        llDscStatus = (LinearLayout) rootView.findViewById(R.id.ll_status_dsc);
        llAscProductId = (LinearLayout) rootView.findViewById(R.id.ll_product_id_asc);
        llDscProductId = (LinearLayout) rootView.findViewById(R.id.ll_product_id_dsc);
        llAscStock = (LinearLayout) rootView.findViewById(R.id.ll_stock_asc);
        llDscStock= (LinearLayout) rootView.findViewById(R.id.ll_stock_dsc);
        llAscCategory = (LinearLayout) rootView.findViewById(R.id.ll_category_asc);
        llDscCategory = (LinearLayout) rootView.findViewById(R.id.ll_category_dsc);


        llAscName.setOnClickListener(this);
        llDscName.setOnClickListener(this);
        llAscWholesaler.setOnClickListener(this);
        llDscWholesaler.setOnClickListener(this);
        llAscNewCost.setOnClickListener(this);
        llDscNewCost .setOnClickListener(this);
        llAscPrevCost.setOnClickListener(this);
        llDscPrevCost.setOnClickListener(this);
        llAscNewPrice.setOnClickListener(this);
        llDscNewPrice.setOnClickListener(this);
        llAscMargin.setOnClickListener(this);
        llDscMargin.setOnClickListener(this);
        llAscPrevPrice.setOnClickListener(this);
        llDscPrevPrice.setOnClickListener(this);
        llAscStatus.setOnClickListener(this);
        llDscStatus.setOnClickListener(this);
        llAscProductId.setOnClickListener(this);
        llDscProductId.setOnClickListener(this);
        llAscStock.setOnClickListener(this);
        llDscStock.setOnClickListener(this);
        llAscCategory.setOnClickListener(this);
        llDscCategory.setOnClickListener(this);



        updateToClover = (Button) rootView.findViewById(R.id.update_to_clover);
        updateToClover.setOnClickListener(this);
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
                spSelectedItem = Integer.parseInt(spDropdown.getSelectedItem().toString());
                pageNum = 1;
                tvPageNum.setText(String.valueOf(pageNum));

                btPrev.setEnabled(false);
                btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
                btNext.setEnabled(true);
                btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));



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
                        Log.e("abhi", "onItemSelected: ------------total num of pages" + totalNoPages );
                        tvShowStats.setText("Showing " +pageNum + " to " +spSelectedItem + " of "  +totalItems );
                    }


                }


                if (myProductList != null)

                {
                    showUpdateProductList = new ArrayList<>();
                    for (int i = 0; i < spSelectedItem && i<totalItems; i++)

                    {
                        Inventory inventoryItems = new Inventory();
                        inventoryItems.setName(myProductList.get(i).getName());
                        inventoryItems.setNewPrice(myProductList.get(i).getNewPrice());
                        inventoryItems.setPreviousCost(myProductList.get(i).getPreviousCost());
                        inventoryItems.setMargin(myProductList.get(i).getMargin());
                        inventoryItems.setWholeSaler(myProductList.get(i).getWholeSaler());
                        inventoryItems.setNewCost(myProductList.get(i).getNewCost());
                        inventoryItems.setStatus(myProductList.get(i).getStatus());
                        inventoryItems.setBUpdate(myProductList.get(i).getBUpdate());
                        inventoryItems.setPreviousPrice(myProductList.get(i).getPreviousPrice());
                        inventoryItems.setProductId(myProductList.get(i).getProductId());
                        inventoryItems.setCloverId(myProductList.get(i).getCloverId());
                        inventoryItems.setCategory(myProductList.get(i).getCategory());
                        inventoryItems.setStock(myProductList.get(i).getStock());
                        showUpdateProductList.add(inventoryItems);

                    }
                    myProductAdapter = new MyProductAdapter(getActivity(), R.layout.myproduct_list_layout, R.id.item_name, showUpdateProductList, productTestId, myProductList);
                    listview.setAdapter(myProductAdapter);
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

                if (myProductList != null) {
                    myProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                if (myProductList != null) {
                    myProductAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setUpRestAdapter();
        getCategoryList();
        MyProductDetails();
        return rootView;
    }

    private void getCategoryList() {
        Call<CategoryListResponse> call = MyProductCategoryAdapter.merchantMyProductCategory(new MyCloverProduct(PrefUtils.getAuthToken(getContext()), "listCategories",orderField,orderType));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<CategoryListResponse>() {

                @Override
                public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getType()!= 0 )
                            Log.e("abhi", "onResponse: "+response.body().getList().size() );

                        else
                        {
                            LoadingDialog.cancelLoading();

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


    private void setUpRestAdapter() {
        MyProductAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMyProductClient.class, BASE_URL, getActivity());
        MyProductCategoryAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMyProductCategoryClient.class, BASE_URL, getActivity());
        MyCloverFetchAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantFetchFromCloverClient.class, BASE_URL, getActivity());

    }




    private void MyProductDetails() {
        Call<MyCloverProductResponse> call = MyProductAdapter.merchantMyProduct(new MyCloverProduct(PrefUtils.getAuthToken(getContext()), "cloverInventory",orderField,orderType));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<MyCloverProductResponse>() {

                @Override
                public void onResponse(Call<MyCloverProductResponse> call, Response<MyCloverProductResponse> response) {

                    if (response.isSuccessful()) {

                        tvLastFetch.setText(response.body().getLastFetch());
                        if (response.body().getInventory().size() != 0 )
                            setMyProducts(response);
                        else
                        {
                            LoadingDialog.cancelLoading();
                            emptyMessage.setVisibility(View.VISIBLE);
                            listview.setEmptyView(emptyMessage);
                        }




                    }
                }

                @Override
                public void onFailure(Call<MyCloverProductResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

    private void setMyProducts(Response<MyCloverProductResponse> response) {
           pageNum = 1;
           tvPageNum.setText(String.valueOf(pageNum));
           btPrev.setEnabled(false);
           btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
           btNext.setEnabled(true);
          myProductList = new ArrayList<>();
        showUpdateProductList = new ArrayList<>();
        for (int i = 0; i < response.body().getInventory().size(); i++) {
            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(response.body().getInventory().get(i).getName());
            inventoryItems.setNewPrice(response.body().getInventory().get(i).getNewPrice());
            inventoryItems.setPreviousCost(response.body().getInventory().get(i).getPreviousCost());
            inventoryItems.setMargin(response.body().getInventory().get(i).getMargin());
            inventoryItems.setWholeSaler(response.body().getInventory().get(i).getWholeSaler());
            inventoryItems.setNewCost(response.body().getInventory().get(i).getNewCost());
            inventoryItems.setStatus(response.body().getInventory().get(i).getStatus());
            inventoryItems.setBUpdate(response.body().getInventory().get(i).getBUpdate());
            inventoryItems.setPreviousPrice(response.body().getInventory().get(i).getPreviousPrice());
            inventoryItems.setProductId(response.body().getInventory().get(i).getProductId());
            inventoryItems.setCloverId(response.body().getInventory().get(i).getCloverId());
            inventoryItems.setCategory(response.body().getInventory().get(i).getCategory());
            inventoryItems.setStock(response.body().getInventory().get(i).getStock());
            //Log.e(TAG, "setUpdateProduct: --------------------" + response.body().getInventory().get(i).getMargin() + " " + response.body().getInventory().get(i).getName());
            //inventoryItems.setReorder("X");
            myProductList.add(inventoryItems);
        }



        totalItems = myProductList.size();

        if (totalItems != 0)

        {

            if ((totalItems % spSelectedItem) == 0) {
                totalNoPages = totalItems / spSelectedItem;
            }

            else {

                totalNoPages = ((totalItems / spSelectedItem)+1);

            }



        }
        tvShowStats.setText("Showing " +pageNum + " to " +spSelectedItem + " of "  +totalItems );




        for (int i = 0; i < spSelectedItem&& i<totalItems; i++) {
            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(myProductList.get(i).getName());
            inventoryItems.setNewPrice(myProductList.get(i).getNewPrice());
            inventoryItems.setPreviousCost(myProductList.get(i).getPreviousCost());
            inventoryItems.setMargin(myProductList.get(i).getMargin());
            inventoryItems.setWholeSaler(myProductList.get(i).getWholeSaler());
            inventoryItems.setNewCost(myProductList.get(i).getNewCost());
            inventoryItems.setStatus(myProductList.get(i).getStatus());
            inventoryItems.setBUpdate(myProductList.get(i).getBUpdate());
            inventoryItems.setPreviousPrice(myProductList.get(i).getPreviousPrice());
            inventoryItems.setProductId(myProductList.get(i).getProductId());
            inventoryItems.setCloverId(myProductList.get(i).getCloverId());
            inventoryItems.setCategory(myProductList.get(i).getCategory());
            inventoryItems.setStock(myProductList.get(i).getStock());
            showUpdateProductList.add(inventoryItems);
        }

        myProductAdapter = new MyProductAdapter(getActivity(), R.layout.myproduct_list_layout, R.id.item_name, showUpdateProductList,productTestId, myProductList);
            listview.setAdapter(myProductAdapter);
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
            case R.id.update_to_clover:
                UpdateToClover();
                break;
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

            case R.id.ll_name_asc:
                orderType = "asc";
                orderField = "name";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscName.setVisibility(View.INVISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_name_dsc:
                orderType = "desc";
                orderField = "name";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_wholesaler_asc:
                orderType = "asc";
                orderField = "wholeSaler";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscWholesaler.setVisibility(View.INVISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_wholesaler_dsc:
                orderType = "desc";
                orderField = "wholeSaler";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_prev_price_asc:
                orderType = "asc";
                orderField = "previousPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscPrevPrice.setVisibility(View.INVISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_price_dsc:
                orderType = "desc";
                orderField = "previousPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_new_price_asc:
                orderType = "asc";
                orderField = "newPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscNewPrice.setVisibility(View.INVISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_price_dsc:
                orderType = "desc";
                orderField = "newPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_new_cost_asc:
                orderType = "asc";
                orderField = "newCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscNewCost.setVisibility(View.INVISIBLE);
                llDscNewCost.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_cost_dsc:
                orderType = "desc";
                orderField = "newCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_prev_cost_asc:
                orderType = "asc";
                orderField = "previousCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscPrevCost.setVisibility(View.INVISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_cost_dsc:
                orderType = "desc";
                orderField = "previousCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_margin_asc:
                orderType = "asc";
                orderField = "margin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscMargin.setVisibility(View.INVISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_margin_dsc:
                orderType = "desc";
                orderField = "margin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.INVISIBLE);

                break;

            case R.id.ll_status_asc:
                orderType = "asc";
                orderField = "status";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscStatus.setVisibility(View.INVISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_status_dsc:
                orderType = "desc";
                orderField = "status";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_product_id_asc:
                orderType = "asc";
                orderField = "productId";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscProductId.setVisibility(View.INVISIBLE);
                llDscProductId.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_product_id_dsc:
                orderType = "desc";
                orderField = "productId";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscProductId.setVisibility(View.VISIBLE);
                llDscProductId.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_stock_asc:
                orderType = "asc";
                orderField = "stock";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscStock.setVisibility(View.INVISIBLE);
                llDscStock.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_stock_dsc:
                orderType = "desc";
                orderField = "stock";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscStock.setVisibility(View.VISIBLE);
                llDscStock.setVisibility(View.INVISIBLE);
                break;

            case R.id.ll_category_asc:
                orderType = "asc";
                orderField = "category";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscCategory.setVisibility(View.INVISIBLE);
                llDscCategory.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_category_dsc:
                orderType = "desc";
                orderField = "category";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                MyProductDetails();
                llAscCategory.setVisibility(View.VISIBLE);
                llDscCategory.setVisibility(View.INVISIBLE);
                break;

            default:
                break;

        }
    }

    private void filterListPages() {
        etSearch.getText().clear();
        if (pageNum == 1)
        {
            tvShowStats.setText("Showing " + pageNum+ " to " + pageNum * spSelectedItem + " of " + totalItems);
        }
        else if(pageNum*spSelectedItem >= totalItems)

        {
            tvShowStats.setText("Showing " + (pageNum - 1) * spSelectedItem + " to " + totalItems + " of " + totalItems);
        }
        else {
            tvShowStats.setText("Showing " + (pageNum - 1) * spSelectedItem + " to " + pageNum * spSelectedItem + " of " + totalItems);
        }
        tvPageNum.setText(String.valueOf(pageNum));
        showUpdateProductList = new ArrayList<>();




            for (int i = (pageNum-1)*spSelectedItem; i < pageNum*spSelectedItem && i<totalItems; i++)
        {

            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(myProductList.get(i).getName());
            inventoryItems.setNewPrice(myProductList.get(i).getNewPrice());
            inventoryItems.setPreviousCost(myProductList.get(i).getPreviousCost());
            inventoryItems.setMargin(myProductList.get(i).getMargin());
            inventoryItems.setWholeSaler(myProductList.get(i).getWholeSaler());
            inventoryItems.setNewCost(myProductList.get(i).getNewCost());
            inventoryItems.setStatus(myProductList.get(i).getStatus());
            inventoryItems.setBUpdate(myProductList.get(i).getBUpdate());
            inventoryItems.setPreviousPrice(myProductList.get(i).getPreviousPrice());
            inventoryItems.setProductId(myProductList.get(i).getProductId());
            inventoryItems.setCloverId(myProductList.get(i).getCloverId());
            inventoryItems.setCategory(myProductList.get(i).getCategory());
            inventoryItems.setStock(myProductList.get(i).getStock());
            showUpdateProductList.add(inventoryItems);

        }

        myProductAdapter = new MyProductAdapter(getActivity(), R.layout.myproduct_list_layout, R.id.item_name, showUpdateProductList, productTestId, myProductList);
        listview.setAdapter(myProductAdapter);
        LoadingDialog.cancelLoading();
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
        listview.setDividerHeight(1);
        listview.setTextFilterEnabled(true);
    }



    private void UpdateToClover() {
        LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
        Call<MarginUpdateAllResponse> call = MyCloverFetchAdapter.merchantFetchFromClover(new MarginUpdateAll(PrefUtils.getAuthToken(getContext()), "fetchCloverInventory"));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<MarginUpdateAllResponse>() {

                @Override
                public void onResponse(Call<MarginUpdateAllResponse> call, Response<MarginUpdateAllResponse> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getType().equals(1)) {
                            tvLastFetch.setVisibility(View.GONE);
                            tvLast.setVisibility(View.GONE);
                            tvProcessing.setVisibility(View.VISIBLE);
                            tvProcessing.setText(response.body().getMsg());
                            tvProcessing.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_green));
                            LoadingDialog.cancelLoading();
                        }


                    }
                }

                @Override
                public void onFailure(Call<MarginUpdateAllResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

}
