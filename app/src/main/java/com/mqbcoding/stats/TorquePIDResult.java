package com.mqbcoding.stats;

public class TorquePIDResult {
    public float value;
    public String unit;
    public int min;
    public int max;

    public void setValue(float value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
