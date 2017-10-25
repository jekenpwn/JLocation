package jeken.com.jlocation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;

/**
 * Created by jeken on 2017/10/25.
 */

public class JBaseAdapter<T> extends BaseAdapter{
    private LinkedList<T> data;
    private Context mContext;
    public JBaseAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
