package com.x_force.unimar.Item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.x_force.unimar.R;
import com.x_force.unimar.Views.ProductView;

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
        TextView descTextView = convertView.findViewById(R.id.item_desc);

        nameTextView.setText(item.getName());
        costTextView.setText(String.valueOf(item.getCost()));
        descTextView.setText(item.getDesc());

        TextView sortButton = convertView.findViewById(R.id.sort_button);
        TextView filterButton = convertView.findViewById(R.id.filter_button);
        SearchView searchBar = convertView.findViewById(R.id.product_searchbar);

        searchBar.setVisibility(View.GONE);
        sortButton.setVisibility(View.GONE);
        filterButton.setVisibility(View.GONE);

        convertView.setOnClickListener(v -> {
            String categories = "";
            for (int i = 0; i < item.getCategory().size(); i++) {
                categories += item.getCategory().get(i) + ",";
            }
            categories = categories.substring(0, categories.length() - 1);
            Intent intent = new Intent(getContext(), ProductView.class);
            intent.putExtra("Name", item.getName());
            intent.putExtra("Category", categories);
            intent.putExtra("Description", item.getDesc());
            intent.putExtra("Cost", item.getCost()+"");
            getContext().startActivity(intent);
        });

        return convertView;
    }
}
