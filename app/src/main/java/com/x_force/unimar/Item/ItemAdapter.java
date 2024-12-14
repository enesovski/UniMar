package com.x_force.unimar.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.x_force.unimar.R;

import java.util.List;

//Listede gözükecek her bir item için nasıl görüneceğini
public class ItemAdapter extends ArrayAdapter<Item> {

    List<Item> items;

    public ItemAdapter(Context context, List<Item> items){
        super(context,0,items);
        this.items = items;
    }

    @NonNull
    @Override
    // Listedeki her item için ui'a göre bir element oluşturuyoruz.
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = items.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_productlisting, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView costTextView = convertView.findViewById(R.id.item_cost);

        nameTextView.setText(item.getName());
        costTextView.setText(String.valueOf(item.getCost()));

        TextView sortButton = convertView.findViewById(R.id.sort_button);
        TextView filterButton = convertView.findViewById(R.id.filter_button);

        sortButton.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);

        return convertView;
    }
}
