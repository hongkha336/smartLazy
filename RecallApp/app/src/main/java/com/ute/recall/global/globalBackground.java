package com.ute.recall.global;

import java.util.ArrayList;
import java.util.List;

public class globalBackground {
    private   static List<modelGolbal> lisGlobal = new ArrayList<>();

    public static modelGolbal  getChannelByID(String id)
    {
        modelGolbal model = new modelGolbal();

        for(int i =lisGlobal.size() -1; i>=0; i--)
        {
            if(lisGlobal.get(i).getChannelKey().equals(id))
            {
                model = lisGlobal.get(i);
                break;
            }
        }

        try
        {
            lisGlobal.remove(model);
        }catch (Exception e)
        {

        }

        return model;
    }

    public static void addModel(modelGolbal m)
    {
        lisGlobal.add(m);
    }


}
