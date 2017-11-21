
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductUpdateResponse {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("inventory")
    @Expose
    private List<Inventory> inventory = null;

    @SerializedName("New")
    @Expose
    private Integer _new;
    @SerializedName("Queued")
    @Expose
    private Integer queued;
    @SerializedName("Processed")
    @Expose
    private Integer processed;
    @SerializedName("Others")
    @Expose
    private Integer others;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    public Integer getNew() {
        return _new;
    }

    public void setNew(Integer _new) {
        this._new = _new;
    }

    public Integer getQueued() {
        return queued;
    }

    public void setQueued(Integer queued) {
        this.queued = queued;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public Integer getOthers() {
        return others;
    }

    public void setOthers(Integer others) {
        this.others = others;
    }


}
