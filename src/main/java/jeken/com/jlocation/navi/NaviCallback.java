package jeken.com.jlocation.navi;

/**
 * Created by jeken on 2017/10/19.
 */

public interface NaviCallback {

    public static int STARTSUCCESS  = 1;
    public static int STARTERROR  = 0;

    public void initSuccess();
    public void startResult(int RES);
    public void message(String message);
}
