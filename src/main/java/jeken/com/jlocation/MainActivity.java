package jeken.com.jlocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.List;

import jeken.com.jlocation.navi.NavInitManager;


public class MainActivity extends AppCompatActivity implements OnGetSuggestionResultListener {

    SuggestionSearch suggestionSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, BaiduMapActivity.class));
//        startService(new Intent(this,CoreSerivce.class));

        NavInitManager.getInstance().initAll(this);

        SDKInitializer.initialize(getApplicationContext());

        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        suggestionSearch.requestSuggestion((new SuggestionSearchOption()).city("上海").keyword("同济大学"));

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

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {

            //未找到相关结果
            Log.e("TAG","未找到相关结果");
            return;
        }else
        {
            List<SuggestionResult.SuggestionInfo> resl=suggestionResult.getAllSuggestions();
            for(int i=0;i<resl.size();i++)
            {
                Log.e("result: ","city"+resl.get(i).city+" dis "+resl.get(i).district+"key "+resl.get(i).key);
            }
        }
    }
}
