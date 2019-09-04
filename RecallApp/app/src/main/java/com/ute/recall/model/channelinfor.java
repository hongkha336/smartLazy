package com.ute.recall.model;

public class channelinfor {
    public channelinfor() {

    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getWritekey() {
        return Writekey;
    }

    public void setWritekey(String writekey) {
        Writekey = writekey;
    }

    public String getReadkey() {
        return Readkey;
    }

    public void setReadkey(String readkey) {
        Readkey = readkey;
    }
    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }


    public channelinfor(int channelID, String writekey, String readkey, String channelName) {
        this.channelID = channelID;
        Writekey = writekey;
        Readkey = readkey;
        ChannelName = channelName;
    }

    private   int channelID;
    private   String Writekey;
    private   String Readkey;



    private String ChannelName;
}
