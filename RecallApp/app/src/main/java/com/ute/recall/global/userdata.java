package com.ute.recall.global;

import java.util.ArrayList;
import java.util.List;
import com.ute.recall.model.channelinfor;
import com.ute.recall.model.channelConfig;
public class userdata {
    public static int getChannelID() {
        return channelID;
    }

    public static void setChannelID(int channelID) {
        userdata.channelID = channelID;
    }

    public static String getWritekey() {
        return Writekey;
    }

    public static void setWritekey(String writekey) {
        Writekey = writekey;
    }

    public static String getReadkey() {
        return Readkey;
    }

    public static void setReadkey(String readkey) {
        Readkey = readkey;
    }

    private  static int channelID;
    private  static String Writekey;
    private  static String Readkey;

    public static String getChannelName() {
        return ChannelName;
    }

    public static void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    private static  String ChannelName;

    public static List<channelinfor> ml = new ArrayList<>();


    public static com.ute.recall.model.channelConfig getChannelConfig() {
        return channelConfig;
    }

    public static void setChannelConfig(com.ute.recall.model.channelConfig channelConfig) {
        userdata.channelConfig = channelConfig;
    }

    private static channelConfig channelConfig;


    public static void createChannelConfigDefault(){
        channelConfig = new channelConfig();
        channelConfig.setAuto(0);
        channelConfig.setWarn(1);
        channelConfig.setChannelID(userdata.channelID);
        channelConfig.setTemp(30.0);
        channelConfig.setHumi(80.0);
        channelConfig.setSoil(10.0);
        channelConfig.setPump(30.0);
    }
}
