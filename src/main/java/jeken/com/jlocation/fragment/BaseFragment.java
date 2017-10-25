package jeken.com.jlocation.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017-09-25.
 */

public abstract class BaseFragment extends Fragment {

    private View view = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getContentView() == null) return null;
        if (getContentView() instanceof Integer) {
            view  = inflater.inflate((Integer) getContentView(),null);
        }else if (getContentView() instanceof View){
            view = (View) getContentView();
        }
        x.view().inject(view);
        initView();
        return view;
    }

    public abstract Object getContentView();
    public abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(view != null) {
            view = null;
        }
    }
}
