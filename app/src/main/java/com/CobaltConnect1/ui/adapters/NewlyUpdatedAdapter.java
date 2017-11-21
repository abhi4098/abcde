package com.CobaltConnect1.ui.adapters;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.LinearLayout;
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


public class NewlyUpdatedAdapter extends ArrayAdapter<Inventory> implements Filterable{
    private RetrofitInterface.MerchantMarginUpdateClient UpdateMarginAdapter;
    int groupid;
    String TAG = "CobaltConnect";
    ArrayList<Inventory> itemList;
    ArrayList<Inventory> updateProductList;
    ArrayList<Inventory> filterItemList;

    ArrayList<Inventory> displayItemList;
    ArrayList<Inventory> tempItemList;
    Context context;
    String listItemId = null;
    String updatedMargin;
    ArrayList<MarginLocalData> productTestId;
    int count =1;
    boolean isChecked =false;

    public NewlyUpdatedAdapter(FragmentActivity context, int vg, int id, ArrayList<Inventory> itemList, ArrayList<MarginLocalData> productTestId, ArrayList<Inventory> updateProductList){
        super(context,vg, id, itemList);
        this.context=context;
        groupid=vg;
        tempItemList = new ArrayList<>(updateProductList);
        displayItemList = new ArrayList<>();
        filterItemList = new ArrayList<>(itemList);
        this.productTestId = new ArrayList<>();
        this.itemList=itemList;
        this.updateProductList = updateProductList;

    }



    // Hold views of the ListView to improve its scrolling performance
    static class ViewHolder {
        public TextView itemName;
        public TextView previousPrice;
        public TextView prevCost;
        public TextView newPrice;
        public TextView newCost;
        public EditText margin;
        public TextView wholesaler;
        public TextView status;
        //public RadioButton reorder;
        public LinearLayout llMargin;
        public Button decreaseButton;
        public Button increaseButton;



    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.itemName= (TextView) rowView.findViewById(R.id.item_name);
            // viewHolder.tableRow= (TableRow) rowView.findViewById(R.id.tableRow);
            viewHolder.previousPrice= (TextView) rowView.findViewById(R.id.prev_price);
            viewHolder.newPrice= (TextView) rowView.findViewById(R.id.new_price);
            viewHolder.prevCost= (TextView) rowView.findViewById(R.id.previous_cost);
            viewHolder.newCost= (TextView) rowView.findViewById(R.id.new_cost);
            viewHolder.margin= (EditText) rowView.findViewById(R.id.integer_number);
            viewHolder.wholesaler= (TextView) rowView.findViewById(R.id.wholesaler);
            viewHolder.status= (TextView) rowView.findViewById(R.id.status);
            // viewHolder.reorder= (RadioButton) rowView.findViewById(R.id.reorder);
          //  viewHolder.decreaseButton= (Button) rowView.findViewById(R.id.decrease);
            viewHolder.increaseButton= (Button) rowView.findViewById(R.id.increase);
          //  viewHolder.llMargin= (LinearLayout) rowView.findViewById(R.id.margin);


            rowView.setTag(viewHolder);

        }
        // Set text to each TextView of ListView item
        final Inventory inventoryItems = getItem(position);
        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final View finalRowView = rowView;

        if (inventoryItems !=null) {

            //  count = Integer.parseInt(inventoryItems.getMargin());
            holder.itemName.setText(inventoryItems.getName());
            holder.previousPrice.setText(inventoryItems.getPreviousPrice());
            holder.newPrice.setText(inventoryItems.getNewPrice());
            holder.prevCost.setText(inventoryItems.getPreviousCost());
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

            if(inventoryItems.getBUpdate() == 1)
            {
                finalRowView.setBackgroundColor(Color.rgb(223,240,216));
            }
            else
            {
                finalRowView.setBackgroundColor(Color.WHITE);
            }
            //holder.reorder.setText(inventoryItems.getReorder());
           /* holder.reorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (holder.reorder.isChecked()) {
                        Log.e(TAG, "onCheckedChanged: ===");
                        holder.reorder.setChecked(false);
                    } else {
                        holder.reorder.setChecked(true);
                        Log.e(TAG, "onCheckedChanged: ===============================");
                    }


                }
            });*/
           holder.margin.setText(inventoryItems.getMargins());



      /*      holder.decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*if (count > 0)

                    {
                       // finalRowView.setBackgroundColor(Color.GREEN);
                        if (listItemId == null || listItemId.equals(inventoryItems.getProductId())) {

                            count = inventoryItems.getMargins();
                            inventoryItems.setMargins(--count);
                            holder.margin.setText(String.valueOf(inventoryItems.getMargins()));
                            listItemId = inventoryItems.getProductId();
                            Log.e(TAG, "onClick: if--------------------- " + inventoryItems.getProductId());


                        } else {
                            listItemId = null;
                            count = inventoryItems.getMargins();
                            inventoryItems.setMargins(--count);
                            holder.margin.setText(String.valueOf(inventoryItems.getMargins()));
                            Log.e(TAG, "onClick: else----------------------" + inventoryItems.getProductId());
                        }

                    }*//*
                }
            });*/


            holder.increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatedMargin = holder.margin.getText().toString();
                    UpdateMarginAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantMarginUpdateClient.class, BASE_URL, getContext());
                    Call<MarginUpdateResponse> call = UpdateMarginAdapter.merchantMarginUpdate(new MarginUpdate("marginUpdate", PrefUtils.getAuthToken(getContext()),inventoryItems.getProductId(),updatedMargin));
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        call.enqueue(new Callback<MarginUpdateResponse>() {

                            @Override
                            public void onResponse(Call<MarginUpdateResponse> call, Response<MarginUpdateResponse> response) {

                                if (response.isSuccessful()) {
                                    //Log.e(TAG, "onResponse: ==================" +response.body().getMsg() );
                                    if (response.body().getMsg().equals("Margin Updated")) {
                                        inventoryItems.setNewPrice(response.body().getNewPrice());
                                        inventoryItems.setMargins(response.body().getMargin());
                                        inventoryItems.setBUpdate(1);
                                        inventoryItems.setStatus("Queued");
                                        NewlyUpdatedAdapter.this.notifyDataSetChanged();
                                        MarginLocalData marginLocalData = new MarginLocalData();
                                        if (productTestId.size() !=0) {
                                            for (int j = 0; j < productTestId.size(); j++) {
                                                if (inventoryItems.getProductId().equals(productTestId.get(j).getProductId()) && productTestId.get(j).getProductId() != null) {
                                                    productTestId.get(j).setMargin(response.body().getMargin());
                                                    productTestId.get(j).setNewPrice(response.body().getNewPrice());
                                                    productTestId.get(j).setBUpdate(1);
                                                    productTestId.get(j).setStatus("Queued");

                                                    break;
                                                }

                                                if ( j==(productTestId.size()-1))
                                                {
                                                    marginLocalData.setProductId(inventoryItems.getProductId());
                                                    marginLocalData.setNewPrice(response.body().getNewPrice());
                                                    marginLocalData.setMargin(response.body().getMargin());
                                                    marginLocalData.setBUpdate(1);
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
                                            marginLocalData.setBUpdate(1);
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
                  // getUpdatedMargin(v);
                  /*  if (count >=0)

                    {
                        if (listItemId == null || listItemId.equals(inventoryItems.getProductId())) {
                            count = inventoryItems.getMargins();
                            inventoryItems.setMargins(++count);
                            holder.margin.setText(String.valueOf(inventoryItems.getMargins()));
                            listItemId = inventoryItems.getProductId();
                            Log.e(TAG, "onClick: if++++++++++++++++++++++++++++++ " + inventoryItems.getProductId());
                        } else {
                            listItemId = null;
                            count = inventoryItems.getMargins();
                            inventoryItems.setMargins(++count);
                            holder.margin.setText(String.valueOf(inventoryItems.getMargins()));
                            Log.e(TAG, "onClick: else++++++++++++++++++++" + inventoryItems.getProductId());
                        }

                    }*/
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
                                        inventoryItems.setNewPrice(productTestId.get(j).getNewPrice());
                                        inventoryItems.setMargins(productTestId.get(j).getMargin());
                                        inventoryItems.setBUpdate(productTestId.get(j).getBUpdate());
                                        break;
                                    }
                                    inventoryItems.setNewPrice(tempItemList.get(i).getNewPrice());
                                    inventoryItems.setMargins(tempItemList.get(i).getMargins());
                                    inventoryItems.setBUpdate(tempItemList.get(i).getBUpdate());
                                    Log.e(TAG, "performFiltering: ============"+ tempItemList.get(i).getMargins() );
                                }


                            }
                            else
                            {
                                inventoryItems.setNewPrice(tempItemList.get(i).getNewPrice());
                                inventoryItems.setMargins(tempItemList.get(i).getMargins());
                                inventoryItems.setBUpdate(tempItemList.get(i).getBUpdate());
                                Log.e(TAG, "performFiltering: ============"+ tempItemList.get(i).getMargins() );
                            }
                            inventoryItems.setNewCost(tempItemList.get(i).getNewCost());
                            inventoryItems.setStatus(tempItemList.get(i).getStatus());
                            inventoryItems.setPreviousCost(tempItemList.get(i).getPreviousCost());
                            inventoryItems.setWholeSaler(tempItemList.get(i).getWholeSaler());
                            inventoryItems.setProductId(tempItemList.get(i).getProductId());

                            // inventoryItems.setReorder(tempItemList.get(i).getReorder());
                            FilteredArrList.add(inventoryItems);

                        }
                    }
                    Log.e("abhi", "filterlist size" +filterItemList.size() + FilteredArrList.size() );
                    for (int i = 0; i < filterItemList.size() && i < FilteredArrList.size(); i++) {
                        Inventory inventoryItems = new Inventory();
                        inventoryItems.setName(FilteredArrList.get(i).getName());
                        inventoryItems.setNewPrice(FilteredArrList.get(i).getNewPrice());
                        inventoryItems.setPreviousCost(FilteredArrList.get(i).getPreviousCost());
                        inventoryItems.setMargins(FilteredArrList.get(i).getMargins());
                        inventoryItems.setWholeSaler(FilteredArrList.get(i).getWholeSaler());
                        inventoryItems.setNewCost(FilteredArrList.get(i).getNewCost());
                        inventoryItems.setStatus(FilteredArrList.get(i).getStatus());
                        inventoryItems.setBUpdate(FilteredArrList.get(i).getBUpdate());
                        inventoryItems.setPreviousPrice(FilteredArrList.get(i).getPreviousPrice());
                        inventoryItems.setProductId(FilteredArrList.get(i).getProductId());
                        //inventoryItems.setCloverId(FilteredArrList.get(i).getCloverId());
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
