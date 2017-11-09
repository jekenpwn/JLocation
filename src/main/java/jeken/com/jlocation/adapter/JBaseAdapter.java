package jeken.com.jlocation.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by jeken on 2017/10/25.
 */

public abstract class JBaseAdapter<T> extends BaseAdapter{
    private List<T> data;
    private Context mContext;
    public JBaseAdapter(Context mContext,List<T> data){
        this.mContext = mContext;
        this.data = data;
    }
    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        //filter the unexpected condition
        if (position < 0 ) return null;
        if (data == null) return  null;
        if (position>=data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            if (setConverView() instanceof View){
                convertView = (View) setConverView();
            }else {
                convertView = View.inflate(mContext, (Integer) setConverView(), null);
            }
        }
        setViewHold(convertView,position);
        return convertView;
    }

    public abstract Object setConverView();
    public abstract void setViewHold(View convertView,int position);
}
