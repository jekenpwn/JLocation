package jeken.com.jlocation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.route.BikingRouteLine;

import java.util.List;

import jeken.com.jlocation.R;

/**
 * Created by jeken on 2017/12/9.
 */

public class BikeinfoAdapter extends JBaseAdapter<BikingRouteLine> {
    private Context mContext;
    private List<BikingRouteLine> data;
    public BikeinfoAdapter(Context mContext, List<BikingRouteLine> data) {
        super(mContext, data);
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public Object setConverView() {
        return null;
    }

    @Override
    public void setViewHold(View convertView, int position) {

        BikingRouteLine itemInfo = data.get(position);
        String direction ="";
        for (BikingRouteLine.BikingStep item:itemInfo.getAllStep()){
            direction += item.getInstructions()+"-->";
        }
        BikeinfoViewHolder holder = (BikeinfoViewHolder) convertView.getTag();
        if (holder == null){
            holder = new BikeinfoViewHolder();
            holder.tv_title = convertView.findViewById(R.id.busrecyc_tv_title);
            holder.tv_time = convertView.findViewById(R.id.busrecyc_tv_time);
            convertView.setTag(holder);
        }
        //holder.tv_title.setText(itemInfo.getTitle()+"\r\n"+direction);
        holder.tv_time.setText("距离："+itemInfo.getDistance());
    }
    static class BikeinfoViewHolder{
        TextView tv_title;
        TextView tv_time;
    }
}
