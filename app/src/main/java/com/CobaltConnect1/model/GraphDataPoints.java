package com.CobaltConnect1.model;

import com.jjoe64.graphview.series.DataPointInterface;

import java.io.Serializable;

/**
 * Created by Abhinandan on 14/7/17.
 */

public class GraphDataPoints implements DataPointInterface,Serializable {
    private static final long serialVersionUID=1428263322645L;

    public GraphDataPoints() {

    }

    private double x;
    private double y;


    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
