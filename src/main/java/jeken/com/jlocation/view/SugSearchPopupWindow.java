package jeken.com.jlocation.view;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import jeken.com.jlocation.R;

/**
 * Created by jeken on 2017/10/24.
 */

public class SugSearchPopupWindow extends CommPopupWindow {

    private ListView listView;

    public SugSearchPopupWindow(Context mContext, int mLayoutRes, int w, int h) {
        super(mContext, mLayoutRes, w, h);
    }

    @Override
    public void initView(View view) {
        if (view == null) return;
        listView = (ListView) view.findViewById(R.id.sugpop_lv);
    }

    @Override
    public void initWindow(PopupWindow mPopupWindow) {

    }
}
