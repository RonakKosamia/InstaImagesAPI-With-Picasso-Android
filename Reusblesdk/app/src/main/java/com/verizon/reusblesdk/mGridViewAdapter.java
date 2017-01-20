package com.verizon.reusblesdk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ronak on 1/19/2017.
 */
public class mGridViewAdapter extends ArrayAdapter<mGridItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<mGridItem> myData = new ArrayList<mGridItem>();

    public mGridViewAdapter(Context mContext, int layoutResourceId, ArrayList<mGridItem> myData) {
        super(mContext, layoutResourceId, myData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.myData = myData;
    }


    /**
     * refresh grid items
     * Updates gridview items
     */
    public void setData(ArrayList<mGridItem> myData) {
        this.myData = myData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        mGridViewAdapter.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new mGridViewAdapter.ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (mGridViewAdapter.ViewHolder) row.getTag();
        }

        mGridItem item = myData.get(position);
        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
