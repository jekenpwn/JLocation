package jeken.com.jlocation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        BNRoutePlanNode sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436,
//                "百度大厦", null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085,
//                "北京天安门", null, BNRoutePlanNode.CoordinateType.BD09LL);
//        NavInitManager.getInstance().startDrivingNavi(this, sNode,eNode, BNDemoGuideActivity.class);

    }
//    public void clickjeken(View v){
//        BNRoutePlanNode sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436,
//                "百度大厦", null, BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085,
//                "北京天安门", null, BNRoutePlanNode.CoordinateType.BD09LL);
//        NavInitManager.getInstance().startDrivingNavi(this, sNode,eNode, BNDemoGuideActivity.class);
//    }

}
