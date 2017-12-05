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
import com.CobaltConnect1.generated.model.Inventory;
import com.CobaltConnect1.generated.model.MarginLocalData;
import com.CobaltConnect1.generated.model.MarginUpdate;
import com.CobaltConnect1.generated.model.MarginUpdateResponse;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class MyProductAdapter extends ArrayAdapter<Inventory> implements Filterable {
    private RetrofitInterface.MerchantMarginUpdateClient UpdateMarginAdapter;
    int groupid;
    ArrayList<Inventory> itemList;
    ArrayList<Inventory> filterItemList;
    ArrayList<Inventory> displayItemList;
    ArrayList<Inventory> tempItemList;
    Context context;
    String listItemId = null;
    String updatedMargin;
    int count =1;
    boolean isChecked =false;
    ArrayList<MarginLocalData> productTestId;
    String newPrice;
    ArrayList<Inventory> myProductList;


    public MyProductAdapter(FragmentActivity context, int vg, int id, ArrayList<Inventory> itemList, ArrayList<MarginLocalData> productTestId, ArrayList<Inventory> myProductList){

        super(context,vg, id, itemList);
        this.context=context;
        groupid=vg;
        tempItemList = new ArrayList<>(myProductList);
        filterItemList = new ArrayList<>(itemList);
        displayItemList = new ArrayList<>();
        this.itemList=itemList;
        this.productTestId = productTestId;
        this.myProductList = myProductList;

    }
    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView itemName;
        public TextView itemId;
        public TextView previousPrice;
        public TextView prevCost;
        public TextView newPrice;
        public TextView newCost;
        public EditText margin;
        public TextView wholesaler;
        public TextView status;
        public TextView category;
        public TextView stock;
        public Button updateButton;




    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.itemName= (TextView) rowView.findViewById(R.id.item_name);
            viewHolder.prevCost= (TextView) rowView.findViewById(R.id.previous_cost);
            viewHolder.newPrice= (TextView) rowView.findViewById(R.id.new_price);
            viewHolder.margin= (EditText) rowView.findViewById(R.id.margin_int);
            viewHolder.newCost= (TextView) rowView.findViewById(R.id.new_cost);
            viewHolder.itemId= (TextView) rowView.findViewById(R.id.item_id);
            viewHolder.previousPrice= (TextView) rowView.findViewById(R.id.prev_price);
            viewHolder.status= (TextView) rowView.findViewById(R.id.status);
            viewHolder.wholesaler= (TextView) rowView.findViewById(R.id.wholesale);
            viewHolder.updateButton= (Button) rowView.findViewById(R.id.update);
            viewHolder.stock= (TextView) rowView.findViewById(R.id.stock);
            viewHolder.category= (TextView) rowView.findViewById(R.id.category);


            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        final Inventory inventoryItems = getItem(position);
        final ViewHolder holder = (ViewHolder) rowView.getTag();

        if (inventoryItems !=null) {

            //  count = Integer.parseInt(inventoryItems.getMargin());
            holder.itemName.setText(inventoryItems.getName());
            holder.previousPrice.setText(inventoryItems.getPreviousPrice());
            holder.newPrice.setText(inventoryItems.getNewPrice());
            holder.prevCost.setText(inventoryItems.getPreviousCost());
            holder.category.setText(inventoryItems.getCategory());
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




            holder.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatedMargin = holder.margin.getText().toString();
                    UpdateMarginAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMarginUpdateClient.class, BASE_URL, getContext());
                    Call<MarginUpdateResponse> call = UpdateMarginAdapter.merchantMarginUpdate(new MarginUpdate("marginMyProducts", PrefUtils.getAuthToken(getContext()),inventoryItems.getProductId(),updatedMargin));
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        call.enqueue(new Callback<MarginUpdateResponse>() {

                            @Override
                            public void onResponse(Call<MarginUpdateResponse> call, Response<MarginUpdateResponse> response) {

                                if (response.isSuccessful()) {

                                    if (response.body().getMsg().equals("Margin Updated")) {
                                        inventoryItems.setNewPrice(response.body().getNewPrice());
                                        inventoryItems.setMargin(response.body().getMargin());
                                        inventoryItems.setStatus("Queued");
                                        MyProductAdapter.this.notifyDataSetChanged();

                                        MarginLocalData marginLocalData = new MarginLocalData();
                                        if (productTestId.size() !=0) {
                                            for (int j = 0; j < productTestId.size(); j++) {
                                                if (inventoryItems.getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                                                    productTestId.get(j).setMargin(response.body().getMargin());
                                                    productTestId.get(j).setNewPrice(response.body().getNewPrice());
                                                    productTestId.get(j).setStatus("Queued");
                                                    break;
                                                }

                                                if ( j==(productTestId.size()-1))
                                                {
                                                    marginLocalData.setProductId(inventoryItems.getProductId());
                                                    marginLocalData.setNewPrice(response.body().getNewPrice());
                                                    marginLocalData.setMargin(response.body().getMargin());
                                                    marginLocalData.setStatus("Queued");
                                                    productTestId.add(marginLocalData);
                                                }
                                            }



                                        }
                                        else
                                        {
                                            marginLocalData.setProductId(inventoryItems.getProductId());
                                            marginLocalData.setNewPrice(response.body().getNewPrice());
                                            marginLocalData.setMargin(response.body().getMargin());
                                            marginLocalData.setStatus("Queued");
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
                    displayItemList = (ArrayList<Inventory>) results.values;

                    if (results != null && results.count > 0) {
                        clear();
                        for (Inventory inventoryItems : new ArrayList<>(displayItemList)) {

                            add(inventoryItems);
                            notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Inventory> FilteredArrList = new ArrayList<>();
                ArrayList<Inventory> FilteredArrList1 = new ArrayList<>();
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
                        String data = tempItemList.get(i).getName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            Inventory inventoryItems = new Inventory();
                            inventoryItems.setName(tempItemList.get(i).getName());
                            inventoryItems.setPreviousPrice(tempItemList.get(i).getPreviousPrice());


                      if (productTestId.size() !=0) {
                          for (int j = 0; j < productTestId.size(); j++) {
                              if (tempItemList.get(i).getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                                 // Log.e(TAG, "performFiltering: if ======================" + productTestId.get(j).getMargin() + "  " + productTestId.get(j).getNewPrice() );
                                  inventoryItems.setNewPrice(productTestId.get(j).getNewPrice());
                                  inventoryItems.setMargin(productTestId.get(j).getMargin());
                                  break;
                              }
                              inventoryItems.setNewPrice(tempItemList.get(i).getNewPrice());
                              inventoryItems.setMargin(tempItemList.get(i).getMargin());

                          }


                      }
                      else
                      {
                          inventoryItems.setNewPrice(tempItemList.get(i).getNewPrice());
                          inventoryItems.setMargin(tempItemList.get(i).getMargin());

                      }



                            inventoryItems.setNewCost(tempItemList.get(i).getNewCost());
                            inventoryItems.setStatus(tempItemList.get(i).getStatus());
                            inventoryItems.setPreviousCost(tempItemList.get(i).getPreviousCost());
                            inventoryItems.setWholeSaler(tempItemList.get(i).getWholeSaler());
                            inventoryItems.setCloverId(tempItemList.get(i).getCloverId());
                            inventoryItems.setProductId(tempItemList.get(i).getProductId());
                            inventoryItems.setCategory(tempItemList.get(i).getCategory());
                            inventoryItems.setStock(tempItemList.get(i).getStock());
                            FilteredArrList.add(inventoryItems);

                        }
                    }
                    // set the Filtered result to return

                    for (int i = 0; i < filterItemList.size()&& i < FilteredArrList.size(); i++) {
                        Inventory inventoryItems = new Inventory();
                        inventoryItems.setName(FilteredArrList.get(i).getName());
                        inventoryItems.setNewPrice(FilteredArrList.get(i).getNewPrice());
                        inventoryItems.setPreviousCost(FilteredArrList.get(i).getPreviousCost());
                        inventoryItems.setMargin(FilteredArrList.get(i).getMargin());
                        inventoryItems.setWholeSaler(FilteredArrList.get(i).getWholeSaler());
                        inventoryItems.setNewCost(FilteredArrList.get(i).getNewCost());
                        inventoryItems.setStatus(FilteredArrList.get(i).getStatus());
                        inventoryItems.setBUpdate(FilteredArrList.get(i).getBUpdate());
                        inventoryItems.setPreviousPrice(FilteredArrList.get(i).getPreviousPrice());
                        inventoryItems.setProductId(FilteredArrList.get(i).getProductId());
                        inventoryItems.setCloverId(FilteredArrList.get(i).getCloverId());
                        inventoryItems.setCategory(FilteredArrList.get(i).getCategory());
                        inventoryItems.setStock(FilteredArrList.get(i).getStock());
                        FilteredArrList1.add(inventoryItems);
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

