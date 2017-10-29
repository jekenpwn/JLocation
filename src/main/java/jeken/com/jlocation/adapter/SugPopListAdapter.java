package jeken.com.jlocation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.LinkedList;
import java.util.List;

import jeken.com.jlocation.R;

import static jeken.com.jlocation.adapter.ViewHold.mTextView;

/**
 * Created by jeken on 2017/10/25.
 */

public class SugPopListAdapter extends JBaseAdapter<SuggestionResult.SuggestionInfo>{
    private Context mContext;
    private List<SuggestionResult.SuggestionInfo> data;
    public SugPopListAdapter(Context mContext, LinkedList<SuggestionResult.SuggestionInfo> data) {
        super(mContext, data);
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public Object setConverView() {
        return R.layout.item_sugpop;
    }

    @Override
    public void setViewHold(View convertView, int position) {
        mTextView =  convertView.findViewById(R.id.tv_sugaddr);
        String addr = data.get(position).city+data.get(position).district
                +data.get(position).key;
        mTextView.setText(addr);
        Log.e("TAG","data:"+data.get(position));
    }
}
class ViewHold{
    static TextView mTextView;
}
