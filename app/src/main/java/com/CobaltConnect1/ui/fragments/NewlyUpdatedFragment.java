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
import com.CobaltConnect1.generated.model.Inventory;
import com.CobaltConnect1.generated.model.MarginLocalData;
import com.CobaltConnect1.generated.model.MarginUpdateAll;
import com.CobaltConnect1.generated.model.MarginUpdateAllResponse;
import com.CobaltConnect1.generated.model.ProductUpdate;
import com.CobaltConnect1.generated.model.ProductUpdateResponse;
import com.CobaltConnect1.ui.activities.LoadingDialog;
import com.CobaltConnect1.ui.adapters.MyProductAdapter;
import com.CobaltConnect1.ui.adapters.NewlyUpdatedAdapter;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class NewlyUpdatedFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private RetrofitInterface.MerchantUpdateProductClient UpdateProductAdapter;
    private RetrofitInterface.MerchantUpdateToCloverClient MyCloverUpdateAdapter;
    String TAG = "CobaltConnect";
    ArrayList<Inventory> updateProductList = null;
    ArrayList<Inventory> showUpdateProductList = null;
    ArrayList<Inventory> searchUpdateProductList = null;

    private NewlyUpdatedAdapter newlyUpdatedAdapter;
    static ArrayList<MarginLocalData> productTestId = new ArrayList<>();
    String orderField,orderType = "";
    EditText etSearch;
    ListView listview;
    LinearLayout llAscName,llDscName,llAscWholesaler,llDscWholesaler,llAscNewCost,llDscNewCost,llDscPrevCost,llAscPrevCost,llAscPrevPrice,llDscPrevPrice,
                 llAscNewPrice,llDscNewPrice,llAscMargin,llDscMargin,llAscStatus,llDscStatus;
    Button updateToClover, btPrev, btNext;
    int pageNum =1;
    Spinner spDropdown;
    int spSelectedItem = 10;
    int totalItems = 0;
    int  totalNoPages = 0;
    String updateMarginAll = " ";
    TextView emptyMessage, tvNew, tvQueued, tvProcessed, tvOthers, tvPageNum, tvShowStats;
    String[] items = new String[]{"10", "25", "50", "100"};

    public NewlyUpdatedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_newly_updated, container, false);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        spDropdown = (Spinner) rootView.findViewById(R.id.spinner);
        listview = (ListView) rootView.findViewById(R.id.listview);
        emptyMessage = (TextView) rootView.findViewById(R.id.empty);
        tvNew = (TextView) rootView.findViewById(R.id.new_data);
        tvQueued = (TextView) rootView.findViewById(R.id.queued);
        tvProcessed = (TextView) rootView.findViewById(R.id.processed);
        tvOthers = (TextView) rootView.findViewById(R.id.others);
        tvPageNum = (TextView) rootView.findViewById(R.id.page_num);
        tvShowStats = (TextView) rootView.findViewById(R.id.show_stats);
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
                etSearch.getText().clear();
                spSelectedItem = Integer.parseInt(spDropdown.getSelectedItem().toString());
                pageNum = 1;
                tvPageNum.setText(String.valueOf(pageNum));

                btPrev.setEnabled(false);
                btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));

                btNext.setEnabled(true);
                btNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_state_selector));

                setPageInformation();



                if (updateProductList != null)

                {
                    showUpdateProductList = new ArrayList<>();
                    for (int i = 0; i < spSelectedItem && i<totalItems; i++)

                    {
                        Inventory inventoryItems = new Inventory();
                        inventoryItems.setName(updateProductList.get(i).getName());
                        inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                        inventoryItems.setPreviousCost(updateProductList.get(i).getPreviousCost());
                        inventoryItems.setMargins(updateProductList.get(i).getMargins());
                        inventoryItems.setWholeSaler(updateProductList.get(i).getWholeSaler());
                        inventoryItems.setNewCost(updateProductList.get(i).getNewCost());
                        inventoryItems.setStatus(updateProductList.get(i).getStatus());
                        inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
                        inventoryItems.setPreviousPrice(updateProductList.get(i).getPreviousPrice());
                        inventoryItems.setProductId(updateProductList.get(i).getProductId());
                        showUpdateProductList.add(inventoryItems);

                    }
                    newlyUpdatedAdapter = new NewlyUpdatedAdapter(getActivity(), R.layout.items_rowlayout, R.id.item_name, showUpdateProductList, productTestId, updateProductList);
                    listview.setAdapter(newlyUpdatedAdapter);
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

                if (updateProductList !=null) {
                    filterSearch(s.toString());
                }
               /* if (updateProductList !=null) {
                    newlyUpdatedAdapter.getFilter().filter(s.toString());
                }*/


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (updateProductList !=null) {
                    newlyUpdatedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setUpRestAdapter();
        updateProductDetails();
        return rootView;
    }

    private void filterSearch(String constraint) {

        constraint = constraint.toString().toLowerCase();
        searchUpdateProductList =new ArrayList<>();

        for (int i = 0; i < updateProductList.size(); i++) {
            String data = updateProductList.get(i).getName();
            if (data.toLowerCase().startsWith(constraint.toString())) {
                Inventory inventoryItems = new Inventory();
                inventoryItems.setName(updateProductList.get(i).getName());
                //inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                inventoryItems.setPreviousCost(updateProductList.get(i).getPreviousCost());
                //inventoryItems.setMargins(updateProductList.get(i).getMargins());
                inventoryItems.setWholeSaler(updateProductList.get(i).getWholeSaler());
                inventoryItems.setNewCost(updateProductList.get(i).getNewCost());
                inventoryItems.setStatus(updateProductList.get(i).getStatus());
                //inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
                inventoryItems.setPreviousPrice(updateProductList.get(i).getPreviousPrice());
                inventoryItems.setProductId(updateProductList.get(i).getProductId());
                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (updateProductList.get(i).getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                            inventoryItems.setNewPrice(productTestId.get(j).getNewPrice());
                            inventoryItems.setMargins(productTestId.get(j).getMargin());
                            inventoryItems.setBUpdate(productTestId.get(j).getBUpdate());
                            break;
                        }
                        inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                        inventoryItems.setMargins(updateProductList.get(i).getMargins());
                        inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());

                    }


                }
                else
                {
                    inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                    inventoryItems.setMargins(updateProductList.get(i).getMargins());
                    inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());

                }
                searchUpdateProductList.add(inventoryItems);
            }
        }

        totalItems = searchUpdateProductList.size();
        setPageInformation();
        showUpdateProductList = new ArrayList<>();

        for (int i = 0;  i < spSelectedItem&& i<totalItems; i++) {
            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(searchUpdateProductList.get(i).getName());
            inventoryItems.setNewPrice(searchUpdateProductList.get(i).getNewPrice());
            inventoryItems.setPreviousCost(searchUpdateProductList.get(i).getPreviousCost());
            inventoryItems.setMargins(searchUpdateProductList.get(i).getMargins());
            inventoryItems.setWholeSaler(searchUpdateProductList.get(i).getWholeSaler());
            inventoryItems.setNewCost(searchUpdateProductList.get(i).getNewCost());
            inventoryItems.setStatus(searchUpdateProductList.get(i).getStatus());
            inventoryItems.setBUpdate(searchUpdateProductList.get(i).getBUpdate());
            inventoryItems.setPreviousPrice(searchUpdateProductList.get(i).getPreviousPrice());
            inventoryItems.setProductId(searchUpdateProductList.get(i).getProductId());
            showUpdateProductList.add(inventoryItems);
        }
        newlyUpdatedAdapter = new NewlyUpdatedAdapter(getActivity(), R.layout.items_rowlayout, R.id.item_name, showUpdateProductList, productTestId, searchUpdateProductList);
        listview.setAdapter(newlyUpdatedAdapter);
        LoadingDialog.cancelLoading();
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
        listview.setDividerHeight(1);
        listview.setTextFilterEnabled(true);
        listview.setOnItemClickListener(this);

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


    private void setUpRestAdapter() {
        UpdateProductAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantUpdateProductClient.class, BASE_URL, getActivity());
        MyCloverUpdateAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantUpdateToCloverClient.class, BASE_URL, getActivity());
    }

    private void UpdateToClover() {
        Call<MarginUpdateAllResponse> call = MyCloverUpdateAdapter.merchantUpdateToClover(new MarginUpdateAll(PrefUtils.getAuthToken(getContext()), "marginUpdateAll"));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<MarginUpdateAllResponse>() {

                @Override
                public void onResponse(Call<MarginUpdateAllResponse> call, Response<MarginUpdateAllResponse> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getType().equals(1)) {
                            updateMarginAll = "updateMarginAll";
                            updateProductDetails();
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


    private void updateProductDetails() {

        Call<ProductUpdateResponse> call = UpdateProductAdapter.merchantUpdateProduct(new ProductUpdate(PrefUtils.getAuthToken(getContext()), "productUpdate",orderField,orderType));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<ProductUpdateResponse>() {

                @Override
                public void onResponse(Call<ProductUpdateResponse> call, Response<ProductUpdateResponse> response) {

                    if (response.isSuccessful()) {

                        tvNew.setText(String.format("New : %s", response.body().getNew().toString()));
                        tvQueued.setText(String.format("Queued : %s", response.body().getQueued().toString()));
                        tvProcessed.setText(String.format("Processed : %s", response.body().getProcessed().toString()));
                        tvOthers.setText(String.format("Others : %s", response.body().getOthers().toString()));

                        if (response.body().getInventory().size() != 0)
                            setUpdateProduct(response);
                        else {
                            LoadingDialog.cancelLoading();
                            emptyMessage.setVisibility(View.VISIBLE);
                            listview.setEmptyView(emptyMessage);

                        }


                    }
                }

                @Override
                public void onFailure(Call<ProductUpdateResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

    private void setUpdateProduct(Response<ProductUpdateResponse> response) {
        pageNum = 1;
        tvPageNum.setText(String.valueOf(pageNum));
        btPrev.setEnabled(false);
        btPrev.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rectangular_background_light_gray));
        btNext.setEnabled(true);
        updateProductList = new ArrayList<>();
        showUpdateProductList = new ArrayList<>();
        for (int i = 0; i < response.body().getInventory().size(); i++) {
            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(response.body().getInventory().get(i).getName());
            inventoryItems.setNewPrice(response.body().getInventory().get(i).getNewPrice());
            inventoryItems.setPreviousCost(response.body().getInventory().get(i).getPreviousCost());
            inventoryItems.setMargins(response.body().getInventory().get(i).getMargins());
            inventoryItems.setWholeSaler(response.body().getInventory().get(i).getWholeSaler());
            inventoryItems.setNewCost(response.body().getInventory().get(i).getNewCost());

            if (updateMarginAll.equals("updateMarginAll") && !response.body().getInventory().get(i).getMargins().equals("0.00")) {
                inventoryItems.setStatus("Queued");
            } else {
                inventoryItems.setStatus(response.body().getInventory().get(i).getStatus());
            }

            inventoryItems.setBUpdate(response.body().getInventory().get(i).getBUpdate());
            inventoryItems.setPreviousPrice(response.body().getInventory().get(i).getPreviousPrice());
            inventoryItems.setProductId(response.body().getInventory().get(i).getProductId());

            updateProductList.add(inventoryItems);
        }

        totalItems = updateProductList.size();
        if (totalItems != 0)

        {
            if ((totalItems % spSelectedItem) == 0) {
                // number is even
                totalNoPages = totalItems / spSelectedItem;

            }

            else {

                totalNoPages = ((totalItems / spSelectedItem)+1);

            }

        }
        tvShowStats.setText("Showing " +pageNum + " to " +spSelectedItem + " of "  +totalItems );




        for (int i = 0; i < spSelectedItem && i<totalItems; i++) {
            Inventory inventoryItems = new Inventory();
            inventoryItems.setName(updateProductList.get(i).getName());
            inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
            inventoryItems.setPreviousCost(updateProductList.get(i).getPreviousCost());
            inventoryItems.setMargins(updateProductList.get(i).getMargins());
            inventoryItems.setWholeSaler(updateProductList.get(i).getWholeSaler());
            inventoryItems.setNewCost(updateProductList.get(i).getNewCost());
            inventoryItems.setStatus(updateProductList.get(i).getStatus());
            inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
            inventoryItems.setPreviousPrice(updateProductList.get(i).getPreviousPrice());
            inventoryItems.setProductId(updateProductList.get(i).getProductId());
            showUpdateProductList.add(inventoryItems);
        }

        newlyUpdatedAdapter = new NewlyUpdatedAdapter(getActivity(), R.layout.items_rowlayout, R.id.item_name, showUpdateProductList, productTestId, updateProductList);
        listview.setAdapter(newlyUpdatedAdapter);
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
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
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
                updateProductDetails();

                llAscName.setVisibility(View.INVISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_name_dsc:
                orderType = "desc";
                orderField = "name";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.INVISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_wholesaler_asc:
                orderType = "asc";
                orderField = "wholeSaler";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.INVISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_wholesaler_dsc:
                orderType = "desc";
                orderField = "wholeSaler";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.INVISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_price_asc:
                orderType = "asc";
                orderField = "previousPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.INVISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_price_dsc:
                orderType = "desc";
                orderField = "previousPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.INVISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_price_asc:
                orderType = "asc";
                orderField = "newPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.INVISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_price_dsc:
                orderType = "desc";
                orderField = "newPrice";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.INVISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_cost_asc:
                orderType = "asc";
                orderField = "newCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.INVISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_new_cost_dsc:
                orderType = "desc";
                orderField = "newCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.INVISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_cost_asc:
                orderType = "asc";
                orderField = "previousCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.INVISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_prev_cost_dsc:
                orderType = "desc";
                orderField = "previousCost";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.INVISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_margin_asc:
                orderType = "asc";
                orderField = "margin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.INVISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_margin_dsc:
                orderType = "desc";
                orderField = "margin";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.INVISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_status_asc:
                orderType = "asc";
                orderField = "status";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.INVISIBLE);
                llDscStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_status_dsc:
                orderType = "desc";
                orderField = "status";
                LoadingDialog.showLoadingDialog(getActivity(), "Loading...");
                updateProductDetails();
                llAscName.setVisibility(View.VISIBLE);
                llDscName.setVisibility(View.VISIBLE);
                llAscWholesaler.setVisibility(View.VISIBLE);
                llDscWholesaler.setVisibility(View.VISIBLE);
                llAscNewCost.setVisibility(View.VISIBLE);
                llDscNewCost .setVisibility(View.VISIBLE);
                llAscPrevCost.setVisibility(View.VISIBLE);
                llDscPrevCost.setVisibility(View.VISIBLE);
                llAscNewPrice.setVisibility(View.VISIBLE);
                llDscNewPrice.setVisibility(View.VISIBLE);
                llAscMargin.setVisibility(View.VISIBLE);
                llDscMargin.setVisibility(View.VISIBLE);
                llAscPrevPrice.setVisibility(View.VISIBLE);
                llDscPrevPrice.setVisibility(View.VISIBLE);
                llAscStatus.setVisibility(View.VISIBLE);
                llDscStatus.setVisibility(View.INVISIBLE);
                break;


            default:
                break;

        }
    }

    private void filterListPages() {

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

        if (etSearch.getText().toString().equals(""))

        {

            for (int i = (pageNum - 1) * spSelectedItem; i < pageNum * spSelectedItem && i < totalItems; i++) {

                Inventory inventoryItems = new Inventory();
                inventoryItems.setName(updateProductList.get(i).getName());
                //inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                inventoryItems.setPreviousCost(updateProductList.get(i).getPreviousCost());
                //inventoryItems.setMargins(updateProductList.get(i).getMargins());
                inventoryItems.setWholeSaler(updateProductList.get(i).getWholeSaler());
                inventoryItems.setNewCost(updateProductList.get(i).getNewCost());
                //inventoryItems.setStatus(updateProductList.get(i).getStatus());
                //inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
                inventoryItems.setPreviousPrice(updateProductList.get(i).getPreviousPrice());
                inventoryItems.setProductId(updateProductList.get(i).getProductId());
                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (updateProductList.get(i).getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                            inventoryItems.setNewPrice(productTestId.get(j).getNewPrice());
                            inventoryItems.setMargins(productTestId.get(j).getMargin());
                            inventoryItems.setBUpdate(productTestId.get(j).getBUpdate());
                            inventoryItems.setStatus(productTestId.get(j).getStatus());
                            break;
                        }
                        inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                        inventoryItems.setMargins(updateProductList.get(i).getMargins());
                        inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
                        inventoryItems.setStatus(updateProductList.get(i).getStatus());

                    }


                }
                else
                {
                    inventoryItems.setNewPrice(updateProductList.get(i).getNewPrice());
                    inventoryItems.setMargins(updateProductList.get(i).getMargins());
                    inventoryItems.setBUpdate(updateProductList.get(i).getBUpdate());
                    inventoryItems.setStatus(updateProductList.get(i).getStatus());

                }
                showUpdateProductList.add(inventoryItems);

            }

            newlyUpdatedAdapter = new NewlyUpdatedAdapter(getActivity(), R.layout.items_rowlayout, R.id.item_name, showUpdateProductList, productTestId, updateProductList);
            listview.setAdapter(newlyUpdatedAdapter);
            LoadingDialog.cancelLoading();
            listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
            listview.setDividerHeight(1);
            listview.setTextFilterEnabled(true);
        }
        else
        {
            for (int i = (pageNum - 1) * spSelectedItem; i < pageNum * spSelectedItem && i < totalItems; i++) {

                Inventory inventoryItems = new Inventory();
                inventoryItems.setName(searchUpdateProductList.get(i).getName());
                //inventoryItems.setNewPrice(searchUpdateProductList.get(i).getNewPrice());
                inventoryItems.setPreviousCost(searchUpdateProductList.get(i).getPreviousCost());
               // inventoryItems.setMargins(searchUpdateProductList.get(i).getMargins());
                inventoryItems.setWholeSaler(searchUpdateProductList.get(i).getWholeSaler());
                inventoryItems.setNewCost(searchUpdateProductList.get(i).getNewCost());
                //inventoryItems.setStatus(searchUpdateProductList.get(i).getStatus());
                //inventoryItems.setBUpdate(searchUpdateProductList.get(i).getBUpdate());
                inventoryItems.setPreviousPrice(searchUpdateProductList.get(i).getPreviousPrice());
                inventoryItems.setProductId(searchUpdateProductList.get(i).getProductId());
                if (productTestId.size() !=0) {
                    for (int j = 0; j < productTestId.size(); j++) {
                        if (searchUpdateProductList.get(i).getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                            inventoryItems.setNewPrice(productTestId.get(j).getNewPrice());
                            inventoryItems.setMargins(productTestId.get(j).getMargin());
                            inventoryItems.setBUpdate(productTestId.get(j).getBUpdate());
                            inventoryItems.setStatus(productTestId.get(j).getStatus());
                            break;
                        }
                        inventoryItems.setNewPrice(searchUpdateProductList.get(i).getNewPrice());
                        inventoryItems.setMargins(searchUpdateProductList.get(i).getMargins());
                        inventoryItems.setBUpdate(searchUpdateProductList.get(i).getBUpdate());
                        inventoryItems.setStatus(searchUpdateProductList.get(i).getStatus());

                    }


                }
                else
                {
                    inventoryItems.setNewPrice(searchUpdateProductList.get(i).getNewPrice());
                    inventoryItems.setMargins(searchUpdateProductList.get(i).getMargins());
                    inventoryItems.setBUpdate(searchUpdateProductList.get(i).getBUpdate());
                    inventoryItems.setStatus(searchUpdateProductList.get(i).getStatus());


                }
                showUpdateProductList.add(inventoryItems);

            }

            newlyUpdatedAdapter = new NewlyUpdatedAdapter(getActivity(), R.layout.items_rowlayout, R.id.item_name, showUpdateProductList, productTestId, searchUpdateProductList);
            listview.setAdapter(newlyUpdatedAdapter);
            LoadingDialog.cancelLoading();
            listview.setDivider(new ColorDrawable(getResources().getColor(R.color.background_light)));
            listview.setDividerHeight(1);
            listview.setTextFilterEnabled(true);
        }


    }

}
