package com.ute.recall.model;

public class channelConfig {
    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumi() {
        return humi;
    }

    public void setHumi(Double humi) {
        this.humi = humi;
    }

    public Double getSoil() {
        return soil;
    }

    public void setSoil(Double soil) {
        this.soil = soil;
    }

    public int getWarn() {
        return warn;
    }

    public void setWarn(int warn) {
        this.warn = warn;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    private  int channelID;
    private  Double temp;
    private Double humi;
    private Double soil;
    private int warn = 1;
    private int auto = 1;

    public Double getPump() {
        return pump;
    }

    public void setPump(Double pump) {
        this.pump = pump;
    }

    private Double pump;
}
