package jeken.com.jlocation.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by jeken on 2017/10/24.
 */

public abstract  class CommPopupWindow {
    private View view;
    private PopupWindow mPopupWindow;
    private Context mContext;

    public CommPopupWindow(Context mContext,int mLayoutRes,int w,int h){
        view = LayoutInflater.from(mContext).inflate(mLayoutRes,null,false);
        if (view != null){
            initView(view);
            mPopupWindow = new PopupWindow(view,w,h);
            if (mPopupWindow != null){
                initWindow(mPopupWindow);
            }
        }
    }

    public abstract void initView(View view);
    public abstract void initWindow(PopupWindow mPopupWindow);
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }
    public void dismiss(){
        mPopupWindow.dismiss();
    }
    public boolean show(){
        return mPopupWindow.isShowing();
    }
}
