package com.ute.recall.model;

import java.io.Serializable;

public class History implements Serializable {
    String time;
    String temperature;
    String humidity;
    String soil;

    public History() {
    }

    public History(String time, String temperature, String humidity, String soil) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.soil = soil;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }
}