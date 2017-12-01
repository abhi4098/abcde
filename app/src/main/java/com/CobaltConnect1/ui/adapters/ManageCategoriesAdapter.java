package com.CobaltConnect1.ui.adapters;

/**
 * Created by Abhinandan on 21/9/17.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.CategoryList;
import com.CobaltConnect1.generated.model.DefaultMarginUpdate;
import com.CobaltConnect1.generated.model.Inventory;
import com.CobaltConnect1.generated.model.MarginLocalData;
import com.CobaltConnect1.generated.model.MarginUpdate;
import com.CobaltConnect1.generated.model.MarginUpdateResponse;
import com.CobaltConnect1.generated.model.MinimumStockUpdate;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class ManageCategoriesAdapter extends ArrayAdapter<CategoryList> implements Filterable {
    private RetrofitInterface.MerchantDefaultMarginUpdateClient UpdateMarginAdapter;
    private RetrofitInterface.MerchantMinStockUpdateClient UpdateMinStockAdapter;
    int groupid;
    ArrayList<CategoryList> itemList;
    ArrayList<CategoryList> filterItemList;
    ArrayList<CategoryList> displayItemList;
    ArrayList<CategoryList> tempItemList;
    Context context;
    String listItemId = null;
    String updatedMargin,updateStock;
    int count =1;
    boolean isChecked =false;
   // ArrayList<MarginLocalData> productTestId;
    ArrayList<MarginLocalData> productTestId;

    String newPrice;
    ArrayList<CategoryList> myProductList;


    public ManageCategoriesAdapter(FragmentActivity context, int vg, int id, ArrayList<CategoryList> itemList, ArrayList<MarginLocalData> productTestId, ArrayList<CategoryList> myProductList){

        super(context,vg, id, itemList);
        this.context=context;
        groupid=vg;
        tempItemList = new ArrayList<>(myProductList);
        filterItemList = new ArrayList<>(itemList);
        displayItemList = new ArrayList<>();
        this.itemList=itemList;
        this.productTestId = new ArrayList<>();
        //this.productTestId = new ArrayList<>();

        this.myProductList = myProductList;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView itemName;
        public TextView itemId;
        public EditText minimumStock;
        public EditText defaultMargin;
        public Button minimumStockButton;
        public Button defaultMarginButton;




    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.itemName= (TextView) rowView.findViewById(R.id.item_category);
            viewHolder.itemId= (TextView) rowView.findViewById(R.id.item_id);
            viewHolder.minimumStock= (EditText) rowView.findViewById(R.id.stock_quantity);
            viewHolder.defaultMargin= (EditText) rowView.findViewById(R.id.margin_int);
            viewHolder.minimumStockButton= (Button) rowView.findViewById(R.id.update_stock);
            viewHolder.defaultMarginButton= (Button) rowView.findViewById(R.id.update_margin);



            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        final CategoryList categoryList = getItem(position);
        final ViewHolder holder = (ViewHolder) rowView.getTag();

        if (categoryList !=null) {

            //  count = Integer.parseInt(inventoryItems.getMargin());
            holder.itemName.setText(categoryList.getTitle());
            holder.itemId.setText(categoryList.getCategoryId());
            holder.minimumStock.setText(categoryList.getMinStock());
            holder.defaultMargin.setText(categoryList.getDefaultMargin());
            /*holder.category.setText(inventoryItems.getCategory());
            holder.stock.setText(String.valueOf(inventoryItems.getStock()));
            holder.wholesaler.setText(inventoryItems.getWholeSaler());
            holder.status.setText(String.valueOf(inventoryItems.getStatus()));
            switch (inventoryItems.getStatus()) {
                case "New":
                    holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_background_new));
                    break;
                case "Queued":
                    holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_background_queued));
                    break;
                case "Processed":
                    holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_background_processed));
                    break;
                default:
                    holder.status.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_background_others));
                    break;
            }

            holder.newCost.setText(inventoryItems.getNewCost());
            holder.margin.setText(inventoryItems.getMargin());
            holder.itemId.setText(inventoryItems.getCloverId());
*/




            holder.defaultMarginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatedMargin = holder.defaultMargin.getText().toString();
                    UpdateMarginAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantDefaultMarginUpdateClient.class, BASE_URL, getContext());
                    Call<MarginUpdateResponse> call = UpdateMarginAdapter.merchantDefaultMarginUpdate(new DefaultMarginUpdate("updateCategoryMargin", PrefUtils.getAuthToken(getContext()),categoryList.getCategoryId(),updatedMargin));
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        call.enqueue(new Callback<MarginUpdateResponse>() {

                            @Override
                            public void onResponse(Call<MarginUpdateResponse> call, Response<MarginUpdateResponse> response) {

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equals("Margin updated!")) {

                                        categoryList.setDefaultMargin(response.body().getMargin());
                                        categoryList.setMinStock(response.body().getStock());
                                        ManageCategoriesAdapter.this.notifyDataSetChanged();

                                        MarginLocalData marginLocalData = new MarginLocalData();

                                        if (productTestId.size() !=0) {
                                            for (int j = 0; j < productTestId.size(); j++) {
                                                if (categoryList.getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {
                                                    productTestId.get(j).setDefaultMargin(response.body().getMargin());
                                                    productTestId.get(j).setMinStock(response.body().getStock());
                                                    Log.e("abhi", "onResponse: margin ---id matches"  );
                                                    break;
                                                }

                                                if ( j==(productTestId.size()-1))
                                                {
                                                    marginLocalData.setCategoryId(categoryList.getCategoryId());
                                                    marginLocalData.setDefaultMargin(response.body().getMargin());
                                                    marginLocalData.setMinStock(response.body().getStock());
                                                    productTestId.add(marginLocalData);
                                                }
                                            }



                                        }
                                        else
                                        {
                                            marginLocalData.setCategoryId(categoryList.getCategoryId());
                                            marginLocalData.setDefaultMargin(response.body().getMargin());
                                            marginLocalData.setMinStock(response.body().getStock());
                                            productTestId.add(marginLocalData);

                                        }



                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<MarginUpdateResponse> call, Throwable t) {

                            }


                        });

                    } else {
                        SnakBarUtils.networkConnected(getContext());
                    }

                }
            });


            holder.minimumStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStock = holder.minimumStock.getText().toString();
                    UpdateMinStockAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMinStockUpdateClient.class, BASE_URL, getContext());
                    Call<MarginUpdateResponse> call = UpdateMinStockAdapter.merchantMinStockUpdate(new MinimumStockUpdate("updateCategoryStock", PrefUtils.getAuthToken(getContext()),categoryList.getCategoryId(),updateStock));
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        call.enqueue(new Callback<MarginUpdateResponse>() {

                            @Override
                            public void onResponse(Call<MarginUpdateResponse> call, Response<MarginUpdateResponse> response) {

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equals("Stock level updated!")) {
                                        categoryList.setDefaultMargin(response.body().getMargin());
                                        categoryList.setMinStock(response.body().getStock());

                                        ManageCategoriesAdapter.this.notifyDataSetChanged();

                                        MarginLocalData marginLocalData = new MarginLocalData();
                                        ;
                                        if (productTestId.size() !=0) {
                                            for (int j = 0; j < productTestId.size(); j++) {
                                                if (categoryList.getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {

                                                    productTestId.get(j).setDefaultMargin(response.body().getMargin());
                                                    productTestId.get(j).setMinStock(response.body().getStock());

                                                    break;
                                                }

                                                if ( j==(productTestId.size()-1))
                                                {
                                                    marginLocalData.setCategoryId(categoryList.getCategoryId());
                                                    marginLocalData.setDefaultMargin(response.body().getMargin());
                                                    marginLocalData.setMinStock(response.body().getStock());

                                                    productTestId.add(marginLocalData);
                                                }
                                            }



                                        }
                                        else
                                        {
                                            marginLocalData.setCategoryId(categoryList.getCategoryId());
                                            marginLocalData.setDefaultMargin(response.body().getMargin());
                                            marginLocalData.setMinStock(response.body().getStock());
                                            productTestId.add(marginLocalData);

                                        }



                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<MarginUpdateResponse> call, Throwable t) {

                            }


                        });

                    } else {
                        SnakBarUtils.networkConnected(getContext());
                    }

                }
            });

        }



        return rowView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                if (results.count == 0)
                {
                    notifyDataSetInvalidated();
                }
                else {
                    displayItemList = (ArrayList<CategoryList>) results.values;

                    if (results != null && results.count > 0) {
                        clear();
                        for (CategoryList categoryList  : new ArrayList<>(displayItemList)) {

                            add(categoryList);
                            notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CategoryList> FilteredArrList = new ArrayList<>();
                ArrayList<CategoryList> FilteredArrList1 = new ArrayList<>();
                if (filterItemList ==null)
                {
                    filterItemList =new ArrayList<>(itemList);
                }

                if (tempItemList == null) {
                    tempItemList = new ArrayList<>(displayItemList); // saves the original data in itemList
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = filterItemList.size();
                    results.values = filterItemList;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < tempItemList.size(); i++) {
                        String data = tempItemList.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            CategoryList categoryList = new CategoryList();
                            categoryList.setTitle(tempItemList.get(i).getTitle());
                            categoryList.setCategoryId(tempItemList.get(i).getCategoryId());


                            if (productTestId.size() !=0) {
                                for (int j = 0; j < productTestId.size(); j++) {
                                    if (tempItemList.get(i).getCategoryId().equals(productTestId.get(j).getCategoryId()) && productTestId.get(j).getCategoryId() != null) {
                                        // Log.e(TAG, "performFiltering: if ======================" + productTestId.get(j).getMargin() + "  " + productTestId.get(j).getNewPrice() );
                                        categoryList.setDefaultMargin(productTestId.get(j).getMargin());
                                        categoryList.setMinStock(productTestId.get(j).getMinStock());
                                        break;
                                    }
                                    categoryList.setDefaultMargin(tempItemList.get(i).getDefaultMargin());
                                    categoryList.setMinStock(tempItemList.get(i).getMinStock());

                                }


                            }
                            else
                            {
                                categoryList.setDefaultMargin(tempItemList.get(i).getDefaultMargin());
                                categoryList.setMinStock(tempItemList.get(i).getMinStock());
                            }



                            /*inventoryItems.setNewCost(tempItemList.get(i).getNewCost());
                            inventoryItems.setStatus(tempItemList.get(i).getStatus());
                            inventoryItems.setPreviousCost(tempItemList.get(i).getPreviousCost());
                            inventoryItems.setWholeSaler(tempItemList.get(i).getWholeSaler());
                            inventoryItems.setCloverId(tempItemList.get(i).getCloverId());
                            inventoryItems.setProductId(tempItemList.get(i).getProductId());
                            inventoryItems.setCategory(tempItemList.get(i).getCategory());
                            inventoryItems.setStock(tempItemList.get(i).getStock());*/
                            FilteredArrList.add(categoryList);

                        }
                    }
                    // set the Filtered result to return

                    for (int i = 0; i < filterItemList.size()&& i < FilteredArrList.size(); i++) {
                        CategoryList categoryList = new CategoryList();
                        categoryList.setTitle(FilteredArrList.get(i).getTitle());
                        categoryList.setCategoryId(FilteredArrList.get(i).getCategoryId());
                        categoryList.setMinStock(FilteredArrList.get(i).getMinStock());
                        categoryList.setDefaultMargin(FilteredArrList.get(i).getDefaultMargin());
                        FilteredArrList1.add(categoryList);
                    }


                    results.count = FilteredArrList1.size();
                    results.values = FilteredArrList1;
                }
                return results;
            }
        };
        return filter;
    }








}

