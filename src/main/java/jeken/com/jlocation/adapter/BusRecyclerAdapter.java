package jeken.com.jlocation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.route.TransitRouteLine;

import java.util.List;

import jeken.com.jlocation.R;

/**
 * Created by jeken on 2017/11/27.
 */

public class BusRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TransitRouteLine> data;

    public BusRecyclerAdapter(Context mContext,List<TransitRouteLine> data){
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.busrecycview_item,parent,false);

        return new BusViewHold(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TransitRouteLine itemInfo = data.get(position);
        String direction ="";
        for (TransitRouteLine.TransitStep item:itemInfo.getAllStep()){
            direction += item.getInstructions()+"-->";
        }
        ((BusViewHold)holder).tv_title.setText(itemInfo.getTitle()+"\r\n"+direction);
        ((BusViewHold)holder).tv_time.setText("距离："+itemInfo.getDistance());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class BusViewHold extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private TextView tv_time;
        public BusViewHold(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.busrecyc_tv_title);
            tv_time = itemView.findViewById(R.id.busrecyc_tv_time);
        }
    }

}
