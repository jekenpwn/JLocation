package jeken.com.jlocation.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.LinkedList;

import jeken.com.jlocation.R;
import jeken.com.jlocation.adapter.SugPopListAdapter;

/**
 * Created by jeken on 2017/10/24.
 */

public class SugSearchPopupWindow extends CommPopupWindow implements AdapterView.OnItemClickListener {

    private ListView listView;
    private SugPopListAdapter adapter;
    private LinkedList<SuggestionResult.SuggestionInfo> data;

    private SugPopItemClickListener onItemClickListener;

    public SugSearchPopupWindow(Context mContext, int mLayoutRes) {
        super(mContext, mLayoutRes);
        data = new LinkedList<SuggestionResult.SuggestionInfo>();
        adapter = new SugPopListAdapter(mContext,data);

    }
    public void initPopup(){
        super.init();
    }
    public void addItem(SuggestionResult.SuggestionInfo sugInfo){
        if (data!=null){
            data.add(sugInfo);
        }
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
    public void clearAllItem(){
        if (data!=null){
            data.clear();
        }
    }
    @Override
    public void initView(View view) {
        if (view == null) return;
        listView = (ListView) view.findViewById(R.id.sugpop_lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void initWindow(PopupWindow mPopupWindow) {

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x2A2AED));
        mPopupWindow.setFocusable(true);
    }
    public void setSugItemListener(SugPopItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onItemClickListener!=null){
                String addr = data.get(position).city+data.get(position).district
                        +data.get(position).key;
                onItemClickListener.getAddr(addr);
                onItemClickListener.getLatLng(data.get(position).pt);
                onItemClickListener.success(true);
            }
    }
    public interface SugPopItemClickListener{
        public void getAddr(String addr);
        public void getLatLng(LatLng pt);
        public void success(boolean isOk);
    }
}
