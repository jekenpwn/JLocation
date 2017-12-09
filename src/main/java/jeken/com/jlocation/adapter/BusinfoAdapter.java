package jeken.com.jlocation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import java.util.List;
import jeken.com.jlocation.R;

/**
 * Created by jeken on 2017/11/28.
 */

public class BusinfoAdapter extends JBaseAdapter<TransitRouteLine> {
    private Context mContext;
    private List<TransitRouteLine> data;

    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    public BusinfoAdapter(Context mContext, List<TransitRouteLine> data) {
        super(mContext, data);
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public Object setConverView() {
        return R.layout.busrecycview_item;
    }

    @Override
    public void setViewHold(View convertView, int position) {
        TransitRouteLine itemInfo = data.get(position);
        String direction ="";
        for (TransitRouteLine.TransitStep item:itemInfo.getAllStep()){
            if (direction.equals("")){
                direction += item.getInstructions();
            }else {
                direction += "➔"+item.getInstructions();
            }

        }

        BusinfoViewHolder holder = (BusinfoViewHolder) convertView.getTag();
        if (holder == null){
            holder = new BusinfoViewHolder();
            holder.tv_title = convertView.findViewById(R.id.busrecyc_tv_title);
            holder.tv_time = convertView.findViewById(R.id.busrecyc_tv_time);
            convertView.setTag(holder);
        }
        holder.tv_title.setText(direction);
        holder.tv_time.setText("距离："+itemInfo.getDistance());
    }

    static class BusinfoViewHolder{
        TextView tv_title;
        TextView tv_time;
    }
}
