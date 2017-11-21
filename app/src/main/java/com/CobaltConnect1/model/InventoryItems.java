
package com.CobaltConnect1.model;


public class InventoryItems {


    private String previousPrice;

    private String itemName;

    private String currentPrice;

    private String finalPrice;

    private String margin;

    private String itemId;

    private String stockQuantity;

    private String reorder;

    public InventoryItems(String itemName, String previousPrice, String margin ,String itemId,String currentPrice ,String stockQuantity,String reorder,String finalPrice) {
        super();
        this.itemName = itemName;
        this.previousPrice = previousPrice;
        this.currentPrice = currentPrice;
        this.itemId = itemId;
        this.margin = margin;
        this.stockQuantity = stockQuantity;
        this.reorder = reorder;
        this.finalPrice = finalPrice;

    }

    public InventoryItems() {

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    public String getReorder() {
        return reorder;
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }



    public String getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(String previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }



    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMargin() {
        return margin ;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
