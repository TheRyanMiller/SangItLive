package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ryan on 8/1/2016.
 */
public class CustomAdapter extends BaseAdapter {

    private List<Response.SetlistsBean.SetlistBean> setlistItem;
    private Context mContext;
    private LayoutInflater inflater;
    TextView venue;
    TextView date;

    public CustomAdapter(List<Response.SetlistsBean.SetlistBean> setlistItem, Context context) {
        this.setlistItem = setlistItem;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return setlistItem.size();
    }

    @Override
    public Object getItem(int position) {
        return setlistItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View rowView = inflater.inflate(R.layout.each_list_item, parent, false);

        Response.SetlistsBean.SetlistBean item = (Response.SetlistsBean.SetlistBean) getItem(position);
        venue = (TextView) rowView.findViewById(R.id.venue);
        date = (TextView) rowView.findViewById(R.id.date);
        venue.setText(item.getVenue().getName());
        date.setText(item.getEventDate().toString());
        return rowView;
    }
}
