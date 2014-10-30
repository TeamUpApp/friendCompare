package com.example.clazell.bestfriends;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class MessageListAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> list;
    private final Activity context;
    private LayoutInflater inflater;

    public MessageListAdapter(Activity context, List<Contact> list) {
        super(context, R.layout.list_row, list);
        this.context = context;
        this.list = list;
    }


    static class ViewHolder {

        protected TextView ranking;
        protected TextView name;
        protected TextView message;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.name = (TextView) convertView.findViewById(R.id.txt_contact_name);
        viewHolder.message = (TextView) convertView.findViewById(R.id.txt_message);
        viewHolder.ranking = (TextView) convertView.findViewById(R.id.txt_rank);


        //final Word sms = list.get(position);

        viewHolder.name.setText(list.get(position).getName());
        viewHolder.message.setText("" + list.get(position).getPercent() + "% match");
        viewHolder.ranking.setText("" + (position + 1));

        return convertView;
    }
}