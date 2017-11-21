package com.CobaltConnect1.model;

/**
 * Created by Abhinandan on 5/8/17.
 */

public class UnderStockItemPieChart {


    private String itemStock;
    private String itemName;


    public UnderStockItemPieChart(String itemName, String itemStock) {
        super();
        this.itemName = itemName;
        this.itemStock = itemStock;


    }
    public UnderStockItemPieChart() {

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemStock() {
        return itemStock;
    }

    public void setItemStock(String itemStock) {
        this.itemStock = itemStock;
    }

}
