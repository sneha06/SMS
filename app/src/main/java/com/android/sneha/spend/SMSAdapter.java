package com.android.sneha.spend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sneha on 12/3/15.
 */
public class SMSAdapter extends ArrayAdapter<String> {

    protected Context mContext;
    protected List<String> sAddress,sSmsbody;


    public SMSAdapter(Context context, List<String> value1,List<String> value2) {
        super(context, R.layout.smslist,value1);
        mContext = context;
        sAddress = value1;
        sSmsbody = value2;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.smslist, null);
            holder = new ViewHolder();
            holder.text1 = (TextView)convertView.findViewById(R.id.addrtext);
            holder.text2 = (TextView)convertView.findViewById(R.id.smsbodytext);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        holder.text1.setText(sAddress.get(position));

        holder.text2.setText(sSmsbody.get(position));
        return convertView;
    }

    private static class ViewHolder{
        ImageView simage;


        TextView text1;
        TextView text2;

    }
}
