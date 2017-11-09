package jeken.com.jlocation.tools;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jeken on 2017/10/30.
 */

public class ActionBarTools  {
    public static byte HINT_TITLE = 0x01;
    public static byte HINT_ICON = 0x02;
    public static void hintXX(AppCompatActivity act, byte HINT_XX){
        ActionBar actionBar = act.getSupportActionBar();
        if ((HINT_XX & HINT_TITLE)>0){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        if((HINT_XX & HINT_ICON)>0){
            actionBar.setDisplayUseLogoEnabled(false);
        }
    }
}
