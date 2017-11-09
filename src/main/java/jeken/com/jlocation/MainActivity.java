package jeken.com.jlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import jeken.com.jlocation.activity.BaiduMapActivity;
import jeken.com.jlocation.navi.NavInitManager;


public class MainActivity extends AppCompatActivity  {

    SuggestionSearch suggestionSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, BaiduMapActivity.class));


        NavInitManager.getInstance().initAll(this);

        SDKInitializer.initialize(getApplicationContext());

    }

    public void clickpwn(View v){
        //DetailSearchInfo
//           BNRoutePlanNode sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436,
//                    "百度大厦", null, BNRoutePlanNode.CoordinateType.BD09LL);
//            BNRoutePlanNode eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085,
//                    "北京天安门", null, BNRoutePlanNode.CoordinateType.BD09LL);
//            NavInitManager.getInstance().startDrivingNavi(this, sNode,eNode, BNDemoGuideActivity.class);
        suggestionSearch.requestSuggestion((new SuggestionSearchOption()).city("上海").keyword("同济大学"));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
//                    Toast.makeText(BNDemoMainActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
//            initNavi();
        } else if (requestCode == 2) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
//            BNRoutePlanNode sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436,
//                    "百度大厦", null, BNRoutePlanNode.CoordinateType.BD09LL);
//            BNRoutePlanNode eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085,
//                    "北京天安门", null, BNRoutePlanNode.CoordinateType.BD09LL);
//            NavInitManager.getInstance().startDrivingNavi(this, sNode,eNode, BNDemoGuideActivity.class);
        }

    }


}
