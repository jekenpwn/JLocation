package jeken.com.jlocation.activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017-09-16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* define view by yourself. */
        beforeSetContentView(this);
        if (getContentView(this) instanceof Integer){
            setContentView((Integer) getContentView(this));
        }else if (getContentView(this) instanceof View){
            setContentView((View) getContentView(this));
        }else if (getContentView(this) instanceof FrameLayout){
            setContentView((FrameLayout)getContentView(this));
        }else{
            setContentView(new View(this));
        }
        initView(this);
        x.view().inject(this);
    }
    public abstract Object getContentView(Context context);
    public abstract void initView(Context context);
    public abstract void beforeSetContentView(Context context);
}
