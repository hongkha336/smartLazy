package com.ute.recall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ute.recall.R;
import com.ute.recall.demoplant.HistoryActivity;
import com.ute.recall.model.feeds;

import java.util.ArrayList;
import java.util.List;

public class DateLayout extends BaseAdapter {


    int myLayout;
    List<feeds> feedsList;
    Context mContext;
    List<List<feeds>> feedSep;
    public DateLayout(Context mContext,int myLayout, List<feeds> feedsList, List<List<feeds>> feedSep)
    {
        this.myLayout = myLayout;
        this.mContext = mContext;
        this.feedsList = feedsList;
        this.feedSep = feedSep;
        System.out.println(feedSep.size());
        for(List<feeds> f : feedSep)
        {
            for(feeds ff : f)
            {
                System.out.println(ff.getCreated_at());
            }
            System.out.println("********************");
        }
    }

    @Override
    public int getCount() {
        return feedSep.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private String getDate(String d)
    {
        String str = "";
        while(d.length() > 0 && d.charAt(0) != 'T')
        {
            str = str + String.valueOf(d.charAt(0));
            d = d.substring(1,str.length()-1);
        }
        return str;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout,null);
        // Ánh xạ và gán giá trị


        TextView txtDate = convertView.findViewById(R.id.txtDate);
        txtDate.setText(feedsList.get(position).getCreated_at());
        String date = feedsList.get(position).getCreated_at();



//       List<feeds> nFeedsList = new ArrayList<>();
//        for(feeds  f: feedsList)
//        {
//            if(getDate((f.getCreated_at())).equals(getDate(date)))
//            {
//                nFeedsList.add(f);
//                feedsList.remove(f);
//            }
//        }
        ListView lv1 = convertView.findViewById(R.id.lv1);
        LogAdapter adapter = new LogAdapter(mContext,R.layout.logitem,feedSep.get(position));
        lv1.setAdapter(adapter);

        return convertView;
    }
}
