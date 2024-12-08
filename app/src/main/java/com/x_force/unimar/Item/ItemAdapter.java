package com.x_force.unimar.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.x_force.unimar.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(Context context, List<Item> items){
        super(context,0,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_tutor, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.item_nameview);
        TextView costTextView = convertView.findViewById(R.id.item_costview);

        nameTextView.setText(item.getName());
        costTextView.setText(String.valueOf(item.getCost()));

        return convertView;
    }
}
