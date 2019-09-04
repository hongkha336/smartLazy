package com.ute.recall.global;

public class modelGolbal {
    public modelGolbal() {

    }

    public String getChannelKey() {
        return channelKey;
    }

    public void setChannelKey(String channelKey) {
        this.channelKey = channelKey;
    }

    public String getReadKey() {
        return readKey;
    }

    public void setReadKey(String readKey) {
        this.readKey = readKey;
    }

    public String getWriteKey() {
        return writeKey;
    }

    public void setWriteKey(String writeKey) {
        this.writeKey = writeKey;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getHumi() {
        return Humi;
    }

    public void setHumi(String humi) {
        Humi = humi;
    }

    public String getSold() {
        return Sold;
    }

    public void setSold(String sold) {
        Sold = sold;
    }

    public String getIsPump() {
        return isPump;
    }

    public void setIsPump(String isPump) {
        this.isPump = isPump;
    }

    private String isPump;

    public modelGolbal(String channelKey, String readKey, String writeKey, String temp, String humi, String sold, String isPump) {
        this.channelKey = channelKey;
        this.readKey = readKey;
        this.writeKey = writeKey;
        Temp = temp;
        Humi = humi;
        Sold = sold;
        this.isPump = isPump;

    }

    private String channelKey;
    private String readKey;
    private String writeKey;
    private String Temp;
    private String Humi;
    private String Sold;

}
